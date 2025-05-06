import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RevokevidComponent } from './revokevid.component';
import { MatDialog } from '@angular/material/dialog';
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { TranslateService } from '@ngx-translate/core';
import { AutoLogoutService } from 'src/app/core/services/auto-logout.service';
import { BreakpointService } from 'src/app/core/services/breakpoint.service';
import { InteractionService } from 'src/app/core/services/interaction.service';
import { AuditService } from 'src/app/core/services/audit.service';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

describe('RevokevidComponent', () => {
  let component: RevokevidComponent;
  let fixture: ComponentFixture<RevokevidComponent>;
  let dataStorageService: jasmine.SpyObj<DataStorageService>;
  let matDialog: jasmine.SpyObj<MatDialog>;
  let translateService: jasmine.SpyObj<TranslateService>;
  let autoLogoutService: jasmine.SpyObj<AutoLogoutService>;
  let breakpointService: jasmine.SpyObj<BreakpointService>;
  let interactionService: jasmine.SpyObj<InteractionService>;
  let auditService: jasmine.SpyObj<AuditService>;

  beforeEach(() => {
    const dataStorageServiceSpy = jasmine.createSpyObj('DataStorageService', ['getVIDs', 'getPolicy', 'generateVID', 'revokeVID']);
    const matDialogSpy = jasmine.createSpyObj('MatDialog', ['open']);
    const translateServiceSpy = jasmine.createSpyObj('TranslateService', ['use', 'getTranslation']);
    const autoLogoutServiceSpy = jasmine.createSpyObj('AutoLogoutService', ['currentMessageAutoLogout', 'getValues', 'setValues', 'keepWatching', 'continueWatching']);
    const breakpointServiceSpy = jasmine.createSpyObj('BreakpointService', ['isBreakpointActive']);
    const interactionServiceSpy = jasmine.createSpyObj('InteractionService', ['getClickEvent']);
    const auditServiceSpy = jasmine.createSpyObj('AuditService', ['audit']);

    TestBed.configureTestingModule({
      declarations: [RevokevidComponent],
      imports: [RouterTestingModule],
      providers: [
        { provide: DataStorageService, useValue: dataStorageServiceSpy },
        { provide: MatDialog, useValue: matDialogSpy },
        { provide: TranslateService, useValue: translateServiceSpy },
        { provide: AutoLogoutService, useValue: autoLogoutServiceSpy },
        { provide: BreakpointService, useValue: breakpointServiceSpy },
        { provide: InteractionService, useValue: interactionServiceSpy },
        { provide: AuditService, useValue: auditServiceSpy },
      ],
    });

    fixture = TestBed.createComponent(RevokevidComponent);
    component = fixture.componentInstance;
    dataStorageService = TestBed.get(DataStorageService) as jasmine.SpyObj<DataStorageService>;
    matDialog = TestBed.get(MatDialog) as jasmine.SpyObj<MatDialog>;
    translateService = TestBed.get(TranslateService) as jasmine.SpyObj<TranslateService>;
    autoLogoutService = TestBed.get(AutoLogoutService) as jasmine.SpyObj<AutoLogoutService>;
    breakpointService = TestBed.get(BreakpointService) as jasmine.SpyObj<BreakpointService>;
    interactionService = TestBed.get(InteractionService) as jasmine.SpyObj<InteractionService>;
    auditService = TestBed.get(AuditService) as jasmine.SpyObj<AuditService>;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  // Add more test cases based on the component's behavior

  afterEach(() => {
    fixture.destroy();
  });
});
