package http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.Const;
import config.Const.ErrorCode;

public class HttpRequest {
	
	static Logger logger = LoggerFactory.getLogger("HttpRequest");
	
	private String method;
	private String url;
	private String protocol;
	private String host;
	private int errorCode;
	final private Socket socket;
	private boolean isAvailable = true;
	final private long startTime;
	
	public HttpRequest(Socket socket) {
		this.startTime = System.currentTimeMillis();
		this.socket = socket;
		try {
			init();
		} catch(Exception e) {
			this.errorCode = ErrorCode.SERVER_INTERNAL_ERROR.getCode();
			logger.error("Request Create Fail", e);
		}
		
	}

	private void init() throws UnsupportedEncodingException, IOException {
		Reader in = new InputStreamReader(new BufferedInputStream(this.socket.getInputStream()), "UTF-8");
		StringBuilder requestLine = new StringBuilder();
		while (in.ready()) {
			int c = in.read();
			requestLine.append((char) c);
		}
		String get = requestLine.toString();
		if(get.length() == 0) {
			isAvailable = false;
			return;
		} 
		
		String[] tokens = get.split("\\s+");
		this.method = tokens[0];
		this.url = tokens[1];
		this.protocol = tokens[2];
		
		for(int i = 0; i < tokens.length; i ++) {
			if(tokens[i].toLowerCase().indexOf("host") > -1) {
				this.host = tokens[i + 1];
				break;
			}
		}
//		this.host = tokens[4];
	}
	
	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getHost() {
		return host;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public Socket getSocket()
	{
		return this.socket;
	}
	
	public boolean isAvailable() {
		return isAvailable;
	}
	
	public long getStartTime() {
		return startTime;
	}

	@Override
	public String toString() {
		return "HttpRequest [method=" + method + ", url=" + url + ", protocol=" + protocol + ", host=" + host + "]";
	}
}
