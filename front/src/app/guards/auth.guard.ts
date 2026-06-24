import { Injectable } from "@angular/core";
import { CanActivate, Router } from "@angular/router";
import { SessionService } from "../core/services/session";

@Injectable({providedIn: 'root'})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private sessionService: SessionService) {}

  public canActivate(): boolean {
    if (!this.sessionService.isLogged()) {
      this.router.navigate(['login']);
      return false;
    }

    return true;
  }
}
