package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.Const.ErrorCode;
import http.HttpRequest;
import http.HttpResponse;

public class Error extends SimpleServlet {
	private static final Logger logger = LoggerFactory.getLogger("ErrorServlet");
	@Override
	public void service(HttpRequest req, HttpResponse res) {
		try {
			
			// TODO Auto-generated method stub
			Writer raw = res.getWriter();
			String filePath = res.getOutPutFilePath();
			if(filePath != null) {
				BufferedReader br = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath)));
				
				String data = "";
				while(br.ready()) {
					data += br.readLine();
				}
				sendHeader(raw, "HTTP/1.1 " + res.getErrorCode() + " " + getErrMsg(res.getErrorCode()), "text/html; charset=utf-8", data.length());
				raw.write(data);
				raw.flush();
			}
		} catch(Exception e) {
			logger.error("can not write.", e);
		} finally {
			logger.info(req.toString() + " response: " + ((double) System.currentTimeMillis() - req.getStartTime()) / 1000 + " ms");
		}
	}
	
	private String getErrMsg(int errCode) {
		for(ErrorCode e : ErrorCode.values()) {
			if(e.getCode() == errCode) {
				return e.name();
			}
		}
		return ErrorCode.SERVER_INTERNAL_ERROR.name();
	}

}
