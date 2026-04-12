import {Component, inject, OnInit} from '@angular/core';
import {ProjectService} from '../../services/project.service';
import {Project} from '../../models/project';
import {ProjectItem} from '../../components/project-item/project-item';
import {RouterLink} from '@angular/router';
import {StatusService} from '../../services/status.service';

@Component({
  selector: 'app-project-list',
  imports: [
    ProjectItem,
    RouterLink
  ],
  templateUrl: './project-list.html',
  styleUrl: './project-list.css',
})
export class ProjectListComponent implements OnInit {
  projectService = inject(ProjectService)
  statusService = inject(StatusService)

  projectList: Project[] = []
  errorMessage = ''

  ngOnInit(): void {
    this.projectService.findAll().subscribe((list) => this.projectList = this.statusService.sortByStatusName(list))
  }

  onDeleteProject(id: number) {
    this.projectService.delete(id).subscribe({
      next: () => {
        this.projectList = this.projectList.filter(project => project.id !== id)
      },
      error: (err) => {
        this.errorMessage = err.error || 'Ошибка удаления'
        setTimeout(() => this.errorMessage = '', 5000);
      }
    })
  }

}
