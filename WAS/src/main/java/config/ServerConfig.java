package config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class ServerConfig {
	protected final Logger log = LoggerFactory.getLogger("ServerConfig");
	
	private int port;
	
	private String rootDirNm;
	
	private int clientPoolMax;
	
	private Map<String, HostConfig> httpRoot = new HashMap<>();
	
	public ServerConfig() {
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			URL url = cl.getResource("server.properties");
			System.out.println(url.getPath());
			InputStreamReader r = new InputStreamReader(url.openStream());
			configParse(r);
			log.info(toString());
			log.info("Complete");
		} catch (IOException e) {
			log.error("init error " + e);
		}
	}
	
	private void configParse(InputStreamReader r) {
		JsonReader reader = new JsonReader(r);
		JsonElement e = new JsonParser().parse(reader);
		JsonObject o = e.getAsJsonObject();
		
		this.port = o.get("port").getAsInt();
		this.clientPoolMax = o.get("clientPoolMax").getAsInt();
		this.rootDirNm = o.get("rootDirNm").getAsString();
		
		//httpRoot 
		JsonArray ar = o.get("HTTP_ROOT").getAsJsonArray();
		for(JsonElement je : ar) {
			JsonObject jo = je.getAsJsonObject();
			String host = jo.get("host").getAsString();
			String subDirNm = jo.get("subDirNm").getAsString();
			
			Map<Integer, String> errorViewMapped = new HashMap<>();
			//get Map<errorCode,ViewSrc>
			for(JsonElement er : jo.get("error").getAsJsonArray()) {
				errorViewMapped.put(er.getAsJsonObject().get("code").getAsInt(),
									er.getAsJsonObject().get("page").getAsString());
			}
			httpRoot.put(host, new HostConfig(host, subDirNm, errorViewMapped));
		}
	}
	
	
	public int getPort() {
		return port;
	}

	public String getRootDirNm() {
		return rootDirNm;
	}

	public int getClientPoolMax() {
		return clientPoolMax;
	}

	public Map<String, HostConfig> getHttpRoot() {
		return httpRoot;
	}

	public class HostConfig {
		final String host;
		final String subDirNm;
		final Map<Integer, String> errorViewMapped;
		
		private HostConfig(String host, String subDirNm, Map<Integer, String> errorViewMapped) {
			this.host = host;
			this.subDirNm = subDirNm;
			this.errorViewMapped = errorViewMapped;
		}
		public String getHost() {
			return host;
		}
		public String getSubDirNm() {
			return subDirNm;
		}
		public Map<Integer, String> getErrorViewMapped() {
			return errorViewMapped;
		}
		@Override
		public String toString() {
			return "HostConfig [host=" + host + ", subDirNm=" + subDirNm + ", errorViewMapped=" + errorViewMapped + "]";
		}
	}

	@Override
	public String toString() {
		return "ServerConfig [port=" + port + ", rootDirNm=" + rootDirNm + ", httpRoot=" + httpRoot.toString() + "]";
	}
}
