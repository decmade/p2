import { Component, OnInit, OnDestroy } from '@angular/core';
import { Routes, Router } from '@angular/router';
import { LoginForm } from '../../entities/login-form';
import { AppRoutingModule } from '../../app-routing.module';

// visual imports
import * as $ from 'jquery';

// routes
// import { navigationRoutes } from '../../app-routing.module';

// entities
import { User } from '../../entities/user';


@Component({
  selector: 'app-main-navigation',
  templateUrl: './main-navigation.component.html',
  styleUrls: ['./main-navigation.component.css']
})

export class MainNavigationComponent implements OnInit, OnDestroy {
  private router: Router;
  private form: LoginForm;
  private user: User;

  constructor(router: Router) {
      this.router = router;

      this.form = new LoginForm();
      this.form.elementId = 'login-form';
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


    private login(): void {
        // if ( this.validateLoginForm(this.form) ) {
        //     this.loginService.login(this.form.identity, this.form.credential);
        // }
    }

    private logout(): void {
        // this.loginService.logout();
    }

    private resetForm(): void {
        this.form.password = '';
    }

    private validateLoginForm(form): boolean {
        if ( form.identity.length === 0 ) {
            return false;
        }

        if ( form.credential.length === 0 ) {
            return false;
        }

        return true;
    }

    ngOnInit() {
        // this.loginSubscription = this.loginService.getCurrentUser()
        //     .subscribe( (user) => this.user = user );
    }

    ngOnDestroy(): void {
        // this.loginSubscription.unsubscribe();
    }

}
