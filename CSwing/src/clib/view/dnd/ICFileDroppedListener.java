/*
 * ICFileDroppedHandler.java
 * Created on 2012/01/27
 * Copyright(c) 2011 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.dnd;

import java.io.File;
import java.util.List;

/**
 * @author macchan
 * 
 */
public interface ICFileDroppedListener {

	public void fileDropped(List<File> files);

}
