import {Component, inject, OnInit} from '@angular/core';
import {AuthService} from '../../auth/auth.service';
import {TaskService} from '../../services/task.service';
import {Task} from '../../models/task';
import {TaskItem} from '../../components/task-item/task-item';
import {Project} from '../../models/project';
import {ProjectService} from '../../services/project.service';
import {ProjectItem} from '../../components/project-item/project-item';
import {ACTIVE_STATUS_COUNT, ACTIVE_STATUSES} from '../../constants/active-statuses';
import {StatusService} from '../../services/status.service';

@Component({
  selector: 'app-main',
  imports: [
    TaskItem,
    ProjectItem
  ],
  templateUrl: './main.html',
  styleUrl: './main.css',
})
export class MainComponent implements OnInit {
  authService = inject(AuthService)
  taskService = inject(TaskService)
  statusService = inject(StatusService)
  projectService = inject(ProjectService)


  tasks: Task[] = []
  projects: Project[] = []

  ngOnInit(): void {
    this.findActiveTasks()
    this.findActiveProjects()
  }

  findActiveTasks() {
    let countRequest = 0
    ACTIVE_STATUSES.forEach(status => {
      this.taskService.findMyTasks(status).subscribe(
        tasks => {
          this.tasks = [...this.tasks, ...tasks]

          if (++countRequest === ACTIVE_STATUS_COUNT)
            this.tasks = this.statusService.sortByStatusName(this.tasks)
        }
      )
    })
  }

  findActiveProjects() {
    let countRequest = 0
    ACTIVE_STATUSES.forEach(status => {
      this.projectService.findMyProjects(status).subscribe(
        projects => {
          this.projects = [...this.projects, ...projects]

          if (++countRequest === ACTIVE_STATUS_COUNT)
            this.projects = this.statusService.sortByStatusName(this.projects)
        }
      )
    })
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
        this.tasks = this.tasks.filter(task => task.id != data.id)
        break
      }
    }
    this.tasks = this.statusService.sortByStatusName(this.tasks)
  }

}
