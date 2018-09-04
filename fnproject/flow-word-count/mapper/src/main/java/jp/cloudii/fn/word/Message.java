package jp.cloudii.fn.word;

import java.io.Serializable;
import java.util.Map;

public class Message {
	public static class MapperRequest implements Serializable {
		public String url;

		public MapperRequest() {}
		public MapperRequest(String url) {
			this.url = url;
		}
	}

	public static class MapperResponse implements Serializable {
		public Map<String, Integer> count;
		public String errorMessage;

		public MapperResponse() {}
		public MapperResponse(Map<String, Integer> count, String errorMessage) {
			this.count = count;
			this.errorMessage = errorMessage;
		}
	}
}
