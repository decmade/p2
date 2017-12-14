import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

// rxjs
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

// service
import { ApiService } from './api.service';
import { AlertService } from './alert.service';
import { AlertMessage } from '../entities/AlertMessage';

// entites
import { Account } from '../entities/Account';
import { User } from '../entities/User';
import { Transaction } from '../entities/Transaction';

@Injectable()
export class TransactionService {

    private http: HttpClient;
    private currentAccountSubject: BehaviorSubject<Account>;
    private apiService: ApiService;
    private alertService: AlertService;
    private trans: Transaction;
    private api: string;
    private savedTransactionSubject: Subject<Transaction>;

    constructor(httpClient: HttpClient, apiService: ApiService, alertService: AlertService) {
        this.http = httpClient;
        this.api = 'transactions';
        this.alertService = alertService;
        this.savedTransactionSubject = new Subject();
        this.currentAccountSubject = new BehaviorSubject(null);
    }

    public getSavedTransactions(): Observable<Transaction> {
        return this.savedTransactionSubject.asObservable();
    }

    public save(trans: Transaction): void {
        const data = JSON.stringify(trans);
        const url = this.apiService.getApiUrl( this.api );

        this.http.post<Transaction>(url, data).subscribe( (savedTrans) => {
            this.savedTransactionSubject.next(savedTrans);
            this.alertService.push('transaction occurred successfully', AlertMessage.CATEGORY_SUCCESS );
        }, (error) => {
            this.alertService.push('transaction failed', AlertMessage.CATEGORY_ERROR);
        });
    }

}
