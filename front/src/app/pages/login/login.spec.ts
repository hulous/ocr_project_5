import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { Session } from 'src/app/core/models/session.interface';
import { AuthService } from 'src/app/core/services/auth';
import { SessionService } from 'src/app/core/services/session';

import { LoginComponent } from './login';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: { login: jest.Mock };
  let sessionService: { logIn: jest.Mock };
  let router: { navigate: jest.Mock };

  const Session: Session = {
    token: 'token',
    type: 'Bearer',
    id: 1,
    username: 'john@doe.com',
    firstName: 'John',
    lastName: 'Doe',
    admin: false
  };

  beforeEach(async () => {
    authService = {
      login: jest.fn(() => of(Session))
    };
    sessionService = {
      logIn: jest.fn()
    };
    router = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: SessionService, useValue: sessionService },
        { provide: Router, useValue: router }
      ],
      imports: [
        LoginComponent
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should login and navigate to home on submit success', () => {
    component.form.setValue({
      login: 'john@doe.com',
      password: 'secret'
    });

    component.submit();

    expect(authService.login).toHaveBeenCalledWith({
      login: 'john@doe.com',
      password: 'secret'
    });
    expect(sessionService.logIn).toHaveBeenCalledWith(Session);
    expect(router.navigate).toHaveBeenCalledWith(['/home']);
    expect(component.onError).toBe(false);
  });

  it('should set onError to true when submit fails', () => {
    authService.login.mockReturnValue(throwError(() => new Error('login-error')));
    component.form.setValue({
      email: 'john@doe.com',
      password: 'secret'
    });

    component.submit();

    expect(component.onError).toBe(true);
    expect(sessionService.logIn).not.toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();
  });
});
