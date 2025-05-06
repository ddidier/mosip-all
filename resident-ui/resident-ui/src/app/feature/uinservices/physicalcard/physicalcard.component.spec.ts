import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PhysicalcardComponent } from './physicalcard.component';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { AutoLogoutService } from 'src/app/core/services/auto-logout.service';
import { AppConfigService } from 'src/app/app-config.service';
import { DataStorageService } from 'src/app/core/services/data-storage.service';

describe('PhysicalcardComponent', () => {
  let component: PhysicalcardComponent;
  let fixture: ComponentFixture<PhysicalcardComponent>;

  const mockTranslateService = {
    use: () => {},
    getTranslation: () => {},
  };

  const mockRouter = {
    navigate: () => {},
  };

  const mockAutoLogoutService = {
    currentMessageAutoLogout: {
      subscribe: () => {},
    },
    getValues: () => {},
    setValues: () => {},
    keepWatching: () => {},
    continueWatching: () => {},
  };

  const mockAppConfigService = {};

  const mockDataStorageService = {
    getPartnerDetails: () => {},
  };

  const mockMatDialog = {
    open: () => {},
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PhysicalcardComponent],
      providers: [
        { provide: MatDialog, useValue: mockMatDialog },
        { provide: TranslateService, useValue: mockTranslateService },
        { provide: Router, useValue: mockRouter },
        { provide: AutoLogoutService, useValue: mockAutoLogoutService },
        { provide: AppConfigService, useValue: mockAppConfigService },
        { provide: DataStorageService, useValue: mockDataStorageService },
      ],
    });

    fixture = TestBed.createComponent(PhysicalcardComponent);
    component = fixture.componentInstance;
  });

  it('should create the PhysicalcardComponent', () => {
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
