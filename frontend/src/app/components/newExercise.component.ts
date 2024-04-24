import { Component } from '@angular/core';
import { Router,ActivatedRoute } from '@angular/router';
import { LoginService } from '../services/login.service';
import { PersonService } from '../services/person.service';
import { Exercise } from '../models/exercise.model';
import { ExerciseService } from '../services/exercise.service';
@Component({
  selector: 'newExercise',
  templateUrl: './newExercise.component.html',
  styleUrls: [
    '../../assets/css/style.css',
    '../../assets/css/responsive.css',
    '../../assets/css/bootstrap.css'
  ]
})
export class NewExerciseComponent {
  exercise:Exercise;

  constructor(private loginService: LoginService, private router: Router,activatedRoute:ActivatedRoute, public personService: PersonService, private exerciseService: ExerciseService) {
    const id = activatedRoute.snapshot.params['id'];
    this.exercise={name:"",description:"", video:"", grp:""};
  }

  ngOnInit(): void {
    this.personService.getPerson().subscribe(
      response => {
      },
      error => {
        this.router.navigate(['../login']);

      }
  );
  }

  addExercise($event: Event){
    this.exerciseService.saveExercise(this.exercise).subscribe(
      response=>{
        this.exercise = response as Exercise;
        this.router.navigate(['/exercise/'+this.exercise.id]);
      }

    );
  }

}
