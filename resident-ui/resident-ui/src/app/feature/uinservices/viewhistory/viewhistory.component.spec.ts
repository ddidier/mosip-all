import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { ViewhistoryComponent } from './viewhistory.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatPaginatorModule } from '@angular/material/paginator';
import { TranslateModule } from '@ngx-translate/core';
import { MatKeyboardService } from 'ngx7-material-keyboard-ios';

describe('ViewhistoryComponent', () => {
  let component: ViewhistoryComponent;
  let fixture: ComponentFixture<ViewhistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ViewhistoryComponent],
      imports: [
        MatDialogModule,
        MatPaginatorModule,
        TranslateModule.forRoot(),
      ],
      providers: [
        MatKeyboardService, // You might need to provide other dependencies as well.
      ],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewhistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  // Add more test cases for component functionality as needed

  afterEach(() => {
    fixture.destroy();
  });
});
