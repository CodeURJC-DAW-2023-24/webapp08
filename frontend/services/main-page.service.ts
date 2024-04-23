import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';



const BASE_URL = '/api/persons/';
const BASE_URL2 = '/api/rutines/';

@Injectable({
  providedIn: 'root'
})
export class MainPageService {
  constructor(private http: HttpClient, public router: Router) {

  }

  getNews(iteracion:number) {
    return this.http.get(BASE_URL + `news?iteracion=${iteracion}`);
  }

  getRutines(){
    return this.http.get(BASE_URL2);

  }




}
