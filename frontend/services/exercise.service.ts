import { Observable } from 'rxjs';

import { Injectable } from '@angular/core';
import { Exercise } from '../models/exercise.model';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

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


}
