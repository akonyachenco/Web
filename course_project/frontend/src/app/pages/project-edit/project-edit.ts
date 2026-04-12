import {Component, inject, OnInit, signal} from '@angular/core';
import {ProjectService} from '../../services/project.service';
import {ActivatedRoute, Router} from '@angular/router';
import {StatusService} from '../../services/status.service';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Project} from '../../models/project';
import {BackButton} from "../../components/back-button/back-button";

@Component({
  selector: 'app-project-edit',
    imports: [
        FormsModule,
        ReactiveFormsModule,
        BackButton
    ],
  templateUrl: './project-edit.html',
  styleUrl: './project-edit.css',
})
export class ProjectEditComponent implements OnInit {
  projectService = inject(ProjectService)
  activatedRoute = inject(ActivatedRoute)
  statusService = inject(StatusService)
  router = inject(Router)

  statusList: String[] = []
  errorMessage = ''
  id = signal<number | null>(null)

  fb = inject(FormBuilder)
  form = this.fb.group({
    name: ['', Validators.required],
    statusName: ['', Validators.required],
    cost: [0, [Validators.required, Validators.min(0)]],
    startDate: ['', Validators.required],
    deadline: ['', Validators.required],
    description: [''],
  })

  get name() {return this.form.get('name')}
  get statusName() {return this.form.get('statusName')}
  get cost() {return this.form.get('cost')}
  get startDate() {return this.form.get('startDate')}
  get deadline() {return this.form.get('deadline')}


  ngOnInit(): void {
    this.statusService.findAll().subscribe(list => {
      this.statusList = list.map(value => value.name)
    })

    this.id.set(this.activatedRoute.snapshot.params['id'])
    if (this.id()) {
      this.projectService.findById(this.id()!).subscribe({
        next: (project) => {
          this.form.patchValue({
            name: project.name,
            statusName: project.statusName,
            cost: project.cost,
            startDate: project.startDate,
            deadline: project.deadline,
            description: project.description
          })
        },
        error: (err) => {
          this.errorMessage = err.error
          setTimeout(() => this.errorMessage = '', 5000)
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
      const data: Project = {
        ...this.form.value as Project,
        id: this.id()!
      }
      this.projectService.update(data).subscribe({
        next: () => {
          this.router.navigateByUrl("/projects")
        },
        error: (err) => {
          this.errorMessage = err.error
          setTimeout(() => this.errorMessage = '', 5000)
        }
      })
    }
    else {
      this.projectService.create(this.form.value as Project).subscribe({
        next: () => {
          this.router.navigateByUrl("/projects")
        },
        error: (err) => {
          this.errorMessage = err.error
          setTimeout(() => this.errorMessage = '', 5000)
        }
      })
    }

  }

}
