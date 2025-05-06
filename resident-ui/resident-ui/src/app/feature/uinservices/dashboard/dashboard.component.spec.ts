import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DashboardComponent } from './dashboard.component';
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { TranslateService } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { AutoLogoutService } from 'src/app/core/services/auto-logout.service';
import { LogoutService } from 'src/app/core/services/logout.service';
import { AuditService } from 'src/app/core/services/audit.service';
import { LocationStrategy } from '@angular/common';
import { BreakpointService } from 'src/app/core/services/breakpoint.service';
import { of } from 'rxjs';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let dataStorageServiceSpy: jasmine.SpyObj<DataStorageService>;
  let translateServiceSpy: jasmine.SpyObj<TranslateService>;
  let routerSpy: jasmine.SpyObj<Router>;
  let autoLogoutServiceSpy: jasmine.SpyObj<AutoLogoutService>;
  let logoutServiceSpy: jasmine.SpyObj<LogoutService>;
  let auditServiceSpy: jasmine.SpyObj<AuditService>;
  let locationStrategySpy: jasmine.SpyObj<LocationStrategy>;
  let breakpointServiceSpy: jasmine.SpyObj<BreakpointService>;

  beforeEach(() => {
    dataStorageServiceSpy = jasmine.createSpyObj('DataStorageService', ['getNotificationCount']);
    translateServiceSpy = jasmine.createSpyObj('TranslateService', ['use', 'getTranslation']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    autoLogoutServiceSpy = jasmine.createSpyObj('AutoLogoutService', ['currentMessageAutoLogout', 'getValues', 'setValues', 'keepWatching', 'continueWatching']);
    logoutServiceSpy = jasmine.createSpyObj('LogoutService', ['logout']);
    auditServiceSpy = jasmine.createSpyObj('AuditService', ['audit']);
    locationStrategySpy = jasmine.createSpyObj('LocationStrategy', ['onPopState']);
    breakpointServiceSpy = jasmine.createSpyObj('BreakpointService', ['isBreakpointActive']);

    TestBed.configureTestingModule({
      declarations: [DashboardComponent],
      providers: [
        { provide: DataStorageService, useValue: dataStorageServiceSpy },
        { provide: TranslateService, useValue: translateServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: AutoLogoutService, useValue: autoLogoutServiceSpy },
        { provide: LogoutService, useValue: logoutServiceSpy },
        { provide: AuditService, useValue: auditServiceSpy },
        { provide: LocationStrategy, useValue: locationStrategySpy },
        { provide: BreakpointService, useValue: breakpointServiceSpy },
      ],
    });

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
  });

  it('should create the DashboardComponent', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize component properties on ngOnInit', () => {
    const mockResponse = {
      menuItems: ['item1', 'item2'],
    };
    translateServiceSpy.getTranslation.and.returnValue(of(mockResponse));
    autoLogoutServiceSpy.getValues.and.returnValue(null);

    component.ngOnInit();

    expect(component.menuItems).toEqual(mockResponse.menuItems);
    expect(autoLogoutServiceSpy.setValues).toHaveBeenCalled();
    expect(autoLogoutServiceSpy.keepWatching).toHaveBeenCalled();
  });

  it('should handle PopState correctly', () => {
    // Simulate a popstate event
    const event = { type: 'popstate' };
    window.dispatchEvent(new Event('popstate'));

    expect(auditServiceSpy.audit).toHaveBeenCalled();
    expect(logoutServiceSpy.logout).toHaveBeenCalled();
  });

  // Add more test cases as needed for other methods and scenarios
});
