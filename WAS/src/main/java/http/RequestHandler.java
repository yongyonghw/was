package http;

import java.net.Socket;

public class RequestHandler implements Handler{

	final private HttpRequest request;
	
	public RequestHandler(Socket socket) {
		this.request = new HttpRequest(socket);
	}
	
	@Override
	public void handleRequest(HttpRequest request, HttpResponse response) {
		
		request = this.request;
		if(request.isAvailable())
			new ServletHandler(request).handleRequest(request, null);
	}

}
