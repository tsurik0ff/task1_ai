package com.example.task.service;

import com.example.task.model.TodoItem;

import java.util.List;

public interface TodoItemService {
    List<TodoItem> getAllTodoItems();

    TodoItem createTodoItem(TodoItem todoItem);

    TodoItem getTodoItemById(Long id);

    TodoItem updateTodoItem(Long id, TodoItem updatedItem);

    void deleteTodoItem(Long id);
}
