
import { Component} from '@angular/core';
import {  OnInit } from '@angular/core';
import { Rutine } from '../models/rutine.model';
import { RutineService } from '../services/rutine.service';
import { ExerciseService } from '../services/exercise.service';
import { ExRutine } from '../models/exRutine.model';
import { Exercise } from '../models/exercise.model';
import { ActivatedRoute, Router } from '@angular/router';



@Component({
  selector: 'addExRutine',
  templateUrl: './addExRutine.component.html',

  styleUrls: [
    '../../assets/css/style.css',
    '../../assets/css/responsive.css',
    '../../assets/css/bootstrap.css'
  ]
})
export class AddExRutineComponent implements OnInit{

exRutine: ExRutine;
rutine:Rutine;
chest: Exercise[];
back: Exercise[];
shoulder: Exercise[];
biceps: Exercise[];
triceps: Exercise[];
lower: Exercise[];
cardio: Exercise[];
;


  constructor( private rutineService:RutineService, private exerciseService: ExerciseService,public router:Router
    ,activatedRoute:ActivatedRoute){
    this.exRutine={grp:"Pecho",exercise:"",series:"",weight:0};
    const id = activatedRoute.snapshot.params['id'];
    this.rutine={id:id,name:"",date:new Date('2024-04-21'),time:0,person:""}
    this.rutineService.getRutine(id).subscribe(
      response=>
        this.rutine = response as Rutine
    );

  }
  ngOnInit(): void {

    this.exerciseService.getExercisesByGroup("Pecho").subscribe(
      response=>
        this.chest= response as Exercise[]
    );
    this.exerciseService.getExercisesByGroup("Espalda").subscribe(
      response=>
        this.back= response as Exercise[]
    );
    this.exerciseService.getExercisesByGroup("Hombro").subscribe(
      response=>
        this.shoulder= response as Exercise[]
    );
    this.exerciseService.getExercisesByGroup("Biceps").subscribe(
      response=>
        this.biceps= response as Exercise[]
    );
    this.exerciseService.getExercisesByGroup("Triceps").subscribe(
      response=>
        this.triceps= response as Exercise[]
    );
    this.exerciseService.getExercisesByGroup("Inferior").subscribe(
      response=>
        this.lower= response as Exercise[]
    );
    this.exerciseService.getExercisesByGroup("Cardio").subscribe(
      response=>
        this.cardio= response as Exercise[]
    );

  }
  addEx($event: MouseEvent) {
    this.rutine.exercises?.push(this.exRutine);
    this.rutineService.saveRutine(this.rutine,false).subscribe(
      response =>{
      this.router.navigate(['/addRutine/'+this.rutine.id])}
    );
    }

}
