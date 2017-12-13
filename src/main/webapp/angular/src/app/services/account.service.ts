import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

// rxjs
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

// entites
import { Account } from '../entities/Account';
import { User } from '../entities/User';

@Injectable()
export class AccountService {

    private http: HttpClient;
    private currentAccountSubject: BehaviorSubject<Account>;
    private serviceUrl: string;
    private acc: Account;

    constructor(httpClient: HttpClient) {
        this.http = httpClient;

        this.serviceUrl = 'acc';
        this.currentAccountSubject = new BehaviorSubject(null);
    }

    public getCurrentAccount() {
        return this.currentAccountSubject.asObservable();
    }

    public viewBalance() {
        const url = this.getUrl();

        this.http.get<Account>(url + '1').subscribe( (account) => {
            this.currentAccountSubject.next(account);
        });
    }
    private getUrl(): string {
        return [
            'http://localhost:8080/bet',
            this.serviceUrl
        ].join('/');
    }

}
