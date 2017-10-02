package com.example.com.superiordownloader.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.com.superiordownloader.DbOperator;
import com.example.com.superiordownloader.Information.FileInfo;
import com.example.com.superiordownloader.Information.ThreadInfo;
import com.example.com.superiordownloader.R;
import com.example.com.superiordownloader.Service.DownloadService;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by 59771 on 2017/10/1.
 */

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    //test
   static int n=0;



    private List<FileInfo> fileInfoList=new ArrayList<>();
    private ViewHolder mViewHolder;
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageButton download_status;
        TextView download_name;
        ImageButton download_delete;
        ProgressBar download_progressbar;
        TextView download_progress;
        TextView download_speed;

        public ViewHolder (View view){
            super(view);
            download_status=(ImageButton)view.findViewById(R.id.download_status);
            download_name=(TextView)view.findViewById(R.id.download_name);
            download_delete=(ImageButton)view.findViewById(R.id.download_delete);
            download_progressbar=(ProgressBar)view.findViewById(R.id.download_progressbar);
            download_progress=(TextView)view.findViewById(R.id.download_progress);
            download_speed=(TextView)view.findViewById(R.id.download_speed);
        }
    }

    public FileAdapter(List<FileInfo> list){
        fileInfoList=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.download_item,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.download_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 发送广播停止下载
                int position=viewHolder.getAdapterPosition();
                FileInfo fileInfo=fileInfoList.get(position);
                Intent intent=new Intent(v.getContext(), DownloadService.class);
                List<ThreadInfo> threadInfoList= DbOperator.queryThreads(fileInfo.getUrl());
                Log.d(TAG, "onClick:0");
                if(!threadInfoList.get(0).isStop()){
                    intent.setAction(DownloadService.ACTION_STOP);
                    intent.putExtra("fileInfo",fileInfo);
                    v.getContext().startService(intent);
                    DbOperator.updateThread(fileInfo.getUrl(),true);
                    viewHolder.download_status.setImageResource(R.drawable.ic_continue);
                    //TODO 是否可以删除？
                    notifyItemChanged(position);
                }else{
                    intent.setAction(DownloadService.ACTION_START);
                    intent.putExtra("fileInfo",fileInfo);
                    v.getContext().startService(intent);
                    DbOperator.updateThread(fileInfo.getUrl(),false);
                    viewHolder.download_status.setImageResource(R.drawable.ic_stop);
                    //TODO 是否可以删除？
                    notifyItemChanged(position);
                }

            }
        });
        viewHolder.download_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 删除下载任务
                int position=viewHolder.getAdapterPosition();
                FileInfo fileInfo=fileInfoList.get(position);
                Intent intent=new Intent(v.getContext(), DownloadService.class);

                    intent.setAction(DownloadService.ACTION_DELETE);
                    intent.putExtra("fileInfo",fileInfo);
                    v.getContext().startService(intent);
                fileInfoList.remove(position);
                  notifyItemRemoved(position);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mViewHolder=holder;
       FileInfo fileInfo=fileInfoList.get(position);
        holder.download_name.setText(fileInfo.getFileName());
        holder.download_speed.setText(fileInfo.getSpeed()+"kb/s");
        holder.download_progressbar.setProgress(fileInfo.getFinished());
        holder.download_progress.setText((double)(fileInfo.getFinished()/(1024*1024))+"/"+(double)(fileInfo.getLength()/(1024*1024))+"MB");
    }

    @Override
    public int getItemCount() {
        return fileInfoList.size();
    }

    /*
    更新UI
     */
    public void updataProgress(int file_id,int progress,double speed,int length){
        Log.d(TAG, "updataProgress: start to update UI");
        Log.d(TAG, Integer.toString(fileInfoList.size()));
        Log.d(TAG, "onReceive: file_id="+file_id+",speed="+speed+",finished="+progress+".");
        for (FileInfo fileinfo:fileInfoList) {
            if(fileinfo.getId()==file_id){
                Log.d(TAG, "updataProgress: found");
                fileinfo.setFinished(progress);
                fileinfo.setLength(length);
                fileinfo.setSpeed(speed);
                notifyDataSetChanged();
                break;
            }
            Log.d(TAG, "Search for next");
        }

       // FileInfo info=fileInfoList.get(id);

    }
}
