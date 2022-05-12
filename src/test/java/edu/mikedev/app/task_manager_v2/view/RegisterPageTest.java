package edu.mikedev.app.task_manager_v2.view;

import static org.junit.Assert.*;

import javax.swing.JFrame;

import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RegisterPageTest {

	FrameFixture window;

	@Before
	public void setUp() throws Exception {
		  JFrame frame = GuiActionRunner.execute(() -> {
			 JFrame jframe = new JFrame();
			 jframe.setContentPane(new RegisterPage());
			 return jframe;
		  });
		  window = new FrameFixture(frame);
		  window.show(); // shows the frame to test
	}

	@Test
	public void testBaseView() {
		window.label("lblUsername").requireText("Username");
		window.textBox("txtUsername").requireEmpty();
		window.label("lblPassword").requireText("Password");
		window.textBox("txtPassword").requireEmpty();
		window.label("lblEmail").requireText("E-mail");
		window.textBox("txtEmail").requireEmpty();
		window.label(JLabelMatcher.withName("lblUsernameError")).requireNotVisible();
		window.label(JLabelMatcher.withName("lblPasswordError")).requireNotVisible();
		window.label(JLabelMatcher.withName("lblEmailError")).requireNotVisible();
		window.button("btnConfirm").requireText("Confirm");
	}
	
	  @After
	  public void tearDown() {
	    window.cleanUp();
	  }
}
