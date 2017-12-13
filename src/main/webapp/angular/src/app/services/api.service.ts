import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable()
export class ApiService {

  constructor() { }

  public getApiUrl(apiNode: string, params = [] ): string {
      const paramSegement = params.join('/');

      return [
          environment.apiPrefix,
          apiNode,
          paramSegement
      ].join('/');
  }

}
