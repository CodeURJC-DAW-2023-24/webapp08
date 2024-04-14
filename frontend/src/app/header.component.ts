import { Component } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: [
    '../assets/css/style.css',
    '../assets/css/responsive.css'
  ]
})
export class HeaderComponent {
  title = 'Header';
}
