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
public class RegisterPageTest extends AssertJSwingJUnitTestCase {

	FrameFixture window;

	@Override
	protected void onSetUp() {
		  JFrame frame = GuiActionRunner.execute(() -> {
			 JFrame jframe = new JFrame();
			 jframe.setContentPane(new RegisterPage());
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
		window.label("lblEmail").requireText("E-mail");
		window.textBox("txtEmail").requireEmpty();
		window.label(JLabelMatcher.withName("lblUsernameError")).requireNotVisible();
		window.label(JLabelMatcher.withName("lblPasswordError")).requireNotVisible();
		window.label(JLabelMatcher.withName("lblEmailError")).requireNotVisible();
		window.button("btnConfirm").requireText("Confirm");
	}
}
