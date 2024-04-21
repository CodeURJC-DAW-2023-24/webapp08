import { Injectable } from '@angular/core';
import { Rutine } from '../models/rutine.model';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';

const BASE_URL = '/api/rutines/';

@Injectable({providedIn: 'root'})
export class RutineService{



  rutine : Rutine;
  constructor(public router: Router, public http:HttpClient){

  }
  newRutine(rutine: Rutine) {
    this.http.post(BASE_URL,rutine).subscribe();
    this.router.navigate(['../mainPage']);
  }
  getRutine(id: number) {
   return this.http.get(BASE_URL+id);
  }
  saveRutine(rutine: Rutine,isNew: boolean) {
    if(isNew){
      return this.http.post(BASE_URL,rutine)
    }
    else{
    return this.http.patch(BASE_URL+rutine.id,rutine);
    }
}
deleteRutine(id: number|undefined ) {
  if(id !==undefined){}
  this.http.delete(BASE_URL+id).subscribe();
}

deleteComment(idRutina: number | undefined, id1Comment: number | undefined) {
  if(idRutina !== undefined && id1Comment !== undefined){
    return this.http.delete('/api/rutines/'+idRutina+'/comments/'+id1Comment)
  }
  return of();

}
addComment(id: number | undefined, newComment: string) {
  if(id !== undefined ){
    const body = { comment: newComment };
    return this.http.post('/api/rutines/'+id+'/comments',body)
  }
  return of();

}

download(id:number | undefined){
  return this.http.get(BASE_URL+'download/'+id, {responseType: 'blob'});
}
}
