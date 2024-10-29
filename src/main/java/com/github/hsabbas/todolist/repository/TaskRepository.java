package com.github.hsabbas.todolist.repository;

import com.github.hsabbas.todolist.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long>{
    List<Task> findAllByUserId(long userId);
}
