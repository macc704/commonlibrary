/**
 * CUnit.java
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.model;

/**
 * グラフ軸および単位の名前を保持します
 * 
 * @author Administrator
 * @version $Id: CUnit.java,v 1.1 2005/03/08 03:04:45 bam Exp $
 */
public class CUnit {

  private String name = null;

  /**
   * Constructor for CUnit.
   */
  public CUnit(String name) {

    this.name = name;
  }

  public String getName() {

    return this.name;
  }

}