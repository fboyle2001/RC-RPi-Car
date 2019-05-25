package com.finlay.rc.connection.incoming;

import java.util.HashMap;
import java.util.Map;

import com.finlay.rc.handlers.RequestType;
import com.google.gson.Gson;

public class JSONIncomingMessage {

	private static final Gson GSON = new Gson();
	
	private String authKey;
	private JSONIncomingRequest request;
	
	public JSONIncomingMessage() {}
	
	public JSONIncomingMessage(JSONIncomingRequest request) {
		this.authKey = "console_host";
		this.request = request;
	}

	public String getAuthKey() {
		return authKey;
	}
	
	public JSONIncomingRequest getRequest() {
		return request;
	}
	
	public String toJSON() {
		return GSON.toJson(this);
	}
	
	public static class Builder {
		
		private RequestType type;
		private Map<String, Object> data = new HashMap<>();;
		
		public Builder setType(RequestType type) {
			this.type = type;
			return this;
		}
		
		public Builder addDataEntry(String key, Object value) {
			data.put(key, value);
			return this;
		}
		
		public JSONIncomingMessage build() {
			JSONIncomingRequest request = new JSONIncomingRequest(type.getType(), data);
			return new JSONIncomingMessage(request);
		}
		
	}
	
}
