import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environment';
import {catchError, map, Observable, of, tap, throwError} from 'rxjs';
import {UserResponse} from '../dto/user-response';
import {UserRequest} from '../dto/user-request';
import {TokenResponse} from '../dto/token-response';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  http = inject(HttpClient)

  private apiUrl = `${environment.baseURL}/auth`

  public getCurrentUser(): Observable<UserResponse> {
    return this.http.get<UserResponse>(this.apiUrl + "/me")
  }

  public register(user: UserRequest): Observable<void> {
    return this.http.post<void>(this.apiUrl + "/register", user)
  }

  public login(user: UserRequest): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(this.apiUrl + "/login", user)
      .pipe(
        tap(val => {
          sessionStorage.setItem("accessToken", val.accessToken)
          sessionStorage.setItem("refreshToken", val.refreshToken)
        })
      )
  }

  public refreshToken(): Observable<TokenResponse> {
    const token = sessionStorage.getItem("refreshToken")
    return this.http.post<TokenResponse>(this.apiUrl + "/refresh", {refreshToken: token})
      .pipe(
        tap(val => {
          sessionStorage.setItem("accessToken", val.accessToken)
        }),
        catchError(err => {
          this.logout()
          return throwError(err)
        })
      )
  }

  public logout() {
    sessionStorage.clear()
    inject(Router).navigateByUrl("login")
  }

  public getToken() {
    return sessionStorage.getItem("accessToken")
  }

  public isAuth() {
    return !!this.getToken()
  }

  public isAdmin(): Observable<boolean> {
    return this.getCurrentUser()
      .pipe(
        map(user => user.roles.includes('ROLE_ADMIN'))
      )
  }
}
