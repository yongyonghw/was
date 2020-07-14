package http;

public interface Handler {
	public void handleRequest(HttpRequest request, HttpResponse response);
}
