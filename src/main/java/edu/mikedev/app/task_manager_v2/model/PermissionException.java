package edu.mikedev.app.task_manager_v2.model;

public class PermissionException extends Exception{
    public PermissionException(String s) {
        super(s);
    }

    public PermissionException(){
        super();
    }
}
