import {HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {catchError, switchMap, throwError} from 'rxjs';
import {AuthService} from './auth.service';
import {inject} from '@angular/core';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService)
  const token = authService.getToken()

  if(!token) return next(req)

  return next(addTokenToHeader(req, token))
    .pipe(
      catchError(err => {
        if (err.status === 401 && !req.url.includes("/auth/refresh")) {
          return authService.refreshToken()
            .pipe(
              switchMap(res => {
                return next(addTokenToHeader(req, res.accessToken))
              })
            )
        }
        return throwError(err)
      })
    )

}

const addTokenToHeader = (req: HttpRequest<any>, token: string) => {
  return req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  })
}

