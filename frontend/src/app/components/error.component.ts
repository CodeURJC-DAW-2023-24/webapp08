import { Component, OnInit } from '@angular/core';
import { ErrorService } from '../services/error.service';
@Component({
  selector: 'error',
  templateUrl: './error.component.html',
  styleUrls: [
    '../../assets/css/style.css',
    '../../assets/css/responsive.css',
    '../../assets/css/bootstrap.css'
  ]
})
export class ErrorComponent implements OnInit {
  incorrectPU: boolean;
  incorrectUserPass:boolean;
  blanks:boolean;
  existingUser:boolean;
  existingEx:boolean;

  constructor(private errorService: ErrorService) { }

  ngOnInit(): void {
    this.errorService.incorrectPU$.subscribe(value => {
      this.incorrectPU = value;
    });
    this.errorService.incorrectUserPass$.subscribe(value => {
      this.incorrectUserPass = value;
    });
    this.errorService.blanks$.subscribe(value => {
      this.blanks = value;
    });
    this.errorService.existingUser$.subscribe(value => {
      this.existingUser = value;
    });
    this.errorService.existingEx$.subscribe(value => {
      this.existingEx = value;
    });


  }
}
