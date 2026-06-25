import { Component, DestroyRef, inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Session } from 'src/app/core/models/session.interface';
import { SessionService } from 'src/app/core/services/session';
import { LoginRequest } from '../../core/models/login-request.interface';
import { AuthService } from '../../core/services/auth';
import { MaterialModule } from "../../shared/material";
import { CommonModule } from '@angular/common';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, MaterialModule],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class LoginComponent {
  private authService = inject(AuthService);
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private sessionService = inject(SessionService);
  private destroyRef = inject(DestroyRef);

  public hide = true;
  public onError = false;

  public form = this.fb.group({
    login: [
      '',
      [
        Validators.required
      ]
    ],
    password: [
      '',
      [
        Validators.required,
        Validators.minLength(3)
      ]
    ]
  });

  public submit(): void {
    const loginRequest = this.form.value as LoginRequest;
    this.authService.login(loginRequest).pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: (response: Session) => {
        this.sessionService.logIn(response);
        this.router.navigate(['/home']);
      },
      error: error => this.onError = true,
    });
  }
}
