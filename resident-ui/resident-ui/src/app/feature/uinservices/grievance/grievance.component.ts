import { Component, OnInit, ElementRef, ViewChildren, HostListener } from '@angular/core';
import { Router, ActivatedRoute } from "@angular/router";
import { NgForm } from '@angular/forms';
import { TranslateService } from "@ngx-translate/core";
import { AppConfigService } from 'src/app/app-config.service';
import { DataStorageService } from "src/app/core/services/data-storage.service";
import { DialogComponent } from 'src/app/shared/dialog/dialog.component';
import { MatDialog } from '@angular/material';
import { Subscription } from "rxjs";
import Utils from 'src/app/app.utils';
import { AutoLogoutService } from 'src/app/core/services/auto-logout.service';
import {
  MatKeyboardRef,
  MatKeyboardComponent,
  MatKeyboardService
} from 'ngx7-material-keyboard-ios';
import defaultJson from "src/assets/i18n/default.json";
import { FontSizeService } from "src/app/core/services/font-size.service";

@Component({
  selector: 'app-grievance',
  templateUrl: './grievance.component.html',
  styleUrls: ['./grievance.component.css']
})
export class GrievanceComponent implements OnInit {
  grievanceData: any;
  reportMsg: string = "";
  message: string;
  popupMessages: any;
  errorCode: string;
  totalCommentCount: number;
  remainingChars: number;
  errorMessage: any;
  source1: string;
  source2: string;
  phoneCharLimit: any;
  emailCharLimit: any;
  userPreferredLangCode = localStorage.getItem("langCode");
  message2:any;
  subscriptions: Subscription[] = [];
  userFormData:any = {name:null,emailId:null,alternateEmailId:null,alternatePhoneNo:null,eventId:null,message:null,phoneNo:null};

  private keyboardRef: MatKeyboardRef<MatKeyboardComponent>;
  @ViewChildren('keyboardRef', { read: ElementRef })
  private attachToElementMesOne: any;
  constructor(
    private router: Router,
    private translateService: TranslateService,
    private dataStorageService: DataStorageService,
    private appConfigService: AppConfigService,
    private dialog: MatDialog,
    private route: ActivatedRoute,
    private autoLogout: AutoLogoutService,
    private keyboardService: MatKeyboardService,
    private fontSizeService: FontSizeService
  ) {
  }

  ngOnInit() {
    this.translateService.use(localStorage.getItem("langCode"));
    this.translateService.getTranslation(localStorage.getItem("langCode"))
      .subscribe(response => {
        this.grievanceData = response["grievanceRedressal"]
        this.popupMessages = response;
      })

    this.getProfileInfo()
    setTimeout(() => {
      this.totalCommentCount = this.appConfigService.getConfig()["resident.grievance-redressal.comments.chars.limit"];
      this.remainingChars = this.totalCommentCount;
      this.phoneCharLimit = this.appConfigService.getConfig()["resident.grievance-redressal.alt-phone.chars.limit"];
      this.emailCharLimit = this.appConfigService.getConfig()["resident.grievance-redressal.alt-email.chars.limit"];
    }, 400);

    this.route.queryParams
      .subscribe(params => {
        this.source1 = params.source1
        this.source2 = params.source2
        this.userFormData.eventId = params.eid;
      }
      );

    const subs = this.autoLogout.currentMessageAutoLogout.subscribe(
      (message) => (this.message2 = message) 
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

  getProfileInfo() {
    this.dataStorageService
      .getProfileInfo(this.userPreferredLangCode)
      .subscribe((response) => {
        if (response["response"]) {
          this.userFormData.name = response["response"]['fullName'];
          this.userFormData.emailId = response["response"]['email'];
          this.userFormData.phoneNo =  response["response"]['phone'];
        }
      });
  }

  onItemSelected(value: any) {
    if (value === "trackservicerequest") {
      this.router.navigateByUrl(`uinservices/trackservicerequest?source=ViewMyHistory&eid=` + this.userFormData.eventId);
    } else {
      this.router.navigate([value])
    }
  }

  captureValue(event: any,keyType:string) {
    this.userFormData[keyType] = event.target.value
  }

  countCharacters(event: any, keyType: string) {
    this.userFormData[keyType] = event.target.value
    let enterdChars = event.target.value.length
    this.remainingChars = this.totalCommentCount - enterdChars
  }

  captureVirtualKeyboard(element: HTMLElement, index: number) {
    this.keyboardRef.instance.setInputInstance(this.attachToElementMesOne._results[index]);
  }

  openKeyboard(inputId:string) {
    if (this.keyboardService.isOpened) {
      this.keyboardService.dismiss();
      this.keyboardRef = undefined;
    } else {
      this.keyboardRef = this.keyboardService.open(defaultJson.keyboardMapping[this.userPreferredLangCode]);
      document.getElementById(inputId).focus();
    }
  }

  get fontSize(): any {
    return this.fontSizeService.fontSize;
  }

  sendGrievanceRedressal() {
    let request = {
      "id": "mosip.resident.grievance.ticket.request",
      "version": "1.0",
      "requesttime": Utils.getCurrentDate(),
      "request": this.userFormData
    }

    this.dataStorageService.sendGrievanceRedressal(request).subscribe(response => {
      if (response["response"]) {
        this.showMessage(response["response"])
        this.router.navigate(["/uinservices/dashboard"])
      } else {
        this.showErrorPopupMessage(response["errors"])
      }
    },
      error => {
        console.log(error)
      })

  }

  showMessage(message: string) {
    this.message = this.popupMessages.genericmessage.grievanceRedressal.successMsg.replace("$ticketId", message["ticketId"])
    const dialogRef = this.dialog.open(DialogComponent, {
      width: '550px',
      data: {
        case: 'MESSAGE',
        title: this.popupMessages.genericmessage.successLabel,
        responseData: message,
        message: this.message,
        endMsg: this.popupMessages.genericmessage.successRemainMsg,
        dearResident: this.popupMessages.genericmessage.dearResident,
        btnTxt: this.popupMessages.genericmessage.successButton,
        isOk:'OK'
      }
    });
    return dialogRef;
  }

  showErrorPopupMessage(message: string) {
    this.errorCode = message[0]["errorCode"];
    if (this.errorCode === "RES-SER-410") {
      let errorMessage = message[0]["message"].split("-")[1].trim();
      this.message = this.popupMessages.serverErrors[this.errorCode][errorMessage];
    } else {
      this.message = this.popupMessages.serverErrors[this.errorCode];
    }

    this.dialog
      .open(DialogComponent, {
        width: '550px',
        data: {
          case: 'MESSAGE',
          title: this.popupMessages.genericmessage.errorLabel,
          message: this.message,
          btnTxt: this.popupMessages.genericmessage.successButton,
          isOk:"OK"
        },
        disableClose: true
      });
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }

  @HostListener("blur", ["$event"])
  @HostListener("focusout", ["$event"])
  private _hideKeyboard() {
    if (this.keyboardService.isOpened) {
      this.keyboardService.dismiss();
    }
  }
}
