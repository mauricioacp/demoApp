package com.spring.react.ppmtoll.services;

import com.spring.react.ppmtoll.domain.Backlog;
import com.spring.react.ppmtoll.domain.Project;
import com.spring.react.ppmtoll.domain.User;
import com.spring.react.ppmtoll.exceptions.ProjectIdException;
import com.spring.react.ppmtoll.exceptions.ProjectNotFoundException;
import com.spring.react.ppmtoll.repositories.IBackLogRepository;
import com.spring.react.ppmtoll.repositories.IProjectRepository;
import com.spring.react.ppmtoll.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

	@Autowired
	private IProjectRepository projectRepositories;

	@Autowired
	private IBackLogRepository backLogRepository;

	@Autowired
	private IUserRepository userRepository;

	public Project saveOrUpdateProject(Project project, String username) {

		//project.getId!=null
		//find by db id -> null
		if (project.getId() != null) {
			Project existingProject = projectRepositories.findByProjectIdentifier(project.getProjectIdentifier());

			if (existingProject != null && (!existingProject.getProjectLeader().equals(username))) {
				throw new ProjectNotFoundException("Project not found in your account");
			} else if (existingProject == null) {
				throw new ProjectNotFoundException("Project with ID: " + project.getProjectIdentifier() + "cannot be updated because it doesn't exist");
			}
		}
		try {

			User user = userRepository.findByUsername(username);
			project.setUser(user);
			project.setProjectLeader(user.getUsername());
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

			if (project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			}

			if (project.getId() != null) {
				project.setBacklog(backLogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
			}
			return projectRepositories.save(project);
		} catch (Exception e) {
			throw new ProjectIdException("ProjectId " + project.getProjectIdentifier().toUpperCase() + " Already Exists");
		}

	}

	public Project findProjectByIdentifier(String projectId, String username) {

		//Only want to return the project if the user looking for it is the owner

		Project project = projectRepositories.findByProjectIdentifier(projectId.toUpperCase());

		if (project == null) {
			throw new ProjectIdException("ProjectId " + projectId + " Does not Exists");
		}

		if (!project.getProjectLeader().equals(username)) {
			throw new ProjectNotFoundException("Project not found in your account");
		}

		return project;
	}


	public Iterable<Project> findAllProjects(String username) {
		return projectRepositories.findAllByProjectLeader(username);
	}

	public void deleteProjectByIdentifier(String projectId, String username) {

		projectRepositories.delete(findProjectByIdentifier(projectId, username));
	}


}
