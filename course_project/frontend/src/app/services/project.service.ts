import {inject, Injectable} from '@angular/core';
import {Project} from '../models/project';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ProjectService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/project`;
  private myProjectsUrl = `${environment.apiUrl}/my-projects`;


  create(project: Project): Observable<Project> {
        return this.http.post<Project>(this.baseUrl, project);
  }

  update(project: Project): Observable<Project> {
    return this.http.put<Project>(this.baseUrl, project);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  findById(id: number): Observable<Project> {
    return this.http.get<Project>(`${this.baseUrl}/${id}`);
  }
  findAll(): Observable<Project[]> {
    return this.http.get<Project[]>(`${this.baseUrl}/all`);
  }

  findAllByStatus(status: string): Observable<Project[]> {
    let params = new HttpParams().set('status', status);
    return this.http.get<Project[]>(`${this.baseUrl}/all/by-status`, { params });
  }

  findAllByStartDateAfter(afterDate: string): Observable<Project[]> {
    let params = new HttpParams().set('after', afterDate);
    return this.http.get<Project[]>(`${this.baseUrl}/all/by-start-date`, { params });
  }

  findAllByEmployeeId(employeeId: number): Observable<Project[]> {
    return this.http.get<Project[]>(`${this.baseUrl}/all/${employeeId}`);
  }

  findMyProjects(status?: string): Observable<Project[]> {
    let params = new HttpParams();

    if (status) {
      params = params.set('status', status);
    }
    return this.http.get<Project[]>(this.myProjectsUrl, { params });
  }


  findBySprintId(id: number): Observable<Project> {
    return this.http.get<Project>(`${this.myProjectsUrl}/by-sprint-id/${id}`)
  }

  addToTeam(projectId: number, employeeIds: number[]): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${projectId}/add-to-team`, employeeIds);
  }

  removeFromTeam(projectId: number, employeeId: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${projectId}/delete-from-team/${employeeId}`, null);
  }


}
