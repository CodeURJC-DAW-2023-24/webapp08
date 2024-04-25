import { LoginService } from '../services/login.service';
import { Component } from '@angular/core';
import { OnInit, ViewChild } from '@angular/core';
import { Rutine } from '../models/rutine.model';
import { RutineService } from '../services/rutine.service';
import { PersonService } from '../services/person.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Person } from '../models/person.model';
import { ExRutine } from '../models/exRutine.model';
import { ErrorService } from '../services/error.service';

@Component({
  selector: 'addRutine',
  templateUrl: './addRutine.component.html',

  styleUrls: [
    '../../assets/css/style.css',
    '../../assets/css/responsive.css',
    '../../assets/css/bootstrap.css'
  ]
})
export class AddRutineComponent implements OnInit {



  constructor(private loginService: LoginService, private rutineService: RutineService, private router: Router, activatedRoute: ActivatedRoute, public personService: PersonService, private errorService: ErrorService) {
    const id = activatedRoute.snapshot.params['id'];
    this.rutine = { id: id, name: "", date: new Date('2024-04-21'), time: 0, person: "" };
    if (id) {
      rutineService.getRutine(id).subscribe(
        response => this.rutine = response as Rutine

      );
      this.isNew = false;
    } else {
      this.rutine = { name: "", date: new Date('2024-04-21'), time: 0, person: "" };
      this.isNew = true;
    }
  }

  rutine: Rutine;
  isNew: boolean;



  ngOnInit(): void {
    this.personService.getPerson().subscribe(
      response => {
      },
      error => {
        this.router.navigate(['../login']);

      }
    );
  }

  newRutine($event: Event) {
    if (this.rutine.name === '' || this.rutine.time === 0 || this.rutine.date === null) {
      this.router.navigate(['../error']);
      this.errorService.setblanks(true);
      this.errorService.setincorrectUserPass(false);
      this.errorService.setIncorrectPU(false);
    } else {
      this.rutineService.saveRutine(this.rutine, this.isNew).subscribe(
        response => {
          this.rutine = response as Rutine;
          this.isNew = false;
          this.router.navigate(['/rutine/' + this.rutine.id]);
        },
      );
    }
  }

  saveRutine($event: MouseEvent) {

    this.rutineService.saveRutine(this.rutine, this.isNew).subscribe(
      response => {
        this.rutine = response as Rutine;
        this.isNew = false;
        this.router.navigate(['/addExRutine/' + this.rutine.id]);
      }

    );

  }
  cancel($event: MouseEvent) {
    if (!this.isNew) {
      this.rutineService.deleteRutine(this.rutine.id);
    }
    this.router.navigate(['/mainPage']);

  }

  deleteExRutine($event: MouseEvent, ex: ExRutine) {
    if (this.rutine.exercises) {
      const index = this.rutine.exercises?.indexOf(ex);
      if (index !== -1) {
        this.rutine.exercises.splice(index, 1);
        this.rutineService.saveRutine(this.rutine, this.isNew).subscribe(
          response => {
            this.rutine = response as Rutine;
            this.isNew = false;
          }

        );
      }
    }
  }



}
