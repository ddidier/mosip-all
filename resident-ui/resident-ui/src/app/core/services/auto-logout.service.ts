import { Injectable } from '@angular/core';
import { UserIdleService, UserIdleConfig } from 'angular-user-idle';
import { MatDialog } from '@angular/material';
import { DialogComponent } from 'src/app/shared/dialog/dialog.component';
import { BehaviorSubject } from 'rxjs';
import { ConfigService } from 'src/app/core/services/config.service';
import { DataStorageService } from './data-storage.service';
import { Router } from "@angular/router";
import { LogoutService } from './logout.service';
import { AppConfigService } from 'src/app/app-config.service';

/**
 * @description This class is responsible for auto logging out user when he is inactive for a
 *  specified period of time.
 * @author Deepak Choudhary
 * @export
 * @class AutoLogoutService
 */

@Injectable({
  providedIn: 'root'
})
export class AutoLogoutService {
  private messageAutoLogout = new BehaviorSubject({});
  currentMessageAutoLogout = this.messageAutoLogout.asObservable();
  isActive = false;
  activeTimerStart:number = 0;


  timer = new UserIdleConfig();
  languagelabels: any;
  langCode = localStorage.getItem('langCode');

  idle: number;
  timeout: number;
  ping: number;
  dialogref;
  dialogreflogout;

  constructor(
    private userIdle: UserIdleService,
    private dialog: MatDialog,
    private configservice: ConfigService,
    private dataStorageService: DataStorageService,
    private router: Router,
    private logoutService: LogoutService,
    private appConfigService: AppConfigService,
  ) {

  }

  /**
   * @description This method gets value of idle,timeout and ping parameter from config file.
   *
   * @returns void
   * @memberof AutoLogoutService
   */

  getValues(langCode) {
    (this.idle = Number(
      this.appConfigService.getConfig()['mosip.webui.auto.logout.idle']
    )),
      (this.timeout = Number(
        this.appConfigService.getConfig()['mosip.webui.auto.logout.timeout']
      )),
      (this.ping = Number(
        this.appConfigService.getConfig()['mosip.webui.auto.logout.ping']
      ));

    this.dataStorageService
      .getI18NLanguageFiles(langCode)
      .subscribe((response) => {
        this.languagelabels = response['autologout'];
      });
  }

  setisActive(value: boolean) {
    this.isActive = value;
    if (this.isActive) {
      if (this.dialogref) {
        this.dialogref.close();
      }
    }
  }
  
  getisActive() {
    return this.isActive;
  }

  changeMessage(message: object) {
    this.messageAutoLogout.next(message);
  }
  /**
   * @description This method sets value of idle,timeout and ping parameter from config file.
   *
   * @returns void
   * @memberof AutoLogoutService
   */
  setValues() {
    this.timer.idle = this.idle;
    this.timer.ping = this.ping;
    this.timer.timeout = this.timeout;
    this.userIdle.setConfigValues(this.timer);
  }

  /**
   * @description This method is fired when dashboard gets loaded and starts the timer to watch for
   * user idle. onTimerStart() is fired when user idle has been detected for specified time.
   * After that onTimeout() is fired.
   *
   * @returns void
   * @memberof AutoLogoutService
   */


  keepWatching() {
    this.userIdle.startWatching();
    this.changeMessage({ timerFired: true });
    this.userIdle.onTimerStart().subscribe(
      res => {
        console.log(res)
        if(!res && this.activeTimerStart == 0){
          this.openPopUp();
          this.setisActive(false);
          this.onLogOut();
          this.userIdle.resetTimer();
        }else{
          this.activeTimerStart = this.activeTimerStart + 1;
        }
        
        if (this.activeTimerStart == 1) {
          this.setisActive(false);
          this.openPopUp();
        }else {
          if (this.isActive) {
            if (this.dialogref) {
              this.dialogref.close();
            }
          }
        }
      }
    );


    this.userIdle.onTimeout().subscribe(() => {
      if (!this.isActive) {
        this.onLogOut();
      } else {
        this.userIdle.resetTimer();
        this.activeTimerStart = 0;
      }
    });
  }

  public continueWatching() {
    this.userIdle.startWatching();
  }
  /**
   * @description This methoed is used to logged out the user.
   *
   * @returns void
   * @memberof AutoLogoutService
   */
  onLogOut() {
    this.dialogref.close();
    this.dialog.closeAll();
    this.userIdle.stopWatching();
    // this.popUpPostLogOut();
    // this.router.navigate(["dashboard"])
    this.dataStorageService.onLogout().subscribe();
    localStorage.setItem("InactiveLogOut", "true")
    this.logoutService.logout();
  }

  /**
   * @description This methoed opens a pop up when user idle has been detected for given time.id
   * @memberof AutoLogoutService
   */

  openPopUp() {
    this.dialogref = this.dialog.open(DialogComponent, {
      width: '400px',
      data: {
        case: 'LOGOUTWARNINGPOPUP',
        message: this.languagelabels.preview
      }
    });
  }
  // popUpPostLogOut() {
  //   this.dialogreflogout = this.dialog.open(DialogComponent, {
  //     width: '400px',
  //     data: {
  //       case: 'POPUP',
  //       message: this.languagelabels.post
  //     }
  //   });
  // }
}
