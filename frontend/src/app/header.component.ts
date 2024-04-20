import { Component, OnInit, Input } from '@angular/core';
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
  @Input() searchOptions: { search: boolean, search2: boolean } = { search: false, search2: false };
  constructor(public loginservice:LoginService) { }

  ngOnInit(): void {

  }

}
