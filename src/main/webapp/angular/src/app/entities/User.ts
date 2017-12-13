import { UserStatus } from './UserStatus';
import { UserRole } from './UserRole';
export class User {
    id: Number;
    identity: String;
    credential: String;
    secret: String;
    lastName: String;
    firstName: String;
    email: String;
    address1: String;
    address2: String;
    city: String;
    state: String;
    zip: String;
    dob: Date;
    phone: String;
    account: Account;
    status: UserStatus;
    role: UserRole;
}
