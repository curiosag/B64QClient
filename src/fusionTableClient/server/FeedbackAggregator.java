package fusionTableClient.server;

import codec.Feedback;

public class FeedbackAggregator implements Feedback {

	StringBuilder sb = new StringBuilder();

	public void echo(String arg0) {
		sb.append(arg0 + "\r\n");
	}

	@Override
	public String toString() {
		return sb.toString();
	}

}
