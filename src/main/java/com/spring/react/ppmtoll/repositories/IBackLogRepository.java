package com.spring.react.ppmtoll.repositories;

import com.spring.react.ppmtoll.domain.Backlog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBackLogRepository extends CrudRepository<Backlog,Long> {

    Backlog findByProjectIdentifier(String projectIdentifier);
}
