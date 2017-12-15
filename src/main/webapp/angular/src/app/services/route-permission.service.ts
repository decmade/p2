import { Injectable } from '@angular/core';

// services
import { AuthenticationService } from './authentication.service';

// beans
import { User } from '../entities/User';
import { Route } from '@angular/router/src/config';

@Injectable()
export class RoutePermissionService {
    private user: User;

  constructor(
      authService: AuthenticationService,
  ) {
      authService.getCurrentUser().subscribe( (user) => {
          this.user = user;
      });
  }

  public authorize(route: Route): boolean {
      
      if ( route.hasOwnProperty('data') === false ) {
          return true;
      }

    /*
    * allow all side navigation routes
    */
    if ( route.data.type === 'side' ) {
        return true;
    }

      switch ( route.path.toLowerCase() ) {
          case ('home') :
          case ('about') :
              return true;
          case ('account') :
              return ( this.detectAuthenticated( this.user) );
          case ('users') :
              if ( this.detectAuthenticated( this.user) === false ) {
                  return false;
              }

              return ( this.user.role.id === 1);
         default :
             return false;
      }
  }

  /*
    * BEGIN: helper functions
    */
    private detectAuthenticated(user: User) {
        switch (true) {
            case ( user === null ) :
            case ( user === undefined ) :
            case ( user.id === 0 ) :
                // console.log('no user logged in');
                 return false;
            default :
                // console.log('user is logged in');
                return true;
        }
    }

}
