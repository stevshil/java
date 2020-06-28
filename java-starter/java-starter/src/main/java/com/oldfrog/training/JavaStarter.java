package com.oldfrog.training;

public class JavaStarter {

	public static void main(String[] args) {
		MessageManager manager = new MessageManager();
		System.out.println(manager.getMessage());
	}
}

class MessageManager {
	public String getMessage() {
		return "Hello";
	}
}
