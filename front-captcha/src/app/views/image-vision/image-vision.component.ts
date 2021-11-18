import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormArray, FormBuilder } from '@angular/forms'
import { HttpClient ,HttpParams} from '@angular/common/http';

@Component({
  selector: 'app-image-vision',
  templateUrl: './image-vision.component.html',
  styleUrls: ['./image-vision.component.css']
})
export class ImageVisionComponent implements OnInit {


  baseURL = 'http://bio-captcha.westeurope.cloudapp.azure.com:8080';

  ngOnInit(): void {
  }

  name = 'Angular';
  dataInput:response[] = [{faces:[{age:"",gender:""}],description:{captions:[{text:""}]}}];

  productForm: FormGroup;
   
  constructor(private fb:FormBuilder, private http: HttpClient) {
   
    this.productForm = this.fb.group({
      name: {},
      urlInputs: this.fb.array([]) 
    });
  }
  
  urlInputs() : FormArray {
    return this.productForm.get("urlInputs") as FormArray
  }
   
  newQuantity(): FormGroup {
    return this.fb.group({
      url: '',
      age:'',
      gender:'',
      text:'',
    })
  }
   
  addUrlInput() {
    this.urlInputs().push(this.newQuantity());
  }
   
  removeUrlInput(i:number) {
    this.urlInputs().removeAt(i);
  }
   
  onSubmit() {

    let params = new HttpParams();
    params = this.productForm.value.urlInputs.map(url => url.url).reduce((p, c) => p.append('url', c), params);

    this.http.get(this.baseURL + '/v1/image/description', { params: params }).subscribe({
      next: (data:response[]) => {
        this.urlInputs().controls.forEach((control, index) => {
          console.log(data);

          control.get('age').setValue(data[index].faces[0].age);
          control.get('gender').setValue(data[index].faces[0].gender);
          control.get('text').setValue(data[index].description.captions[0].text);
        });
      },
     error: error => {
       console.error('There was an error!', error);
     }}
    );
  }


  
}



export interface caption {
  text: string;
}
export interface face {
  age: string;
  gender:string;
}

export interface description {
    captions:caption[];
}

export interface response{
  faces:face[];
  description:description;

}