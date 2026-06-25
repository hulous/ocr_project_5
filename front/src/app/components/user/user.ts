import { ChangeDetectionStrategy, Component, DestroyRef, Injector, OnInit, inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { User } from '../../core/models/user.interface';
import { SessionService } from '../../core/services/session';
import { UserService } from '../../core/services/user';
import { MaterialModule } from "../../shared/material";
import { CommonModule } from "@angular/common";
import { takeUntilDestroyed, toObservable } from '@angular/core/rxjs-interop';
import { EMPTY, Observable } from 'rxjs';
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
  private destroyRef = inject(DestroyRef);
  private injector = inject(Injector);
  public user$!: Observable<User>;


  ngOnInit(): void {
    this.user$ = toObservable(this.sessionService.session, { injector: this.injector }).pipe(
        tap((Session) => {
          if (!Session) {
            this.router.navigate(['/login']);
          }
        }),
        filter((Session): Session is Session => !!Session),
        switchMap((Session) => this.userService.getById(Session.id.toString())),
        catchError(() => {
          this.matSnackBar.open('Unable to load your profile', 'Close', { duration: 3000 });
          return EMPTY;
        })
        );
  }

  public back(): void {
    window.history.back();
  }

  public delete(): void {
    const Session = this.sessionService.session();
    if (!Session) {
      this.router.navigate(['/login']);
      return;
    }

    this.userService
      .delete(Session.id.toString())
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (_) => {
          this.matSnackBar.open("Your account has been deleted !", 'Close', { duration: 3000 });
          this.sessionService.logOut();
          this.router.navigate(['/']);
        },
        error: () => this.matSnackBar.open('Unable to delete your account', 'Close', { duration: 3000 })
      });
  }

}
