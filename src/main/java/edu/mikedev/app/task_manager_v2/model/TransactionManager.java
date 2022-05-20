package edu.mikedev.app.task_manager_v2.model;

import java.util.function.Function;

@FunctionalInterface
public interface TransactionManager {
    <T> T doInTransaction(Function<UserTaskRepository, T> f);
}
