import {Component, EventEmitter, inject, input, Input, Output, WritableSignal} from '@angular/core';
import {Employee} from '../../models/employee';
import {AuthService} from '../../auth/auth.service';

@Component({
  selector: 'app-employee-item',
  imports: [],
  templateUrl: './employee-item.html',
  styleUrl: './employee-item.css',
})
export class EmployeeItem {
  @Input() employee!: Employee
  @Input() mode!: WritableSignal<string>
  @Output() changeCheckbox = new EventEmitter<number>()
  @Output() removeFromTeam = new EventEmitter<number>()
  @Output() setTaskEmployee = new EventEmitter<number>()

  authService = inject(AuthService)

  onChange() {
    this.changeCheckbox.emit(this.employee.id)
  }

  onRemove() {
    this.removeFromTeam.emit(this.employee.id)
  }

  onSetEmployee() {
    this.setTaskEmployee.emit(this.employee.id)
  }

}
