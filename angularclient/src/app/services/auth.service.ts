import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ApiService} from "./api-service";
import {salonUser} from "../model/entities/salonUser";

@Injectable({
  providedIn: 'root'})
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
  login(username: string | undefined, token: string) {
    if (typeof sessionStorage !== 'undefined') {
      sessionStorage.setItem('token', token);
    } else {
      alert("Authentication failed. Please check your username and password.");
    }
    this.logged = true;
    this.username = username;
    this.router.navigate(['']);  }
  logout() {
    const token = sessionStorage.getItem('token');
    if (token) {
      this.logged = false;
      console.log(token);
      // this.http.post(this.apiService + "/users/logout", token).subscribe({
      this.http.post("/api" + "/users/logout", token).subscribe({
        next: () => {
          sessionStorage.removeItem('token');
          this.router.navigate(['/login']);
        },
        error: err => console.error('Logout failed', err)
      });
    } else {
      this.router.navigate(['/login']);
    }
  }
  isLoggedIn(): boolean {
    return this.logged;
  }
  getUsername(): string | undefined {
    return this.username;
  }
}
