package fusionTableClient.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CodecServiceAsync {
	void encode(String message, String clearSample, String encodedSample, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void decode(String message, String clearSample, String encodedSample, AsyncCallback<String> callback)
			throws IllegalArgumentException;
}
