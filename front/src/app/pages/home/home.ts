import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';
import { NotLoggedLogoComponent } from '../../components/logo/logo';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [MatButtonModule, RouterModule, NotLoggedLogoComponent],
  templateUrl: './home.html'
})

export class HomeComponent {}
