/**
 * ICDataSet.java
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.model;

/**
 * GraphViewのための折れ線グラフモデルのインターフェースです
 * 
 * @author Administrator
 * @version $Id: ICDataSet.java,v 1.1 2005/03/08 03:04:45 bam Exp $
 */
public interface ICDataSet {

  /**
   * 凡例を返します
   */
  public String getLabel();

  /**
   * X軸の情報（軸名、単位名）を返します
   */
  public CAxis getAxisX();

  /**
   * X軸の情報（軸名、単位名）を返します
   */
  public CAxis getAxisY();

  /**
   * 折れ線を構成するGraphPoint群を返します
   */
  public CPointList getPoints();

}