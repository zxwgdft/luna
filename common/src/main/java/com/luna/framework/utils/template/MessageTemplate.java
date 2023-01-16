package com.luna.framework.utils.template;

/**
 * 消息模板接口
 * @author TontoZhou
 *
 */
public interface MessageTemplate {
	/**
	 * 获取消息模板
	 * @return
	 */
	String getTemplate();
	
	/**
	 * 创建一个消息
	 * @return
	 */
	String createMessage(Object... args);
}
