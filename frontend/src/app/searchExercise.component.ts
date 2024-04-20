import { Component,Input, Output, EventEmitter } from '@angular/core';
import { LoginService } from './../../services/login.service';
import { Person } from '../../models/person.model';
import { PersonService } from './../../services/person.service';
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

  constructor(public loginservice:LoginService,public personService: PersonService) {
  }

  ngOnInit(): void {
    if (this.loginservice.isLogged()){
      this.person= this.loginservice.currentUser();}
      this.roles=this.person.roles;
      if (this.roles.includes('ADMIN')) {
        this.admin = true;
      }else{
        this.admin=false;
      }
    }

}
