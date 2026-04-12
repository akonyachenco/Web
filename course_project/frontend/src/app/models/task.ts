import {PriorityEnum} from '../enums/priority.enum';

export interface Task {
  id?: number
  title: string
  description?: string
  priority: PriorityEnum
  createdAt?: string
  statusName: string
  employeeId: number
  sprintId?: number
}
