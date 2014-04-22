package com.jason.wechat.infrastruture.util;


import java.io.Writer;
import java.util.regex.Pattern;

import com.jason.wechat.domain.message.resp.RespMusicMessage;
import com.jason.wechat.domain.message.resp.RespNewsMessage;
import com.jason.wechat.domain.message.resp.RespTextMessage;
import com.jason.wechat.domain.message.resp.model.Article;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * Message 响应 
 * @author Jason
 * @data 2014-4-14 下午02:46:05
 */
public class MessageUtils {

    /**
     * 文本消息对象转换成xml
     *
     * @param textMessage 文本消息对象
     * @return xml string
     */
    public static String textMessageToXml(RespTextMessage textMessage) {
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }

    /**
     * 音乐消息对象转换成xml
     *
     * @param musicMessage 音乐消息对象
     * @return xml string
     */
    public static String musicMessageToXml(RespMusicMessage musicMessage) {
        xstream.alias("xml", musicMessage.getClass());
        return xstream.toXML(musicMessage);
    }

    /**
     * 图文消息对象转换成xml
     *
     * @param newsMessage 图文消息对象
     * @return xml string
     */
    public static String newsMessageToXml(RespNewsMessage newsMessage) {
        xstream.alias("xml", newsMessage.getClass());
        xstream.alias("item", new Article().getClass());
        return xstream.toXML(newsMessage);
    }

	/**
	 * 扩展xstream，使其支持CDATA块
	 * 如果是整数或浮点数 就不要加[CDATA[]了
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对那些xml节点的转换增加CDATA标记 true增加 false反之
				boolean cdata = false;
				
				@Override
				public void setValue(String text) {
					if (text != null && !"".equals(text)) {
						// 浮点型判断
						Pattern patternInt = Pattern.compile("[0-9]*(\\.?)[0-9]*");
						// 整型判断
						Pattern patternFloat = Pattern.compile("[0-9]+");
						// 如果是整数或浮点数 就不要加[CDATA[]了
						if (patternInt.matcher(text).matches()
								|| patternFloat.matcher(text).matches()) {
							cdata = false;
						} else {
							cdata = true;
						}
					}
					super.setValue(text);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
}
