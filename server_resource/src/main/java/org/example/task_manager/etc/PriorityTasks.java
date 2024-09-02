package org.example.task_manager.etc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PriorityTasks {
    LOW, MEDIUM, HIGH;

    @JsonCreator
    public static PriorityTasks fromValue(String value) {
        return PriorityTasks.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }
}