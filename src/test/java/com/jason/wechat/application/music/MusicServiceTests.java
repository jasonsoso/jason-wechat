package com.jason.wechat.application.music;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jason.wechat.AbstractTestBase;
import com.jason.wechat.domain.message.resp.model.Music;



public class MusicServiceTests extends AbstractTestBase {

	@Autowired
	private MusicService musicService;
	@Test
	public void testSearchMusic() throws Exception {
		
		Music music = musicService.searchMusic("刘德华");
		System.out.println(music);
		
		System.out.println("音乐名称：" + music.getTitle());
		System.out.println("音乐描述：" + music.getDescription());
		System.out.println("普通品质链接：" + music.getMusicUrl());
		System.out.println("高品质链接：" + music.getHQMusicUrl());
		
	}
}
