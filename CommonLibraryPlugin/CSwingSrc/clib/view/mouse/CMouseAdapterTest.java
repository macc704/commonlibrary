/*
 * CMouseTest.java
 * Created on 2012/06/07
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.mouse;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import clib.common.utils.CFrameTester;

/**
 * @author macchan
 * 
 */
public class CMouseAdapterTest extends JPanel {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		CMouseAdapterTest test = new CMouseAdapterTest();
		CFrameTester.open(test);
	}

	/**
	 * 
	 */
	public CMouseAdapterTest() {
		JPanel p = new JPanel();
		p.setBackground(Color.RED);
		p.setBounds(20, 20, 20, 20);
		add(p);
		A a = new A();
		p.addMouseListener(a);
		p.addMouseMotionListener(a);

		JPanel p2 = new JPanel();
		p2.setBackground(Color.BLUE);
		p2.setBounds(50, 20, 20, 20);
		add(p2);
		B b = new B();
		p2.addMouseListener(b);
		p2.addMouseMotionListener(b);
	}

}

class A extends MouseAdapter {
	boolean dragging = false;
	int px, py;

	public A() {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		dragging = true;
		px = e.getX();
		py = e.getY();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (dragging) {
			Component target = e.getComponent();
			int dx = e.getX();
			int dy = e.getY();
			int newx = target.getX() - px + dx;
			int newy = target.getY() - py + dy;
			target.setLocation(newx, newy);
			// System.out.println(dx + "," + dy);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		dragging = false;
	}
}

class B extends MouseAdapter {
	enum State {
		RELEASED, DRAGGING, LEFT, RIGHT, TOP, BOTTOM
	};

	static int X = 3;

	State state = State.RELEASED;
	int px, py;

	public B() {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		px = e.getX();
		py = e.getY();
		if (px <= X) {
			state = State.LEFT;
		} else if (px >= e.getComponent().getWidth() - X - 1) {
			state = State.RIGHT;
		} else if (py <= X) {
			state = State.TOP;
		} else if (py >= e.getComponent().getHeight() - X - 1) {
			state = State.BOTTOM;
		} else {
			state = State.DRAGGING;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (state == State.RELEASED) {
			return;
		}

		Component target = e.getComponent();
		int dx = e.getX();
		int dy = e.getY();
		int x = target.getX();
		int y = target.getY();
		int w = target.getWidth();
		int h = target.getHeight();
		switch (state) {
		case LEFT:
			x = x + dx;
			w = w - dx;
			break;
		case RIGHT:
			w = dx;
			break;
		case TOP:
			y = y + dy;
			h = h - dy;
			break;
		case BOTTOM:
			h = dy;
			break;
		case DRAGGING:
			x = x - px + dx;
			y = y - py + dy;
			break;
		default:
			break;
		}
		target.setBounds(x, y, w, h);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		state = State.RELEASED;
	}
}
