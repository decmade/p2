import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

// rxjs
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

// service
import { ApiService } from './api.service';
// entites
import { Account } from '../entities/Account';
import { User } from '../entities/User';

@Injectable()
export class AccountService {

    private http: HttpClient;
    private currentAccountSubject: BehaviorSubject<Account>;
    private apiService: ApiService;
    private acc: Account;

    constructor(httpClient: HttpClient) {
        this.http = httpClient;

        this.currentAccountSubject = new BehaviorSubject(null);
    }

    public getCurrentAccount() {
        return this.currentAccountSubject.asObservable();
    }

    public viewBalance(user: User) {
        const url = 'http://localhost:8080/bet/accounts/' + user.account.id;

        this.http.get<Account>(url).subscribe( (account) => {
            this.currentAccountSubject.next(account);
        });
    }

}
