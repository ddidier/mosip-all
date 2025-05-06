import {
  Component,
  OnInit,
  Input,
  OnChanges,
  Output,
  EventEmitter
} from '@angular/core';
import { Router } from '@angular/router';
import { SortModel } from 'src/app/core/models/sort.model';
import { AppConfigService } from 'src/app/app-config.service';
import * as appConstants from 'src/app/app.constants';
import { CommonService } from 'src/app/core/services/common.service';
import { TranslateService } from '@ngx-translate/core';
import { AuditService } from 'src/app/core/services/audit.service';
import { HeaderService } from 'src/app/core/services/header.service';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})
export class TableComponent implements OnInit, OnChanges {
  @Input() data: [];
  @Input() displayedColumns: [];
  @Input() buttonList: [];
  @Input() sortData: SortModel[];
  @Input() noDataFlag: boolean;
  @Output() sort = new EventEmitter();
  tableData = [];
  columnsOfTableData = [];
  linkColumnsOfTableData =[];
  sortStatusArray: string[] = [];
  currentRoute: string;
  lang: string;
  sortTrackIndex: number;
  sortIconTrackerArray = new Array(15).fill(0);
  ellipsisList = [];
  imageSource: string;
  partnerType: string;
  showapilistbutton:boolean = false;

  constructor(
    private headerService: HeaderService,
    private router: Router,
    private appConfig: AppConfigService,
    private commonService: CommonService,
    private translate: TranslateService,
    private auditService: AuditService
  ) {
   translate.use(this.headerService.getlanguageCode());
  }
  ngOnInit(): void {
    this.tableData = [...this.data];
    this.sortStatusArray = [];
    this.partnerType = this.headerService.getRoles();
    if(this.partnerType.includes("AUTH PARTNER") || this.partnerType.includes("CREDENTIAL PARTNER")){
      this.showapilistbutton = true;
    }
    this.lang = this.headerService.getlanguageCode();
    const route = this.router.url.split('/')[3];
    this.imageSource = appConstants.ListViewIdKeyMapping[`${route}`]['imagePath'];
  }

  ngOnChanges(): void {
    this.tableData = [...this.data];
    this.columnsOfTableData = [];
    this.linkColumnsOfTableData = [];
    this.displayedColumns.forEach(column => {
      this.columnsOfTableData.push(column['name']);
    });

    this.displayedColumns.forEach(column => {
      if(column['showAsLink'] == "true"){
        this.linkColumnsOfTableData.push(column['name'])
      }
    });
    this.currentRoute = this.router.url.split('/')[3];
    this.setSortDirection();
  }

  setSortDirection() {
    if (this.sortData) {
      this.sortData.forEach((data: SortModel) => {
        if (this.sortStatusArray.indexOf(data.sortField) === -1) {
          this.sortStatusArray.push(data.sortField);
        }
        const index = this.columnsOfTableData.indexOf(data.sortField);
        if (data.sortType.toLowerCase() === 'asc') {
          this.sortIconTrackerArray[index] = 1;
        } else if (data.sortType.toLowerCase() === 'desc') {
          this.sortIconTrackerArray[index] = -1;
        }
      });
    }
  }

  selectedRow(data: any, specData: any) {
    const currentRouteType = this.router.url.split('/')[3];
    let id = appConstants.ListViewIdKeyMapping[`${currentRouteType}`];
    if(!id)
      id =  { idKey: 'id', auditEventId: 'ADM-069' };
    if (specData.callBackFunction && specData.callBackFunction !== '') {
      this.commonService[specData.callBackFunction]({...data}, specData.redirectURL, id.idKey);
    }
  }

  getTableRowData(data: any, index: number, columnName: string) {
    const routeIndex = this.router.url.lastIndexOf('/');
    this.currentRoute = this.router.url.slice(0, routeIndex);    
    const currentRouteType = this.router.url.split('/')[3];
    
    const id = appConstants.ListViewIdKeyMapping[`${currentRouteType}`];
    this.auditService.audit(7, id.auditEventId, currentRouteType);
    if (index === 0) {
      /*this.router.navigate([
        `${this.currentRoute}/single-view`,
        data[id.idKey]
      ]);*/
    }
  }
  sortColumn(columnName: string, columnIndex: number) {
    this.auditService.audit(13, 'ADM-093', {
      masterdataName: this.router.url.split('/')[3],
      columnName
    });
    const sortObject = this.sortData.filter(
      data => data.sortField === columnName
    );
    let sortModel = new SortModel();
    if (sortObject.length === 0) {
      sortModel.sortField = columnName;
      sortModel.sortType = 'asc';
      this.sortIconTrackerArray[columnIndex] = 1;
    } else {
      sortModel = sortObject[0];
      if (sortModel.sortType.toLowerCase() === 'asc') {
        sortModel.sortType = 'desc';
        this.sortIconTrackerArray[columnIndex] = -1;
      } else if (sortModel.sortType.toLowerCase() === 'desc') {
        sortModel.sortType = null;
        this.sortIconTrackerArray[columnIndex] = 0;
      }
    }
    this.sort.emit(sortModel);
  }

  ellipsisAction(data) {
    let self = this;
    this.buttonList.forEach(function (obj, i) {
      if(!self.partnerType.includes("PARTNER ADMIN")){
        if(obj["callBackFunction"] === "approve"){
          self.buttonList.splice(i, 1);
        }
      }
    });
    this.buttonList.forEach(function (obj, i) {
      if(!self.partnerType.includes("PARTNER ADMIN")){
        if(obj["callBackFunction"] === "reject"){
          self.buttonList.splice(i, 1);
        }
      }
    });
    this.ellipsisList = [...this.buttonList];
    if (data.isActive === true || data.active === true) {
      this.ellipsisList.filter(values => {
        if (values.buttonName.eng === 'Activate') {
          const index = this.ellipsisList.indexOf(values);
          this.ellipsisList.splice(index, 1);
        }
      });
    } else if (data.isActive === false || data.active === false) {
      this.ellipsisList.filter(values => {
        if (values.buttonName.eng === 'Deactivate') {
          const index = this.ellipsisList.indexOf(values);
          this.ellipsisList.splice(index, 1);
        }
      });
    }else if(data.statusCode === "approved" || data.statusCode === 'rejected' || data.statusCode === 'InProgress'){      
      this.ellipsisList.forEach(values => {      
        if(!self.partnerType.includes("PARTNER ADMIN")){
          if(self.partnerType.includes("AUTH PARTNER") && data.statusCode === "approved"){
            if (values.buttonName.eng === 'Edit') {
              const index = this.ellipsisList.indexOf(values);
              this.ellipsisList.splice(index, 1);
            }
          }else if(self.partnerType.includes("CREDENTIAL PARTNER") && data.statusCode === "approved"){
            if (values.buttonName.eng === 'Edit') {
              const index = this.ellipsisList.indexOf(values);
              this.ellipsisList.splice(index, 1);
            }
          }else if(self.partnerType.includes("AUTH PARTNER") && data.statusCode === "InProgress"){
            if (values.buttonName.eng === 'Generate API Key') {
              const index = this.ellipsisList.indexOf(values);
              this.ellipsisList.splice(index, 1);
            }
          }else if(self.partnerType.includes("CREDENTIAL PARTNER") && data.statusCode === "InProgress"){
            if (values.buttonName.eng === 'Generate API Key') {
              const index = this.ellipsisList.indexOf(values);
              this.ellipsisList.splice(index, 1);
            }
          }
          if (values.buttonName.eng === 'Manage Policy') {
            const index = this.ellipsisList.indexOf(values);
            this.ellipsisList.splice(index, 1);
          }         
        }else if(self.partnerType.includes("PARTNER ADMIN")){
          if(data.statusCode === "approved"){
            if (values.buttonName.eng === 'Manage Policy') {
              const index = self.ellipsisList.indexOf(values);
              self.ellipsisList = self.ellipsisList.splice(index, 1);
            }
          }else if(data.statusCode === "InProgress"){
            if (values.buttonName.eng === 'Edit') {
              const index = self.ellipsisList.indexOf(values);
              self.ellipsisList = self.ellipsisList.splice(index, 1);
            }
            if (values.buttonName.eng === 'Manage Policy') {
              const index = self.ellipsisList.indexOf(values);
              self.ellipsisList = self.ellipsisList.splice(index, 1);
            }
          }          
        }
      });
    }
    if (data.approvalStatus === 'approved') {
      this.ellipsisList.filter(values => {
        if (values.buttonName.eng === 'Approve') {
          const index = this.ellipsisList.indexOf(values);
          this.ellipsisList.splice(index, 1);
        }
      });
    }else if (data.approvalStatus === 'rejected') {
      this.ellipsisList.filter(values => {
        if (values.buttonName.eng === 'Reject') {
          const index = this.ellipsisList.indexOf(values);
          this.ellipsisList.splice(index, 1);
        }
      });
    } 
  }

  tableStyle(index, columnValue, columnName) {
    const myTableStyles = {
      color: '#0F2126',
      cursor: '',
      border: '',
      padding: '',
      borderRadius: '',
      backgroundColor: '',
      whiteSpace: 'nowrap',
      textTransform: ''
    };
    if (index === 0) {
      myTableStyles.color = '#0F2126';
      return myTableStyles;
    }
    if(this.linkColumnsOfTableData.find(x=>x===columnName)){
      myTableStyles.color = '#0F2126';
      return myTableStyles;
  // color: white;
  // padding: 14px 25px;
  // text-align: center; 
  // text-decoration: none;
  // display: inline-block;
    }
    /*if (columnValue === true && (columnName === 'isActive' || columnName === 'active')) {
      myTableStyles.backgroundColor = '#C2F2DA';
      myTableStyles.padding = '5px';
      myTableStyles.border = '1px solid #4AD991';
      myTableStyles.borderRadius = '7px';
      return myTableStyles;
    } else if (columnValue === false && (columnName === 'isActive' || columnName === 'active')) {
      myTableStyles.backgroundColor = '#CECFD0';
      myTableStyles.padding = '5px';
      myTableStyles.border = '1px solid #9C9F9F';
      myTableStyles.borderRadius = '7px';
      return myTableStyles;
    }
    if (columnValue === true && columnName === 'licensKeyStatus') {
      myTableStyles.backgroundColor = '#C2F2DA';
      myTableStyles.padding = '5px';
      myTableStyles.border = '1px solid #4AD991';
      myTableStyles.borderRadius = '7px';
      return myTableStyles;
    } else if (columnValue === false && columnName === 'licensKeyStatus') {
      myTableStyles.backgroundColor = '#CECFD0';
      myTableStyles.padding = '5px';
      myTableStyles.border = '1px solid #9C9F9F';
      myTableStyles.borderRadius = '7px';
      return myTableStyles;
    }*/
  }
}
