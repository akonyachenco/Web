import {Component, inject} from '@angular/core';
import {Location} from '@angular/common';

@Component({
  selector: 'app-back-button',
  imports: [],
  templateUrl: './back-button.html',
  styleUrl: './back-button.css',
})
export class BackButton {
  location = inject(Location)
}
