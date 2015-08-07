package org.kaivos.phl.util;

import static org.kaivos.phl.util.Assert.nonNull;
import static org.kaivos.phl.util.Assert.positive;

/** A version number according to <a href="http://semver.org/">semantic versioning</a>.
 * 
 * <p>The instances of this class follow the format
 * 
 * <p>{@code [major].[minor].[patch]-[release]+[metadata]}
 * 
 * <p>where {@code release} and {@code metadata} are optional. When missing, they are denoted
 * by an empty string ({@code ""}). Both must match the regex {@code [a-zA-Z0-9\-\.]+}. In addition,
 * numeric <i>fields</i> (sections of the strings separated by dots) must not have leading zeroes.
 * Also, every numeric field must be {@code Integer.MAX_VALUE} by maximum.
 * 
 * <p>The three numbers {@code major}, {@code minor}, {@code patch}
 * are mandatory. Their values range from {@code 0} to {@code Integer.MAX_VALUE}. Therefore, they
 * are always positive.
 * 
 * <p>Instances are returned by the static factory methods of this class. They throw an
 * {@code IllegalArgumentException} if any version number is negative and a {@code NullPointerException}
 * when either string is equal to {@code null}. The empty string {@code ""} may be passed to indicate
 * a missing string value.
 * 
 * <p>This class has three methods to comparison of instances: {@link Version#equals(Object)},
 * {@link Version#compareTo(Version)} and {@link Version#isCompatible(Version)}. All of them use
 * a different algorithm which is detailed in the documentation.
 * 
 * <p>This class has a standardized string representation that can be parsed by
 * {@link Version#parse(String)}. See {@code toString()} documentation for details.
 * 
 * <p>This class is immutable. The properties of an instance can not be changed after creation.
 * This class is also <i>value-based</i>, which means that operations based on object identity
 * are not permitted on the instances of this class.
 * 
 * @author expositionrabbit
 * 
 */
public final class Version implements Comparable<Version> {
	
	private final int major;
	private final int minor;
	private final int patch;
	private final String release;
	private final String metadata;
	
	private final int hash;
	
	private Version(int major, int minor, int patch, String release, String metadata) {
		this.major = positive(major);
		this.minor = positive(minor);
		this.patch = positive(patch);
		this.release = validProperty(nonNull(release));
		this.metadata = validProperty(nonNull(metadata));
		
		this.hash = calculateHashCode();
	}
	
	private static String validProperty(String s) {
		if(!s.isEmpty() && !s.matches("[a-zA-Z0-9\\-\\.]+")) {
			throw new IllegalArgumentException("Illegal property: " + s);
		}
		
		for(String field : s.split("\\.")) {
			if(field.length() > 1 && isNumeric(field) && field.startsWith("0")) {
				throw new IllegalArgumentException("Leading zeroes not permitted");
			}
		}
		
		return s;
	}
	
	private static boolean isNumeric(String field) {
		for(int i = 0; i < field.length(); i++) {
			char chr = field.charAt(i);
			
			if(chr < '0' || chr > '9') { return false; }
		}
		
		return true;
	}
	
	private int calculateHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + major;
		result = prime * result + metadata.hashCode();
		result = prime * result + minor;
		result = prime * result + patch;
		result = prime * result + release.hashCode();
		return result;
	}
	
	/** Returns the major version number.*/
	public int major() { return major; }
	
	/** Returns the minor version number.*/
	public int minor() { return minor; }
	
	/** Returns the patch number.*/
	public int patch() { return patch; }
	
	/** Returns the release string or an empty string if not present.*/
	public String release() { return release; }
	
	/** Returns the metadata string or an empty string if not present.*/
	public String metadata() { return metadata; }
	
	/** Returns whether this version is compatible with another versinos as defined
	 * in <a href="semver.org">Semantic versioning</a>. The algorithm is as follows:
	 * 
	 * <ul><li>The major version number of both version must match. If if is {@code 0},
	 * minor and patch numbers must also be equal.</li>
	 * <li>In addition, the release strings must match if present, as the Semantic
	 * versioning specification states that "A pre-release version indicates that the version
	 * is unstable and might not satisfy the intended compatibility requirements as denoted by its
	 * associated normal version."</li>
	 * <li>Metadata does not affect compatibility.</li></ul>
	 * 
	 * <p>This operation is symmetric. {@code a compatible b → b compatible a}
	 * 
	 * @param with the version to compare compatibility with
	 * @return whether this version is compatible with {@code with}*/
	public boolean isCompatible(Version with) {
		if(major == 0 || !release.isEmpty()) {
			return major == with.major &&
					minor == with.minor &&
					patch == with.patch &&
					release.equals(with.release);
		}
		
		return with.release.isEmpty() && major == with.major;
	}
	
	/** Converts this version to a standardized string representation. It is as
	 * follows:
	 * 
	 * <p>{@code [major].[minor].[patch](-release)(+metadata)}
	 * 
	 * <p>If {@code release} or {@code metadata} is an empty string, it or the leading
	 * sign will not appear in the resulting string.
	 * 
	 * <p>This format is guaranteed to remain consistent and parseable by
	 * {@link Version#parse(String)}.
	 * 
	 * @return the string representation of this version*/
	@Override
	public String toString() {
		return major + "." + minor + "." + patch +
				(release.isEmpty() ? "" : "-" + release) +
				(metadata.isEmpty() ? "" : "+" + metadata);
	}
	
	@Override
	public int hashCode() { return hash; }
	
	/** Returns whether this object is equal to some other version. Versions are equal
	 * if and only if all of their fields are.
	 * 
	 * <p>Note that this method does not reflect the specification of Semantic
	 * versioning and is therefore inconsistent with {@link Version#compareTo(Version)}
	 * and {@link Version#isCompatible(Version)}.
	 * 
	 * @param obj the object to compare to
	 * @return whether this version is equal to {@code obj}*/
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Version)) {
			return false;
		}
		Version other = (Version) obj;
		if(hash != other.hash) {
			return false;
		}
		if (major != other.major) {
			return false;
		}
		if (minor != other.minor) {
			return false;
		}
		if (patch != other.patch) {
			return false;
		}
		if (!release.equals(other.release)) {
			return false;
		}
		if (!metadata.equals(other.metadata)) {
			return false;
		}
		return true;
	}
	
	/** Compares this version to another according to <a href="semver.org">Semantic
	 * versioning</a>. Note that this method is inconsistent with {@link Version#equals(Object)}
	 * and {@link Version#isCompatible(Version)}. This operation is defined as follows:
	 * 
	 * <ul><li>First, {@code major}, {@code minor}, and {@code patch} are compared in that
	 * order.</li>
	 * <li>If all are equal, {@code release} is then compared.</li>
	 * <li>If {@code release} strings are inequal, they are compared field-by-field where
	 * a <i>field</i> is a section separated by a dot. If both fields are numeric, they
	 * are compared numerically. If only one is, the numeric field is defined to be less than
	 * the non-numeric field. If both fields are non-numeric, they are compared lexically.
	 * If their lengths are unequal and the smaller field is contained at the start of the
	 * bigger field, the bigger field is defined to be more.</li>
	 * <li>Metadata does not affect the comparison.</li></ul>
	 * 
	 * @param o the other version to compare to
	 * @return the result of the comparaison
	 * @see Comparable#compareTo(Object)*/
	@Override
	public int compareTo(Version o) {
		if(major > o.major) { return 1; }
		if(major < o.major) { return -1; }
		
		if(minor > o.minor) { return 1; }
		if(minor < o.minor) { return -1; }
		
		if(patch > o.patch) { return 1; }
		if(patch < o.patch) { return -1; }
		
		if(release.equals(o.release)) { return 0; }
		
		if(release.isEmpty()) { return 1; }
		if(o.release.isEmpty()) { return -1; }
		
		return compareRelease(o);
	}
	
	private int compareRelease(Version o) {
		String[] fields = release.split("\\.");
		String[] otherFields = o.release.split("\\.");
		
		if(fields.length != otherFields.length) {
			if(fields.length > otherFields.length) {
				return 1;
			}
			return -1;
		}
		
		for(int i = 0; i < fields.length; i++) {
			String field = fields[i];
			String otherField = otherFields[i];
			
			if(!field.equals(otherField)) {
				if(isNumeric(field)) {
					if(isNumeric(otherField)) {
						int result = compareNumerically(field, otherField);
						if(result != 0) { return result; }
						continue;
					}
					return -1;
				}
				
				if(isNumeric(otherField)) { return 1; }
				
				int result = compareLexically(field, otherField);
				if(result != 0) { return result; }
			}
		}
		
		return 0;
	}
	
	private static int compareNumerically(String field, String other) {
		return Integer.parseInt(field) > Integer.parseInt(other) ? 1 : -1;
	}
	
	private static int compareLexically(String field, String other) {
		int maxIndex = Math.min(field.length(), other.length());
		for(int i = 0; i < maxIndex; i++) {
			char chr = field.charAt(i);
			char otherChr = other.charAt(i);
			
			if(chr > otherChr) { return 1; }
			if(otherChr > chr) { return -1; }
		}
		
		if(field.length() != other.length()) {
			return field.length() > other.length() ? 1 : -1;
		}
		
		return 0;
	}
	
	/** Returns an instance composed of the three numbers.
	 * @param major the major version number
	 * @param minor the minor version number
	 * @param patch the patch number
	 * @return a matching {@code Version} instance*/
	public static Version of(int major, int minor, int patch) {
		return new Version(major, minor, patch, "", "");
	}
	
	/** Returns an instance composed of the three numbers and the release string.
	 * @param major the major version number
	 * @param minor the minor version number
	 * @param patch the patch number
	 * @param release the release string
	 * @return a matching {@code Version} instance*/
	public static Version of(int major, int minor, int patch, String release) {
		return new Version(major, minor, patch, release, "");
	}
	
	/** Returns an instance composed of the three numbers, the release string and the metadata
	 * string..
	 * @param major the major version number
	 * @param minor the minor version number
	 * @param patch the patch number
	 * @param release the release string
	 * @param metadata the metadata string
	 * @return a matching {@code Version} instance*/
	public static Version of(int major, int minor, int patch, String release, String metadata) {
		return new Version(major, minor, patch, release, metadata);
	}
	
	/** Parses a given string into a {@code Version} instance.
	 * 
	 * <p>The string must be formatted as if returned by {@code toString()}.
	 * 
	 * @param versionString the string to parse
	 * @return a parsed {@code Version} instance that matches the argument
	 * @throws NullPointerException if {@code versionString} is {@code null}
	 * @throws NumberFormatException if any version number is formatted wrong
	 * @throws IllegalArgumentException if an miscellaneous formatting error is found
	 * @see Version#toString()*/
	public static Version parse(String versionString) {
		nonNull(versionString);
		
		String metadata = "", release = "";
		
		int metadataStart = versionString.indexOf('+');
		if(metadataStart != -1) {
			metadata = versionString.substring(metadataStart, versionString.length()).substring(1);
			versionString = versionString.substring(0, metadataStart);
			if(metadata.isEmpty()) throw new IllegalArgumentException("Empty metadata");
		}
		
		int releaseStart = versionString.indexOf('-');
		if(releaseStart != -1) {
			release = versionString.substring(releaseStart, versionString.length()).substring(1);
			versionString = versionString.substring(0, releaseStart);
			if(release.isEmpty()) throw new IllegalArgumentException("Empty release");
		}
		
		String[] numbers = versionString.split("\\.");
		if(numbers.length != 3) throw new IllegalArgumentException("Malformed version numbers");
		
		return Version.of(Integer.parseInt(numbers[0]),
				Integer.parseInt(numbers[1]),
				Integer.parseInt(numbers[2]),
				release,
				metadata);
	}
}