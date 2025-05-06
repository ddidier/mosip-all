import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FontSizeService {
  currentFontSize:any = localStorage.getItem("selectedfontsize") + 'px';
  fontSizesList: any = {
    "12px": { "heading": "18px", "paragraph": "14px", "headingForUIN": "16px", "breadcrumb":"10px", "subHeading":"14px", "warningMessages":"9px", "checkBoxText":"15px", "lableText":"12px", "capchaSize":0.7, "tabs":"13px" },
    "14px": { "heading": "22px", "paragraph": "16px", "headingForUIN": "18px", "breadcrumb":"12px", "subHeading":"16px", "warningMessages":"10px", "checkBoxText":"16px", "lableText":"14px", "capchaSize":0.8, "tabs":"15px" }, 
    "16px": { "heading": "26px", "paragraph": "17px", "headingForUIN": "20px", "breadcrumb":"14px", "subHeading":"18px", "warningMessages":"12px", "checkBoxText":"17px", "lableText":"15px", "capchaSize":0.88, "tabs":"16px" }, 
    "18px": { "heading": "30px", "paragraph": "18px", "headingForUIN": "22px", "breadcrumb":"16px", "subHeading":"20px", "warningMessages":"14px", "checkBoxText":"18px", "lableText":"17px", "capchaSize":0.94, "tabs":"18px" }
  }
  fontSize: any = this.fontSizesList[this.currentFontSize];
  constructor() { }

  setFontSize(size: any): void {
    if (size == 12) {
      this.fontSize = this.fontSizesList['12px'];
    }
    if (size == 14) {
      this.fontSize = this.fontSizesList['14px'];
    }
    if (size == 16) {
      this.fontSize = this.fontSizesList['16px'];
    }
    if (size == 18) {
      this.fontSize = this.fontSizesList['18px'];
    }
  }
}
