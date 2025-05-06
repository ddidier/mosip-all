import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GrievanceComponent } from './grievance.component';
import { RouterTestingModule } from '@angular/router/testing';
import { TranslateService, TranslateModule } from '@ngx-translate/core';
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { AppConfigService } from 'src/app/app-config.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { AutoLogoutService } from 'src/app/core/services/auto-logout.service';
import { MatKeyboardService } from 'ngx7-material-keyboard-ios';
import { of } from 'rxjs';

describe('GrievanceComponent', () => {
  let component: GrievanceComponent;
  let fixture: ComponentFixture<GrievanceComponent>;
  let dataStorageServiceSpy: jasmine.SpyObj<DataStorageService>;
  let translateServiceSpy: jasmine.SpyObj<TranslateService>;
  let appConfigServiceSpy: jasmine.SpyObj<AppConfigService>;
  let dialogSpy: jasmine.SpyObj<MatDialog>;
  let autoLogoutServiceSpy: jasmine.SpyObj<AutoLogoutService>;
  let matKeyboardServiceSpy: jasmine.SpyObj<MatKeyboardService>;

  beforeEach(() => {
    dataStorageServiceSpy = jasmine.createSpyObj('DataStorageService', ['getProfileInfo', 'sendGrievanceRedressal']);
    translateServiceSpy = jasmine.createSpyObj('TranslateService', ['use', 'getTranslation']);
    appConfigServiceSpy = jasmine.createSpyObj('AppConfigService', ['getConfig']);
    dialogSpy = jasmine.createSpyObj('MatDialog', ['open']);
    autoLogoutServiceSpy = jasmine.createSpyObj('AutoLogoutService', ['currentMessageAutoLogout', 'getValues', 'setValues', 'keepWatching', 'continueWatching']);
    matKeyboardServiceSpy = jasmine.createSpyObj('MatKeyboardService', ['open', 'dismiss']);

    TestBed.configureTestingModule({
      declarations: [GrievanceComponent],
      imports: [
        RouterTestingModule,
        TranslateModule.forRoot(),
        MatDialogModule
      ],
      providers: [
        { provide: DataStorageService, useValue: dataStorageServiceSpy },
        { provide: TranslateService, useValue: translateServiceSpy },
        { provide: AppConfigService, useValue: appConfigServiceSpy },
        { provide: MatDialog, useValue: dialogSpy },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: convertToParamMap({ eid: '123', source1: 'source1', source2: 'source2' }) } } },
        { provide: AutoLogoutService, useValue: autoLogoutServiceSpy },
        { provide: MatKeyboardService, useValue: matKeyboardServiceSpy }
      ],
    });

    fixture = TestBed.createComponent(GrievanceComponent);
    component = fixture.componentInstance;
  });

  it('should create the GrievanceComponent', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize component properties on ngOnInit', () => {
    const mockResponse = {
      grievanceRedressal: { /* your mock data here */ },
    };
    const mockAppConfig = {
      'resident.grievance-redressal.comments.chars.limit': 100,
      'resident.grievance-redressal.alt-phone.chars.limit': 50,
      'resident.grievance-redressal.alt-email.chars.limit': 100,
    };

    translateServiceSpy.getTranslation.and.returnValue(of(mockResponse));
    appConfigServiceSpy.getConfig.and.returnValue(mockAppConfig);
    autoLogoutServiceSpy.getValues.and.returnValue(null);

    component.ngOnInit();

    expect(component.grievanceData).toEqual(mockResponse.grievanceRedressal);
    expect(component.totalCommentCount).toEqual(mockAppConfig['resident.grievance-redressal.comments.chars.limit']);
    expect(component.remainingChars).toEqual(mockAppConfig['resident.grievance-redressal.comments.chars.limit']);
    expect(component.phoneCharLimit).toEqual(mockAppConfig['resident.grievance-redressal.alt-phone.chars.limit']);
    expect(component.emailCharLimit).toEqual(mockAppConfig['resident.grievance-redressal.alt-email.chars.limit']);
  });

  it('should capture values and update userFormData', () => {
    const event = { target: { value: 'testValue' } };
    component.captureValue(event, 'name');

    expect(component.userFormData.name).toBe('testValue');
  });

  it('should count remaining characters', () => {
    const event = { target: { value: 'testValue' } };
    component.countCharacters(event, 'message');

    expect(component.remainingChars).toBe(component.totalCommentCount - event.target.value.length);
  });

  // Add more test cases as needed for other methods and scenarios
});
