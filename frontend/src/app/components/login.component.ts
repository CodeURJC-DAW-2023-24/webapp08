import { LoginService } from '../services/login.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',

  styleUrls: [
    '../../assets/css/style.css',
    '../../assets/css/responsive.css',
    '../../assets/css/bootstrap.css'
  ]
})
export class LoginComponent {
  constructor(public loginservice:LoginService){ }



  logIn(event: any, username:string, password: string){
    event.preventDefault();
    this.loginservice.logIn(username,password)
  }

  logOut(){
    this.loginservice.logOut();
  }
}
