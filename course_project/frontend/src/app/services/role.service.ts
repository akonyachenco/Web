import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Role} from '../models/role';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class RoleService {
  http = inject(HttpClient)
  url = `${environment.apiUrl}/role`

  create(role: Role): Observable<Role> {
    return this.http.post<Role>(this.url, role)
  }

  update(id: number, role: Role): Observable<Role> {
    return this.http.put<Role>(`${this.url}/${id}`, role)
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`)
  }

  findById(id: number): Observable<Role> {
    return this.http.get<Role>(`${this.url}/${id}`)
  }

  findAll(): Observable<Role[]> {
    return this.http.get<Role[]>(`${this.url}/all`)
  }

  findAllByEmployeeId(id: number): Observable<Role[]> {
    return this.http.get<Role[]>(`${this.url}/all/employee/${id}`)
  }
}
