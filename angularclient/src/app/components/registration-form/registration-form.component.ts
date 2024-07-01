import {Component} from '@angular/core';
import {salonUser} from "../../model/entities/salonUser";
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-registration-form',
  templateUrl: './registration-form.component.html',
  styleUrl: './registration-form.component.scss'
})
export class RegistrationFormComponent {
  user: salonUser = new salonUser();
  confirmPassword: string = '';

  constructor(
    // private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
  ) {
  }

  onSubmit(form: NgForm) {
    if (form.valid && this.passwordsMatch()) {
      this.registration(this.user);
      console.log('Form Submitted!', this.user);
    } else {
      console.error('Form is invalid or passwords do not match');
      this.router.navigate(['redirect/registration']);
    }
  }
  gotoUserList() {
    this.router.navigate(['/users']);
  }
  passwordsMatch(): boolean {
    return this.user.password === this.confirmPassword;
  }

  registration(user: salonUser) {
    this.authService.save(user).subscribe(result => this.regSuccess());
  }
  emailFormatValid() {
    return this.user.email?.includes("@") && this.user.email?.includes(".");
  }
  private regSuccess() {
    this.router.navigate(['/login']);
  }

  protected readonly salonUser = salonUser;
}
