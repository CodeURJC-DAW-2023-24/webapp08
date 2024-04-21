import { AddRutineComponent } from './addRutine.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login.component';
import { MainPageComponent } from './main-page.component';
import { StatisticsComponent } from './statistics.component';
import { PersonComponent } from './person.component';
import { HeaderComponent } from './header.component';
import { AddExRutineComponent } from './addExRutine.component';
import {SearchExerciseComponent} from './searchExercise.component';
import { RutineComponent } from './rutine.component';
import { CommunityComponent } from './community.component';

const routes: Routes = [
  {path:'mainPage', component: MainPageComponent},
  {path: 'login', component: LoginComponent },
  {path:'mainPage/statistics', component: StatisticsComponent},
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {path: 'person', component: PersonComponent },
  {path: 'addRutine', component:  AddRutineComponent },
  {path: 'rutine/:id', component:  RutineComponent },
  {path: 'addRutine/:id', component:  AddRutineComponent },
  {path: 'editRutine/:id', component:  AddRutineComponent },
  {path: 'addExRutine/:id', component:  AddExRutineComponent },
  {path:'searchExercise', component:SearchExerciseComponent},
  {path:'mainPage/community', component:CommunityComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
