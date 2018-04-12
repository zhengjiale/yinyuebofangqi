package com.example.zjl.musicplayer;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by ZJL on 2018/4/7.
 */

public class MusicListFragment extends ListFragment {
    public List<Music>musicList;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        musicList=getMusicList();
        if(musicList==null||musicList.size()==0){
            musicList=MusicUtils.getMusicData(getActivity());
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState){
        Constants.musiclist=musicList;
        if(musicList!=null){
            setListAdapter(new MusicAdapter());
        }else{
            Toast.makeText(getActivity(),"存储卡中没有音乐",Toast.LENGTH_SHORT).show();
        }
        Intent intent =new Intent(getActivity(),MusicService.class);
        getActivity().startService(intent);
        return super.onCreateView(inflater,container,saveInstanceState);
    }
    public void onStart(){
        registerForContextMenu(getListView());
        super.onStart();
    }
    private class MusciAdapter extends BaseAdapter{
        public int getCount(){
            return musicList.size();
        }
        public Object getItem(int position){
            return musicList.get(position);
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position,View convertView,ViewGroup parent){
            if(convertView==null){
                convertView= LinearLayout.inflate(getActivity(),R.layout.music_item,null);
            }
            ImageView icon=(ImageView)convertView.findViewById(R.id.icon);
            TextView title=(TextView)convertView.findViewById(R.id.title);
            TextView artist=(TextView)convertView.findViewById(R.id.artist);
            TextView time=(TextView)convertView.findViewById(R.id.time);
            Bitmap bitmap=MusicUtils.getAlbumPic(getActivity(),musicList.get(position));
            if(bitmap!=null){
                icon.setImageBitmap(bitmap);
            }else{
                icon.setImageResource(R.drawable.music);
            }
            title.setText(musicList.get(position).getTitle());
            artist.setText(musicList.get(position).getSinger());
            time.setText(MusicUtils.timeToString(musicList.get(position).getTime()));
            return convertView;
        }
    }
    public List<Music>getMusicList(){
        return musicList;
    }
    public void setMusicList(List<Music>musicList){
        this.musicList=musicList;
    }
    public void onListItemClick(ListView l,View v,int position,long id){
        Intent intent=new Intent(getActivity(),MusicPlayActivtiy.class);
        intent.putExtra("listType",Constants.ALL_MUSIC);
        intent.putExtra("music",musicList.get(position));
        intent.putExtra("position",position);
        startActivity(intent);
        super.onListItemClick(l,v,position,id);
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        getActivity().getMenuInflater().inflate(R.menu.musiclist_context,menu);
        super.onCreateContextMenu(menu,v,menuInfo);
    }
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.setToBell:
                setRing(musicList.get(info.position));
                break;
            case R.id.addToPlayList:
                Music music=musicList.get(info.position);
                int i=0;
                for(;i<Constants.playlist.size();i++){
                    if(Constants.playlist.get(i).getTitile().equalsIgnoreCase(music.getTitle())){
                        break;
                    }
                }
                if(i== Constants.playlist.size()){
                    Constants.playlist.add(music);
                }
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
    public void setRing(Music music){
        ContentValues values=new ContentValues();
        values.put(MediaStore.MediaColumns.DATA,music.getUrl());
        values.put(MediaStore.MediaColumns.MIME_TYPE,"audio/mp3");
        values.put(MediaStore.Audio.Media.IS_RINGTONE,true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION,true);
        values.put(MediaStore.Audio.Media.IS_ALARM,false);
        values.put(MediaStore.Audio.Media.IS_MUSIC,false);
        Uri uri=MediaStore.Audio.Media.getContentUriForPath(music.getUrl());
        Uri newUri=getActivity().getContentResolver().insert(uri,values);
        RingtoneManager.setActualDefaultRingtoneUri(getActivity(),RingtoneManager.TYPE_RINGTONE,newUri);
    }
}
