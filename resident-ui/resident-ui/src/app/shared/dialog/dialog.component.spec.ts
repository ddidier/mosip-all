import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';
import { DialogComponent } from './dialog.component';

describe('DialogComponent', () => {
  let component: DialogComponent;
  let fixture: ComponentFixture<DialogComponent>;
  let mockMatDialogRef: MatDialogRef<DialogComponent>;
  let mockMatDialog: MatDialog;
  let mockRouter: Router;
  let mockTranslateService: TranslateService;

  const mockDialogData = {
    title: 'Test',
    // Add any other properties you need for testing here
  };

  beforeEach(() => {
    mockMatDialogRef = jasmine.createSpyObj(['close']);
    mockMatDialog = jasmine.createSpyObj(['closeAll']);
    mockRouter = jasmine.createSpyObj(['navigateByUrl']);

    TestBed.configureTestingModule({
      declarations: [DialogComponent],
      providers: [
        { provide: MatDialogRef, useValue: mockMatDialogRef },
        { provide: MAT_DIALOG_DATA, useValue: mockDialogData },
        { provide: MatDialog, useValue: mockMatDialog },
        { provide: Router, useValue: mockRouter },
        { provide: TranslateService, useValue: mockTranslateService },
      ],
    });

    fixture = TestBed.createComponent(DialogComponent);
    component = fixture.componentInstance;
  });

  it('should create the DialogComponent', () => {
    expect(component).toBeTruthy();
  });

  it('should close the dialog when calling onNoClick', () => {
    component.onNoClick();
    expect(mockMatDialogRef.close).toHaveBeenCalled();
  });

  it('should navigate to the specified URL when calling viewDetails', () => {
    const eventId = '12345';
    component.viewDetails(eventId);
    expect(mockRouter.navigateByUrl).toHaveBeenCalledWith(`uinservices/trackservicerequest?eid=${eventId}`);
  });

  it('should toggle isChecked property when calling agreeConditions', () => {
    const initialCheckedState = component.isChecked;
    component.agreeConditions();
    expect(component.isChecked).toBe(!initialCheckedState);
  });

  it('should set otpTimeMinutes and disableInput properties when calling setOtpTime', () => {
    component.appConfigService = {
      getConfig: () => ({ 'mosip.kernel.otp.expiry-time': 120 }),
    } as any;

    component.setOtpTime();

    expect(component.otpTimeMinutes).toBe(2);
    expect(component.disableInput).toBe(false);
  });

  // Add more test cases as needed for other methods and properties

});
