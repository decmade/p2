import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

// rxjs
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs/Observable';

// entities
import { User } from '../entities/User';

// services
import { ApiService } from './api.service';


@Injectable()
export class UserService {

    private http: HttpClient;
    private apiService: ApiService;

    private selectedUserSubject: Subject<User>;
    private savedUserSubject: Subject<User>;
    private api: string;

    constructor(httpClient: HttpClient, apiService: ApiService) {
        this.http = httpClient;
        this.apiService = apiService;

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
        const data = JSON.stringify(user);
        const url = this.apiService.getApiUrl(this.api);

        this.http.post<User>(url, data, {
            withCredentials: true,
            headers: { 'Content-Type': 'application/json' }
        }).subscribe( (savedUser) => this.savedUserSubject.next(savedUser) );
    }
}
