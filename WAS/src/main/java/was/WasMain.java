package was;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.ConfigHandler;
import config.ServerConfig;

public class WasMain {
	final static private Logger logger = LoggerFactory.getLogger("WasMain");
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Thread server = new Thread(new HttpServer());
			server.start();
			logger.info("Server Start");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Server Running Error", e);
		}
	}

}
