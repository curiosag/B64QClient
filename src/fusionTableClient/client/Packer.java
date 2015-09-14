package fusionTableClient.client;

public class Packer {
	private static String separator = "gummibaerenfell";

	public static String pack(String result, String error, String trace) {
		return result + separator + error + separator + trace;
	}

	public static Unpacked unpack(String packed) {
		String[] parts = packed.split(separator);

		return new Unpacked(parts[0], parts[1], parts[2]);
	}

}
