import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';


const BASE_URL = '/api/persons/';

@Injectable({
  providedIn: 'root'
})
export class CommunityService {

  constructor(private http: HttpClient, public router: Router) {
  }

  getNews(iteracion:number) {
    return this.http.get(BASE_URL + `news?iteracion=${iteracion}`);
  }
}
