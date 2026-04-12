import {computed, inject, Injectable, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {LoginDto} from '../models/login-dto';
import {catchError, Observable, tap, throwError} from 'rxjs';
import {TokenResponse} from '../models/token-response';
import {RegistrationDto} from '../models/registration-dto';
import {Employee} from '../models/employee';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  http = inject(HttpClient)
  router = inject(Router)

  private user = signal<Employee | null>(null)

  isAdmin = computed(() => {
    return this.user()?.roles.includes('ROLE_ADMIN')
  })

  url = `${environment.apiUrl}/auth`

  constructor() {
    window.addEventListener('storage', (event) => {
      if (event.key === 'logout') {
        this.user.set(null)
        this.router.navigateByUrl('/login')
      }
      else if (event.key === 'accessToken' && event.newValue && !event.oldValue) {
        window.location.reload()
      }
    });
  }

  login(dto: LoginDto): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(`${this.url}/login`, dto).pipe(
      tap(tokens => {
        localStorage.setItem("accessToken", tokens.accessToken)
        localStorage.setItem("refreshToken", tokens.refreshToken)
              })
    )
  }


  register(dto: RegistrationDto): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(`${this.url}/register`, dto).pipe(
      tap(tokens => {
        localStorage.setItem("accessToken", tokens.accessToken)
        localStorage.setItem("refreshToken", tokens.refreshToken)
      })
    )
  }

  refresh(): Observable<TokenResponse> {
    const refreshToken = localStorage.getItem('refreshToken')
    return this.http.post<TokenResponse>(`${this.url}/refresh`, refreshToken)
      .pipe(
        tap(tokens => {
          if (!tokens) throw new Error()
          localStorage.setItem('accessToken', tokens.accessToken)
        }),
        catchError(err => {
          this.logout()
          return throwError(() => err)
        })
      )
  }

  getCurrentUser() {
    return this.http.get<Employee>(`${this.url}/me`).pipe(
      tap(user => {
        this.user.set(user)
      })
    )
  }

  logout() {
    this.user.set(null)
    localStorage.clear()

    localStorage.setItem('logout', Date.now().toString());
    setTimeout(() => localStorage.removeItem('logout'), 100);

    this.router.navigateByUrl('/login')
  }

  getToken() {
    return localStorage.getItem('accessToken')
  }

  isAuth(): boolean {
    return !!this.getToken()
  }
}
