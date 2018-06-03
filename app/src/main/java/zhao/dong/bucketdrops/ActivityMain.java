package zhao.dong.bucketdrops;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import zhao.dong.bucketdrops.adapters.AdapterDrops;
import zhao.dong.bucketdrops.beans.Drop;
import zhao.dong.bucketdrops.widgets.BucketRecyclerView;

public class ActivityMain extends AppCompatActivity {

    Toolbar mToolbar;
    BucketRecyclerView mRecycler;
    Realm mRealm;
    RealmResults<Drop> mResults;
    View mEmptyView;
    AdapterDrops mAdapter;

    private RealmChangeListener mChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            mAdapter.update(mResults);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRealm = Realm.getDefaultInstance();
        mResults = mRealm.where(Drop.class).findAllAsync();

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        initBackgroundImage();

        mEmptyView = findViewById(R.id.empty_drops);

        mRecycler = findViewById(R.id.rv_drops);
        mRecycler.hideIfEmpty(mToolbar);
        mRecycler.showIfEmpty(mEmptyView);

        mAdapter = new AdapterDrops(this, mResults);
        mRecycler.setAdapter(mAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(manager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mResults.addChangeListener(mChangeListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mResults.removeChangeListener(mChangeListener);
    }

    private void initBackgroundImage() {

        ImageView background = findViewById(R.id.iv_background);
        Glide.with(this).load(R.drawable.background).centerCrop().into(background);
    }

    public void showDialog(View view) {

        showDialogAdd();
    }

    private void showDialogAdd() {

        DialogAdd dialog = new DialogAdd();
        dialog.show(getSupportFragmentManager(), "Add");
    }
}