package com.finlay.mapper.connection.outgoing;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.finlay.mapper.connection.outgoing.lookup.CodeMessageLookup;
import com.google.gson.Gson;

public class JSONOutgoingMessage {
	
	private static final Gson gson = new Gson();
	
	private JSONOutgoingStatus status;
	private Map<String, Object> content;
	
	private JSONOutgoingMessage(JSONOutgoingStatus status, Map<String, Object> content) {
		this.status = status;
		this.content = content;
	}
	
	public JSONOutgoingStatus getStatus() {
		return status;
	}
	
	public Map<String, Object> getContent() {
		return content;
	}
	
	public String getString(String key) {
		return content.get(key).toString();
	}
	
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(getString(key));
	}
	
	public int getInteger(String key) throws NumberFormatException {
		return Integer.parseInt(getString(key));
	}
	
	public double getDouble(String key) throws NumberFormatException {
		return Double.parseDouble(getString(key));
	}
	
	public String toJson() {
		return gson.toJson(this);
	}
	
	public static class Builder {
		
		private int code;
		private String message;
		private int specific;
		private Map<String, Object> content;
		
		public Builder() {
			this.code = -1;
			this.message = null;
			this.specific = 0;
			this.content = new HashMap<>();
		}
		
		public Builder setStatusCode(int code) {
			this.code = code;
			return this;
		}
		
		public Builder setStatusCode(CodeMessageLookup lookupItem) {
			this.code = lookupItem.getCode();
			return this;
		}
		
		public Builder setStatusMessage(String message) {
			this.message = message;
			return this;
		}
		
		public Builder setStatusSpecific(int specific) {
			this.specific = specific;
			return this;
		}
		
		public Builder addContent(Map<String, Object> content) {
			for(Entry<String, Object> entry : content.entrySet()) {
				setKeyValue(entry);
			}
			
			return this;
		}
		
		public Builder setKeyValue(Entry<String, Object> entry) {
			setKeyValue(entry.getKey(), entry.getValue());
			return this;
		}
		
		public Builder setKeyValue(String key, Object value) {
			content.put(key, value);
			return this;
		}
		
		public JSONOutgoingMessage build() {
			if(code == -1) {
				throw new RuntimeException("Status code must be set");
			}
			
			if(specific != 0) {
				message = CodeMessageLookup.getSpecific(code).getMessage(specific);
			}
			
			if(message == null) {
				message = CodeMessageLookup.getDefaultMessage(code);
			}
			
			return new JSONOutgoingMessage(new JSONOutgoingStatus(code, message, specific), content);
		}
		
	}
	
}
