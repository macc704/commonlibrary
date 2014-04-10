/*
 * OValueChangeListener.java
 * Created on 2003/10/12
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.crewlib;

/**
 * Class OValueChangeListener.
 * 
 * @author macchan
 * @version $Id: OValueChangeListener.java,v 1.2 2005/03/08 03:05:06 bam Exp $
 */
public interface OValueChangeListener {

  /**
   * @param value
   * @param index
   */
  void valueReorderd(Object value, int index);

  /**
   * @param value
   * @param index
   */
  void valueAdded(Object value, int index);

  /**
   * @param value
   */
  void valueRemoved(Object value);

}