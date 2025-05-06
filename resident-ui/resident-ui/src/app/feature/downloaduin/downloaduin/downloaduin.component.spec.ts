import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { of, throwError } from 'rxjs';
import { saveAs } from 'file-saver';
import { DownloadUinComponent } from './downloaduin.component';
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { AppConfigService } from 'src/app/app-config.service';
import { AuditService } from 'src/app/core/services/audit.service';

describe('DownloadUinComponent', () => {
  let component: DownloadUinComponent;
  let fixture: ComponentFixture<DownloadUinComponent>;
  let dataStorageService: DataStorageService;
  let appConfigService: AppConfigService;
  let auditService: AuditService;
  let dialog: MatDialog;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DownloadUinComponent],
      imports: [RouterTestingModule, HttpClientTestingModule, MatDialogModule],
      providers: [
        {
          provide: TranslateService,
          useValue: {
            use: jasmine.createSpy('use'),
            getTranslation: jasmine.createSpy('getTranslation').and.returnValue(of({})),
          },
        },
        {
          provide: MatDialog,
          useValue: {
            open: jasmine.createSpy('open').and.returnValue({
              afterClosed: () => of({}),
            }),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(DownloadUinComponent);
    component = fixture.componentInstance;
    dataStorageService = TestBed.get(DataStorageService);
    appConfigService = TestBed.get(AppConfigService);
    auditService = TestBed.get(AuditService);
    dialog = TestBed.get(MatDialog);
  });

  it('should create the DownloadUinComponent', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize component properties on ngOnInit', () => {
    spyOn(appConfigService, 'getConfig').and.returnValue({
      'mosip.kernel.otp.expiry-time': 600, // 10 minutes
    });

    component.ngOnInit();

    expect(component.downloadUinData).toBeDefined();
    expect(component.popupMessages).toBeDefined();
    expect(component.otpTimeMinutes).toBe(10); // 10 minutes
    expect(component.interval).toBeDefined();
  });

  it('should handle item selection correctly', () => {
    const routerSpy = spyOn(component['router'], 'navigate');
    component.onItemSelected('home');
    expect(routerSpy).toHaveBeenCalledWith(['dashboard']);

    component.onItemSelected('back');
    expect(routerSpy).toHaveBeenCalledWith(['getuin']);
    expect(component.displaySeconds).toBe('00');
  });

  // Add more test cases as needed for other scenarios
});
