package com.sxonecard.util;

/**
 * @author Administrator
 * 售货机
 */
public class clsHttpConst {
	/**
	 * 打开串口
	 */
	public static final int CMD_OPEN_SERIAL_PORT              =244;
	public static final int CMD_TO_VEDIO_VIEW              =243;
	public static final int CMD_TO_PRODUCT_LST              =242;
	public static final int CMD_TO_CFG_PAGE              =241;
	public static final int CMD_TO_SLOT_SET              =240;
	public static final int CMD_FRESH_IMAGE_INFO    =239;
	public static final int CMD_FRESH_QRCODE    =238;
	public static final int CMD_HEART_HTTP    =237;
	public static final int CMD_SEND_VENDER_INFO_HTTP    =236;
	public static final int CMD_TO_DEFAULT_FACE    =235;
	public static final int CMD_HEART_DATA_PARSE_HTTP    =234;
	public static final int CMD_REQUEST_TRADE = 233;
	public static final int CMD_TRANSFOR_OK = 232;
	
	/**
	 * 错误命令代码(有使用)
	 */
	public static final int CMD_PC_MES_ERR              =-1;
	/**
	 * 握手命令(有使用)
	 */
	public static final int CMD_PC_LNK                  =1001;
	/**
	 * 查询主板状态命令(有使用)
	 */
	public static final int CMD_PC_POLL                 =0X02;
	/**
	 * 获取投入钱币总额(没有使用)
	 */
	public static final int CMD_PC_CREDIT               =0X11;  /*获取投入钱币总额*/
	/**
	 * 设置和获取可收钱币状态(没有使用)
	 */
	public static final int CMD_PC_CASH_TYPE            =0x12;  /*设置和获取可收钱币状态*/    
	/**
	 * 找零(没有使用)
	 */
	public static final int CMD_PC_CASH_PAYOUT          =0x15;  /*找零*/
	/**
	 * 硬币筒余额查询(没有使用)
	 */
	public static final int CMD_PC_COIN_BALANCE         =0x16;  /*硬币筒余额查询*/
	/**
	 * 找零过程查询(没有使用)
	 */
	public static final int CMD_PC_CASH_PAYOUT_POLL     =0x17;/*找零过程查询*/
	/**
	 * 现金设备连接状态(没有使用)
	 */
	public static final int CMD_PC_MDB_DEVICE_LINK      =0x18;/*现金设备连接状态*/
	/**
	 * 清除投入金额(没有使用)
	 */
	public static final int CMD_PC_MDB_CLEAR_CREDIT     =0x19;/*清除投入金额*/
	/**
	 * 让暂存纸币压钱箱或者退出纸币器(没有使用)
	 */
	public static final int CMD_PC_MDB_BILL_ESCROW      =0x1a;
	/**
	 * MDB设备设置(没有使用)
	 */
	public static final int CMD_PC_MDB_SET              =0x1B;  
	/**
	 * 上位机从主板中获取参数(没有使用)
	 */
	public static final int CMD_PC_SLOT_TOPC            =0x30;
	/**
	 * 上位机通知下位机出货，上位机指定货道编号(有使用)
	 */
	public static final int CMD_PC_TRANSFOR             =0x34;
	
	/**
	 * 上位机通知下位机出货，上位机指定商品编号(有使用)
	 */
	public static final int CMD_PC_TRANSFOR_BY_GOODSID  =0x35;	
	/**
	 * 上位机得到下位机交易传递通知时，从下位机货取最近一次的交易信息(有使用)
	 */
	public static final int CMD_PC_SEND_TRADE           =1000;
	/**
	 * 上位机发送时间到下位机(有使用)
	 */
	public static final int CMD_PC_SYNC_TIME            =0x37;
	/**
	 * 上位机通知下位机，已经获取到下位机的上货信息(有使用)
	 */
	public static final int CMD_PC_RELOAD_OK            =0x38;
	/**
	 * 上位机通知下位机保存数据到flash(有使用)
	 */
	public static final int CMD_PC_WRT_SLOT             =0x39;    
	/**
	 * 上位机通过参数编号发送货道信息到下位机(有使用)
	 */
	public static final int CMD_PC_SEND_SLOT_BY_PARA_ID =0x3a;    
	/**
	 * 上位机从下位机获取货道参数(有使用)
	 */
	public static final int CMD_PC_GET_SLOT             =0x3b;
//	/**
//	 * 上位机获取货道列表(没有使用)
//	 */
//	public static final int CMD_PC_GET_SLOT_LST         =0x3C;
//	/**
//	 * 
//	 */
//	public static final int CMD_PC_CREDIT_CASH          =0X41;
//	/**
//	 * 
//	 */
//	public static final int CMD_PC_GET_KEY_VALUE        =0X42;
//	
	/**
	 * 下位机获取交易统计复位数(有使用)
	 */
	public static final int CMD_PC_GET_STATIC_DATA      =0X43;
//	
//	/**
//	 * 
//	 */
//	public static final int CMD_PC_GET_FIRM_VER         =0x80;/*获取固件版本*/
//	/**
//	 * 
//	 */
//	public static final int CMD_PC_UPDATE_FIRM          =0x81;/*更新固件程序*/
//	/**
//	 * 
//	 */
//	public static final int CMD_PC_UPDATE_SYS_DATA      =0x82;/*更新系统参数*/
//	/**
//	 * 
//	 */
//	public static final int CMD_PC_ENTER_UPDATE_MODE    =0x83;/*进入更新模式*/
//	/**
//	 * 
//	 */
//	public static final int CMD_PC_EXIT_UPDATE_MODE     =0x84;/*退出更新模式，进入到用户代码*/
	

}
