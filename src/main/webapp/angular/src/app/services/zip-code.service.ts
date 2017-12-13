import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

// rxjs
import { Observable } from 'rxjs/Observable';

// entities
import { ZipInformation } from '../entities/ZipInformation';

@Injectable()
export class ZipCodeService {
    private http: HttpClient;

    constructor( httpClient: HttpClient ) {
        this.http = httpClient;

    }

    public lookup(zip: string): Observable<ZipInformation> {
        return this.http.get<ZipInformation>( this.getUrl(zip), {withCredentials: false} );
    }

    private getUrl(zipCode: string): string {
        return [
            'https:/',
            'www.zipcodeapi.com',
            'rest',
            this.getApiKey(),
            'info.json',
            zipCode,
            'radians'
        ].join('/');
    }

    private getApiKey(): string {
        return 'js-dpVjU4sSvsULAcueA7sOGGUIYNBLl8hysVtrRXBs4zHIc9e0ldhCvtWu7oy5Mv6x';
    }
}