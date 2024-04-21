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
  @Input() searchOptions: { search: boolean, search2: boolean, admin:boolean } = { search: false, search2: true, admin:false };
  constructor(public loginservice:LoginService) { }

  ngOnInit(): void {

  }

}
