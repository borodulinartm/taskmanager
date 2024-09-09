package org.example.task_manager.service;

import org.example.task_manager.models.body.Authorization;

public interface KeyCloakService {
    // Создаёт пользователя. Если были ошибки, то высрет ошибку
    void createUser(Authorization authorization);
}
