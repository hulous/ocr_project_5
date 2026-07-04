import { Component, DestroyRef, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { HeaderBarComponent } from '../../components/header-bar/header-bar';
import { PostService } from '../../core/services/post';
import { Post } from '../../core/models/post.interface';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-posts-show',
  standalone: true,
  imports: [CommonModule, RouterModule, MatButtonModule, MatCardModule, MatProgressSpinnerModule, HeaderBarComponent],
  templateUrl: './posts-show.html',
})

export class PostsShowComponent {
  private readonly postService = inject(PostService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  public post: Post | null = null;
  public loading = true;
  public error = false;

  constructor() {
    this.loadPost();
  }

  private loadPost(): void {
    const postId = Number(this.route.snapshot.paramMap.get('id'));

    if (!postId || Number.isNaN(postId)) {
      this.error = true;
      this.loading = false;
      return;
    }

    this.postService.show(postId).pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: post => {
        this.post = post;
        this.loading = false;
      },
      error: () => {
        this.error = true;
        this.loading = false;
      }
    });
  }

  public backToPosts(): void {
    this.router.navigate(['/posts']);
  }
}
