import { Observable } from 'rxjs';

import { Injectable } from '@angular/core';
import { Exercise } from '../models/exercise.model';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';
const BASE_URL = '/api/exercises/';

@Injectable({providedIn: 'root'})
export class ExerciseService{
  exercise: Exercise[];
  newExercise: Exercise;

  constructor(public router: Router, public http:HttpClient){

  }
  getExercisesByGroup(grp: string): Observable<Object> {
    return this.http.get(BASE_URL+'group/'+ grp)
  }
  saveExercise(newExercise: Exercise) {
      return this.http.post(BASE_URL,newExercise);
  }
  getExerciseById(id: number){
    return this.http.get(BASE_URL+ id);
  }
  deleteExerciseById(id:number| undefined){
    if(id !== undefined ){
    }
    return this.http.delete(BASE_URL+id).subscribe();
  }
  editExerciseById(id:number| undefined, exercise: Exercise){
    if(id !== undefined ){
    }
    return this.http.put(BASE_URL+id,exercise);
  }
  getExercisesByGroupPageable(grp:string, page:number){
    return this.http.get(BASE_URL+`group/?group=${grp}&page=${page}`);
  }


}
