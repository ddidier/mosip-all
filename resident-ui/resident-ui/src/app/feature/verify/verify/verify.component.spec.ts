import { ComponentFixture, TestBed, tick, fakeAsync } from '@angular/core/testing';
import { VerifyComponent } from './verify.component';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { AppConfigService } from 'src/app/app-config.service';
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { MatDialog } from '@angular/material';
import { AuditService } from 'src/app/core/services/audit.service';
import { BreakpointObserver } from '@angular/cdk/layout';
import { of } from 'rxjs';

describe('VerifyComponent', () => {
  let component: VerifyComponent;
  let fixture: ComponentFixture<VerifyComponent>;
  let routerSpy: jasmine.SpyObj<Router>;
  let translateService: jasmine.SpyObj<TranslateService>;
  let appConfigServiceSpy: jasmine.SpyObj<AppConfigService>;
  let dataStorageServiceSpy: jasmine.SpyObj<DataStorageService>;
  let dialogSpy: jasmine.SpyObj<MatDialog>;
  let auditServiceSpy: jasmine.SpyObj<AuditService>;
  let breakpointObserverSpy: jasmine.SpyObj<BreakpointObserver>;

  beforeEach(() => {
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    translateService = jasmine.createSpyObj('TranslateService', ['getTranslation']);
    appConfigServiceSpy = jasmine.createSpyObj('AppConfigService', ['getConfig']);
    dataStorageServiceSpy = jasmine.createSpyObj('DataStorageService', ['generateOTP', 'isVerified', 'verifyOTP']);
    dialogSpy = jasmine.createSpyObj('MatDialog', ['open']);
    auditServiceSpy = jasmine.createSpyObj('AuditService', ['audit']);
    breakpointObserverSpy = jasmine.createSpyObj('BreakpointObserver', ['observe']);

    TestBed.configureTestingModule({
      declarations: [VerifyComponent],
      providers: [
        { provide: Router, useValue: routerSpy },
        { provide: TranslateService, useValue: translateService },
        { provide: AppConfigService, useValue: appConfigServiceSpy },
        { provide: DataStorageService, useValue: dataStorageServiceSpy },
        { provide: MatDialog, useValue: dialogSpy },
        { provide: AuditService, useValue: auditServiceSpy },
        { provide: BreakpointObserver, useValue: breakpointObserverSpy },
      ],
    });

    fixture = TestBed.createComponent(VerifyComponent);
    component = fixture.componentInstance;
  });

  it('should create the VerifyComponent', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize component properties on ngOnInit', () => {
    const mockResponse = {
      verifyuinvid: {},
      InfomationContent: {
        verifyChannel: 'Some info',
      },
    };
    translateService.getTranslation.and.returnValue(of(mockResponse));
    appConfigServiceSpy.getConfig.and.returnValue({});
    
    component.ngOnInit();
    
    expect(component.verifyChannelData).toBeDefined();
    expect(component.popupMessages).toBeDefined();
    expect(component.infoText).toBeDefined();
  });

  it('should handle radioChange correctly', () => {
    component.radioChange('PHONE');
    expect(component.otpChannel).toEqual(['PHONE']);
    expect(component.numBtnColors).toBe("#03A64A");
    expect(component.emailBtnColors).toBe("#909090");
    expect(component.phoneIcon).toBeFalsy();
    expect(component.mailIcon).toBeFalsy();
    
    component.radioChange('EMAIL');
    expect(component.otpChannel).toEqual(['EMAIL']);
    expect(component.numBtnColors).toBe("#909090");
    expect(component.emailBtnColors).toBe("#03A64A");
    expect(component.phoneIcon).toBeFalsy();
    expect(component.mailIcon).toBeFalsy();
  });

  it('should handle getCaptchaToken correctly', () => {
    component.channelSelected = true;
    component.individualId = '1234567890';
    component.captchaChecked = true;

    component.getCaptchaToken(null);
    expect(component.disableSendOtp).toBeFalsy();

    component.channelSelected = false;
    component.individualId = '1234567890';
    component.captchaChecked = true;

    component.getCaptchaToken(null);
    expect(component.disableSendOtp).toBeFalsy();
  });

  // Add more test cases as needed for other methods and scenarios
});
