/*
 * CActionUtils.java
 * Created on Apr 8, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import clib.common.system.CJavaSystem;
import clib.common.thread.ICTask;

/**
 * @author macchan
 * 
 */
public class CActionUtils {

	public static CAction createAction(String name, final ICTask task) {
		CAction action = new CAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				task.doTask();
			}
		};
		action.putValue(Action.NAME, name);
		return action;
	}

	/*
	 * @deprecated use CJavaInfoPanels#createJavaInformationAction()
	 */
	@Deprecated
	public static CAction createJavaInformationAction() {
		return createAction("Java Information", new ICTask() {
			public void doTask() {
				JOptionPane.showConfirmDialog(null, new JavaInfoPanel(),
						"Java Information", JOptionPane.DEFAULT_OPTION);
			}
		});
	}

}

class JavaInfoPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
	JavaInfoPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		String version = CJavaSystem.getInstance().getVersionString();
		add(new JLabel("JVM Version: " + version));
		String path = CJavaSystem.getInstance().getPath();
		add(new JLabel("JVM Path: " + path));
		String memoryInfo = CJavaSystem.getInstance().getMemoryInfo();
		add(new JLabel(memoryInfo));
	}
}
