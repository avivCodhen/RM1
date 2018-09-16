package com.strongest.savingdata.MyViews;

import android.animation.Animator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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

    private View main = this;

    private static final int DEFAULT_DURATION = 200;

    private RecyclerView recyclerView;

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
        inflate(context, R.layout.layout_smart_empty_view, this);
        this.setAlpha(0);
        image = findViewById(R.id.empty_image);
        title = findViewById(R.id.empty_title);
        body = findViewById(R.id.empty_body);
        actionBtn = findViewById(R.id.empty_actionbtn);
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

    public SmartEmptyView setActionBtn(View.OnClickListener func) {
        actionBtn.setOnClickListener(func);
        return this;
    }

    public SmartEmptyView setUpWithRecycler(RecyclerView recycler, boolean startObservertion) {
        this.recyclerView = recycler;
        observerRecycler();
        if (startObservertion) {
            decide();
        }
        return this;
    }

    public SmartEmptyView setButtonText(String text){
        actionBtn.setText(text);
        return this;
    }

    public SmartEmptyView hideBtn(){
        actionBtn.setVisibility(GONE);
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
                decide();
                super.onChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                decide();

                super.onItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                decide();

                super.onItemRangeChanged(positionStart, itemCount, payload);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                decide();

                super.onItemRangeRemoved(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                decide();

                super.onItemRangeInserted(positionStart, itemCount);
            }
        });
    }

    private void decide() {
        if (recyclerView.getAdapter().getItemCount() == 0) {
            show();
        } else {
            hide();
        }
    }

    private void hide() {
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
        recyclerView.animate().alpha(1f).setDuration(DEFAULT_DURATION).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                recyclerView.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void show() {
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
        recyclerView.animate().alpha(0f).setDuration(DEFAULT_DURATION).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                recyclerView.setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }


    public Drawable getRocketImage() {
        return rocketImage;
    }

    public Drawable getDocImage() {
        return docImage;
    }
}
