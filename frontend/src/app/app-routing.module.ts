import { NgModule, Component } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login.component';
import { HeaderComponent } from './header.component';

const routes: Routes = [

  {path: '', redirectTo: '/login', pathMatch: 'full' },
  {path:'/mainPage', component: HeaderComponent},
  {path: '/login', component: LoginComponent }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
