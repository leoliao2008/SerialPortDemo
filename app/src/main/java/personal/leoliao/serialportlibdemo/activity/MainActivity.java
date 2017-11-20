package personal.leoliao.serialportlibdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private Button btn_sendHelloWorld;
    private MainActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView= (ListView) findViewById(R.id.list_view);
        mSpinner= (Spinner) findViewById(R.id.spinner_view);
        mRadioGroup= (RadioGroup) findViewById(R.id.radio_group);
        btn_sendHelloWorld= (Button) findViewById(R.id.btn_send_hello_world);

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
        btn_sendHelloWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 2017/11/20
            }
        });
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

    public Button getBtn_sendHelloWorld() {
        return btn_sendHelloWorld;
    }
}
