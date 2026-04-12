import {inject, Injectable } from '@angular/core';
import {Sprint} from '../models/sprint';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class SprintService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/sprint`;


  create(sprint: Sprint): Observable<Sprint> {
    return this.http.post<Sprint>(this.baseUrl, sprint);
  }


  update(sprint: Sprint): Observable<Sprint> {
    return this.http.put<Sprint>(this.baseUrl, sprint);
  }


  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }


  findById(id: number): Observable<Sprint> {
    return this.http.get<Sprint>(`${this.baseUrl}/${id}`);
  }


  findAll(): Observable<Sprint[]> {
    return this.http.get<Sprint[]>(`${this.baseUrl}/all`);
  }

  findAllByProjectId(projectId: number): Observable<Sprint[]> {
    return this.http.get<Sprint[]>(`${this.baseUrl}/all/project/${projectId}`);
  }
}
