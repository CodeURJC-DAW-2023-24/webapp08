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



  searchUsers(alias:string){
    return this.http.get(BASE_URL+`names?alias=${alias}`)
  }

  sendFriendRequest(alias:string) {
    return	this.http.post(BASE_URL+`friends/requests/${alias}`,undefined)
    }
    
  deleteUser(id:number){
    return this.http.delete(BASE_URL+`${id}`)
  }



}
