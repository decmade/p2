import { Component, OnInit, OnDestroy } from '@angular/core';
import { Routes, Router } from '@angular/router';
import { LoginForm } from '../../entities/LoginForm';
import { AppRoutingModule } from '../../app-routing.module';

import { Subscription } from 'rxjs/Subscription';

// visual imports
import * as $ from 'jquery';



// routes
// import { navigationRoutes } from '../../app-routing.module';

// services
import { AuthenticationService } from '../../services/authentication.service';
import { UserService } from '../../services/user.service';
import { AccountService } from '../../services/account.service';

// entities
import { User } from '../../entities/User';
import { Account } from '../../entities/Account';



@Component({
  selector: 'app-main-navigation',
  templateUrl: './main-navigation.component.html',
  styleUrls: ['./main-navigation.component.css']
})

export class MainNavigationComponent implements OnInit, OnDestroy {
  private router: Router;
  private form: LoginForm;
  private user: User;
  private userAcc: Account;

  private currentUserSubscription: Subscription;

  // consumed services
  private authService: AuthenticationService;
  private userService: UserService;
  private accService: AccountService;

  constructor(router: Router, authService: AuthenticationService, userService: UserService, accService: AccountService) {
      this.router = router;

      this.authService = authService;
      this.userService = userService;
      this.accService = accService;

      this.form = new LoginForm();
      this.form.elementId = 'login-form';
      this.user = null;
  }

  /*
    * I can make the navigation bar dynamic based
    * on policies here
    */
    public getRoutes(): Routes {
        return AppRoutingModule.getTopNavigation();
    }

    public onLogin(): void {
        this.login();
        this.resetForm();
    }

    public onLogout(): boolean {
        this.logout();
        return false;
    }

    public onLoginFormFocus(): void {
        const element = $(`#${this.form.elementId}`);

        element.find('.fa-user-o').removeClass('fa-user-o').addClass('fa-user');
        element.find('.btn-outline-dark').removeClass('btn-outline-dark').addClass('btn-dark');
    }

    public onLoginFormBlur(): void {
        const element = $(`#${this.form.elementId}`);

        element.find('.fa-user').removeClass('fa-user').addClass('fa-user-o');
        element.find('.btn-dark').removeClass('btn-dark').addClass('btn-outline-dark');
    }

    public onRegister(): void {
      this.userService.selectNewUser();

    }

    public viewAccountBalance(): Number {
        this.accService.viewBalance(this.user);
        this.userAcc = this.user.account;
        return this.userAcc.balance;
      }

    private login(): void {
        if ( this.validateLoginForm(this.form) ) {
            this.authService.login(this.form);
        }
    }

    private logout(): void {
        this.authService.logout();
    }

    private resetForm(): void {
        this.form.password = '';
    }

    private validateLoginForm(form): boolean {
        if ( form.identity.length === 0 ) {
            return false;
        }

        if ( form.password.length === 0 ) {
            return false;
        }

        return true;
    }

    ngOnInit() {
        this.currentUserSubscription = this.authService.getCurrentUser()
            .subscribe( (user) => this.user = user );
    }

    ngOnDestroy(): void {
        this.currentUserSubscription.unsubscribe();
    }

}
