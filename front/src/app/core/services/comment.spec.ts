import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { CreateCommentRequest } from '../models/create-comment-request.interface.js';
import { Comment } from '../models/comment.interface.js';
import { CommentService } from './comment';

describe('CommentService', () => {
  let service: CommentService;
  let httpMock: HttpTestingController;

  const comment: Comment = {
    id: 1,
    postId: 1,
    authorId: 1,
    authorUsername: 'john@doe.com',
    content: 'Test comment',
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    service = TestBed.inject(CommentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create a comment for a post', () => {
    const payload: CreateCommentRequest = {
      content: 'Test comment'
    };

    service.create(1, payload).subscribe((response) => {
      expect(response).toEqual(comment);
    });

    const req = httpMock.expectOne('/api/posts/1/comments');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush(comment);
  });
});
