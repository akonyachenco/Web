import { Routes } from '@angular/router';
import {HeaderLayout} from './components/header-layout/header-layout';
import {LoginComponent} from './pages/login/login';
import {canActivateAuth} from './auth/access.guard';
import {RegistrationComponent} from './pages/registration/registration';
import {MainComponent} from './pages/main/main';
import {canActivatePublic} from './auth/public.guard';
import {StatusListComponent} from './pages/status-list/status-list';
import {canActivateAdmin} from './auth/admin.guard';
import {ProjectListComponent} from './pages/project-list/project-list';
import {ProjectEditComponent} from './pages/project-edit/project-edit';
import {SprintListComponent} from './pages/sprint-list/sprint-list';
import {SprintEditComponent} from './pages/sprint-edit/sprint-edit';
import {EmployeeListComponent} from './pages/employee-list/employee-list';
import {TaskListComponent} from './pages/task-list/task-list';
import {TaskEditComponent} from './pages/task-edit/task-edit';
import {MyProjectsComponent} from './pages/my-projects/my-projects';
import {MyTasksComponent} from './pages/my-tasks/my-tasks';


export const routes: Routes = [
  {path: '', component: HeaderLayout, children: [
      {path: '', component: MainComponent},
      {path: 'my-projects', component: MyProjectsComponent},
      {path: 'my-tasks', component: MyTasksComponent},
      {path: 'statuses', component: StatusListComponent, canActivate: [canActivateAdmin]},
      {path: 'projects', component: ProjectListComponent, canActivate: [canActivateAdmin]},
      {path: 'project/edit/:id', component: ProjectEditComponent, canActivate: [canActivateAdmin]},
      {path: 'project/new', component: ProjectEditComponent, canActivate: [canActivateAdmin]},
      {path: 'project/:projectId/sprint', component: SprintListComponent},
      {path: 'project/:projectId/sprint/edit/:id', component: SprintEditComponent, canActivate: [canActivateAdmin]},
      {path: 'project/:projectId/sprint/new', component: SprintEditComponent, canActivate: [canActivateAdmin]},
      {path: 'project/:projectId/team/:mode', component: EmployeeListComponent},
      {path: 'project/:projectId/sprint/:sprintId/task', component: TaskListComponent, canActivate: [canActivateAdmin]},
      {path: 'project/:projectId/sprint/:sprintId/task/edit/:id', component: TaskEditComponent, canActivate: [canActivateAdmin]},
      {path: 'project/:projectId/sprint/:sprintId/task/new', component: TaskEditComponent, canActivate: [canActivateAdmin]},
      ], canActivate: [canActivateAuth]},
  {path: 'login', component: LoginComponent, canActivate: [canActivatePublic]},
  {path: 'register', component: RegistrationComponent, canActivate: [canActivatePublic]}
];
