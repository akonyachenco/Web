import {Component, inject} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../../auth/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class LoginComponent {
  authService = inject(AuthService)
  router = inject(Router)

  errorMessage: string = ''

  form = new FormGroup({
    username: new FormControl(null, Validators.required),
    password: new FormControl(null, Validators.required)
  })


  onSubmit() {
    if(this.form.valid) {
      //@ts-ignore
      this.authService.login(this.form.value).subscribe({
        next: () => {
          this.errorMessage = ''
          this.router.navigateByUrl('tasks')
        },
        error: (err) => {
          if (err.status === 404 || err.status === 401)
            this.errorMessage = "Вы ввели неправильные учётные данные"
        }
      })
    }
    else {
      this.errorMessage = "Имя пользователя и пароль обязательны"
    }
  }
}
