package com.crappyengineering.micronaut.demo.repository;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.crappyengineering.micronaut.demo.model.Task;

public interface TaskRepository {

    Optional<Task> findById(@NotNull long id);

    Optional<List<Task>> findAll();

    Task save(@NotNull Task task);

    int deleteById(@NotNull long id);

    int update(@NotNull long id, @NotNull Task task);

}
