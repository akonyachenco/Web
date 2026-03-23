import {TaskStatus} from '../enums/task-status';


export interface Task {
  id?: number;
  title: string;
  status: TaskStatus;
  createdBy?: number;
  createdAt?: Date;
}
