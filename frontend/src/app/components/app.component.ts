import { Component, HostListener } from '@angular/core';

@Component({
  selector: 'app-root',
  template:  `<router-outlet></router-outlet>`
})
export class AppComponent {
  admin: boolean = false;



}
