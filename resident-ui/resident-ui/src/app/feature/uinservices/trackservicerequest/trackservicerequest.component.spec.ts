import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { TrackservicerequestComponent } from './trackservicerequest.component';
import { MatDialog } from '@angular/material/dialog';
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { TranslateService } from '@ngx-translate/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AppConfigService } from 'src/app/app-config.service';
import { AutoLogoutService } from 'src/app/core/services/auto-logout.service';
import { MatKeyboardService } from 'ngx7-material-keyboard-ios';
import { of } from 'rxjs';

describe('TrackservicerequestComponent', () => {
  let component: TrackservicerequestComponent;
  let fixture: ComponentFixture<TrackservicerequestComponent>;
  let matDialog: jasmine.SpyObj<MatDialog>;
  let dataStorageService: jasmine.SpyObj<DataStorageService>;
  let translateService: jasmine.SpyObj<TranslateService>;
  let route: jasmine.SpyObj<ActivatedRoute>;
  let router: jasmine.SpyObj<Router>;
  let appConfigService: jasmine.SpyObj<AppConfigService>;
  let autoLogoutService: jasmine.SpyObj<AutoLogoutService>;
  let keyboardService: jasmine.SpyObj<MatKeyboardService>;

  beforeEach(() => {
    const matDialogSpy = jasmine.createSpyObj('MatDialog', ['open']);
    const dataStorageServiceSpy = jasmine.createSpyObj('DataStorageService', ['getEIDStatus', 'downloadAcknowledgement']);
    const translateServiceSpy = jasmine.createSpyObj('TranslateService', ['use', 'getTranslation']);
    const routeSpy = jasmine.createSpyObj('ActivatedRoute', ['queryParams']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigateByUrl', 'navigate']);
    const appConfigServiceSpy = jasmine.createSpyObj('AppConfigService', ['getConfig']);
    const autoLogoutServiceSpy = jasmine.createSpyObj('AutoLogoutService', ['currentMessageAutoLogout', 'getValues', 'setValues', 'keepWatching', 'continueWatching']);
    const keyboardServiceSpy = jasmine.createSpyObj('MatKeyboardService', ['isOpened', 'open', 'dismiss']);

    TestBed.configureTestingModule({
      declarations: [TrackservicerequestComponent],
      providers: [
        { provide: MatDialog, useValue: matDialogSpy },
        { provide: DataStorageService, useValue: dataStorageServiceSpy },
        { provide: TranslateService, useValue: translateServiceSpy },
        { provide: ActivatedRoute, useValue: routeSpy },
        { provide: Router, useValue: routerSpy },
        { provide: AppConfigService, useValue: appConfigServiceSpy },
        { provide: AutoLogoutService, useValue: autoLogoutServiceSpy },
        { provide: MatKeyboardService, useValue: keyboardServiceSpy },
      ],
    });

    fixture = TestBed.createComponent(TrackservicerequestComponent);
    component = fixture.componentInstance;
    matDialog = TestBed.get(MatDialog) as jasmine.SpyObj<MatDialog>;
    dataStorageService = TestBed.get(DataStorageService) as jasmine.SpyObj<DataStorageService>;
    translateService = TestBed.get(TranslateService) as jasmine.SpyObj<TranslateService>;
    route = TestBed.get(ActivatedRoute) as jasmine.SpyObj<ActivatedRoute>;
    router = TestBed.get(Router) as jasmine.SpyObj<Router>;
    appConfigService = TestBed.get(AppConfigService) as jasmine.SpyObj<AppConfigService>;
    autoLogoutService = TestBed.get(AutoLogoutService) as jasmine.SpyObj<AutoLogoutService>;
    keyboardService = TestBed.get(MatKeyboardService) as jasmine.SpyObj<MatKeyboardService>;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  // Add more test cases based on the component's behavior

  afterEach(() => {
    fixture.destroy();
  });
});
