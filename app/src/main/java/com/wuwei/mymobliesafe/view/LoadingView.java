package com.wuwei.mymobliesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wuwei.mymobliesafe.R;
import com.wuwei.mymobliesafe.view.shimmer.Shimmer;
import com.wuwei.mymobliesafe.view.shimmer.ShimmerTextView;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by 无为 on 2017/6/1.
 */

public class LoadingView extends FrameLayout{
    //加载时显示文字
    protected ShimmerTextView mLoadingTextTv;
    public Context mContext;
    //加载错误视图
    protected LinearLayout mLoadErrorLl;
    //加载错误点击事件处理
    private LoadingHandler mLoadingHandler;
    //加载view
    private View loadingView;
    //加载失败view
    private View loadingErrorView;
    //数据为空
    private View emptyView;

    private Shimmer shimmer;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }


    public void setLoadingHandler(LoadingHandler loadingHandler) {
        mLoadingHandler = loadingHandler;
    }

    public void setLoadingErrorView(View loadingErrorView) {
        this.removeViewAt(1);
        this.loadingErrorView = loadingErrorView;
        this.loadingErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadingHandler != null) {
                    mLoadingHandler.doRequestData();
                    LoadingView.this.load();
                }
            }
        });
        this.addView(loadingErrorView,1);
    }

    public void setLoadingView(View loadingView) {
        this.removeViewAt(0);
        this.loadingView = loadingView;
        this.addView(loadingView,0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        loadingView = inflate(mContext, R.layout.loading_view, null);
        loadingErrorView = inflate(mContext, R.layout.loading_error_view, null);
        emptyView = inflate(mContext, R.layout.loading_view, null);
        this.addView(loadingView);
        this.addView(loadingErrorView);
        this.addView(emptyView);
        loadingErrorView.setVisibility(GONE);
        emptyView.setVisibility(GONE);
        initView(this);
    }


    public void setMessage(String message) {
        mLoadingTextTv.setText(message);
    }


    private void initView(View rootView) {
        mLoadingTextTv = (ShimmerTextView) rootView.findViewById(R.id.tv_loading);
        mLoadErrorLl = (LinearLayout) rootView.findViewById(R.id.ll_loading);

        GifImageView loading = (GifImageView) rootView.findViewById(R.id.gv_loading);
        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(),R.drawable.renwu);
            loading.setImageDrawable(gifDrawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(shimmer != null && shimmer.isAnimating())
        {
            shimmer.cancel();
        }else {
            shimmer = new Shimmer();
            shimmer.start(mLoadingTextTv);
        }
        mLoadErrorLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadingHandler != null) {
                    LoadingView.this.load();
                    mLoadingHandler.doRequestData();
                }
            }
        });
    }

    public void load(){
        loadingView.setVisibility(VISIBLE);
        loadingErrorView.setVisibility(GONE);
        emptyView.setVisibility(GONE);
    }

    public void load(String message){
        mLoadingTextTv.setText(message);
        loadingView.setVisibility(VISIBLE);
        loadingErrorView.setVisibility(GONE);
        emptyView.setVisibility(GONE);
    }


    public void loadSuccess(){
        this.loadSuccess(false);
    }

    public void loadSuccess(boolean isEmpty){

        loadingView.setVisibility(GONE);
        loadingErrorView.setVisibility(GONE);
        if (isEmpty) {
            emptyView.setVisibility(VISIBLE);
        }else{
            emptyView.setVisibility(GONE);
        }
    }


    public void loadError(){
        loadingView.setVisibility(GONE);
        loadingErrorView.setVisibility(VISIBLE);
    }

    public interface LoadingHandler{
        void doRequestData();
    }

}
