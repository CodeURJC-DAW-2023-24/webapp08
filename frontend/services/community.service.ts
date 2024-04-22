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


	sendFriendRequest(alias:string) {
	return	this.http.post(BASE_URL+`friends/requests/${alias}`,undefined)
  }
  searchUsers(alias:string){
    return alias;
  }

  deleteUser(id:string){

  }

  loadFriends(){

  }
}
