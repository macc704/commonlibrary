package matsuzawa.app.updater;

public class Version {
	private int major = 0;
	private int middle = 0;
	private int minor = 0;

	public Version(String text) {
		try {
			if (text.isEmpty()) {
				return;
			}
			String[] tokens = text.split("[.]");
			int len = tokens.length;
			if (len >= 1) {
				major = Integer.parseInt(tokens[0].trim());
			}
			if (len >= 2) {
				middle = Integer.parseInt(tokens[1]);
			}
			if (len >= 3) {
				minor = Integer.parseInt(tokens[2]);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public int getMajor() {
		return major;
	}

	public int getMiddle() {
		return middle;
	}

	public int getMinor() {
		return minor;
	}

	public boolean isEqual(Version another) {
		return compare(another) == 0;
	}

	public boolean isNewer(Version another) {
		return compare(another) > 0;
	}

	public int compare(Version another) {
		if (major != another.major) {
			return major - another.major;
		}

		if (middle != another.middle) {
			return middle - another.middle;
		}

		if (minor != another.minor) {
			return minor - another.minor;
		}

		return 0;
	}

	@Override
	public String toString() {
		return major + "." + middle + "." + minor;
	}

	// test
	public static void main(String[] args) {
		System.out.println(new Version("1.2.2"));
		System.out.println(new Version("1.2.2").compare(new Version("1.2.2")));
		System.out.println(new Version("1.2.2").compare(new Version("1.2.3")));
		System.out.println(new Version("1.2.5").isNewer(new Version("1.2.3")));
		System.out.println(new Version("1.3.5").isNewer(new Version("1.3.5")));
		System.out.println(new Version("1.3.5").isEqual(new Version("1.3.5")));
		System.out
				.println(new Version("2.1.1").isNewer(new Version("1.10.10")));
	}
}
