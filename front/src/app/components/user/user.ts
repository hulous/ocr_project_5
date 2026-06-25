import { ChangeDetectionStrategy, Component, DestroyRef, Injector, OnInit, inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { User } from '../../core/models/user.interface';
import { SessionService } from '../../core/services/session';
import { UserService } from '../../core/services/user';
import { AuthService } from '../../core/services/auth';
import { MaterialModule } from "../../shared/material";
import { CommonModule } from "@angular/common";
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { EMPTY, Observable, firstValueFrom } from 'rxjs';
import { catchError, filter, switchMap, tap } from 'rxjs/operators';
import { Session } from '../../core/models/session.interface';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [CommonModule, MaterialModule],
  templateUrl: './user.html',
  styleUrls: ['./user.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserComponent implements OnInit {
  private router = inject(Router);
  private sessionService = inject(SessionService);
  private matSnackBar = inject(MatSnackBar);
  private userService = inject(UserService);
  private authService = inject(AuthService);
  private destroyRef = inject(DestroyRef);
  private injector = inject(Injector);
  public user$!: Observable<User>;

  ngOnInit(): void {
    this.user$ = toObservable<Session | undefined>(this.sessionService.session, { injector: this.injector }).pipe(
      tap((session) => {
        if (!session) {
          this.router.navigate(['/login']);
        }
      }),
      filter((session): session is Session => !!session),
      switchMap(() => this.authService.me()),
      catchError((error) => {
        this.handleUnauthorizedError(error, 'Unable to load your profile');
        return EMPTY;
      })
    );
  }

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

