package cc.lotuscard;


public class LotusCardDriver {

	// 寻卡请求类型 _eRequestType
	public final static int RT_ALL = 0x52; // /< 符合14443A卡片
	public final static int RT_NOT_HALT = 0x26; // /< 未进入休眠状态的卡
	// 密钥验证模式 _eAuthMode
	public final static int AM_A = 0x60; // /< 验证A密码
	public final static int AM_B = 0x61; // /< 验证B密码

	/*********************** 以下对象要外部要赋值 **************************************/
	// public static UsbDeviceConnection m_UsbDeviceConnection = null;
	// public static UsbEndpoint m_InEndpoint = null;
	// public static UsbEndpoint m_OutEndpoint = null;
	public static long m_nDeviceHandle = -1;
	public static LotusCardParam m_tLotusCardParam = null;
	public static LotusCardDriver m_LotusCardDriver = null;

	/*************************************************************/
	private LotusCardDriver() {
		m_tLotusCardParam = new LotusCardParam();
	}

	public static LotusCardDriver getInstance() {
		return new LotusCardDriver();
	}

	static {
		System.load("C:\\Users\\bobobod\\Desktop\\bishe\\毕设项目\\library-management-server\\LotusCardDriver.dll");
	}

	/**
	 * 打开设备
	 *
	 * @param strDeviceName      串口设备名称
	 * @param nVID               USB设备VID
	 * @param nPID               USB设备PID
	 * @param nUsbDeviceIndex    USB设备索引
	 * @param unRecvTimeOut      接收超时
	 * @param bUseExendReadWrite 是否使用外部读写通道 如果没有设备写权限时，可以使用外部USB或串口进行通讯，
	 *                           需要改造callBackProcess中相关代码完成读写工作 目前范例提供USB操作
	 * @return 设备句柄
	 */
	public native long OpenDevice(String strDeviceName, int nVID, int nPID,
								  int nUsbDeviceIndex, int unRecvTimeOut, boolean bUseExendReadWrite);

	/**
	 * 关闭设备
	 *
	 * @param nDeviceHandle 设备句柄
	 */
	public native void CloseDevice(long nDeviceHandle);

	/**
	 * 寻卡
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param nRequestType    请求类型
	 * @param tLotusCardParam 结果值 用里面的卡片类型
	 * @return true = 成功
	 */
	public native boolean Request(long nDeviceHandle, int nRequestType,
								  LotusCardParam tLotusCardParam);

	/**
	 * 防冲突
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param tLotusCardParam 结果值 用里面的卡号
	 * @return true = 成功
	 */
	public native boolean Anticoll(long nDeviceHandle,
								   LotusCardParam tLotusCardParam);

	/**
	 * 选卡
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param tLotusCardParam 参数(使用里面的卡号)与结果值(使用里面的卡容量大小)
	 * @return true = 成功
	 */
	public native boolean Select(long nDeviceHandle,
								 LotusCardParam tLotusCardParam);

	/**
	 * 密钥验证
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param nAuthMode       验证模式
	 * @param nSectionIndex   扇区索引
	 * @param tLotusCardParam 参数(使用里面的卡号)
	 * @return true = 成功
	 */
	public native boolean Authentication(long nDeviceHandle, int nAuthMode,
										 int nSectionIndex, LotusCardParam tLotusCardParam);

	/**
	 * 卡片中止响应
	 *
	 * @param nDeviceHandle 设备句柄
	 * @return true = 成功
	 */
	public native boolean Halt(long nDeviceHandle);

	/**
	 * 读指定地址数据
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param nAddress        块地址
	 * @param tLotusCardParam 结果值（读写缓冲）
	 * @return true = 成功
	 */
	public native boolean Read(long nDeviceHandle, int nAddress,
							   LotusCardParam tLotusCardParam);

	/**
	 * 写指定地址数据
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param nAddress        块地址
	 * @param tLotusCardParam 参数（读写缓冲）
	 * @return true = 成功
	 */
	public native boolean Write(long nDeviceHandle, int nAddress,
								LotusCardParam tLotusCardParam);

	/**
	 * 加值
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param nAddress      块地址
	 * @param nValue        值
	 * @return true = 成功
	 */
	public native boolean Increment(long nDeviceHandle, int nAddress, int nValue);

	/**
	 * 减值
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param nAddress      块地址
	 * @param nValue        值
	 * @return true = 成功
	 */
	public native boolean Decreament(long nDeviceHandle, int nAddress,
									 int nValue);

	/**
	 * 卡数据块传入卡的内部寄存器
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param nAddress      块地址
	 * @return true = 成功
	 */
	public native boolean Restore(long nDeviceHandle, int nAddress);

	/**
	 * 内部寄存器传入卡的卡数据块
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param nAddress      块地址
	 * @return true = 成功
	 */
	public native boolean Transfer(long nDeviceHandle, int nAddress);

	/**
	 * 装载密钥
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param nAuthMode       验证模式
	 * @param nSectionIndex   扇区索引
	 * @param tLotusCardParam 参数（密钥）
	 * @return true = 成功
	 */
	public native boolean LoadKey(long nDeviceHandle, int nAuthMode,
								  int nSectionIndex, LotusCardParam tLotusCardParam);

	/**
	 * 蜂鸣
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param nBeepLen      蜂鸣长度 毫秒为单位
	 * @return true = 成功
	 */
	public native boolean Beep(long nDeviceHandle, int nBeepLen);

	/**
	 * 发送指令 用于CPU卡
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param nTimeOut        超时参数
	 * @param tLotusCardParam 参数（指令缓冲,返回结果）
	 * @return true = 成功
	 */
	public native boolean SendCpuCommand(long nDeviceHandle, int nTimeOut,
										 LotusCardParam tLotusCardParam);

	/******************************** 以下函数调用上述函数，为了简化第三方调用操作 ***************************/
	/**
	 * 获取卡号 上位机简化
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param nRequestType    请求类型
	 * @param tLotusCardParam 结果值
	 * @return true = 成功
	 */
	public native boolean GetCardNo(long nDeviceHandle, int nRequestType,
									LotusCardParam tLotusCardParam);

	/**
	 * 获取卡号 MCU简化
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param nRequestType    请求类型
	 * @param ucBeepLen       蜂鸣长度 最长255毫秒
	 * @param ucUseHalt       使用中止 1=调用中止操作 0=不动作
	 * @param ucUseLoop       使用循环 1=读卡器内部循环获取卡号 获取到数据再返回 上位机接收超时后 应立即再次读取 0=读卡器内部只动作一次 *
	 * @param tLotusCardParam 结果值
	 * @return true = 成功
	 */
	public native boolean GetCardNoEx(long nDeviceHandle, int nRequestType,
									  byte ucBeepLen, byte ucUseHalt, byte ucUseLoop,
									  LotusCardParam tLotusCardParam);

	/**
	 * 初始值
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param nAddress      块地址
	 * @param nValue        值
	 * @return true = 成功
	 */
	public native boolean InitValue(long nDeviceHandle, int nAddress, int nValue);

	/**
	 * 修改密码AB
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param strPasswordA  密码A
	 * @param strPasswordB  密码B
	 * @return true = 成功
	 */
	public native boolean ChangePassword(long nDeviceHandle, int nSectionIndex,
										 String strPasswordA, String strPasswordB);

	/**
	 * 复位CPU卡
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param tLotusCardParam 结果值
	 * @return true = 成功
	 */
	public native boolean ResetCpuCard(long nDeviceHandle,
									   LotusCardParam tLotusCardParam);

	/**
	 * 复位CPU卡 此方法里面没有调用GetCardNo
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param tLotusCardParam 结果值
	 * @return true = 成功
	 */
	public native boolean ResetCpuCardNoGetCardNo(long nDeviceHandle,
												  LotusCardParam tLotusCardParam);

	/**
	 * 取消激活CPU卡 后续可以发送WUPA
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param tLotusCardParam 结果值
	 * @return true = 成功
	 */
	public native boolean DeselectCpuCard(long nDeviceHandle,
										  LotusCardParam tLotusCardParam);

	/**
	 * 发送指令 用于CPU卡 封装LotusCardSendCpuCommand
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param tLotusCardParam 参数（指令缓冲,返回结果）
	 * @return true = 成功
	 */
	public native boolean SendCOSCommand(long nDeviceHandle,
										 LotusCardParam tLotusCardParam);

	/**
	 * 获取银行卡卡号
	 *
	 * @param nDeviceHandle
	 * @param pBankCardNo
	 *            银行卡号 同印刷号码
	 * @param unBankCardNoLength
	 *            银行卡号长度
	 * @return true = 成功
	 */

	/**
	 * 获取银行卡卡号
	 *
	 * @param nDeviceHandle
	 * @return 银行卡卡号
	 */
	public native String GetBankCardNo(long nDeviceHandle);

	/**
	 * 读指定地址数据 一个指令就完成所有动作
	 *
	 * @param nDeviceHandle          设备句柄
	 * @param nRequestType           请求类型
	 * @param nAddress               块地址
	 * @param ucUseParameterPassWord 使用密码 1=使用参数密码 0 =使用设备内部密码
	 * @param ucBeepLen              蜂鸣长度 最长255毫秒
	 * @param ucUseHalt              使用中止
	 * @param tLotusCardParam        结果值（读写缓冲）
	 * @return true = 成功
	 */
	public native boolean ReadData(long nDeviceHandle, int nRequestType,
								   int nAddress, byte ucUseParameterPassWord, byte ucBeepLen,
								   byte ucUseHalt, LotusCardParam tLotusCardParam);

	/**
	 * 写指定地址数据
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param nAddress        块地址
	 * @param ucBeepLen       蜂鸣长度 最长255毫秒
	 * @param ucUseHalt       使用中止
	 * @param tLotusCardParam 参数（读写缓冲）
	 * @return true = 成功
	 */
	public native boolean WriteData(long nDeviceHandle, int nAddress,
									byte ucBeepLen, byte ucUseHalt, LotusCardParam tLotusCardParam);

	/****************************** 以下代码为IP通道相关代码 **********************************************/
	/**
	 * 读指定地址文本
	 *
	 * @param nSectionIndex 扇区索引
	 * @return 读取到的字串
	 */
	public native String ReadText(long nDeviceHandle, int nSectionIndex);

	/**
	 * 写指定地址文本
	 *
	 * @param nSectionIndex 扇区索引
	 * @param strTextInfo   写入文本字串
	 * @return true = 成功
	 */
	public native boolean WriteText(long nDeviceHandle, int nSectionIndex,
									String strTextInfo);

	/**
	 * 连接测试
	 *
	 * @param strServerIp     服务器IP地址
	 * @param nConnectTimeOut 超时us为单位
	 * @return true = 成功
	 */
	public native boolean ConnectTest(String strServerIp, int nConnectTimeOut);

	/**
	 * 获取错误编码
	 *
	 * @param nDeviceHandle 设备句柄
	 * @return 错误编码 详见枚举值定义
	 */
	public native int GetErrorCode(long nDeviceHandle);

	/**
	 * 获取二代证操作错误编码
	 *
	 * @param nDeviceHandle 设备句柄
	 * @return 错误编码 详见枚举值定义
	 */
	public native int GetTwoIdErrorCode(long nDeviceHandle);

	/**
	 * 获取错误信息
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param errCode       错误编码
	 */
	public native String GetErrorInfo(long nDeviceHandle, int errCode);

	/**
	 * 获取二代证错误信息
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param errCode       错误编码
	 */
	public native String GetTwoIdErrorInfo(long nDeviceHandle, int errCode);

	/**
	 * 设置卡片类型
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param cCardType     卡片类型 A='A'/'a' B='B'/'b' F='F'/'f' C='C'/'c'
	 * @return true = 成功
	 */
	public native boolean SetCardType(long nDeviceHandle, char cCardType);

	/**
	 * Felica寻卡
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param unTimerSlot     timer slot
	 * @param tLotusCardParam 参数（读写缓冲）
	 * @return true = 成功
	 */
	public native boolean FelicaPolling(long nDeviceHandle, int unTimerSlot,
										LotusCardParam tLotusCardParam);

	/**
	 * 读取NFC缓冲
	 *
	 * @param nDeviceHandle 设备句柄
	 * @return 缓冲字串
	 */
	public native String ReadNfcBuffer(long nDeviceHandle);

	/**
	 * 写入NFC缓冲
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param strNfcBuffer  缓冲地址
	 * @return true = 成功
	 */
	public native boolean WriteNfcBuffer(long nDeviceHandle, String strNfcBuffer);

	/******************************** 以下函数为type b操作函数 ***************************/
	/**
	 * 寻卡
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param nRequestType    请求类型
	 * @param tLotusCardParam 结果值 用里面的卡片类型
	 * @return true = 成功
	 */
	public native int RequestB(long nDeviceHandle, int nRequestType,
							   LotusCardParam tLotusCardParam);

	/**
	 * 选卡
	 *
	 * @param nDeviceHandle   设备句柄
	 * @param tLotusCardParam 参数(使用里面的卡号)与结果值(使用里面的卡容量大小)
	 * @return true = 成功
	 */
	public native int SelectB(long nDeviceHandle, LotusCardParam tLotusCardParam);

	/**
	 * 卡片中止响应
	 *
	 * @param nDeviceHandle 设备句柄
	 * @return true = 成功
	 */
	public native int HaltB(long nDeviceHandle);

	/**
	 * 获取设备号
	 *
	 * @param nDeviceHandle 设备句柄
	 * @return 读取到的字串
	 */
	public native String GetDeviceNo(long nDeviceHandle);

	/**
	 * 获取二代身份证卡片ID
	 *
	 * @param nDeviceHandle 设备句柄
	 * @return 读取到的字串
	 */
	public native String GetTwoGenerationIDCardNo(long nDeviceHandle);

	/**
	 * 设置射频开关
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param ucRfOnOff     1=打开射频信号 0= 关闭射频信号
	 * @return true = 成功
	 */
	public native boolean SetRfOnOff(long nDeviceHandle, byte ucRfOnOff);

	/**
	 * 获取二代证信息
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param tTwoIdInfo    参数 二代证信息结构体地址
	 * @return true = 成功
	 */
	public native boolean GetTwoIdInfo(long nDeviceHandle,
									   TwoIdInfoParam tTwoIdInfo);

	/**
	 * 通过网络获取二代证信息
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param strServerIp   参数 安全模块所在服务器IP
	 * @param tTwoIdInfo    参数 二代证信息结构体地址
	 * @return true = 成功
	 */
	public native boolean GetTwoIdInfoByServer(long nDeviceHandle,
											   String strServerIp, TwoIdInfoParam tTwoIdInfo);

	/**
	 * 通过网络获取二代证信息
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param strServerIp   参数 安全模块所在服务器IP
	 * @param unServerPort  参数 服务器端口
	 * @param tTwoIdInfo    参数 二代证信息结构体地址
	 * @param unRecvTimeOut 参数 接收超时 默认10秒
	 * @return true = 成功
	 */
	public native boolean GetTwoIdInfoByServerEx(long nDeviceHandle,
												 String strServerIp, long unServerPort, TwoIdInfoParam tTwoIdInfo,
												 long unRecvTimeOut);

	/**
	 * 通过网络获取二代证信息
	 *
	 * @param objContect          上下文环境对象实例 用于andorid
	 * @param objUserObject       用户对象实例 用于回调动作中
	 * @param nDeviceHandle       设备句柄
	 * @param strDispatchServerIp 参数 调度服务器IP
	 * @param nUserAccount        参数 用户账号 整形 便于服务器检索
	 * @param strPassWord         参数 登录调度服务器 密码
	 * @param tTwoIdInfo          参数 二代证信息结构体地址
	 * @param nPostcode           参数 邮政编码
	 * @param nLineType           参数 线路类型 0=未知 1=电信 2=联通 3=移动 4=其他
	 * @param unRecvTimeOut       参数 接收超时 默认10秒
	 * @param bIsLotusDevice      参数 LOTUS 设备 默认问false
	 * @return true = 成功
	 */
	public native boolean GetTwoIdInfoByMcuServer(Object objContect,
												  Object objUserObject, long nDeviceHandle,
												  String strDispatchServerIp, int nUserAccount, String strPassWord,
												  TwoIdInfoParam tTwoIdInfo, long nPostcode, int nLineType,
												  long unRecvTimeOut, boolean bIsLotusDevice);

	/**
	 * 通过网络获取二代证照片信息 WL格式输入
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param strServerIp   参数 安全模块所在服务器IP
	 * @param tTwoIdInfo    参数 二代证信息结构体地址
	 * @return true = 成功
	 */
	public native boolean WlDecodeByServer(long nDeviceHandle,
										   String strServerIp, TwoIdInfoParam tTwoIdInfo);

	/**
	 * 通过网络获取二代证信息 这个API用于网络环境比较糟糕的地方 内部有重试动作
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param strServerIp   参数 安全模块所在服务器IP
	 * @param unServePort   参数 服务器端口
	 * @param tTwoIdInfo    参数 二代证信息结构体地址
	 * @param unRecvTimeOut 参数 接收超时 默认10秒
	 * @return true = 成功
	 */
	public native boolean GetTwoIdInfoByWireless(long nDeviceHandle,
												 String strServerIp, long unServePort, TwoIdInfoParam tTwoIdInfo,
												 long unRecvTimeOut);

	/**
	 * 通过带PSAM单片机服务器网络获取二代证信息
	 *
	 * @param objContect    上下文环境对象实例 用于andorid
	 * @param objUserObject 用户对象实例 用于回调动作中
	 * @param nDeviceHandle 设备句柄
	 * @param strServerIp   参数 安全模块所在服务器IP
	 * @param unServePort   参数 服务器端口
	 * @param tTwoIdInfo    参数 二代证信息结构体地址
	 * @param unRecvTimeOut 参数 接收超时 默认10秒
	 * @return true = 成功
	 */
	public native boolean GetTwoIdInfoByPsamServer(Object objContect,
												   Object objUserObject, long nDeviceHandle, String strServerIp,
												   long unServePort, TwoIdInfoParam tTwoIdInfo, long unRecvTimeOut);

	/**
	 * 获取安全模块管理信息 可以视为编号
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param strServerIp   参数 安全模块所在服务器IP
	 * @param unServePort   参数 服务器端口
	 * @return 安全模块管理信息 可以视为编号
	 */
	public native String GetSamvManagerMessageByServer(long nDeviceHandle,
													   String strServerIp, long unServePort);

	/**
	 * 获取二代证服务器MCU MAC地址
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param strServerIp   参数 安全模块所在服务器IP
	 * @param unServePort   参数 服务器端口
	 * @return MAC地址
	 */
	public native byte[] GetTwoIdServerMacAddress(long nDeviceHandle,
												  String strServerIp, long unServePort);

	/**
	 * 获取二代证服务器MCU序列号
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param strServerIp   参数 安全模块所在服务器IP
	 * @param unServePort   参数 服务器端口
	 * @return MCU序列号
	 */
	public native byte[] GetTwoIdServerMcuSerialNo(long nDeviceHandle,
												   String strServerIp, long unServePort);

	/**
	 * 通过小模块 控制其他小模块电源
	 *
	 * @param nDeviceHandle     设备句柄
	 * @param strServerIp       参数 安全模块IP
	 * @param unServerPort      参数 安全模块端口 由于通过路由器中转 实际指向路由IP 和PORT
	 * @param nPowerOnOff       1 = 打开电源 0 = 关闭电源
	 * @param strConfigPassword 密码  只有密码核对通过才允许动作 避免被攻击
	 * @return >0 成功 <=0 失败
	 */
	public native int PowerOnOffOtherDevice(long nDeviceHandle, String strServerIp, long unServerPort,
											long nPowerOnOff, String strConfigPassword);

	/**
	 * 获取二代证用随机数
	 *
	 * @param arrRandomBuffer 参数 随机数缓冲
	 */
	public native void GetIdRandom(byte[] arrRandomBuffer);

	/**
	 * 保存日志
	 *
	 * @param strLogFile 参数 日志文件
	 * @param strLog     参数 日志内容
	 */
	public native void SaveLog(String strLogFile, String strLog);

	/**
	 * 获取USB设备数量
	 *
	 * @param nVID 参数 设备VID
	 * @param nPID 参数 设备PID
	 */
	public native int GetUsbDeviceCount(int nVID, int nPID);

	/**
	 * 获取MCU配置
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param arrMcuConfig  参数 MCU配置数据缓冲
	 * @return true = 成功
	 */
	public native boolean GetMcuConfig(long nDeviceHandle, byte[] arrMcuConfig);

	/**
	 * 获取ISP选项
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param arrIspOption  参数 选项数据缓冲
	 * @return true = 成功
	 */
	public native boolean GetIspOption(long nDeviceHandle, byte[] arrIspOption);

	/**
	 * 设置ISP选项
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param arrIspOption  参数 选项数据地址 0=禁止 非0=允许
	 * @return true = 成功
	 */
	public native boolean SetIspOption(long nDeviceHandle, byte[] arrIspOption);

	/**
	 * 获取ntag版本 21x支持
	 *
	 * @param nDeviceHandle        设备句柄
	 * @param arrNtagVersionBuffer 参数 版本信息缓冲
	 * @return true = 成功
	 */
	public native boolean NtagGetVersion(long nDeviceHandle,
										 byte[] arrNtagVersionBuffer);

	/**
	 * ntag密码验证 21x支持
	 *
	 * @param nDeviceHandle         设备句柄
	 * @param arrNtagPasswordBuffer 参数 密码缓冲 参数 密码缓冲长度 默认4字节
	 * @return true = 成功
	 */
	public native boolean NtagPwdAuth(long nDeviceHandle,
									  byte[] arrNtagPasswordBuffer);

	/**
	 * CPU卡根据名称选择文件或目录
	 *
	 * @param nDeviceHandle    设备句柄
	 * @param strDirOrFileName 文件或目录名
	 * @return 返回结果BUFFER
	 */
	public native byte[] CpuCardSelectByName(long nDeviceHandle,
											 String strDirOrFileName);

	/**
	 * CPU卡读取二进制文件
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param nSfi          文件标识
	 * @return 返回结果BUFFER
	 */
	public native byte[] CpuCardReadBinary(long nDeviceHandle, int nSfi);

	/**
	 * 单DES
	 *
	 * @param strInData 输入字符串
	 * @param strKey    密钥
	 * @param bDecrypt  TRUE = 解密 FALSE =加密
	 * @return true = 成功
	 */
	public native String CalcDes(String strInData, String strKey,
								 boolean bDecrypt);

	/**
	 * 3DES
	 *
	 * @param strInData 输入字符串
	 * @param strKey    密钥
	 * @param bDecrypt  TRUE = 解密 FALSE =加密
	 * @return true = 成功
	 */
	public native String Calc3Des(String strInData, String strKey,
								  boolean bDecrypt);

	/**
	 * MAC计算
	 *
	 * @param strInitString 初始字符串 PBOC是随机数 普通MAC是8个0字符串
	 * @param strInData     输入字符串
	 * @param strKey        密钥
	 * @return true = 成功
	 */
	public native String CalcMac(String strInitString, String strInData,
								 String strKey);

	/**
	 * 通过回调处理数据读写
	 *
	 * @param nDeviceHandle 设备句柄
	 * @param bRead         是否读操作
	 * @param arrBuffer     缓冲
	 * @return true = 操作成功
	 */
	public static boolean callBackProcess(long nDeviceHandle, boolean bRead,
										  byte[] arrBuffer) {
		int nResult = 0;
		boolean bResult = false;
		int nBufferLength = arrBuffer.length;
		int nWaitCount = 0;
		// if (null == m_UsbDeviceConnection)
		// return false;
		// if (null == m_OutEndpoint)
		// return false;
		// if (null == m_InEndpoint)
		// return false;
		// if (nBufferLength != 64)
		// return false;
		// if (true == bRead) {
		// arrBuffer[0] = 0;
		// while (true) {
		// nResult = m_UsbDeviceConnection.bulkTransfer(m_InEndpoint,
		// arrBuffer, nBufferLength, 3000);
		// if (nResult <= 0)
		// break;
		// if (arrBuffer[0] != 0)
		// break;
		// nWaitCount++;
		// if (nWaitCount > 1000)
		// break;
		// }
		// if (nResult == nBufferLength) {
		// bResult = true;
		// } else {
		// bResult = false;
		// }
		// } else {
		// nResult = m_UsbDeviceConnection.bulkTransfer(m_OutEndpoint,
		// arrBuffer, nBufferLength, 3000);
		// if (nResult == nBufferLength) {
		// bResult = true;
		// } else {
		// bResult = false;
		// }
		// }
		return bResult;
	}

	/**
	 * 通过回调完成第三方设备操作二代证
	 *
	 * @param arrBuffer 参数 与 结果都在其中 根据回调来的参数操作二代证并把结果填入其中 缓冲
	 * @return true = 操作成功
	 */
	public static boolean callBackExtendIdDeviceProcess(Object objUser,
														byte[] arrBuffer) {
		boolean bResult = false;
		byte bytTmp = 0;
		if (null == m_tLotusCardParam)
			return bResult;
		if (null == m_LotusCardDriver)
			return bResult;
		TwoIdInfoParam son = (TwoIdInfoParam) objUser;
		System.arraycopy(arrBuffer, 1, m_tLotusCardParam.getArrBuffer(), 0,
				arrBuffer[0]);
		m_tLotusCardParam.setNBufferSize(arrBuffer[0]);
		bResult = m_LotusCardDriver.SendCpuCommand(m_nDeviceHandle, 10,
				m_tLotusCardParam);
		if (false == bResult)
			return bResult;
		arrBuffer[0] = (byte) m_tLotusCardParam.getUnCosReultBufferLength();
		System.arraycopy(m_tLotusCardParam.getArrCosResultBuffer(), 0, arrBuffer, 1,
				arrBuffer[0]);

		return bResult;
	}

}

