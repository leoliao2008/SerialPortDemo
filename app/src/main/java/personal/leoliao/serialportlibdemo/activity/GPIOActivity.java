package personal.leoliao.serialportlibdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import personal.leoliao.serialportlibdemo.R;
import personal.leoliao.serialportlibdemo.StaticData;
import personal.leoliao.serialportlibdemo.model.GPIOModel;

public class GPIOActivity extends AppCompatActivity {
    private GPIOModel mGpioModel;
    private EditText edt_gpioPow;
    private EditText edt_gpioSpCtrl;
    private SharedPreferences mSharedPreferences;
    private String str_gpioPowPath;
    private String str_gpioSpCtrlPath;
    private boolean flag1;
    private boolean flag2;

    public static void start(Context context) {
        Intent starter = new Intent(context, GPIOActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpio);
        initView();
        initData();
        initListeners();

    }

    private void initView() {
        edt_gpioPow= (EditText) findViewById(R.id.activity_gpio_test_edt_gpio_pow_ctrl);
        edt_gpioSpCtrl= (EditText) findViewById(R.id.activity_gpio_test_edt_gpio_sp_ctrl);
    }

    private void initData() {
        mSharedPreferences=getSharedPreferences(StaticData.SHARED_PREFERENCES_NAME,MODE_PRIVATE);
        str_gpioPowPath=mSharedPreferences.getString(StaticData.GPIO_POW_CTRL,StaticData.GPIO_POW_CTRL_PATH);
        str_gpioSpCtrlPath=mSharedPreferences.getString(StaticData.GPIO_SP_CTRL,StaticData.GPIO_SP_CTRL_PATH);
        edt_gpioPow.setText(str_gpioPowPath);
        edt_gpioPow.setSelection(str_gpioPowPath.length());
        edt_gpioSpCtrl.setText(str_gpioSpCtrlPath);
        edt_gpioSpCtrl.setSelection(str_gpioSpCtrlPath.length());
        mGpioModel=new GPIOModel(str_gpioPowPath,str_gpioSpCtrlPath);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            handleException(e);
        }

    }

    private void initListeners() {
//        edt_gpioPow.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                str_gpioPowPath=s.toString();
//                edt_gpioPow.setText(str_gpioPowPath);
//                mSharedPreferences.edit().putString(StaticData.GPIO_POW_CTRL,str_gpioPowPath).apply();
//            }
//        });
//
//        edt_gpioSpCtrl.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                str_gpioSpCtrlPath=s.toString();
//                edt_gpioSpCtrl.setText(str_gpioSpCtrlPath);
//                mSharedPreferences.edit().putString(StaticData.GPIO_SP_CTRL,str_gpioSpCtrlPath).apply();
//            }
//        });

    }

    public void testPower(View view) {
        if(!flag1){
            try {
                mGpioModel.turnOnCdRadio();
            } catch (IOException e) {
                handleException(e);
            }
        }else {
            try {
                mGpioModel.turnOffCdRadio();
            } catch (IOException e) {
                handleException(e);
            }
        }
        flag1=!flag1;
    }

    public void testToggleSp(View view) {
        if(!flag2){
            try {
                mGpioModel.connectCdRadioToCPU();
            } catch (IOException e) {
                handleException(e);
            }
        }else {
            try {
                mGpioModel.connectCDRadioToBeidou();
            } catch (IOException e) {
                handleException(e);
            }
        }
        flag2=!flag2;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleException(Exception e){
        Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
    }
}
