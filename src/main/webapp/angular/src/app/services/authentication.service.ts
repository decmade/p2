import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

// rxjs
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

// entites
import { User } from '../entities/User';
import { LoginForm } from '../entities/LoginForm';

@Injectable()
export class AuthenticationService {

    private http: HttpClient;
    private currentUserSubject: BehaviorSubject<User>;
    private serviceUrl: string;

    constructor(httpClient: HttpClient) {
        this.http = httpClient;

        this.serviceUrl = 'auth';
        this.currentUserSubject = new BehaviorSubject(null);
        this.getAuthenticatedUser();
    }

    public getCurrentUser() {
        return this.currentUserSubject.asObservable();
    }

    public login(loginForm: LoginForm): void {
        const url = this.getUrl();

        this.http.post<User>(url, loginForm.getCredentials(), { withCredentials: true }).subscribe( (user) => {
            this.currentUserSubject.next(user);
        }, (error) => {
            this.currentUserSubject.next(null);
        });
    }

    public logout(): void {
        const url = this.getUrl();

        this.http.delete<any>(url, { withCredentials: true } ).subscribe( () => {
            this.currentUserSubject.next(null);
        });
    }

    private getAuthenticatedUser(): void {
        const url = this.getUrl();

        this.http.get<User>(url, {withCredentials: true} ).subscribe( (user) => {
            this.currentUserSubject.next(user);
        });
    }

    private getUrl(): string {
        return [
            'http://localhost:8080/bet',
            this.serviceUrl
        ].join('/');
    }

}
