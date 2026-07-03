import { Component, DestroyRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../../shared/material';
import { TopicService } from '../../core/services/topic';
import { Topic } from '../../core/models/topic.interface';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-topic-list',
  standalone: true,
  imports: [CommonModule, RouterModule, MaterialModule],
  templateUrl: './topic-list.html',
})
export class TopicListComponent {
  private readonly topicService = inject(TopicService);
  private readonly destroyRef = inject(DestroyRef);

  public topics: Topic[] = [];
  public loading = true;
  public error = false;

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
}
