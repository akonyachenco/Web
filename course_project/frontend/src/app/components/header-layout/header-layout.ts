import {Component, inject} from '@angular/core';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {AuthService} from '../../auth/auth.service';

@Component({
  selector: 'app-header-layout',
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
  ],
  templateUrl: './header-layout.html',
  styleUrl: './header-layout.css',
})
export class HeaderLayout {
  authService = inject(AuthService)
  router = inject(Router)

  logout() {
    this.authService.logout()
    this.router.navigateByUrl('/login')
  }
}
