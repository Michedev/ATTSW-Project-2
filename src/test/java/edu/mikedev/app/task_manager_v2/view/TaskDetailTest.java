package edu.mikedev.app.task_manager_v2.view;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.mikedev.app.task_manager_v2.data.Task;
import org.junit.runner.RunWith;

@RunWith(GUITestRunner.class)
public class TaskDetailTest extends AssertJSwingJUnitTestCase {

	FrameFixture window;

	@Override
	protected void onSetUp() {
		Task task = new Task("TaskTitle", "subtask1", "subtask2", "subtask3");
		JFrame frame = GuiActionRunner.execute(() -> {
			 JFrame jframe = new JFrame();
			 jframe.setContentPane(new TaskDetail(task));
			 return jframe;
		  });
		  window = new FrameFixture(robot(), frame);
		  window.show(); // shows the frame to test
	}

	@Test @GUITest
	public void testBaseView() {
		window.label("lblTaskTitle").requireText("TaskTitle");
		window.label("lblSubtask1").requireText("subtask1");
		window.label("lblSubtask2").requireText("subtask2");
		window.label("lblSubtask3").requireText("subtask3");
		
		window.button("btnUpdate").requireText("Update");
		window.button("btnDelete").requireText("Delete");
	}
}
