import { NgModule} from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login.component';
import { HeaderComponent } from './header.component';

const routes: Routes = [
  {path:'login/mainPage', component: HeaderComponent},
  {path: 'login', component: LoginComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
