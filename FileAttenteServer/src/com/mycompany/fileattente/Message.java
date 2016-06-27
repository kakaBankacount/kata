package com.mycompany.fileattente;

import java.util.Arrays;
import java.util.List;

public class Message {
	
	private MessageType type;
	
	private List<String> params;
	
	public Message(MessageType type) {
		this.type = type;
	}
	
	public Message(MessageType type, List<String> params) {
		this.type = type;
		this.params = params;
	}
	
	public String serialize() {
		StringBuilder sb = new StringBuilder();
		sb.append(type);
		if (params != null && params.size() > 0) {
			sb.append(":");
			for (int i = 0; i < params.size(); i ++) {
				if (i > 0) {
					sb.append(",");
				}
				sb.append(params.get(i));
			}
		}
		
		return sb.toString();
	}
	
	public static Message newMessage(String message) {
		
		Message socketMessage = null;
		
		if (message != null) {
			String[] sections = message.split(":");
			if (sections != null && sections.length > 0) {
				String commandType = sections[0];
				
				MessageType commandTypeEnum = null;
				try {
					 commandTypeEnum = MessageType.valueOf(commandType);
				} catch(RuntimeException Exceptions) {
					return null;
				}
				
				if (sections.length > 1) {

					String paramSections = sections[1];

					List<String> paramList = null;
					if (paramSections != null) {
						String[] params = paramSections.split(",");
						if (params != null) {
							paramList = Arrays.asList(params);
						}
						socketMessage = new Message(commandTypeEnum, paramList);
					}
				} else {
					socketMessage = new Message(commandTypeEnum);
				}
				
				return socketMessage;
			}
		}
		
		return null;
	}

	public MessageType getType() {
		return type;
	}

	public List<String> getParams() {
		return params;
	}
}
