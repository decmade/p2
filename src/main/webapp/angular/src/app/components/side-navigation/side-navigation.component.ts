import { Component, OnInit } from '@angular/core';
import { AppRoutingModule } from '../../app-routing.module';


@Component({
  selector: 'app-side-navigation',
  templateUrl: './side-navigation.component.html',
  styleUrls: ['./side-navigation.component.css']
})

export class SideNavigationComponent implements OnInit {
  private routingModule: AppRoutingModule;

  constructor(routingModule: AppRoutingModule) {
    this.routingModule = routingModule;
  }

  public getRoutes() {
      return this.routingModule.getSideNavigation();
  }

  ngOnInit() {
  }

}
