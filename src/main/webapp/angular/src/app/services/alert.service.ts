import { Injectable } from '@angular/core';

// rxjs
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

// entities
import { AlertMessage } from '../entities/AlertMessage';


@Injectable()
export class AlertService {

    private alertSubject: Subject<AlertMessage>;

  constructor() {
      this.alertSubject = new Subject();
  }

  public getAlerts(): Observable<AlertMessage> {
      return this.alertSubject.asObservable();
  }

  public push(message: string, category: string): void {
      const alert = new AlertMessage(message, category);

      this.alertSubject.next(alert);
  }

}
