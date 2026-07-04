import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreatePostRequest } from '../models/create-post-request.interface';
import { Post } from '../models/post.interface';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private readonly pathService = '/api';

  constructor(private readonly httpClient: HttpClient) {}

  public listForTopic(topicId: number): Observable<Post[]> {
    return this.httpClient.get<Post[]>(`${this.pathService}/topics/${topicId}/posts`);
  }

  public show(postId: number): Observable<Post> {
    return this.httpClient.get<Post>(`${this.pathService}/posts/${postId}`);
  }

  public create(topicId: number, payload: CreatePostRequest): Observable<Post> {
    return this.httpClient.post<Post>(`${this.pathService}/topics/${topicId}/posts`, payload);
  }
}
