package personal.leoliao.serialportlibdemo.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import personal.leoliao.serialportlibdemo.R;
import personal.leoliao.serialportlibdemo.StaticData;
import personal.leoliao.serialportlibdemo.activity.MainActivity;
import personal.leoliao.serialportlibdemo.model.SerialPortModel;
import project.SerialPort.SerialPort;

/**
 * Created by 廖华凯 on 2017/11/20.
 */

public class MainActivityPresenter {
    private MainActivity mActivity;
    private SerialPortModel mSerialPortModel;
    private SharedPreferences mSharedPreferences;
    private int mBrIndex;
    private int mSpIndex;
    private ArrayList<String> mList=new ArrayList<>();
    private ArrayAdapter<String> mAdapter;
    private RadioGroup mRadioGroup;
    private String[] mPortPaths;
    private String[] mBaudRates;
    private String mBaudRate;
    private String mPortPath;
    private SerialPort mSerialPort;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private Thread mThread;
    private Handler mHandler;
    private byte[] temp=new byte[256];

    public MainActivityPresenter(MainActivity activity) {
        mActivity = activity;
        mSerialPortModel=new SerialPortModel();
    }


    public void init() {
        mSharedPreferences=mActivity.getSharedPreferences(StaticData.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        mHandler=new Handler(mActivity.getMainLooper());
        initSpinnerView();
        initListView();
        initRadioGroup();
    }

    private void initRadioGroup() {
        mRadioGroup = mActivity.getRadioGroup();
        mPortPaths = mSerialPortModel.getSerialPortPaths();
        mPortPath=mSharedPreferences.getString(StaticData.CURRENT_SERIAL_PORT,"ttyAMA0");
        final RadioGroup.LayoutParams params=new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (String path: mPortPaths){
            RadioButton rb=new RadioButton(mActivity);
            rb.setText(path);
            rb.setLayoutParams(params);
            mRadioGroup.addView(rb);
        }
        final int childCount = mRadioGroup.getChildCount();
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                for(int i=0;i<childCount;i++){
                    RadioButton rb = (RadioButton) mRadioGroup.getChildAt(i);
                    if(rb.isChecked()){
                        mPortPath = rb.getText().toString();
                        openSerialPort(mPortPath,Integer.valueOf(mBaudRate));
                        break;
                    }
                }
            }
        });
    }

    private void openSerialPort(String portPath, Integer bdRate) {
        closeSerialPort();
        try {
            mSerialPort=mSerialPortModel.openSerialPort(portPath,bdRate);
            mInputStream = mSerialPort.getInputStream();
            mOutputStream = mSerialPort.getOutputStream();
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                     while (true){
                         if(mThread==null){
                             break;
                         }
                         if(mThread!=null&&mThread.isInterrupted()){
                             break;
                         }
                         try {
                             int available = mInputStream.available();
                             if(available>0){
                                 int len = mInputStream.read(temp);
                                 updateListView(new String(temp,0,len));
                             }
                         } catch (IOException e) {
                             updateListView(e.getMessage());
                             break;
                         }
                     }
                }
            });
            mThread.setPriority(Thread.MAX_PRIORITY);
            updateListView("串口"+portPath+"打开成功！");
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(StaticData.CURRENT_SERIAL_PORT,portPath);
            editor.putString(StaticData.CURRENT_BAUD_RATE,String.valueOf(bdRate));
            editor.apply();
            mThread.start();
        } catch (Exception e) {
            updateListView(e.getMessage());
        }
    }

    private void closeSerialPort() {
        if(mThread!=null){
            mThread.interrupt();
        }
        if(mInputStream!=null){
            try {
                mInputStream.close();
            } catch (IOException e) {
                updateListView(e.getMessage());
            } finally {
                mInputStream=null;
            }

        }
        if(mOutputStream!=null){
            try {
                mOutputStream.close();
            } catch (IOException e) {
                updateListView(e.getMessage());
            } finally {
                mOutputStream=null;
            }
        }
        if(mSerialPort!=null){
            mSerialPort.close();
            mSerialPort=null;
        }
    }

    private void initListView() {
        mAdapter = new ArrayAdapter<String>(
                mActivity,
                android.R.layout.simple_list_item_1,
                mList
        );
        mActivity.getListView().setAdapter(mAdapter);
    }

    private void updateListView(final String msg){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                int size = mList.size();
                if(size>=50){
                    mList.remove(0);
                }
                mList.add(msg);
                mAdapter.notifyDataSetChanged();
                mActivity.getListView().smoothScrollToPosition(Integer.MAX_VALUE);
            }
        });

    }

    private void initSpinnerView() {
        mBaudRates = mActivity.getResources().getStringArray(R.array.baud_rates_name);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                mActivity,
                android.R.layout.simple_spinner_item,
                mBaudRates
        ){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setGravity(Gravity.CENTER);
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mActivity.getSpinner().setAdapter(adapter);
        mBaudRate = mSharedPreferences.getString(StaticData.CURRENT_BAUD_RATE, "115200");
        mBrIndex=-1;
        for (int i = 0, len = mBaudRates.length; i<len; i++){
            if(mBaudRates[i].equals(mBaudRate)){
                mBrIndex=i;
                break;
            }
        }
        mActivity.getSpinner().setSelection(mBrIndex);
        mActivity.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBrIndex=position;
                mBaudRate=mBaudRates[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onStart() {

    }

    public void onStop() {
        if(mActivity.isFinishing()){
            closeSerialPort();
        }
    }

    public void sendMessageToPort(String msg) {
        if(mOutputStream!=null){
            try {
                mOutputStream.write(msg.getBytes());
            } catch (IOException e) {
                updateListView(e.getMessage());
            }
        }
    }
}
