package com.ikunkun.kunmusic.tools;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.ikunkun.kunmusic.R;
import com.ikunkun.kunmusic.comn.MusicInfo;

import java.io.ByteArrayOutputStream;
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
                //专辑封面
                int ID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);
//                    int album_Id =ID;
//                    String albumArt = getAlbumArt(album_Id);
//                    Bitmap bm = null;
//                    if (albumArt == null)
//                    {
//                        System.out.println("musicimageisnull");
////                        mImageView.setBackgroundResource(R.drawable.noalbum);
//                    }
//                    else
//                    {
//                        bm = BitmapFactory.decodeFile(albumArt);
//
//                        song.setBmpDraw(bmpDraw);
//                    }
                //歌曲路径
                song.setMusicPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
//              封面
                Bitmap musicimage=loadingCover(song.getMusicPath());
                if(musicimage==null){
                    System.out.println("musicimageisnull");
                }else {
                    BitmapDrawable bmpDraw = new BitmapDrawable(musicimage);
                    song.setBmp_pic(getBytes(musicimage));
                }
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
    /**
     * 加载封面
     * mediaUri MP3文件路径
     */
    private static Bitmap loadingCover(String mediaUri) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mediaUri);
        byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
        Bitmap bitmap = null;
        if (picture != null) {
            bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        }
        return bitmap;
    }

    public static byte[] getBytes(Bitmap bitmap){
        //实例化字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);//压缩位图
        return baos.toByteArray();//创建分配字节数组
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