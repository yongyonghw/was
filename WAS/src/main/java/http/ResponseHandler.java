package http;

public class ResponseHandler implements Handler {

	@Override
	public void handleRequest(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
		response.getServlet().service(request, response);
	}

}
