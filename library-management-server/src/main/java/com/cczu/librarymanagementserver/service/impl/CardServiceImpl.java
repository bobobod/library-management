package com.cczu.librarymanagementserver.service.impl;

import com.cczu.librarymanagementserver.common.DeviceUtils;
import com.cczu.librarymanagementserver.service.CardService;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService {
	@Override
	public Boolean write(String str) {
		return DeviceUtils.write(str);
	}

	@Override
	public Boolean write(Integer str) {
		return DeviceUtils.write(str);
	}

	@Override
	public String read() {
		return DeviceUtils.read(true);
	}
	@Override
	public String readNtg() {
		return DeviceUtils.read(false);
	}
}
