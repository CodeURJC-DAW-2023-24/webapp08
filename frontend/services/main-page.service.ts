import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { throwError } from 'rxjs/internal/observable/throwError';
import { catchError } from 'rxjs/internal/operators/catchError';


const BASE_URL = '/api/persons';

@Injectable({
  providedIn: 'root'
})
export class MainPageService {
  news: any;
  constructor(private http: HttpClient, public router: Router) {

  }

  getNews(): Observable<Object[]>{
   return this.http.get(BASE_URL + '/news') as Observable<Object[]>;

  }

private handleError(error: any) {
  console.error(error);
  return throwError("Server error (" + error.status + "): " + error.text())
}

}
