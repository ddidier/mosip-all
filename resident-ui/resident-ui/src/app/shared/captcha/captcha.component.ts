import { Component, OnInit, Input, EventEmitter, Output } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { TranslateService } from "@ngx-translate/core";
import { FontSizeService } from "src/app/core/services/font-size.service";

@Component({
  selector: "app-captcha",
  templateUrl: "./captcha.component.html",
  styleUrls: ["./captcha.component.css"],
})
export class CaptchaComponent implements OnInit {
  @Input() captchaSiteKey: string;
  @Input() resetCaptcha: boolean;
  @Output() captchaEvent = new EventEmitter<string>();
  langCode: string = localStorage.getItem("langCode");
  captchaLangCode:any;
  constructor(private activatedRoute: ActivatedRoute,private translateService: TranslateService,private fontSizeService: FontSizeService) {}

  changeCaptchLang(){
    if(this.captchaLangCode){
      const iframeGoogleCaptcha = document.getElementById("recaptcha-container").querySelector('iframe');
      const currentLang = iframeGoogleCaptcha.getAttribute("src").match(/hl=(.*?)&/).pop();
      if (currentLang !== this.captchaLangCode) {
          iframeGoogleCaptcha.setAttribute(
              "src",
              iframeGoogleCaptcha.getAttribute("src").replace(
                  /hl=(.*?)&/,
                  'hl=' + this.captchaLangCode + '&'
              )
          );
      }
      return
    }else{
      setTimeout(() => {
        this.changeCaptchLang()
      },100)
      
    }
  }

  ngOnInit() {
    this.translateService
    .getTranslation('default')
      .subscribe(response => {
          this.captchaLangCode = response.keyboardMapping[this.langCode]
      })
    let count = 0;
    this.changeCaptchLang()
  }

  ngOnChanges(): void {
    if (this.resetCaptcha) this.handleReset();
  }

  recaptcha(captchaResponse: any) {
    console.log(`Resolved captcha with response: ${captchaResponse}`);
      this.captchaEvent.emit(captchaResponse);
   }

  recaptchaError(event) {
    alert(event);
  }

  get fontSize():string {
    let captchSize =  this.fontSizeService.fontSize.capchaSize;
    return `scale(${captchSize})`
  }

  handleReset() {
    //grecaptcha.reset();
  }
}
