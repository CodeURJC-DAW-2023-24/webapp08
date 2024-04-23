import { Component,Input, Output, EventEmitter } from '@angular/core';
import { LoginService } from './../../services/login.service';
import { Person } from '../../models/person.model';
import { PersonService } from './../../services/person.service';
import { Router } from '@angular/router';
@Component({
  selector: 'searchExercise',
  templateUrl: './searchExercise.component.html',
  styleUrls: [
    '../assets/css/style.css',
    '../assets/css/responsive.css',
    '../assets/css/bootstrap.css'
  ]
})
export class SearchExerciseComponent {
 admin: boolean;
 person:Person;
 roles: String[];

  constructor(public loginservice:LoginService,public personService: PersonService,public router: Router) {
  }

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
  );
  }

}
