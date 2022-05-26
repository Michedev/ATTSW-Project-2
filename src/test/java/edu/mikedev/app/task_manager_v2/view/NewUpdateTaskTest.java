package edu.mikedev.app.task_manager_v2.view;

import static org.junit.Assert.*;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.mikedev.app.task_manager_v2.data.Task;
import org.junit.runner.RunWith;

import javax.swing.*;

@RunWith(GUITestRunner.class)
public class NewUpdateTaskTest extends AssertJSwingJUnitTestCase {
	FrameFixture window;
	NewUpdateTask view;

	@Override
	protected void onSetUp() {
		Task task = new Task("TaskTitle", "subtask1", "subtask2", "subtask3");
		JFrame frame = GuiActionRunner.execute(() -> {
			view = new NewUpdateTask();
			JFrame jframe = new JFrame();
			 jframe.setContentPane(view);
			 return jframe;
		  });
		  window = new FrameFixture(robot(), frame);
		  window.show(); // shows the frame to test
	}

	@Test @GUITest
	public void testBaseView() {
		window.label("lblTaskTitle").requireText("Title");
		window.textBox("txtTaskTitle").requireEmpty();
		window.label(JLabelMatcher.withName("lblTitleErrorLabel")).requireNotVisible();
		window.label("lblStep1").requireText("Step 1");
		window.textBox("txtStep1").requireEmpty();
		window.label(JLabelMatcher.withName("lblStep1ErrorLabel")).requireNotVisible();
		window.label("lblStep2").requireText("Step 2");
		window.textBox("txtStep2").requireEmpty();
		window.label(JLabelMatcher.withName("lblStep2ErrorLabel")).requireNotVisible();
		window.label("lblStep3").requireText("Step 3");
		window.textBox("txtStep3").requireEmpty();
		window.label(JLabelMatcher.withName("lblStep3ErrorLabel")).requireNotVisible();

		window.button("btnMake").requireText("Add");
	}

	@Test @GUITest
	public void testUpdateMode() {
		Task task = new Task("ToBeUpdated", "S1", "S2", "S3");

		GuiActionRunner.execute(
				() -> {
					view.setUpdateMode(task);
				}
		);

		window.textBox("txtTaskTitle").requireText(task.getTitle());
		window.textBox("txtStep1").requireText(task.getSubtask1());
		window.textBox("txtStep2").requireText(task.getSubtask2());
		window.textBox("txtStep3").requireText(task.getSubtask3());
		
		window.button("btnMake").requireText("Update");
	}

}
