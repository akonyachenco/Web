import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {RouterLink} from '@angular/router';
import {DatePipe} from '@angular/common';
import {Task} from '../../dto/task';
import {TaskStatus} from '../../enums/task-status';
import {AuthService} from '../../auth/auth.service';

@Component({
  selector: 'app-task-item',
  imports: [RouterLink, DatePipe],
  templateUrl: './task-item.html',
  styleUrl: './task-item.css',
})
export class TaskItemComponent {
  @Input() task!: Task
  @Input() isDeleteDisabled!: boolean
  @Output() deleteTask = new EventEmitter<number>();

  getStatusColor(status: TaskStatus): string {
    switch (status) {
      case TaskStatus.OPEN:
        return 'open'
      case TaskStatus.IN_PROGRESS:
        return 'in-progress'
      case TaskStatus.DONE:
        return 'done'
      case TaskStatus.CLOSED:
        return 'closed'
    }
  }

  onDelete(): void {
    this.deleteTask.emit(this.task.id)
  }

}
