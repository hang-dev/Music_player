package com.zzu.middlemediaplayer0732;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocalMusicAdapter extends RecyclerView.Adapter<localMusicHolder> {
    Context mcontext;
    List<LocalMusicBean> mDatas;
    OnItemClickLister lister;
    public interface OnItemClickLister{
        void onClick(int pos);
    }

    public LocalMusicAdapter(Context mcontext,List<LocalMusicBean> mDatas,OnItemClickLister lister) {
        this.mcontext = mcontext;
        this.mDatas=mDatas;
        this.lister=lister;
    }

    @NonNull
    @Override
    public localMusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_music, parent, false);
        localMusicHolder holder = new localMusicHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull localMusicHolder holder, final int position) {
        LocalMusicBean musicBean = mDatas.get(position);
        holder.idTv.setText(musicBean.getId());
        holder.songTv.setText(musicBean.getSong());
        holder.singerTv.setText(musicBean.getSinger());
        holder.albumTv.setText(musicBean.getAlbum());
        holder.timeTv.setText(musicBean.getDuration());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   lister.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
  class localMusicHolder extends RecyclerView.ViewHolder{
    TextView idTv,songTv,singerTv,albumTv,timeTv;
      public localMusicHolder(@NonNull View itemView) {
          super(itemView);
          idTv=itemView.findViewById(R.id.item_local_music_num);
          songTv=itemView.findViewById(R.id.item_local_music_song);
          singerTv=itemView.findViewById(R.id.item_local_music_singer);
          albumTv=itemView.findViewById(R.id.item_local_music_album);
          timeTv=itemView.findViewById(R.id.item_local_music_durtion);
      }
  }
