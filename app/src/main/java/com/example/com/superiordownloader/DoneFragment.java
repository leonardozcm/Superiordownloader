package com.example.com.superiordownloader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.com.superiordownloader.Adapter.DoneFileAdapter;
import com.example.com.superiordownloader.Information.FileInfo;
import com.example.com.superiordownloader.Util.FileOperator;

import java.util.List;

public class DoneFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
 public DoneFileAdapter mDoneFileAdapter;
    public List<FileInfo> mFileInfoList;

    public DoneFragment() {
        // Required empty public constructor
    }

    public static DoneFragment newInstance(String param1, String param2) {
        DoneFragment fragment = new DoneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       view=inflater.inflate(R.layout.fragment_done, container, false);

        FileOperator fileOperator=new FileOperator(view.getContext());
        mFileInfoList=fileOperator.queryFilesCompleted();
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.done_list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mDoneFileAdapter=new DoneFileAdapter(mFileInfoList);
        recyclerView.setAdapter(mDoneFileAdapter);
        return view;
    }

}
