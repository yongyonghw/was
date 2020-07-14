package http;

import java.net.Socket;

public class TestHttpRequest extends HttpRequest {
	public TestHttpRequest(Socket socket) {
		super(socket);
		// TODO Auto-generated constructor stub
	}

	String host;

	public void setHostTest(String mock) {

		this.host = mock;
	}

	public String getHostTest() {
		return host;
	}
}
