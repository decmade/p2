import { Pipe, PipeTransform } from '@angular/core';
import { User } from '../entities/User';

@Pipe({
    name: 'displayName',
})

export class UserDisplayNamePipe implements PipeTransform {

    public transform(user: User): string {
        return [
            user.firstName,
            user.lastName,
        ].join(' ');
    }
}
