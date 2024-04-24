import { Component, Input, Output, EventEmitter } from '@angular/core';
import { LoginService } from '../services/login.service';
import { Person } from '../models/person.model';
import { PersonService } from '../services/person.service';
import { Router } from '@angular/router';
import { PersonRegister } from '../models/personRegister.model';
import { OnInit, ViewChild } from '@angular/core';
@Component({
  selector: 'newPerson',
  templateUrl: './newPerson.component.html',
  styleUrls: [
    '../../assets/css/style.css',
    '../../assets/css/responsive.css',
    '../../assets/css/bootstrap.css'
  ]
})
export class NewPersonComponent {
  admin: boolean;
  person: Person;
  personRegister: PersonRegister;
  roles: String[];
  encodedpassword2: string;
  @ViewChild("image")
  image: any;

  constructor(public loginservice: LoginService, public personService: PersonService, public router: Router) {
  }

  ngOnInit(): void {
    this.personService.getPerson().subscribe(
      response => {
        this.person = response as Person;
        this.roles = this.person.roles;
        if (this.roles.includes('ADMIN')) {
          this.admin = true;
        } else {
          this.admin = false;
        }

      },
      error => {
        this.personRegister = { alias: "", name: "", date: "", weight: 0, encodedPassword:""};
      }
    );
  }

  registerPerson($event: Event) {

    if (this.personRegister.encodedPassword === this.encodedpassword2) {
      const image = this.image.nativeElement.files[0];
      this.personService.savePerson(this.personRegister, image).subscribe();
      this.router.navigate(['../login']);
    }

  }



}
