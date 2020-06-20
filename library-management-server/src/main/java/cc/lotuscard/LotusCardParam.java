package cc.lotuscard;


import lombok.Data;

@Data
public class LotusCardParam {
	public LotusCardParam()
	{
		this.arrCardNo = new byte[8];
		this.arrBuffer = new byte[64];
		this.arrKeys = new byte[64];
		this.arrCosResultBuffer =  new byte[256];
		this.arrCosSendBuffer = new byte[256];
	}
	/**
	 * 卡片类型
	 */
	private int nCardType;
	/**
	 * 8字节卡号
	 */
	private byte[] arrCardNo;

	/**
	 * 卡片容量大小
	 */
	private int nCardSize;

	/**
	 * 读写缓冲
	 */
	private byte[] arrBuffer;

	/**
	 * 缓冲大小
	 */
	private int nBufferSize;
	/**
	 * 密钥
	 */
	private byte[] arrKeys;

	/**
	 * KEYs大小
	 *
	 */
	private int nKeysSize;
	/**
	 * pCosResultBuffer COS执行结果缓冲
	 */
	private byte[] arrCosResultBuffer;
	/**
	 * unCosReultBufferLength COS执行结果缓冲长度
	 */
	private int unCosReultBufferLength;

	/**
	 * pCosSendBuffer COS指令发送缓冲
	 */
	private byte[] arrCosSendBuffer;
	/**
	 * unCosSendBufferLength COS指令发送缓冲长度
	 */
	private int unCosSendBufferLength;
}
