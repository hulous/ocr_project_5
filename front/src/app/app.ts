import { Component, inject } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { SessionService } from './core/services/session';
import { CommonModule } from "@angular/common";
import { MaterialModule } from "./shared/material";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, MaterialModule, RouterOutlet, RouterModule],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})

export class AppComponent {
  private router = inject(Router);
  private sessionService = inject(SessionService);
  public isLogged = this.sessionService.isLogged;

  public logout(): void {
    this.sessionService.logOut();
    this.router.navigate([''])
  }
}
