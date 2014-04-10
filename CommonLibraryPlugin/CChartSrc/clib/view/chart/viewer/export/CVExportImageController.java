/*
 * Created on 2005/02/16
 *
 * Copyright (c) 2005 CreW Poject.  All rights reserved.
 */
package clib.view.chart.viewer.export;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import clib.common.thread.ICTask;
import clib.crewlib.OExtensionFileFilter;
import clib.view.actions.CAction;
import clib.view.actions.CActionUtils;
import clib.view.chart.viewer.resource.ChartResource;
import clib.view.chart.viewer.view.CVChartCanvas;
import clib.view.chart.viewer.view.axis.CVAbstractAxisPanel;
import clib.view.chart.viewer.view.axis.CVXAxisContainerPanel;
import clib.view.chart.viewer.view.axis.CVYAxisContainerPanel;

/**
 * Class CVExportImageController
 * 
 * @author bam
 * @version $Id: CVExportImageController.java,v 1.3 2005/12/18 13:03:25 bam Exp
 *          $
 */
public class CVExportImageController {

	/******************************************
	 * Constant(s).
	 ******************************************/

	private static final String BUTTON_TEXT = ChartResource
			.get("CVExportImagePanel.ButtonSave");

	private static final String EXTENSION = "jpg";
	private static final String FILE_CHOOSE_MESSAGE = "Image File ("
			+ EXTENSION + ")";

	/******************************************
	 * Attribute(s).
	 ******************************************/

	private CVChartCanvas graphCanvas;
	private CVXAxisContainerPanel xAxisContainerPanel;
	private CVYAxisContainerPanel yAxisContainerPanel;

	/******************************************
	 * Constructor(s).
	 ******************************************/

	public CVExportImageController(CVChartCanvas canvas,
			CVXAxisContainerPanel xAxisContainer,
			CVYAxisContainerPanel yAxisContainer) {

		super();

		this.graphCanvas = canvas;
		this.xAxisContainerPanel = xAxisContainer;
		this.yAxisContainerPanel = yAxisContainer;
	}

	/******************************************
	 * Operation(s).
	 ******************************************/

	public CAction createAction() {
		CAction action = CActionUtils.createAction(BUTTON_TEXT, new ICTask() {
			public void doTask() {
				doSaveImageFile();
			}
		});
		return action;
	}

	public JPanel createButtonPanel() {
		JPanel panel = new JPanel();
		JButton saveButton = new JButton(createAction());
		panel.setLayout(new BorderLayout());
		panel.add(saveButton, BorderLayout.CENTER);
		return panel;
	}

	/**********************
	 * Save Image File
	 **********************/

	private void doSaveImageFile() {
		// input width, height, origin
		Dimension setting = this.inputExportSetting();
		if (setting == null) {
			return;
		}

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new OExtensionFileFilter(EXTENSION,
				FILE_CHOOSE_MESSAGE));

		int result = fileChooser.showSaveDialog(graphCanvas);
		if (result == JFileChooser.APPROVE_OPTION) {
			File imageFile = fileChooser.getSelectedFile();
			if (!imageFile.getName().endsWith("." + EXTENSION)) {
				imageFile = new File(imageFile.getAbsolutePath() + "."
						+ EXTENSION);
			}

			exportImage(imageFile, setting);
		}
	}

	private void exportImage(File imageFile, Dimension setting) {
		try {
			if (!imageFile.exists()) {
				imageFile.createNewFile();
			}

			// calculate size
			Collection<CVAbstractAxisPanel> xPanels = this.xAxisContainerPanel
					.getAxisPanels();
			int height = calculateHeight(xPanels, setting.height);
			Collection<CVAbstractAxisPanel> yPanels = this.yAxisContainerPanel
					.getAxisPanels();
			int width = calculateWidth(yPanels, setting.width);

			// preprocess
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);// =
			// this.graphCanvas.getgetImage();
			Graphics2D g = (Graphics2D) image.getGraphics();

			// export
			int xPosition = exportYAxis(yPanels, g, setting);
			int yPosition = exportXAxis(xPanels, height, g, xPosition, setting);
			exportGraph(g, xPosition, setting);
			exportCorner(height, width, g, yPosition, setting);

			ImageIO.write(image, EXTENSION, imageFile);
		} catch (Exception e) {
			throw new RuntimeException("Draw Image File failed.:", e);
		}
	}

	/**
	 * @return
	 */
	private Dimension inputExportSetting() {
		CVExportImageDialog dialog = new CVExportImageDialog(this.graphCanvas);
		dialog.setVisible(true);
		if (!dialog.isOK()) {
			return null;
		}

		Dimension setting = new Dimension();
		setting.height = dialog.getHeight();
		setting.width = dialog.getWidth();

		return setting;
	}

	/**
	 * @param height
	 * @param width
	 * @param g
	 * @param yPosition
	 */
	private void exportCorner(int height, int width, Graphics2D g,
			int yPosition, Dimension setting) {
		Graphics2D gCorner = (Graphics2D) g.create(0, yPosition, width
				- setting.width, height - setting.height);
		gCorner.fillRect(0, 0, width - setting.height, height - setting.height);
	}

	/**
	 * @param g
	 * @param xPosition
	 */
	private void exportGraph(Graphics2D g, int xPosition, Dimension setting) {
		Graphics2D gGraph = (Graphics2D) g.create(xPosition, 0, setting.width,
				setting.height);
		this.graphCanvas.paintComponent(gGraph);
	}

	/**
	 * @param xPanels
	 * @param height
	 * @param g
	 * @param xPosition
	 * @return
	 */
	private int exportXAxis(Collection<CVAbstractAxisPanel> xPanels,
			int height, Graphics2D g, int xPosition, Dimension setting) {
		int yPosition = height;
		for (CVAbstractAxisPanel panel : xPanels) {
			JComponent xPanel = panel.getMeasureCanvas();
			yPosition -= xPanel.getHeight();

			Graphics2D gXPanel = (Graphics2D) g.create(xPosition, yPosition,
					setting.width, xPanel.getHeight());
			gXPanel.fillRect(0, 0, setting.width, xPanel.getHeight());
			xPanel.paint(gXPanel);
		}
		return yPosition;
	}

	/**
	 * @param yPanels
	 * @param g
	 * @return
	 */
	private int exportYAxis(Collection<CVAbstractAxisPanel> yPanels,
			Graphics2D g, Dimension setting) {
		int xPosition = 0;
		for (CVAbstractAxisPanel panel : yPanels) {
			JComponent yPanel = panel.getMeasureCanvas();

			Graphics2D gYPanel = (Graphics2D) g.create(xPosition, 0,
					yPanel.getWidth(), setting.height);
			gYPanel.fillRect(0, 0, yPanel.getWidth(), setting.height);
			yPanel.paint(gYPanel);

			xPosition += yPanel.getWidth();
		}
		return xPosition;
	}

	/**
	 * @param xPanels
	 * @return
	 */
	private int calculateHeight(Collection<CVAbstractAxisPanel> xPanels,
			int height) {
		for (CVAbstractAxisPanel panel : xPanels) {
			JComponent xPanel = panel.getMeasureCanvas();
			height += xPanel.getHeight();
		}
		return height;
	}

	/**
	 * @param yPanels
	 * @return
	 */
	private int calculateWidth(Collection<CVAbstractAxisPanel> yPanels,
			int width) {
		for (CVAbstractAxisPanel panel : yPanels) {
			JComponent yPanel = panel.getMeasureCanvas();
			width += yPanel.getWidth();
		}
		return width;
	}

}