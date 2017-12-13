import { Component, OnInit, Input, OnDestroy } from '@angular/core';

// rxjs
import { Subscription } from 'rxjs/Subscription';

// services
import { AlertService } from '../../services/alert.service';

// entities
import { AlertMessage } from '../../entities/AlertMessage';

// jquery
import * as $ from 'jquery';

@Component({
  selector: 'app-alerts',
  templateUrl: './alerts.component.html',
  styleUrls: ['./alerts.component.css']
})

export class AlertsComponent implements OnInit, OnDestroy {
    private alerts: AlertMessage[];
    private alertService: AlertService;
    private subscription: Subscription;

    constructor(alertService: AlertService) {
        this.alertService = alertService;
        this.alerts = [];
    }

    public getAlertClass(alert: AlertMessage): string {
        const classes = [
            'show',
            'alert',
            'alert-dismissable',
            'm-0',
            'mb-1'
        ];

        switch ( alert.category ) {
            case AlertMessage.CATEGORY_SUCCESS :
                classes.push('alert-success');
                break;
            case AlertMessage.CATEGORY_INFO :
                classes.push('alert-info');
                break;
            case AlertMessage.CATEGORY_ERROR :
                classes.push('alert-danger');
                break;
            case AlertMessage.CATEGORY_WARNING :
                classes.push('alert-warning');
                break;
            default :
                classes.push('alert-secondary');
        }

        return classes.join(' ');
    }

    public remove(alert: AlertMessage): void {
        this.alerts = this.alerts.filter( a => a.id !== alert.id);
    }

    private addAlert(alert: AlertMessage) {
        this.alerts.push(alert);

        setTimeout( () => {
            this.remove( alert );
        }, 10000);

        // $(window).scrollTop(0);
    }

  ngOnInit() {
      this.subscription = this.alertService.getAlerts().subscribe( (alert) => this.addAlert(alert) );
  }

  ngOnDestroy(): void {
      this.subscription.unsubscribe();
  }

}
