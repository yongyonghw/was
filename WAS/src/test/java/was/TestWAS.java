package was;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.ConfigHandler;
import config.Const;
import config.Const.ErrorCode;
import config.ServerConfig;
import http.TestHttpRequest;
import http.TestHttpResponse;

public class TestWAS {
	static Logger logger = LoggerFactory.getLogger("TestWAS");
	static Thread server;

	@BeforeClass
	public static void startServer() throws InterruptedException {
		server = new Thread(new HttpServer());
		server.start();
		Thread.sleep(3000);
	}

	// 1. host localhost 와 127.0.0.1 로 들어왔을 때 생성되는 페이지 비교
//	@Test
	public void parserHeaderEachOther() {

		TestHttpRequest q = new TestHttpRequest(null);
		q.setHostTest("localhost");
		TestHttpResponse s = new TestHttpResponse(q);
		s.registerErrorServletTest(q);
		String a = s.getOutPutFilePath();

		q = new TestHttpRequest(null);
		q.setHostTest("127.0.0.1");
		s = new TestHttpResponse(q);
		s.registerErrorServletTest(q);
		String b = s.getOutPutFilePath();

		logger.info(a);
		logger.info(b);

		assertFalse(a.equals(b));
	}

	// 2. 설정파일 관리
//	@Test
	public void loadConfig() {

		ServerConfig config = ConfigHandler.getInstance().getServerConfig();
		logger.info(config.toString());
		assertTrue(config != null);
	}

	// 3. 404 Error 처리
	@Test
	public void errorHandleNotFound() throws IOException {
		URL u = new URL("http://localhost/notFound");
		HttpURLConnection http = (HttpURLConnection) u.openConnection();
		http.setRequestMethod("GET");
		http.setRequestProperty("host", "localhost");
		http.setRequestProperty("Cache-Control", "no-cache");
		http.setRequestProperty("Accept", "*/*");
		http.setReadTimeout(20000);
		http.setConnectTimeout(20000);
		logger.info(String.valueOf(http.getResponseCode()));
		logger.info(http.getResponseMessage());
		BufferedReader br = new BufferedReader(new InputStreamReader(http.getErrorStream()));
		logger.info(br.readLine());
		assertTrue(ErrorCode.NOT_FOUND.getCode() == http.getResponseCode());
	}

	// 4. 403 Error 처리
	@Test
	public void errorHandleForbidden() throws IOException {
		URL u = new URL("http://localhost/cmd.exe");
		HttpURLConnection http = (HttpURLConnection) u.openConnection();
		http.setRequestMethod("GET");
		http.setRequestProperty("host", "localhost");
		http.setRequestProperty("Cache-Control", "no-cache");
		http.setRequestProperty("Accept", "*/*");
		logger.info(String.valueOf(http.getResponseCode()));
		logger.info(http.getResponseMessage());
		BufferedReader br = new BufferedReader(new InputStreamReader(http.getErrorStream()));
		logger.info(br.readLine());
		assertTrue(ErrorCode.FORBIDDEN.getCode() == http.getResponseCode());
	}
	
	@Test
	public void errorHandleForbidden2() throws IOException {
		URL u = new URL("http://localhost/../../");
		HttpURLConnection http = (HttpURLConnection) u.openConnection();
		http.setRequestMethod("GET");
		http.setRequestProperty("host", "localhost");
		http.setRequestProperty("Cache-Control", "no-cache");
		http.setRequestProperty("Accept", "*/*");
		logger.info(String.valueOf(http.getResponseCode()));
		logger.info(http.getResponseMessage());
		BufferedReader br = new BufferedReader(new InputStreamReader(http.getErrorStream()));
		logger.info(br.readLine());
		assertTrue(ErrorCode.FORBIDDEN.getCode() == http.getResponseCode());
	}

	// 5. Hello Test
	@Test
	public void packageNameTest() throws IOException {
		URL u = new URL("http://localhost/service.Hello");
		HttpURLConnection http = (HttpURLConnection) u.openConnection();
		http.setRequestMethod("GET");
		http.setRequestProperty("host", "localhost");
		http.setRequestProperty("Cache-Control", "no-cache");
		http.setRequestProperty("Accept", "*/*");
		logger.info(String.valueOf(http.getResponseCode()));
		logger.info(http.getResponseMessage());
		BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
		String serviceHello = br.readLine();

		u = new URL("http://localhost/Hello");
		http = (HttpURLConnection) u.openConnection();
		http.setRequestMethod("GET");
		http.setRequestProperty("host", "localhost");
		http.setRequestProperty("Cache-Control", "no-cache");
		http.setRequestProperty("Accept", "*/*");
		logger.info(String.valueOf(http.getResponseCode()));
		logger.info(http.getResponseMessage());
		br = new BufferedReader(new InputStreamReader(http.getInputStream()));
		String hello = br.readLine();

		assertTrue(serviceHello.equals(hello));
	}

	// 6. SimpleServlet > NowTime
	@Test
	public void getNowTime() throws IOException {
		URL u = new URL("http://localhost/NowTime");
		HttpURLConnection http = (HttpURLConnection) u.openConnection();
		http.setRequestMethod("GET");
		http.setRequestProperty("host", "localhost");
		http.setRequestProperty("Cache-Control", "no-cache");
		http.setRequestProperty("Accept", "*/*");
		logger.info(String.valueOf(http.getResponseCode()));
		logger.info(http.getResponseMessage());
		BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
		String dateStr = br.readLine();
		logger.info(dateStr);

		DateFormat format = new SimpleDateFormat(Const.dateFormat);
		format.setLenient(false);
		boolean isDate = true;
		try {
			format.parse(dateStr);
		} catch (ParseException e) {
			isDate = false;
		}

		assertTrue(isDate);
	}

	@AfterClass
	public static void shutdownServer() throws InterruptedException {
		Thread.sleep(10000);
		server.interrupt();
	}

}
