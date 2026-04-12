import {Component, inject, OnInit, signal} from '@angular/core';
import {EmployeeService} from '../../services/employee.service';
import {Employee} from '../../models/employee';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {EmployeeItem} from '../../components/employee-item/employee-item';
import {ProjectService} from '../../services/project.service';
import {BackButton} from '../../components/back-button/back-button';
import {AuthService} from '../../auth/auth.service';

@Component({
  selector: 'app-employee-list',
  imports: [
    EmployeeItem,
    BackButton
  ],
  templateUrl: './employee-list.html',
  styleUrl: './employee-list.css',
})
export class EmployeeListComponent implements OnInit {
  employeeService = inject(EmployeeService)
  projectService = inject(ProjectService)
  activatedRoute = inject(ActivatedRoute)
  authService = inject(AuthService)
  router = inject(Router)

  employeeList: Employee[] = []
  employeeIds = signal<number[]>([])
  errorMessage = ''
  mode = signal<string>(this.activatedRoute.snapshot.params['mode'])
  projectId = signal<number>(this.activatedRoute.snapshot.params['projectId'])

  ngOnInit(): void {
    if (this.mode() && this.projectId()) {
      if (this.mode() === 'add') {
        if (!this.authService.isAdmin())
          this.router.navigateByUrl('/')
        this.employeeService.findEmployeesNotInProject(this.projectId())
          .subscribe(list => this.employeeList = list)
      }
      else if (this.mode() === 'view') {
        this.employeeService.findByProjectId(this.projectId())
          .subscribe(list => this.employeeList = list)
      }
    }
  }


  onChangeCheckbox(id: number) {
    if (this.employeeIds().includes(id)) {
      this.employeeIds.update(current => current.filter(val => val !== id))
    }
    else {
      this.employeeIds.update(current => [...current, id])
    }
  }

  addToTeam() {
    this.projectService.addToTeam(this.projectId(), this.employeeIds()).subscribe({
      next: () => {
        this.employeeIds.set([])
        this.employeeService.findEmployeesNotInProject(this.projectId())
          .subscribe(list => this.employeeList = list)
      },
      error: (err) => {
        this.errorMessage = err.error
        setTimeout(() => this.errorMessage = '', 5000);
      }
    })
  }

  onRemoveFromTeam(id: number) {
    this.projectService.removeFromTeam(this.projectId(), id).subscribe({
      next: () => {
        this.employeeList = this.employeeList.filter(employee => employee.id !== id)
      },
      error: (err) => {
        this.errorMessage = err.error
        setTimeout(() => this.errorMessage = '', 5000);
      }
    })
  }

}
