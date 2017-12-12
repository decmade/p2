export class LoginForm {
    public identity: string;
    public password: string;
    public elementId: string;

    public getCredentials(): any {
        return {
            identity: this.identity,
            password: this.password,
        };
    }
}
