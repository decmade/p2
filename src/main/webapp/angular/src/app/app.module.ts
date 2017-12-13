import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

// PIPES
import { UserDisplayNamePipe } from './pipes/user-display-name.pipe';
import { UserCityStatePipe } from './pipes/user-city-state.pipe';
import { TelephonePipe } from './pipes/telephone.pipe';

// SERVICES
import { AuthenticationService } from './services/authentication.service';
import { UserService } from './services/user.service';
import { ZipCodeService } from './services/zip-code.service';
import { ApiService } from './services/api.service';


// ROUTING
import { AppRoutingModule } from './app-routing.module';

// VIEW COMPONENTS
import { AppComponent } from './app.component';
import { MainNavigationComponent } from './components/main-navigation/main-navigation.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { SideNavigationComponent } from './components/side-navigation/side-navigation.component';
import { FootballComponent } from './components/football/football.component';
import { AccountComponent } from './accounts/accounts.component';
import { TransactionsComponent } from './transactions/transactions.component';
import { TransactionTypeComponent } from './transactions/transactiontype/transactiontype.component';
import { UserDetailComponent } from './components/user-detail/user-detail.component';





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
    UserDetailComponent,
    UserCityStatePipe,
    TelephonePipe
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
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
