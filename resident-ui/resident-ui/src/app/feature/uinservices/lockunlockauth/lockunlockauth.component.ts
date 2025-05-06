import { Component, OnInit, OnDestroy } from "@angular/core";
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { TranslateService } from "@ngx-translate/core";
import { Subscription } from "rxjs";
import { Router } from "@angular/router";
import Utils from 'src/app/app.utils';
import { AppConfigService } from 'src/app/app-config.service';
import { DialogComponent } from 'src/app/shared/dialog/dialog.component';
import { MatDialog } from '@angular/material';
import { InteractionService } from "src/app/core/services/interaction.service";
import { AuditService } from "src/app/core/services/audit.service";
import { AutoLogoutService } from "src/app/core/services/auto-logout.service";
import { BreakpointService } from "src/app/core/services/breakpoint.service";
import { FontSizeService } from "src/app/core/services/font-size.service";

@Component({
  selector: "app-lockunlockauth",
  templateUrl: "lockunlockauth.component.html",
  styleUrls: ["lockunlockauth.component.css"],
})
export class LockunlockauthComponent implements OnInit, OnDestroy {
  langJSON:any;
  popupMessages:any;
  subscriptions: Subscription[] = [];
  selectedValue:string = "generatevid";
  authlist: any;
  policyType: any;
  vidType:string = "";
  notificationType:Array<string>=[];
  vidValue:string = "";
  infoMsg:string;
  shortInfoMsg:any;
  submitBtnDisable:boolean = true;
  message:any;
  clickEventSubscription: Subscription; 
  changedItems:any = [];
  showSpinner:boolean = true;
  cols: number;
  rowHeight:string;
  userPreferredLangCode = localStorage.getItem("langCode");
  message2:any;
  sitealignment:any = localStorage.getItem('direction');

  constructor(private autoLogout: AutoLogoutService,private interactionService: InteractionService,private dialog: MatDialog,private appConfigService: AppConfigService, private dataStorageService: DataStorageService, private translateService: TranslateService, 
    private router: Router,private auditService: AuditService, private breakPointService: BreakpointService, private fontSizeService: FontSizeService) {
      this.clickEventSubscription = this.interactionService.getClickEvent().subscribe((id) => {
      if (id === "confirmBtn") {
        this.updateAuthlockStatus()
      }
    });

    this.breakPointService.isBreakpointActive().subscribe(active =>{
      if (active) {
        if(active === "extraSmall"){
          this.cols = 1;
          this.rowHeight = '230px';
        }
        if(active === "small"){
          this.cols = 1;
          this.rowHeight = '200px';
        }
        if(active === "medium"){
          this.cols = 2;
          this.rowHeight = '200px';
        }
        if(active === "ExtraLarge" || active === "large"){
          this.cols = 3;
          this.rowHeight = '195px';
        }
      }
    });
  }

  async ngOnInit() {
    this.translateService.use(localStorage.getItem("langCode"));

    this.translateService
    .getTranslation(localStorage.getItem("langCode"))
    .subscribe(response => {
      this.langJSON = response;
      this.popupMessages = response;
      this.infoMsg = response.InfomationContent.secureMyID;
    });

    setTimeout(() => {
      this.getAuthlockStatus();  
    }, 700);   
    
    const subs = this.autoLogout.currentMessageAutoLogout.subscribe(
      (message) => (this.message2 = message) //message =  {"timerFired":false}
    );
    this.subscriptions.push(subs);

    if (!this.message2["timerFired"]) {
      this.autoLogout.getValues(this.userPreferredLangCode);
      this.autoLogout.setValues();
      this.autoLogout.keepWatching();
    } else {
      this.autoLogout.getValues(this.userPreferredLangCode);
      this.autoLogout.continueWatching();
    }
    
  }

  getAuthlockStatus(){
    let authTypes = this.appConfigService.getConfig()["auth.types.allowed"].split(',');
    let authTypesJSON = {};
    let newAuthlist = [];
    this.dataStorageService
    .getAuthlockStatus()
    .subscribe((response) => {
      if(response["response"]){  
        newAuthlist = response["response"]["authTypes"];
        if(response["response"]["authTypes"].length == 0){
          for (var i=0 ; i < authTypes.length ; i++){
            authTypesJSON = {};
            authTypesJSON["authType"] = authTypes[i].split('-')[0];
            authTypesJSON["authSubType"] =  authTypes[i].split('-')[1];
            authTypesJSON["locked"] = false;
            authTypesJSON["unlockForSeconds"] = null;
            authTypesJSON["recorddirty"] = false;
            authTypesJSON["label"] = this.langJSON.lockunlockauth.labelmap[authTypes[i]];
            this.authlist.push(authTypesJSON);
          }
        }else{
          this.authlist = [];
          for (var i=0 ; i < authTypes.length ; i++){
            authTypesJSON = {};
            authTypesJSON["authType"] = authTypes[i].split('-')[0];
            authTypesJSON["authSubType"] =  authTypes[i].split('-')[1];

            if(authTypes[i].split('-')[1]){
              newAuthlist.forEach(el => {      
                if(el.authSubType === authTypes[i].split('-')[1]){
                  authTypesJSON["locked"] = el.locked;
                }           
              })
            }else{
              newAuthlist.forEach(el => {  
                if(el.authType === authTypes[i]){                  
                  authTypesJSON["locked"] = el.locked;
                }                    
              })
            }   
            authTypesJSON["label"] = this.langJSON.lockunlockauth.labelmap[authTypes[i]];       
            authTypesJSON["recorddirty"] = false;
            authTypesJSON["unlockForSeconds"] = null;
            this.authlist.push(authTypesJSON);
          }
        }
        localStorage.setItem("authList", JSON.stringify(this.authlist));
        this.showSpinner = false;
      }else{
          this.showErrorPopup(response["errors"])
      }
    });
    
  }

  updateAuthlockStatusBtn(){
    this.auditService.audit('RP-025', 'Lock/unlock authentication type', 'RP-Lock/unlock authentication type', 'Lock/unlock authentication type', 'User clicks on "submit" button', '');
    this.showWarningMessage("")
  }

  setAuthlockStatus(authTypes: any){  
    let authTypeValidate = "";
    if(authTypes.authSubType){
      if(this.changedItems.includes(authTypes.authSubType)){
          this.changedItems = this.changedItems.filter(item => item !== authTypes.authSubType)
      }else{
        this.changedItems.push(authTypes.authSubType)
      }
    }else{
      if(this.changedItems.includes(authTypes.authType)){
        this.changedItems = this.changedItems.filter(item => item !== authTypes.authType)
    }else{
      this.changedItems.push(authTypes.authType)
    }
    }
    
    if(this.changedItems.length){
      this.submitBtnDisable = false;
    }else{
      this.submitBtnDisable = true;
    }

    if(authTypes.authSubType){
      authTypeValidate = authTypes.authType+"-"+authTypes.authSubType;
    }else{
      authTypeValidate = authTypes.authType;
    }    
    for(var i=0 ; i < this.authlist.length ; i++){
      if(authTypeValidate.includes("-")){
        if(authTypeValidate === (this.authlist[i].authType+"-"+this.authlist[i].authSubType)) {
          if(this.authlist[i].locked){
            //this.unlockConfirm(this.authlist[i])
            this.authlist[i].locked = false;
          }else{
            this.authlist[i].locked = true;
          }
          if(authTypes.recorddirty){
            this.authlist[i].recorddirty = false;
          }else{
            this.authlist[i].recorddirty = true;
          }
        }
      }else{
        if(authTypeValidate === this.authlist[i].authType) {
          if(this.authlist[i].locked){
            //this.unlockConfirm(this.authlist[i])
            this.authlist[i].locked = false;
          }else{
            this.authlist[i].locked = true;
          }

          if(authTypes.recorddirty){
            this.authlist[i].recorddirty = false;
          }else{
            this.authlist[i].recorddirty = true;
          }
        }
      }      
    }
  }

  updateAuthlockStatus(){
    this.showSpinner = true;
    let buildfinaldata = [];
    this.authlist.forEach(item =>{
      if(item.authSubType){
        if(this.changedItems.includes(item.authSubType)){
            buildfinaldata.push(item)
        }
      }else{
        if(this.changedItems.includes(item.authType)){
          buildfinaldata.push(item)
        }
      }
    })

    const request = {
      "id": "mosip.resident.auth.lock.unlock",
      "version": this.appConfigService.getConfig()["resident.vid.version.new"],
      "requesttime": Utils.getCurrentDate(),
      "request":{
        "individualId": "",      
        "authTypes": buildfinaldata
      }
    };
    this.dataStorageService.updateAuthlockStatus(request).subscribe(response => {
        this.getAuthlockStatus();  
        if(!response.body["errors"]){
          this.submitBtnDisable = true;
          let eventId = response.headers.get("eventid")
          this.showMessage(eventId);
          this.changedItems = {}
          this.router.navigate(['uinservices/dashboard']);
        }else{
          this.showErrorPopup(response.body["errors"]);
        }
      },
      error => {
        console.log(error);
      }
    );
  }

  unlockConfirm(authInfo: any) {    
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '450px',
      hasBackdrop: false,
      data: {
        case: 'UNLOCKCONFIRMATION',
        title: "",
        message: authInfo,
        btnTxt: this.popupMessages.genericmessage.successButton,
      }
    });
    return dialogRef;
  }

  showMessage(eventId:any) {   
    this.message =  this.popupMessages.genericmessage.secureMyId.successMsg.replace("$eventId",eventId)
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '550px',
      data: {
        case: 'MESSAGE',
        title: this.popupMessages.genericmessage.successLabel,
        message: this.message,
        eventId,
        btnTxt: this.popupMessages.genericmessage.successButton,
        isOk:"OK",
        clickHere:this.popupMessages.genericmessage.clickHere,
        dearResident:this.popupMessages.genericmessage.dearResident,
        trackStatusText:this.popupMessages.genericmessage.trackStatusText
      }
    });
    return dialogRef;
  }

  showWarningMessage(vidType: any) {
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '550px',
      data: {
        case: 'MESSAGE',
        title: this.popupMessages.genericmessage.warningLabel,
        message: this.popupMessages.genericmessage.secureMyId.confirmationMessage,
        btnTxt: this.popupMessages.genericmessage.yesButton,
        isYes:"Yes",
        yesBtnFor:"lockunlockauth",
        btnTxtNo: this.popupMessages.genericmessage.noButton,
        isNo:"No"
      }
    });
    return dialogRef;
  }

  showErrorPopup(message: string) {
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
            isOk:'OK'
          },
          disableClose: true
        });
    },400)
  }

  get fontSize(): any {
    document.documentElement.style.setProperty('--fs', this.fontSizeService.fontSize.breadcrumb)
    return this.fontSizeService.fontSize;
  }

  onToggle(event: any){
    this.selectedValue = event.source.value;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
    this.clickEventSubscription.unsubscribe()
  }

  onItemSelected(item: any) {
      this.router.navigate([item]);
  }


}
