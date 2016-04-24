package com.example.lenovo.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/22.
 */

public class ListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    MessageListAdapter messageListAdapter;
    ArrayList<MessageData> dataList = new ArrayList<MessageData>();;
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    public static ListFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ListFragment listFragment = new ListFragment();
        listFragment.setArguments(args);
        return listFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRecyclerView =
                (RecyclerView) inflater.inflate(R.layout.message_list_recyclerview, container, false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        messageListAdapter = new MessageListAdapter(dataList,mRecyclerView);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isShowTop = false;
            boolean isShowBottom = false;

            // @Override
            public void onScrolled(int arg0, int arg1) {
                // TODO Auto-generated method stub
                if (mLayoutManager.findLastCompletelyVisibleItemPosition() == 99) {
                    if (!isShowTop) {
                        Toast.makeText(mRecyclerView.getContext(), "滑动到底部",
                                Toast.LENGTH_SHORT).show();
                    }
                    isShowTop = true;

                } else {
                    isShowTop = false;
                }
                if (mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    if (!isShowBottom) {
                        Toast.makeText(mRecyclerView.getContext(), "滑动到顶部",
                                Toast.LENGTH_SHORT).show();
                    }
                    isShowBottom = true;
                } else {
                    isShowBottom = false;
                }
            }
        });
        MessageListAdapter.OnRecyclerViewListener rc = new MessageListAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                MessageData messageData = dataList.get(position);
                Intent intent = new Intent(mRecyclerView.getContext(), MessageDetailActivity.class);
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
        mRecyclerView.setAdapter(messageListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                mRecyclerView.getContext(), DividerItemDecoration.VERTICAL_LIST));
        return mRecyclerView;
    }

    @Override
    //先接受数据
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<MessageData> tempList = ((MessageListActivity)getActivity()).getDataList();
        for(int i=0;i<tempList.size();i++){
            dataList.add(tempList.get(i));
        }

        mPage = getArguments().getInt(ARG_PAGE);
        switch (mPage){
            case 0:
                for(int i=0;i<dataList.size();){
                    if(dataList.get(i).getType()!=0){
                        dataList.remove(i);
                    }
                    else{
                        i++;
                    }
                }
                break;
            case 1:
                for(int i=0;i<dataList.size();){
                    if(dataList.get(i).getType()!=1){
                        dataList.remove(i);
                    }
                    else{
                        i++;
                    }
                }
                break;
            case 2:

                for(int i=0;i<dataList.size();){
                    if(dataList.get(i).getType()!=2){
                        dataList.remove(i);
                    }
                    else{
                        i++;
                    }
                }
                break;
        }
    }
}
