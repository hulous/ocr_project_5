import { ChangeDetectionStrategy, Component, OnInit, inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { User } from '../../core/models/user.interface.js';
import { SessionService } from '../../core/services/session';
import { AuthService } from '../../core/services/auth';
import { MaterialModule } from "../../shared/material";
import { CommonModule } from "@angular/common";
import { EMPTY, Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [CommonModule, MaterialModule],
  templateUrl: './user.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})

export class UserComponent implements OnInit {
  private router = inject(Router);
  private sessionService = inject(SessionService);
  private matSnackBar = inject(MatSnackBar);
  private authService = inject(AuthService);
  public user$!: Observable<User>;

  ngOnInit(): void {
    const session = this.sessionService.session();
    if (!session) {
      this.router.navigate(['/login']);
      return;
    }

    this.user$ = this.authService.me().pipe(
      catchError((error) => {
        this.handleUnauthorizedError(error, 'Unable to load your profile');
        return EMPTY;
      })
    );
  }

  // used on user view on click on back arrow
  public back(): void {
    window.history.back();
  }

  private handleUnauthorizedError(error: unknown, message: string): void {
    if (error && typeof error === 'object' && 'status' in error && (error as { status: number }).status === 401) {
      this.sessionService.logOut();
      this.router.navigate(['/login']);
      this.matSnackBar.open('Session expired. Please login again.', 'Close', { duration: 3000 });
      return;
    }

    this.matSnackBar.open(message, 'Close', { duration: 3000 });
  }
}

