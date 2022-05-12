package edu.mikedev.app.task_manager_v2.view;

import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

public class LoginPageTest {

	FrameFixture window;

	@Before
	public void setUp() throws Exception {
		  JFrame frame = GuiActionRunner.execute(() -> {
			 JFrame jframe = new JFrame();
			 jframe.setContentPane(new LoginPage());
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
		window.label(JLabelMatcher.withName("lblLoginError")).requireNotVisible();
		window.button("btnLogin").requireText("Login");
		window.button("btnRegister").requireText("Register");
	}

	  @After
	  public void tearDown() {
	    window.cleanUp();
	  }
}
