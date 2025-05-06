import { LoginRedirectService } from './loginredirect.service';
import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AppConfigService } from 'src/app/app-config.service';

@Injectable({
  providedIn: 'root'
})
export class LogoutService {
  constructor(
    private http: HttpClient,
    private router: Router,
    private redirectService: LoginRedirectService,
    private appService: AppConfigService,
  ) {}

  logout() {
    let url = window.location.href
    if(url.split("#")[1] !== "/dashboard"){
      url = url.replace(url.split("#")[1], "/dashboard");
    }
    window.location.href = `${this.appService.getConfig().baseUrl}/logout/user?redirecturi=`+btoa(window.location.href.split("#")[0]+"#/dashboard");
    localStorage.setItem("logOut",'true')
  }
}
