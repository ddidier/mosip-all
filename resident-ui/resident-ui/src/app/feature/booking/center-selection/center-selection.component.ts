import { Component, OnInit, OnDestroy, ViewChild, ElementRef, ViewChildren } from "@angular/core";
import { MatDialog, MatPaginator,MatPaginatorIntl } from "@angular/material";
import { DataStorageService } from "src/app/core/services/data-storage.service";
import { RegistrationCentre } from "./registration-center-details.model";
import { Router, ActivatedRoute } from "@angular/router";
import { DialogComponent } from 'src/app/shared/dialog/dialog.component';
import { BookingService } from "../booking.service";
import { TranslateService } from "@ngx-translate/core";
import * as appConstants from "./../../../app.constants";
import { Subscription } from "rxjs";
import { saveAs } from 'file-saver';
import { AuditService } from "src/app/core/services/audit.service";
import {
  MatKeyboardRef,
  MatKeyboardComponent,
  MatKeyboardService
} from 'ngx7-material-keyboard-ios';
import defaultJson from "src/assets/i18n/default.json";
import { BreakpointService } from "src/app/core/services/breakpoint.service";
import { FontSizeService } from "src/app/core/services/font-size.service";

@Component({
  selector: "app-center-selection",
  templateUrl: "./center-selection.component.html",
  styleUrls: ["./center-selection.component.css"]
})
export class CenterSelectionComponent implements OnInit, OnDestroy {
  // @ViewChild(MatPaginator) paginator: MatPaginator;

  REGISTRATION_CENTRES: RegistrationCentre[] = [];
  searchClick: boolean = false;
  isWorkingDaysAvailable = false;
  canDeactivateFlag = true;
  locationTypes = [];
  identityData = [];
  allLocationTypes = [];
  locationType = null;
  searchText = null;
  showTable = false;
  selectedCentre = null;
  showMap = false;
  showMessage = false;
  enableNextButton = false;
  bookingDataList = [];
  errorlabels: any;
  nearbyClicked = false;
  apiErrorCodes: any;
  step = 0;
  textDir = localStorage.getItem("direction");
  showDescription = false;
  mapProvider = "OSM";
  searchTextFlag = false;
  displayMessage = "Showing nearby registration centers";
  subscriptions: Subscription[] = [];
  langCode = localStorage.getItem("langCode");
  workingDays: string;
  preRegId = [];
  recommendedCenterLocCode = 1;
  locationNames = [];
  locationCodes = [];
  showFirstLastButtons:any = true
  // MatPaginator Inputs
  totalItems = 0;
  defaultPageSize = 5;
  pageSize = this.defaultPageSize;
  pageIndex = 0;
  pageSizeOptions: number[] = [5, 10, 15, 20];
  centerSelection: any;
  isBlankSpace:boolean = true;
  showWarningMsg:boolean = false;
  showMesssageText:string="";
  popupMessages: any;
  isMobileView:boolean = false;
  showLocationDetails:boolean = true;
  showBackBtn:boolean = false;
  positions:any;

  private keyboardRef: MatKeyboardRef<MatKeyboardComponent>;
  @ViewChildren('keyboardRef', { read: ElementRef })
  private attachToElementMesOne: any;
  constructor(
    public dialog: MatDialog,
    private service: BookingService,
    private dataService: DataStorageService,
    private router: Router,
    private route: ActivatedRoute,
    private translate: TranslateService,
    private activatedRoute: ActivatedRoute,
    private auditService: AuditService,
    private paginator: MatPaginatorIntl,
    private keyboardService: MatKeyboardService,
    private breakPointService: BreakpointService,
    private fontSizeService: FontSizeService
  ) {
    this.translate.use(this.langCode); 
    this.breakPointService.isBreakpointActive().subscribe(active => {
      if (active) {
        if (active === "extraSmall") {
           this.isMobileView = true;
           this.showBackBtn = true;
           this.showMap = false;
        }else{
          this.showBackBtn = false;
          this.isMobileView = false;
          this.showMap = true;
          this.showLocationDetails = true;
        }
      }
    })
  }

  async ngOnInit() {
    this.recommendedCenterLocCode = 5;
    const subs = this.dataService
      .getLocationHierarchyLevel(this.langCode)
      .subscribe((response) => {
        //get all location types from db
        this.allLocationTypes = response[appConstants.RESPONSE]["locationHierarchyLevels"];
        this.locationTypes = this.allLocationTypes.filter(
          (locType) =>
            locType.hierarchyLevel <= this.recommendedCenterLocCode
        );
        //sort the filtered array in ascending order of hierarchyLevel
        this.locationTypes.sort(function (a, b) {
          return a.hierarchyLevel - b.hierarchylevel;
        });
        this.getRecommendedCenters();
      });
    this.subscriptions.push(subs);
    this.getLocation();
    this.getErrorLabels();
  }

  captureVirtualKeyboard(element: HTMLElement, index: number) {
    this.keyboardRef.instance.setInputInstance(this.attachToElementMesOne._results[index]);
  }

  captureValue(event: any) {
      this.searchText = event.target.value;
      this.searchInput();
  }

  openKeyboard(id) {
    if (this.keyboardService.isOpened) {
      this.keyboardService.dismiss();
      this.keyboardRef = undefined;
    } else {
      this.keyboardRef = this.keyboardService.open(defaultJson.keyboardMapping[this.langCode]);
      document.getElementById(id).focus();
    }
  }

  getErrorLabels() {
    this.dataService.getI18NLanguageFiles(localStorage.getItem('langCode')).subscribe((response) => {
      this.errorlabels = response["error"];
      this.apiErrorCodes = response[appConstants.API_ERROR_CODES];
      this.popupMessages = response;
      this.paginator.itemsPerPageLabel = response['paginatorIntl'].itemsPerPageLabel;
      const originalGetRangeLabel = this.paginator.getRangeLabel;
        this.paginator.getRangeLabel = (page: number, size: number, len: number) => {
          return originalGetRangeLabel(page, size, len)
              .replace('of', response['paginatorIntl'].of);
      }; 
    });
  }

  async getRecommendedCenters() {
    this.totalItems = 0;
    this.nearbyClicked = false;
    let uiFieldName = null;
    this.identityData.forEach((obj) => {
      if (
        obj.inputRequired === true &&
        obj.controlType !== null &&
        (obj.controlType !== "fileupload")
      ) {
        if (obj.locationHierarchyLevel && this.recommendedCenterLocCode == obj.locationHierarchyLevel) {
          uiFieldName = obj.id;
        }
      }
    });
    this.showTable = true;
    this.isWorkingDaysAvailable = true;
    await this.getLocationNamesByCodes();
  }

  getLocationNamesByCodes() {
    return new Promise((resolve) => {
      this.locationCodes.forEach(async (pins, index) => {
        //console.log(pins);
        await this.getLocationNames(pins);
        if (index === this.locationCodes.length - 1) {
          resolve(true);
        }
      });
    });
  }

  getRecommendedCentersApiCall() {
    this.REGISTRATION_CENTRES = [];
    const subs = this.dataService
      .recommendedCenters(
        this.langCode,
        5,
        ["14022"]
      )
      .subscribe((response) => {
        if (response[appConstants.RESPONSE]) {
          this.displayResults(response["response"]);
        }
      },
        (error) => {
          this.showErrorMessage(error, this.errorlabels.regCenterNotavailabe);
        });
    this.subscriptions.push(subs);
  }

  getLocationNames(locationCode) {
    return new Promise((resolve) => {
      this.dataService
        .getLocationInfoForLocCode(locationCode, this.langCode)
        .subscribe((response) => {
          if (response[appConstants.RESPONSE]) {
            let locName = response[appConstants.RESPONSE]["name"];
            this.locationNames.push(locName);
            resolve(true);
          }
        },
          (error) => {
            this.showErrorMessage(error, this.errorlabels.regCenterNotavailabe);
          });
    });
  }

  setSearchClick(flag: boolean) {
    //this.searchClick = flag;
    this.nearbyClicked = false;
  }

  onSubmit() {
    this.searchTextFlag = true;
    if (this.searchText.length !== 0 || this.searchText !== null) {
      this.displayMessage = `Searching results for ${this.searchText} ....`;
    } else {
      this.displayMessage = "";
    }
  }
  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
    this.showDescription = true;
  }

  prevStep() {
    this.step--;
  }
  resetPagination() {
    this.totalItems = 0;
    this.pageSize = this.defaultPageSize;
    this.pageIndex = 0;
    this.getRecommendedCenters();
  }
  
  searchInput(){
    if(this.searchText.length > 2 && this.searchText.match(/[\p{Letter}\p{Number}\p{Mark}\s]+/gu)){
      this.isBlankSpace = false;
    }else{
      this.isBlankSpace = true;
    }
    if(!this.searchText.match(/[\p{Letter}\p{Number}\p{Mark}\s]+/gu)){
      this.showWarningMsg = true;
    }else{
      this.showWarningMsg = false;
    }
  }

  showResults(pageEvent) {
    this.showLocationDetails = true;
    this.isMobileView = this.showBackBtn ?  true : false;
    this.isWorkingDaysAvailable = false;
    if (this.keyboardService.isOpened) {
      this.keyboardService.dismiss();
    }
    this.auditService.audit('RP-040', 'Locate registration center', 'RP-Locate registration center', 'Locate registration center', 'User clicks on "search" button on locate registration center page','');
    this.REGISTRATION_CENTRES = [];
    if (this.locationType !== null && this.searchText) {
      this.showMap = false;
      if (pageEvent) {
        this.pageSize = pageEvent.pageSize;
        this.pageIndex = pageEvent.pageIndex;
      }
      const subs = this.dataService
        .getRegistrationCentersByNamePageWise(
          this.locationType.hierarchyLevel,
          this.searchText,
          this.pageIndex,
          this.pageSize
        )
        .subscribe(
          (response) => {
            if (response[appConstants.RESPONSE]) {
              this.totalItems = response[appConstants.RESPONSE].totalItems;
              this.displayResults(response[appConstants.RESPONSE]);
              this.showMessage = false;
            } else {
              this.totalItems = 0;
              this.showMessage = true;
              this.showMesssageText = this.popupMessages.centerSelection.noRegCenters;
              this.selectedCentre = null;
              this.isWorkingDaysAvailable = true;
            }
          },
          (error) => {
            this.showMessage = true;
            this.totalItems = 0;
            this.selectedCentre = null;
          });
      this.subscriptions.push(subs);
    } else {
      this.showMessage = true;
      this.totalItems = 0;
      this.selectedCentre = null;
    }
  }

  onChangeLocationType() {
    this.showMessage = false;
    this.totalItems = 0;
    this.searchText = "";
    this.selectedCentre = null;
    this.isBlankSpace = true;
    if (this.keyboardService.isOpened) 
      this.keyboardService.dismiss();
  }

  plotOnMap() {
    this.showMap = true;
    this.service.changeCoordinates([
      Number(this.selectedCentre.longitude),
      Number(this.selectedCentre.latitude),
    ]);
  }

  selectedRow(row) {
    this.selectedCentre = row;
    this.enableNextButton = true;

    if (Object.keys(this.selectedCentre).length !== 0 && !this.isMobileView) {
      this.plotOnMap();
    }
  }

  selectedEachMap(center:any){
    if(this.isMobileView){
      this.showLocationDetails = false;
    }
    this.isMobileView = false;
    this.selectedRow(center)
  }

  showAllCenters(){
    this.isMobileView = false;
    this.showLocationDetails = false;
    this.selectedRow(this.REGISTRATION_CENTRES[0]);
  }

  backBtn(){
    this.showMap = false;
    this.showLocationDetails = true;
    this.isMobileView = true;
  }

  getLocation() {
    this.REGISTRATION_CENTRES = [];
    this.nearbyClicked = true;
    if ("geolocation" in navigator) {
      this.showMap = false;
      this.positions = navigator.geolocation;
      this.positions.getCurrentPosition((position) => {
        const subs = this.dataService
          .getNearbyRegistrationCenters(position.coords)
          .subscribe(
            (response) => {
              this.totalItems = 0;
              if (!response["errors"]) {
                this.displayResults(response[appConstants.RESPONSE]);
                this.showMessage = false;
              } else {
                if (response['errors'][0].errorCode === "RES-SER-418") {
                  this.showMesssageText = this.popupMessages.centerSelection.noRegCentersNearby;
                }
                this.searchClick = false;
                this.showMessage = true;
                this.selectedCentre = null;
              }
            },
            (error) => {
              this.showMessage = true;
              this.selectedCentre = null;
              //this.showErrorMessage(error);
            });
        this.subscriptions.push(subs);
      }) 
    }
  }

  changeTimeFormat(time: string): string | number {
    let inputTime = time.split(":");
    let formattedTime: any;
    if (Number(inputTime[0]) < 12 && Number(inputTime[0]) > 0) {
      formattedTime = inputTime[0];
      formattedTime += ":" + inputTime[1] + " am";
    } else if (Number(inputTime[0]) === 0) {
      formattedTime = Number(inputTime[0]) + 12;
      formattedTime += ":" + inputTime[1] + " am";
    } else if (Number(inputTime[0]) === 12) {
      formattedTime = inputTime[0];
      formattedTime += ":" + inputTime[1] + " pm";
    } else {
      formattedTime = Number(inputTime[0]) - 12;
      formattedTime += ":" + inputTime[1] + " pm";
    }
    return formattedTime;
  }

  showTime(startTime: string, endTime: string): string | number {
    let formattedStartTime = this.changeTimeFormat(startTime);
    let formattedEndTime = this.changeTimeFormat(endTime);
    let formattedTime = formattedStartTime + ' - ' + formattedEndTime;
    if (this.textDir == "rtl") {
      formattedTime = formattedEndTime + ' - ' + formattedStartTime;
    }
    return formattedTime;
  }

  dispatchCenterCoordinatesList() {
    const coords = [];
    this.REGISTRATION_CENTRES.forEach((centre) => {
      const data = {
        id: centre.id,
        latitude: Number(centre.latitude),
        longitude: Number(centre.longitude),
      };
      coords.push(data);
    });
    this.service.listOfCenters(coords);
  }

  routeNext() {
    this.canDeactivateFlag = false;
    this.router.navigate(["../pick-time"], {
      relativeTo: this.route,
      queryParams: { regCenter: this.selectedCentre.id },
    });
  }

  routeDashboard() {
    this.canDeactivateFlag = false;
    this.router.navigate([`${this.langCode}/dashboard`]);
  }

  async displayResults(response: any) {
    if (response["registrationCenters"]) {
      this.REGISTRATION_CENTRES = response["registrationCenters"];
    } else if (response["data"]) {
      this.REGISTRATION_CENTRES = response["data"];
    }
    await this.getWorkingDays();
    this.showTable = true;
    this.isWorkingDaysAvailable = true;
    if (this.REGISTRATION_CENTRES) {
      this.selectedRow(this.REGISTRATION_CENTRES[0]);
      this.dispatchCenterCoordinatesList();
    }
  }

  getWorkingDays() {
    return new Promise((resolve) => {
      this.REGISTRATION_CENTRES.forEach((center) => {
        this.dataService
          .getWorkingDays(center.id, this.langCode)
          .subscribe((response) => {
            center.workingDays = "";
            if (response[appConstants.RESPONSE] && response[appConstants.RESPONSE]["workingdays"]) {
              response[appConstants.RESPONSE]["workingdays"].forEach((day) => {
                if (center.workingDays != "") {
                  center.workingDays += ", ";
                }
                center.workingDays = center.workingDays + day.name;
              });
            }
            this.isWorkingDaysAvailable = true;
            resolve(true);
          },
            (error) => {
              this.isWorkingDaysAvailable = true;
              this.showErrorMessage(error);
            });
      });
    });
  }

  /**
   * @description This is a dialoug box whenever an error comes from the server, it will appear.
   *
   * @private
   * @memberof CenterSelectionComponent
   */
  private showErrorMessage(error: any, customErrMsg?: string) {
    const titleOnError = this.errorlabels.errorLabel;
    let message = "";
    if (customErrMsg) {
      message = customErrMsg;
    } else {
    }
    const body = {
      case: "ERROR",
      title: titleOnError,
      message: message,
      yesButtonText: this.errorlabels.button_ok,
    };
  }

  openDialog() {
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '850px',
      data: {
        case: 'CONFIRMATION',
        title: "",
        message: "message",
      },
    });
    return dialogRef;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }

  downloadCentersPdf() {
    this.auditService.audit('RP-041', 'Locate registration center', 'RP-Locate registration center', 'Locate registration center', 'User clicks on "download" button on locate registration center page','');
    if (this.locationType && this.searchText) {
      this.dataService.registrationCentersList(this.langCode, this.locationType.hierarchyLevel, this.searchText)
        .subscribe(response => {
          if (response.headers.get('Content-Type') === 'application/pdf') {
            var fileName = "";
            const contentDisposition = response.headers.get('Content-Disposition');
            if (contentDisposition) {
              const fileNameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
              const matches = fileNameRegex.exec(contentDisposition);
              if (matches != null && matches[1]) {
                fileName = matches[1].replace(/['"]/g, '');
              }
            }
            saveAs(response.body, fileName);
          } else {
            console.log("")
          }
        }, error => {
          console.log(error)
        })
    } else {
      if (navigator.geolocation) {
        this.positions.getCurrentPosition((position) => {
          this.dataService.nearByRegistrationCentersList(this.langCode, position.coords)
            .subscribe(response =>{
              if (response.headers.get('Content-Type') === 'application/pdf') {
                var fileName = "";
                const contentDisposition = response.headers.get('Content-Disposition');
                if (contentDisposition) {
                  const fileNameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                  const matches = fileNameRegex.exec(contentDisposition);
                  if (matches != null && matches[1]) {
                    fileName = matches[1].replace(/['"]/g, '');
                  }
                }
                saveAs(response.body, fileName);
              } else {
                console.log("");
              }
            }, error =>{
              console.log(error);
            })
        })
      }
    }
  }

  get fontSize(): any {
    document.documentElement.style.setProperty('--fs', this.fontSizeService.fontSize.tabs)
    return this.fontSizeService.fontSize;
  }

  onItemSelected(item: any) {
    if (item.index === 1) {
      this.router.navigate(["document"]);
    } else if (item === "home") {
      this.router.navigate(["dashboard"]);
    } else {
      this.router.navigate(["regcenter"]);
    }
  }
}