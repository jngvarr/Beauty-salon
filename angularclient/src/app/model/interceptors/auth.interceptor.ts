import {Injectable} from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {Router} from "@angular/router";


@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private isRefreshing = false;

  constructor(private http: HttpClient, private router: Router) {
  }
  // intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
  //   const token = sessionStorage.getItem("token");
  //   let authRequest = req;
  //
  //   if (token) {
  //     authRequest = req.clone({
  //       headers: req.headers.set('Authorization', `Bearer ${token}`)
  //     });
  //   }
  //
  //   return next.handle(authRequest).pipe(
  //     catchError((error: HttpErrorResponse) => {
  //       if (error.status === 401) {
  //         // Перенаправляем пользователя на страницу входа
  //         this.router.navigate(['/login'], {
  //           queryParams: {
  //             returnUrl: req.url
  //           }
  //         });
  //         alert('You need to login if yuo want to continue');
  //       }
  //       return throwError(error);
  //     })
  //   );
  // }
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = sessionStorage.getItem("token")
    let authRequest = req;
    if (token) {
      authRequest = this.addToken(req, token);
    }
    return next.handle(authRequest)

  }

  private addToken(req: HttpRequest<any>, token: string): HttpRequest<any> {
    const clonedRequest = req.clone({
      headers: req.headers
        .set('Authorization', `Bearer ${token}`)
        .set('Content-Type', 'application/json')
    });
    console.log('Cloned request headers:', clonedRequest.headers.keys());
    return clonedRequest;
  }
}
