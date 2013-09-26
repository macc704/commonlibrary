/*
 * AMetaModel.java
 * Created on 2012/05/15
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package cswing.table.test;

import java.util.ArrayList;
import java.util.List;

import clib.view.table.model.ICTableModelDescripter;
import cswing.table.model.CAbstractEditableFieldProvider;
import cswing.table.model.CNumberEditableField;
import cswing.table.model.CObjectEditableField;
import cswing.table.model.CStringEditableField;
import cswing.table.model.ICEditableFieldProvider;
import cswing.table.model.ICTableElementEditorDescripter;

/**
 * @author macchan
 * 
 */
public class AMetaModel implements ICTableElementEditorDescripter<A> {
	public AMetaModel() {
	}

	public String getName() {
		return "ƒeƒXƒg";
	}

	public A newInstance() {
		return new A();
	}

	private ICTableModelDescripter<A> descripter = new ICTableModelDescripter<A>() {
		public java.lang.Class<?> getValiableClass(int index) {
			return new Class[] { String.class, Integer.class }[index];
		}

		public Object getVariableAt(A model, int index) {
			return new Object[] { model.getA(), model.getC() }[index];
		}

		public int getVariableCount() {
			return 2;
		}

		public String getVariableName(int index) {
			return new String[] { "AA", "BB" }[index];
		}
	};

	public ICTableModelDescripter<A> getDescripter() {
		return descripter;
	}

	public ICEditableFieldProvider<A> createEditableFieldProvider(A model) {
		return new CAbstractEditableFieldProvider<A>(model) {
			protected void initialize() {
				add(new CStringEditableField("‚Ù‚°‚P", getField("a"),
						getModel()));
				add(new CStringEditableField("‚Ù‚°‚Q", getField("b"),
						getModel()));
				add(new CNumberEditableField("‚Ù‚°‚R", getField("c"),
						getModel()));
				List<A> items = new ArrayList<A>();
				items.add(new A("hoge1"));
				items.add(new A("hoge2"));
				items.add(new A("hoge3"));
				add(new CObjectEditableField("‚Ù‚°‚S", getField("e"),
						getModel(), items));
			}
		};
	}
}
