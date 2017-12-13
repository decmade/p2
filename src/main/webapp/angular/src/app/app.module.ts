import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

// PIPES
import { UserDisplayNamePipe } from './pipes/user-display-name.pipe';

// SERVICES
import { AuthenticationService } from './services/authentication.service';


// ROUTING
import { AppRoutingModule } from './app-routing.module';

// VIEW COMPONENTS
import { AppComponent } from './app.component';
import { MainNavigationComponent } from './components/main-navigation/main-navigation.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { SideNavigationComponent } from './components/side-navigation/side-navigation.component';
import { FootballComponent } from './components/football/football.component';
import { BaseballComponent } from './components/baseball/baseball.component';
import { AccountComponent } from './accounts/accounts.component';
import { TransactionsComponent } from './transactions/transactions.component';
import { TransactionTypeComponent } from './transactions/transactiontype/transactiontype.component';
import { SoccerComponent } from './components/soccer/soccer.component';
import { HockeyComponent } from './components/hockey/hockey.component';
import { GolfComponent } from './components/golf/golf.component';
import { BasketballComponent} from './components/basketball/basketball.component';
import { TennisComponent } from './components/tennis/tennis.component';




@NgModule({
  declarations: [
    // pipes
    UserDisplayNamePipe,

    // components
    AppComponent,
    MainNavigationComponent,
    HomePageComponent,
    SideNavigationComponent,
    FootballComponent,
    AccountComponent,
    TransactionsComponent,
    TransactionTypeComponent,
    BaseballComponent,
    SoccerComponent,
    HockeyComponent,
    GolfComponent,
    BasketballComponent,
    TennisComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
  ],
  providers: [
    AuthenticationService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
