import {Component, inject} from '@angular/core';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../../auth/auth.service';
import {LoginDto} from '../../models/login-dto';

@Component({
  selector: 'app-login',
  imports: [
    FormsModule,
    RouterLink,
    ReactiveFormsModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class LoginComponent {
  router = inject(Router)
  authService = inject(AuthService)

  errorMessage = ''

  fb = inject(FormBuilder)
  form = this.fb.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]]
  })

  get username() {return this.form.get('username')}
  get password() {return this.form.get('password')}

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched()
      return
    }
    this.authService.login(this.form.value as LoginDto).subscribe({
      next: () => {
        this.router.navigateByUrl('')
      },
      error: (err) => {
        this.errorMessage = err.error
        setTimeout(() => {this.errorMessage = ''}, 5000)
      }
    })

  }
}
