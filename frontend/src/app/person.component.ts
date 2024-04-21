
import { LoginService } from './../../services/login.service';
import { Component} from '@angular/core';
import {  OnInit , ViewChild} from '@angular/core';
import { Person } from '../../models/person.model';
import { PersonService } from './../../services/person.service';
import { Router } from '@angular/router';


@Component({
  selector: 'person',
  templateUrl: './person.component.html',

  styleUrls: [
    '../assets/css/style.css',
    '../assets/css/responsive.css',
    '../assets/css/bootstrap.css'
  ]
})
export class PersonComponent implements OnInit {
admin: boolean;



  constructor(public loginservice:LoginService,public personService: PersonService, public router: Router){
    this.isReadOnly = true;
    this.styleE = 'block';
    this.styleG = 'none';


   }
  person: Person ;
  isReadOnly: boolean;
  styleG: string;
  styleE: string;
  imageUrl: string | undefined;
  @ViewChild("image")
  image: any;
  roles:String[];


  ngOnInit(): void {
     this.personService.getPerson().subscribe(
      response => {
          this.person= response as Person;
      },
      error => {
        this.router.navigate(['../login']);
        this.person = {alias:"",name:"",date:"",weight:0, roles:[]};

      }
  );
    this.personService.getImage().subscribe(data => {
      if(data){
      const blob = new Blob([data], { type: 'image/jpeg' });
      this.imageUrl = URL.createObjectURL(blob);

      }
      else{
        this.imageUrl = undefined;
      }
    });
    this.roles=this.person.roles;
    if (this.roles.includes('ADMIN')) {
    this.admin = true;
      }else{
    this.admin=false;
    }

  }


  logOut(event: any) {
   this.loginservice.logOut();
    }

  change(event: any) {
      this.isReadOnly=false;
      this.styleE = 'none';
      this.styleG = 'block';
      }

   editUser(event: any) {
        const image = this.image.nativeElement.files[0];
        this.personService.editPerson(this.person,image);

        this.personService.getImage().subscribe(data => {
          if(data){
          const blob = new Blob([data], { type: 'image/jpeg' });
          this.imageUrl = URL.createObjectURL(blob);
          }
          else{
            this.imageUrl = undefined;
          }
        });

        this.isReadOnly=true;
        this.styleE = 'block';
        this.styleG = 'none';


      }

}
