package com.crappyengineering.micronaut.demo.controller;

import static io.micronaut.http.HttpResponse.created;
import static io.micronaut.http.HttpResponse.ok;
import static io.micronaut.http.HttpStatus.NOT_FOUND;
import static io.micronaut.http.HttpStatus.OK;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.crappyengineering.micronaut.demo.model.Task;
import com.crappyengineering.micronaut.demo.repository.TaskRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.context.ServerRequestContext;
import io.micronaut.http.exceptions.UriSyntaxException;
import org.springframework.web.util.UriComponentsBuilder;

@Controller("/api/task")
public class TaskController {

    @Inject
    TaskRepository taskRepository;

    @Get
    public HttpResponse getAllTasks() {
        return taskRepository.findAll().map(HttpResponse::ok).orElseGet(HttpResponse::noContent);
    }

    @Post
    public HttpResponse createTask(@Body Task task) {
        return Optional.of(taskRepository.save(task))
                .map(task1 -> created(ServerRequestContext.currentRequest()
                        .map(HttpRequest::getUri)
                        .map(UriComponentsBuilder::fromUri)
                        .map(uriComponentsBuilder -> uriComponentsBuilder
                                .path("/{id}")
                                .buildAndExpand(task1.getId())
                                .toUri())
                        .orElseThrow(() -> new UriSyntaxException(new java.net.URISyntaxException("", "Could not obtain thread local request to build path"))))
                ).orElseGet(HttpResponse::badRequest);
    }

    @Get("/{id}")
    public Task getTaskById(long id) {
        return taskRepository.findById(id).orElse(null);
    }


    @Put("/{id}")
    public HttpStatus updateById(long id, @Body Task task) {
        return taskRepository.update(id, task) == 1 ? OK : NOT_FOUND;
    }

    @Delete("/{id}")
    public HttpStatus deleteById(long id){
        return taskRepository.deleteById(id) == 1 ? OK : NOT_FOUND;
    }
}