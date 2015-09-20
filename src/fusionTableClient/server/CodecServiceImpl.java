package fusionTableClient.server;

import fusionTableClient.client.CodecService;
import fusionTableClient.client.Packer;
import codec.AyudaSystems;
import codec.Feedback;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CodecServiceImpl extends RemoteServiceServlet implements
		CodecService {

	private enum Op {
		encode, decode
	}

	private String runOp(Op op, String text, String clearSample,
			String encodedSample) {
		
		Feedback f = new FeedbackAggregator();
		String error = "";
		String result = "";

		try {
			AyudaSystems en = new AyudaSystems(clearSample, encodedSample, f);
			if (op == Op.encode)
				result = en.encode(text);
			else
				result = en.decode(text);

		} catch (Exception e) {
			error = e.getMessage();
		}

		return Packer.pack(result, error, f.toString());
	}

	public String encode(String message, String clearSample,
			String encodedSample) throws IllegalArgumentException {
		return runOp(Op.encode, message, clearSample, encodedSample);
	}

	public String decode(String message, String clearSample,
			String encodedSample) throws IllegalArgumentException {
		return runOp(Op.decode, message, clearSample, encodedSample);
	}
}
