import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

//import { RouteGuard } from './guards/route.guard';

// components ( for routes )
import { HomePageComponent } from './components/home-page/home-page.component';
import { FootballComponent } from './components/football/football.component';

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
