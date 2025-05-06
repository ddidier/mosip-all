import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TranslateService } from '@ngx-translate/core';
import { of, throwError } from 'rxjs';
import { saveAs } from 'file-saver';
import { DocumentComponent } from './document.component';
import { DataStorageService } from 'src/app/core/services/data-storage.service';
import { AuditService } from 'src/app/core/services/audit.service';

describe('DocumentComponent', () => {
  let component: DocumentComponent;
  let fixture: ComponentFixture<DocumentComponent>;
  let dataStorageService: DataStorageService;
  let auditService: AuditService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DocumentComponent],
      imports: [RouterTestingModule, HttpClientTestingModule],
      providers: [
        {
          provide: TranslateService,
          useValue: {
            use: jasmine.createSpy('use'),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(DocumentComponent);
    component = fixture.componentInstance;
    dataStorageService = TestBed.get(DataStorageService);
    auditService = TestBed.get(AuditService);
  });

  it('should create the DocumentComponent', () => {
    expect(component).toBeTruthy();
  });

  it('should set pdfSrc when ngOnInit is called', () => {
    const mockBlob = new Blob(['PDF content'], { type: 'application/pdf' });
    const mockResponse = new Response();
    spyOn(dataStorageService, 'getSupportingDocument').and.returnValue(of(mockResponse));
    
    component.ngOnInit();
    
    expect(component.pdfSrc).toContain('blob:');
  });

  it('should handle errors when ngOnInit is called', () => {
    spyOn(dataStorageService, 'getSupportingDocument').and.returnValue(throwError('Error loading document'));
    
    component.ngOnInit();
    
    expect(component.pdfSrc).toBe('');
  });

  it('should download supporting document', () => {
    const saveAsSpy = spyOn(saveAs, 'saveAs');
    const mockBlob = new Blob(['PDF content'], { type: 'application/pdf' });
    const mockResponse = new Response();
    spyOn(dataStorageService, 'downloadSupportingDocument').and.returnValue(of(mockResponse));
    
    component.downloadSupportingDocument();
    
    expect(saveAsSpy).toHaveBeenCalled();
  });

  it('should handle errors when downloading supporting document', () => {
    spyOn(dataStorageService, 'downloadSupportingDocument').and.returnValue(throwError('Error downloading document'));
    spyOn(console, 'error');

    component.downloadSupportingDocument();

    expect(console.error).toHaveBeenCalled();
  });

  it('should handle item selection', () => {
    const routerSpy = spyOn(component['router'], 'navigate');
    component.onItemSelected('home');
    expect(routerSpy).toHaveBeenCalledWith(['dashboard']);
    
    component.onItemSelected('document');
    expect(routerSpy).toHaveBeenCalledWith(['document']);
    
    component.onItemSelected('regcenter');
    expect(routerSpy).toHaveBeenCalledWith(['regcenter']);
  });

  // Add more test cases as needed for other scenarios
});
