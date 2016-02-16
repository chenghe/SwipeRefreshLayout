package com.example.hecheng.swiperefreshlayout;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipe;
    private List<TestBean> bean;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isScroll=true;
    public TestBean load;
    public  MyAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bean = new ArrayList<TestBean>();
        bean.add(new TestBean(1));
        bean.add(new TestBean(1));
        bean.add(new TestBean(1));
        bean.add(new TestBean(2));
        bean.add(new TestBean(2));
        bean.add(new TestBean(1));
        bean.add(new TestBean(2));
        bean.add(new TestBean(1));
        bean.add(new TestBean(1));
        bean.add(new TestBean(1));
        bean.add(new TestBean(2));
        bean.add(new TestBean(2));
        bean.add(new TestBean(1));
        bean.add(new TestBean(2));

        load = new TestBean(3);

        mSwipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipe.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            Boolean isScrolling = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && isScroll) {
                    int lastVisibleItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = mLinearLayoutManager.getItemCount();
                    if (lastVisibleItem == (totalItemCount - 1)) {
                        LoadMore();
                        isScroll = false;
                    }
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    isScrolling = true;
                } else {
                    isScrolling = false;
                }
            }
        });
         mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 停止刷新
                mSwipe.setRefreshing(false);
            }
        },3000); // 5秒后发送消息，停止刷新
    }
    public void onPreLoadMore() {
        bean.add(load);
        mAdapter.notifyDataSetChanged();
    }

    public void onPostLoadMore() {
        bean.remove(load);
        isScroll=true;
        mAdapter.notifyDataSetChanged();
    }

    public void LoadMore(){
        onPreLoadMore();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bean.add(new TestBean(2));
                bean.add(new TestBean(2));
                bean.add(new TestBean(1));
                bean.add(new TestBean(2));
                bean.add(new TestBean(2));
                bean.add(new TestBean(1));
                onPostLoadMore();
            }
        }, 3000);

    }
    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public int getItemCount() {
            return bean.size();
        }

        @Override
        public int getItemViewType(int position) {
            return bean.get(position).getType();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = null;
            View v = null;
            switch (viewType){
                case 1:
                    v = LayoutInflater.from(MainActivity.this).inflate(R.layout.item1,parent,false);
                    holder = new MyViewHolder1(v);
                    break;
                case 2:
                    v = LayoutInflater.from(MainActivity.this).inflate(R.layout.item2,parent,false);
                    holder = new MyViewHolder2(v);
                    break;
                case 3:
                    v = LayoutInflater.from(MainActivity.this).inflate(R.layout.loadmore,parent,false);
                    holder = new MyViewHolder3(v);
                    break;
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        class MyViewHolder1 extends RecyclerView.ViewHolder{
            private LinearLayout ll;
            public MyViewHolder1(View view) {
                super(view);
            }

        }
        class MyViewHolder2 extends RecyclerView.ViewHolder{
            private LinearLayout ll;
            public MyViewHolder2(View view) {
                super(view);
            }

        }
        class MyViewHolder3 extends RecyclerView.ViewHolder{
            public MyViewHolder3(View view) {
                super(view);
            }

        }
    }
}
