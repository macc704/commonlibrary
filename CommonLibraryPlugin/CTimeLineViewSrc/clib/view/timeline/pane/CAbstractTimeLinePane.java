/*
 * PhaseTimeLinePane.java
 * Created on Mar 24, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.timeline.pane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import clib.view.panels.CLayerLayout;

/**
 * @author macchan
 * 
 *         T型のModelについては，identifyがきっちりできる必要があることに注意すること．
 *         equals()が正しく実装されていない場合，入れ替え時に不具合が発生する．
 * 
 *         2012/06/07　高さが，モデルの種類によって変えられるようにした． getComponentHeight(T)
 */
public abstract class CAbstractTimeLinePane<T> extends JComponent {

	private static final long serialVersionUID = 1L;

	private static final Color DUMMY_COLOR = Color.WHITE;

	private CTimeLinePane timelinePane = new CTimeLinePane();

	private List<T> models = new ArrayList<T>();
	private JPanel verticalPanel = new JPanel();
	private Map<T, XPanel> vpanels = new HashMap<T, XPanel>();
	private JPanel mainPanel = new JPanel();
	private Map<T, XPanel> mpanels = new HashMap<T, XPanel>();

	private JLayeredPane layeredPane = new JLayeredPane();
	private JPanel dragLayer = new JPanel();
	private JPanel vDummyPanel = new JPanel();
	private JPanel mDummyPanel = new JPanel();

	private int defaultComponentHeight = 30;

	private List<ICSelectionListener> selectionListeners = new ArrayList<ICSelectionListener>();
	private T selectedModel;

	/**
	 * 
	 */
	public CAbstractTimeLinePane() {
		setLayout(new BorderLayout());

		layeredPane.setLayout(new CLayerLayout());
		add(layeredPane);
		layeredPane.add(timelinePane, JLayeredPane.DEFAULT_LAYER);
		dragLayer.setOpaque(false);

		verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));
		verticalPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		timelinePane.setVerticalPanel(verticalPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		timelinePane.setMainPanel(mainPanel);

		vDummyPanel
				.setMinimumSize(new Dimension(1, getDefaultComponentHeight()));
		vDummyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,
				getDefaultComponentHeight()));
		vDummyPanel.setBackground(DUMMY_COLOR);
		// mDummyPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mDummyPanel
				.setMinimumSize(new Dimension(1, getDefaultComponentHeight()));
		mDummyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,
				getDefaultComponentHeight()));
		mDummyPanel.setBackground(DUMMY_COLOR);
	}

	/**
	 * @return the timelinePane
	 */
	public CTimeLinePane getTimelinePane() {
		return timelinePane;
	}

	/**
	 * @param student
	 */
	public final void addModel(final T model) {
		addModel(model, models.size());
	}

	/**
	 * @param student
	 */
	public final void addModel(final T model, int index) {
		int n = models.size();
		if (!(0 <= index && index < n)) {
			index = n;
		}

		models.add(index, model);

		// vertical
		XPanel vpanel = new XPanel(createLeftPanel(model), model);
		vpanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 1) {
					if (!e.isShiftDown()) {
						clearSelection();
					}
					select(model);
				} else if (e.getClickCount() == 2) {
					if (!e.isShiftDown()) {
						clearSelection();
					}
				}
			}
		});
		verticalPanel.add(vpanel, index);
		vpanels.put(model, vpanel);

		// main
		XPanel mpanel = new XPanel(createRightPanel(model), model);
		// mpanel.addMouseListener(new MouseAdapter() {
		// public void mousePressed(MouseEvent e) {
		// if (e.getClickCount() == 1) {
		// if (!e.isShiftDown()) {
		// clearSelection();
		// }
		// select(model);
		// }
		// }
		// });
		mainPanel.add(mpanel, index);
		mpanels.put(model, mpanel);

		validate();
	}

	public final void removeModel(T model) {
		if (!models.contains(model)) {
			throw new RuntimeException("cannot remove model = " + model);
		}
		models.remove(model);
		verticalPanel.remove(vpanels.remove(model));
		mainPanel.remove(mpanels.remove(model));
		validate();
		repaint();
	}

	public final void removeAllModels() {
		List<T> copies = new ArrayList<T>(models);
		for (T t : copies) {
			removeModel(t);
		}
	}

	public List<T> getModels() {
		return models;
	}

	public int getModelCount() {
		return models.size();
	}

	public abstract JComponent createLeftPanel(T model);

	public abstract JComponent createRightPanel(T model);

	public int getComponentHeight(T model) {
		return getDefaultComponentHeight();
	}

	public int getDefaultComponentHeight() {
		return defaultComponentHeight;
	}

	public void setDefaultComponentHeight(int componentHeight) {
		this.defaultComponentHeight = componentHeight;
	}

	/************************************************
	 * Panel 系
	 ************************************************/

	class XPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public XPanel(JComponent comp, T model) {
			setMinimumSize(new Dimension(1, getComponentHeight(model)));
			setMaximumSize(new Dimension(Integer.MAX_VALUE,
					getDefaultComponentHeight()));
			setBackground(Color.WHITE);
			setLayout(new BorderLayout());
			add(comp);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.JComponent#getPreferredSize()
		 */
		@Override
		public Dimension getPreferredSize() {
			Dimension d = super.getPreferredSize();
			if (d.height <= getMinimumSize().height) {
				d.height = getMinimumSize().height;
			}
			return d;
		}

		public JComponent getPanel() {
			return (JComponent) getComponent(0);
		}
	}

	public List<JComponent> getVPanels() {
		return getPanels(vpanels);
	}

	public List<JComponent> getMPanels() {
		return getPanels(mpanels);
	}

	private List<JComponent> getPanels(Map<T, XPanel> map) {
		List<JComponent> panels = new ArrayList<JComponent>();
		for (T model : models) {
			panels.add(map.get(model).getPanel());
		}
		return panels;
	}

	// private List<JPanel> getPanels(Collection<JPanel> xpanels) {
	// List<JPanel> panels = new ArrayList<JPanel>();
	// for (JPanel dummyPanel : xpanels) {
	// panels.add((JPanel) dummyPanel.getComponent(0));
	// }
	// return panels;
	// }

	/************************************************
	 * Selection 系
	 ************************************************/

	public Color getSelectedColor() {
		return Color.YELLOW;
	}

	public final void select(T model) {
		this.selectedModel = model;
		vpanels.get(model).setBackground(getSelectedColor());
		mpanels.get(model).setBackground(getSelectedColor());
		fireSelectionChanged();
	}

	public void clearSelection() {
		this.selectedModel = null;
		for (JPanel panel : vpanels.values()) {
			panel.setBackground(Color.WHITE);
		}
		for (JPanel panel : mpanels.values()) {
			panel.setBackground(Color.WHITE);
		}
		fireSelectionChanged();
	}

	public void addSelectionListener(ICSelectionListener listener) {
		selectionListeners.add(listener);
	}

	public void removeSelectionListener(ICSelectionListener listener) {
		selectionListeners.remove(listener);
	}

	public void fireSelectionChanged() {
		for (ICSelectionListener listener : selectionListeners) {
			listener.selectionChanged();
		}
	}

	public T getSelectedModel() {
		return selectedModel;
	}

	/************************************************
	 * Drag Listener 系
	 ************************************************/

	private void setDummy(int index) {
		int n = models.size();
		if (!(0 <= index && index < n)) {
			index = n;// getDummyIndex();
		}

		try {
			getVerticalPanel().add(vDummyPanel, index);
			getMainPanel().add(mDummyPanel, index);
			getVerticalPanel().validate();
			getMainPanel().validate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void removeDummy() {
		getVerticalPanel().remove(vDummyPanel);
		getMainPanel().remove(mDummyPanel);
		getVerticalPanel().validate();
		getMainPanel().validate();
	}

	private int getDummyIndex() {
		return getVerticalPanel().getComponentZOrder(vDummyPanel);
	}

	protected MouseAdapter createDragMouseListener(T model) {
		return new DragMouseListener(model);
	}

	class DragMouseListener extends MouseAdapter {

		private T model;
		private JPanel draggingPanel = new JPanel();

		public DragMouseListener(T model) {
			this.model = model;
		}

		public void mousePressed(MouseEvent e) {
			layeredPane.add(dragLayer, JLayeredPane.DRAG_LAYER);

			// 順序重要　removeModelの前　削除してしまと相対位置が変更されるため，ここで変換する必要がある
			Point p = SwingUtilities.convertPoint(e.getComponent(),
					e.getPoint(), CAbstractTimeLinePane.this);

			// 順序重要　removeModelの前　削除してしまうため，ここで一旦保存の必要がある
			JComponent vPanel = vpanels.get(model).getPanel();
			JComponent mPanel = mpanels.get(model).getPanel();

			removeModel(model);

			draggingPanel.setBorder(BorderFactory.createRaisedBevelBorder());
			draggingPanel.setBackground(Color.WHITE);
			draggingPanel.setLayout(null);// prefferedを変えると他に影響を与える可能性があるので，自前でLayout
			draggingPanel.setSize(vPanel.getWidth() + mPanel.getWidth(),
					vPanel.getHeight());
			mPanel.setLocation(vPanel.getWidth(), 0);
			draggingPanel.add(vPanel);
			draggingPanel.add(mPanel);

			getDragLayer().add(draggingPanel);
			draggingPanel.setLocation(p);
			arrangeDummy(e);
		}

		public void mouseReleased(MouseEvent e) {
			getDragLayer().remove(draggingPanel);
			draggingPanel.removeAll();
			int index = getDummyIndex();
			removeDummy();
			addModel(model, index);

			layeredPane.remove(dragLayer);
			layeredPane.repaint();
		}

		public void mouseDragged(MouseEvent e) {
			Point p = SwingUtilities.convertPoint(e.getComponent(),
					e.getPoint(), CAbstractTimeLinePane.this);
			draggingPanel.setLocation(p);
			arrangeScroll(e);
			arrangeDummy(e);
		}

		private void arrangeScroll(MouseEvent e) {
			JViewport vport = timelinePane.getViewportVertical();
			Point mouseP = SwingUtilities.convertPoint(e.getComponent(),
					e.getPoint(), vport);

			int upperOffset = mouseP.y;
			if (upperOffset < 0) {
				doScroll(mouseP, vport, upperOffset);
			}
			int lowerOffset = mouseP.y - vport.getHeight();
			if (lowerOffset > 0) {
				doScroll(mouseP, vport, lowerOffset);
			}
		}

		private void doScroll(Point mouseP, JComponent c, int offset) {
			timelinePane.moveViewPosition(new Dimension(0, offset));

			Point p = new Point(mouseP.x, mouseP.y - offset);
			SwingUtilities.convertPointToScreen(p, c);
			try {
				new Robot().mouseMove(p.x, p.y);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		private void arrangeDummy(MouseEvent e) {
			JPanel vp = getVerticalPanel();
			Point p = SwingUtilities.convertPoint(e.getComponent(),
					e.getPoint(), vp);
			if (p.y <= 10) {
				p.y = 10;
			}
			p.x = 10;
			Component c = vp.getComponentAt(p);
			int index = vp.getComponentZOrder(c);
			setDummy(index);
		}
	}

	private JPanel getDragLayer() {
		return dragLayer;
	}

	private JPanel getVerticalPanel() {
		return verticalPanel;
	}

	private JPanel getMainPanel() {
		return mainPanel;
	}
}
