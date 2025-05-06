import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ErrorComponent } from './error.component';
import { LoginRedirectService } from 'src/app/core/services/loginredirect.service';
import { TranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

describe('ErrorComponent', () => {
  let component: ErrorComponent;
  let fixture: ComponentFixture<ErrorComponent>;
  let redirectService: LoginRedirectService;
  let translateService: TranslateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ErrorComponent],
      providers: [
        {
          provide: LoginRedirectService,
          useValue: {
            redirect: jasmine.createSpy('redirect'),
          },
        },
        {
          provide: TranslateService,
          useValue: {
            getTranslation: jasmine.createSpy('getTranslation').and.returnValue(of({})),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(ErrorComponent);
    component = fixture.componentInstance;
    redirectService = TestBed.get(LoginRedirectService);
    translateService = TestBed.get(TranslateService);
  });

  it('should create the ErrorComponent', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize component properties on ngOnInit', () => {
    spyOn(translateService, 'getTranslation').and.returnValue(of({ genericmessage: 'some message' }));
    component.ngOnInit();
    expect(component.longJson).toBeDefined();
    expect(component.redirectUrl).toBeDefined();
  });

  it('should call login() method to redirect', () => {
    spyOn(redirectService, 'redirect');
    component.login();
    expect(redirectService.redirect).toHaveBeenCalledWith(component.redirectUrl);
  });

  // Add more test cases as needed for other scenarios
});
