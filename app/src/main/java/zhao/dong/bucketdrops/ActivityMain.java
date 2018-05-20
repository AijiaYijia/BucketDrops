package zhao.dong.bucketdrops;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class ActivityMain extends AppCompatActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        initBackgroundImage();
    }

    private void initBackgroundImage() {

        ImageView background = findViewById(R.id.iv_background);
        Glide.with(this).load(R.drawable.background).centerCrop().into(background);
    }

    public void showDialog(View view) {

        Toast.makeText(ActivityMain.this, "Button was clicked", Toast.LENGTH_SHORT).show();
    }
}