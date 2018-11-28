package com.crappyengineering.micronaut.demo.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import com.crappyengineering.micronaut.demo.model.Task;
import io.micronaut.spring.tx.annotation.Transactional;

public class TaskRepositoryImpl implements TaskRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Optional<Task> findById(@NotNull long id) {
        return Optional.ofNullable(entityManager.find(Task.class, id));
    }

    @Override
    @Transactional
    public Optional<List<Task>> findAll() {
        return Optional.ofNullable(entityManager.createQuery("SELECT t from Task t", Task.class).getResultList());
    }

    @Override
    @Transactional
    public Task save(@NotNull Task task) {
        entityManager.persist(task);
        return task;
    }

    @Override
    @Transactional
    public int deleteById(@NotNull long id) {
        return entityManager.createQuery("DELETE FROM Task WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate();

        //findById(id).ifPresent(entityManager::remove);
    }

    @Override
    @Transactional
    public int update(@NotNull long id, @NotNull Task task) {
        return entityManager.createQuery("UPDATE Task SET description = :description, parent = :parent, status = :status WHERE id = :id")
                .setParameter("description", task.getDescription())
                .setParameter("parent", task.getParent())
                .setParameter("status", task.getStatus())
                .setParameter("id", id)
                .executeUpdate();
    }
}
