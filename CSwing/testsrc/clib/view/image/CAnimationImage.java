/*
 * CAnimationImage.java
 * Created on Jul 22, 2010 
 * Copyright(c) 2010 Yoshiaki Matsuzawa, Shizuoka University. All rights reserved.
 */
package clib.view.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

/**
 * @author macchan
 */
public class CAnimationImage {

	private List<BufferedImage> images;

	private int count = 0;

	public void coundUp() {
		count++;
		if (count >= size()) {
			count = 0;
		}
	}

	public BufferedImage getImage() {
		return images.get(count);
	}

	public int size() {
		return images.size();
	}

	public CAnimationImage(Object sender, String name) {
		images = load(sender.getClass().getResource(name));
	}

	private List<BufferedImage> load(URL url) {
		try {
			Iterator<ImageReader> itr = ImageIO
					.getImageReadersByFormatName("gif");
			ImageReader reader = null;
			ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

			if (itr.hasNext()) {
				reader = itr.next();
			} else {
				throw new RuntimeException();
			}

			reader.setInput(ImageIO
					.createImageInputStream(new File(url.toURI())));

			int count = reader.getNumImages(true);
			for (int i = 0; i < count; i++) {
				images.add(reader.read(i));
			}

			return images;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
