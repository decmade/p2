import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { AccountComponent } from './accounts/accounts.component';
import { TransactionsComponent } from './transactions/transactions.component';
import { TransactionTypeComponent } from './transactions/transactiontype/transactiontype.component';


@NgModule({
  declarations: [
    AppComponent,
    AccountComponent,
    TransactionsComponent,
    TransactionTypeComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
