import {Component, inject, OnInit} from '@angular/core';
import {SprintService} from '../../services/sprint.service';
import {Sprint} from '../../models/sprint';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {SprintItem} from '../../components/sprint-item/sprint-item';
import {AuthService} from '../../auth/auth.service';
import {BackButton} from '../../components/back-button/back-button';
import {StatusService} from '../../services/status.service';

@Component({
  selector: 'app-sprint-list',
  imports: [
    RouterLink,
    SprintItem,
    BackButton
  ],
  templateUrl: './sprint-list.html',
  styleUrl: './sprint-list.css',
})
export class SprintListComponent implements OnInit {
  sprintService = inject(SprintService)
  activatedRoute = inject(ActivatedRoute)
  authService = inject(AuthService)
  statusService = inject(StatusService)

  sprintList: Sprint[] = []
  errorMessage = ''

  ngOnInit(): void {
    const projectId = this.activatedRoute.snapshot.params['projectId']
    if (projectId)
      this.sprintService.findAllByProjectId(projectId).subscribe(list => this.sprintList = this.statusService.sortByStatusName(list))
  }

  onDeleteSprint(id: number) {
    this.sprintService.delete(id).subscribe({
      next: () => {
        this.sprintList = this.sprintList.filter(sprint => sprint.id !== id)
      },
      error: (err) => {
        this.errorMessage = err.error || 'Ошибка удаления'
        setTimeout(() => this.errorMessage = '', 5000);
      }
    })
  }

}
