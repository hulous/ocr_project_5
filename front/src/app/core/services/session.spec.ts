import { TestBed } from '@angular/core/testing';
import { Injector } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
import { Session } from '../models/session.interface.js';

import { SessionService } from './session';

describe('SessionService', () => {
  let service: SessionService;
  let injector: Injector;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
    injector = TestBed.inject(Injector);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should start logged out', () => {
    expect(service.session()).toBeUndefined();
    expect(service.isLogged()).toBe(false);
  });

  it('should log in and expose snapshot values', () => {
    const session: Session = {
      token: 'token',
      type: 'Bearer',
      id: 7,
      username: 'admin@site.com',
      admin: true
    };

    service.logIn(session);

    expect(service.session()).toEqual(session);
    expect(service.isLogged()).toBe(true);
  });

  it('should emit login state updates', () => {
    const emitted: boolean[] = [];
    const sub = toObservable(service.isLogged, { injector }).subscribe((value) => emitted.push(value));
    TestBed.flushEffects();

    service.logIn({
      token: 'token',
      type: 'Bearer',
      id: 1,
      username: 'user@site.com',
      admin: false
    });
    TestBed.flushEffects();

    service.logOut();
    TestBed.flushEffects();

    expect(emitted).toEqual([false, true, false]);
    sub.unsubscribe();
  });

  it('should emit admin state updates', () => {
    const emitted: boolean[] = [];
    const sub = toObservable(service.isAdmin, { injector }).subscribe((value) => emitted.push(value));
    TestBed.flushEffects();

    service.logIn({
      token: 'token',
      type: 'Bearer',
      id: 2,
      username: 'admin@site.com',
      admin: true
    });
    TestBed.flushEffects();

    service.logOut();
    TestBed.flushEffects();

    expect(emitted).toEqual([false, true, false]);
    sub.unsubscribe();
  });

  it('should emit session object updates', () => {
    const emitted: Array<Session | undefined> = [];
    const sub = toObservable(service.session, { injector }).subscribe((value) => emitted.push(value));
    TestBed.flushEffects();

    const session: Session = {
      token: 'token',
      type: 'Bearer',
      id: 3,
      username: 'john@site.com',
      admin: false
    };

    service.logIn(session);
    TestBed.flushEffects();
    service.logOut();
    TestBed.flushEffects();

    expect(emitted).toEqual([undefined, session, undefined]);
    sub.unsubscribe();
  });
});
