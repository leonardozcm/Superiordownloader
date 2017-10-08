package com.example.com.superiordownloader.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.superiordownloader.Information.FileInfo;
import com.example.com.superiordownloader.R;
import com.example.com.superiordownloader.Service.DownloadService;
import com.example.com.superiordownloader.UpdateReceiver;
import com.example.com.superiordownloader.Util.DataTransFormer;
import com.example.com.superiordownloader.Util.FileCleaner;
import com.example.com.superiordownloader.Util.FileOperator;
import com.example.com.superiordownloader.Util.ThreadOperator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by 59771 on 2017/10/1.
 */

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    public List<FileInfo> fileInfoList=new ArrayList<>();
    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageButton download_status;
        TextView download_name;
        ImageButton download_delete;
        ProgressBar download_progressbar;
        TextView download_progress;
        TextView download_speed;
        View mView;

        public ViewHolder (View view){
            super(view);
            mView=view;
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
        final ThreadOperator threadOperator=new ThreadOperator(parent.getContext());
       final FileOperator fileOperator=new FileOperator(parent.getContext());

         View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.download_item,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.download_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 发送广播停止下载
                int position=viewHolder.getAdapterPosition();
                FileInfo fileInfo=fileInfoList.get(position);
                Intent intent=new Intent(v.getContext(), DownloadService.class);
                intent.putExtra("fileInfo",fileInfo);

                FileOperator fileOperator=new FileOperator(v.getContext());
                List<FileInfo> fileinfolist=fileOperator.queryFiles(fileInfo.getUrl());
                int IsStop=fileinfolist.get(0).getIsStop();
                Log.d("onCreateViewHolder", "onClick: "+IsStop);

                if((IsStop%2)==0){
                    intent.setAction(DownloadService.ACTION_STOP);

                    v.getContext().startService(intent);
                    fileOperator.updateFileInfo(fileInfo.getUrl());
                    fileInfo.setSpeed(0);
                    //TODO 是否可以删除？
                    notifyDataSetChanged();
                }else{
                    intent.setAction(DownloadService.ACTION_START);
                    v.getContext().startService(intent);
                    fileOperator.updateFileInfo(fileInfo.getUrl());
                    //TODO 是否可以删除？
                    notifyDataSetChanged();
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

                Intent delete_notify=new Intent(v.getContext(), UpdateReceiver.class);
                delete_notify.putExtra("Action",DownloadService.ACTION_DELETE_NOTIFY);
                delete_notify.putExtra("fileInfo",fileInfo);
                v.getContext().sendBroadcast(delete_notify);

                File dir = new File(DownloadService.DownloadPath+fileInfo.getFileName());
                if (!FileCleaner.deleteDir(dir)) {
                    Toast.makeText(v.getContext(), "Already null", Toast.LENGTH_SHORT).show();
                }
                threadOperator.deleteThread(fileInfo.getUrl());
                fileOperator.deleteFileInfo(fileInfo.getUrl());
                notifyItemRemoved(position);
                fileInfoList.remove(position);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FileInfo fileInfo=fileInfoList.get(position);
        Log.d(TAG, "onBindViewHolder: POSTITION = "+position);

        FileOperator fileOperator=new FileOperator(holder.mView.getContext());
        List<FileInfo> mFileinfolist=fileOperator.queryFiles(fileInfo.getUrl());
        Log.d(TAG, "onBindViewHolder: "+fileInfo.getUrl());


        int IsStop=mFileinfolist.get(0).getIsStop();
        Log.d(TAG, "onBindViewHolder: mFileinfolist.size = "+mFileinfolist.size()+", and Isstop = "+IsStop);

        if((IsStop%2)==0){
            holder.download_status.setImageResource(R.drawable.ic_pause);
        }else{
            holder.download_status.setImageResource(R.drawable.ic_continue);
        }
        holder.download_name.setText(" "+fileInfo.getFileName());
        holder.download_speed.setText((int)fileInfo.getSpeed()+"kB/s");
        holder.download_progressbar.setProgress(fileInfo.getFinished());
        holder.download_progress.setText(DataTransFormer.ToString(((fileInfo.getFinished()/100.0)*fileInfo.getLength())/(1024.0*1024.0))+"MB/"+DataTransFormer.ToString(fileInfo.getLength()/(1024.0*1024.0))+"MB");
    }

    @Override
    public int getItemCount() {
        return fileInfoList.size();
    }

    /*
    更新UI
     */
    public void updataProgress(int file_id,int progress,double speed,int length){
        int pos=0;
        for (FileInfo fileinfo:fileInfoList) {
            if(fileinfo.getId()==file_id){
if(progress==100){
    fileInfoList.remove(pos);
    notifyItemRemoved(pos);
}else {
    fileinfo.setFinished(progress);
    fileinfo.setLength(length);
    fileinfo.setSpeed(speed);
    notifyDataSetChanged();
}
                break;
            }
            pos++;
        }

       // FileInfo info=fileInfoList.get(id);

    }
}
