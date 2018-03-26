package personal.leoliao.serialportlibdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import personal.leoliao.serialportlibdemo.R;
import personal.leoliao.serialportlibdemo.presenter.MainActivityPresenter;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private Spinner mSpinner;
    private RadioGroup mRadioGroup;
    private Button btn_sendTest;
    private MainActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView= (ListView) findViewById(R.id.list_view);
        mSpinner= (Spinner) findViewById(R.id.spinner_view);
        mRadioGroup= (RadioGroup) findViewById(R.id.radio_group);
        btn_sendTest = (Button) findViewById(R.id.btn_test);

        mPresenter=new MainActivityPresenter(this);
        mPresenter.init();

        initListeners();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    private void initListeners() {
        btn_sendTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mPresenter.sendMessageToPort("Hello World!");
                mPresenter.sendTestMessage("AT+RM=2");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_main_to_gpio_test:
                GPIOActivity.start(this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public ListView getListView() {
        return mListView;
    }

    public Spinner getSpinner() {
        return mSpinner;
    }

    public RadioGroup getRadioGroup() {
        return mRadioGroup;
    }

    public Button getBtn_sendTest() {
        return btn_sendTest;
    }
}
