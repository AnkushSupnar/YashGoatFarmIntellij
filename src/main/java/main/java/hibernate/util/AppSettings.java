package main.java.main.java.hibernate.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

/**
 * App-wide settings persisted to a plain properties file. Used today for the
 * quotation stamp/signature image and its target dimensions in the PDF.
 */
public class AppSettings {

	public static final String SETTINGS_DIR = "D:\\Software\\Settings";
	public static final String STAMP_PROPS_FILE = SETTINGS_DIR + "\\stamp.properties";
	public static final String STAMP_IMAGE_FILE = SETTINGS_DIR + "\\stamp.png";

	public static final float DEFAULT_STAMP_WIDTH = 120f;
	public static final float DEFAULT_STAMP_HEIGHT = 70f;

	private AppSettings() { }

	public static class StampConfig {
		public final String imagePath;
		public final float widthPt;
		public final float heightPt;

		public StampConfig(String imagePath, float widthPt, float heightPt) {
			this.imagePath = imagePath;
			this.widthPt = widthPt;
			this.heightPt = heightPt;
		}

		public boolean isUsable() {
			return imagePath != null && new File(imagePath).exists();
		}
	}

	public static StampConfig loadStampConfig() {
		File f = new File(STAMP_PROPS_FILE);
		if (!f.exists()) return null;
		try (FileInputStream in = new FileInputStream(f)) {
			Properties p = new Properties();
			p.load(in);
			String path = p.getProperty("path");
			float w = parseFloatOr(p.getProperty("width"), DEFAULT_STAMP_WIDTH);
			float h = parseFloatOr(p.getProperty("height"), DEFAULT_STAMP_HEIGHT);
			if (path == null || path.isEmpty()) return null;
			return new StampConfig(path, w, h);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Copy the source image into the settings folder and persist its display
	 * dimensions. Returns the absolute path of the saved image, or null on failure.
	 */
	public static String saveStampConfig(File sourceImage, float widthPt, float heightPt) {
		try {
			File dir = new File(SETTINGS_DIR);
			if (!dir.exists() && !dir.mkdirs()) {
				throw new IOException("Cannot create " + SETTINGS_DIR);
			}
			String name = sourceImage.getName().toLowerCase();
			String ext = name.contains(".") ? name.substring(name.lastIndexOf('.')) : ".png";
			File target = new File(SETTINGS_DIR + "\\stamp" + ext);
			Files.copy(sourceImage.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);

			Properties p = new Properties();
			p.setProperty("path", target.getAbsolutePath());
			p.setProperty("width", Float.toString(widthPt));
			p.setProperty("height", Float.toString(heightPt));
			try (FileOutputStream out = new FileOutputStream(STAMP_PROPS_FILE)) {
				p.store(out, "Quotation stamp configuration");
			}
			return target.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static float parseFloatOr(String s, float fallback) {
		if (s == null) return fallback;
		try { return Float.parseFloat(s.trim()); } catch (Exception e) { return fallback; }
	}
}
