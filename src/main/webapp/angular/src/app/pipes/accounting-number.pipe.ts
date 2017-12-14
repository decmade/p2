import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'accountingNumber'
})
export class AccountingNumberPipe implements PipeTransform {

  transform(value: number): any {
      switch (true) {
          case (value === undefined) :
          case (value === 0) :
              return '--';
          default :
              return `${ value }`;
      }
  }

}
