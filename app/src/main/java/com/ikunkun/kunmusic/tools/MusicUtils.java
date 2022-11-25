package com.ikunkun.kunmusic.tools;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.comn.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 音乐扫描工具
 *
 */
public class MusicUtils {
    /**
     * 扫描系统里面的音频文件，返回一个list集合
     */

    static Context contexttemp;
    public static List<MusicInfo> getMusicData(Context context) {
        contexttemp=context;
        List<MusicInfo> list = new ArrayList<MusicInfo>();
        // 媒体库查询语句（写一个工具类MusicUtils）
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , null, null, null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MusicInfo song = new MusicInfo();
                //歌曲名称
                song.setMusicName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                //歌手
                song.setMusicSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                //专辑名
                int ID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
                    int album_Id =ID;
                    String albumArt = getAlbumArt(album_Id);
                    Bitmap bm = null;
                    if (albumArt == null)
                    {
                        System.out.println("musicimageisnull");
//                        mImageView.setBackgroundResource(R.drawable.noalbum);
                    }
                    else
                    {
                        bm = BitmapFactory.decodeFile(albumArt);
                        BitmapDrawable bmpDraw = new BitmapDrawable(bm);
                        song.setBmpDraw(bmpDraw);
                    }
                //歌曲路径
                song.setMusicPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                //歌曲时长
//                song.g = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                //歌曲大小
                song.setMusicSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));

                if (song.getMusicSize() > 1000 * 800) {
                    // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
                    if (song.getMusicName().contains("-")) {
                        String[] str = song.getMusicName().split("-");
                        song.setMusicSinger(str[0]);
                        song.setMusicName(str[1]);
                    }
                    list.add(song);
                }
            }
            // 释放资源
            cursor.close();
        }
        System.out.println("test2"+list.size());
        return list;
    }

    private static String getAlbumArt(int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cur =contexttemp.getContentResolver().query( Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),  projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        return album_art;
    }
}