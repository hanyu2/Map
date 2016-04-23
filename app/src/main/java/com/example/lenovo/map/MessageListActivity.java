package com.example.lenovo.map;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {

    Bundle bundle;
    RecyclerView recyclerView;
    Toolbar toolbar;
    LinearLayoutManager mLayoutManager;
    MessageListAdapter messageListAdapter;
    List<MessageData> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        bundle = this.getIntent().getExtras();
        dataList = bundle.getParcelableArrayList("MessageList");

        toolbar = (Toolbar) this.findViewById(R.id.toolbar_list_message);
        final View.OnClickListener toolbar_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        toolbar.setNavigationOnClickListener(toolbar_listener);

        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerview_messageList);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        messageListAdapter = new MessageListAdapter(dataList,recyclerView);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isShowTop = false;
            boolean isShowBottom = false;

            // @Override
            public void onScrolled(int arg0, int arg1) {
                // TODO Auto-generated method stub
                if (mLayoutManager.findLastCompletelyVisibleItemPosition() == 99) {
                    if (!isShowTop) {
                        Toast.makeText(MessageListActivity.this, "滑动到底部",
                                Toast.LENGTH_SHORT).show();
                    }
                    isShowTop = true;

                } else {
                    isShowTop = false;
                }
                if (mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    if (!isShowBottom) {
                        Toast.makeText(MessageListActivity.this, "滑动到顶部",
                                Toast.LENGTH_SHORT).show();
                    }
                    isShowBottom = true;
                } else {
                    isShowBottom = false;
                }
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        messageListAdapter = new MessageListAdapter(dataList,recyclerView);

        MessageListAdapter.OnRecyclerViewListener rc = new MessageListAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                MessageData messageData = dataList.get(position);
                Intent intent = new Intent(MessageListActivity.this, MessageDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("MessageDetail",messageData);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        };
        messageListAdapter.setOnRecyclerViewListener(rc);
        recyclerView.setAdapter(messageListAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST));
    }
}
