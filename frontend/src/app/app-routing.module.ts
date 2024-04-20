import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login.component';
import { MainPageComponent } from './main-page.component';
import { StatisticsComponent } from './statistics.component';
import { PersonComponent } from './person.component';
import { HeaderComponent } from './header.component';
import {SearchExerciseComponent} from './searchExercise.component';

const routes: Routes = [
  {path:'mainPage', component: MainPageComponent},
  {path: 'login', component: LoginComponent },
  {path:'mainPage/statistics', component: StatisticsComponent},
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {path: 'person', component: PersonComponent },
  {path:'searchExercise', component:SearchExerciseComponent}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
