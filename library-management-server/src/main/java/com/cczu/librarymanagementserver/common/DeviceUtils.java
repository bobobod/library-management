package com.cczu.librarymanagementserver.common;

import cc.lotuscard.LotusCardDriver;
import cc.lotuscard.LotusCardParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 智能卡读写工具类
 */
public class DeviceUtils {
	private static LotusCardDriver mLotusCardDriver = LotusCardDriver.getInstance();


	private static final Logger logger = LoggerFactory.getLogger(DeviceUtils.class);

	public static Boolean write(Object strTextInfo) {
		long nDeviceHandle = mLotusCardDriver.OpenDevice("", 0, 0, 0, 0, false);
		if (nDeviceHandle != -1) {
			LotusCardParam lotusCardParam = new LotusCardParam();
			// 设置30s后不会写卡
			long currentTime = System.currentTimeMillis();
			long timeline = 0; // 统计时间片
			while (!mLotusCardDriver.GetCardNo(nDeviceHandle, LotusCardDriver.RT_ALL, lotusCardParam)) {
				long checkTime = System.currentTimeMillis();
				timeline = checkTime - currentTime;
				if (timeline >= 30 * 1000) break;
			}
			if (timeline < 30 * 1000) {
				mLotusCardDriver.Beep(nDeviceHandle, 20);
				Boolean flag = false;
				if (strTextInfo instanceof String) {
					if (mLotusCardDriver.Authentication(nDeviceHandle, LotusCardDriver.AM_A, 1, lotusCardParam)) {
						flag = mLotusCardDriver.WriteText(nDeviceHandle, 1, (String) strTextInfo);
					}
				} else if(strTextInfo instanceof Integer) {
					Integer str = (Integer) strTextInfo;
					flag = mLotusCardDriver.WriteText(nDeviceHandle, 5, str.toString());
				}
				mLotusCardDriver.CloseDevice(nDeviceHandle);
				return flag;
			} else {
				System.out.println("超时");
				logger.debug("超时连接");
				mLotusCardDriver.CloseDevice(nDeviceHandle);

				return false;
			}
		}
		logger.debug("无可用设备");

		return false;
	}

	public static String read(Boolean check) {
		long nDeviceHandle = mLotusCardDriver
				.OpenDevice("", 0, 0, 0, 0, false);
		String str = null;
		if (nDeviceHandle != -1) {
			LotusCardParam lotusCardParam1 = new LotusCardParam();
			// 设置30s后不会读卡
			long currentTime = System.currentTimeMillis();
			long timeline = 0; // 统计时间片
			while (!mLotusCardDriver.GetCardNo(nDeviceHandle, LotusCardDriver.RT_ALL, lotusCardParam1)) {
				long checkTime = System.currentTimeMillis();
				timeline = checkTime - currentTime;
				if (timeline >= 30 * 1000) break; 
			}
			if (timeline < 30 * 1000) {
				mLotusCardDriver.Beep(nDeviceHandle, 20);
				if (check) {
					if (mLotusCardDriver
							.Authentication(nDeviceHandle, LotusCardDriver.AM_A, 1, lotusCardParam1)) {
						str = mLotusCardDriver.ReadText(nDeviceHandle, 1);
					}
				} else {
					str = mLotusCardDriver.ReadText(nDeviceHandle, 5);
				}
				mLotusCardDriver.CloseDevice(nDeviceHandle);
			} else {
				logger.debug("超时连接");
				System.out.println("超时");
				mLotusCardDriver.CloseDevice(nDeviceHandle);
			}
		} else {
			logger.debug("无可用设备");
		}
		return str;
	}

	public static void main(String[] args) {
		Integer a = 1343;
	//	System.out.println(DeviceUtils.read(true));
		System.out.println(DeviceUtils.read(false));
//		System.out.println(DeviceUtils.write(a));
	}
}
