import { Injectable, computed, signal } from '@angular/core';
import { Session } from '../models/session.interface.js';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private readonly _session = signal<Session | undefined>(undefined);

  public readonly session = this._session.asReadonly();
  public readonly isLogged = computed(() => !!this._session());
  public readonly isAdmin = computed(() => !!this._session()?.admin);

  public logIn(user: Session): void {
    this._session.set(user);
  }

  public logOut(): void {
    this._session.set(undefined);
  }
}
