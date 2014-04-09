/*
 * CTimeLinePane.java
 * Created on Mar 9, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.timeline.pane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import clib.common.model.ICModelChangeListener;
import clib.common.time.CTime;
import clib.view.panels.COverViewPanel;
import clib.view.scrollpane.CRichScrollPane;
import clib.view.scrollpane.CSelectionHandler;
import clib.view.scrollpane.CViewportUtils;
import clib.view.timeline.model.CTimeModel;
import clib.view.timeline.model.CTimeTransformationModel;

/**
 * @author macchan
 * 
 */
public class CTimeLinePane extends JComponent {

	private static final long serialVersionUID = 1L;

	private static final Color BACKGROUND_COLOR = Color.WHITE;

	private CTimeTransformationModel transModel;

	private JPanel panelWest = new JPanel();
	private JPanel panelEast = new JPanel();
	private JSplitPane splitPaneWestEast = new JSplitPane(
			JSplitPane.HORIZONTAL_SPLIT);

	private JPanel panelTopLeft = new JPanel();
	// private JViewport viewportVertical = new JViewport();
	private JScrollPane viewportVertical = new JScrollPane();
	private JPanel panelVertical = new JPanel();

	private JViewport viewportHorizontal = new JViewport();
	private JPanel panelHorizontal = new JPanel();
	private JPanel panelTimeGauge;

	private CTimeIndicatorPainterManager indicatorPainterManager = new CTimeIndicatorPainterManager();
	private CRichScrollPane scrollPaneMain = new CRichScrollPane();
	private CTimeIndicatorShowablePanel panelMain = new CTimeIndicatorShowablePanel(
			indicatorPainterManager);

	public CTimeLinePane() {
		initialize();
		setTimeTransModel(new CTimeTransformationModel());
		hookListeners();
	}

	private void initialize() {
		setLayout(new BorderLayout());

		// Splitpane
		splitPaneWestEast.setResizeWeight(0.2);
		splitPaneWestEast.setDividerSize(2);
		splitPaneWestEast.setLeftComponent(panelWest);
		splitPaneWestEast.setRightComponent(panelEast);
		add(splitPaneWestEast);

		// West
		panelWest.setLayout(new BorderLayout());
		panelTopLeft.setBackground(BACKGROUND_COLOR);
		panelWest.add(panelTopLeft, BorderLayout.NORTH);
		panelVertical.setBackground(BACKGROUND_COLOR);
		panelVertical.setLayout(new BorderLayout());
		getViewportVertical().setView(panelVertical);
		viewportVertical
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		viewportVertical
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		panelWest.add(viewportVertical, BorderLayout.CENTER);

		// East
		panelEast.setLayout(new BorderLayout());
		panelHorizontal.setLayout(new BorderLayout());
		panelHorizontal.setBackground(BACKGROUND_COLOR);
		viewportHorizontal.setView(panelHorizontal);
		panelEast.add(viewportHorizontal, BorderLayout.NORTH);
		panelMain.setBackground(BACKGROUND_COLOR);
		panelMain.setLayout(new BorderLayout());
		scrollPaneMain.setView(panelMain);
		panelEast.add(scrollPaneMain, BorderLayout.CENTER);

		// handler setting
		scrollPaneMain.setSelectionHandler(new CSelectionHandler() {
			public void handleSelection(Rectangle rect) {
				doSelectScale(rect);
			}
		});
	}

	public void setTimeTransModel(CTimeTransformationModel transModel) {
		this.transModel = transModel;

		// gauge setting
		panelTimeGauge = new CTimeGaugePanelB(transModel,
				indicatorPainterManager);
		panelTimeGauge.setBorder(BorderFactory.createLoweredBevelBorder());
		panelHorizontal.add(panelTimeGauge, BorderLayout.CENTER);

		transModel.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				refreshGauge();
			}
		});

		// gauge painter
		indicatorPainterManager.setModel(transModel);

		refreshGauge();
	}

	/**
	 * @return the transModel
	 */
	public CTimeTransformationModel getTimeTransModel() {
		return transModel;
	}

	/**
	 * @param panelTimeGauge
	 *            the panelTimeGauge to set
	 */
	public void setPanelTimeGauge(JPanel panelTimeGauge) {
		this.panelTimeGauge = panelTimeGauge;
	}

	/*******************************************************
	 * Utilities
	 *******************************************************/

	public void createDefaultButtons() {
		int SIZE = 25;
		{
			JButton button = new JButton();
			button.setBorder(BorderFactory.createEmptyBorder());
			button.setPreferredSize(new Dimension(SIZE, SIZE));
			button.setAction(getMinusAction());
			panelTopLeft.add(button);
		}
		{
			JButton button = new JButton();
			button.setBorder(BorderFactory.createEmptyBorder());
			button.setPreferredSize(new Dimension(SIZE, SIZE));
			button.setAction(getFitAction());
			panelTopLeft.add(button);
		}
		{
			JButton button = new JButton();
			button.setBorder(BorderFactory.createEmptyBorder());
			button.setPreferredSize(new Dimension(SIZE, SIZE));
			button.setAction(getPlusAction());
			panelTopLeft.add(button);
		}
	}

	public Action getPlusAction() {
		return new AbstractAction("+") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				changeScaleByNow(transModel.getScale() * 2);
			}
		};
	}

	public Action getFitAction() {
		return new AbstractAction("*") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				fitScale();
			}
		};
	}

	public Action getMinusAction() {
		return new AbstractAction("-") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				changeScaleByNow(transModel.getScale() / 2);
			}
		};
	}

	public COverViewPanel createOverviewPanel() {
		return new COverViewPanel(scrollPaneMain.getViewport());
	}

	/*******************************************************
	 * Scaling
	 *******************************************************/

	public void fitScale() {
		changeScaleByNow(getFitScaleRate());
	}

	public double getFitScaleRate() {
		int width = scrollPaneMain.getViewport().getViewRect().width;
		if (width > 0) {
			return transModel.getFitScale(width);
		} else {
			return 1d;
		}
	}

	public void doSelectScale(Rectangle selectedRect) {
		final CTime time = transModel.x2Time(selectedRect.getCenterX());
		JViewport viewport = scrollPaneMain.getViewport();
		double rate = (double) viewport.getWidth() / selectedRect.getWidth();
		changeScale(transModel.getScale() * rate, time);
	}

	private void changeScaleByNow(double newScale) {
		Rectangle r = scrollPaneMain.getViewport().getViewRect();
		CTime time = transModel.x2Time(r.getCenterX());
		changeScale(newScale, time);
	}

	// 従って，Resizeイベントがきたタイミングでキューに乗せる方式に変更 の関係
	private CTime requestTime;

	private void changeScale(double newScale, final CTime time) {
		this.requestTime = time;
		transModel.setScale(newScale);

		// setSize系のイベントがsetされた後から発行されるので，その後にpositionをきめなおす
		// さらに，EventQueueに入れても，あとからresizeイベントが入ることがある（Peerの関係か？それ以上不明）
		// 従って，Resizeイベントがきたタイミングでキューに乗せる方式にに変更
		// SwingUtilities.invokeLater(new Runnable() {
		// public void run() {
		// setViewPositionByTime(time);
		// }
		// });
	}

	private void setViewPositionByTime(CTime centerTime) {
		double centerX = transModel.time2X(centerTime);
		double x = centerX - scrollPaneMain.getViewport().getWidth() / 2;
		double y = scrollPaneMain.getViewport().getViewPosition().y;
		setViewPosition(new Point((int) x, (int) y));
	}

	public void moveViewPosition(Dimension d) {
		Point current = scrollPaneMain.getViewport().getViewPosition();
		Point newP = new Point(current.x + d.width, current.y + d.height);
		setViewPosition(newP);
	}

	private void setViewPosition(Point p) {
		Point newP = CViewportUtils.arrangeViewPositionRange(p,
				scrollPaneMain.getViewport());
		scrollPaneMain.getViewport().setViewPosition(newP);
	}

	/*******************************************************
	 * Listners and Hooking
	 *******************************************************/

	private ComponentAdapter hvToMainListener = new ComponentAdapter() {
		public void componentResized(ComponentEvent e) {
			refreshHVToMain();
		}
	};

	private ChangeListener mainToHVListener = new ChangeListener() {
		public void stateChanged(ChangeEvent e) {
			synchronized (getTreeLock()) {
				if (requestTime != null) {
					final CTime time = requestTime;
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							setViewPositionByTime(time);
						}
					});
					requestTime = null;
				}
			}
			refreshMainToHV();
		}
	};

	protected void hookListeners() {
		panelVertical.addComponentListener(hvToMainListener);
		// panelHorizontal.addComponentListener(hvToMainListener);
		scrollPaneMain.getViewport().addChangeListener(mainToHVListener);
	}

	protected void unhookListeners() {
		panelVertical.removeComponentListener(hvToMainListener);
		// panelHorizontal.removeComponentListener(hvToMainListener);
		scrollPaneMain.getViewport().removeChangeListener(mainToHVListener);
	}

	/*******************************************************
	 * refresh interface
	 *******************************************************/

	private void refreshMainToHV() {
		Rectangle r = scrollPaneMain.getViewport().getViewRect();
		getViewportVertical().setViewPosition(
				new Point(getViewportVertical().getViewPosition().x, r.y));
		viewportHorizontal.setViewPosition(new Point(r.x, viewportHorizontal
				.getViewPosition().y));
	}

	// view size changed with positioning
	private void refreshHVToMain() {
		// top left
		panelTopLeft.setPreferredSize(new Dimension(panelVertical.getWidth(),
				panelHorizontal.getHeight()));

		// main
		panelMain.setPreferredSize(new Dimension(panelHorizontal.getWidth(),
				panelVertical.getHeight()));
		panelMain.setSize(panelMain.getPreferredSize());
	}

	private void refreshGauge() {
		panelHorizontal.setPreferredSize(new Dimension(transModel
				.getPreferredWidth(), 30));
		panelHorizontal.setSize(panelHorizontal.getPreferredSize()); // EventQueueに入る
		panelMain.setPreferredSize(new Dimension(
				transModel.getPreferredWidth(), panelVertical.getHeight()));
		panelMain.setSize(panelMain.getPreferredSize());// EventQueueに入る
		// panelMain.validate();

		// Toolkit.getEventQueue().postEvent(e);
		// Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(theEvent);
	}

	/*******************************************************
	 * public interface
	 *******************************************************/

	public void hookIndicationChangeMouseListener() {
		MouseAdapter listener = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				setTime(e);
			}

			public void mouseDragged(MouseEvent e) {
				// scrolling
				JViewport viewport = scrollPaneMain.getViewport();
				Rectangle viewrect = viewport.getViewRect();
				int left = viewrect.x;
				int right = left + viewrect.width;
				int mouseX = e.getX();
				if (mouseX < left) {
					int move = mouseX - left;
					CViewportUtils.moveViewPosition(new Dimension(move, 0),
							viewport);
				} else if (mouseX > right) {
					int move = mouseX - right;
					CViewportUtils.moveViewPosition(new Dimension(move, 0),
							viewport);
				}

				// settime
				setTime(e);
			}

			private void setTime(MouseEvent e) {
				try {
					CTime time = transModel.x2Time(e.getX());
					CTime start = transModel.getRange().getStart();
					if (time.getAsLong() < start.getAsLong()) {
						time = start;
					}
					CTime end = transModel.getRange().getEnd();
					if (time.getAsLong() > end.getAsLong()) {
						time = end;
					}

					// TODO ここ，微妙にMVCになってない
					List<CTimeIndicatorPainter> indicators = indicatorPainterManager
							.getIndicators();
					int size = indicators.size();

					if (size <= 0) {
						return;
					}

					CTimeIndicatorPainter target = null;
					if (size >= 2 && e.isShiftDown()) {
						target = indicators.get(1);
					} else if (size >= 2 && e.isMetaDown()) {
						target = indicators.get(1);
					} else {
						target = indicators.get(0);
					}
					if (target != null) {
						target.getTimeModel().setTime(time);
						refreshIndicatorViews();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		panelHorizontal.addMouseListener(listener);
		panelHorizontal.addMouseMotionListener(listener);
	}

	public void createIndicator(Color color) {
		createIndicator(color, new CTimeModel());
	}

	public void createIndicator(Color color, CTimeModel timeModel) {
		CTimeIndicatorPainter painter = new CTimeIndicatorPainter();
		painter.setColor(color);
		painter.setTimeModel(timeModel);
		this.indicatorPainterManager.add(painter);
		refreshIndicatorViews();
		timeModel.addModelListener(new ICModelChangeListener() {
			public void modelUpdated(Object... args) {
				refreshIndicatorViews();
			}
		});
	}

	public int getIndicatorCount() {
		return this.indicatorPainterManager.getIndicators().size();
	}

	public void setIndicatorTime(CTime... times) {
		assert 0 <= times.length
				&& times.length < this.indicatorPainterManager.getIndicators()
						.size();
		for (int i = 0; i < times.length; i++) {
			indicatorPainterManager.getIndicators().get(i).getTimeModel()
					.setTime(times[i]);
		}
		refreshIndicatorViews();
	}

	// public CTimeIndicatorPainter getIndicator(int index) {
	// assert 0 <= index
	// && index < this.indicatorPainterManager.getIndicators().size();
	// return this.indicatorPainterManager.getIndicators().get(index);
	// }

	public void refreshIndicatorViews() {
		this.panelMain.repaint();
		this.panelTimeGauge.repaint();
	}

	/**
	 * @return the splitPaneWestEast
	 */
	public JSplitPane getSplitPaneWestEast() {
		return splitPaneWestEast;
	}

	/**
	 * @return the scrollPaneMain
	 */
	public CRichScrollPane getScrollPaneMain() {
		return scrollPaneMain;
	}

	/**
	 * @return the panelMain
	 */
	public void setMainPanel(JPanel panel) {
		// panelMain.removeAll();
		panelMain.add(panel);
	}

	/**
	 * @return the panelVertical
	 */
	public void setVerticalPanel(JPanel panel) {
		// panelVertical.removeAll();
		panelVertical.add(panel);
	}

	/**
	 * @return the viewportHorizontal
	 */
	public JViewport getViewportHorizontal() {
		return viewportHorizontal;
	}

	/**
	 * @return the viewportVertical
	 */
	public JViewport getViewportVertical() {
		// return viewportVertical;
		return viewportVertical.getViewport();
	}

}
