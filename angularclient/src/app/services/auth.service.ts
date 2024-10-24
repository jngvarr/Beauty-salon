import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ApiService} from "./api-service";
import {salonUser} from "../model/entities/salonUser";
import {catchError, of} from "rxjs";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  logged: boolean = false;
  username: string | undefined;

  constructor(private router: Router,
              private http: HttpClient,
              public apiService: ApiService) {
  }

  save(user: salonUser) {
    return this.http.post<salonUser>(this.apiService.apiUrl + "/users/registration", user);
  }

  login(username: string | undefined, accessToken: string) {
    if (typeof sessionStorage !== 'undefined') {
      sessionStorage.setItem('accessToken', accessToken);
    } else {
      alert("Authentication failed. Please check your username and password.");
    }
    this.logged = true;
    this.username = username;
    this.router.navigate(['']);
  }

  logout() {
    const accessToken = sessionStorage.getItem('accessToken');
    if (accessToken) {
      this.logged = false;
      console.log(accessToken);
      // this.http.post(this.apiService + "/users/logout", token).subscribe({
      this.http.post("/api" + "/users/logout", accessToken).subscribe({
        next: () => {
          sessionStorage.removeItem('accessToken');
          // this.router.navigate(['/login']);
        },
        error: err => console.error('Logout failed', err)
      });
    }
    // else {
    // }
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return this.logged;
  }

  getUsername(): string | undefined {
    return this.username;
  }

  refreshToken() {
    const refreshToken = localStorage.getItem('refresh_token');

    if (!refreshToken) {
      console.error('No refresh token available');
      return of(null); // Возвращаем Observable с null, если токен отсутствует
    }

    return this.http.post(this.apiService.apiUrl + '/refresh', {
      refreshToken: refreshToken
    }).pipe(
      map((res: any) => {
        if (res && res.accessToken) {
          // Обновляем access-токен в localStorage
          localStorage.setItem('access_token', res.accessToken);
          return res.accessToken;
        } else {
          console.error("No access token found in response");
          return null;
        }
      }),
      catchError(error => {
        console.error('Token refresh failed', error);
        return of(null); // Возвращаем Observable с null при ошибке
      })
    );
  }

}
