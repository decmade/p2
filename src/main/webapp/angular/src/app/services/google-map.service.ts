import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';

@Injectable()
export class GoogleMapService {

  constructor() { }

  public getVenueMapUrl(venue: Venue): string {
      return this.getMapUrl([
          venue.name.replace('\w', '+'),
          venue.address,
          venue.city,
          venue.state,
          venue.zip
       ]);
  }

  public getMapUrl(query: string[] ): string {
      const params = new HttpParams()
          .set('?api', '1')
          .set('query', this.escapeQuery(query) );

      return [
          'https:/',
          'www.google.com',
          'maps',
          'search',
          params.toString(),
      ].join('/');
  }

  private escapeQuery(query: string[]): string {
      return query
          .join('+')
          .replace(' ', '+')
          .toLowerCase();
  }


}
