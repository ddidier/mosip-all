import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterExtService } from './router-ext.service';

describe('RouterExtService', () => {
  let service: RouterExtService;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RouterExtService, Router],
    });
    service = TestBed.get(RouterExtService);
    router = TestBed.get(Router);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should initialize with undefined previous and current URLs', () => {
    expect(service.getPreviousUrl()).toBeUndefined();
  });

  it('should update previous and current URLs on navigation', () => {
    router.navigate(['/home']); // Navigate to a URL
    expect(service.getPreviousUrl()).toBeUndefined(); // The previous URL should still be undefined as there was no previous navigation within this test
    expect(service.getPreviousUrl()).toBe('/home'); // The current URL should be the one we navigated to

    router.navigate(['/about']); // Navigate to another URL
    expect(service.getPreviousUrl()).toBe('/home'); // Now, the previous URL should be '/home'
    expect(service.getPreviousUrl()).toBe('/about'); // The current URL should be the one we navigated to
  });

  // Add more test cases as needed for other scenarios
});
