package com.spring.react.ppmtoll.repositories;

import com.spring.react.ppmtoll.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProjectTaskRepository extends CrudRepository<ProjectTask,Long> {

    List<ProjectTask> findByProjectIdentifierOrderByPriority(String id);

    ProjectTask findByProjectSequence(String projectSequence);
}
