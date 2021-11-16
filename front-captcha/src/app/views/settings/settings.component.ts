import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { HttpClient } from '@angular/common/http';
import { FormControl, Validators } from '@angular/forms';


@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  captchalengthInput = new FormControl(this.data.captchalength, [Validators.required]);
  AttempsInput = new FormControl(this.data.attempts, [Validators.required]);
  caseSensitiveInput = new FormControl(String(this.data.caseeSensitive), [Validators.required]);
  modeInput = new FormControl(this.data.mode, [Validators.required]);

  constructor(private http: HttpClient,
    private dialogRef: MatDialogRef<SettingsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: settings) { }

  ngOnInit(): void {

  }

  save() {

    let sconf: settings = { captchalength: this.captchalengthInput.value, attempts: this.AttempsInput.value, caseeSensitive: this.caseSensitiveInput.value, mode: this.modeInput.value };
    console.log(sconf);
    this.http.post<settings>("http://localhost:8080/v1/captcha/settings", sconf).subscribe({
      next: data => {
        console.log(data)
      },
      error: error => {
        console.error('There was an error!', error);
      }
    }
    );
    this.dialogRef.close();

  }

  close() {
    this.dialogRef.close();
  }
}
export interface settings {
  captchalength: number;
  attempts: number;
  caseeSensitive: boolean;
  mode: string;
}