import { Component, OnInit, OnDestroy, ViewChild ,ElementRef, ViewChildren, HostListener} from "@angular/core";
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { TranslateService } from "@ngx-translate/core";
import { Subscription } from "rxjs";
import { Router } from "@angular/router";
import { AppConfigService } from 'src/app/app-config.service';
import { DialogComponent } from 'src/app/shared/dialog/dialog.component';
import { MatDialog, MatPaginatorIntl } from '@angular/material';
import { saveAs } from 'file-saver';
import { HeaderService } from 'src/app/core/services/header.service';
import { AuditService } from "src/app/core/services/audit.service";
import { AutoLogoutService } from "src/app/core/services/auto-logout.service";
import { MatPaginator } from '@angular/material/paginator';
import { BreakpointService } from "src/app/core/services/breakpoint.service";
import {
  MatKeyboardRef,
  MatKeyboardComponent,
  MatKeyboardService
} from 'ngx7-material-keyboard-ios';
import defaultJson from "src/assets/i18n/default.json";
import { FontSizeService } from "src/app/core/services/font-size.service";

@Component({
  selector: "app-viewhistory",
  templateUrl: "viewhistory.component.html",
  styleUrls: ["viewhistory.component.css"],
})
export class ViewhistoryComponent implements OnInit, OnDestroy {
  langJSON:any;
  popupMessages:any;
  subscriptions: Subscription[] = [];
  responselist: any;
  totalItems = 0;
  defaultPageSize = 10;
  pageSize = this.defaultPageSize;
  pageIndex = 0;
  pageSizeOptions: number[] = [5, 10, 15, 20];
  serviceTypeFilter:any;
  statusTypeFilter:any;
  showFirstLastButtons:boolean = true;
  cols:number;
  today: Date = new Date();
  presentYear: any = new Date().getFullYear();
  startdate: Date = new Date(this.presentYear, 0, 1);
  selectedDate:any = new Date();
  toDateStartDate:any = this.startdate;
  searchText:string = "";
  serviceType:string = "";
  statusFilter:string = "";
  fromDate:string = this.startdate.getFullYear() + "-" + ("0" + (this.startdate.getMonth() + 1)).slice(-2) + "-" + ("0" + this.startdate.getDate()).slice(-2);
  toDate:string = this.today.getFullYear() + "-" + ("0" + (this.today.getMonth() + 1)).slice(-2) + "-" + ("0" + this.today.getDate()).slice(-2);
  controlTypes = ["searchText", "serviceType", "statusFilter", "fromDate", "toDate"];
  datas:{};
  isStatusAllValue:boolean = false;
  statusSelectedValue:string;
  isHistoryAllValue:boolean = false;
  historySelectedValue:string;
  @ViewChild('statusFilterSelectAll') statusFilterSelectAll: any;
  @ViewChild('serviceTypeSelectAll') serviceTypeSelectAll: any;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  searchParam:string;
  message2:any;
  langCode = localStorage.getItem("langCode");
  serviceHistorySelectedValue: string;
  statusHistorySelectedValue: string;
  isLoading:boolean = true;
  dataAvailable:boolean = false;
  sitealignment:string = localStorage.getItem('direction');
  disableDownloadBtn:boolean = false;
  

  private keyboardRef: MatKeyboardRef<MatKeyboardComponent>;
  @ViewChildren('keyboardRef', { read: ElementRef })
  private attachToElementMesOne: any;
  constructor(private autoLogout: AutoLogoutService,private dialog: MatDialog, private appConfigService: AppConfigService, private dataStorageService: DataStorageService, 
    private translateService: TranslateService, private router: Router, 
    public headerService: HeaderService,private auditService: AuditService, 
    private breakPointService: BreakpointService,
    private paginator2: MatPaginatorIntl,
    private keyboardService: MatKeyboardService,
    private fontSizeService: FontSizeService
    ) {
    this.breakPointService.isBreakpointActive().subscribe(active =>{
      if (active) {
        if(active === "extraSmall"){
          this.cols = 1;
        }
        if(active === "medium"){
          this.cols = 3;
        }
        if(active === "large"){
          this.cols = 4;
        }
        if(active === "ExtraLarge"){
          this.cols = 6;
        }
        if(active === "small"){
          this.cols = 2;
        }
      }
    });
  }

  async ngOnInit() {
    this.translateService.use(localStorage.getItem("langCode"));
    
    this.getLangJsonData();
    this.captureValue("","ALL","", "")


    const subs = this.autoLogout.currentMessageAutoLogout.subscribe(
      (message) => (this.message2 = message) //message =  {"timerFired":false}
    );

    this.subscriptions.push(subs);

    if (!this.message2["timerFired"]) {
      this.autoLogout.getValues(this.langCode);
      this.autoLogout.setValues();
      this.autoLogout.keepWatching();
    } else {
      this.autoLogout.getValues(this.langCode);
      this.autoLogout.continueWatching();
    }
  }

  async getLangJsonData(){
    this.translateService
    .getTranslation(localStorage.getItem("langCode"))
    .subscribe(response => {
      this.langJSON = response;
      this.popupMessages = response;
      this.serviceHistorySelectedValue = response.viewhistory.historyType;
      this.statusHistorySelectedValue = response.viewhistory.status;
      this.paginator2.itemsPerPageLabel = response['paginatorIntl'].itemsPerPageLabel;
      const originalGetRangeLabel = this.paginator2.getRangeLabel;
      this.paginator2.getRangeLabel = (page: number, size: number, len: number) => {
        return originalGetRangeLabel(page, size, len)
            .replace('of', response['paginatorIntl'].of);
    };
    this.getServiceHistory("","","");
    });

  }

  getServiceHistory(pageEvent:any, filters:any, actionTriggered:string){
    this.dataStorageService
      .getServiceHistory(pageEvent, filters,this.pageSize)
      .subscribe((response) => {
        if(response["response"]){
          this.responselist = response["response"]["data"];
          this.totalItems = response["response"]["totalItems"];
          this.serviceTypeFilter = this.appConfigService.getConfig()["resident.view.history.serviceType.filters"].split(',');
          this.statusTypeFilter = this.appConfigService.getConfig()["resident.view.history.status.filters"].split(',');
          this.pageSize = response["response"]['pageSize']
          this.parsedrodowndata();
          if (this.responselist.length) {
            this.dataAvailable = false;
          } else {
            this.dataAvailable = true;
          }
          this.isLoading = false;
        } else {
          this.isLoading = false;
          this.showErrorMessagePopup(response["errors"])
        }
      });
  }

  parsedrodowndata() {
    let serviceTypeFilter = this.serviceTypeFilter;
    this.serviceTypeFilter = [];
    let statusTypeFilter = this.statusTypeFilter;
    this.statusTypeFilter = [];
   
    serviceTypeFilter.forEach((element) => {
      if (this.langJSON.viewhistory.serviceTypeFilter[element]) {
        this.serviceTypeFilter.push({ "label": this.langJSON.viewhistory.serviceTypeFilter[element], "value": element });
      }
    });
    statusTypeFilter.forEach((element) => {
      if (this.langJSON.viewhistory.statusTypeFilter[element]) {
        this.statusTypeFilter.push({ "label": this.langJSON.viewhistory.statusTypeFilter[element], "value": element });
      }
    });
  }

  tosslePerOne(isStatusAllValue:boolean, formControlName:string){
    if (isStatusAllValue) {
      this[formControlName] = 'ALL';
      this.statusHistorySelectedValue = this.langJSON.viewhistory.selectAll;
      this.statusTypeFilter = this.statusTypeFilter.map(eachServiceType => {
        eachServiceType.label.checked = true;
        return eachServiceType
      })
    } else {
      this[formControlName] = "";
      this.statusHistorySelectedValue = this.langJSON.viewhistory.status;
      this.statusTypeFilter = this.statusTypeFilter.map(eachServiceType => {
        eachServiceType.label.checked = false;
        return eachServiceType
      })
    }
  }

  selectingStatusOneValue(event:string,formControlName:string){
    let count = 0;
    this.statusTypeFilter.forEach(eachServiceType => {
      if (eachServiceType.value === "ALL") {
        eachServiceType.label.checked = false;
      } else if (eachServiceType.value === event) {
        eachServiceType.label.checked = !eachServiceType.label.checked;
      }

      if(eachServiceType.label.checked){
        count += 1;
        this[formControlName] += eachServiceType.value + ",";
        this.statusHistorySelectedValue += eachServiceType.label.statusType + ","
      }
    
    });

    if(!this.statusHistorySelectedValue){
      this.statusHistorySelectedValue = this.langJSON.viewhistory.status;
    }else{
      this.statusHistorySelectedValue = this.statusHistorySelectedValue.replace(/,\s*$/, "");
    }
    
    if(count === (this.statusTypeFilter.length - 1)){
      this.isStatusAllValue = !this.isStatusAllValue;
      this.tosslePerOne(this.isStatusAllValue, formControlName);
    }

  }

  historyTosslePerOne(isHistoryAllValue: boolean, formControlName: string) {
    if (isHistoryAllValue) {
      this[formControlName] = 'ALL';
      this.serviceHistorySelectedValue = this.langJSON.viewhistory.selectAll;
      this.serviceTypeFilter = this.serviceTypeFilter.map(eachServiceType => {
        eachServiceType.label.checked = true;
        return eachServiceType
      })
    } else {
      this[formControlName] = "";
      this.serviceHistorySelectedValue = this.langJSON.viewhistory.historyType;
      this.serviceTypeFilter = this.serviceTypeFilter.map(eachServiceType => {
        eachServiceType.label.checked = false;
        return eachServiceType
      })
    }
  }

  selectingHistoryOneValues(event:string,formControlName:string){
    let count = 0;
    this.serviceTypeFilter.forEach(eachServiceType => {
      if (eachServiceType.value === "ALL") {
        eachServiceType.label.checked = false;
      } else if (eachServiceType.value === event) {
        eachServiceType.label.checked = !eachServiceType.label.checked;
      }

      if(eachServiceType.label.checked){
        count += 1
        this[formControlName] += eachServiceType.value + ",";
        this.serviceHistorySelectedValue += eachServiceType.label.serviceType + ","
      }
    });
  
    if(!this.serviceHistorySelectedValue){
      this.serviceHistorySelectedValue = this.langJSON.viewhistory.historyType;
    }else if(this.serviceHistorySelectedValue.length > 26){
      this.serviceHistorySelectedValue = this.serviceHistorySelectedValue.substring(0,36) + "...";
    }else{
      this.serviceHistorySelectedValue = this.serviceHistorySelectedValue.replace(/,\s*$/, "");
    }

    if(count ===  (this.serviceTypeFilter.length - 1)){
      this.isHistoryAllValue = !this.isHistoryAllValue;
      this.historyTosslePerOne(this.isHistoryAllValue, formControlName);
    }
  }

  captureValue(event: any,selectedValue:any, formControlName: string, controlType: string) {
    if(event !== "")this.disableDownloadBtn = true;
    this.selectedDate = this.today;
    if (controlType === "dropdown") {
      if (selectedValue === "ALL") {
        if (formControlName === "serviceType") {
          this.isHistoryAllValue = !this.isHistoryAllValue;
          this.historyTosslePerOne(this.isHistoryAllValue, formControlName);
        } else {
          this.isStatusAllValue = !this.isStatusAllValue;
          this.tosslePerOne(this.isStatusAllValue,formControlName);
        }
      } else {
        if (formControlName === "serviceType") {
          this.serviceType = "";
          this.isHistoryAllValue = false;
          this.serviceHistorySelectedValue = "";
          this.selectingHistoryOneValues(selectedValue,formControlName)
        } else {
          this.statusFilter = "";
          this.isStatusAllValue = false;
          this.statusHistorySelectedValue = "";
          this.selectingStatusOneValue(selectedValue,formControlName)
        }
      }
    } else if (controlType === "datepicker") {
      let dateFormat = new Date(event.target.value);
      formControlName === "fromDate" ? this.toDateStartDate = dateFormat : "";
      let formattedDate = dateFormat.getFullYear() + "-" + ("0" + (dateFormat.getMonth() + 1)).slice(-2) + "-" + ("0" + dateFormat.getDate()).slice(-2);
      this[formControlName] = formattedDate;

    }else{
      if(event.target){
      this.auditService.audit('RP-003', 'View history', 'RP-View history', 'View history', 'User clicks on search button for searching "EventId"', '');
      this[formControlName] = event.target.value;
      }
    }
    if(formControlName === "serviceType"){
      this.auditService.audit('RP-009', 'View history', 'RP-View history', 'View history', 'User chooses the "history filter" from the drop-down', '');
    }else if(formControlName === "statusFilter"){
      this.auditService.audit('RP-010', 'View history', 'RP-View history', 'View history', 'User chooses the "status filter" from the drop-down', '');
    }
    if(event){
      event.stopPropagation()
    }
  }

  pinData(data:any){
    this.auditService.audit('RP-006', 'View history', 'RP-View history', 'View history', 'User clicks on "Pin to top"', '');
    this.dataStorageService
      .pinData(data.eventId)
      .subscribe((response) => {
        this.getServiceHistory("","","");
    });
    this.paginator.pageIndex = 0;
  }

  unpinData(data:any){
    this.dataStorageService
      .unpinData(data.eventId)
      .subscribe((response) => {
        this.getServiceHistory("", "", "");
      });
      this.paginator.pageIndex = 0;
  }

  viewDetails(data: any) {
    this.auditService.audit('RP-008', 'View history', 'RP-View history', 'View history', 'User clicks on "View Details"', '');
    this.router.navigateByUrl(`uinservices/trackservicerequest?source=ViewMyHistory&eid=` + data.eventId);
  }

  reportDetails(data: any) {
    this.auditService.audit('RP-007', 'View history', 'RP-View history', 'View history', 'User clicks on "Report an issue"', '');
    this.router.navigateByUrl(`uinservices/grievanceRedressal?source1=viewMyHistory&eid=` + data.eventId);
  }

  search() {
    this.disableDownloadBtn = false;
    let searchParam = "",
      self = this;
    this.controlTypes.forEach(controlType => {
      if (self[controlType]) {
        if (searchParam) {
          searchParam = searchParam + "&" + controlType + "=" + self[controlType];
        } else {
          searchParam = controlType + "=" + self[controlType];
        }
      }
    });
    this.getServiceHistory("", searchParam, "search");
    this.auditService.audit('RP-004', 'View history', 'RP-View history', 'View history', 'User clicks on "Go" button for applying "the chosen filter"', '');
    this.paginator.pageIndex = 0;
  }

  capturePageValue(pageEvent: any) {
    let searchParam = "",
      self = this;
    if(pageEvent.pageIndex > this.pageIndex) {
      this.auditService.audit('RP-011', 'View history', 'RP-View history', 'View history', 'User clicks on next page in pagination', '');
    }
    if(pageEvent.pageSize != this.pageSize){
      this.auditService.audit('RP-012', 'View history', 'RP-View history', 'View history', 'User chooses the number of items to be shown on each page from drop-down', '');
    }
    this.controlTypes.forEach(controlType => {
      if (self[controlType]) {
        if (searchParam) {
          searchParam = searchParam + "&" + controlType + "=" + self[controlType];
        } else {
          searchParam = controlType + "=" + self[controlType];
        }
      }
    });

    this.getServiceHistory(pageEvent, searchParam, "");
  }

  downloadServiceHistory() {
    this.isLoading = true;
    this.auditService.audit('RP-005', 'View history', 'RP-View history', 'View history', 'User clicks on "download" button', '');
    let searchParam = "", self = this;
    this.controlTypes.forEach(controlType => {
      if (self[controlType]) {
        if (searchParam) {
          searchParam = searchParam + "&" + controlType + "=" + self[controlType];
        } else {
          searchParam = controlType + "=" + self[controlType];
        }
      }
    });
    this.dataStorageService
      .downloadServiceHistory(searchParam)
      .subscribe(data => {
        var fileName = ""
        const contentDisposition = data.headers.get('content-disposition');
        if (contentDisposition) {
          this.isLoading = false;
          const fileNameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
          const matches = fileNameRegex.exec(contentDisposition);
          if (matches != null && matches[1]) {
            fileName = matches[1].replace(/['"]/g, '');
          }
          saveAs(data.body, fileName);
        }else{
          this.isLoading = false;
        }
        
      },
        err => {
          console.error(err);
        });
  }

  showMessage(message: string) {
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '650px',
      data: {
        case: 'MESSAGE',
        title: this.popupMessages.genericmessage.successLabel,
        message: message,
        btnTxt: this.popupMessages.genericmessage.successButton,
        isOk:'OK'
      }
    });
    return dialogRef;
  }

  captureVirtualKeyboard(element: HTMLElement, index: number) {
    this.keyboardRef.instance.setInputInstance(this.attachToElementMesOne._results[index]);
  }

  openKeyboard() {
    if (this.keyboardService.isOpened) {
      this.keyboardService.dismiss();
      this.keyboardRef = undefined;
    } else {
      this.keyboardRef = this.keyboardService.open(defaultJson.keyboardMapping[this.langCode]);
      document.getElementById("appIdValue").focus();
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }

  showErrorMessagePopup(message: string) {
    let errorCode = message[0]['errorCode']
    setTimeout(() => {
        this.dialog
          .open(DialogComponent, {
            width: '650px',
            data: {
              case: 'MESSAGE',
              title: this.popupMessages.genericmessage.errorLabel,
              message: this.popupMessages.serverErrors[errorCode],
              btnTxt: this.popupMessages.genericmessage.successButton,
              isOk:"OK"
            },
            disableClose: true
          });
    }, 500)
  }

  get fontSize(): any {
    return this.fontSizeService.fontSize;
  }

  onItemSelected(item: any) {
    this.router.navigate([item]);
  }

  @HostListener("blur", ["$event"])
  @HostListener("focusout", ["$event"])
  private _hideKeyboard() {
    if (this.keyboardService.isOpened) {
      this.keyboardService.dismiss();
    }
  }
}
