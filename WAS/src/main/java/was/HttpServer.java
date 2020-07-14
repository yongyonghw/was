package was;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import config.ConfigHandler;
import config.ServerConfig;


/**
 * Created by cybaek on 15. 5. 22..
 */
public class HttpServer implements Runnable {
    static Logger logger = LoggerFactory.getLogger("HttpServer");

    
    private void start() throws IOException {
    	ServerConfig serverConfig = ConfigHandler.getInstance().getServerConfig();
    	
        ExecutorService pool = Executors.newFixedThreadPool(serverConfig.getClientPoolMax());
        try (ServerSocket server = new ServerSocket(serverConfig.getPort())) {
            logger.info("Accepting connections on port " + server.getLocalPort());
            while (true) {
                try {
                    Socket request = server.accept();
                    Runnable r = new RequestProcessor(request);
                    pool.submit(r);
                } catch (IOException ex) {
                    logger.error("Error accepting connection", ex);
                }
            }
        }
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("server running error", e);
		}
	}
}