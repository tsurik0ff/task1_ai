package com.example.task.controller;

import com.example.task.model.TodoItem;
import com.example.task.repository.TodoItemRepository;
import com.example.task.service.TodoItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TodoItemController.class)
public class TodoItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoItemService todoItemService;

    @MockBean
    private TodoItemRepository todoItemRepository;

    @Test
    public void getAllTodoItems_ReturnsListOfTodoItems() throws Exception {
        // Arrange
        List<TodoItem> mockTodoItems = Arrays.asList(
                createTodoItem(1L, "Task 1", "Description 1"),
                createTodoItem(2L, "Task 2", "Description 2")
        );
        when(todoItemService.getAllTodoItems()).thenReturn(mockTodoItems);

        // Act & Assert
        mockMvc.perform(get("/api/todo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Task 1")))
                .andExpect(jsonPath("$[0].description", is("Description 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Task 2")))
                .andExpect(jsonPath("$[1].description", is("Description 2")));
    }

    @Test
    public void getTodoItemById_ExistingId_ReturnsTodoItem() throws Exception {
        // Arrange
        Long itemId = 1L;
        TodoItem mockTodoItem = createTodoItem(itemId, "Task 1", "Description 1");
        when(todoItemService.getTodoItemById(itemId)).thenReturn(mockTodoItem);

        // Act & Assert
        mockMvc.perform(get("/api/todo/{id}", itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Task 1")))
                .andExpect(jsonPath("$.description", is("Description 1")));
    }

    @Test
    public void getTodoItemById_NonExistingId_ReturnsNotFound() throws Exception {
        // Arrange
        Long nonExistingItemId = 999L;
        when(todoItemService.getTodoItemById(nonExistingItemId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/todo/{id}", nonExistingItemId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createTodoItem_ValidItem_ReturnsCreatedItem() throws Exception {
        // Arrange
        TodoItem newItem = createTodoItem(null, "New Task", "New Description");
        TodoItem createdItem = createTodoItem(1L, "New Task", "New Description");
        when(todoItemService.createTodoItem(newItem)).thenReturn(createdItem);

        // Act & Assert
        mockMvc.perform(post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newItem)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("New Task")))
                .andExpect(jsonPath("$.description", is("New Description")));
    }

    @Test
    public void updateTodoItem_ExistingId_ValidItem_ReturnsUpdatedItem() throws Exception {
        // Arrange
        Long itemId = 1L;
        TodoItem updatedItem = createTodoItem(itemId, "Updated Task", "Updated Description");
        TodoItem existingItem = createTodoItem(itemId, "Task 1", "Description 1");
        when(todoItemService.updateTodoItem(itemId, updatedItem)).thenReturn(updatedItem);

        // Act & Assert
        mockMvc.perform(post("/api/todo/{id}", itemId, updatedItem)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Updated Task")))
                .andExpect(jsonPath("$.description", is("Updated Description")));
    }

    @Test
    public void updateTodoItem_NonExistingId_ReturnsNotFound() throws Exception {
        // Arrange
        Long nonExistingItemId = 999L;
        TodoItem updatedItem = createTodoItem(nonExistingItemId, "Updated Task", "Updated Description");
        when(todoItemService.updateTodoItem(nonExistingItemId, updatedItem)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/api/todo/{id}", nonExistingItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedItem)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteTodoItem_ExistingId_ReturnsNoContent() throws Exception {
        // Arrange
        Long itemId = 1L;

        // Act & Assert
        mockMvc.perform(delete("/api/todo/{id}", itemId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteTodoItem_NonExistingId_ReturnsNotFound() throws Exception {
        // Arrange
        Long nonExistingItemId = 999L;

        // Act & Assert
        mockMvc.perform(delete("/api/todo/{id}", nonExistingItemId))
                .andExpect(status().isNotFound());
    }

    private TodoItem createTodoItem(Long id, String title, String description) {
        TodoItem todoItem = new TodoItem();
        todoItem.setId(id);
        todoItem.setTitle(title);
        todoItem.setDescription(description);
        return todoItem;
    }

    // Helper method to convert object to JSON
    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
