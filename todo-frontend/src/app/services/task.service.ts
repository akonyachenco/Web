import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../environment';
import {Observable} from 'rxjs';
import {Task} from '../dto/task';

@Injectable({
  providedIn: 'root',
})
export class TaskService {

  http = inject(HttpClient)

  private apiUrl = `${environment.baseURL}/tasks`

  public getTasks(from?: Date, to?: Date): Observable<Task[]> {
    let params = new HttpParams()

    if (from)
      params.set("from", from.toISOString())

    if (to)
      params.set("to", to.toISOString())

    return this.http.get<Task[]>(this.apiUrl, {params})
  }

  public getTask(id: number): Observable<Task> {
    return this.http.get<Task>(this.apiUrl + "/" + id)
  }

  public createTask(task: Task): Observable<Task> {
    return this.http.post<Task>(this.apiUrl, task)
  }

  public updateTask(id: number, task: Task): Observable<void> {
    return this.http.put<void>(this.apiUrl + "/" + id, task)
  }

  public deleteTask(id: number): Observable<void> {
    return this.http.delete<void>(this.apiUrl + "/" + id)
  }

  public getActiveTaskCount(userId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/active/count?${userId}`)
  }
}
