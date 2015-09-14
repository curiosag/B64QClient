package fusionTableClient.client;

public class Unpacked {
	public final String result;
	public final String error;
	public final String trace;

	public Unpacked(String result, String error, String trace) {
		this.result = result;
		this.error = error;
		this.trace = trace;
	}
}