package com.cczu.librarymanagementserver.service;

public interface CardService {
	Boolean write(String str);
	Boolean write(Integer str);
	String read();
	String readNtg();
}
