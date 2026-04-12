import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Status} from '../../models/status';

@Component({
  selector: 'app-status-item',
  imports: [],
  templateUrl: './status-item.html',
  styleUrl: './status-item.css',
})
export class StatusItem {
  @Input() status!: Status
  @Output() editStatus = new EventEmitter<{id: number, name: string}>()
  @Output() deleteStatus = new EventEmitter<number>()


  onDelete() {
    this.deleteStatus.emit(this.status.id)
  }


  onEdit() {
    this.editStatus.emit({id: this.status.id!, name: this.status.name})
  }

}
