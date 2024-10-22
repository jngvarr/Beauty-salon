import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, map} from 'rxjs/operators';
import {of} from 'rxjs';
import {salonUser} from "../../model/entities/salonUser";
import {ApiService} from "../../services/api-service";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'login',
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.scss'
})
export class LoginFormComponent {
  user: salonUser = new salonUser();
  loading: boolean = false;
  apiUrl: string = this.apiService.apiUrl + '/users/login';

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private apiService: ApiService,
  ) {
  }

  ngOnInit() {
    if (sessionStorage.getItem('accessToken')) {
      sessionStorage.setItem('accessToken', '');
    }
  }

  login() {
    this.loading = true;
    this.http.post(this.apiUrl, {
      username: this.user.username,
      password: this.user.password
    }).pipe(
      map((res: any) => {
        this.loading = false;
        if (res && res.accessToken) {
          return res.accessToken;
        } else {
          console.error("No access token found in response");
          return null;
        }
      }),
      catchError(error => {
        this.loading = false;
        console.error('Login failed', error);
        return of(null);
      })
    ).subscribe(accessToken => {
      if (accessToken) {
        if (this.user.username) {
          this.authService.login(this.user.username, accessToken);
        } else {
          console.error("Username is undefined");
        }
      } else {
        alert("Authentication failed. Please check your username and password.");
      }
    });
  }

  protected readonly salonUser = salonUser;
}

