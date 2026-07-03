import { Component, DestroyRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MaterialModule } from '../../shared/material';
import { TopicService } from '../../core/services/topic';
import { Topic } from '../../core/models/topic.interface';
import { HeaderBarComponent } from '../../components/header-bar/header-bar';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-topic-list',
  standalone: true,
  imports: [CommonModule, RouterModule, MaterialModule, HeaderBarComponent],
  templateUrl: './topic-list.html',
})
export class TopicListComponent {
  private readonly topicService = inject(TopicService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly snackBar = inject(MatSnackBar);

  public topics: Topic[] = [];
  public loading = true;
  public error = false;
  public subscribingTopicIds = new Set<number>();

  constructor() {
    this.topicService.list().pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: topics => {
        this.topics = topics;
        this.loading = false;
      },
      error: () => {
        this.error = true;
        this.loading = false;
      }
    });
  }

  public subscribeTopic(topicId: number): void {
    const topic = this.topics.find(t => t.id === topicId);
    if (this.subscribingTopicIds.has(topicId) || topic?.subscribed) {
      return;
    }

    this.subscribingTopicIds.add(topicId);
    this.topicService.subscribe(topicId).pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: () => {
        this.subscribingTopicIds.delete(topicId);
        if (topic) {
          topic.subscribed = true;
        }
        this.snackBar.open('Abonnement effectué.', 'Fermer', { duration: 3000 });
      },
      error: () => {
        this.subscribingTopicIds.delete(topicId);
        this.snackBar.open('Impossible de s’abonner à ce thème.', 'Fermer', { duration: 3000 });
      }
    });
  }
}
