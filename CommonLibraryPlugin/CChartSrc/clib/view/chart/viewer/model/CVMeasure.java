/*
 * CVMeasure.java
 * Created on 2004/05/05
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.model;

/**
 * Class CVMeasure.
 * 
 * @author macchan
 * @version $Id: CVMeasure.java,v 1.1 2005/03/08 03:04:46 bam Exp $
 */
public class CVMeasure {

  public static final double DEFAULT_STEP = 5d;

  private CVAxis axis;

  private boolean auto = true;
  private boolean showGrid = false;
  private double step = DEFAULT_STEP;

  public CVMeasure(CVAxis axis) {

    this.axis = axis;
  }

  public boolean isAuto() {

    return auto;
  }

  public void setAuto(boolean auto) {

    this.auto = auto;
  }

  public void setStep(double value) {

    this.step = value;
  }

  public double getStep() {

    if (auto) {
      return getProperValue(DEFAULT_STEP * (1 / axis.getScale().getValue()));
    }
    return step;
  }

  public void setShowGrid(boolean showGrid) {

    this.showGrid = showGrid;
  }

  public boolean isShowGrid() {

    return showGrid;
  }

  /**
   * 小数点の有効桁数が1桁になるように倍率を計算しなおす
   */
  private double getProperValue(double value) {

    if (value >= 1d) {
      return (int) value;
    }

    //有効桁数を求める
    int validPlace = getValidPlace(value);

    //有効桁数以下を切り捨てる
    int power = (int) Math.pow(10, validPlace);
    int tmp = (int) (value * (double) power);
    value = ((double) tmp / power);

    return value;
  }

  /**
   * 有効桁数を求めます
   */
  private int getValidPlace(double value) {

    int place = 0;
    do {
      value = value * 10;
      place++;
    } while ((int) value == 0);

    return place;
  }

  public static void main(String args[]) {

    CVMeasure measure = new CVMeasure(null);
    System.out.println(measure.getProperValue(4.2));
    System.out.println(measure.getProperValue(3));
    System.out.println(measure.getProperValue(0.18));
    System.out.println(measure.getProperValue(0.92));
    System.out.println(measure.getProperValue(0.092));
  }
}