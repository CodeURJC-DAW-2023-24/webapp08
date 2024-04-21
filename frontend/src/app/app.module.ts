
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
import { AddRutineComponent } from './addRutine.component';
import { AddExRutineComponent } from './addExRutine.component';

import { PersonService } from './../../services/person.service';
import { LoginService } from './../../services/login.service';
import { MainPageService } from '../../services/main-page.service';
import { RutineService } from './../../services/rutine.service';
import { ExerciseService } from './../../services/exercise.service';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    LoginComponent,
    MainPageComponent,
    StatisticsComponent,
    PersonComponent,
    AddRutineComponent,
    AddExRutineComponent
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
    MainPageComponent,
    RutineService,
    ExerciseService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
