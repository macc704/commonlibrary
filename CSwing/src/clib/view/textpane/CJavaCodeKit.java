/*
 * JavaCodeKit.java
 * Created on 2007/09/22 by macchan
 * Copyright(c) 2007 CreW Project
 */
package clib.view.textpane;

import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.StyledEditorKit;

/**
 * JavaCodeKit
 */
public class CJavaCodeKit extends StyledEditorKit {
	private static final long serialVersionUID = 1L;

	public Document createDefaultDocument() {
		// return new JavaCodeDocument3();
		CJavaCodeDocument doc = new CJavaCodeDocument();
		doc.putProperty(PlainDocument.tabSizeAttribute, new Integer(2));
		return doc;

		// JavaCodeDocument2 doc = new JavaCodeDocument2();
		// Vector<String> keywords = new Vector<String>();
		// keywords.addElement("abstract");
		// keywords.addElement("boolean");
		// keywords.addElement("break");
		// keywords.addElement("byte");
		// keywords.addElement("byvalue");
		// keywords.addElement("case");
		// keywords.addElement("cast");
		// keywords.addElement("catch");
		// keywords.addElement("char");
		// keywords.addElement("class");
		// keywords.addElement("const");
		// keywords.addElement("continue");
		// keywords.addElement("default");
		// keywords.addElement("do");
		// keywords.addElement("double");
		// keywords.addElement("extends");
		// keywords.addElement("else");
		// keywords.addElement("false");
		// keywords.addElement("final");
		// keywords.addElement("finally");
		// keywords.addElement("float");
		// keywords.addElement("for");
		// keywords.addElement("future");
		// keywords.addElement("generic");
		// keywords.addElement("if");
		// keywords.addElement("implements");
		// keywords.addElement("import");
		// keywords.addElement("inner");
		// keywords.addElement("instanceof");
		// keywords.addElement("int");
		// keywords.addElement("interface");
		// keywords.addElement("long");
		// keywords.addElement("native");
		// keywords.addElement("new");
		// keywords.addElement("null");
		// keywords.addElement("operator");
		// keywords.addElement("outer");
		// keywords.addElement("package");
		// keywords.addElement("private");
		// keywords.addElement("protected");
		// keywords.addElement("public");
		// keywords.addElement("rest");
		// keywords.addElement("return");
		// keywords.addElement("short");
		// keywords.addElement("static");
		// keywords.addElement("super");
		// keywords.addElement("switch");
		// keywords.addElement("synchronized");
		// keywords.addElement("this");
		// keywords.addElement("throw");
		// keywords.addElement("throws");
		// keywords.addElement("transient");
		// keywords.addElement("true");
		// keywords.addElement("try");
		// keywords.addElement("var");
		// keywords.addElement("void");
		// keywords.addElement("volatile");
		// keywords.addElement("while");
		// doc.setKeywords(keywords);
		// return doc;
	}

}