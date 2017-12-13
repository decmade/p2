import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'telephone'
})
export class TelephonePipe implements PipeTransform {

  transform(input: string): string {
    const value = this.getDigitsOnly(input);

    if ( value.length > 0 ) {
      return `(${ value.substr(0, 3) }) ${ value.substr(3, 3) }-${ value.substr(6) }`;
    } else {
      return value;
    }
  }

  private getDigitsOnly(input: string): string {

    if ( input === undefined ) {
      return '';
    } else {
      input = input.replace(/\D/g, '');
      return input.substr(0, 10);
    }


  }
}
