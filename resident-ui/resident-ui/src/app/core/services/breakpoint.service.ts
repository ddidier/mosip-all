import { Injectable } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class BreakpointService {

  constructor(private breakpointObserver: BreakpointObserver) {}
  isBreakpointActive(): Observable<any> {
    return this.breakpointObserver.observe([Breakpoints.XSmall,
    Breakpoints.Small,
    Breakpoints.Medium,
    Breakpoints.Large,
    Breakpoints.XLarge,]).pipe(
      map(result => {
        if (result.matches) {
          if (result.breakpoints[Breakpoints.XSmall]) {
            return "extraSmall"
          }
          if (result.breakpoints[Breakpoints.Small]) {
            return "small"
          }
          if (result.breakpoints[Breakpoints.Medium]) {
            return "medium"
          }
          if (result.breakpoints[Breakpoints.Large]) {
            return "large"
          }
          if (result.breakpoints[Breakpoints.XLarge]) {
            return "ExtraLarge"
          }
        }
      })
    );
  }
}