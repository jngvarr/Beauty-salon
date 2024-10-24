import {Injectable} from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import {catchError, Observable, switchMap, throwError} from 'rxjs';
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";


@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private isRefreshing = false;

  constructor(private http: HttpClient, private router: Router, private authService: AuthService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const accessToken = localStorage.getItem('accessToken');
    let clonedRequest = req;
    if (accessToken) {
      clonedRequest = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${accessToken}`)
      });
      // console.log(clonedRequest.headers.get('Authorization'));
    }

    return next.handle(clonedRequest).pipe(
      catchError(err => {
        if (err.status === 403) {
          console.error('Доступ запрещен: недостаточно прав.');
          this.router.navigate(['/forbidden']);
          return throwError('Доступ запрещен: недостаточно прав.');
        } else if (err.status === 401 && !this.isRefreshing) {
          this.isRefreshing = true;
          // Если получили 401, пробуем обновить access-токен
          return this.authService.refreshTokens().pipe(
            switchMap(newAccessToken => {
              if (newAccessToken) {
                // Обновляем запрос с новым access-токеном
                const newClonedRequest = req.clone({
                  headers: req.headers.set('Authorization', `Bearer ${newAccessToken}`)
                });
                this.isRefreshing = false;
                return next.handle(newClonedRequest);
              } else {
                // Если обновить не удалось, перенаправляем на страницу входа
                this.authService.logout();
                this.router.navigate(['/login']);
                return throwError('Failed to refresh token');
              }
            })
          );
        }
        return throwError(err); // Если ошибка не 401, продолжаем обработку
      })
    );
  }

  // intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
  //   const token = localStorage.getItem("token")
  //   let authRequest = req;
  //   if (token) {
  //     authRequest = this.addToken(req, token);
  //   }
  //   return next.handle(authRequest)
  //
  // }
  //
  // private addToken(req: HttpRequest<any>, token: string): HttpRequest<any> {
  //   const clonedRequest = req.clone({
  //     headers: req.headers
  //       .set('Authorization', `Bearer ${token}`)
  //       .set('Content-Type', 'application/json')
  //   });
  //   console.log('Cloned request headers:', clonedRequest.headers.keys());
  //   return clonedRequest;
  // }
}
