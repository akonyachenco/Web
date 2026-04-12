import {Component, inject, OnInit, signal} from '@angular/core';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {TaskService} from '../../services/task.service';
import {ActivatedRoute, Router} from '@angular/router';
import {StatusService} from '../../services/status.service';
import {PriorityEnum, PriorityLabels} from '../../enums/priority.enum';
import {Employee} from '../../models/employee';
import {EmployeeService} from '../../services/employee.service';
import {EmployeeItem} from '../../components/employee-item/employee-item';
import { map} from 'rxjs';
import { Task } from '../../models/task';
import {BackButton} from '../../components/back-button/back-button';

@Component({
  selector: 'app-task-edit',
  imports: [
    ReactiveFormsModule,
    FormsModule,
    EmployeeItem,
    BackButton
  ],
  templateUrl: './task-edit.html',
  styleUrl: './task-edit.css',
})
export class TaskEditComponent implements OnInit {
  taskService = inject(TaskService)
  statusService = inject(StatusService)
  employeeService = inject(EmployeeService)
  activatedRoute = inject(ActivatedRoute)
  router = inject(Router)

  employeeList: Employee[] = []
  employee = signal<Employee | null>(null)
  statusList: string[] = []

  priorityList = Object.values(PriorityEnum)
  priorityLabels = PriorityLabels

  errorMessage = ''

  id = signal<number>(this.activatedRoute.snapshot.params['id'])
  projectId = signal<number>(this.activatedRoute.snapshot.params['projectId'])
  sprintId = signal<number>(this.activatedRoute.snapshot.params['sprintId'])

  mode = signal<string>('')
  addEmployee = signal<boolean>(false)

  fb = inject(FormBuilder)
  form = this.fb.group({
    title: ['', Validators.required],
    priority: ['', Validators.required],
    statusName: ['', Validators.required],
    description: ['']
  })

  get title() {return this.form.get('title')}
  get priority() {return this.form.get('priority')}
  get statusName() {return this.form.get('statusName')}


  ngOnInit(): void {
    this.statusService.findAll().subscribe(list => {
      this.statusList = list.map(value => value.name)
    })

    if (this.id()) {
      this.taskService.findById(this.id()).subscribe({
        next: task => {
          this.form.patchValue({
            title: task.title,
            priority: task.priority,
            statusName: task.statusName,
            description: task.description
          })

          this.employeeService.findById(task.employeeId).subscribe({
            next: employee => {
              this.employee.set(employee)
            },
            error: err => {
              this.errorMessage = err.error
              setTimeout(() => this.errorMessage ='', 5000)
            }
          })
        },
        error: err => {
          this.errorMessage = err.error
          setTimeout(() => this.errorMessage ='', 5000)
        }
      })

    }
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched()
      return
    }

    if (this.id()) {
      const data: Task = {
        ...this.form.value as Task,
        id: this.id(),
        employeeId: this.employee()?.id!,
      }

      this.taskService.update(data).subscribe({
        next: () => {
          this.router.navigateByUrl(`/project/${this.projectId()}/sprint/${this.sprintId()}/task`)
        },
        error: err => {
          this.errorMessage = err.error
          setTimeout(() => this.errorMessage ='', 5000)
        }
      })
    }
    else {
      const data: Task = {
        ...this.form.value as Task,
        id: this.id(),
        employeeId: this.employee()?.id!,
        sprintId: this.sprintId()
      }

      this.taskService.create(data).subscribe({
        next: () => {
          this.router.navigateByUrl(`/project/${this.projectId()}/sprint/${this.sprintId()}/task`)
        },
        error: err => {
          this.errorMessage = err.error
          setTimeout(() => this.errorMessage ='', 5000)
        }
      })
    }

  }

  onChange() {
    this.employeeService.findByProjectId(this.projectId())
      .pipe(
        map(list => list.filter(employee => employee.id != this.employee()?.id))
      )
      .subscribe(list => this.employeeList = list)
    this.mode.set('setTaskEmployee')
    this.addEmployee.set(true)
  }

  onSetEmployee(id: number) {
    this.employeeService.findById(id).subscribe({
      next: employee => {
        this.employee.set(employee)
        this.mode.set('')
        this.addEmployee.set(false)
      },
      error: err => {
        this.errorMessage = err.error
        setTimeout(() => this.errorMessage ='', 5000)
      }
    })
  }

}
