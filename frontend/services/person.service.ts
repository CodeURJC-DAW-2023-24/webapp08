import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Person } from '../models/person.model';
import { Router } from '@angular/router';

import { throwError } from 'rxjs';

const BASE_URL = '/api/persons/';

@Injectable({providedIn: 'root'})
export class PersonService{

 person:Person

  constructor(private http: HttpClient,public router: Router){

  }


  getPerson(){

    return this.http.get('/api/persons/', { withCredentials: true })


}

  editPerson(person: Person, image: File){

    if(image){
      let formData = new FormData();
      formData.append("image", image);
      this.http.patch(BASE_URL,person);
      return this.http.post(BASE_URL+ 'image',formData);
    }
    else{
      return this.http.patch(BASE_URL,person);
    }



}

  getImage(){
    return this.http.get(BASE_URL + 'image', { responseType: 'arraybuffer' })
  }
  getImageByAlias(alias:String){
    return this.http.get(BASE_URL + 'image/'+alias, { responseType: 'arraybuffer' })
  }




}
