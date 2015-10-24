package me.lake.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import me.lake.live4danmaku.extra.SimpleTextDanmaku;
import me.lake.live4danmaku.widget.DanmakuSurfaceView;

public class MainActivity extends Activity {
    DanmakuSurfaceView danmakuSurfaceView;
    EditText et_danmaku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        danmakuSurfaceView = (DanmakuSurfaceView) findViewById(R.id.danmakuSurfaceView);
        danmakuSurfaceView.showFPS(true);
        et_danmaku = (EditText) findViewById(R.id.et_danmaku);
        findViewById(R.id.btn_fire).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = et_danmaku.getText().toString();
                SimpleTextDanmaku danmaku = new SimpleTextDanmaku(text, 0);
                danmakuSurfaceView.addDanmaku(danmaku);
            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
