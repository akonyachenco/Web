import {inject} from '@angular/core';
import {AuthService} from './auth.service';
import {Router} from '@angular/router';

export const canActivateAdmin = () => {
  const authService = inject(AuthService)
  const router = inject(Router)

  if (authService.isAdmin())
    return true

  return router.navigateByUrl('')
}
