import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comment } from '../models/comment.interface';
import { CreateCommentRequest } from '../models/create-comment-request.interface';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private readonly pathService = '/api';

  constructor(private readonly httpClient: HttpClient) {}

  public create(postId: number, payload: CreateCommentRequest): Observable<Comment> {
    return this.httpClient.post<Comment>(`${this.pathService}/posts/${postId}/comments`, payload);
  }
}
