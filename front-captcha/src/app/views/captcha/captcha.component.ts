import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { of } from 'rxjs';
import { fromFetch } from 'rxjs/fetch';
import { switchMap, catchError } from 'rxjs/operators';
import { FormControl,  Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import { settings, SettingsComponent } from '../settings/settings.component';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-captcha',
  templateUrl: './captcha.component.html',
  styleUrls: ['./captcha.component.css']
})
export class CaptchaComponent implements OnInit {

  @ViewChild('canvas', { static: true })
  canvs: ElementRef<HTMLCanvasElement>;

  baseURL = 'http://localhost:8080';
  captchaURI = '/v1/captcha';
  settingURI = '/v1/captcha/settings';
  validateaURI = '/v1/captcha/validate';
  codeCaptcha: string;
  captcha: string = '';
  captchaTrys: number = 0;
  captchaMaxTry: number = 3;
  captchaInput = new FormControl('', [Validators.required]);

  conf: settings;

  constructor(private http:HttpClient, private _snackBar: MatSnackBar,private dialog: MatDialog) { }
  

  ngOnInit(): void {
    this.beginCaptcha();

      this.http.get<settings>(this.baseURL+this.settingURI).subscribe( data => {
        this.conf = data;
      });


  }

  beginCaptcha() {
    this.data$.subscribe({
      next: (result: CaptchaModel) => this.getImgValiCode(result.value),
      complete: () => console.log('done')
    });

  }

  onSubmit() {
    this.captchaMaxTry=this.conf.attempts;
    if (this.captchaInput.value === '') {
      this.openSnackBar('Please enter captcha', 'OK');
    } else {
      this.http.post<validation>(this.baseURL+this.validateaURI, {value:this.captchaInput.value,captchaValue:this.codeCaptcha}).subscribe({
        next: (result: validation) => {

          if (result.correct) {
            this.openSnackBar('Captcha is correct', 'OK');
            this.beginCaptcha();
            this.captchaTrys = 0;
          } else {
            this.captchaTrys++;
            if (this.captchaTrys === this.captchaMaxTry) {
              this.openSnackBar('Captcha is incorrect', 'OK');
              this.captchaTrys = 0;
              this.beginCaptcha();
            } else {
              this.openSnackBar('Captcha incorrecto quedan ' + (this.conf.attempts - this.captchaTrys) + ' intentos', 'Try again');
            }
          }
        }
      });
    }
  }


  openDialog() {

    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.maxWidth = '350px';
    dialogConfig.data = this.conf;

    this.dialog.open(SettingsComponent, dialogConfig);
}
  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action);
  }

  getImgValiCode(valueDraw: string = '') {
    this.codeCaptcha = valueDraw;
    let showNum = [];
    let canvasWinth = 300;
    let canvasHeight = 100;
    let context = this.canvs.nativeElement.getContext('2d');
    this.canvs.nativeElement.width = canvasWinth;
    this.canvs.nativeElement.height = canvasHeight;
    let textArray = valueDraw.split('');

    for (let i = 0; i < textArray.length; i++) {
      let sDeg = (Math.random() * 30 * Math.PI) / 180;
      let cTxt = textArray[i];
      showNum[i] = cTxt.toLowerCase();
      let x = 10 + i * 20;
      let y = 20 + Math.random() * 8;
      context.font = '50px bold';
      context.translate(x, y);
      context.rotate(sDeg);
      context.fillStyle = this.randomColor();
      context.fillText(cTxt, 70, 15);
      context.rotate(-sDeg);
      context.translate(-x, -y);
    }

    for (let i = 0; i <= 5; i++) {
      context.strokeStyle = this.randomColor();
      context.beginPath();
      context.moveTo(Math.random() * canvasWinth, Math.random() * canvasHeight);
      context.lineTo(Math.random() * canvasWinth, Math.random() * canvasHeight);
      context.stroke();
    }
    for (let i = 0; i < 30; i++) {
      context.strokeStyle = this.randomColor();
      context.beginPath();
      let x = Math.random() * canvasWinth;
      let y = Math.random() * canvasHeight;
      context.moveTo(x, y);
      context.lineTo(x + 1, y + 1);
      context.stroke();
    }
  }

  randomColor() {
    let r = Math.floor(Math.random() * 256);
    let g = Math.floor(Math.random() * 256);
    let b = Math.floor(Math.random() * 256);
    return 'rgb(' + r + ',' + g + ',' + b + ')';
  }


  data$ = fromFetch(this.baseURL+this.captchaURI).pipe(
    switchMap(response => {
      if (response.ok) {
        // OK devuelve datos
        return response.json();
      } else {
        // El servidor está devolviendo un estado que requiere que el cliente intente algo más.
        return of({ error: true, message: `Error ${response.status}` });
      }
    }),
    catchError(err => {
      // Red u otro error, maneje apropiadamente
      console.error(err);
      return of({ error: true, message: err.message })
    })
  );

}


export interface CaptchaModel {
  value: string;
  captchaValue: string;
}

export interface validation {
  correct: boolean;
}
