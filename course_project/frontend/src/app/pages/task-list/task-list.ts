import {Component, inject, OnInit, signal} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {TaskService} from '../../services/task.service';
import { Task } from '../../models/task';
import {AuthService} from '../../auth/auth.service';
import {TaskItem} from '../../components/task-item/task-item';
import {Location} from '@angular/common';
import {BackButton} from '../../components/back-button/back-button';
import {StatusService} from '../../services/status.service';

@Component({
  selector: 'app-task-list',
  imports: [
    RouterLink,
    TaskItem,
    BackButton
  ],
  templateUrl: './task-list.html',
  styleUrl: './task-list.css',
})
export class TaskListComponent implements OnInit {
  taskService = inject(TaskService)
  authService = inject(AuthService)
  statusService = inject(StatusService)
  activatedRoute = inject(ActivatedRoute)


  tasks: Task[] = []
  errorMessage = ''
  projectId = signal<number>(this.activatedRoute.snapshot.params['projectId'])
  sprintId = signal<number>(this.activatedRoute.snapshot.params['sprintId'])

  ngOnInit(): void {
    this.taskService.findAllBySprintId(this.sprintId()).subscribe(list => this.tasks = this.statusService.sortByStatusName(list))
  }


  onDeleteTask(id: number) {
    this.taskService.delete(id).subscribe({
      next: () => {
        this.tasks = this.tasks.filter(task => task.id !== id)
      },
      error: (err) => {
        this.errorMessage = err.error || 'Ошибка удаления'
        setTimeout(() => this.errorMessage = '', 5000);
      }
    })
  }

}
