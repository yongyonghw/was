package config;

public class ConfigHandler {
	
	final private ServerConfig serverConfig;
	
	private ConfigHandler() {
		this.serverConfig = new ServerConfig();
	}
	
	public static ConfigHandler getInstance(){
        return LazyHolder.INSTANCE;
    }
	
	private static class LazyHolder {
	    private static final ConfigHandler INSTANCE = new ConfigHandler();  
	  }
	
	public ServerConfig getServerConfig() {
		return serverConfig;
	}
}
