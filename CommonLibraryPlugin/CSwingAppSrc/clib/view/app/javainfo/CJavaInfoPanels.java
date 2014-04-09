package clib.view.app.javainfo;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

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
		String javahome = CJavaSystem.getInstance().getJVMHome();
		add(new JLabel("Java Home: " + javahome));
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
		final CMapTableModel<String, String> tm = new CMapTableModel<String, String>(
				env);
		tm.setTitles(Arrays.asList(new String[] { "Key", "Value" }));
		final JTable table = new JTable(tm);
		JScrollPane pane = new JScrollPane(table);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pane.setPreferredSize(new Dimension(400, 300));
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					if (row >= 0) {
						JPanel panel = new JPanel();
						panel.setPreferredSize(new Dimension(450, 200));
						panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
						{
							JPanel ipanel = new JPanel();
							String key = (String) tm.getValueAt(row, 0);
							JLabel label = new JLabel("key:  ");
							ipanel.add(label);
							JTextArea area = new JTextArea(key);
							area.setColumns(30);
							area.setRows(4);
							// area.setPreferredSize(new Dimension(200, 150));
							// //これだめ
							area.setLineWrap(true);
							area.setEditable(false);
							JScrollPane scroll = new JScrollPane(area);
							scroll.setViewportView(area);
							scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
							scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
							ipanel.add(scroll);
							panel.add(ipanel);
						}
						{
							JPanel ipanel = new JPanel();
							String value = (String) tm.getValueAt(row, 1);
							ipanel.add(new JLabel("value:"));
							JTextArea area = new JTextArea(value);
							area.setColumns(30);
							area.setRows(7);
							// area.setPreferredSize(new Dimension(200, 150));
							// //これだめ
							area.setLineWrap(true);
							area.setEditable(false);
							JScrollPane scroll = new JScrollPane(area);
							scroll.setViewportView(area);
							scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
							scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
							ipanel.add(scroll);
							panel.add(ipanel);
						}
						JOptionPane.showConfirmDialog(null, panel, "Variable",
								JOptionPane.DEFAULT_OPTION);
					}
				}
			}
		});
		JOptionPane.showConfirmDialog(null, pane, "Java Information",
				JOptionPane.DEFAULT_OPTION);
		// showDialog("Java Information", pane, JOptionPane.DEFAULT_OPTION,
		// null);
	}
}