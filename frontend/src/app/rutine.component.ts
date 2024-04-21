import { Component} from '@angular/core';
import {  OnInit } from '@angular/core';
import { Rutine } from '../../models/rutine.model';
import { RutineService } from './../../services/rutine.service';
import { Injectable } from '@angular/core';
import { Router,ActivatedRoute } from '@angular/router';
import { LoginService } from './../../services/login.service';
import { Person } from '../../models/person.model';
import { PersonService } from './../../services/person.service';


@Component({
  selector: 'rutine',
  templateUrl: './rutine.component.html',

  styleUrls: [
    '../assets/css/style.css',
    '../assets/css/responsive.css',
    '../assets/css/bootstrap.css'
  ]
})
@Injectable({providedIn: 'root'})
export class RutineComponent implements OnInit{




  constructor(private loginservice: LoginService, private personService:PersonService, private rutineService:RutineService, public router: Router,activatedRoute:ActivatedRoute){
    const id = activatedRoute.snapshot.params['id'];
    this.sameUser=false;
    if (id) {
      rutineService.getRutine(id).subscribe(
        response => this.rutine = response as Rutine

      );

    } else {
      this.router.navigate(['/mainPage/']);
    }
  }

  rutine: Rutine;
  person: Person;
  sameUser: boolean;
  imageUrl: string | undefined;




  ngOnInit(): void {
    this.personService.getPerson().subscribe(
      response => {
          this.person= response as Person;
          if(this.person.alias===this.rutine.person){
            this.sameUser=true;
          }
      },
      error => {
        this.person = {alias:"",name:"",date:"",weight:0, roles:[]};
        this.sameUser=false;

      }
  );
      this.personService.getImageByAlias(this.rutine.person).subscribe(data => {
        if(data){
        const blob = new Blob([data], { type: 'image/jpeg' });
        this.imageUrl = URL.createObjectURL(blob);

        }
        else{
          this.imageUrl = undefined;
        }
      });

    }
    deleteRutine($event: MouseEvent) {
      this.rutineService.deleteRutine(this.rutine.id);
      this.router.navigate(['/mainPage']);

    }
    editRutine($event: MouseEvent) {
      this.router.navigate(['/editRutine/'+this.rutine.id])
      }


  }



