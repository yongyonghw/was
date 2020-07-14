package service;

import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.Const;
import http.HttpRequest;
import http.HttpResponse;

public class NowTime extends SimpleServlet{
	private static final Logger logger = LoggerFactory.getLogger("NowTime");
	@Override
	public void service(HttpRequest req, HttpResponse res) {
		// TODO Auto-generated method stub
		try {
			// TODO Auto-generated method stub
			Writer raw = res.getWriter();
			Date now = new Date();
			SimpleDateFormat f = new SimpleDateFormat(Const.dateFormat);
			String d = f.format(now);
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
