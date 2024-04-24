import { Component, ViewChild } from '@angular/core';
import { Router,ActivatedRoute } from '@angular/router';
import { LoginService } from '../services/login.service';
import { PersonService } from '../services/person.service';
import { Exercise } from '../models/exercise.model';
import { ExerciseService } from '../services/exercise.service';
import { ErrorService } from '../services/error.service';
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
  @ViewChild("image")
  image: any;

  constructor(private loginService: LoginService, private router: Router, activatedRoute: ActivatedRoute, public personService: PersonService, private exerciseService: ExerciseService, private errorService: ErrorService) {
    const id = activatedRoute.snapshot.params['id'];
    this.exercise = { name: "", description: "", video: "", grp: "" };
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

  addExercise($event: Event) {
    if (this.exercise.name=== ""|| this.exercise.description=== "" || this.exercise.grp==="" ) {
      this.router.navigate(['../error']);
          this.errorService.setblanks(true);
    }else {
      const image = this.image.nativeElement.files[0];
    this.exerciseService.saveExercise(this.exercise).subscribe(
        (response) => {
          this.exercise = response as Exercise;
          if(image && this.exercise.id !== undefined){
        this.exerciseService.saveImage(image,this.exercise.id).subscribe(()=>{
          this.router.navigate(['/exercise/' + this.exercise.id]);
        },
        (error) => {
          this.router.navigate(['../error']);
          this.errorService.setexistingEx(true);
          })}
        else{
        this.router.navigate(['/exercise/'+this.exercise.id]);}
      }

      );
    }
  }

}
