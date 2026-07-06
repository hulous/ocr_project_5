import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { CreatePostRequest } from '../models/create-post-request.interface.js';
import { Post } from '../models/post.interface.js';
import { PostService } from './post';

describe('PostService', () => {
  let service: PostService;
  let httpMock: HttpTestingController;

  const post: Post = {
    id: 1,
    topicId: 1,
    authorId: 1,
    authorUsername: 'john@doe.com',
    topicTitle: 'Angular',
    topicDescription: 'A frontend framework for building web apps.',
    title: 'Test post',
    content: 'Test content',
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString(),
    comments: []
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    service = TestBed.inject(PostService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should list posts for a topic', () => {
    service.listForTopic(1).subscribe((response) => {
      expect(response).toEqual([post]);
    });

    const req = httpMock.expectOne('/api/topics/1/posts');
    expect(req.request.method).toBe('GET');
    req.flush([post]);
  });

  it('should fetch a single post', () => {
    service.show(1).subscribe((response) => {
      expect(response).toEqual(post);
    });

    const req = httpMock.expectOne('/api/posts/1');
    expect(req.request.method).toBe('GET');
    req.flush(post);
  });

  it('should create a new post', () => {
    const payload: CreatePostRequest = {
      title: 'Test post',
      content: 'Test content'
    };

    service.create(1, payload).subscribe((response) => {
      expect(response).toEqual(post);
    });

    const req = httpMock.expectOne('/api/topics/1/posts');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush(post);
  });
});
