import { ComponentFixture, TestBed } from '@angular/core/testing';
import { WritableSignal, signal } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { SessionInformation } from 'src/app/core/models/sessionInformation.interface';
import { User } from 'src/app/core/models/user.interface';
import { SessionService } from 'src/app/core/service/session.service';
import { UserService } from 'src/app/core/service/user.service';

import { MeComponent } from './me.component';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let router: { navigate: jest.Mock };
  let userService: { getById: jest.Mock; delete: jest.Mock };
  let sessionInformationSignal: WritableSignal<SessionInformation | undefined>;
  let sessionService: { sessionInformation: WritableSignal<SessionInformation | undefined>; logOut: jest.Mock };
  let snackBarOpen: jest.Mock;

  const sessionInformation: SessionInformation = {
    token: 'token',
    type: 'Bearer',
    id: 1,
    username: 'user@test.com',
    firstName: 'Jane',
    lastName: 'Doe',
    admin: false
  };

  const user: User = {
    id: 1,
    email: 'user@test.com',
    firstName: 'Jane',
    lastName: 'Doe',
    admin: false,
    password: 'secret',
    createdAt: new Date('2026-06-01')
  };

  const mount = () => {
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    snackBarOpen = jest.fn();

    (component as any).router = router;
    (component as any).userService = userService;
    (component as any).sessionService = sessionService;
    (component as any).matSnackBar = { open: snackBarOpen };

    fixture.detectChanges();
  };

  beforeEach(async () => {
    router = { navigate: jest.fn() };
    userService = {
      getById: jest.fn(() => of(user)),
      delete: jest.fn(() => of(undefined))
    };
    sessionInformationSignal = signal<SessionInformation | undefined>(sessionInformation);
    sessionService = {
      sessionInformation: sessionInformationSignal,
      logOut: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [MeComponent],
      providers: [
        { provide: SessionService, useValue: sessionService },
        { provide: UserService, useValue: userService },
        { provide: Router, useValue: router },
        { provide: MatSnackBar, useValue: { open: jest.fn() } }
      ]
    }).compileComponents();

    mount();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should build user stream on init', () => {
    component.user$.subscribe((result) => {
      expect(result).toEqual(user);
    });

    expect(userService.getById).toHaveBeenCalledWith('1');
  });

  it('should render user information when logged in', () => {
    const content = fixture.nativeElement.textContent;

    expect(content).toContain('User information');
    expect(content).toContain('Name: Jane DOE');
    expect(content).toContain('Email: user@test.com');
  });

  it('should render delete account section for non-admin users', () => {
    const content = fixture.nativeElement.textContent;

    expect(content).toContain('Delete my account:');
  });

  it('should hide delete account section for admin users', () => {
    const adminUser: User = { ...user, admin: true };
    sessionInformationSignal.set(sessionInformation);
    userService.getById.mockReturnValue(of(adminUser));

    mount();

    const content = fixture.nativeElement.textContent;
    expect(content).toContain('You are admin');
    expect(content).not.toContain('Delete my account:');
  });

  it('should redirect to login when no session is available on init', () => {
    sessionInformationSignal.set(undefined);

    mount();

    component.user$.subscribe();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should show snackbar when profile loading fails', () => {
    sessionInformationSignal.set(sessionInformation);
    userService.getById.mockReturnValue(throwError(() => new Error('profile-error')));

    mount();

    component.user$.subscribe();
    expect(snackBarOpen).toHaveBeenCalledWith('Unable to load your profile', 'Close', { duration: 3000 });
  });

  it('should go back in browser history', () => {
    const backSpy = jest.spyOn(window.history, 'back').mockImplementation(() => undefined);

    component.back();

    expect(backSpy).toHaveBeenCalled();
    backSpy.mockRestore();
  });

  it('should redirect to login when deleting without session', () => {
    sessionInformationSignal.set(undefined);

    component.delete();

    expect(router.navigate).toHaveBeenCalledWith(['/login']);
    expect(userService.delete).not.toHaveBeenCalled();
  });

  it('should delete account and logout', () => {
    component.delete();

    expect(userService.delete).toHaveBeenCalledWith('1');
    expect(snackBarOpen).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
    expect(sessionService.logOut).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/']);
  });

  it('should show snackbar when account deletion fails', () => {
    userService.delete.mockReturnValue(throwError(() => new Error('delete-error')));

    component.delete();

    expect(snackBarOpen).toHaveBeenCalledWith('Unable to delete your account', 'Close', { duration: 3000 });
  });
});
