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

  login(username: string | undefined, accessToken: string, refreshToken: string) {
    if (typeof window !== 'undefined' && window.localStorage) {
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
      // console.log(accessToken);
      // console.log(refreshToken);
    } else {
      alert("Authentication failed. Please check your username and password.");
    }
    this.logged = true;
    this.username = username;
    this.router.navigate(['']);
  }

  logout() {
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
      this.logged = false;
      // console.log(accessToken);
      this.http.post("/api" + "/users/logout", accessToken).subscribe({
        next: () => {
          localStorage.removeItem('accessToken');
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

  refreshTokens() {
    const refreshToken = localStorage.getItem('refreshToken');

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
          localStorage.setItem('accessToken', res.accessToken);
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
