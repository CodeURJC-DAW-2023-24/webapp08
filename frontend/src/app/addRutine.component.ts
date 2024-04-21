import { LoginService } from './../../services/login.service';
import { Component} from '@angular/core';
import {  OnInit , ViewChild} from '@angular/core';
import { Rutine } from '../../models/rutine.model';
import { RutineService } from './../../services/rutine.service';
import { Injectable } from '@angular/core';
import { Router,ActivatedRoute } from '@angular/router';


@Component({
  selector: 'addRutine',
  templateUrl: './addRutine.component.html',

  styleUrls: [
    '../assets/css/style.css',
    '../assets/css/responsive.css',
    '../assets/css/bootstrap.css'
  ]
})
@Injectable({providedIn: 'root'})
export class AddRutineComponent implements OnInit{


  constructor(private loginService: LoginService, private rutineService:RutineService, private router: Router,activatedRoute:ActivatedRoute){
    const id = activatedRoute.snapshot.params['id'];
    if (id) {
      rutineService.getRutine(id).subscribe(
        response => this.rutine = response as Rutine

      );
      this.isNew = false;
    } else {
      this.rutine={name:"",date:new Date('2024-04-21'),time:0,person:""};
      this.isNew = true;
    }
  }

  rutine: Rutine;
  isNew: boolean;



  ngOnInit(): void {
    if (this.loginService.isLogged()){
      this.rutine.person= this.loginService.currentUser().alias;


  }
  else {
    this.loginService.logInReq();
  }
 }
    newRutine($event: Event) {
      this.rutineService.saveRutine(this.rutine,this.isNew).subscribe(
        response=>{
          this.rutine = response as Rutine;
          this.isNew=false;
          this.router.navigate(['/mainPage/']);
        }

      );
    }

    saveRutine($event: MouseEvent) {
      
     this.rutineService.saveRutine(this.rutine,this.isNew).subscribe(
      response=>{
        this.rutine = response as Rutine;
        this.isNew=false;
        this.router.navigate(['/addExRutine/'+this.rutine.id]);
      }

    );

  }
  cancel($event: MouseEvent) {
    if(!this.isNew){
    this.rutineService.deleteRutine(this.rutine.id);}
    this.router.navigate(['/mainPage']);

    }



}
