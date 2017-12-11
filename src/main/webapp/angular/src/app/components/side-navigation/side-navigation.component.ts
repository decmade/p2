import { Component, OnInit } from '@angular/core';
import { AppRoutingModule } from '../../app-routing.module';

@Component({
  selector: 'app-side-navigation',
  templateUrl: './side-navigation.component.html',
  styleUrls: ['./side-navigation.component.css']
})

export class SideNavigationComponent implements OnInit {

  constructor() {

  }

  public getRoutes() {
      return AppRoutingModule.getSideNavigation();
  }

  ngOnInit() {
  }

}
