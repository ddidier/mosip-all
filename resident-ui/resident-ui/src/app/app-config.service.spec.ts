import { TestBed, async } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AppConfigService } from './app-config.service';

describe('AppConfigService', () => {
  let service: AppConfigService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AppConfigService],
    });
    service = TestBed.get(AppConfigService);
    httpTestingController = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should load app config from JSON file', async(() => {
    const mockResponse = {
      baseUrl: 'https://example.com',
      // ... other properties
    };

    service.loadAppConfig();

    const req = httpTestingController.expectOne('./assets/config.json');
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);

    expect(service.getConfig()).toEqual(mockResponse);
  }));

  it('should handle HTTP error when loading app config', async(() => {
    service.loadAppConfig();

    const req = httpTestingController.expectOne('./assets/config.json');
    expect(req.request.method).toBe('GET');
    req.error(new ErrorEvent('network error'));

    // Handle the error as needed, e.g., by logging it.
    spyOn(console, 'error');
    expect(console.error).toHaveBeenCalled();
  }));

  // Add more test cases as needed for other scenarios and error handling
});
