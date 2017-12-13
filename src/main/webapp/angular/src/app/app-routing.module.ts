import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HttpModule } from '@angular/http';

//import { RouteGuard } from './guards/route.guard';

// components ( for routes )
import { HomePageComponent } from './components/home-page/home-page.component';
import { FootballComponent } from './components/football/football.component';
import { AccountComponent } from './components/accounts/accounts.component';
// import { HttpModule } from '@angular/http/src/http_module';

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
        data: {
            type: 'top',
            title: 'Account',
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

export class AppRoutingModule {
    public static getRoutes(): Routes {
        return navigationRoutes.filter( (route) => route.hasOwnProperty('data') );
    }

    public static getTopNavigation(): Routes {
        return this.getRoutes().filter( (route) => route.data.type === 'top' );
    }

    public static getSideNavigation(): Routes {
        return this.getRoutes().filter( (route) => route.data.type === 'side' );
    }
}
