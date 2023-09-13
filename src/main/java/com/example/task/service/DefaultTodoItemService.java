package com.example.task.service;

import com.example.task.model.TodoItem;
import com.example.task.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultTodoItemService implements TodoItemService {
    @Autowired
    private TodoItemRepository todoItemRepository;

    @Override
    public List<TodoItem> getAllTodoItems() {
        return todoItemRepository.findAll();
    }

    @Override
    public TodoItem createTodoItem(TodoItem todoItem) {
        return todoItemRepository.save(todoItem);
    }

    @Override
    public TodoItem getTodoItemById(Long id) {
        return todoItemRepository.findById(id).orElse(null);
    }

    @Override
    public TodoItem updateTodoItem(Long id, TodoItem updatedItem) {
        TodoItem existingItem = todoItemRepository.findById(id).orElse(null);
        if (existingItem != null) {
            existingItem.setTitle(updatedItem.getTitle());
            existingItem.setDescription(updatedItem.getDescription());
            return todoItemRepository.save(existingItem);
        }
        return null;
    }

    @Override
    public void deleteTodoItem(Long id) {
        todoItemRepository.deleteById(id);
    }
}
