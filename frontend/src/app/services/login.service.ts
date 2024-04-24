import { ErrorService } from './error.service';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Person } from '../models/person.model';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { Observable, throwError } from 'rxjs';
const BASE_URL = '/api/auth';

@Injectable({ providedIn: 'root' })
export class LoginService {

    logged: boolean;
    user: Person ;

    constructor(private http: HttpClient, public router: Router, private errorService: ErrorService) {
    }

    reqIsLogged() {

        this.http.get('/api/persons/', { withCredentials: true }).subscribe(
            response => {
                this.user = response as Person;
                this.logged = true;
                this.router.navigate(['../mainPage']);
            },
            error => {
                if (error.status != 404) {
                    console.error('Error when asking if logged: ' + JSON.stringify(error));
                }
            }
        );

    }

    logIn(user: string, pass: string) {

        this.http.post(BASE_URL + "/login", { username: user, password: pass }, { withCredentials: true })
            .subscribe(
                (response) => this.reqIsLogged(),
                (error) => {
                this.router.navigate(['../error']);
                this.errorService.setincorrectUserPass(true);}
            );

    }

    logOut() {

        return this.http.post(BASE_URL + '/logout', { withCredentials: true })
            .subscribe((resp: any) => {
                console.log("LOGOUT: Successfully");
                this.logged = false;
                this.logInReq();
            });

    }

    isLogged() {
        return this.logged;
    }

    isAdmin() {
        return this.user && this.user.roles.indexOf('ADMIN') !== -1;
    }

    currentUser() {
        return this.user;
    }

    logInReq(){
      this.router.navigate(['../login']);
    }

}
