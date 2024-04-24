import { Exercise } from './../../models/exercise.model';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { LoginService } from './../../services/login.service';
import { PersonService } from './../../services/person.service';
import { Person } from '../../models/person.model';
import { Page } from '../../models/page.model';
import { ExerciseService } from '../../services/exercise.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'exercisesList',
  templateUrl: './exerciseList.component.html',
  styleUrls: [
    '../assets/css/style.css',
    '../assets/css/responsive.css',
    '../assets/css/bootstrap.css'
  ]
})
export class ExerciseListComponent implements OnInit, OnDestroy {
  admin: boolean;
  person: Person;
  roles: String[];
  next: boolean;
  previous: boolean;
  list: Page;
  group: string;
  pageN: number;
  routerSubscription: Subscription;
  id:any;

  constructor(
    public loginservice: LoginService,
    public exerciseService: ExerciseService,
    public personService: PersonService,
    public router: Router,
    public activatedRoute: ActivatedRoute
  ) {this.routerSubscription = this.router.events.subscribe((event) => {
    if (event instanceof NavigationEnd) {
      const grp = this.activatedRoute.snapshot.params['grp'];
      const page = this.activatedRoute.snapshot.params['page'];
      this.group = grp;
      this.pageN = +page;
      this.loadExercises();
    }
  });}

  ngOnInit(): void {
    this.personService.getPerson().subscribe((response) => {
      this.person = response as Person;
      this.roles = this.person.roles;
      if (this.roles.includes('ADMIN')) {
        this.admin = true;
      } else {
        this.admin = false;
      }
    });


  }

  loadExercises() {
    this.exerciseService
      .getExercisesByGroupPageable(this.group, this.pageN)
      .subscribe((response) => {
        this.list = response as Page;
        if (this.list.last) {
          this.next = false;
        } else {
          this.next = true;
        }
        if (this.pageN != 0 && this.pageN != this.list.totalPages) {
          this.previous = true;
        } else {
          this.previous = false;
        }
      });
  }

  previousPage($event: Event) {
    this.pageN = this.pageN - 1;
    this.router.navigate(['../exercisesList', this.group, this.pageN]);
  }
  nextPage($event: Event) {
    this.pageN = this.pageN + 1;
    this.router.navigate(['../exercisesList', this.group, this.pageN]);
  }

  ngOnDestroy() {
    this.routerSubscription.unsubscribe();
  }

  sendToExercise(exerciseId: any) {
    if (exerciseId) {
      this.router.navigate(['../exercise',exerciseId])
    }
  }


}
