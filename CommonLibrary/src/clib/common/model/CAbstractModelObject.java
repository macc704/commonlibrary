/*
 * CAbstractModelObject.java
 * Created on Jun 27, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.common.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;

public class CAbstractModelObject implements ICModelObject {

	private static final long serialVersionUID = 1L;

	private transient Map<ICModelChangeListener, ICModelChangeListener> listeners;
	private transient LinkedList<Object[]> queue;
	private boolean notifyMode = true;
	private transient boolean transaction = false;

	public CAbstractModelObject() {
		initializeTransientFields();
	}

	public void addModelListener(ICModelChangeListener l) {
		if (!this.listeners.containsKey(l)) {
			this.listeners.put(l, l);
		}
	}

	public void removeModelListener(ICModelChangeListener l) {
		if (this.listeners.containsKey(l)) {
			this.listeners.remove(l);
		}
	}

	public void fireModelUpdated(Object... args) {
		if (notifyMode) {
			if (transaction) {
				queue.push(args);
			} else {
				fire(args);
			}
		}
	}

	private void fire(Object... args) {
		for (ICModelChangeListener l : listeners.values()) {
			l.modelUpdated(args);
		}
	}

	public void setNotifyMode(boolean notifyMode) {
		this.notifyMode = notifyMode;
	}

	public synchronized void beginTransaction() {
		if (transaction) {
			throw new RuntimeException("already in transaction");
		}
		transaction = true;
	}

	public synchronized void endTransaction() {
		if (!transaction) {
			throw new RuntimeException("is not in transaction");
		}
		if (!queue.isEmpty()) {// 1回だけ引数なしを発火
			fire();
			queue.clear();
		}
		transaction = false;
	}

	private void initializeTransientFields() {
		listeners = new WeakHashMap<ICModelChangeListener, ICModelChangeListener>();
		queue = new LinkedList<Object[]>();
	}

	private void readObject(ObjectInputStream stream) throws IOException,
			ClassNotFoundException {
		initializeTransientFields();
		stream.defaultReadObject();
	}

}
