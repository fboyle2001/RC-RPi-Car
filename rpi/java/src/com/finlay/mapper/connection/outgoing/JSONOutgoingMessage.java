package com.finlay.mapper.connection.outgoing;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.finlay.mapper.connection.outgoing.lookup.CodeMessageLookup;
import com.finlay.mapper.handlers.RequestType;
import com.google.gson.Gson;

public class JSONOutgoingMessage {
	
	private static final Gson gson = new Gson();
	
	private JSONOutgoingStatus status;
	private JSONOutgoingContext context;
	private Map<String, Object> content;
	
	private JSONOutgoingMessage(JSONOutgoingStatus status, JSONOutgoingContext context, Map<String, Object> content) {
		this.status = status;
		this.context = context;
		this.content = content;
	}
	
	public JSONOutgoingStatus getStatus() {
		return status;
	}
	
	public JSONOutgoingContext getContext() {
		return context;
	}
	
	public Map<String, Object> getContent() {
		return content;
	}
	
	public String toJson() {
		return gson.toJson(this);
	}
	
	public static class Builder {
		
		private int code;
		private String message;
		private int specific;
		private int requestType;
		private Map<String, Object> content;
		
		public Builder() {
			this.code = -1;
			this.message = null;
			this.specific = 0;
			this.requestType = -1;
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
		
		public Builder setRequestType(int requestType) {
			this.requestType = requestType;
			return this;
		}

		public Builder setRequestType(RequestType requestType) {
			return setRequestType(requestType.getType());
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
			
			return new JSONOutgoingMessage(new JSONOutgoingStatus(code, message, specific), 
					new JSONOutgoingContext(requestType != -1, requestType),
					content);
		}
		
	}
	
}
