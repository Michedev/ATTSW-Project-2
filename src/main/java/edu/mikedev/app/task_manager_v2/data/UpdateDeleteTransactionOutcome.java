package edu.mikedev.app.task_manager_v2.data;

public class UpdateDeleteTransactionOutcome<T> {
    private T data;
    private int missingId;

    public UpdateDeleteTransactionOutcome(T data, int missingId) {
        this.data = data;
        this.missingId = missingId;
    }

    public T getData() {
        return data;
    }

    public int getMissingId() {
        return missingId;
    }
}
