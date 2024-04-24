import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ErrorService {
  private _incorrectPUSubject = new BehaviorSubject<boolean>(false);
  incorrectPU$: Observable<boolean> = this._incorrectPUSubject.asObservable();
  private _incorrectUserPass = new BehaviorSubject<boolean>(false);
  incorrectUserPass$: Observable<boolean> = this._incorrectUserPass.asObservable();
  private _blanks = new BehaviorSubject<boolean>(false);
  blanks$: Observable<boolean> = this._blanks.asObservable();
  private _existingUser = new BehaviorSubject<boolean>(false);
  existingUser$: Observable<boolean> = this._existingUser.asObservable();
  private _existingEx = new BehaviorSubject<boolean>(false);
  existingEx$: Observable<boolean> = this._existingEx.asObservable();


  constructor() { }

  setIncorrectPU(value: boolean) {
    this._incorrectPUSubject.next(value);
  }
  setincorrectUserPass(value: boolean) {
    this._incorrectUserPass.next(value);
  }
  setblanks(value: boolean) {
    this._blanks.next(value);
  }

  setexistingUser(value: boolean) {
    this._existingUser.next(value);
  }
  setexistingEx(value: boolean) {
    this._existingEx.next(value);
  }
}
