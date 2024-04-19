import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LoginService } from '../../services/login.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: [
    '../assets/css/style.css',
    '../assets/css/responsive.css'
  ]
})
export class HeaderComponent {
  admin:boolean = false;
  search: boolean = false;
  constructor(public loginservice:LoginService) { }

  ngOnInit(): void {

  }

}
