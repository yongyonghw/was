package config;

public class Const {
	
	public final static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	
	public enum ErrorCode  {
		SERVER_INTERNAL_ERROR(500), NOT_FOUND(404), FORBIDDEN(403);
		int code;
		private ErrorCode(int code) {
			this.code = code;
		}
		public int getCode() {
			return code;
		}
	}
	
}
