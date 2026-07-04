import { ChangeDetectionStrategy, Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { User } from '../../core/models/user.interface';
import { SessionService } from '../../core/services/session';
import { AuthService } from '../../core/services/auth';
import { MaterialModule } from "../../shared/material";
import { CommonModule } from "@angular/common";
import { EMPTY } from 'rxjs';
import { catchError, finalize, tap } from 'rxjs/operators';
import { HeaderBarComponent } from '../../components/header-bar/header-bar';
import { TopicService } from '../../core/services/topic';
import { Topic } from '../../core/models/topic.interface';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [CommonModule, MaterialModule, HeaderBarComponent, ReactiveFormsModule],
  templateUrl: './user.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})

export class UserComponent implements OnInit {
  private readonly router = inject(Router);
  private readonly sessionService = inject(SessionService);
  private readonly matSnackBar = inject(MatSnackBar);
  private readonly authService = inject(AuthService);
  private readonly topicService = inject(TopicService);
  private readonly formBuilder = inject(FormBuilder);

  public user = signal<User | null>(null);
  public subscribedTopics = signal<Topic[]>([]);
  public unsubscribingTopicIds = signal<number[]>([]);

  public userForm = this.formBuilder.group({
    username: [''],
    email: [''],
    password: ['']
  });

  ngOnInit(): void {
    const session = this.sessionService.session();
    if (!session) {
      this.router.navigate(['/login']);
      return;
    }

    this.authService.me().pipe(
      tap(user => {
        this.user.set(user);
        this.userForm.patchValue({
          username: user.username,
          email: user.email,
          password: ''
        });
        this.loadSubscribedTopics();
      }),
      catchError((error) => {
        this.handleUnauthorizedError(error, 'Unable to load your profile');
        return EMPTY;
      })
    ).subscribe();
  }

  public saveProfile(): void {
    this.matSnackBar.open('Sauvegarde du profil non disponible pour le moment.', 'Fermer', { duration: 3000 });
  }

  public unsubscribeTopic(topicId: number): void {
    if (this.unsubscribingTopicIds().includes(topicId)) {
      return;
    }

    this.unsubscribingTopicIds.set([...this.unsubscribingTopicIds(), topicId]);
    this.topicService.unsubscribe(topicId).pipe(
      tap(() => {
        this.subscribedTopics.set(this.subscribedTopics().filter(topic => topic.id !== topicId));
        this.matSnackBar.open('Abonnement supprimé.', 'Fermer', { duration: 3000 });
      }),
      catchError(() => {
        this.matSnackBar.open('Impossible de se désabonner pour le moment.', 'Fermer', { duration: 3000 });
        return EMPTY;
      }),
      finalize(() => this.unsubscribingTopicIds.set(this.unsubscribingTopicIds().filter(id => id !== topicId)))
    ).subscribe();
  }

  private loadSubscribedTopics(): void {
    this.topicService.list().pipe(
      tap(topics => {
        this.subscribedTopics.set(topics.filter(topic => topic.subscribed));
      }),
      catchError(() => {
        this.matSnackBar.open('Impossible de charger les abonnements.', 'Fermer', { duration: 3000 });
        return EMPTY;
      })
    ).subscribe();
  }

  public back(): void {
    globalThis.history.back();
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

