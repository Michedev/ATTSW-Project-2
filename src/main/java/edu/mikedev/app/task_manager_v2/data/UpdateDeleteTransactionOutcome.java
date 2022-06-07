package edu.mikedev.app.task_manager_v2.data;

public class UpdateDeleteTransactionOutcome<T> {
    private T data;
    private int missingTaskId;

    public UpdateDeleteTransactionOutcome(T data, int missingTaskId) {
        this.data = data;
        this.missingTaskId = missingTaskId;
    }

    public T getData() {
        return data;
    }

    public int getMissingId() {
        return missingTaskId;
    }
}
