import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LockunlockauthComponent } from './lockunlockauth.component';
import { RouterTestingModule } from '@angular/router/testing';
import { TranslateService, TranslateModule } from '@ngx-translate/core';
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { AppConfigService } from 'src/app/app-config.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { InteractionService } from 'src/app/core/services/interaction.service';
import { AuditService } from 'src/app/core/services/audit.service';
import { AutoLogoutService } from 'src/app/core/services/auto-logout.service';
import { BreakpointService } from 'src/app/core/services/breakpoint.service';
import { of } from 'rxjs';

describe('LockunlockauthComponent', () => {
  let component: LockunlockauthComponent;
  let fixture: ComponentFixture<LockunlockauthComponent>;
  let dataStorageServiceSpy: jasmine.SpyObj<DataStorageService>;
  let translateServiceSpy: jasmine.SpyObj<TranslateService>;
  let appConfigServiceSpy: jasmine.SpyObj<AppConfigService>;
  let dialogSpy: jasmine.SpyObj<MatDialog>;
  let interactionServiceSpy: jasmine.SpyObj<InteractionService>;
  let auditServiceSpy: jasmine.SpyObj<AuditService>;
  let autoLogoutServiceSpy: jasmine.SpyObj<AutoLogoutService>;
  let breakpointServiceSpy: jasmine.SpyObj<BreakpointService>;

  beforeEach(() => {
    dataStorageServiceSpy = jasmine.createSpyObj('DataStorageService', [
      'getAuthlockStatus',
      'updateAuthlockStatus'
    ]);
    translateServiceSpy = jasmine.createSpyObj('TranslateService', ['use', 'getTranslation']);
    appConfigServiceSpy = jasmine.createSpyObj('AppConfigService', ['getConfig']);
    dialogSpy = jasmine.createSpyObj('MatDialog', ['open']);
    interactionServiceSpy = jasmine.createSpyObj('InteractionService', ['getClickEvent']);
    auditServiceSpy = jasmine.createSpyObj('AuditService', ['audit']);
    autoLogoutServiceSpy = jasmine.createSpyObj('AutoLogoutService', [
      'currentMessageAutoLogout',
      'getValues',
      'setValues',
      'keepWatching',
      'continueWatching'
    ]);
    breakpointServiceSpy = jasmine.createSpyObj('BreakpointService', ['isBreakpointActive']);

    TestBed.configureTestingModule({
      declarations: [LockunlockauthComponent],
      imports: [RouterTestingModule, TranslateModule.forRoot(), MatDialogModule],
      providers: [
        { provide: DataStorageService, useValue: dataStorageServiceSpy },
        { provide: TranslateService, useValue: translateServiceSpy },
        { provide: AppConfigService, useValue: appConfigServiceSpy },
        { provide: MatDialog, useValue: dialogSpy },
        { provide: InteractionService, useValue: interactionServiceSpy },
        { provide: AuditService, useValue: auditServiceSpy },
        { provide: AutoLogoutService, useValue: autoLogoutServiceSpy },
        { provide: BreakpointService, useValue: breakpointServiceSpy }
      ]
    });

    fixture = TestBed.createComponent(LockunlockauthComponent);
    component = fixture.componentInstance;
  });

  it('should create the LockunlockauthComponent', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize component properties on ngOnInit', () => {
    const mockResponse = {
      InfomationContent: {
        secureMyID: 'Secure My ID Message'
      },
      genericmessage: {
        secureMyId: {
          successMsg: 'Secure My ID Success Message'
        },
        warningLabel: 'Warning Label',
        yesButton: 'Yes',
        noButton: 'No'
      },
      serverErrors: {
        'error-code-1': 'Error Message 1',
        'error-code-2': 'Error Message 2'
      }
    };
    const mockAppConfig = {
      'auth.types.allowed': 'type1-subtype1,type2-subtype2',
      'resident.vid.version.new': '1.0'
    };

    translateServiceSpy.getTranslation.and.returnValue(of(mockResponse));
    appConfigServiceSpy.getConfig.and.returnValue(mockAppConfig);
    autoLogoutServiceSpy.getValues.and.returnValue(null);

    component.ngOnInit();

    expect(component.langJSON).toEqual(mockResponse);
    expect(component.popupMessages).toEqual(mockResponse);
    expect(component.infoMsg).toEqual(mockResponse.InfomationContent.secureMyID);
    expect(component.authlist).toBeTruthy(); // You can add more specific assertions
  });

  // Add more test cases as needed for other methods and scenarios
});
