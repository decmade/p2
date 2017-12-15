import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

// rxjs
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

// entities
import { User } from '../entities/User';
import { AlertMessage } from '../entities/AlertMessage';
import { Account } from '../entities/Account';

// services
import { ApiService } from './api.service';
import { AlertService } from './alert.service';
import { Transaction } from '../entities/Transaction';



@Injectable()
export class UserService {

    private http: HttpClient;
    private apiService: ApiService;
    private alertService: AlertService;

    private selectedUserSubject: Subject<User>;
    private savedUserSubject: Subject<User>;
    private api: string;

    constructor(httpClient: HttpClient, apiService: ApiService, alertService: AlertService) {
        this.http = httpClient;
        this.apiService = apiService;
        this.alertService = alertService;

        this.api = 'users';
        this.selectedUserSubject = new Subject();
        this.savedUserSubject = new Subject();
    }

    public getSelectedUsers(): Observable<User> {
        return this.selectedUserSubject.asObservable();
    }

    public setSelectedUser(user: User) {
        this.selectedUserSubject.next(user);
    }

    public selectNewUser(): void {
        const user = new User();

        user.id = 0;
        user.identity = 'guest';

        this.setSelectedUser(user);
    }

    public getSavedUsers(): Observable<User> {
        return this.savedUserSubject.asObservable();
    }

    public save(user: User): void {
        let data: string;
        const url = this.apiService.getApiUrl(this.api);
        const isNewUser = this.userIsNew(user);

        if ( isNewUser ) {
            user.account = this.generateAccount();
        }

        data = JSON.stringify(user);

        this.http.post<User>(url, data, {
            withCredentials: true,
            headers: { 'Content-Type': 'application/json' }
        }).subscribe( (savedUser) => {
            this.savedUserSubject.next(savedUser);

            if ( isNewUser ) {
               this.alertService.push('user registration successful', AlertMessage.CATEGORY_SUCCESS);
            } else {
                this.alertService.push('profile update successful', AlertMessage.CATEGORY_SUCCESS);
            }
            
        }, (error) => {
            if ( isNewUser) {
                this.alertService.push('user registration failed', AlertMessage.CATEGORY_ERROR);
            } else {
                this.alertService.push('profile update failed', AlertMessage.CATEGORY_ERROR);
            }
            console.log(data);
            console.log(error);
        });
    }

    private userIsNew (user: User): boolean {
        switch (true) {
            case ( user.id === undefined ) :
            case ( user.id === 0 ) :
                return true;
            default :
                return false;
        }
    }

    private generateAccount(): Account {
        return {
            id: 0,
            balance: 200,
            transactions: [
                {
                    id: 0,
                    created: new Date(),
                    type: {
                        id: 1,
                        description: 'Credit',
                    },
                    amount: 200,
                },
            ],
        };
    }

    
}
