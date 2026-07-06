import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Topic } from '../models/topic.interface.js';
import { TopicService } from './topic';

describe('TopicService', () => {
  let service: TopicService;
  let httpMock: HttpTestingController;

  const topics: Topic[] = [
    {
      id: 1,
      title: 'Angular',
      description: 'Angular topic',
      subscribed: false
    },
    {
      id: 2,
      title: 'TypeScript',
      description: 'TypeScript topic',
      subscribed: true
    }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    service = TestBed.inject(TopicService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve topic list from the API', () => {
    service.list().subscribe((response) => {
      expect(response).toEqual(topics);
    });

    const req = httpMock.expectOne('/api/topics');
    expect(req.request.method).toBe('GET');
    req.flush(topics);
  });

  it('should subscribe to a topic with POST /api/topics/:id/subscription', () => {
    service.subscribe(1).subscribe((response) => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne('/api/topics/1/subscription');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBeNull();
    req.flush(null);
  });

  it('should unsubscribe from a topic with DELETE /api/topics/:id/subscription', () => {
    service.unsubscribe(2).subscribe((response) => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne('/api/topics/2/subscription');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
