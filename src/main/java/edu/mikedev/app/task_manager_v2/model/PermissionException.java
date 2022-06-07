package edu.mikedev.app.task_manager_v2.model;

public class PermissionException extends Exception{
    
	private static final long serialVersionUID = 3468326642793355429L;

	public PermissionException(String s) {
        super(s);
    }

    public PermissionException(){
        super();
    }
}
