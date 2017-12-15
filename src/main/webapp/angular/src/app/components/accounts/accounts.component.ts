import { Component, OnInit, OnDestroy } from '@angular/core';

// rxjs
import { Subscription } from 'rxjs/Subscription';

// entities
import { Account } from '../../entities/Account';
import { Transaction } from '../../entities/Transaction';
import { User } from '../../entities/User';

// service
import { AccountService } from '../../services/account.service';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})

export class AccountComponent implements OnInit, OnDestroy {

  private currentAccountSubscription: Subscription;

  private user: User;
  private account: Account;

  private accountService: AccountService;


  constructor(accService: AccountService ) {
    this.accountService = accService;
  }

  public getAmountClass(transaction: Transaction): any {
      return {
        'text-success': ( transaction.type.id === 1),
        'text-danger' : ( transaction.type.id === 2),
      };
  }

  ngOnInit(): void {
    this.currentAccountSubscription = this.accountService.getCurrentAccount()
      .subscribe( (account) => this.account = account );
  }

  ngOnDestroy(): void {
    this.currentAccountSubscription.unsubscribe();
  }
}
