import {HttpHandlerFn, HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {inject} from '@angular/core';
import {AuthService} from './auth.service';
import {BehaviorSubject, catchError, filter, switchMap, tap, throwError} from 'rxjs';

let isRefreshing$ = new BehaviorSubject<boolean>(false)

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService)
  const token = authService.getToken()!

  const PUBLIC_PATHS = [
    '/auth/login',
    '/auth/register',
    '/auth/refresh'
  ]


  if (PUBLIC_PATHS.some(path => req.url.includes(path))) {
    return next(req)
  }

  if (isRefreshing$.value)
    return refreshingAndTryAgain(req, next, authService)

  return next(addTokenToHeader(req, token))
    .pipe(
      catchError(err => {
        if(err.status === 401) {
          console.error('ОШИБКА 401')
          return refreshingAndTryAgain(req, next, authService)
        }
        return throwError(() => err)
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

const refreshingAndTryAgain = (
  req: HttpRequest<any>,
  next: HttpHandlerFn,
  authService: AuthService)=> {

  if (!isRefreshing$.value) {
    isRefreshing$.next(true)
    return authService.refresh()
      .pipe(
        switchMap(tokens => {
          return next(addTokenToHeader(req, tokens.accessToken))
            .pipe(
              tap(()=> isRefreshing$.next(false))
            )
        }),
        catchError((err) => {
          isRefreshing$.next(false)
          return throwError(() => err)
        })
      )
  }

  return isRefreshing$.pipe(
    filter(isRefreshing => !isRefreshing),
    switchMap(()=> {
      return next(addTokenToHeader(req, authService.getToken()!))
    })
  )
}

