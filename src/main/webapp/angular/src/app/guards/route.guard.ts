import { Injectable, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';

// services
import { RoutePermissionService } from '../services/route-permission.service';



@Injectable()
export class RouteGuard implements CanActivate {
    private routePermService: RoutePermissionService;
    private router: Router;

    constructor(
      routePermService: RoutePermissionService,
      router: Router
    ) {
        this.routePermService = routePermService;
        this.router = router;
    }

    public canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
      const allowed = this.routePermService.authorize(route.routeConfig);

       if ( allowed ) {
           return true;
       } else {
           this.router.navigate(['home']);
           return false;
       }
    }
}
