/*
 * CActionManager.java
 * Created on Apr 8, 2011 
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.actions;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.Action;

import clib.common.thread.ICTask;

/**
 * @author macchan
 * 
 */
public class CActionManager {

	private Map<String, Action> actions = new LinkedHashMap<String, Action>();

	public Action createAction(String name, final ICTask task) {
		if (actions.containsKey(name)) {
			throw new RuntimeException(name + " action is already created.");
		}
		Action action = CActionUtils.createAction(name, task);
		actions.put(name, action);
		return action;
	}

	public void setActionStates(boolean active) {
		for (Action action : actions.values()) {
			action.setEnabled(active);
		}
	}
}
