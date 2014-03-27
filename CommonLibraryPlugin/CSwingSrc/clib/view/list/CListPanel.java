/*
 * CListPanel.java
 * Copyright (c) 2002 Boxed-Economy Project.  All rights reserved.
 */
package clib.view.list;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * @author macchan
 * @version $Id: CListPanel.java,v 1.3 2006/07/14 09:55:01 macchan Exp $
 */
public class CListPanel<T> extends JPanel {

	private static final long serialVersionUID = 1L;

	/***************************************************************************
	 * Instance Variables
	 **************************************************************************/

	private ICListModel<T> model = null;

	@SuppressWarnings("rawtypes")
	private JList jList = new JList();

	/***************************************************************************
	 * Constructors
	 **************************************************************************/

	/**
	 * Constructor for CListPanel.
	 */
	public CListPanel() {
		this(new ArrayList<T>());
	}

	/**
	 * Constructor for CListPanel.
	 */
	public CListPanel(List<T> list) {
		this.model = new CListListModel<T>(list);
		this.initialize();
	}

	/***************************************************************************
	 * Initializer
	 **************************************************************************/

	/**
	 * Initialize this Panel
	 */
	@SuppressWarnings("unchecked")
	private void initialize() {
		// initialize layout
		this.setLayout(new BorderLayout());

		// create components
		JScrollPane scroll = new JScrollPane();

		// initialize components
		jList.setModel(model);

		// add components
		scroll.getViewport().add(jList);
		this.add(scroll, BorderLayout.CENTER);

	}

	/***************************************************************************
	 * PrefferedSize
	 **************************************************************************/

	public Dimension getPreferredSize() {
		Dimension size = super.getPreferredSize();
		size.width = Math.max(size.width, 50);
		size.height = Math.max(size.height, 50);
		return size;
	}

	/***************************************************************************
	 * Updater
	 **************************************************************************/

	/**
	 * Update List
	 */
	public void refresh() {
		this.model.update();
		validate();
		initializeSelection();
	}

	/**
	 * initialize the selection
	 */
	private void initializeSelection() {
		this.jList.setSelectedIndices(new int[0]);
	}

	/***************************************************************************
	 * getter and setter
	 **************************************************************************/

	/**
	 * Returns the list.
	 * 
	 * @return List
	 */
	public List<T> getList() {
		return new ArrayList<T>(this.model.getList());
	}

	/**
	 * Add the Element.
	 * 
	 * @param element
	 */
	public void addElement(T element) {
		this.model.getList().add(element);
		initializeSelection();
	}

	/**
	 * Sets the list.
	 * 
	 * @param list
	 *            The list to set
	 */
	public void addElements(List<T> elements) {
		this.model.getList().addAll(elements);
		initializeSelection();
	}

	/**
	 * Sets the list.
	 * 
	 * @param list
	 *            The list to set
	 */
	public void setList(List<T> list) {
		this.model.setList(list);
		initializeSelection();
	}

	public boolean containsElement(T elements) {
		return this.model.getList().contains(elements);
	}

	/**
	 * Remove all elements
	 */
	public void removeAll() {
		this.model.getList().removeAll(model.getList());
		initializeSelection();
	}

	/**
	 * Returns the jList.
	 * 
	 * @return JList
	 */
	@SuppressWarnings("rawtypes")
	public JList getJList() {
		return jList;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	@SuppressWarnings("unchecked")
	public void setModel(ICListModel<T> model) {
		this.model = model;
		jList.setModel(model);
		initializeSelection();
	}

	/**
	 * Returns the model.
	 * 
	 * @return CListListModel
	 */
	public ICListModel<T> getModel() {
		return model;
	}

	/**
	 * Returns the selectedElements
	 * 
	 * @return selectedElements
	 */
	public List<T> getSelectedElements() {
		List<T> elements = new ArrayList<T>();
		int[] indices = jList.getSelectedIndices();
		for (int i : indices) {
			elements.add(getModel().getList().get(i));
		}
		return elements;
	}

	/**
	 * Returns the selectedElement
	 * 
	 * @return selectedElement
	 */
	public T getSelectedElement() {
		int index = jList.getSelectedIndex();
		if (index < 0) {
			return null;
		}
		return getModel().getList().get(jList.getSelectedIndex());
	}

	/***************************************************************************
	 * test code
	 **************************************************************************/

	public static void main(String args[]) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(300, 300);

		CListPanel<Object> listPanel = new CListPanel<Object>();
		frame.getContentPane().add(listPanel);
		frame.setVisible(true);

		List<Object> list = new ArrayList<Object>();
		list.add("��");
		list.add("��");
		list.add("��");
		list.add("��");
		listPanel.setList(list);

		list.add("��");
		list.add(new JCheckBox("Hoge"));
		listPanel.refresh();
	}

}
