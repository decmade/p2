import { Pipe, PipeTransform } from '@angular/core';

import { User } from '../entities/User';

@Pipe({
  name: 'userCityState'
})

export class UserCityStatePipe implements PipeTransform {

  transform(user: User): string {

      switch (true) {
          case ( user.city === undefined ) :
          case ( user.state === undefined ) :
              return '';
          default:
            return [user.city, user.state].join(', ');
      }
  }

}
