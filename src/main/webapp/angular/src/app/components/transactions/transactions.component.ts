import { Component, OnInit } from '@angular/core';

import { Transaction } from '../../entities/Transaction';

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css']
})
export class TransactionsComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
