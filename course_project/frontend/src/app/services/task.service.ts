import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {PriorityEnum} from '../enums/priority.enum';
import { Task } from '../models/task';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/task`;
  private myTasksUrl = `${environment.apiUrl}/my-tasks`;


  create(task: Task): Observable<Task> {
    return this.http.post<Task>(this.baseUrl, task);
  }


  update(task: Task): Observable<Task> {
    return this.http.put<Task>(this.baseUrl, task);
  }


  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }


  findById(id: number): Observable<Task> {
    return this.http.get<Task>(`${this.baseUrl}/${id}`);
  }


  findAll(
    filters?: {
      priority?: PriorityEnum;
      start?: Date;
      end?: Date;
      status?: string;
    }
  ): Observable<Task[]> {
    let params = new HttpParams();

    if (filters?.priority) {
      params = params.set('priority', filters.priority);
    }

    if (filters?.start) {
      params = params.set('start', this.formatDateTime(filters.start));
    }

    if (filters?.end) {
      params = params.set('end', this.formatDateTime(filters.end));
    }

    if (filters?.status) {
      params = params.set('status', filters.status);
    }

    return this.http.get<Task[]>(`${this.baseUrl}/all`, { params });
  }


  findAllByEmployeeId(employeeId: number, status?: string): Observable<Task[]> {
    let params = new HttpParams();

    if (status) {
      params = params.set('status', status);
    }

    return this.http.get<Task[]>(`${this.baseUrl}/all/employee/${employeeId}`, { params });
  }


  findAllBySprintId(sprintId: number, status?: string): Observable<Task[]> {
    let params = new HttpParams();

    if (status) {
      params = params.set('status', status);
    }

    return this.http.get<Task[]>(`${this.baseUrl}/all/sprint/${sprintId}`, { params });
  }


  findAllByEmployeeIdAndSprintId(employeeId: number, sprintId: number): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.baseUrl}/all/employee/${employeeId}/sprint/${sprintId}`);
  }


  findAllByEmployeeIdAndProjectId(employeeId: number, projectId: number): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.baseUrl}/all/employee/${employeeId}/project/${projectId}`);
  }


  findMyTasks(status?: string): Observable<Task[]> {
    let params = new HttpParams();

    if (status) {
      params = params.set('status', status);
    }

    return this.http.get<Task[]>(this.myTasksUrl, { params });
  }


  findMyTasksBySprintId(sprintId: number): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.myTasksUrl}/sprint/${sprintId}`);
  }


  findMyTasksByProjectId(projectId: number): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.myTasksUrl}/project/${projectId}`);
  }

  changeStatus(id: number, statusName: string): Observable<void> {
    return this.http.put<void>(`${this.myTasksUrl}/${id}/change-status`, statusName)
  }


  private formatDateTime(date: Date): string {
    return date.toISOString();
  }
}
