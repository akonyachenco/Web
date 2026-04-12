import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Status} from '../models/status';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class StatusService {
  http = inject(HttpClient)
  url = `${environment.apiUrl}/status`

  private readonly STATUS_ORDER = [
    'В процессе',
    'Запланировано',
    'Завершено',
    'Отменено'
  ]

  create(status: Status): Observable<Status> {
    return this.http.post<Status>(this.url, status)
  }

  update(id: number, name: string): Observable<Status> {
    return this.http.put<Status>(`${this.url}/${id}`, name)
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`)
  }

  findAll(): Observable<Status[]> {
    return this.http.get<Status[]>(`${this.url}/all`)
  }

  sortByStatusName<T extends Record<'statusName', string>>(list: T[]): T[] {
    return [...list].sort((a,b) => {
      const indexA = this.STATUS_ORDER.indexOf(a['statusName'])
      const indexB = this.STATUS_ORDER.indexOf(b['statusName'])

      if (indexA === -1 && indexB === -1)
        return 0
      else if (indexA === -1)
        return 1
      else if (indexB === -1)
        return -1

      return indexA - indexB
    })
  }
}
