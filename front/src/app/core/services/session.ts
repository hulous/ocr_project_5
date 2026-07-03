import { Injectable, computed, signal } from '@angular/core';
import { Session } from '../models/session.interface.js';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private static readonly storageKey = 'mddapi_session';
  private readonly _session = signal<Session | undefined>(this.restoreSession());

  public readonly session = this._session.asReadonly();
  public readonly isLogged = computed(() => !!this._session());
  public readonly isAdmin = computed(() => !!this._session()?.admin);

  public logIn(user: Session): void {
    this._session.set(user);
    this.saveSession(user);
  }

  public logOut(): void {
    this._session.set(undefined);
    this.clearSession();
  }

  private restoreSession(): Session | undefined {
    const raw = localStorage.getItem(SessionService.storageKey);

    if (!raw) {
      return undefined;
    }

    try {
      return JSON.parse(raw) as Session;
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
}
