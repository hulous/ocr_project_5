import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [MatButtonModule],
  templateUrl: './home.html',
  styleUrls: ['./home.scss'],
})

export class HomeComponent {
  constructor() {}

  start() {
    alert('Commencez par lire le README et à vous de jouer !');
  }
}
