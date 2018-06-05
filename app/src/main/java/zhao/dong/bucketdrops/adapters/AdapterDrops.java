package zhao.dong.bucketdrops.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;
import zhao.dong.bucketdrops.MarkListener;
import zhao.dong.bucketdrops.R;
import zhao.dong.bucketdrops.beans.Drop;
import zhao.dong.bucketdrops.extras.Util;

public class AdapterDrops extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeListener {

    public static final int ITEM = 0;
    public static final int FOOTER = 1;

    private Realm mRealm;
    private LayoutInflater mInflater;
    private RealmResults<Drop> mResults;

    private AddListener mAddListener;
    private MarkListener mMarkListener;

    public AdapterDrops(Context context, Realm realm, RealmResults<Drop> results, MarkListener markListener) {

        mInflater = LayoutInflater.from(context);
        mRealm = realm;
        update(results);
        mMarkListener = markListener;
    }

    public void update(RealmResults<Drop> results) {

        mResults = results;
        notifyDataSetChanged();
    }

    public void setAddListener(AddListener listener) {

        mAddListener = listener;
    }

    @Override
    public int getItemViewType(int position) {

        if (mResults == null || position < mResults.size()) {
            return ITEM;
        } else {
            return FOOTER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == FOOTER) {
            View view = mInflater.inflate(R.layout.footer, parent, false);
            return new FooterHolder(view);
        } else {
            View view = mInflater.inflate(R.layout.row_drop, parent, false);
            return new DropHolder(view, mMarkListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof DropHolder) {
            DropHolder dropHolder = (DropHolder) holder;
            Drop drop = mResults.get(position);
            dropHolder.setWhat(drop.getWhat());
            dropHolder.setWhen(drop.getWhen());
            dropHolder.setBackground(drop.isCompleted());
        }
    }

    @Override
    public int getItemCount() {

        if (mResults == null || mResults.isEmpty()) {
            return 0;
        } else {
            return mResults.size() + 1;
        }
    }

    @Override
    public void onSwipe(int position) {

        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).deleteFromRealm();
            mRealm.commitTransaction();
            notifyItemRemoved(position);
        }
    }

    public void markComplete(int position) {

        if (position < mResults.size()) {
            mRealm.beginTransaction();
            mResults.get(position).setCompleted(true);
            mRealm.commitTransaction();
            notifyItemChanged(position);
        }
    }

    public static class DropHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTextWhat;
        TextView mTextWhen;
        MarkListener mMarkListener;
        Context mContext;
        View mItemView;

        public DropHolder(View itemView, MarkListener listener) {
            super(itemView);

            mItemView = itemView;
            mContext = itemView.getContext();
            mTextWhat = itemView.findViewById(R.id.tv_what);
            mTextWhen = itemView.findViewById(R.id.tv_when);
            mMarkListener = listener;

            itemView.setOnClickListener(this);
        }

        public void setWhat(String what) {

            mTextWhat.setText(what);
        }

        public void setWhen(long when) {

            mTextWhen.setText(DateUtils.getRelativeTimeSpanString(when, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL));
        }

        @Override
        public void onClick(View v) {

            mMarkListener.onMark(getAdapterPosition());
        }

        public void setBackground(boolean completed) {

            Drawable drawable;

            if (completed) {
                drawable = ContextCompat.getDrawable(mContext, R.color.bg_drop_complete);
            } else {
                drawable = ContextCompat.getDrawable(mContext, R.drawable.bg_row_drop);
            }

            Util.setBackground(mItemView, drawable);
        }
    }

    public class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button mBtnAdd;

        public FooterHolder(View itemView) {
            super(itemView);

            mBtnAdd = itemView.findViewById(R.id.btn_footer);
            mBtnAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mAddListener.add();
        }
    }
}