package service;

import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.HttpRequest;
import http.HttpResponse;

public class Hello extends SimpleServlet {
	private static final Logger logger = LoggerFactory.getLogger("HelloServlet");
	@Override
	public void service(HttpRequest req, HttpResponse res) {
		// TODO Auto-generated method stub
		try {
			// TODO Auto-generated method stub
			Writer raw = res.getWriter();
			String d = "Hello";
			sendHeader(raw, "HTTP/1.1 200 OK" , "text/html; charset=utf-8", d.length());
			raw.write(d);
			raw.flush();
		} catch(Exception e) {
			logger.error("can not write.", e);
		} finally {
			logger.info(req.toString() + " response: " + ((double) System.currentTimeMillis() - req.getStartTime()) / 1000 + " ms");
		}
	}
}
