import {inject} from '@angular/core';
import {AuthService} from './auth.service';
import {Router} from '@angular/router';

export const canActivateAuth = () => {
  const authService = inject(AuthService)

  if (authService.isAuth())
    return true

  return inject(Router).navigateByUrl("login")
}
