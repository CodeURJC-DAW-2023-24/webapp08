import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

const BASE_URL = '/api/persons/charts';

@Injectable({
  providedIn: 'root'
})
export class StatisticsService {

  constructor(private http: HttpClient, public router: Router ) {}

  getCharts() {
    return this.http.get(BASE_URL);
  }
}
