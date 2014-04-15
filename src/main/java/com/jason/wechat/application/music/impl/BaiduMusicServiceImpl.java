package com.jason.wechat.application.music.impl;

import java.util.HashMap;

import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jason.framework.mapper.JsonMapper;
import com.jason.framework.util.EncodeUtils;
import com.jason.wechat.application.music.MusicService;
import com.jason.wechat.domain.message.resp.model.Music;
import com.jason.wechat.infrastruture.http.HttpRequester;
import com.jason.wechat.infrastruture.http.HttpRespons;


@Service
public class BaiduMusicServiceImpl implements MusicService {
	
	private static final Logger logger = LoggerFactory.getLogger(BaiduMusicServiceImpl.class);

	
	public Music searchMusic(String musicKey) {
		//1. 组装百度音乐搜索地址
		String requestUrl = "http://nnlife.duapp.com/baidumusic.php?key={musicKey}";

		requestUrl = requestUrl.replace("{musicKey}", EncodeUtils.urlEncode(musicKey));
		//2. 发请求查询，并获取返回结果
		HttpRespons httpRespons =  HttpRequester.sendGet(requestUrl, new HashMap<String, String>());
        String content = httpRespons.getContent();
        logger.info("contentCharset:"+httpRespons.getContentCharset()+",content:"+content);
        //3. 从返回结果中解析出Music
		Music music = parseMusic(content);
		return music;
	}

	/**
	 * 解析音乐参数
	 * 
	 * @param inputStream 百度音乐搜索API返回的输入流
	 * @return Music
	 */
	private static Music parseMusic(String json) {
		Music music = null;
		try {
            JsonNode rootNode = JsonMapper.readTree(json);// 读取Json
            
            JsonNode data = JsonMapper.path(rootNode, "data");
            
            JsonNode songList = JsonMapper.path(data, "songList");

            JsonNode song = songList.get(0);
            if(null != song){
            	String songName = JsonMapper.asText(song, "songName");
                String artistName = JsonMapper.asText(song, "artistName");
                String songLink = JsonMapper.asText(song, "songLink");
                String albumName = JsonMapper.asText(song, "albumName");
                
                music = new Music();
                music.setDescription(albumName);
                //删除&后面的url，只保留xcode参数 这里写得有点勉强
                //eg:http://yinyueshiting.baidu.com/data2/music/42766464/33350601205200128.mp3?xcode=7b7d61e6504f596ecb2643aec670b5cc1456a2ae94a9864f
                if(songLink.indexOf("&")>0){
                	songLink = songLink.substring(0, songLink.lastIndexOf("&"));
                }
                music.setHQMusicUrl(songLink);
                music.setMusicUrl(songLink);
                
                music.setTitle(artistName+" - "+songName);
                return music;
            }
            
		} catch (Exception e) {
			logger.error("parse music is error!", e);
		}
		return music;
	}
	
}