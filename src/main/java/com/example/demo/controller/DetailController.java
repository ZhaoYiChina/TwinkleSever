package com.example.demo.controller;


import com.example.demo.entity.*;
import com.example.demo.entity.result.ResultEntity;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

@Controller
public class DetailController {

    @Autowired
    private UserService userService;
    @Autowired
    private SongService songService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private SongListService songListService;
    @Autowired
    private AutoShowUtil showUtil;
    @Autowired
    private SingerService singerService;

    @RequestMapping(value ="/Song",method = RequestMethod.GET)
    public String songDetail(@RequestParam("songid") String songid, Map<String, Object> map,HttpServletRequest request){
        Song song = songService.getSongById(songid);
        ResultEntity e = songService.getSingersInSong(songid);
        ArrayList<Singer> singers = (ArrayList<Singer>)e.getObject();
        ArrayList<Comment> comments = songService.getCommentsInSong(songid);
        ArrayList<User> users = new ArrayList<>();
        for(Comment comment:comments){
            e = userService.getUserById(comment.getUserID());
            users.add((User) e.getObject());
        }
        Album album = albumService.getAlbumByAlbumId(song.getAlbumid());
        map.put("song",song);
        map.put("singers",singers);
        map.put("album",album);
        map.put("comments",comments);
        map.put("commentsUser",users);
        return "Details/song_detail";
    }
    @RequestMapping(value ="/SongList",method = RequestMethod.GET)
    public String songListDetail(@RequestParam("songlistid") String songlistid, Map<String, Object> map,HttpServletRequest request){
        SongList songList = songListService.getSongListById(songlistid);
        ResultEntity e = songListService.getSongsInSongList(songList);
        ArrayList<Song>songs = (ArrayList<Song>)e.getObject();
        String style = showUtil.getSongListStyle(songs);
        map.putAll(showUtil.showSong(songs));
        e = songListService.getSongListSavedNum(songList);
        int num = (int)e.getObject();
        e = userService.getUserById(songList.getUserid());
        User creater = (User)e.getObject();
        map.put("creater",creater);
        map.put("savenum",num);
        map.put("style",style);
        map.put("songlist",songList);
        return "Details/songlist_detail";
        //"songs" "singers" "singername" "albums"
    }
    @RequestMapping(value ="/Singer",method = RequestMethod.GET)
    public String singerDetail(@RequestParam("singerid") String singerid, Map<String, Object> map,HttpServletRequest request){
        Singer singer = singerService.getSingerById(singerid);
        ArrayList<Song> songs = singerService.getSingerSong(singerid);
        if(songs.size()>5){
            map.put("songs",songs.subList(0,4));
        }else {
            map.put("songs",songs);
        }
        ArrayList<Album> albums = singerService.getSingerAlbum(singerid);
        int follownum = singerService.getFansNum(singerid);
        map.put("albums",albums);
        map.put("singer",singer);
        map.put("follownum",follownum);
        return "/Details/singer_detail";
       //"songs" "albums" "follownum"
    }
    @RequestMapping(value ="/Album",method = RequestMethod.GET)
    public String albumDetail(@RequestParam("albumid") String songlistid, Map<String, Object> map,HttpServletRequest request){
        return "/Details/album_detail";
    }

}