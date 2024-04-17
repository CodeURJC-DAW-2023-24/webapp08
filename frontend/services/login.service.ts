import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Person } from '../models/person.model';
import { Router } from '@angular/router';

const BASE_URL = '/api/auth';

@Injectable({ providedIn: 'root' })
export class LoginService {

    logged: boolean;
    user: Person | undefined;

    constructor(private http: HttpClient, public router: Router) {
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
                (error) => alert("Wrong credentials")
            );

    }

    logOut() {

        return this.http.post(BASE_URL + '/logout', { withCredentials: true })
            .subscribe((resp: any) => {
                console.log("LOGOUT: Successfully");
                this.logged = false;
                this.user = undefined;
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
}
