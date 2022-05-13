package edu.mikedev.app.task_manager_v2.view;

import static org.junit.Assert.*;

import javax.swing.JFrame;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.mikedev.app.task_manager_v2.data.Task;

public class NewUpdateTaskTest {
	FrameFixture window;

	@Before
	public void setUp() throws Exception {
		Task task = new Task("TaskTitle", "subtask1", "subtask2", "subtask3");
		JFrame frame = GuiActionRunner.execute(() -> {
			 JFrame jframe = new JFrame();
			 jframe.setContentPane(new NewUpdateTask());
			 return jframe;
		  });
		  window = new FrameFixture(frame);
		  window.show(); // shows the frame to test
	}

	@Test
	public void testBaseView() {
		window.label("lblTaskTitle").requireText("Title");
		window.label("lblStep1").requireText("Step 1");
		window.textBox("txtStep1").requireEmpty();
		window.label("lblStep2").requireText("Step 2");
		window.textBox("txtStep2").requireEmpty();
		window.label("lblStep3").requireText("Step 3");
		window.textBox("txtStep3").requireEmpty();
		
		window.button("btnMake").requireText("Add");
	}
	
	
	  @After
	  public void tearDown() {
	    window.cleanUp();
	  }

}
