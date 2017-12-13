import { UserStatus } from './UserStatus';
import { UserRole } from './UserRole';

export class User {
    id: Number;
    identity: string;
    credential: string;
    secret: string;
    lastName: string;
    firstName: string;
    email: string;
    address1: string;
    address2: string;
    city: string;
    state: string;
    zip: string;
    dob: Date;
    phone: string;
    account: Account;
    status: UserStatus;
    role: UserRole;
}
