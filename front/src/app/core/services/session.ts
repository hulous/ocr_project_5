import { Injectable, computed, signal } from '@angular/core';
import { Session } from '../models/session.interface.js';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private static readonly storageKey = 'mddapi_session';
  private tokenExpirationTimer?: number;
  private readonly _session = signal<Session | undefined>(this.restoreSession());

  public readonly session = this._session.asReadonly();
  public readonly isLogged = computed(() => !!this._session());
  public readonly isAdmin = computed(() => !!this._session()?.admin);

  public logIn(user: Session): void {
    this._session.set(user);
    this.saveSession(user);
    this.startTokenExpirationTimer(user.token);
  }

  public logOut(): void {
    this._session.set(undefined);
    this.clearSession();
    this.clearTokenExpirationTimer();
  }

  private restoreSession(): Session | undefined {
    const raw = localStorage.getItem(SessionService.storageKey);

    if (!raw) {
      return undefined;
    }

    try {
      const session = JSON.parse(raw) as Session;
      const expiration = this.getTokenExpiration(session.token);

      if (!expiration || expiration <= Date.now()) {
        this.clearSession();
        return undefined;
      }

      this.startTokenExpirationTimer(session.token);
      return session;
    } catch {
      localStorage.removeItem(SessionService.storageKey);
      return undefined;
    }
  }

  private saveSession(session: Session): void {
    localStorage.setItem(SessionService.storageKey, JSON.stringify(session));
  }

  private clearSession(): void {
    localStorage.removeItem(SessionService.storageKey);
  }

  private startTokenExpirationTimer(token: string): void {
    this.clearTokenExpirationTimer();

    const expiration = this.getTokenExpiration(token);
    if (!expiration) {
      return;
    }

    const delay = expiration - Date.now();
    if (delay <= 0) {
      this.logOut();
      return;
    }

    this.tokenExpirationTimer = globalThis.setTimeout(() => {
      this.logOut();
    }, delay);
  }

  private clearTokenExpirationTimer(): void {
    if (this.tokenExpirationTimer !== undefined) {
      globalThis.clearTimeout(this.tokenExpirationTimer);
      this.tokenExpirationTimer = undefined;
    }
  }

  private getTokenExpiration(token: string): number | undefined {
    const parts = token.split('.');
    if (parts.length !== 3) {
      return undefined;
    }

    try {
      const payload = JSON.parse(atob(parts[1]));
      if (typeof payload.exp !== 'number') {
        return undefined;
      }

      return payload.exp * 1000;
    } catch {
      return undefined;
    }
  }
}
