import { HttpClient } from '@angular/common/http';
import {inject, Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {Employee} from '../models/employee';
import {environment} from '../../environments/environment';


@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/employee`;


  update(employee: Employee): Observable<Employee> {
    return this.http.put<Employee>(this.baseUrl, employee);
  }

  updateRoles(id: number, roles: string[]): Observable<Employee> {
    return this.http.put<Employee>(`${this.baseUrl}/${id}/set-roles`, roles);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }


  findById(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${this.baseUrl}/${id}`);
  }


  findAll(): Observable<Employee[]> {
    return this.http.get<Employee[]>(`${this.baseUrl}/all`);
  }


  findByUsername(username: string): Observable<Employee> {
    return this.http.get<Employee>(`${this.baseUrl}`, {
      params: { username }
    });
  }


  findByFirstNameContaining(firstName: string): Observable<Employee[]> {
    return this.http.get<Employee[]>(`${this.baseUrl}/all/by-firstname`, {
      params: { firstName }
    });
  }

  findByLastNameContaining(lastName: string): Observable<Employee[]> {
    return this.http.get<Employee[]>(`${this.baseUrl}/all/by-lastname`, {
      params: { lastName }
    });
  }

  findByProjectId(projectId: number): Observable<Employee[]> {
    return this.http.get<Employee[]>(`${this.baseUrl}/all/project/${projectId}`);
  }

  findEmployeesNotInProject(projectId: number): Observable<Employee[]> {
    return this.http.get<Employee[]>(`${this.baseUrl}/all/not-in-project/${projectId}`);
  }
}
