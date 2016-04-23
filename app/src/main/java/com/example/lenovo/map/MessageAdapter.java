package com.example.lenovo.map;

/**
 * Created by Administrator on 2016/4/19.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageAdapter extends RecyclerView.Adapter {
    private String nick_name;
    private String title;
    private String tag;
    private String message;
    private String time;
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

    public MessageAdapter(String name, String title, String tag, String time, RecyclerView recyclerView) {
        this.nick_name = name;
        this.title = title;
        this.tag = tag;
        this.time = time;
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.height = recyclerView.getHeight() / getItemCount();
        view.setLayoutParams(lp);
        return new MeesageItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        MeesageItemViewHolder holder = (MeesageItemViewHolder) viewHolder;
        holder.position = i;
        switch (i){
            case 0:
                holder.titleTv.setText("name");
                holder.contentTv.setText(nick_name);
                break;
            case 1:
                holder.titleTv.setText("title");
                holder.contentTv.setText(title);
                break;
            case 2:
                holder.titleTv.setText("tag");
                holder.contentTv.setText(tag);
                break;
            case 3:
                holder.titleTv.setText("time");
                holder.contentTv.setText(time);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return 4;   //name,title,tag,time
    }

    class MeesageItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        public View rootView;
        public TextView titleTv;
        public TextView contentTv;
        public int position;

        public MeesageItemViewHolder(View itemView) {
            super(itemView);
            titleTv = (TextView) itemView.findViewById(R.id.message_item_title);
            contentTv = (TextView) itemView.findViewById(R.id.message_item_content);
            rootView = itemView.findViewById(R.id.message_item_layout);
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
