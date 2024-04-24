import { ExerciseService } from '../services/exercise.service';
import { Component, ViewChild} from '@angular/core';
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
 imageUrl: string | undefined;
 @ViewChild("image")
 image: any;
  constructor(private loginservice: LoginService, private personService:PersonService, private exerciseService:ExerciseService, public router: Router,public activatedRoute:ActivatedRoute,private sanitizer: DomSanitizer){
    const id = activatedRoute.snapshot.params['id'];
    this.imageUrl= undefined;
    this.exerciseService.getImage(id).subscribe((data) => {
      if(data){
      const blob = new Blob([data], { type: 'image/jpeg' });
      this.imageUrl = URL.createObjectURL(blob);
      }
    });
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
    const image = this.image.nativeElement.files[0];
    this.exerciseService.editExerciseById(this.exercise.id,this.exercise).subscribe(()=>{
    if (image) {
      let formData = new FormData();
      formData.append("image", image);
      if(this.exercise.id !== undefined){
        this.exerciseService.saveImage(image,this.exercise.id).subscribe(()=>{
          if(this.exercise.id !== undefined){
          this.exerciseService.getImage(this.exercise.id).subscribe((data)=>{
          const blob = new Blob([data], { type: 'image/jpeg' });
          this.imageUrl = URL.createObjectURL(blob);
          this.router.navigate(['/exercise/'+this.exercise.id]);
        })
        }});
      }
    }
    else{
      this.router.navigate(['/exercise/'+this.exercise.id]);
    }});
  }
}
