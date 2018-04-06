package com.example.zjl.musicplayer;

import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TabHost mTabHost;
    private String[] titles=new String[]{"艺术家","音乐","专辑","播放列表"};
    private  String []tags=new String[]{"artist","music","album","playlist"};
    private int []icons=new int []{R.drawable.music,R.drawable.artist,R.drawable.album,R.drawable.playlist};
    private MyOpenHelper mHelper;
    private SQLiteDatabase mDatabase;
    //private MyOpenHelper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initData();
        mTabHost=(TabHost)findViewById(R.id.mTabHost);
        mTabHost.setup();
        for(int i=0;i<titles.length;i++)
        {
            TabHost.TabSpec tabSpec=mTabHost.newTabSpec(tags[i]);
            View view=getLayoutInflater().inflate(R.layout.tab,null);
            TextView titleVIew=(TextView)view.findViewById(R.id.title);
            ImageView iconView=(ImageView)view.findViewById(R.id.icon);
            titleVIew.setText(titles[i]);
            iconView.setImageResource(icons[i]);
            tabSpec.setIndicator(view);
            tabSpec.setContent(R.id.realContent);
            mTabHost.addTab(tabSpec);
        }
        mTabHost.setOnTabChangedListener(new MyTabChangeListener());
        mTabHost.setCurrentTab(1);
    }
    public void initData(){
        mHelper=new MyOpenHelper(this,"music",null,1);
        mDatanbase=mHelper.getWritableDatabase();
        Constants.playlist=MusicUtils.getDataFromDB(mDatabase);
    }
    private class MyTabChangeListener implements TabHost.OnTabChangeListener{
        public void onTabChanged(String tabTag){
            FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
            if(tabTag.equalsIgnoreCase("music")){
                MusicListFragment musicListFragment=new MusicListFragment();
                musicListFragment.setMusicList(null);
                fragmentTransaction.replace(R.id.realContent,musicListFragment);
            }else if(tabTag.equalsIgnoreCase("artist")){
                fragmentTransaction.replace(R.id.realContent,new ArtistListFragment());
            }else if(tabTag.equalsIgnoreCase("album")){
                fragmentTransaction.replace(R.id.realContent,new AlbuListFragment());
            }else if(tabTag.equalsIgnoreCase("playlist")){
                fragmentTransaction.replace(R.id.realContent,new PlayListFragment());
            }
            fragmentTransaction.commit();

        }
    }
    protected void onDestroy(){
        mDatabase.execSQL("delete from music_tb");
        for(int i=0;i<Constants.playlist.size();i++){
            Music music=Constants.playlist.get(i);
            mDatabase.execSQL("insert into music_tb(title,artist,album,album_id,time,url)values(?,?,?,?,?,?)",new String[]{
                    music.getTitle(),music.getSinger(),music.getAlbum(),music.getAlbum_id()+"",music.getTime()+"",music.getUrl()});
        }
        super.onDestroy();
    }
}
