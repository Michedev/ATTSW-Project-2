package edu.mikedev.app.task_manager_v2.view;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.swing.*;

@RunWith(GUITestRunner.class)
public class LoginPageTest extends AssertJSwingJUnitTestCase {

	FrameFixture window;
	private JFrame frame;

	@Override
	protected void onSetUp() {
		frame = GuiActionRunner.execute(() -> {
		   JFrame jframe = new JFrame();
		   jframe.setContentPane(new LoginPage());
		   return jframe;
		});
		  window = new FrameFixture(robot(), frame);
		  window.show(); // shows the frame to test
	}

	@Test @GUITest
	public void testBaseView() {
		window.label("lblUsername").requireText("Username");
		window.textBox("txtUsername").requireEmpty();
		window.label("lblPassword").requireText("Password");
		window.textBox("txtPassword").requireEmpty();
		window.label(JLabelMatcher.withName("lblLoginError")).requireNotVisible();
		window.button("btnLogin").requireText("Login");
		window.button("btnRegister").requireText("Register");
	}

	@Test @GUITest
	public void testSetLabelError(){
		LoginPage contentPane = (LoginPage) frame.getContentPane();
		GuiActionRunner.execute(() -> {
			contentPane.setErrorLabelText("Test Label");
		});

		window.label(JLabelMatcher.withName("lblLoginError")).requireVisible().requireText("Test Label");
	}
}
