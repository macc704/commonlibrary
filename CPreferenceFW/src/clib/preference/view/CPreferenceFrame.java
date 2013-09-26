package clib.preference.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import clib.preference.app.CPreferenceManager;
import clib.preference.model.ICPreferenceCategory;
import clib.view.list.CListPanel;

public class CPreferenceFrame extends JDialog {

	private static final long serialVersionUID = 1L;

	private CPreferenceManager manager;
	private ICPreferenceCategory current;

	private JButton buttonOK = new JButton("OK");
	private JButton buttonApply = new JButton("Apply");
	private JButton buttonCancel = new JButton("Cancel");

	private JPanel panelLeft = new JPanel();
	private JPanel panelRight = new JPanel();
	private JSplitPane splitter = new JSplitPane();

	private JPanel panelMain = new JPanel();
	private JPanel panelButtons = new JPanel();
	private CListPanel<ICPreferenceCategory> panelCategories;

	public CPreferenceFrame(CPreferenceManager manager) {
		this.manager = manager;
		initialize(getContentPane());
		initialSelection();
	}

	private void initialSelection() {
		if (panelCategories.getJList().getModel().getSize() > 0) {
			panelCategories.getJList().setSelectedIndex(0);
		}
	}

	private void initialize(Container contentPane) {
		contentPane.setLayout(new BorderLayout());
		panelLeft.setLayout(new BorderLayout());
		panelRight.setLayout(new BorderLayout());

		splitter.setLeftComponent(panelLeft);
		splitter.setRightComponent(panelRight);
		splitter.setResizeWeight(0.3);
		contentPane.add(splitter);

		panelMain.setLayout(new BorderLayout());
		// contentPane.add(panelMain, BorderLayout.CENTER);
		panelRight.add(panelMain);

		panelCategories = new CListPanel<ICPreferenceCategory>(
				manager.getCategories());
		panelCategories.getJList().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		panelCategories.getJList().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						selectionChanged();
					}
				});
		// contentPane.add(panelCategories, BorderLayout.WEST);
		panelLeft.add(panelCategories);

		panelButtons.add(buttonOK);
		buttonOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doOK();
			}
		});
		panelButtons.add(buttonApply);
		buttonApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doApply();
			}
		});
		panelButtons.add(buttonCancel);
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});
		// contentPane.add(panelButtons, BorderLayout.SOUTH);
		panelRight.add(panelButtons, BorderLayout.SOUTH);
	}

	private void selectionChanged() {
		this.current = panelCategories.getSelectedElement();
		if (this.current != null) {
			panelMain.removeAll();
			JPanel panel = current.getPage();
			panelMain.add(panel);
			validate();
			repaint();
		}
	}

	private void doOK() {
		for (ICPreferenceCategory category : manager.getCategories()) {
			category.save();
		}
		manager.saveToFile();
		this.dispose();
	}

	private void doApply() {
		if (current != null) {
			current.save();
		}
	}

	private void doCancel() {
		this.dispose();
	}
}
