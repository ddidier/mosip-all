import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PDFViewerComponent } from './pdf-viewer.component';
import { EventEmitter } from '@angular/core';

describe('PDFViewerComponent', () => {
  let component: PDFViewerComponent;
  let fixture: ComponentFixture<PDFViewerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PDFViewerComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PDFViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the PDFViewerComponent', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with default values', () => {
    expect(component.pageNumber).toEqual(1);
    expect(component.zoom).toEqual(1.0);
    expect(component.bgColor).toEqual('rgba(0,0,0,0)');
  });

  it('should emit PdfDocumentLoad event after ngOnInit', async () => {
    spyOn(component.PdfDocumentLoad, 'emit');
    component.pdfSrc = 'path/to/pdf.pdf';
    await component.ngOnInit();
    expect(component.PdfDocumentLoad.emit).toHaveBeenCalledWith({
      numPages: component.getNumPages(),
    });
  });

  it('should handle ngOnDestroy correctly', () => {
    component._pdfDocument = {}; // Simulate that _pdfDocument exists
    spyOn(component._pdfDocument, 'destroy');
    component.ngOnDestroy();
    expect(component._pdfDocument.destroy).toHaveBeenCalled();
    expect(component._pdfDocument).toBeNull();
  });

  it('should return true for isValidPageNumberRequest if page number is valid', () => {
    component._pdfDocument = {
      _pdfInfo: {
        numPages: 5, // Set the total number of pages
      },
    };
    expect(component.isValidPageNumberRequest(3)).toBe(true);
  });

  it('should return false for isValidPageNumberRequest if page number is invalid', () => {
    component._pdfDocument = {
      _pdfInfo: {
        numPages: 5, // Set the total number of pages
      },
    };
    expect(component.isValidPageNumberRequest(0)).toBe(false);
    expect(component.isValidPageNumberRequest(6)).toBe(false);
  });

  // Add more test cases as needed for other methods and scenarios
});
