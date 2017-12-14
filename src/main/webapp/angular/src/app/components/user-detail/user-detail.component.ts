import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { FormsModule } from '@angular/forms';

// pipes
import { CityStatePipe } from '../../pipes/city-state.pipe';

// rxjs
import { Subscription } from 'rxjs/Subscription';

// services
import { UserService } from '../../services/user.service';
import { ZipCodeService } from '../../services/zip-code.service';

// entities
import { User } from '../../entities/User';
import { ZipInformation } from '../../entities/ZipInformation';

// jquery
import * as any from 'jquery';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit, OnDestroy {
    private cityStatePipe: CityStatePipe;

    private userService: UserService;
    private zipCodeService: ZipCodeService;

    private selectedUsersSubscription: Subscription;
    private savedUsersSubscription: Subscription;
    private user: User;
    private userClone: User;
    private password: string;
    private passwordConfirmation: string;

    @ViewChild('userModal') modalElement: ElementRef;
    @ViewChild('phoneInput') telephoneElement: ElementRef;
    @ViewChild('userForm') userForm: ElementRef;

  constructor(userService: UserService, zipCodeService: ZipCodeService) {
      this.userService = userService;
      this.zipCodeService = zipCodeService;

      this.user = null;
      this.userClone = new User();
      this.cityStatePipe = new CityStatePipe();

      this.password = '';
      this.passwordConfirmation = '';
  }

  public onZipKeyUp(): void {
      const zip = this.user.zip;

      if ( this.validateZip(zip) === true ) {
          this.zipCodeService.lookup(zip).subscribe((zipInfo) => {
              this.user.city = zipInfo.city;
              this.user.state = zipInfo.state;
          });
      } else {
        this.user.city = '';
        this.user.state = '';
      }
  }

  public onPhoneUpdate(): void {
      this.user.phone = this.telephoneElement.nativeElement.value.replace(/\D/g, '');
  }

  public onSubmit(): void {
    const modal = this.modalElement.nativeElement;

      if ( this.validateForm() ) {
        this.user.identity = this.user.email;

        if ( this.password.length > 0 ) {
            this.user.credential = this.password;
        }

        this.userService.save( this.user );
      } else {
          this.password = '';
          this.passwordConfirmation = '';
      }
  }

  public onCancel(): void {
    const modal = this.modalElement.nativeElement;
    const form = this.userForm.nativeElement;

    this.unparkUser();

    $(form).removeClass('was-validated');
    $(modal).modal('hide');
  }

  public getCityState(): string {

      switch (true) {
          case ( this.user.city === undefined ) :
          case ( this.user.state === undefined ) :
          case ( this.user.city.length === 0 ) :
          case ( this.user.state.length === 0 ) :
            return '';
          default :
            return this.cityStatePipe.transform(this.user);

      }
  }

  private clearForm(): void {
    const form = this.userForm.nativeElement;

    form.reset();
  }

  private validateForm(): boolean {
      const form = this.userForm.nativeElement;

      switch (true) {
          case ( form.checkValidity() === false ):
          case ( this.password !== this.passwordConfirmation ) :
              $(form).addClass('was-validated');
              return false;
          default :
              return true;
      }
  }

  private validateZip(input: string): boolean {
      const zipPattern = new RegExp('[0-9]{5}');

      switch (true) {
          case ( input === undefined ) :
          case ( input.length !== 5 ) :
          case ( zipPattern.test(input) === false ) :
              return false;
          default :
              return true;
      }
  }

  private setUser(user: User) {

      if ( user ) {
        this.user = user;
        this.parkUser();
        $(this.modalElement.nativeElement).modal('show');
      }
  }

  private parkUser(): void {
      Object.assign(this.userClone, this.user);
  }

  private unparkUser(): void {
      Object.assign(this.user, this.userClone);
  }

  ngOnInit(): void {
      this.selectedUsersSubscription = this.userService.getSelectedUsers()
          .subscribe( (user) => this.setUser(user) );

      this.savedUsersSubscription = this.userService.getSavedUsers()
          .subscribe( (user) => {
            const modal = this.modalElement.nativeElement;

            $(modal).modal('hide');
            this.userService.selectNewUser();
          });
  }

  ngOnDestroy(): void {
      this.selectedUsersSubscription.unsubscribe();

      this.savedUsersSubscription.unsubscribe();
  }
}
