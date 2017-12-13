import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Http, Response } from '@angular/http';
import { BehaviorSubject } from 'rxjs/Rx';
import { Observable } from 'rxjs/Observable';

import { Account } from '../../entities/Account';
import { User } from '../../entities/User';

import { Subscription } from 'rxjs/Subscription';
import { AuthenticationService } from '../../services/authentication.service';
import { AccountService } from '../../services/account.service';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})
export class AccountComponent implements OnInit, OnDestroy {

  currentUserSubscription: Subscription;
  user: User;
  private authService: AuthenticationService;
  private accService: AccountService;

  constructor(authService: AuthenticationService, accService: AccountService) {
    this.authService = authService;
    this.accService = accService;
  }
  ngOnInit(): void {
    this.currentUserSubscription = this.authService.getCurrentUser().subscribe( (user) => this.user = user );
  }

  viewAccountBalance() {
    this.accService.viewBalance(this.user);
  }

ngOnDestroy(): void {
  this.currentUserSubscription.unsubscribe();
}
}
