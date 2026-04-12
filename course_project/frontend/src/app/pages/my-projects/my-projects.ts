import {Component, inject, OnInit} from '@angular/core';
import {ProjectService} from '../../services/project.service';
import {Project} from '../../models/project';
import {ProjectItem} from '../../components/project-item/project-item';
import {StatusService} from '../../services/status.service';

@Component({
  selector: 'app-my-projects',
  imports: [
    ProjectItem
  ],
  templateUrl: './my-projects.html',
  styleUrl: './my-projects.css',
})
export class MyProjectsComponent implements OnInit {
  projectService = inject(ProjectService)
  statusService = inject(StatusService)

  projects: Project[] = []

  ngOnInit(): void {
    this.projectService.findMyProjects().subscribe(
      projects =>
        this.projects = this.statusService.sortByStatusName(projects)
    )
  }
}
