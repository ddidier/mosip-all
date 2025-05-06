import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HeaderComponent } from './header.component';
import { RouterTestingModule } from '@angular/router/testing';
import { TranslateService } from '@ngx-translate/core';
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { DomSanitizer } from '@angular/platform-browser';
import { LogoutService } from './../../core/services/logout.service';
import { HeaderService } from 'src/app/core/services/header.service';
import { AuditService } from 'src/app/core/services/audit.service';
import { MatDialog } from '@angular/material';
import { InteractionService } from 'src/app/core/services/interaction.service';
import { of } from 'rxjs';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  // Mock services and dependencies
  const translateServiceMock = {
    use: () => {},
    getTranslation: () => of({}),
  };

  const dataStorageServiceMock = {
    getProfileInfo: () => of({}),
    getNotificationCount: () => of({}),
    getNotificationData: () => of({}),
    updateNotificationTime: () => of({}),
  };

  const sanitizerMock = {
    bypassSecurityTrustResourceUrl: (url: string) => url,
  };

  const logoutServiceMock = {
    logout: () => {},
  };

  const headerServiceMock = {
    setUsername: () => {},
  };

  const auditServiceMock = {
    audit: () => {},
  };

  const dialogMock = {
    open: () => ({ afterClosed: () => of({}) }),
  };

  const interactionServiceMock = {
    getClickEvent: () => of('logOutBtn'),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HeaderComponent],
      imports: [RouterTestingModule],
      providers: [
        { provide: TranslateService, useValue: translateServiceMock },
        { provide: DataStorageService, useValue: dataStorageServiceMock },
        { provide: DomSanitizer, useValue: sanitizerMock },
        { provide: LogoutService, useValue: logoutServiceMock },
        { provide: HeaderService, useValue: headerServiceMock },
        { provide: AuditService, useValue: auditServiceMock },
        { provide: MatDialog, useValue: dialogMock },
        { provide: InteractionService, useValue: interactionServiceMock },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should call onScroll when scrolling down', () => {
    window.dispatchEvent(new Event('scroll'));
  });

  it('should call onScrollUp when scrolling up', () => {
    window.dispatchEvent(new Event('scroll'));
  });

  it('should call doLogout when logOutBtn is clicked', () => {
    spyOn(logoutServiceMock, 'logout');
    component.ngOnInit(); // Simulate ngOnInit to subscribe to the clickEventObservable
    expect(logoutServiceMock.logout).toHaveBeenCalled();
  });

  // Add more test cases as needed for other methods and scenarios
});
