import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';


const BASE_URL = '/api/persons/';
@Injectable({
  providedIn: 'root'
})
export class HeaderService {

  constructor(private http: HttpClient,public router: Router){

  }

  getRequests(){
    return this.http.get(BASE_URL + 'requests');

  }

 proccessRequest(notification:number, accepted:boolean){
    return this.http.put(BASE_URL + `friends/requests/${notification}?accepted=${accepted}`,undefined);

  }

}
