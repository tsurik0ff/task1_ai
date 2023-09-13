package com.example.task.controller;

import com.example.task.model.TodoItem;
import com.example.task.service.TodoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
public class TodoItemController {
    @Autowired
    private TodoItemService todoItemService;

    @GetMapping
    public List<TodoItem> getAllTodoItems() {
        return todoItemService.getAllTodoItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoItem> getTodoItemById(@PathVariable Long id) {
        TodoItem todoItem = todoItemService.getTodoItemById(id);
        if (todoItem != null) {
            return ResponseEntity.ok(todoItem);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TodoItem> createTodoItem(@RequestBody TodoItem todoItem) {
        TodoItem createdItem = todoItemService.createTodoItem(todoItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @PostMapping("/{id}")
    public ResponseEntity<TodoItem> updateTodoItem(@PathVariable Long id, @RequestBody TodoItem updatedItem) {
        TodoItem updatedTodoItem = todoItemService.updateTodoItem(id, updatedItem);
        if (updatedTodoItem != null) {
            return ResponseEntity.ok(updatedItem);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoItem(@PathVariable Long id) {
        todoItemService.deleteTodoItem(id);
        return ResponseEntity.noContent().build();
    }
}
