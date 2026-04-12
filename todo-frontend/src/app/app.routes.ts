import { Routes } from '@angular/router';
import {LoginComponent} from './pages/login/login';
import {TaskListComponent} from './pages/task-list/task-list';
import {EditTaskComponent} from './pages/edit-task/edit-task';
import {canActivateAuth} from './auth/access.guard';

export const routes: Routes = [
  {path: "", redirectTo: "login", pathMatch: "full"},
  {path: "login", component: LoginComponent},
  {path: "tasks", component: TaskListComponent, canActivate:[canActivateAuth]},
  {path: "tasks/new", component: EditTaskComponent, canActivate:[canActivateAuth]},
  {path: "tasks/:id", component: EditTaskComponent, canActivate:[canActivateAuth]}
];
