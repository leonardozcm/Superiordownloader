package com.example.com.superiordownloader;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.com.superiordownloader.Information.FileInfo;
import com.example.com.superiordownloader.Information.ThreadInfo;
import com.example.com.superiordownloader.Service.DownloadService;
import com.example.com.superiordownloader.Util.FileCleaner;
import com.example.com.superiordownloader.Util.UrlNameGeter;
import com.example.com.superiordownloader.adapter.ViewPagerAdapter;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private TabLayout layoutTab;
    private ViewPager viewPagerTab;
    private List<Fragment> fragmentList;
    private String[] Tablist = new String[]{"正在下载", "已完成"};
    private ViewPagerAdapter viewPagerAdapter;
    private DoingFragment doingFragment;
    private DoneFragment doneFragment;
    private String url;
    public UIRecive mRecive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*
        载入view视图
         */
        initView();
        initData();
        /*
        注册广播
         */
        mRecive = new UIRecive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.ACTION_UPDATE);
        intentFilter.addAction(DownloadService.ACTION_FINISHED);
        intentFilter.addAction(DownloadService.ACTION_START);
        registerReceiver(mRecive, intentFilter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mRecive);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.add:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                final View dialogview = layoutInflater.inflate(R.layout.tap_url, (ViewGroup) findViewById(R.id.tap_url));
                final EditText editText = (EditText) dialogview.findViewById(R.id.get_url);
                url=editText.getText().toString();
                builder.setView(dialogview);
                builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        }else if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                        } else {
                                  try{
                                      Intent intent = new Intent(MainActivity.this, DownloadService.class);
                                      intent.setAction(DownloadService.ACTION_START);

                                      int max = 0;
                                      max = DataSupport.max(FileInfo.class, "id", int.class);
                                      FileInfo fileInfo = new FileInfo(max + 1, url, UrlNameGeter.get(url), 0, 0);
                                      DbOperator.insertFile(fileInfo);

                                      intent.putExtra("fileInfo", fileInfo);
                                      startService(intent);
                                      Log.d("MainActivity", "onClick: add Intent send");
                                      doingFragment.mFileInfoList.add(fileInfo);
                                      doingFragment.fileAdapter.notifyItemInserted(0);
                                  }catch (SecurityException e){
                                      e.printStackTrace();
                                  }

                        }
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                break;
            case R.id.delete:
                DataSupport.deleteAll(ThreadInfo.class);
                DataSupport.deleteAll(FileInfo.class);
                File dir = new File(DownloadService.DownloadPath);
                if (!FileCleaner.deleteDir(dir)) {
                    Toast.makeText(MainActivity.this, "FALSE", Toast.LENGTH_SHORT).show();
                }
                break;
           }
            return true;
        }


    private void initView() {
        layoutTab = (TabLayout) findViewById(R.id.layoutTab);
        viewPagerTab = (ViewPager) findViewById(R.id.viewpagerTab);
        fragmentList = new ArrayList<>();

        doingFragment = new DoingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param1", String.valueOf(0));
        doingFragment.setArguments(bundle);
        fragmentList.add(doingFragment);

        doneFragment = new DoneFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("param1", String.valueOf(1));
        doneFragment.setArguments(bundle2);
        fragmentList.add(doneFragment);
    }

    private void initData() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Tablist, fragmentList);
        viewPagerTab.setAdapter(viewPagerAdapter);
        viewPagerTab.setOffscreenPageLimit(3);
        viewPagerTab.addOnPageChangeListener(this);
        layoutTab.setupWithViewPager(viewPagerTab);
        layoutTab.setTabsFromPagerAdapter(viewPagerAdapter);
    }

    /*
    viewPage三兄弟
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //广播接收器
    public class UIRecive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Mainactivity", "onReceive: RECEIVED INTENT"+intent.getAction());
            Log.d("Mainactivity", "Is ACTION_UPDATE?"+Boolean.toString(DownloadService.ACTION_UPDATE.equals(intent.getAction())));
            if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {
                Log.d("Mainactivity", "onReceive: Update Intent from Task");
                int finished = (int)intent.getLongExtra("finished", 0);
                double speed = intent.getDoubleExtra("speed", 0.0);
                int fileinfo_id = intent.getIntExtra("fileinfo_id", 0);
                int length=intent.getIntExtra("length",0);
                Log.d("Mainactivity", "onReceive: id="+fileinfo_id+",speed="+speed+",finished="+finished+".");
                doingFragment.fileAdapter.updataProgress(fileinfo_id, finished, speed,length);
                Log.d("Mainactivity", "Ask fileAdapter to update");
            } else if (DownloadService.ACTION_FINISHED.equals(intent.getAction())) {
                // 下载结束的时候
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
                int fileinfo_id = intent.getIntExtra("fileinfo_id", 0);
                int length=intent.getIntExtra("length",0);
                doingFragment.fileAdapter.updataProgress(fileinfo_id, 100, 0,length);
                Toast.makeText(MainActivity.this, "下载完毕", Toast.LENGTH_SHORT).show();
            } else if (DownloadService.ACTION_START.equals(intent.getAction())) {
                Toast.makeText(MainActivity.this, "下载开始", Toast.LENGTH_SHORT).show();
            }
        }

    }
@Override
    public void onRequestPermissionsResult(int requestcode,String[] permissions,int[] grantResults){
    switch (requestcode){
        case 1:
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                try{
                    Intent intent = new Intent(MainActivity.this, DownloadService.class);
                    intent.setAction(DownloadService.ACTION_START);

                    int max = 0;
                    max = DataSupport.max(FileInfo.class, "id", int.class);
                    FileInfo fileInfo = new FileInfo(max + 1, url, UrlNameGeter.get(url), 0, 0);
                    DbOperator.insertFile(fileInfo);

                    intent.putExtra("fileInfo", fileInfo);
                    startService(intent);
                    Log.d("MainActivity", "onClick: add Intent send");
                    doingFragment.mFileInfoList.add(fileInfo);
                    doingFragment.fileAdapter.notifyItemInserted(0);
                }catch (SecurityException e){
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(this,"permission denied",Toast.LENGTH_SHORT).show();
            }
            break;
        default:break;
    }
}
}
