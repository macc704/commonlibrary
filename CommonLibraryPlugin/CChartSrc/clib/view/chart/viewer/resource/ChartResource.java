/*
 * ChartResource.java
 * Created on 2004/06/06
 * 
 * Copyright (c) 2005 PlatBox Project. All rights reserved.
 */
package clib.view.chart.viewer.resource;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Class ChartResource.
 * 
 * @author macchan
 * @version $Id: ChartResource.java,v 1.1 2005/03/08 03:04:46 bam Exp $
 */
public class ChartResource {

	private static final String BUNDLE_NAME = "chart"; //$NON-NLS-1$

	private static final ResourceBundle bundle;

	static {
		String packageName = ChartResource.class.getPackage().getName();
		String bundleFullName = packageName + "." + BUNDLE_NAME;
		bundle = ResourceBundle.getBundle(bundleFullName, Locale.US);
	}

	/**
	 * Constructor for ChartResource.;
	 */
	private ChartResource() {
	}

	public static String get(String key) {
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}