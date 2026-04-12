import {Component, inject, OnInit, signal} from '@angular/core';
import {StatusService} from '../../services/status.service';
import {Status} from '../../models/status';
import {StatusItem} from '../../components/status-item/status-item';
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'app-status-list',
  imports: [
    StatusItem,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './status-list.html',
  styleUrl: './status-list.css',
})
export class StatusListComponent implements OnInit {
  statusService = inject(StatusService)

  statusList: Status[] = []
  createMode = signal<boolean>(false)
  editMode = signal<boolean>(false)
  idForUpdate: number | undefined = undefined
  errorMessage = ''

  fb = inject(FormBuilder)
  form = this.fb.group({
    name: ['', Validators.required]
  })

  get name() {return  this.form.get('name')}

  ngOnInit(): void {
    this.statusService.findAll().subscribe(list => {this.statusList = list})
  }

  onEditStatus(data:{id: number, name: string}) {
    this.editMode.set(true)
    this.name?.setValue(data.name)
    this.idForUpdate = data.id
  }

  onDeleteStatus(id: number) {
    this.statusService.delete(id).subscribe({
      next: () => {
        this.statusList = this.statusList.filter(s => s.id !== id)
      },
      error: (err) => {
        this.errorMessage = err.error || 'Ошибка удаления'
        setTimeout(() => this.errorMessage = '', 5000);
      }
    })
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched()
      return
    }
    if(this.editMode()) {
      this.statusService.update(this.idForUpdate!, this.name?.value!).subscribe({
        next: (value) => {
          this.statusList = this.statusList.map(s => s.id === value.id ? value : s)
          this.editMode.set(false)
          this.idForUpdate = undefined
        },
        error: (err) => {
          this.errorMessage = err.error
          setTimeout(() => this.errorMessage = '', 5000)
        }
      })
    }
    else {
      this.statusService.create(this.form.value as Status).subscribe({
        next: (value) => {
          this.statusList = [...this.statusList, value]
          this.createMode.set(false)
        },
        error: (err) => {
          this.errorMessage = err.error
          setTimeout(() => this.errorMessage = '', 5000)
        }
      })
    }
    this.form.reset()
  }

  onCancel() {
    this.form.reset()
    this.errorMessage = ''
    this.editMode.set(false)
    this.createMode.set(false)
  }

}
