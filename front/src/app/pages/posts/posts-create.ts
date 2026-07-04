import { Component, DestroyRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, RouterModule } from '@angular/router';
import { MaterialModule } from '../../shared/material';
import { TopicService } from '../../core/services/topic';
import { PostService } from '../../core/services/post';
import { CreatePostRequest } from '../../core/models/create-post-request.interface';
import { Post } from '../../core/models/post.interface';
import { Topic } from '../../core/models/topic.interface';
import { HeaderBarComponent } from '../../components/header-bar/header-bar';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-posts-create',
  standalone: true,
  imports: [CommonModule, RouterModule, MaterialModule, HeaderBarComponent],
  templateUrl: './posts-create.html',
})
export class PostsCreateComponent {
  private readonly postService = inject(PostService);
  private readonly topicService = inject(TopicService);
  private readonly fb = inject(FormBuilder);
  private readonly snackBar = inject(MatSnackBar);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  public topics: Topic[] = [];
  public loading = true;
  public error = false;
  public formError = false;
  public submitted = false;
  public submitting = false;

  public form = this.fb.group({
    topicId: [null, [Validators.required]],
    title: ['', [Validators.required, Validators.minLength(3)]],
    content: ['', [Validators.required, Validators.minLength(10)]]
  });

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
        this.loading = false;
      },
      error: () => {
        this.error = true;
        this.loading = false;
      }
    });
  }

  public submit(): void {
    this.submitted = true;
    this.formError = false;

    if (this.form.invalid) {
      this.formError = true;
      return;
    }

    const topicIdValue = this.form.controls.topicId.value;
    if (typeof topicIdValue !== 'number') {
      this.formError = true;
      return;
    }

    const payload: CreatePostRequest = {
      title: this.form.controls.title.value ?? '',
      content: this.form.controls.content.value ?? ''
    };

    this.submitting = true;
    this.postService.create(topicIdValue, payload).pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: (post: Post) => {
        this.submitting = false;
        this.formError = false;
        this.snackBar.open('Publication créée.', 'Fermer', { duration: 3000 });
        this.router.navigate(['/posts']);
      },
      error: () => {
        this.submitting = false;
        this.snackBar.open('Impossible de créer la publication.', 'Fermer', { duration: 3000 });
      }
    });
  }
}
