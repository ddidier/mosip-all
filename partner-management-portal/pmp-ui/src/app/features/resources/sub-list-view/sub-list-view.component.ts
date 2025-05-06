import { Component, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import * as appConstants from 'src/app/app.constants';
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { RequestModel } from 'src/app/core/models/request.model';
import { CenterRequest } from 'src/app/core/models/centerRequest.model';
import { PaginationModel } from 'src/app/core/models/pagination.model';
import { SortModel } from 'src/app/core/models/sort.model';
import { AppConfigService } from 'src/app/app-config.service';
import Utils from 'src/app/app.utils';
import { MatDialog } from '@angular/material';
import { DialogComponent } from 'src/app/shared/dialog/dialog.component';
import { TranslateService } from '@ngx-translate/core';
import { AuditService } from 'src/app/core/services/audit.service';

@Component({
  selector: 'app-sub-list-view',
  templateUrl: './sub-list-view.component.html',
  styleUrls: ['./sub-list-view.component.scss']
})
export class SubListViewComponent implements OnDestroy {
  headerName: string;
  displayedColumns = [];
  actionButtons = [];
  actionEllipsis = [];
  paginatorOptions: any;
  sortFilter = [];
  pagination = new PaginationModel();
  centerRequest = {} as CenterRequest;
  requestModel: RequestModel;
  masterData = [];
  mapping: any;
  errorMessages: any;
  subscribed: any;
  noData = false;
  filtersApplied = false;
  masterDataType: string;
  auditEventId: string[];

  constructor(
    private router: Router,
    private dataStorageService: DataStorageService,
    private appService: AppConfigService,
    private activatedRoute: ActivatedRoute,
    public dialog: MatDialog,
    private translateService: TranslateService,
    private auditService: AuditService
  ) {
    translateService
      .getTranslation(appService.getConfig().primaryLangCode)
      .subscribe(response => {
        console.log("response>>>>"+response);
        this.errorMessages = response.errorPopup;
      });
    this.subscribed = router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.initializeComponent();
      }
    });
  }

  async initializeComponent() {
    await this.loadData();
    if (this.activatedRoute.snapshot.params.type !== this.masterDataType) {
      this.masterDataType = this.activatedRoute.snapshot.params.type;
      this.auditService.audit(3, this.auditEventId[0], this.masterDataType);
    }
    await this.getMasterDataTypeValues(
      this.appService.getConfig().primaryLangCode
    );
  }
  loadData() {
    return new Promise((resolve, reject) => {
      const routeParts = this.activatedRoute.snapshot.params.childurl;
      console.log("routeParts>>>"+routeParts);
      this.mapping = appConstants.masterdataMapping[`${routeParts}`];
      this.headerName =
        appConstants.masterdataMapping[`${routeParts}`].headerName;
      console.log(this.mapping);
      this.dataStorageService
        .getSpecFileForMasterDataEntity(this.mapping.specFileName)
        .subscribe(response => {
          console.log(response);
          this.displayedColumns = response.columnsToDisplay.filter(
            values => values.showInListView === 'true'
          );
          console.log(this.displayedColumns.length);
          this.actionButtons = response.actionButtons.filter(
            value => value.showIn.toLowerCase() === 'ellipsis'
          );
          console.log(this.actionButtons);
          this.actionEllipsis = response.actionButtons.filter(
            value => value.showIn.toLowerCase() === 'button'
          );
          console.log(this.actionEllipsis);
          this.paginatorOptions = response.paginator;
          console.log(this.paginatorOptions);
          this.auditEventId = response.auditEventIds;
          resolve(true);
        });
    });
  }

  getSortColumn(event: SortModel) {
    console.log(event);
    this.sortFilter.forEach(element => {
      if (element.sortField === event.sortField) {
        const index = this.sortFilter.indexOf(element);
        this.sortFilter.splice(index, 1);
      }
    });
    if (event.sortType != null) {
      this.sortFilter.push(event);
    }
    console.log(this.sortFilter);
    const filters = Utils.convertFilter(
      this.activatedRoute.snapshot.queryParams,
      this.appService.getConfig().primaryLangCode
    );
    filters.sort = this.sortFilter;
    const url = Utils.convertFilterToUrl(filters);
    this.router.navigateByUrl(
      `pmp/resources/${this.activatedRoute.snapshot.params.type}/${this.activatedRoute.snapshot.params.childurl}/view/${this.activatedRoute.snapshot.params.id}?${url}`
    );
  }

  pageEvent(event: any) {
    const filters = Utils.convertFilter(
      this.activatedRoute.snapshot.queryParams,
      this.appService.getConfig().primaryLangCode
    );
    filters.pagination.pageFetch = event.pageSize;
    filters.pagination.pageStart = event.pageIndex;
    const url = Utils.convertFilterToUrl(filters);
    this.router.navigateByUrl(
      `pmp/resources/${this.activatedRoute.snapshot.params.type}/${this.activatedRoute.snapshot.params.childurl}/view/${this.activatedRoute.snapshot.params.id}?${url}`
    );
  }

  getMasterDataTypeValues(language: string) {
    return new Promise((resolve, reject) => {
      this.masterData = [];
      this.noData = false;
      this.filtersApplied = false;
      const routeParts = this.activatedRoute.snapshot.params.childurl;
      this.mapping = appConstants.masterdataMapping[`${routeParts}`];
      if(this.activatedRoute.snapshot.params.id)
        this.mapping['id'] = this.activatedRoute.snapshot.params.id;
      const filters = Utils.convertFilter(
        this.activatedRoute.snapshot.queryParams,
        language
      );
      if (filters.filters.length > 0) {
        this.filtersApplied = true;
      }
      this.sortFilter = filters.sort;
      this.requestModel = new RequestModel(null, null, filters);
      console.log("this.requestModel>>>"+JSON.stringify(this.requestModel));
      this.requestModel.request.filters.push({columnName: "deviceDetailId", type: "equals", value: this.activatedRoute.snapshot.params.id});
      this.dataStorageService
        .getDataByTypeAndId(this.mapping, this.requestModel)
        .subscribe(({ response }) => {
          if (response != null) {
            this.paginatorOptions.totalEntries = response.totalRecord;
            this.paginatorOptions.pageIndex = filters.pagination.pageStart;
            this.paginatorOptions.pageSize = filters.pagination.pageFetch;
            if (response.data !== null) {
              this.masterData = response.data ? [...response.data] : [];
            } else {
              this.noData = true;
            }
          } else {
            this.dialog
              .open(DialogComponent, {
                data: {
                  case: 'MESSAGE',
                  title: this.errorMessages.technicalError.title,
                  message: this.errorMessages.technicalError.message,
                  btnTxt: this.errorMessages.technicalError.btnTxt
                },
                width: '700px'
              })
              .afterClosed()
              .subscribe(result => {
                console.log('dialog is closed from view component');
              });
          }
          resolve(response);
        });
    });
  }

  changePage() {
    this.router.navigateByUrl(
      `pmp/resources/${this.masterDataType}/view`
    );
  }

  ngOnDestroy() {
    this.subscribed.unsubscribe();
  }
}
