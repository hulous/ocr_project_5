import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MaterialModule } from '../../shared/material';
import { SessionService } from '../../core/services/session';

@Component({
  selector: 'app-header-bar',
  standalone: true,
  imports: [CommonModule, RouterModule, MaterialModule],
  templateUrl: './header-bar.html',
})

export class HeaderBarComponent {
  private readonly router = inject(Router);
  private readonly sessionService = inject(SessionService);

  public logout(): void {
    this.sessionService.logOut();
    this.router.navigate(['/login']);
  }
}
