package clib.view.app.javainfo;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import clib.common.system.CJavaSystem;
import clib.common.thread.ICTask;
import clib.view.actions.CAction;
import clib.view.actions.CActionUtils;
import clib.view.table.model.CMapTableModel;

public class CJavaInfoPanels {
	public static CAction createJavaInformationAction() {
		return CActionUtils.createAction("Java Information", new ICTask() {
			public void doTask() {
				JOptionPane.showConfirmDialog(null, new JavaInfoPanel(),
						"Java Information", JOptionPane.DEFAULT_OPTION);
			}
		});
	}
}


class JavaInfoPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	JavaInfoPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		String version = CJavaSystem.getInstance().getVersionString();
		add(new JLabel("JVM Version: " + version));
		String path = CJavaSystem.getInstance().getPath();
		add(new JLabel("JVM Path: " + path));
		String memoryInfo = CJavaSystem.getInstance().getMemoryInfo();
		add(new JLabel(memoryInfo));
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		{
			CAction action = CActionUtils.createAction("OS環境変数", new ICTask() {
				public void doTask() {
					showTable(CJavaSystem.getInstance().getSystemEnv());
				}
			});
			JButton button = new JButton(action);
			panel.add(button);
		}
		{
			CAction action = CActionUtils.createAction("JVM環境変数", new ICTask() {
				public void doTask() {
					showTable(CJavaSystem.getInstance().getJVMEnv());
				}
			});
			JButton button = new JButton(action);
			panel.add(button);
		}
		add(panel);
	}
	
	private void showTable(Map<String, String> env) {
		CMapTableModel<String, String> tm = new CMapTableModel<String, String>(env);
		tm.setTitles(Arrays.asList(new String[]{"Key","Value"}));
		JTable table = new JTable(tm);
		JScrollPane pane = new JScrollPane(table);
		pane.setPreferredSize(new Dimension(400, 300));
		JOptionPane.showConfirmDialog(null, pane,
				"Java Information", JOptionPane.DEFAULT_OPTION);
	}
}