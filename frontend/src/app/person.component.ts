
import { LoginService } from './../../services/login.service';
import { Component, EventEmitter, Output} from '@angular/core';
import {  OnInit , ViewChild} from '@angular/core';
import { Person } from '../../models/person.model';
import { PersonService } from './../../services/person.service';


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



  constructor(public loginservice:LoginService,public personService: PersonService){
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
  @Output() imageUrlChanged: EventEmitter<string > = new EventEmitter<string >();

  ngOnInit(): void {
      if (this.loginservice.isLogged()){
        this.person= this.loginservice.currentUser();
        this.personService.getImage().subscribe(data => {
          if(data){
          const blob = new Blob([data], { type: 'image/jpeg' });
          this.imageUrl = URL.createObjectURL(blob);

          }
          else{
            this.imageUrl = undefined;
          }
        });

      }
      else{
        this.loginservice.logInReq();
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
