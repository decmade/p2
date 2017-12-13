import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { FormsModule } from '@angular/forms';

// pipes
import { UserCityStatePipe } from '../../pipes/user-city-state.pipe';

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
    private cityStatePipe: UserCityStatePipe;

    private userService: UserService;
    private zipCodeService: ZipCodeService;

    private selectedUsersSubscription: Subscription;
    private savedUsersSubscription: Subscription;
    private user: User;
    private passwordConfirmation: string;

    @ViewChild('userModal') modalElement: ElementRef;
    @ViewChild('phoneInput') telephoneElement: ElementRef;
    @ViewChild('userForm') userForm: ElementRef;

  constructor(userService: UserService, zipCodeService: ZipCodeService) {
      this.userService = userService;
      this.zipCodeService = zipCodeService;

      this.user = null;
      this.cityStatePipe = new UserCityStatePipe();
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
        this.userService.save( this.user );
      } else {
        this.passwordConfirmation = '';
      }
  }

  public onCancel(): void {
    const modal = this.modalElement.nativeElement;

    this.userService.selectNewUser();

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


  private validateForm(): boolean {
      const form = this.userForm.nativeElement;

      switch (true) {
          case ( form.checkValidity() === false ):
          case ( this.user.credential !== this.passwordConfirmation ) :
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
        $(this.modalElement.nativeElement).modal('show');
      }
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
