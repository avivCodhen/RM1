package com.strongest.savingdata.MyViews;

import android.animation.Animator;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strongest.savingdata.R;

public class SmartEmptyView extends LinearLayout {


    private Context context;
    private ImageView image;
    private TextView title, body;
    private Button actionBtn;

    private Drawable rocketImage;
    private Drawable docImage;
    private Drawable logInImage;

    private View main = this;

    private static final int DEFAULT_DURATION = 200;

    private RecyclerView recyclerView;
    private ViewPager viewPager;
    private View[] views;
    private boolean hasViews;
    private OnHide onHide;
    private OnShow onShow;

    public SmartEmptyView(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public SmartEmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);

    }

    private void initView(Context context) {
        this.setVisibility(GONE);
        rocketImage = ContextCompat.getDrawable(context, R.drawable.empty_rocket);
        docImage = ContextCompat.getDrawable(context, R.drawable.empty_doc);
        logInImage= ContextCompat.getDrawable(context, R.drawable.empty_login);
        inflate(context, R.layout.layout_smart_empty_view, this);
        this.setAlpha(0);
        image = findViewById(R.id.empty_image);
        title = findViewById(R.id.empty_title);
        body = findViewById(R.id.empty_body);
        actionBtn = findViewById(R.id.empty_actionbtn);
    }

    public SmartEmptyView noButton(){
        this.actionBtn.setVisibility(GONE);
        return this;
    }
    public SmartEmptyView setImage(Drawable drawable) {
        image.setImageDrawable(drawable);
        return this;
    }

    public SmartEmptyView setTitle(String title) {
        this.title.setText(title);
        return this;
    }

    public SmartEmptyView setBody(String body) {
        this.body.setText(body);
        return this;
    }

    public SmartEmptyView noImage(){
        this.image.setVisibility(GONE);
        return this;
    }

    public SmartEmptyView setActionBtn(View.OnClickListener func) {
        actionBtn.setOnClickListener(func);
        return this;
    }

    public SmartEmptyView setUpWithRecycler(RecyclerView recycler, boolean observe,  boolean start) {
        this.recyclerView = recycler;
        if(observe){
            observerRecycler();
        }
        if (start) {
            decide(recycler.getAdapter().getItemCount() == 0);
        }
        return this;
    }
    public SmartEmptyView setUpWithViewPager(ViewPager viewPager,boolean observe, boolean start) {

        this.viewPager = viewPager;
        if(observe){
            observerViewPager();
        }
        if(start){
            decide(viewPager.getAdapter().getCount() == 0);
        }

        return this;
    }

    public SmartEmptyView setUpWithMoreViews(View...views){
        this.views = views;
        hasViews = views != null;
        return this;
    }

    public void onCondition(boolean b){
        decide(b);
    }

    public void onCondition(OnCondition onCondition){
        if(onCondition.condition() == false){
            decide(false);
        }else{
            decide(true);
        }
    }

        public SmartEmptyView setButtonText(String text){
        actionBtn.setText(text);
        return this;
    }

    public SmartEmptyView onHideFunc(OnHide onHide) {
        this.onHide = onHide;
        return this;
    }

    public SmartEmptyView onShowFunc(OnShow onShow){
        this.onShow = onShow;
        return this;
    }


    private void observerRecycler() {
        if (recyclerView == null) {
            throw new IllegalArgumentException("Recyclerview cannot be null");
        }
        if (recyclerView.getAdapter() == null) {
            throw new IllegalArgumentException("Adapter cannot be null");

        }

        recyclerView.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                decide(recyclerView.getAdapter().getItemCount() == 0);
                super.onChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                decide(itemCount == 0);

                super.onItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                decide(itemCount == 0);

                super.onItemRangeChanged(positionStart, itemCount, payload);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                decide(itemCount == 0);

                super.onItemRangeRemoved(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                decide(itemCount == 0);

                super.onItemRangeInserted(positionStart, itemCount);
            }
        });
    }

    private void observerViewPager(){
        if(viewPager == null){
            throw  new IllegalArgumentException("Adapter cannot be null");
        }
        if(viewPager.getAdapter() == null){
            throw new IllegalArgumentException("Adapter cannot be null");
        }
        viewPager.getAdapter().registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                decide(viewPager.getAdapter().getCount() == 0);
                super.onChanged();
            }
        });
    }

    private void decide(boolean b) {
        View v = recyclerView == null? viewPager : recyclerView;

        if(v == null){
            throw new IllegalArgumentException("view is null");
        }
        if (b) {
            show(v);
        } else {
            hide(v);
        }
    }

    public void hide(View v) {
        main.animate().alpha(0f).setDuration(DEFAULT_DURATION).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                main.setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        v.animate().alpha(1f).setDuration(DEFAULT_DURATION).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                v.setVisibility(VISIBLE);
                setViewsVisibility(VISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        if(onHide != null){
            onHide.func();
        }
    }

    private void show(View v) {
        main.animate().alpha(1f).setDuration(DEFAULT_DURATION).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                main.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        v.animate().alpha(0f).setDuration(DEFAULT_DURATION).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                v.setVisibility(GONE);
                setViewsVisibility(GONE);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        if(onShow != null){
            onShow.func();
        }
    }

    private void setViewsVisibility(int visibility){
        if(hasViews){
            for (View vews : views){
                vews.setVisibility(visibility);
            }
        }
    }

    public void hideEmptyView() {
        decide(false);
    }

    public void hidshowEmptyView() {
        decide(true);
    }


    public Drawable getRocketImage() {
        return rocketImage;
    }

    public Drawable getDocImage() {
        return docImage;
    }

    public Drawable getLogInImage() {
        return logInImage;
    }



    public interface OnCondition{

        boolean condition();

    }

    public interface OnHide {
        void func();
    }

    public interface OnShow{
        void func();
    }
}
