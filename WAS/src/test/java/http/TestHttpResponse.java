package http;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import config.ConfigHandler;
import config.ServerConfig;
import config.Const.ErrorCode;
import config.ServerConfig.HostConfig;
import service.SimpleServlet;

public class TestHttpResponse extends HttpResponse{
	
	int errorCode;
	SimpleServlet servlet;
	String outPutFilePath;
	
	
	public TestHttpResponse(TestHttpRequest request) {
		super(request);
		// TODO Auto-generated constructor stub
	}

	
public void registerErrorServletTest(TestHttpRequest request) {
		
		ServerConfig serverConfig = ConfigHandler.getInstance().getServerConfig();
		Map<String, HostConfig> httpRootMap = serverConfig.getHttpRoot();
		
		HostConfig hostConfig = httpRootMap.get(request.getHostTest());
		
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
	
	
	public void registerServletTest(TestHttpRequest request) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
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
	
	public String getOutPutFilePath() {
		return outPutFilePath;
	}
}
