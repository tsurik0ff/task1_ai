package com.example.task.controller;

import com.example.task.controller.TodoItemController;
import com.example.task.model.TodoItem;
import com.example.task.service.TodoItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TodoItemControllerTest {

    @InjectMocks
    private TodoItemController todoItemController;

    @Mock
    private TodoItemService todoItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTodoItems() {
        // Mock data
        TodoItem item1 = createTodoItem(1L, "Task 1", "first");
        TodoItem item2 = createTodoItem(2L, "Task 2", "second");
        List<TodoItem> todoItemList = Arrays.asList(item1, item2);

        when(todoItemService.getAllTodoItems()).thenReturn(todoItemList);

        // Call the controller method
        List<TodoItem> result = todoItemController.getAllTodoItems();

        // Verify the result
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("Task 2", result.get(1).getTitle());
    }

    @Test
    void testGetTodoItemById() {
        // Mock data
        Long itemId = 1L;
        TodoItem item = createTodoItem(itemId, "Task 1", "first");

        when(todoItemService.getTodoItemById(itemId)).thenReturn(item);

        // Call the controller method
        ResponseEntity<TodoItem> response = todoItemController.getTodoItemById(itemId);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Task 1", response.getBody().getTitle());
    }

    @Test
    void testCreateTodoItem() {
        Long itemId = 1L;
        TodoItem item = createTodoItem(itemId, "Task 1", "first");

        when(todoItemService.createTodoItem(item)).thenReturn(item);

        ResponseEntity<TodoItem> response = todoItemController.createTodoItem(item);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Task 1", response.getBody().getTitle());
    }

    @Test
    void testGetTodoItemById_NotFound() {
        Long itemId = 1L;
        when(todoItemService.getTodoItemById(itemId)).thenReturn(null);

        ResponseEntity<TodoItem> response = todoItemController.getTodoItemById(itemId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private TodoItem createTodoItem(Long id, String title, String description) {
        TodoItem todoItem = new TodoItem();
        todoItem.setId(id);
        todoItem.setTitle(title);
        todoItem.setDescription(description);
        return todoItem;
    }

    @Test
    void testUpdateTodoItem() {
        // Mock data
        Long itemId = 1L;
        TodoItem updatedItem = createTodoItem(itemId, "Updated Task", "first");

        when(todoItemService.updateTodoItem(itemId, updatedItem)).thenReturn(updatedItem);

        // Call the controller method
        ResponseEntity<TodoItem> response = todoItemController.updateTodoItem(itemId, updatedItem);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Task", response.getBody().getTitle());
    }

    @Test
    void testUpdateTodoItem_NotFound() {
        Long itemId = 1L;
        TodoItem updatedItem = createTodoItem(itemId, "Updated Task", "first");

        when(todoItemService.updateTodoItem(itemId, updatedItem)).thenReturn(null);

        ResponseEntity<TodoItem> response = todoItemController.updateTodoItem(itemId, updatedItem);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteTodoItem() {
        Long itemId = 1L;

        ResponseEntity<Void> response = todoItemController.deleteTodoItem(itemId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(todoItemService, times(1)).deleteTodoItem(itemId);
    }

    // You can write similar tests for the other controller methods (create, update, delete).
}
