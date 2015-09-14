package fusionTableClient.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

@SuppressWarnings("deprecation")
public class FusionTableClient implements EntryPoint {

	String defaultCodeSample = "MzI2NDk+OD09PDU5Ozo9ODg6QD02Mzg3Nzg+OjpAOTYyNTw9OjhBQDE3OzY9Pjk+PTowNjo1ND49O0E9NDY7NTc1PT85PzQxODU8Ozg3QEI5OTg1PDU5O0A7NTQ2NTU2PTc+QDk5NDQ4PTY/Pj4xNDQ7Njg2PT49NzE7Njw5Oj04QjU2Mjg8Nzg6OUAyNjU4PTk2Pzk7ODU6NDU2PTs9OTI5NjQ0Nz03OUIzOTc1NTY2PD0+OTc2Nzo3OEA8QTk2Njw3NTk/OUI2NTY1PD03N0FANTc4OD04OTs8PzEzOjc7Ojw7QDszNDk7Ojw+Ojk/NTM5NDY1N0A4QjE1Nzk4PTs9PkIyNDY5NDg6Pz46MDU3Nzc3PD08QTIyNTY9ODw3Pzs2MTQ3PTY6ODpAMzg0Nzk9PTc4PzYxODY1Ojs/QDo3NTo7NTo4N0E7MDo4NTw3Pzk9PTA6Mzo1Ojk9PDw=";

	TextArea clearText;
	TextArea encodedText;
	TextArea clearSampleText;
	TextArea codeSampleText;
	TextArea responseText;
	Timer waitInfoTimer;
	Image cow;

	private String getDefaultClearText() {
		String defaultClearText = "";
		for (int i = 0; i < 350; i++)
			defaultClearText = defaultClearText + Integer.toString(i % 10);
		return defaultClearText;
	}

	private final CodecServiceAsync codecService = GWT
			.create(CodecService.class);

	private void refreshWaitInfo(TextArea ta) {
		ta.setText(ta.getText() + ".");
	}

	private String normalize(String s) {
		if (s == null)
			return "";
		else
			return s.replace("\r", "").replace("\n", "").replace(" ", "");
	}

	private int expectedCodeLength(int clearLength) {
		int result = (clearLength / 3);
		if (clearLength % 3 != 0)
			result = result + 1;

		return result * 4;
	}

	private String checkCommonFields() {
		String error = "";

		int lenClearSample = normalize(clearSampleText.getText()).length();
		int lenCodeSample = normalize(codeSampleText.getText()).length();

		if (lenClearSample == 0)
			error = error + "clear text sample missing.\r\n";

		if (lenCodeSample == 0)
			error = error + "code sample text missing.\r\n";

		int lenCodeSampleExpected = expectedCodeLength(lenClearSample);
		if (lenCodeSample != lenCodeSampleExpected)
			error = error
					+ "length of code sample does not correspond to length of clear text sample("
					+ Integer.valueOf(lenClearSample)
					+ ").\r\n Sample code length should be "
					+ Integer.valueOf(lenCodeSampleExpected) + " but is "
					+ Integer.valueOf(lenCodeSample);

		return error;
	}

	private void alert(String error) {
		if (!error.isEmpty())
			responseText.setText("There's a PROBLEM\r\n\r\n" + error);
	}

	private boolean checkInputForEncoding() {
		String error = "";

		int lenClearText = clearText.getText().length();
		if (lenClearText == 0)
			error = error + "no text to encode.\r\n";

		String normalizedSampleText = normalize(clearSampleText.getText());
		if (lenClearText > normalizedSampleText.length())
			error = error
					+ "clear text sample must be of equal or greater length than text to encode.\r\n";

		error = error + checkCommonFields();
		alert(error);
		return error.isEmpty();
	}

	private boolean checkInputForDecoding() {
		String error = "";

		String normalizedEncodedText = normalize(encodedText.getText());
		int lenEncodedText = normalizedEncodedText.length();
		if (lenEncodedText == 0)
			error = error + "no text to decode.\r\n";

		String normalizedCodeSampleText = normalize(codeSampleText.getText());
		if (lenEncodedText > normalizedCodeSampleText.length())
			error = error
					+ "code sample must be of equal or greater length than text to decode.\r\n";

		error = error + checkCommonFields();
		alert(error);
		return error.isEmpty();
	}

	boolean isPrime(int n) {
		for (int i = 2; i < Math.sqrt(n); i++)
			if (n % i == 0)
				return false;

		return true;
	}

	private String getActiveCow() {
		if (isPrime(normalize(clearSampleText.getText()).length()))
			return "./dancing.gif";
		else
			return "fetching.gif";
	}

	private void beginOp() {
		responseText.setText("fetching result");
		waitInfoTimer.scheduleRepeating(1000);
		cow.setUrl(getActiveCow());
	}

	private void endOp() {
		waitInfoTimer.cancel();
		cow.setUrl("./grazing.gif");
	}

	private Element getElement(String id) {
		return RootPanel.get(id).getElement();
	}

	@SuppressWarnings("unused")
	private void scrollDown() {
		Window.scrollTo(0, Window.getScrollTop() + Window.getClientHeight());
	}

	public void onModuleLoad() {
		waitInfoTimer = new Timer() {
			public void run() {
				refreshWaitInfo(responseText);
			}
		};

		final Button encryptButton = new Button("-> encode ->");
		final Button decryptButton = new Button("<- decode <-");

		encryptButton.addStyleName("anyButton");
		encryptButton.setFocus(true);

		cow = Image.wrap(getElement("cow"));
		clearText = TextArea.wrap(getElement("clearText"));
		encodedText = TextArea.wrap(getElement("encodedText"));
		clearSampleText = TextArea.wrap(getElement("clearSampleText"));
		codeSampleText = TextArea.wrap(getElement("codeSampleText"));
		responseText = TextArea.wrap(getElement("responseText"));

		RootPanel.get("encryptButton").add(encryptButton);
		RootPanel.get("decryptButton").add(decryptButton);

		responseText
				.setText("> processing info will go here, hopefully no exceptions <");
		clearText.setText("> clear or decoded text will come here <");
		encodedText.setText("> Encoded text comes here <");
		clearSampleText.setText(getDefaultClearText());
		codeSampleText.setText(defaultCodeSample);

		decryptButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (checkInputForDecoding()) {
					beginOp();
					codecService.decode(normalize(encodedText.getText()),
							normalize(clearSampleText.getText()),
							normalize(codeSampleText.getText()),
							new AsyncCallback<String>() {
								public void onFailure(Throwable caught) {
									endOp();
									responseText.setText(caught.getMessage());
								}

								public void onSuccess(String result) {
									endOp();
									Unpacked p = Packer.unpack(result);
									if (p.error.isEmpty()) {
										clearText.setText(p.result);
										responseText.setText(p.trace);
									} else
										responseText.setText(p.error);
								}
							});
				}
			}
		});

		encryptButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (checkInputForEncoding()) {
					beginOp();
					codecService.encode(clearText.getText(),
							normalize(clearSampleText.getText()),
							normalize(codeSampleText.getText()),
							new AsyncCallback<String>() {
								public void onFailure(Throwable caught) {
									endOp();
									responseText.setText(caught.getMessage());
								}

								public void onSuccess(String result) {
									endOp();
									Unpacked p = Packer.unpack(result);
									if (p.error.isEmpty()) {
										encodedText.setText(p.result);
										responseText.setText(p.trace);
									} else
										responseText.setText(p.error);

								}
							});
				}
			}
		});
	}
}
