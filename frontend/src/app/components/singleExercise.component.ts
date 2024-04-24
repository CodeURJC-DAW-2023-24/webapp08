import { ExerciseService } from '../services/exercise.service';
import { Component} from '@angular/core';
import {  OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Router,ActivatedRoute } from '@angular/router';
import { LoginService } from '../services/login.service';
import { Person } from '../models/person.model';
import { PersonService } from '../services/person.service';
import { Exercise } from '../models/exercise.model';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';


@Component({
  selector: 'exercise',
  templateUrl: './singleExercise.component.html',
  styleUrls: [
    '../../assets/css/style.css',
    '../../assets/css/responsive.css',
    '../../assets/css/bootstrap.css'
  ]
})
export class SingleExerciseComponent {
admin: boolean;
 person:Person;
 eVideo:boolean;
 roles: String[];
 editMode:boolean=false;
 exercise:Exercise;
 safeVideo:SafeResourceUrl;
  constructor(private loginservice: LoginService, private personService:PersonService, private exerciseService:ExerciseService, public router: Router,activatedRoute:ActivatedRoute,private sanitizer: DomSanitizer){
    const id = activatedRoute.snapshot.params['id'];
    exerciseService.getExerciseById(id).subscribe(
      response => {this.exercise = response as Exercise;
        this.safeVideo =this.sanitizer.bypassSecurityTrustResourceUrl(this.exercise.video);
        if (this.exercise.video===''){
          this.eVideo=false;
        }else{
          this.eVideo=true;
        }
      }
    );}

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

      },);
  }
  editExerciseMode($event: Event){
    this.editMode=true
  }
  deleteExercise($event:  MouseEvent){
    this.exerciseService.deleteExerciseById(this.exercise.id);
    this.router.navigate(['/mainPage']);
  }

  editExercise($event: Event){
    this.editMode=false;
    this.exerciseService.editExerciseById(this.exercise.id,this.exercise).subscribe();
    this.router.navigate(['/exercise/'+this.exercise.id]);
  }
}
