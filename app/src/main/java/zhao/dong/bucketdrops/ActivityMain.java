package zhao.dong.bucketdrops;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import zhao.dong.bucketdrops.adapters.AdapterDrops;
import zhao.dong.bucketdrops.adapters.AddListener;
import zhao.dong.bucketdrops.adapters.CompleteListener;
import zhao.dong.bucketdrops.adapters.Divider;
import zhao.dong.bucketdrops.adapters.SimpleTouchCallback;
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

    private AddListener mAddListener = new AddListener() {
        @Override
        public void add() {
            showDialogAdd();
        }
    };

    private MarkListener mMarkListener = new MarkListener() {
        @Override
        public void onMark(int position) {
            showDialogMark(position);
        }
    };

    private CompleteListener mCompleteListener = new CompleteListener() {
        @Override
        public void onComplete(int position) {
            mAdapter.markComplete(position);
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
        mRecycler.addItemDecoration(new Divider(this, LinearLayoutManager.VERTICAL));
        mRecycler.hideIfEmpty(mToolbar);
        mRecycler.showIfEmpty(mEmptyView);

        mAdapter = new AdapterDrops(this, mRealm, mResults, mMarkListener);
        mAdapter.setAddListener(mAddListener);
        mRecycler.setAdapter(mAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(manager);

        SimpleTouchCallback callback = new SimpleTouchCallback(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecycler);
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

    private void showDialogMark(int position) {

        DialogMark dialog = new DialogMark();
        Bundle bundle = new Bundle();
        bundle.putInt("POSITION", position);
        dialog.setArguments(bundle);
        dialog.setCompleteListener(mCompleteListener);
        dialog.show(getSupportFragmentManager(), "Mark");
    }
}