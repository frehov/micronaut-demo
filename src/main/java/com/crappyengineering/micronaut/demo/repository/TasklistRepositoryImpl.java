package com.crappyengineering.micronaut.demo.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import com.crappyengineering.micronaut.demo.model.Tasklist;
import io.micronaut.spring.tx.annotation.Transactional;

@Singleton
public class TasklistRepositoryImpl implements TasklistRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Optional<Tasklist> findById(@NotNull long id) {
        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.fetchgraph", entityManager.getEntityGraph("graph.Tasklist.tasks"));

        return Optional.ofNullable(entityManager.find(Tasklist.class, id, hints));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<List<Tasklist>> findAll() {
        return Optional.ofNullable(entityManager.createQuery("SELECT t from Tasklist t", Tasklist.class)
                .setHint("javax.persistence.fetchgraph", entityManager.getEntityGraph("graph.Tasklist.tasks"))
                .getResultList());
    }

    @Override
    @Transactional
    public Tasklist save(@NotNull Tasklist tasklist) {
        entityManager.persist(tasklist);
        return tasklist;
    }

    @Override
    @Transactional
    public int deleteById(@NotNull long id) {
        return entityManager.createQuery("DELETE FROM Tasklist WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate();

        //findById(id).ifPresent(entityManager::remove);
    }

    @Override
    @Transactional
    public int update(@NotNull long id, @NotNull Tasklist tasklist) {
        return entityManager.createQuery("UPDATE Tasklist SET title = :title, owner = :owner where id = :id")
                .setParameter("title", tasklist.getTitle())
                .setParameter("owner", tasklist.getOwner())
                .setParameter("id", id)
                .executeUpdate();
    }
}
