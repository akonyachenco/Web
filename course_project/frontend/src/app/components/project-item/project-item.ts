import {Component, EventEmitter, inject, Input, OnInit, Output, signal} from '@angular/core';
import {Project} from '../../models/project';
import {AuthService} from '../../auth/auth.service';
import {Router, RouterLink} from '@angular/router';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-project-item',
  imports: [
    RouterLink,
    DatePipe
  ],
  templateUrl: './project-item.html',
  styleUrl: './project-item.css',
})
export class ProjectItem implements OnInit {
  @Input() project!: Project
  @Output() deleteProject = new EventEmitter<number>()

  authService = inject(AuthService)
  router = inject(Router)


  showAdminPanel = signal<boolean>(true)
  private readonly IGNORE_ADMIN_PATHS = [
    '/',
    '/my-projects'
  ]

  ngOnInit(): void {
    this.showAdminPanel.set(!this.IGNORE_ADMIN_PATHS.some(path => this.router.url === path))
  }

  onDelete() {
    this.deleteProject.emit(this.project.id)
  }

}
