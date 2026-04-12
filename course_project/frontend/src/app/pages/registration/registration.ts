import {Component, inject, input} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../../auth/auth.service';
import {catchError, throwError} from 'rxjs';
import {RegistrationDto} from '../../models/registration-dto';

@Component({
  selector: 'app-registration',
  imports: [
    FormsModule,
    RouterLink,
    ReactiveFormsModule
  ],
  templateUrl: './registration.html',
  styleUrl: './registration.css',
})
export class RegistrationComponent{
  authService = inject(AuthService)
  router = inject(Router)

  errorMessage = ''

  fb = inject(FormBuilder)
  form: FormGroup = this.fb.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]],
    firstName: [''],
    lastName: [''],
    position: ['']
  })

  get username() {return this.form.get('username')}
  get password() {return this.form.get('password')}

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched()
      return
    }
    this.authService.register(this.form.value as RegistrationDto).subscribe({
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
