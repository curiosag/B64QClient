package fusionTableClient.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("codec")
public interface CodecService extends RemoteService {
	String encode(String message, String clearSample, String encodedSample) throws IllegalArgumentException;
	String decode(String message, String clearSample, String encodedSample) throws IllegalArgumentException;
}
