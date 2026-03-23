import {Component, inject, OnInit} from '@angular/core';
import {TaskService} from '../../services/task.service';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {Task} from '../../dto/task';
import {TaskStatus} from '../../enums/task-status';
import {FormsModule, NgForm} from '@angular/forms';

@Component({
  selector: 'app-edit-task',
  imports: [
    FormsModule,
    RouterLink
  ],
  templateUrl: './edit-task.html',
  styleUrl: './edit-task.css',
})
export class EditTaskComponent implements OnInit{

  taskService = inject(TaskService)
  route = inject(ActivatedRoute)
  router = inject(Router)

  task: Task = {
    title: '',
    status: TaskStatus.OPEN
  }

  statuses = Object.values(TaskStatus)

  id?: number
  errorMessage: string = ''

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id']

    if (this.id) {
      this.taskService.getTask(this.id).subscribe({
        next: (res) => {
          this.task = res
        },
        error: (err) => {
          this.errorMessage = err.error
        }
      })
    }
  }

  onSubmit(form: NgForm) {
    if(form.invalid) {
      form.control.markAllAsTouched()
      return
    }

    if (this.id) {
      this.taskService.updateTask(this.id, this.task).subscribe({
        next: () => {
          this.router.navigateByUrl("tasks")
        },
        error: (err) => {
          this.errorMessage = err.error
      }
      })
    }
    else {
      this.taskService.createTask(this.task).subscribe({
        next: () => {
          this.router.navigateByUrl("tasks")
        },
        error: (err) => {
          this.errorMessage = err.error
        }
      })
    }
  }

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

}
