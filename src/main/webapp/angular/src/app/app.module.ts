import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';


// ROUTING
import { AppRoutingModule } from './app-routing.module';

// VIEW COMPONENTS
import { AppComponent } from './app.component';
import { MainNavigationComponent } from './components/main-navigation/main-navigation.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { SideNavigationComponent } from './components/side-navigation/side-navigation.component';
import { FootballComponent } from './components/football/football.component';
<<<<<<< HEAD

import { AccountComponent } from './components/accounts/accounts.component';
import { TransactionsComponent } from './components/transactions/transactions.component';
import { TransactionTypeComponent } from './components/transactions/transactiontype/transactiontype.component';
=======
import { AccountComponent } from './accounts/accounts.component';
import { TransactionsComponent } from './transactions/transactions.component';
import { TransactionTypeComponent } from './transactions/transactiontype/transactiontype.component';

>>>>>>> origin/master


@NgModule({
  declarations: [
<<<<<<< HEAD
=======
    // pipes


>>>>>>> origin/master
    // components
    AppComponent,
    MainNavigationComponent,
    HomePageComponent,
    SideNavigationComponent,
    FootballComponent,
<<<<<<< HEAD

    // pipes

    AppComponent,
=======
>>>>>>> origin/master
    AccountComponent,
    TransactionsComponent,
    TransactionTypeComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
  ],
  providers: [

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
