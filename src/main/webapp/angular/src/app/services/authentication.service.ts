import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

// rxjs
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

// entites
import { User } from '../entities/User';
import { LoginForm } from '../entities/LoginForm';

// services
import { ApiService } from './api.service';

@Injectable()
export class AuthenticationService {

    private http: HttpClient;
    private apiService: ApiService;

    private currentUserSubject: BehaviorSubject<User>;
    private api: string;
    

    constructor(httpClient: HttpClient, apiService: ApiService) {
        this.http = httpClient;
        this.apiService = apiService;

        this.api = 'auth';
        this.currentUserSubject = new BehaviorSubject(null);
        this.getAuthenticatedUser();
    }

    public getCurrentUser() {
        return this.currentUserSubject.asObservable();
    }

    public login(loginForm: LoginForm): void {
        const url = this.apiService.getApiUrl( this.api );

        this.http.post<User>(url, loginForm.getCredentials(), { withCredentials: true }).subscribe( (user) => {
            this.currentUserSubject.next(user);
        }, (error) => {
            this.currentUserSubject.next(null);
        });
    }

    public logout(): void {
        const url = this.apiService.getApiUrl( this.api );

        this.http.delete<any>(url, { withCredentials: true } ).subscribe( () => {
            this.currentUserSubject.next(null);
        });
    }

    private getAuthenticatedUser(): void {
        const url = this.apiService.getApiUrl( this.api );

        this.http.get<User>(url, {withCredentials: true} ).subscribe( (user) => {
            this.currentUserSubject.next(user);
        });
    }

}
