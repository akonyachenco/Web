import {Component, inject, OnInit, signal} from '@angular/core';
import {SprintService} from '../../services/sprint.service';
import {ActivatedRoute, Router} from '@angular/router';
import {StatusService} from '../../services/status.service';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Sprint} from '../../models/sprint';
import {BackButton} from "../../components/back-button/back-button";

@Component({
  selector: 'app-sprint-edit',
    imports: [
        FormsModule,
        ReactiveFormsModule,
        BackButton
    ],
  templateUrl: './sprint-edit.html',
  styleUrl: './sprint-edit.css',
})
export class SprintEditComponent implements OnInit {
  sprintService = inject(SprintService)
  statusService = inject(StatusService)
  activatedRoute = inject(ActivatedRoute)
  router = inject(Router)

  statusList: String[] = []
  errorMessage = ''
  projectId = signal<number | null>(null)
  id = signal<number | null>(null)

  fb = inject(FormBuilder)
  form = this.fb.group({
    name: ['', Validators.required],
    statusName: ['', Validators.required],
    goal: ['', Validators.required],
    startDate: ['', Validators.required],
    endDate: ['', Validators.required]
  })

  get name() {return this.form.get('name')}
  get statusName() {return this.form.get('statusName')}
  get goal() {return this.form.get('goal')}
  get startDate() {return this.form.get('startDate')}
  get endDate() {return this.form.get('endDate')}

  ngOnInit(): void {
    this.statusService.findAll().subscribe(list => {
      this.statusList = list.map(value => value.name)
    })

    this.projectId.set(this.activatedRoute.snapshot.params['projectId'])
    this.id.set(this.activatedRoute.snapshot.params['id'])

    if (this.id()) {
      this.sprintService.findById(this.id()!).subscribe({
        next: sprint => {
          this.form.patchValue({
            name: sprint.name,
            statusName: sprint.statusName,
            goal: sprint.goal,
            startDate: sprint.startDate,
            endDate: sprint.endDate
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
      const data: Sprint = {
        ...this.form.value as Sprint,
        id: this.id()!
      }
      this.sprintService.update(data).subscribe({
        next: () => {
          this.router.navigateByUrl(`/project/${this.projectId()}/sprint`)
        },
        error: (err) => {
          this.errorMessage = err.error
          setTimeout(() => this.errorMessage = '', 5000)
        }
      })
    }
    else {
      const data: Sprint = {
        ...this.form.value as Sprint,
        id: this.id()!,
        projectId: this.projectId()!
      }
      this.sprintService.create(data).subscribe({
        next: () => {
          this.router.navigateByUrl(`/project/${this.projectId()}/sprint`)
        },
        error: (err) => {
          this.errorMessage = err.error
          setTimeout(() => this.errorMessage = '', 5000)
        }
      })
    }
  }

}
