package com.crappyengineering.micronaut.demo.controller;

import static io.micronaut.http.HttpResponse.created;
import static io.micronaut.http.HttpStatus.NOT_FOUND;
import static io.micronaut.http.HttpStatus.OK;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import com.crappyengineering.micronaut.demo.model.Tasklist;
import com.crappyengineering.micronaut.demo.repository.TasklistRepository;
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

@Controller("/api/tasklist")
public class TasklistController {

    @Inject
    TasklistRepository tasklistRepository;

    @Get
    public HttpResponse getAllTasklists() {
        return tasklistRepository.findAll().map(HttpResponse::ok).orElseGet(HttpResponse::noContent);
    }

    @Post
    public HttpResponse createTasklist(@Body Tasklist tasklist) {
        return Optional.of(tasklistRepository.save(tasklist))
                .map(tasklist1 -> created(ServerRequestContext.currentRequest()
                        .map(HttpRequest::getUri)
                        .map(UriComponentsBuilder::fromUri)
                        .map(uriComponentsBuilder -> uriComponentsBuilder
                                .path("/{id}")
                                .buildAndExpand(tasklist1.getId())
                                .toUri())
                        .orElseThrow(() -> new UriSyntaxException(new java.net.URISyntaxException("", "Could not obtain thread local request to build path"))))
                ).orElseGet(HttpResponse::badRequest);
    }

    @Get("/{id}")
    public Tasklist getTasklistById(long id) {
        return tasklistRepository.findById(id).orElse(null);
    }

    @Put("/{id}")
    public HttpStatus updateTaskListById(long id, @Body Tasklist tasklist) {
        return tasklistRepository.update(id, tasklist) == 1 ? OK : NOT_FOUND;
    }

    @Delete("/{id}")
    public HttpStatus deleteTasklistById(long id) {
        return tasklistRepository.deleteById(id) == 1 ? OK : NOT_FOUND;
    }
}