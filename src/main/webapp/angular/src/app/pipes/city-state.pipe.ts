import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'cityState'
})

export class CityStatePipe implements PipeTransform {

  transform(entity: any): string {

      switch (true) {
          case ( entity.city === undefined ) :
          case ( entity.state === undefined ) :
              return '';
          default:
            return [entity.city, entity.state].join(', ');
      }
  }

}
