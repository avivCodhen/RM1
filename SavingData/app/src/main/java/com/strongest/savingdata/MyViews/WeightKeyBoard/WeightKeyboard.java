package com.strongest.savingdata.MyViews.WeightKeyBoard;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.strongest.savingdata.R;


/**
 * Created by Cohen on 12/10/2017.
 */

public class WeightKeyboard extends LinearLayout implements View.OnClickListener {

    public static String DEFAULT_DECIMAL = ".5";
    private final double FIXED_VALUE = 2;
    private Context context;

    //  @BindView(R.id.keyboard_even_numbers)
    private TextView naturalTextView;

    //   @BindView(R.id.keyboard_unEven_numbers)
    private TextView decimalTextView;

    //  @BindView(R.id.keyboard_btnC)
    private Button upArrow;

    // @BindView(R.id.keyboard_btnX)
    private Button downArrow;

    private Switch decimalSwitch;
    private Button cleanBtn, deleteBtn;
    private Button btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9;
    private Button btn_plus_fixed, btn_minus_fixed;

    private String mainNumbers = "";
    private Button[] btnArrays;
    private View decimalView;

    private TextView setupedTextView;


    private boolean flag = true;

    public WeightKeyboard(Context context) {
        super(context);
        /*View v = LayoutInflater.from(context).inflate(R.layout.keyboard_layout, this);
        addView(v);
        initViews(v);*/
        //  ButterKnife.bind(this);
        setOrientation(VERTICAL);
        View v = inflate(context, R.layout.keyboard_layout, this);
        initViews(v);


    }

    public WeightKeyboard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(VERTICAL);
        View v = inflate(context, R.layout.keyboard_layout, this);
        initViews(v);

    }

    private void initViews(View v) {

        decimalView = v.findViewById(R.id.weight_keyboard_decimal_layout);
        naturalTextView = (TextView) v.findViewById(R.id.keyboard_even_numbers);
        decimalTextView = (TextView) decimalView.findViewById(R.id.keyboard_unEven_numbers);
        decimalSwitch = (Switch) v.findViewById(R.id.keyboard_switch);

        upArrow = (Button) decimalView.findViewById(R.id.keyboard_up_arrow);
        downArrow = (Button) decimalView.findViewById(R.id.keyboard_down_arrow);

        cleanBtn = (Button) v.findViewById(R.id.keyboard_btnC);
        deleteBtn = (Button) v.findViewById(R.id.keyboard_btnX);

        btn_minus_fixed = (Button) v.findViewById(R.id.keyboard_btn_minus);
        btn_plus_fixed = (Button) v.findViewById(R.id.keyboard_btn_plus);

        btn_0 = (Button) v.findViewById(R.id.keyboard_btn0);
        btn_1 = (Button) v.findViewById(R.id.keyboard_btn1);
        btn_2 = (Button) v.findViewById(R.id.keyboard_btn2);
        btn_3 = (Button) v.findViewById(R.id.keyboard_btn3);
        btn_4 = (Button) v.findViewById(R.id.keyboard_btn4);
        btn_5 = (Button) v.findViewById(R.id.keyboard_btn5);
        btn_6 = (Button) v.findViewById(R.id.keyboard_btn6);
        btn_7 = (Button) v.findViewById(R.id.keyboard_btn7);
        btn_8 = (Button) v.findViewById(R.id.keyboard_btn8);
        btn_9 = (Button) v.findViewById(R.id.keyboard_btn9);


        btnArrays = new Button[]{
                btn_0,
                btn_1,
                btn_2,
                btn_3,
                btn_4,
                btn_5,
                btn_6,
                btn_7,
                btn_8,
                btn_9
        };


        onClickListeners();
    }

    private void onClickListeners() {
        for (int i = 0; i < btnArrays.length; i++) {
            btnArrays[i].setOnClickListener(this);
        }
        decimalSwitch.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        cleanBtn.setOnClickListener(this);
        upArrow.setOnClickListener(this);
        downArrow.setOnClickListener(this);

        btn_plus_fixed.setOnClickListener(this);
        btn_minus_fixed.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.keyboard_btn_plus) {
            if (mainNumbers.equals("")) {
                mainNumbers = "0";
            }
            int d = mainNumberDoubleVal();
            if (decimalSwitch.isChecked()) {
                d += 3;
                decimalSwitch.setChecked(false);
                onSwitch();
            } else {
                d += 2;
                decimalSwitch.setChecked(true);
                onSwitch();

            }
            mainNumbers = mainNumbersToString(d);
            onRefresh();
        }
        if (v.getId() == R.id.keyboard_btn_minus) {
            if (mainNumberDoubleVal() != 0) {
                int d = mainNumberDoubleVal();
                if (decimalSwitch.isChecked()) {
                    d -= 2;
                    decimalSwitch.setChecked(false);
                    onSwitch();
                } else {
                    d -= 3;
                    decimalSwitch.setChecked(true);
                    onSwitch();
                }
                mainNumbers = mainNumbersToString(d);
                onRefresh();
            }
        }

        if (v.getId() == R.id.keyboard_up_arrow) {
            incrementOrDecrement(true);

        }


        if (v.getId() == R.id.keyboard_down_arrow) {
            incrementOrDecrement(false);
        }


        if (v.getId() == R.id.keyboard_switch) {
            onSwitch();
          //  onRefresh();
            flag = false;
        }

        if (v.getId() == R.id.keyboard_btnC) {
            mainNumbers = "";
            flag = false;
        }
        if (v.getId() == R.id.keyboard_btnX) {
            if (mainNumbers.length() > 0) {
                mainNumbers = mainNumbers.substring(0, mainNumbers.length() - 1);
            }
            flag = false;

        }

        if (mainNumbers.length() >= 3) {
            return;
        }
        if (flag)
            if(!mainNumbers.equals("") && Integer.parseInt(mainNumbers) == 0){
                mainNumbers = "";
            }
            for (int i = 0; i < btnArrays.length; i++) {
                if (btnArrays[i].getId() == v.getId()) {
                    mainNumbers += i;
                    break;
                }
            }
        flag = true;
        naturalTextView.setText(mainNumbers);

        if(setupedTextView != null){
            if(decimalSwitch.isChecked()){
                setupedTextView.setText(String.valueOf(getValue()));
            }else{
                setupedTextView.setText(naturalTextView.getText().toString());
            }
        }
    }

    private void incrementOrDecrement(boolean increment) {
        String text = decimalTextView.getText().toString();
        char cChar = text.charAt(text.length() - 1);
        int intChar = Character.getNumericValue(cChar);
        int newChar;
        if (increment) {
            if (!(intChar >= 9)) {
                newChar = intChar + 1;
            } else {
                return;
            }
        } else {
            if (!(intChar <= 1)) {
                newChar = intChar - 1;
            } else {
                return;
            }
        }
        String newText = "." + String.valueOf(newChar);
        decimalTextView.setText(newText);

    }

    private void onSwitch() {
        if (decimalSwitch.isChecked()) {
            decimalTextView.setText(DEFAULT_DECIMAL);
            naturalTextView.setText(mainNumbers);
            decimalView.setVisibility(VISIBLE);
        } else {
            decimalView.setVisibility(INVISIBLE);

        }
    }

    private void onRefresh() {
        naturalTextView.setText(mainNumbers);

    }

    private int mainNumberDoubleVal() {
        String newText = mainNumbers;

        if (mainNumbers.contains(".")) {
            newText = mainNumbers.substring(0, mainNumbers.length() - 2);
        }
        int val = Integer.parseInt(newText);
        return val;
    }

    private String mainNumbersToString(int d) {
        return String.valueOf(d);
    }

    public double getValue() {
        String text = naturalTextView.getText().toString();
        String stringValue;
        double value;
        if (!text.equals("")) {
            if (decimalSwitch.isChecked()) {
                stringValue = text + decimalTextView.getText().toString();
                value = Double.parseDouble(stringValue);
                return value;
            } else {
                return Double.parseDouble(text);
            }
        }
        return 0;
    }

    public void setWeight(double weight) {
        String d = String.valueOf(weight);
        char c = '0';
        if (/*d.length() => 3*/ isDecimal(d)) {
            c = d.charAt(d.length() - 1);
            decimalSwitch.setChecked(true);
            onSwitch();
            decimalTextView.setText("." + c);
        } else {
            decimalSwitch.setChecked(false);
            onSwitch();

        }

        int newWeight = (int) weight;
        mainNumbers = newWeight + "";
        naturalTextView.setText(mainNumbers);
    }

    private boolean isDecimal(String d) {
        if (Character.getNumericValue(d.charAt(d.length() - 1)) > 0) {
            return true;
        }
        return false;
    }

    public void setUpwithTextView(TextView tv){
        this.setupedTextView = tv;
        setWeight(Double.parseDouble(tv.getText().toString()));


    }

}
