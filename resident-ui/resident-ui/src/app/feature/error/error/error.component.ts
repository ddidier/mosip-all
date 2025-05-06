import { Component, OnInit } from '@angular/core';
import { LoginRedirectService } from 'src/app/core/services/loginredirect.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {
  redirectUrl:string;
  longJson:any;
  constructor(
    private redirectService: LoginRedirectService,
    private translateService: TranslateService
  ) { }

  ngOnInit() {
    this.translateService
    .getTranslation(localStorage.getItem("langCode"))
    .subscribe(response => {
      this.longJson = response.genericmessage;
      console.log(this.longJson)
    });
   let urlData = window.location.href.split('#', )
   this.redirectUrl = urlData[0] + "/#/" + "uinservices/dashboard"
  }
  login(){
    this.redirectService.redirect(this.redirectUrl);
  }
}