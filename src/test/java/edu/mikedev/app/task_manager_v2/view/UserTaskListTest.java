package edu.mikedev.app.task_manager_v2.view;

import edu.mikedev.app.task_manager_v2.data.Task;
import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

@RunWith(GUITestRunner.class)
public class UserTaskListTest extends AssertJSwingJUnitTestCase {

	FrameFixture window;

	@Override
	protected void onSetUp() {
		List<Task> tasks = Arrays.asList(new Task("Title1", "Subtask1", "Subtask2", "Subtask3" ), new Task("Title2", "Subtask4", "Subtask5", "Subtask6" ), new Task("Title3", "Subtask7", "Subtask8", "Subtask9" ) );
		  JFrame frame = GuiActionRunner.execute(() -> {
			 JFrame jframe = new JFrame();
			 jframe.setContentPane(new UserTasksList(tasks));
			 return jframe;
		  });
		  window = new FrameFixture(robot(), frame);
		  window.show(); // shows the frame to test
	}

	@Test @GUITest
	public void testBaseView() {
		window.label("lblTitleTask1").requireText("Title1");
		window.button("btnDetailTask1").requireText("Detail");
		window.label("lblTitleTask2").requireText("Title2");
		window.button("btnDetailTask2").requireText("Detail");
		window.label("lblTitleTask3").requireText("Title3");
		window.button("btnDetailTask3").requireText("Detail");
		window.label(JLabelMatcher.withName("lblError")).requireNotVisible();

		window.button("btnNew").requireText("New");
		window.button("btnDeleteUser").requireText("Delete");
	}
}
