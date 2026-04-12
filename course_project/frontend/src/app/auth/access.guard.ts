import {AuthService} from './auth.service';
import {inject} from '@angular/core';
import {Router} from '@angular/router';
import {catchError, map} from 'rxjs';


export const canActivateAuth = () => {
  const authService = inject(AuthService)
  const router = inject(Router)

  if (!authService.isAuth())
    return router.navigateByUrl('/login')

  return authService.getCurrentUser().pipe(
    map(() => true),
    catchError(() => router.navigateByUrl('/login'))
  );
}
