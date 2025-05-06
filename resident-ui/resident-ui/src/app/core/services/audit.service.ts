import { Injectable } from '@angular/core';
import { HeaderService } from './header.service';
import { AuditModel } from '../models/audit-model';
import { HttpClient } from '@angular/common/http';
import { RequestModel } from '../models/request.model';
import { AppConfigService } from 'src/app/app-config.service';

@Injectable({
  providedIn: 'root'
})
export class AuditService {
  api:string;
  constructor(
    private headerService: HeaderService,
    private http: HttpClient,
    private appService: AppConfigService
  ) {}

  audit(auditEventId: string, auditEventName: string, moduleId: string, moduleName: string, description: string, id:string) {
    const auditObject = new AuditModel();
    
    if(this.headerService.getUsername()){
      auditObject.createdBy = this.headerService.getUsername();
      auditObject.sessionUserId = this.headerService.getUsername();
      auditObject.sessionUserName = this.headerService.getUsername();
      this.api = '/auth-proxy/audit/log'
    }else{
      auditObject.createdBy = 'Unknown';
      auditObject.sessionUserId = 'UnknownSessionId';
      auditObject.sessionUserName = 'UnknownSessionName';
      this.api = '/proxy/audit/log'
    }

    if(id){
      auditObject.id = id;
    }

    auditObject.auditEventId = auditEventId;
    auditObject.auditEventName = auditEventName;
    auditObject.moduleId = moduleId;
    auditObject.moduleName = moduleName;
    auditObject.description = description;

    this.postAuditLog(auditObject);
  }

  private postAuditLog(auditObject: AuditModel) {
    this.http.post(this.appService.getConfig().baseUrl + this.api, auditObject).subscribe(
      response => {
        console.log(response);
      },
      error => {
        console.log(error);
      }
    );
  }
}
