import { Component, DestroyRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MaterialModule } from '../../shared/material';
import { TopicService } from '../../core/services/topic';
import { PostService } from '../../core/services/post';
import { Post } from '../../core/models/post.interface';
import { Topic } from '../../core/models/topic.interface';
import { HeaderBarComponent } from '../../components/header-bar/header-bar';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-posts',
  standalone: true,
  imports: [CommonModule, RouterModule, MaterialModule, HeaderBarComponent],
  templateUrl: './posts.html',
})

export class PostsComponent {
  private readonly postService = inject(PostService);
  private readonly topicService = inject(TopicService);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);
  private readonly destroyRef = inject(DestroyRef);

  public topics: Topic[] = [];
  public topicPosts: Array<{ topic: Topic; posts: Post[] }> = [];
  public loading = true;
  public error = false;

  constructor() {
    this.loadSubscribedTopics();
  }

  private loadSubscribedTopics(): void {
    this.loading = true;
    this.error = false;

    this.topicService.list().pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: topics => {
        this.topics = topics.filter(topic => topic.subscribed);
        this.loadPostsForTopics();
      },
      error: () => {
        this.error = true;
        this.loading = false;
      }
    });
  }

  private loadPostsForTopics(): void {
    if (this.topics.length === 0) {
      this.topicPosts = [];
      this.loading = false;
      return;
    }

    const requests = this.topics.map(topic =>
      this.postService.listForTopic(topic.id).pipe(
        catchError(() => of([]))
      )
    );

    forkJoin(requests).pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: postsArrays => {
        this.topicPosts = this.topics.map((topic, index) => ({
          topic,
          posts: postsArrays[index]
        }));
        this.loading = false;
      },
      error: () => {
        this.error = true;
        this.loading = false;
      }
    });
  }

  public cancel(): void {
    this.router.navigate(['/topics']);
  }
}
