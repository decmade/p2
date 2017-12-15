import { Injectable, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HttpModule } from '@angular/http';

import { RouteGuard } from './guards/route.guard';

// components ( for routes )
import { HomePageComponent } from './components/home-page/home-page.component';
import { FootballComponent } from './components/football/football.component';
import { BaseballComponent } from './components/baseball/baseball.component';
import { SoccerComponent } from './components/soccer/soccer.component';
import { HockeyComponent } from './components/hockey/hockey.component';
import { GolfComponent } from './components/golf/golf.component';
import { BasketballComponent } from './components/basketball/basketball.component';
import { TennisComponent } from './components/tennis/tennis.component';
import { FutureComponent } from './components/future/future.component';
import { AboutComponent } from './components/about/about.component';
import { AccountComponent } from './components/accounts/accounts.component';
import { RoutePermissionService } from './services/route-permission.service';

export const navigationRoutes: Routes = [
    // TOP NAV
    {
        path: 'home',
        component: HomePageComponent,
        data: {
            type: 'top',
            title: 'Home',
        },
    },
    {
        path: 'account',
        component: AccountComponent,
        canActivate: [
            RouteGuard,
        ],
        data: {
            type: 'top',
            title: 'My Account',
        },
    },
    {
        path: 'about',
        component: AboutComponent,
        data: {
            type: 'top',
            title: 'How to Bet',
            icon: 'assets/images/question.png',
        },
       
    },

    // SIDE NAV
    {
        path: 'football',
        component: FootballComponent,
        data: {
            type: 'side',
            title: 'Football',
            icon: 'assets/images/football-icon.png',
        },
    },

    {
        path: 'baseball',
        component: BaseballComponent,
        data: {
            type: 'side',
            title: 'Baseball',
            icon: 'assets/images/baseball.png',
        },
    },

    {
        path: 'soccer',
        component: SoccerComponent,
        data: {
            type: 'side',
            title: 'Soccer',
            icon: 'assets/images/Soccer.png',
        },
    },

    {
        path: 'hockey',
        component: HockeyComponent,
        data: {
            type: 'side',
            title: 'Hockey',
            icon: 'assets/images/hockey2.png',
        },
    },

    {
        path: 'golf',
        component: GolfComponent,
        data: {
            type: 'side',
            title: 'Golf',
            icon: 'assets/images/golf.png',
        },
    },

    {
        path: 'basketball',
        component: BasketballComponent,
        data: {
            type: 'side',
            title: 'Basketball',
            icon: 'assets/images/basketball2.png',
        },
    },

    {
        path: 'tennis',
        component: TennisComponent,
        data: {
            type: 'side',
            title: 'Tennis',
            icon: 'assets/images/tennis.png',
        },
    },

    {
        path: 'future',
        component: FutureComponent,
        data: {
            type: 'side',
            title: 'Betting Calculator',
            icon: 'assets/images/calculator.png',
        },
       
    },
    

    // DEFAULT
    {
        path: '**',
        redirectTo: 'home',
    },
];

@NgModule({
    exports: [ RouterModule ],
    imports: [
        RouterModule.forRoot( navigationRoutes ),
        HttpModule
    ],
})

@Injectable()
export class AppRoutingModule {
    private routePermService: RoutePermissionService;

    constructor(routePermService: RoutePermissionService ) {
        this.routePermService = routePermService;
    }

    public getRoutes(): Routes {
        return navigationRoutes
            .filter( (route) => route.hasOwnProperty('data') )
            .filter( (route) => this.routePermService.authorize(route) );
    }

    public getTopNavigation(): Routes {
        return this.getRoutes().filter( (route) => route.data.type === 'top' );
    }

    public getSideNavigation(): Routes {
        return this.getRoutes().filter( (route) => route.data.type === 'side' );
    }
}
