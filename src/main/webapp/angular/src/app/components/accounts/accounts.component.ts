import { Component, OnInit } from '@angular/core';
import { Account } from '../../entities/Account';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})
export class AccountComponent implements OnInit {

  balance: Account;
  constructor() { }

  ngOnInit() {
  }

}
