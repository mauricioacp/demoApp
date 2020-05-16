package com.spring.react.ppmtoll.services;

import com.spring.react.ppmtoll.domain.Backlog;
import com.spring.react.ppmtoll.domain.ProjectTask;
import com.spring.react.ppmtoll.exceptions.ProjectNotFoundException;
import com.spring.react.ppmtoll.repositories.IBackLogRepository;
import com.spring.react.ppmtoll.repositories.IProjectRepository;
import com.spring.react.ppmtoll.repositories.IProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private IBackLogRepository backLogRepository;

    @Autowired
    private IProjectTaskRepository projectTaskRepository;

    @Autowired
    private IProjectRepository projectRepository;

     @Autowired
     private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask,String username) {

            //Exceptions project not found

            //All ProjectTask to be added to a specific project, project !=null Backlog exists
            Backlog backlog =  projectService.findProjectByIdentifier(projectIdentifier,username).getBacklog();
            //Set the Backlog to the projectTask
            projectTask.setBacklog(backlog);
            //we want our project sequence to be like this IDPROJECT-1 IDPROJECT-2 ... 100 101
            Integer BacklogSequence = backlog.getPTSequence();
            System.out.println(BacklogSequence);
            //Update the backlog sequence
            BacklogSequence++;

            backlog.setPTSequence(BacklogSequence);
            //Add sequence to the project task
            projectTask.setProjectSequence(projectIdentifier + "-" + BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);
            //Initial Status when status is null
            if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
                projectTask.setStatus("TO_DO");
            }
            //Initial Priority when priority is null
            if (projectTask.getPriority() == null || projectTask.getPriority()==0) {
                projectTask.setPriority(3);
            }


        return projectTaskRepository.save(projectTask);
    }

    public Iterable<ProjectTask> findBacklogById(String id,String username) {

       projectService.findProjectByIdentifier(id,username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id,String username){

        //Make sure we  are searching on an existing backlog
        projectService.findProjectByIdentifier(backlog_id,username);

        ProjectTask projectTask=projectTaskRepository.findByProjectSequence(pt_id);

        //make sure our task exists
        if(projectTask==null){
            throw  new ProjectNotFoundException("Project task "+pt_id+" does not exist");
        }

        //make sure that the backlog/project id in the path corresponds  to the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task "+pt_id+" does not exist in project "+backlog_id);
        }
        return projectTask;


    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id,String username){

        ProjectTask projectTask=findPTByProjectSequence(backlog_id,pt_id,username);
            projectTask=updatedTask;

          return projectTaskRepository.save(projectTask);
    }
    public void deletePTByProjectSequence(String backlog_id, String pt_id,String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id,username);
        projectTaskRepository.delete(projectTask);
    }
}
