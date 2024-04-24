
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { NgApexchartsModule } from "ng-apexcharts";

import { AppRoutingModule } from './app-routing.module';
import { HeaderComponent } from './header.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LoginComponent } from './login.component';
import { HttpClientModule } from '@angular/common/http';
import { MainPageComponent } from './main-page.component';
import { StatisticsComponent } from './statistics.component';
import { PersonComponent } from './person.component';
import {SearchExerciseComponent} from './searchExercise.component';
import { AddRutineComponent } from './addRutine.component';
import { AddExRutineComponent } from './addExRutine.component';
import { RutineComponent } from './rutine.component';

import { PersonService } from './../../services/person.service';
import { LoginService } from './../../services/login.service';
import { MainPageService } from '../../services/main-page.service';
import { RutineService } from './../../services/rutine.service';
import { ExerciseService } from './../../services/exercise.service';
import { CommunityComponent } from './community.component';
import { NewExerciseComponent } from './newExercise.component';
import { SingleExerciseComponent } from './singleExercise.component';
import { HeaderService } from '../../services/header.service';
import { CommunityService } from '../../services/community.service';
import { StatisticsService } from '../../services/statistics.service';
import {ExerciseListComponent} from './exerciseList.component';
import { NewPersonComponent } from './newPerson.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LoginComponent,
    MainPageComponent,
    StatisticsComponent,
    PersonComponent,
    SearchExerciseComponent,
    AddRutineComponent,
    AddExRutineComponent,
    RutineComponent,
    CommunityComponent,
    NewExerciseComponent,
    SingleExerciseComponent,
    ExerciseListComponent,
    NewPersonComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule,
    HttpClientModule,
    NgApexchartsModule
  ],
  providers: [PersonService,
    LoginService,
    MainPageService,
    RutineService,
    ExerciseService,
    HeaderService,
    CommunityService,
    StatisticsService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
