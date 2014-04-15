package com.jason.wechat.application.music;

import com.jason.wechat.domain.message.resp.model.Music;


/**
 * 百度音乐搜索API操作类
 * @author Jason
 * @data 2014-4-10 下午09:49:41
 */
public interface MusicService {

	/**
	 * 根据关键字搜索
	 * @param musicKey 音乐关键字
	 * @return
	 */
	Music searchMusic(String musicKey);
}