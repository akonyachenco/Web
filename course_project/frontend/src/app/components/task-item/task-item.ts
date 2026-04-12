import {Component, EventEmitter, inject, Input, OnInit, Output, signal} from '@angular/core';
import {Task} from '../../models/task';
import {PriorityEnum, PriorityLabels} from '../../enums/priority.enum';
import {DatePipe} from '@angular/common';
import {AuthService} from '../../auth/auth.service';
import {Router, RouterLink} from '@angular/router';
import {EmployeeService} from '../../services/employee.service';
import {Employee} from '../../models/employee';
import {EmployeeItem} from '../employee-item/employee-item';
import {ACTIVE_STATUSES} from '../../constants/active-statuses';
import {Sprint} from '../../models/sprint';
import {Project} from '../../models/project';
import {SprintService} from '../../services/sprint.service';
import {ProjectService} from '../../services/project.service';

@Component({
  selector: 'app-task-item',
  imports: [
    DatePipe,
    RouterLink,
    EmployeeItem
  ],
  templateUrl: './task-item.html',
  styleUrl: './task-item.css',
})
export class TaskItem implements  OnInit {
  @Input() task!: Task
  @Output() deleteTask = new EventEmitter<number>()
  @Output() changeStatus = new EventEmitter<{id: number, currentStatus: string}>()

  authService = inject(AuthService)
  employeeService = inject(EmployeeService)
  sprintService = inject(SprintService)
  projectService = inject(ProjectService)
  router = inject(Router)

  sprint = signal<Sprint | null>(null)
  project = signal<Project | null>(null)
  mode = signal<string>('')

  employee = signal<Employee | null>(null)
  showAdminPanel = signal<boolean>(true)
  private readonly IGNORE_ADMIN_PATHS = [
    '/',
    '/my-tasks'
  ]

  priorityLabels = PriorityLabels;

  ngOnInit(): void {
    this.showAdminPanel.set(!this.IGNORE_ADMIN_PATHS.some(path => this.router.url === path))
    if (this.task.employeeId !== null && this.showAdminPanel())
      this.employeeService.findById(this.task?.employeeId).subscribe(employee => this.employee.set(employee))

    if (this.task.sprintId !== null) {
      this.sprintService.findById(this.task?.sprintId!).subscribe(sprint => this.sprint.set(sprint))
      this.projectService.findBySprintId(this.task.sprintId!).subscribe(project => this.project.set(project))
    }
  }

  onDelete() {
    this.deleteTask.emit(this.task.id)
  }

  onChangeStatus() {
    this.changeStatus.emit({id: this.task.id!, currentStatus: this.task.statusName})
  }

  protected readonly ACTIVE_STATUSES = ACTIVE_STATUSES;
}
