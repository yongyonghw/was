package http;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.ConfigHandler;
import config.Const.ErrorCode;
import config.ServerConfig;
import config.ServerConfig.HostConfig;
import service.SimpleServlet;

public class HttpResponse {
	static Logger logger = LoggerFactory.getLogger("HttpResponse");
	private String outPutFilePath;
	private int errorCode;
	private SimpleServlet servlet;
	private OutputStream raw; 
	private Writer out;

	public HttpResponse(HttpRequest request) {
		try {
			this.raw = new BufferedOutputStream(request.getSocket().getOutputStream());
			this.out = new OutputStreamWriter(raw);
			// request로 부터 요청을 확인한다.
			parseRequest(request);
		} catch(Exception e) {
			logger.error("Response Create Fail", e);
			this.errorCode = ErrorCode.SERVER_INTERNAL_ERROR.getCode();
			registerErrorServlet(request);
		} finally {
			//Request 에서의 에러 
			if(request.getErrorCode() != 0) {
				this.errorCode = request.getErrorCode();
				registerErrorServlet(request);
			}
		}
		
	}

	private void parseRequest(HttpRequest request) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		// 사용할 servlet을 정한다.
		registerServlet(request);
	}
	
	private void registerServlet(HttpRequest request) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String classNm = request.getUrl().replace("/", "");
		if(classNm.indexOf("service.") < 0) {
			classNm = "service." + classNm;
		} 
		
		if(request.getUrl().indexOf("..") > - 1 || 
				request.getUrl().indexOf(".exe") > - 1 ) {
			this.errorCode = ErrorCode.FORBIDDEN.getCode();
			registerErrorServlet(request);
			return;
		}
		
		Class<?> temp = null;
		try {
			temp = Class.forName(classNm);
		} catch(ClassNotFoundException e) {
			this.errorCode = ErrorCode.NOT_FOUND.getCode();
		}
		
		if(errorCode == 0)
			this.servlet = (SimpleServlet) temp.newInstance();
		else 
			registerErrorServlet(request);
	}
	
	public void registerErrorServlet(HttpRequest request) {
		
		ServerConfig serverConfig = ConfigHandler.getInstance().getServerConfig();
		Map<String, HostConfig> httpRootMap = serverConfig.getHttpRoot();
		
		HostConfig hostConfig = httpRootMap.get(request.getHost());
		
		String tempFilePath = "";
		Map<Integer, String> errorMap = null;
		if(hostConfig != null) {
			tempFilePath = serverConfig.getRootDirNm() + File.separator + hostConfig.getSubDirNm();
			errorMap = hostConfig.getErrorViewMapped();
		} else {
			tempFilePath = serverConfig.getRootDirNm() + File.separator + "TypeA";
			errorMap = new HashMap<Integer, String>();
			errorMap.put(ErrorCode.FORBIDDEN.getCode(), "A_403.html");
			errorMap.put(ErrorCode.NOT_FOUND.getCode(), "A_404.html");
			errorMap.put(ErrorCode.SERVER_INTERNAL_ERROR.getCode(), "A_500.html");
		}
		if(this.errorCode == 0) {
			this.errorCode = ErrorCode.SERVER_INTERNAL_ERROR.getCode();
		}
		
		
		String errorPage = errorMap.get(this.errorCode);
		if(errorPage == null)
			errorPage = "A_500.html";
	
		this.outPutFilePath = tempFilePath + File.separator + errorPage;
		Class<?> temp;
		try {
			temp = Class.forName("service.Error");
			this.servlet = (SimpleServlet) temp.newInstance();
		} catch(Exception e) {
			logger.error("Error Servlet Create fail", e);
		}
	}
	
	
	
	public String getOutPutFilePath() {
		return outPutFilePath;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public SimpleServlet getServlet() {
		return servlet;
	}

	public Writer getWriter() {
		return out;
	}
	
}








