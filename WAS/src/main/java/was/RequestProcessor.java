package was;
import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.RequestHandler;

public class RequestProcessor implements Runnable {
	private final static Logger logger = LoggerFactory.getLogger("RequestProcessor");
	private Socket socket;

	public RequestProcessor(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		new RequestHandler(socket).handleRequest(null, null);
		try {
			socket.close();
		} catch (IOException e) {
			logger.error("Socket close Fail", e);
		} finally {
			if(!socket.isClosed())
				try {
					logger.info("Socket close Retry");
					socket.close();
				} catch (IOException e) {
					logger.error("Socket close Retry Fail", e);
				}
		}
	}
}