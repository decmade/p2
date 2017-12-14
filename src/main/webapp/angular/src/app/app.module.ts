import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

// PIPES
import { UserDisplayNamePipe } from './pipes/user-display-name.pipe';
import { CityStatePipe } from './pipes/city-state.pipe';
import { TelephonePipe } from './pipes/telephone.pipe';

// SERVICES
import { AuthenticationService } from './services/authentication.service';
import { UserService } from './services/user.service';
import { ZipCodeService } from './services/zip-code.service';
import { ApiService } from './services/api.service';
import { AlertService } from './services/alert.service';
import { AccountService } from './services/account.service';
import { GameService } from './services/game.service';
import { GoogleMapService} from './services/google-map.service';


// ROUTING
import { AppRoutingModule } from './app-routing.module';

// VIEW COMPONENTS
import { AppComponent } from './app.component';
import { AlertsComponent } from './components/alerts/alerts.component';
import { MainNavigationComponent } from './components/main-navigation/main-navigation.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { SideNavigationComponent } from './components/side-navigation/side-navigation.component';
import { FootballComponent } from './components/football/football.component';

import { BaseballComponent } from './components/baseball/baseball.component';
import { AccountComponent } from './components/accounts/accounts.component';
import { TransactionsComponent } from './components/transactions/transactions.component';
import { TransactionTypeComponent } from './components/transactions/transactiontype/transactiontype.component';
import { UserDetailComponent } from './components/user-detail/user-detail.component';
import { SoccerComponent } from './components/soccer/soccer.component';
import { HockeyComponent } from './components/hockey/hockey.component';
import { GolfComponent } from './components/golf/golf.component';
import { BasketballComponent} from './components/basketball/basketball.component';
import { TennisComponent } from './components/tennis/tennis.component';
import { FutureComponent } from './components/future/future.component';
import { AboutComponent } from './components/about/about.component';


import { GameDetailComponent } from './components/game-detail/game-detail.component';

@NgModule({
  declarations: [
    // pipes
    UserDisplayNamePipe,
    TelephonePipe,

    // components
    AppComponent,
    AlertsComponent,
    MainNavigationComponent,
    HomePageComponent,
    SideNavigationComponent,
    FootballComponent,

    // pipes

    AppComponent,
    AccountComponent,
    TransactionsComponent,
    TransactionTypeComponent,
    UserDetailComponent,
    CityStatePipe,
    BaseballComponent,
    SoccerComponent,
    HockeyComponent,
    GolfComponent,
    BasketballComponent,
    TennisComponent,
    FutureComponent,
    AboutComponent,
    
    GameDetailComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
  ],
  providers: [
    AuthenticationService,
    UserService,
    ZipCodeService,
    ApiService,
    AlertService,
    AccountService,
    GameService,
    GoogleMapService,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
