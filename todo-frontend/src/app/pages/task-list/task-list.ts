import {Component, inject, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';
import {TaskService} from '../../services/task.service';
import {Task} from '../../dto/task';
import {TaskItemComponent} from '../../components/task-item/task-item';
import {AuthService} from '../../auth/auth.service';

@Component({
  selector: 'app-task-list',
  imports: [
    RouterLink,
    TaskItemComponent
  ],
  templateUrl: './task-list.html',
  styleUrl: './task-list.css',
})
export class TaskListComponent implements OnInit{

  taskService = inject(TaskService)
  authService = inject(AuthService)

  tasks: Task[] = []
  isAdmin: boolean = false
  errorMessage: string = ''

  ngOnInit(): void {
    this.taskService.getTasks().subscribe({
      next: (res) => {
        this.tasks = res
        this.authService.isAdmin().subscribe({
          next: (roleAdmin) => {
            this.isAdmin = roleAdmin
          }
        })
      },
      error: (err) => {
        this.errorMessage = err.error
      }
    })
  }

  deleteTaskHandler(id: number) {
    this.taskService.deleteTask(id).subscribe({
      next: () => {
        this.tasks = this.tasks.filter(task => task.id !== id)
      },
      error: (err) => {
        this.errorMessage = err.error.errorMessage
        setTimeout(()=>this.errorMessage = '', 5000)
      }
    })
  }

}
