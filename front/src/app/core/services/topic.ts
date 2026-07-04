import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Topic } from '../models/topic.interface';

@Injectable({
  providedIn: 'root'
})
export class TopicService {
  private readonly pathService = '/api/topics';

  constructor(private readonly httpClient: HttpClient) {}

  public list(): Observable<Topic[]> {
    return this.httpClient.get<Topic[]>(this.pathService);
  }

  public subscribe(topicId: number): Observable<unknown> {
    return this.httpClient.post<unknown>(`${this.pathService}/${topicId}/subscription`, null);
  }

  public unsubscribe(topicId: number): Observable<unknown> {
    return this.httpClient.delete<unknown>(`${this.pathService}/${topicId}/subscription`);
  }
}
