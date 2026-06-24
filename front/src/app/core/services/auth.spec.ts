import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { LoginRequest } from '../models/login-request.interface.js';
import { RegisterRequest } from '../models/register-request.interface.js';
import { Session } from '../models/session.interface.js';

import { AuthService } from './auth.js';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should register user with POST /api/auth/register', () => {
    const payload: RegisterRequest = {
      email: 'john@doe.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'secret'
    };

    service.register(payload).subscribe((response) => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne('/api/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush(null);
  });

  it('should login user with POST /api/auth/login', () => {
    const payload: LoginRequest = {
      email: 'john@doe.com',
      password: 'secret'
    };

    const sessionInfo: Session = {
      token: 'token',
      type: 'Bearer',
      id: 1,
      username: 'john@doe.com',
      firstName: 'John',
      lastName: 'Doe',
      admin: false
    };

    service.login(payload).subscribe((response) => {
      expect(response).toEqual(sessionInfo);
    });

    const req = httpMock.expectOne('/api/auth/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush(sessionInfo);
  });
});
