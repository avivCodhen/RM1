package com.strongest.savingdata.MyViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strongest.savingdata.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SaveExitToolBar extends LinearLayout{


    private Context context;

    @BindView(R.id.fragment_choose_back_iv)
    ImageView back;
    @BindView(R.id.fragment_choose_nosave_iv)
    ImageView cancel;
    @BindView(R.id.optional_text)
    TextView optionalText;
    @BindView(R.id.save_exit_toolbar_optional_iv)
    ImageView optionalIV;
    @BindView(R.id.save_exit_toolbar_optional_tv)
    TextView optionalTV;

    public SaveExitToolBar(Context context) {
        super(context);
        this.context = context;
    }

    public SaveExitToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void instantiate(){
        inflate(context, R.layout.tool_bar_save_exit, this);
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        setElevation(10);
        ButterKnife.bind(this, this);
    }

    public void showBack(boolean b){
        if(b){
            back.setImageResource(R.drawable.back_arrow_24px);
        }
    }

    public void showCancel(boolean b){
        if(!b){
            cancel.setVisibility(GONE);
        }else{
            cancel.setVisibility(VISIBLE);
        }
    }

    public void setOptionalText(String txt){
        optionalText.setText(txt);
    }

    public void setOptionalTV(String t, OnClickListener onClickListener){
        optionalTV.setVisibility(VISIBLE);
        optionalTV.setText(t);
        optionalTV.setOnClickListener(onClickListener);
    }

    public void setOptionalIV(int resource, OnClickListener onClickListener){
        optionalIV.setVisibility(VISIBLE);
        optionalIV.setImageResource(resource);
        optionalIV.setOnClickListener(onClickListener);
    }

    public void setSaveButton(OnClickListener onClickListener){
        back.setOnClickListener(onClickListener);
    }

    public void setCancelButton(OnClickListener onClickListener){
        cancel.setOnClickListener(onClickListener);
    }
}
