import { DestroyRef, Injector, runInInjectionContext } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { FormBuilder } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from './core/services/auth';
import { SessionService } from './core/services/session';
import { TopicService } from './core/services/topic';
import { PostService } from './core/services/post';
import { CommentService } from './core/services/comment';
import { LoginComponent } from './pages/login/login';
import { TopicListComponent } from './pages/topic-list/topic-list';
import { PostsCreateComponent } from './pages/posts/posts-create';
import { PostsShowComponent } from './pages/posts/posts-show';
import { HeaderBarComponent } from './components/header-bar/header-bar';

describe('Front app integration flow', () => {
  let injector: Injector;
  let router: { navigate: jest.Mock };
  let authService: { login: jest.Mock };
  let sessionService: { isLogged: jest.Mock; logIn: jest.Mock; logOut: jest.Mock };
  let topicService: { list: jest.Mock; subscribe: jest.Mock };
  let postService: { create: jest.Mock; show: jest.Mock };
  let commentService: { create: jest.Mock };
  let destroyRef: DestroyRef;

  const loginSession = {
    token: 'header.payload.signature',
    type: 'Bearer',
    id: 1,
    username: 'john@doe.com'
  };

  beforeEach(() => {
    let loggedIn = false;
    let listCalls = 0;

    router = {
      navigate: jest.fn()
    } as unknown as { navigate: jest.Mock };

    authService = {
      login: jest.fn(() => of(loginSession))
    };

    sessionService = {
      isLogged: jest.fn(() => loggedIn),
      logIn: jest.fn(() => {
        loggedIn = true;
      }),
      logOut: jest.fn(() => {
        loggedIn = false;
      })
    };

    topicService = {
      list: jest.fn(() => {
        listCalls += 1;
        return of([
          {
            id: 1,
            title: 'Angular Topics',
            description: 'A topic to subscribe to',
            subscribed: listCalls > 1
          }
        ]);
      }),
      subscribe: jest.fn(() => of(null))
    };

    postService = {
      create: jest.fn((topicId: number, payload: { title: string; content: string }) =>
        of({
          id: 1,
          topicId,
          authorId: 1,
          authorUsername: 'john@doe.com',
          topicTitle: 'Angular Topics',
          title: payload.title,
          content: payload.content,
          createdAt: new Date().toISOString(),
          comments: []
        })
      ),
      show: jest.fn(() =>
        of({
          id: 1,
          authorId: 1,
          authorUsername: 'john@doe.com',
          topicId: 1,
          topicTitle: 'Angular Topics',
          title: 'A sample post',
          content: 'A sample post body.',
          createdAt: new Date().toISOString(),
          comments: []
        })
      )
    };

    commentService = {
      create: jest.fn((postId: number, payload: { content: string }) =>
        of({
          id: 1,
          postId,
          authorId: 1,
          authorUsername: 'john@doe.com',
          content: payload.content
        })
      )
    };

    destroyRef = {
      onDestroy: jest.fn()
    } as unknown as DestroyRef;

    TestBed.configureTestingModule({
      providers: [
        FormBuilder,
        { provide: DestroyRef, useValue: destroyRef },
        { provide: Router, useValue: router },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' } } } },
        { provide: MatSnackBar, useValue: { open: jest.fn() } },
        { provide: AuthService, useValue: authService },
        { provide: SessionService, useValue: sessionService },
        { provide: TopicService, useValue: topicService },
        { provide: PostService, useValue: postService },
        { provide: CommentService, useValue: commentService }
      ]
    });

    injector = TestBed.inject(Injector);
  });

  it('should login, subscribe, create post, create comment, and logout', () => {
    const loginComponent = runInInjectionContext(injector, () => new LoginComponent());
    loginComponent.form.setValue({ login: 'john@doe.com', password: 'secret' });
    loginComponent.submit();

    expect(authService.login).toHaveBeenCalledWith({ login: 'john@doe.com', password: 'secret' });
    expect(sessionService.logIn).toHaveBeenCalled();

    const topicListComponent = runInInjectionContext(injector, () => new TopicListComponent());
    topicListComponent.subscribeTopic(1);

    expect(topicService.subscribe).toHaveBeenCalledWith(1);
    expect(topicListComponent.topics[0].subscribed).toBe(true);

    const postsCreateComponent = runInInjectionContext(injector, () => new PostsCreateComponent());
    postsCreateComponent.form.controls.topicId.setValue(1 as any);
    postsCreateComponent.form.controls.title.setValue('Integration test post');
    postsCreateComponent.form.controls.content.setValue('This is a post created during integration testing.');
    postsCreateComponent.submit();

    expect(postService.create).toHaveBeenCalledWith(1, {
      title: 'Integration test post',
      content: 'This is a post created during integration testing.'
    });

    const postsShowComponent = runInInjectionContext(injector, () => new PostsShowComponent());
    postsShowComponent.commentForm.controls.content.setValue('Great post!');
    postsShowComponent.submitComment();

    expect(commentService.create).toHaveBeenCalledWith(1, { content: 'Great post!' });
    expect(postsShowComponent.post()!.comments?.[0].content).toBe('Great post!');

    const headerComponent = runInInjectionContext(injector, () => new HeaderBarComponent());
    headerComponent.logout();

    expect(sessionService.logOut).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });
});
