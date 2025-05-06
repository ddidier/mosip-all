import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProgressComponent } from './progress.component';

describe('ProgressComponent', () => {
  let component: ProgressComponent;
  let fixture: ComponentFixture<ProgressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProgressComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProgressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the ProgressComponent', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with progress set to 0', () => {
    expect(component.progress).toBe(0);
  });

  it('should display the progress value', () => {
    const element: HTMLElement = fixture.nativeElement;
    const progressElement = element.querySelector('.progress');
    expect(progressElement.textContent).toContain('0%'); // Assuming you have an element with the class 'progress' to display the progress value
  });

  it('should update the progress value when @Input is changed', () => {
    component.progress = 50;
    fixture.detectChanges();
    const element: HTMLElement = fixture.nativeElement;
    const progressElement = element.querySelector('.progress');
    expect(progressElement.textContent).toContain('50%');
  });

  // Add more test cases as needed for other scenarios
});
