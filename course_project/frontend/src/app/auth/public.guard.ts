import {inject} from '@angular/core';
import {AuthService} from './auth.service';
import {Router} from '@angular/router';

export const canActivatePublic = () => {
  const authService = inject(AuthService)
  const router = inject(Router)

  if (!authService.isAuth())
    return true

  return router.navigateByUrl('')
}
