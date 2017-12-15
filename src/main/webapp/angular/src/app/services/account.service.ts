import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

// rxjs
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

// service
import { ApiService } from './api.service';
import { AuthenticationService } from './authentication.service';

// entites
import { Account } from '../entities/Account';
import { User } from '../entities/User';


@Injectable()
export class AccountService {

    private apiService: ApiService;
    private authService: AuthenticationService;
    private http: HttpClient;

    private currentAccountSubject: BehaviorSubject<Account>;
    private savedAccountSubject: Subject<Account>;
    private api: string;
 

    constructor(httpClient: HttpClient, apiSerivce: ApiService, authService: AuthenticationService) {
        this.apiService = apiSerivce;
        this.authService = authService;
        this.http = httpClient;

        this.currentAccountSubject = new BehaviorSubject(null);
        this.savedAccountSubject = new Subject();
        this.api = 'accounts';

        this.listenForCurrentUser();
    }

    public getCurrentAccount(): Observable<Account> {
        return this.currentAccountSubject.asObservable();
    }

    public getSavedAccount(): Observable<Account> {
        return this.savedAccountSubject.asObservable();
    }

    public save(account: Account): void {
        const data = JSON.stringify(account);
        const url = this.apiService.getApiUrl(this.api);

        this.http.post<Account>(url, data, { 
            withCredentials: true, 
            headers: { 'Content-Type': 'application/json' }
        }).subscribe( (account) => {
            this.savedAccountSubject.next(account);
        });
    }

    private listenForCurrentUser(): void {
        this.authService.getCurrentUser().subscribe( (user) => {
            if (user) {
                this.currentAccountSubject.next( user.account );
            } else {
                this.currentAccountSubject.next(null);
            }
        });
    }

}
