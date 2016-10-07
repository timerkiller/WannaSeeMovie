package com.timekiller.wannaseemovie.ui.fragment;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.timekiller.wannaseemovie.R;
import com.timekiller.wannaseemovie.ui.fragment.CommunityFragment.OnListFragmentInteractionListener;
import com.timekiller.wannaseemovie.ui.fragment.dummy.CommunityDummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyCommunityRecyclerViewAdapter extends RecyclerView.Adapter<MyCommunityRecyclerViewAdapter.ViewHolder> {

    private final String mTag = "MyCommunityAdapter";
    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyCommunityRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_community, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);
        Uri uri = Uri.parse("http://image18.poco.cn/mypoco/myphoto/20160906/16/178343444201609061642092927346408251_000.jpg");
        if(holder.mSimpleDraweeView == null){
            Log.e(mTag,"mSimpleDraweeView is null");
        }
        else {
            Log.e(mTag,"mSimpleDraweeView is not null");
            holder.mSimpleDraweeView.setImageURI(uri);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);

                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
//        public final TextView mIdView;
//        public final TextView mContentView;
        public final SimpleDraweeView mSimpleDraweeView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            mIdView = (TextView) view.findViewById(R.id.id);
//            mContentView = (TextView) view.findViewById(R.id.content);
            mSimpleDraweeView = (SimpleDraweeView)view.findViewById(R.id.simple_draweeview_community);
        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}
