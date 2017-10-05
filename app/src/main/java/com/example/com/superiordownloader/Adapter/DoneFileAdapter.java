package com.example.com.superiordownloader.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.superiordownloader.Information.FileInfo;
import com.example.com.superiordownloader.R;
import com.example.com.superiordownloader.Service.DownloadService;
import com.example.com.superiordownloader.Util.DbOperator;
import com.example.com.superiordownloader.Util.FileCleaner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 59771 on 2017/10/5.
 */

public class DoneFileAdapter extends RecyclerView.Adapter<DoneFileAdapter.ViewHolder> {
    public List<FileInfo> mFileInfoList=new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        ImageView done_icon;
        TextView done_name;
        ImageButton done_delete;

        public ViewHolder(View view){
            super(view);
            mView=view;
            done_name=(TextView)view.findViewById(R.id.done_name);
            done_icon=(ImageView)view.findViewById(R.id.done_icon);
            done_delete=(ImageButton)view.findViewById(R.id.done_delete);
        }
    }

    public DoneFileAdapter(List<FileInfo> list){
        mFileInfoList=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.done_item,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 打开文件逻辑
            }
        });
        viewHolder.done_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=viewHolder.getAdapterPosition();
                FileInfo fileInfo=mFileInfoList.get(position);
                DbOperator.deleteThread(fileInfo.getUrl());
                DbOperator.deleteFileInfo(fileInfo.getUrl());

                File dir = new File(DownloadService.DownloadPath+fileInfo.getFileName());
                if (!FileCleaner.deleteDir(dir)) {
                    Toast.makeText(v.getContext(), "Already null", Toast.LENGTH_SHORT).show();
                }
                notifyItemRemoved(position);
                mFileInfoList.remove(position);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FileInfo fileInfo=mFileInfoList.get(position);
        holder.done_name.setText(fileInfo.getFileName());
    }

    @Override
    public int getItemCount() {
        return mFileInfoList.size();
    }


}
