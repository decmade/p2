import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';


// ROUTING
import { AppRoutingModule } from './app-routing.module';

// VIEW COMPONENTS
import { AppComponent } from './app.component';
<<<<<<< HEAD
import { MainNavigationComponent } from './components/main-navigation/main-navigation.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { SideNavigationComponent } from './components/side-navigation/side-navigation.component';
import { FootballComponent } from './components/football/football.component';

=======
import { AccountComponent } from './accounts/accounts.component';
import { TransactionsComponent } from './transactions/transactions.component';
import { TransactionTypeComponent } from './transactions/transactiontype/transactiontype.component';
>>>>>>> master


@NgModule({
  declarations: [
<<<<<<< HEAD
    // components
    AppComponent,
    MainNavigationComponent,
    HomePageComponent,
    SideNavigationComponent,
    FootballComponent,

    // pipes

=======
    AppComponent,
    AccountComponent,
    TransactionsComponent,
    TransactionTypeComponent
>>>>>>> master
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
