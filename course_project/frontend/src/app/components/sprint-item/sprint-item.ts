import {Component, EventEmitter, inject, Input, Output} from '@angular/core';
import {Sprint} from '../../models/sprint';
import {AuthService} from '../../auth/auth.service';
import {RouterLink} from '@angular/router';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-sprint-item',
  imports: [
    RouterLink,
    DatePipe
  ],
  templateUrl: './sprint-item.html',
  styleUrl: './sprint-item.css',
})
export class SprintItem {
  @Input() sprint!: Sprint
  @Output() deleteSprint = new EventEmitter<number>()

  authService = inject(AuthService)

  onDelete() {
    this.deleteSprint.emit(this.sprint.id)
  }
}
