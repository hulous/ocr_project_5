import { Component, DestroyRef, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { HeaderBarComponent } from '../../components/header-bar/header-bar';
import { PostService } from '../../core/services/post';
import { CommentService } from '../../core/services/comment';
import { Post } from '../../core/models/post.interface';
import { CreateCommentRequest } from '../../core/models/create-comment-request.interface';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-posts-show',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    HeaderBarComponent,
  ],
  templateUrl: './posts-show.html',
})

export class PostsShowComponent {
  private readonly postService = inject(PostService);
  private readonly commentService = inject(CommentService);
  private readonly fb = inject(FormBuilder);
  private readonly snackBar = inject(MatSnackBar);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  public readonly post = signal<Post | null>(null);
  public readonly loading = signal(true);
  public readonly error = signal(false);
  public readonly submittingComment = signal(false);
  public readonly commentError = signal(false);

  public readonly commentForm = this.fb.group({
    content: ['', [Validators.required, Validators.minLength(3)]],
  });

  constructor() {
    this.loadPost();
  }

  private loadPost(): void {
    const postId = Number(this.route.snapshot.paramMap.get('id'));

    if (!postId || Number.isNaN(postId)) {
      this.error.set(true);
      this.loading.set(false);
      return;
    }

    this.postService.show(postId).pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: post => {
        this.post.set(post);
        this.loading.set(false);
      },
      error: () => {
        this.error.set(true);
        this.loading.set(false);
      }
    });
  }

  public backToPosts(): void {
    this.router.navigate(['/posts']);
  }

  public submitComment(): void {
    this.commentError.set(false);

    if (!this.post() || this.commentForm.invalid) {
      this.commentError.set(true);
      return;
    }

    const payload: CreateCommentRequest = {
      content: this.commentForm.controls.content.value ?? '',
    };

    this.submittingComment.set(true);

    this.commentService.create(this.post()!.id, payload).pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe({
      next: comment => {
        this.post.update(post =>
          post ? { ...post, comments: post.comments ? [comment, ...post.comments] : [comment] } : post
        );
        this.commentForm.reset();
        this.submittingComment.set(false);
        this.snackBar.open('Commentaire ajouté.', 'Fermer', { duration: 3000 });
      },
      error: () => {
        this.submittingComment.set(false);
        this.snackBar.open('Impossible d\'ajouter le commentaire.', 'Fermer', { duration: 3000 });
      }
    });
  }
}
