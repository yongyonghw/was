package http;
/*
 *	대상 Servlet을 선택한다.
 * 
 */
public class ServletHandler implements Handler{

	private HttpResponse response;
	
	public ServletHandler(HttpRequest request) {
		this.response = new HttpResponse(request);
	}
	
	
	@Override
	public void handleRequest(HttpRequest request, HttpResponse response) {
		// TODO Auto-generated method stub
		new ResponseHandler().handleRequest(request, this.response );
	}

}
