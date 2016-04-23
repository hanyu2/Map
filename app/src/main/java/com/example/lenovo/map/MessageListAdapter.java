package com.example.lenovo.map;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Administrator on 2016/4/21.
 */
public class MessageListAdapter extends RecyclerView.Adapter {
    private List<MessageData> dataList;
    private RecyclerView recyclerView;

    public static interface OnRecyclerViewListener {
        void onItemClick(int position);
        boolean onItemLongClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final String TAG = MessageAdapter.class.getSimpleName();

    public MessageListAdapter(List<MessageData> dataList, RecyclerView recyclerView) {
        this.dataList = dataList;
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_list_item, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.height = recyclerView.getHeight() / 10;
        view.setLayoutParams(lp);
        return new MeesageListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        MeesageListItemViewHolder holder = (MeesageListItemViewHolder) viewHolder;
        holder.position = i;
        MessageData messageData = dataList.get(holder.position);
        String title = messageData.getTitle();
        String time = messageData.getTime();
        holder.titleTv.setText(title);
        holder.timeTv.setText(time);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MeesageListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        public View rootView;
        public TextView titleTv;
        public TextView timeTv;
        public int position;

        public MeesageListItemViewHolder(View itemView) {
            super(itemView);
            titleTv = (TextView) itemView.findViewById(R.id.message_list_item_title);
            timeTv = (TextView) itemView.findViewById(R.id.message_list_item_time);
            rootView = itemView.findViewById(R.id.message_list_item_layout);
            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(null != onRecyclerViewListener){
                return onRecyclerViewListener.onItemLongClick(position);
            }
            return false;
        }
    }

}
