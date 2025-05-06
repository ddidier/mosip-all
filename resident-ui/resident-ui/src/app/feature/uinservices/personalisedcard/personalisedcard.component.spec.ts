import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';
import { AutoLogoutService } from 'src/app/core/services/auto-logout.service';
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { InteractionService } from 'src/app/core/services/interaction.service';
import { AuditService } from 'src/app/core/services/audit.service';
import { BreakpointService } from 'src/app/core/services/breakpoint.service';
import { AppConfigService } from 'src/app/app-config.service';
import { DialogComponent } from 'src/app/shared/dialog/dialog.component';
import { PersonalisedcardComponent } from './personalisedcard.component';

describe('PersonalisedcardComponent', () => {
  let component: PersonalisedcardComponent;
  let fixture: ComponentFixture<PersonalisedcardComponent>;

  const mockTranslateService = {
    use: () => {},
    getTranslation: () => of({}),
  };

  const mockRouter = {
    navigate: () => {},
  };

  const mockAutoLogoutService = {
    currentMessageAutoLogout: of({}),
    getValues: () => {},
    setValues: () => {},
    keepWatching: () => {},
    continueWatching: () => {},
  };

  const mockDataStorageService = {
    getUserInfo: () => of({}),
    getMappingData: () => of({}),
    getUpdateMyDataSchema: () => of({}),
    convertpdf: () => of({}),
  };

  const mockMatDialog = {
    open: () => {},
  };

  const mockAuditService = {
    audit: () => {},
  };

  const mockBreakpointService = {
    isBreakpointActive: () => of('medium'), // Adjust as needed
  };

  const mockAppConfigService = {
    getConfig: () => ({}),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PersonalisedcardComponent],
      providers: [
        { provide: MatDialog, useValue: mockMatDialog },
        { provide: TranslateService, useValue: mockTranslateService },
        { provide: Router, useValue: mockRouter },
        { provide: AutoLogoutService, useValue: mockAutoLogoutService },
        { provide: DataStorageService, useValue: mockDataStorageService },
        { provide: InteractionService, useValue: {} },
        { provide: AuditService, useValue: mockAuditService },
        { provide: BreakpointService, useValue: mockBreakpointService },
        { provide: AppConfigService, useValue: mockAppConfigService },
      ],
    });

    fixture = TestBed.createComponent(PersonalisedcardComponent);
    component = fixture.componentInstance;
  });

  it('should create the PersonalisedcardComponent', () => {
    expect(component).toBeTruthy();
  });

  it('should call ngOnInit', () => {
    spyOn(component, 'ngOnInit').and.callThrough();
    component.ngOnInit();
    expect(component.ngOnInit).toHaveBeenCalled();
  });

  it('should call ngOnDestroy', () => {
    spyOn(component, 'ngOnDestroy').and.callThrough();
    component.ngOnDestroy();
    expect(component.ngOnDestroy).toHaveBeenCalled();
  });

  // Add more test cases for your component methods as needed
});
