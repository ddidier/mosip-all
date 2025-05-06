import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA
} from '@angular/material/dialog';
import { Component, OnInit, Inject, ViewEncapsulation } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { LoginRedirectService } from 'src/app/core/services/loginredirect.service';
import { FormGroup } from '@angular/forms';
import { RequestModel } from 'src/app/core/models/request.model';
import { AppConfigService } from 'src/app/app-config.service';
import { TranslateService } from '@ngx-translate/core';
import { LogoutService } from './../../core/services/logout.service';
import { InteractionService } from 'src/app/core/services/interaction.service';
import { FontSizeService } from "src/app/core/services/font-size.service";

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DialogComponent implements OnInit {
  input: any;
  confirm = true;
  FilterData = [];
  missingData = [];
  noMissingDataFlag = false;
  filterGroup = new FormGroup({});
  routeParts: string;
  filters = [];
  existingFilters: any;
  requestModel: RequestModel;
  options = [];
  createUpdateSteps: any = {};
  momentDate: any;
  primaryLangCode: string = localStorage.getItem("langCode");
  requiredError = false;
  rangeError = false;
  fieldName = '';
  popMsgbgColor: string = "#E8FDF2"
  popMsgColor: string = "#03A64A"
  cancelApplied = false;
  filterOptions: any = {};
  holidayForm: FormGroup;
  sitealignment = 'ltr';
  icon: string = "./assets/sucess_icon.png";
  isChecked: boolean = true;
  otpTimeMinutes: number = 2;
  otpTimeSeconds: any = "00";
  displaySeconds: any = this.otpTimeSeconds
  interval: any;
  submitBtnDisabled: boolean = true;
  resendBtnDisabled: boolean = true;
  submitBtnBgColor: string = "#BCBCBC";
  resendBtnBgColor: string = "#BCBCBC";
  disableInput:boolean = false;
  isInprogressDetialsShow:boolean = false;
  langJSON:any;
  selectedIndex:any;
  eidDetails:any;

  constructor(
    public dialog: MatDialog,
    public dialogRef: MatDialogRef<DialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private router: Router,
    private dataStorageService: DataStorageService,
    private config: AppConfigService,
    private activatedRoute: ActivatedRoute,
    private translate: TranslateService,
    private logoutService: LogoutService,
    private interactionService: InteractionService,
    public appConfigService: AppConfigService,
    private redirectService: LoginRedirectService,
    private fontSizeService: FontSizeService,
    private translateService: TranslateService
  ) {
    this.translate.use(this.primaryLangCode);
    if (this.primaryLangCode === "ara") {
      this.sitealignment = 'rtl';
    }
    if (this.data.title === "Error") {
      this.popMsgbgColor = "#FFD9D8";
      this.popMsgColor = "#C90500";
      this.icon = "./assets/cancel_icon.png";
    } else if (this.data.warningForChannel === "warningForChannel") {
      this.popMsgbgColor = "#FFF9DB";
      this.popMsgColor = "#89730B";
      this.icon = "./assets/icons/iconfont/Group 130.svg";
    }
    else if (this.data.title === "Warning") {
      this.popMsgbgColor = "#FFF9DB";
      this.popMsgColor = "#89730B";
      this.icon = "./assets/icons/iconfont/Group 130.svg";
    }

    if (this.data.case === "OTP") {
      this.setOtpTime()
    }
    this.appConfigService.getConfig();
  }

  async ngOnInit() {
    this.input = this.data;
    this.translateService
    .getTranslation(localStorage.getItem("langCode"))
    .subscribe(response => {
      this.langJSON = response.trackservicerequest;
    }); 
  }

  setOtpTime() {
    this.otpTimeMinutes = this.appConfigService.getConfig()['mosip.kernel.otp.expiry-time']/60;
    this.disableInput = false;
    this.interval = setInterval(() => {
      if (this.otpTimeSeconds < 0 || this.displaySeconds === "00") {
        this.otpTimeSeconds = 59
        this.otpTimeMinutes -= 1
      }
      if (this.otpTimeMinutes < 0 && this.displaySeconds === "00") {
        this.otpTimeSeconds = 0;
        this.otpTimeMinutes = 0;
        clearInterval(this.interval)
        this.displaySeconds = "00";
        this.submitBtnDisabled = true;
        this.resendBtnDisabled = false;
        this.disableInput = true;
        this.submitBtnBgColor = "#BCBCBC";
        this.resendBtnBgColor = "#03A64A";
      }
      if (this.otpTimeSeconds < 10) {
        this.displaySeconds = "0" + this.otpTimeSeconds.toString()
      } else {
        this.displaySeconds = this.otpTimeSeconds
      }
      this.otpTimeSeconds -= 1


    }, 1000);
  }

  onNoClick(): void {
    this.cancelApplied = true;
    this.dialog.closeAll();
  }

  dismiss(): void {
    this.dialog.closeAll();
  }

  agreeConditions() {
    this.isChecked = !this.isChecked;
  }

  shareInfoBtn(id:any): void {
    this.dialog.closeAll();
    this.interactionService.sendClickEvent(id);
  }
  
  shareInfoForUpdateMyData(id:any){
    this.dialog.closeAll();
    this.interactionService.sendClickEvent(id);
  }
  vidWarning(): void {
    this.dialog.closeAll();
    this.interactionService.sendClickEvent("confirmBtnForVid");
  }

  vidDelete(): void {
    this.dialog.closeAll();
    this.interactionService.sendClickEvent("deleteVID");
  }

  vidDownload(): void {
    this.dialog.closeAll();
    this.interactionService.sendClickEvent("downloadVID");
  }

  downloadPersonalCard() {
    this.dialog.closeAll();
    this.interactionService.sendClickEvent("downloadPersonalCard");
  }

  changeLanguage(){
    this.dialog.closeAll();
    this.interactionService.sendClickEvent("changeLanguage");
  }

  getInputValues(value: any) {
    if (value.length > 0) {
      this.submitBtnDisabled = false
      this.submitBtnBgColor = "#03A64A"
    } else {
      this.submitBtnDisabled = true
      this.submitBtnBgColor = "#BCBCBC"
    }

  }

  lockunlockauthWarning(){
    this.interactionService.sendClickEvent("confirmBtn")
    this.dialog.closeAll();
  }

  viewDetails(eventId: any) {
    this.router.navigateByUrl(`uinservices/trackservicerequest?eid=` + eventId);
    this.dialog.closeAll();
  }

  openLoginPage(){
    this.redirectService.redirect(window.location.href);
    this.dialog.closeAll();
  }

  regCenters(){
    this.router.navigate(['regcenter'])
    this.dialog.closeAll();
  }

  sendResponse(value: any) {
    if (value.length > 0) {
      this.submitBtnDisabled = true
    }
    
    if (value !== "resend") {
      clearInterval(this.interval)
      let data = {'otp':value, 'type':"otp"}
      this.interactionService.sendClickEvent(data)
    } else {
      clearInterval(this.interval)
      this.interactionService.sendClickEvent(value)
      this.otpTimeMinutes = this.appConfigService.getConfig();
      console.log(value)
      this.displaySeconds = "00";
      this.setOtpTime()
      this.resendBtnDisabled = true;
      this.submitBtnBgColor = "#BCBCBC";
      this.resendBtnBgColor = "#BCBCBC";
      
    }
  }
  dismissPage(){
    this.dialog.closeAll()
  }

  dismissPageToHome(){
    this.dialog.closeAll()
    this.router.navigate(['uinservices/dashboard'])
  }
  logOut(){
    this.redirectService.redirect(window.location.href);
  }
  get fontSize(): any {
    return this.fontSizeService.fontSize;
  }

  showInprogressDataDetails(btnType:string, index:any, eid:string){
    this.selectedIndex = index;
    this.isInprogressDetialsShow = false;

    if(btnType === 'viewMore'){
      this.dataStorageService
      .getEIDStatus(eid)
      .subscribe((response) => {
        if(response["response"]){
          this.eidDetails = response["response"];
          this.isInprogressDetialsShow = true;
        }else if(response["errors"]){
          this.eidDetails = ""
        }   
      });
    }
  }
  
  logOutBtn(){
    this.interactionService.sendClickEvent("logOutBtn");
  }
}
