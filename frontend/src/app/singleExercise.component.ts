import { ExerciseService } from './../../services/exercise.service';
import { Component} from '@angular/core';
import {  OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Router,ActivatedRoute } from '@angular/router';
import { LoginService } from '../../services/login.service';
import { Person } from '../../models/person.model';
import { PersonService } from '../../services/person.service';
import { Exercise } from '../../models/exercise.model';

@Component({
  selector: 'exercise',
  templateUrl: './singleExercise.component.html',
  styleUrls: [
    '../assets/css/style.css',
    '../assets/css/responsive.css',
    '../assets/css/bootstrap.css'
  ]
})
@Injectable({providedIn: 'root'})
export class SingleExerciseComponent {
  admin: boolean;
 person:Person;
 eVideo:boolean;
 roles: String[];
  constructor(private loginservice: LoginService, private personService:PersonService, exerciseService:ExerciseService, public router: Router,activatedRoute:ActivatedRoute){
    const id = activatedRoute.snapshot.params['id'];
    exerciseService.getExerciseById(id).subscribe(
      response => {this.exercise = response as Exercise
        if (this.exercise.video===''){
          this.eVideo=false;
        }else{
          this.eVideo=true;
        }
      }
    );}
  exercise:Exercise;
  ngOnInit(): void {
    this.personService.getPerson().subscribe(
      response => {
          this.person= response as Person;
          this.roles=this.person.roles;
          if (this.roles.includes('ADMIN')) {
            this.admin = true;
          }else{
            this.admin=false;
          }

      },
      error => {
        this.router.navigate(['../login']);
        this.person = {alias:"",name:"",date:"",weight:0, roles:[]};

      });
  }
}
