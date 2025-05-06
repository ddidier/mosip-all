import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { CaptchaComponent } from './captcha.component';

describe('CaptchaComponent', () => {
  let component: CaptchaComponent;
  let fixture: ComponentFixture<CaptchaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CaptchaComponent],
      imports: [TranslateModule.forRoot()],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { queryParams: {} } }, // You may need to customize this based on your needs
        },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CaptchaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should emit captcha response', () => {
    spyOn(component.captchaEvent, 'emit');
    const captchaResponse = 'testResponse';
    component.recaptcha(captchaResponse);
    expect(component.captchaEvent.emit).toHaveBeenCalledWith(captchaResponse);
  });

  it('should handle recaptcha error', () => {
    spyOn(window, 'alert');
    const error = 'testError';
    component.recaptchaError(error);
    expect(window.alert).toHaveBeenCalledWith(error);
  });

  it('should handle resetCaptcha', () => {
    component.resetCaptcha = true;
    spyOn(component, 'handleReset');
    component.ngOnChanges();
    expect(component.handleReset).toHaveBeenCalled();
  });
});
