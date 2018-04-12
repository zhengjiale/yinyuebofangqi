package com.example.zjl.musicplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZJL on 2018/4/7.
 */

public class MusicUtils {
    public static List<Music>getMusicData(Context context){
        ContentResolver mResolver=context.getContentResolver();
        if(mResolver!=null){
            Cursor cursor=mResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER
            );
            return cursorToList(cursor,context);
        }
        return null;
    }
    public static List<Music>cursorToList(Cursor cursor,Context context){
        if(cursor==null||cursor.getCount()==0){
            return null;
        }
        List<Music>musicList=new ArrayList<Music>();
        while(cursor.moveToNext()){
            Music m=new Music();
            String title=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            if("<unknown>".equals(artist)){
                artist="未知艺术家";
            }
            String album=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            int album_id=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            long size =cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            int time=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String url=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            String name=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            String sub=name.substring(name.lastIndexOf(".")+1);
            if(sub.equals("mp3")&&time>50000){
                m.setTitle(title);
                m.setSinger(artist);
                m.setAlbum(album);
                m.setAlbum_id(album_id);
                m.setSize(size);
                m.setTime(time);
                m.setUrl(url);
                m.setName(name);
                musicList.add(m);
            }
        }
        cursor.close();
        return musicList;
    }
    public static String timeToString(int time){
        int temp=time/1000;
        int minute=temp/60;
        int second=temp%60;
        return String.format("%02d:02d",minute,second);
    }
    public static List<Music>getDataFromDB(SQLiteDatabase db){
        List<Music>musics=new ArrayList<Music>();
        Cursor cursor=db.rawQuery("select * from music_tb",null);
        if(cursor==null||cursor.getCount()==0){
            return musics;
        }
        while(cursor.moveToNext()){
            Music music=new Music();
            music.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            music.setSinger(cursor.getString(cursor.getColumnIndex("artist")));
            music.setAlbum(cursor.getString(cursor.getColumnIndex("album")));
            music.setAlbum_id(cursor.getInt(cursor.getColumnIndex("album_id")));
            music.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            music.setTime(cursor.getInt(cursor.getColumnIndex("time")));
            musics.add(music);
        }
        return musics;
    }
    public static Bitmap getAlbumPic(Context context,Music music){
        ContentResolver mResolver=context.getContentResolver();
        Uri uri= ContentUris.withAppendedId(Constants.ALBUM_URL,music.getAlbum_id());
        try{
            InputStream inputStream=mResolver.openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        }catch(FileNotFoundException ex){
            try{
                ParcelFileDescriptor pfd=mResolver.openFileDescriptor(uri,"r");
                if(pfd!=null){
                    FileDescriptor fd=pfd.getFileDescriptor();
                    Bitmap bitmap=BitmapFactory.decodeFileDescriptor(fd);
                    return bitmap;
                }
            }catch (Exception e){
                return null;
            }
            return null;
        }
    }
}
