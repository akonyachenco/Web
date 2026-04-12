import {Component, inject, OnInit} from '@angular/core';
import {TaskService} from '../../services/task.service';
import {Task} from '../../models/task';
import {TaskItem} from '../../components/task-item/task-item';
import {StatusService} from '../../services/status.service';

@Component({
  selector: 'app-my-tasks',
  imports: [
    TaskItem
  ],
  templateUrl: './my-tasks.html',
  styleUrl: './my-tasks.css',
})
export class MyTasksComponent implements OnInit {
  taskService = inject(TaskService)
  statusService = inject(StatusService)

  tasks: Task[] = []

  ngOnInit(): void {
    this.taskService.findMyTasks().subscribe(
      tasks =>
        this.tasks = this.statusService.sortByStatusName(tasks))
  }


  onChangeTaskStatus(data: {id: number, currentStatus: string}) {
    switch (data.currentStatus) {
      case ('Запланировано'): {
        this.taskService.changeStatus(data.id, 'В процессе').subscribe()
        this.tasks.map(task => {
          if (task.id === data.id)
            task.statusName = 'В процессе'
        })
        break
      }
      case ('В процессе'): {
        this.taskService.changeStatus(data.id, 'Завершено').subscribe()
        this.tasks.map(task => {
          if (task.id === data.id)
            task.statusName = 'Завершено'
        })
        break
      }
    }
  }

}
