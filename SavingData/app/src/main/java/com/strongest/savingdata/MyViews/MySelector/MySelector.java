package com.strongest.savingdata.MyViews.MySelector;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.strongest.savingdata.BaseWorkout.Muscle;
import com.strongest.savingdata.Database.Exercise.Beans;
import com.strongest.savingdata.Database.Exercise.DBExercisesHelper;
import com.strongest.savingdata.Database.Managers.DataManager;
import com.strongest.savingdata.MyViews.WeightKeyBoard.WeightKeyboard;
import com.strongest.savingdata.R;

import java.util.ArrayList;

import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_METHODS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REPS;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_REST;
import static com.strongest.savingdata.Database.Exercise.DBExercisesHelper.TABLE_SETS;
import static com.strongest.savingdata.MyViews.MySelector.MySelector.SelectorTypes.Exercise;
import static com.strongest.savingdata.MyViews.MySelector.MySelector.SelectorTypes.Method;
import static com.strongest.savingdata.MyViews.MySelector.MySelector.SelectorTypes.Reps;
import static com.strongest.savingdata.MyViews.MySelector.MySelector.SelectorTypes.Rest;
import static com.strongest.savingdata.MyViews.MySelector.MySelector.SelectorTypes.Sets;
import static com.strongest.savingdata.MyViews.MySelector.MySelector.SelectorTypes.Weight;

/**
 * Created by Cohen on 11/1/2017.
 */

public class MySelector extends LinearLayout {


    public enum SelectorTypes {
        Exercise, Reps, Method, Sets, Rest, Weight;
    }

    private Context context;
    private Adapter mAdapter;
    //private double[] mValueArray = new double[3901];


    private DataManager dataManager;

    public MySelector(Context context, AttributeSet attributes) {
        super(context, attributes);
        this.context = context;
        dataManager = new DataManager(context);

    }

    public MySelector(Context context) {
        super(context);
        this.context = context;
        dataManager = new DataManager(context);
        //   this.parent = (ViewGroup) view.findViewById(R.id.dialog_fragment_choose_linear_layout);
    }

    private void createLayout() {
        //    TimingLogger t = new TimingLogger("aviv", "createLayoutLoop");
        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            SelectorTypes type = mAdapter.getTypes()[i];
            //   t.addSplit("first " + type.toString());

            ViewHolder viewHolder = (ViewHolder) mAdapter.onCreateViewHolder(this, type);
            //     t.addSplit("second " + type.toString());

            viewHolder.setPosition(i);
            viewHolder.setType(type);
            //      t.addSplit("third " + type.toString());
            addView(viewHolder.getV());
            //       t.addSplit("fourth " + type.toString());
            onCreateLayout(viewHolder);
            //      t.addSplit("after onCreate");
        }
        //    t.dumpToLog();
        dataManager.closeDataBases();
    }


    /*loads the correct data for the buttons
    * attaches the radiogroup by position
    * creates toggle buttons and adds to the radioGroup
    * sends the data to the adapter
    * */

    public void onCreateLayout(ViewHolder viewHolder) {
        int vhPosition = viewHolder.getPosition();
        mAdapter.bindViewHolders(viewHolder.getPosition(), viewHolder);
        ArrayList<Beans> beans_ar = null;
        RadioGroup radioGroup = null;
        View v = viewHolder.getV();
        //HorizontalScrollView sc = null;
        String[] array = null;
        Beans checkedBean = null;

        switch (viewHolder.getType()) {
            case Exercise:
                if (mAdapter.getMuscleType() != null) {

                    beans_ar = (ArrayList<Beans>) dataManager.getExerciseDataManager()
                            .readByConstraint(mAdapter.getMuscleType().getMuscle_name(),
                                    new String[]{DBExercisesHelper.MUSCLES},
                                    new String[]{mAdapter.getMuscleType().getMuscle_name()});
                }
                radioGroup = (RadioGroup) v.findViewById(R.id.my_selector_exercise_radio_group);
                radioGroup.removeAllViews();
                //sc = (HorizontalScrollView) v.findViewById(R.id.my_selector_exercise_scrollview);
                break;
            case Reps:
                beans_ar = Beans.sortList((ArrayList<Beans>) dataManager.getExerciseDataManager().readByTable(TABLE_REPS));
                radioGroup = (RadioGroup) v.findViewById(R.id.my_selector_reps_radio_group);
                //final HorizontalPicker set_hp = (HorizontalPicker) v.findViewById(R.id.my_selector_set_wheel);
                //HorizontalPicker rest_hp = (HorizontalPicker) v.findViewById(R.id.my_selector_rest_wheel);
                /*mAdapter.bindPickers(set_hp, rest_hp);

                set_hp.setSelectedItem(mAdapter.getPickerHolders()[0].getIntObject());
                rest_hp.setSelectedItem(mAdapter.getPickerHolders()[1].getIntObject());*/

                //sc = (HorizontalScrollView) v.findViewById(R.id.my_selector_reps_scrollview);
                break;
            case Method:
                beans_ar = (ArrayList<Beans>) dataManager.getExerciseDataManager().readByTable(TABLE_METHODS);
                radioGroup = (RadioGroup) v.findViewById(R.id.my_selector_method_radio_group);
                //sc = (HorizontalScrollView) v.findViewById(R.id.my_selector_method_scrollview);
                break;
            case Sets:
                //array = context.getResources().getStringArray(R.array.sets_arr);
                beans_ar = Beans.sortList((ArrayList<Beans>) dataManager.getExerciseDataManager().readByTable(TABLE_SETS));
                radioGroup = (RadioGroup) v.findViewById(R.id.my_selector_sets_radio_group);
                //sc = (HorizontalScrollView) v.findViewById(R.id.my_selector_sets_scrollview);
                break;
            case Rest:
                beans_ar = (ArrayList<Beans>) dataManager.getExerciseDataManager().readByTable(TABLE_REST);
                //array = context.getResources().getStringArray(R.array.rest_arr);
                radioGroup = (RadioGroup) v.findViewById(R.id.my_selector_rest_radio_group);
                // sc = (HorizontalScrollView) v.findViewById(R.id.my_selector_rest_scrollview);

                break;
            case Weight:

                mAdapter.setWeightKeyBoard((WeightKeyboard) v);
                mAdapter.weightKeyBoard.setWeight(mAdapter.weightValue);

        }
        mAdapter.addBeansHolder(viewHolder.getPosition(),beans_ar);

        ArrayList<ToggleHolder> toggles = new ArrayList<>();
      /*  if (viewHolder.getType() == Sets *//*&& array != null*//*) {
            length = array.length;
        } else {
            if (beans_ar != null)
                length = beans_ar.size();
        }*/
        if (viewHolder.getType() != Weight && beans_ar != null) {
            int length = beans_ar.size();
            for (int i = 0; i < length; i++) {

                final ToggleButton toggleButton = createToggleButton(viewHolder,
                        beans_ar.get(i));
                if (beans_ar != null) {
                    toggles.add(new ToggleHolder(beans_ar.get(i).getName(), ""));
                    boolean flag = true;
                    if (mAdapter.checkedHolders[vhPosition] != null) {
                        if (mAdapter.checkedHolders[vhPosition].getBean() != null &&
                                mAdapter.checkedHolders[vhPosition].getBean().getName().equals(beans_ar.get(i).getName())) {
                            checkedBean = beans_ar.get(i);
                            mAdapter.bindView(viewHolder, checkedBean,false);

                            checkToggle(i, toggleButton, radioGroup, viewHolder.getSc());
                            flag = false;
                            mAdapter.checkedHolders[vhPosition] = new CheckedHolder(vhPosition, i, beans_ar.get(i).getName(), true);

                        }
                   /* if (array != null && mAdapter.checkedHolders[viewHolder.getPosition()].getBean() ==
                            i) {
                        flag = false;
                        checkToggle(i, toggleButton, radioGroup, sc);
                        mAdapter.checkedHolders[viewHolder.getPosition()] = new CheckedHolder(viewHolder.getPosition(),
                                i, true);

                    }*/
                    }
                    if (flag) {
                        radioGroup.addView(toggleButton);
                    }
                    // t.addSplit("after loop");

                }

            }

        }
        //this is required to initiate the view at first load
        //when the beansholder is null
        mAdapter.bindView(viewHolder, checkedBean, true);
        viewHolder.setInitiated(true);
        mAdapter.bindToggleHolders(viewHolder.getPosition(), toggles);
        mAdapter.bindRadioGroups(viewHolder.getPosition(), radioGroup);
    }

    private void checkToggle(int i, final ToggleButton toggleButton,
                             RadioGroup radioGroup, final HorizontalScrollView sc) {
        toggleButton.setChecked(true);
        toggleButton.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        radioGroup.addView(toggleButton);
        final int x = i;
        final HorizontalScrollView scrollView = sc;
        if (scrollView != null) {
            sc.post(new Runnable() {

                @Override
                public void run() {
                    ObjectAnimator objectAnimator = ObjectAnimator
                            .ofInt(scrollView, "scrollX", (int) toggleButton.getX() / 2, (int) toggleButton.getX())
                            .setDuration(800);
                    objectAnimator.start();
                }
            });
        }
    }

    private ToggleButton createToggleButton(ViewHolder viewHolder, Beans bean) {
        ToggleButton tg = new ToggleButton(context);
        RadioGroup.LayoutParams params = null;

        int resources = 0;
        if (viewHolder.getType() == Exercise) {
            // params = new RadioGroup.LayoutParams(180, 180);
            resources = R.drawable.strongest_choose_buttons_selector;
            tg.setText(bean.getName());
            // tg.setChecked(false);
            tg.setTextOn(bean.getName());
            tg.setTextOff(bean.getName());
            tg.setAllCaps(false);
            params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            //tg.setBackground;
        }
        if (viewHolder.getType() == Reps) {
            resources = R.drawable.strongest_choose_buttons_selector;
            //String s = bean.getDetails().replaceAll("\\\\n", "\\\n");
            String s = bean.getName();
            tg.setText(s);
            tg.setTextOn(s);
            tg.setTextOff(s);
            tg.setAllCaps(false);

            params = new RadioGroup.LayoutParams(140, 100);


        }
        if (viewHolder.getType() == Method) {

            resources = R.drawable.strongest_choose_buttons_selector;
            tg.setText(bean.getName());
            tg.setTextOn(bean.getName());
            tg.setTextOff(bean.getName());
            tg.setAllCaps(false);

            params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        if (viewHolder.getType() == Rest) {
            resources = R.drawable.strongest_choose_buttons_selector;
            tg.setText(bean.getName());
            tg.setTextOn(bean.getName());
            tg.setTextOff(bean.getName());
            tg.setAllCaps(false);

            params = new RadioGroup.LayoutParams(140, 100);
        }

        if (viewHolder.getType() == Sets) {
            resources = R.drawable.strongest_choose_buttons_selector;
            tg.setText(bean.getName());
            tg.setTextOn(bean.getName());
            tg.setTextOff(bean.getName());
            tg.setAllCaps(false);

            params = new RadioGroup.LayoutParams(140, 100);
        }

        //params.setMargins(30, 0, 0, 0);
        tg.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.my_selector_buttons_text_size));
        tg.setMaxWidth(20);
        tg.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        tg.setMaxLines(2);
        tg.setSingleLine(false);
        //tg.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        tg.setGravity(Gravity.CENTER);
        tg.setBackgroundResource(resources);
        tg.setLayoutParams(params);
        tg.setTag(viewHolder.getPosition());
        return tg;
    }

    public void setAdapter(Adapter mAdapter) {
        this.mAdapter = mAdapter;
        this.mAdapter.setSelector(this);
        setOrientation(VERTICAL);
        createLayout();
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public static abstract class Adapter<ViewHolder> implements RadioGroup.OnCheckedChangeListener,
            OnClickListener,
            OnSelectorDialogListener {


        private OnSelectorDialogListener onSelectorDialogListener = this;
        private FragmentManager fm;
        private Context context;
        private Muscle muscle;
        //private int[] values;
        private final MySelector.SelectorTypes[] types;
        private ArrayList<RadioGroup> radioGroups;
        private ArrayList<MySelector.ViewHolder> viewHolders;
        private MySelector.CheckedHolder[] checkedHolders;
        private ArrayList<ArrayList<ToggleHolder>> toggleHolders;
        private ArrayList<ArrayList<Beans>> beansMatrixList = new ArrayList<>();
        // private PickerHolder[] pickerHolders;
        private WeightKeyboard weightKeyBoard;
        private double weightValue;
        private MySelector mSelector;

        public Adapter(FragmentManager fm, Context context, Muscle muscle, @Nullable MySelector.CheckedHolder[] checkedHolders, MySelector.SelectorTypes... types) {
            this.fm = fm;
            this.context = context;

            this.muscle = muscle;
            //    this.values = values;
            this.types = types;
            viewHolders = new ArrayList<>();
            radioGroups = new ArrayList<>();
            toggleHolders = new ArrayList<>();
            if (checkedHolders == null) {
                this.checkedHolders = new CheckedHolder[types.length];
            } else {
                this.checkedHolders = checkedHolders;
            }
        }

        public void updateMuscle(Muscle m) {
            muscle = m;
            ChooseSelectorAdapter.ViewHolderExercise vh = (ChooseSelectorAdapter.ViewHolderExercise) viewHolders.get(0);
            mSelector.onCreateLayout(vh);
            vh.el.collapse();
            vh.toggleCollapseExpand();
            vh.getMySelectorOnBeansHolderChange().notifyMuscleChange(m);

        }

        public DataManager getDataManager() {
            return mSelector.getDataManager();
        }

        public ArrayList<MySelector.ViewHolder> getViewHolders() {
            return viewHolders;
        }

        public MySelector getmSelector() {
            return mSelector;
        }

        public abstract int getItemCount();

        public ArrayList<ArrayList<Beans>> getBeansMatrixList() {
            return beansMatrixList;
        }

        public MySelector.SelectorTypes[] getTypes() {
            return this.types;
        }

        public abstract ViewHolder onCreateViewHolder(ViewGroup parent, MySelector.SelectorTypes type);

        public void addBeansHolder(int position, ArrayList<Beans> beansList) {
            if(this.beansMatrixList.size() <= position){
                this.beansMatrixList.add(position, beansList);
            }else{
                this.beansMatrixList.set(position, beansList);

            }
        }

        public void bindRadioGroups(int position, RadioGroup radioGroup) {
            radioGroup.setOnCheckedChangeListener(this);
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                radioGroup.getChildAt(i).setOnClickListener(this);
            }
            if(radioGroups.size() <= position ){
                radioGroups.add(position, radioGroup);
            }else{
                this.radioGroups.set(position, radioGroup);
            }
        }

        public abstract void bindTypeViews(ViewHolder viewHolder);

        public ArrayList<RadioGroup> getRadioGroups() {
            return this.radioGroups;
        }

        public Muscle getMuscleType() {
            return this.muscle;
        }

        public void bindToggleHolders(int position, ArrayList<ToggleHolder> toggleHolder) {
            //this.toggleHolders.remove(position);
            if (this.toggleHolders.size() <= position) {
                this.toggleHolders.add(position, toggleHolder);
            } else {
                this.toggleHolders.set(position, toggleHolder);
            }
        }

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

        }


        public abstract void bindView(MySelector.ViewHolder viewHolder, Beans b, boolean sendNull);
        //public abstract void updateView(MySelector.ViewHolder viewHolder, int position);

        public MySelector.CheckedHolder[] getCheckedHolders() {
            return checkedHolders;
        }

       /* public void setCheckedHolder(int count) {
            int currentCount = checkedHolders.length;
            CheckedHolder[] newHolders = new CheckedHolder[currentCount + count];
            for (int i = 0; i < checkedHolders.length; i++) {
                newHolders[i] = checkedHolders[i];
            }
            checkedHolders = newHolders;
        }*/

        private boolean check(MySelector.ViewHolder viewHolder, int group, int position) {
            if (checkedHolders[group] == null || checkedHolders[group].getPosition() != position) {
                ToggleButton b = (ToggleButton) radioGroups.get(group).getChildAt(position);
                b.setChecked(true);
                b.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                checkedHolders[group] = new CheckedHolder(group, position, b.getText().toString(), true);
            } else {
                //bindView(viewHolder, "");
                checkedHolders[group] = null;
            }
            return checkedHolders[group] == null;

        }

        private void unCheck(int group) {
            RadioGroup radioGroup = radioGroups.get(group);
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                ((ToggleButton) radioGroup.getChildAt(i)).setChecked(false);
                ((ToggleButton) radioGroup.getChildAt(i)).setTextColor(ContextCompat.getColor(context, R.color.textColor));
            }

        }


        @Override
        public void onClick(View v) {
            String[] array = null;
            RadioGroup group = (RadioGroup) v.getParent();
            allloop:
            for (int i = 0; i < radioGroups.size(); i++) {
                if (radioGroups.get(i).getId() == group.getId()) { //finds the correct row
                    for (int j = 0; j < group.getChildCount(); j++) {
                        if (group.getChildAt(j) == v) { //finds the corrent button
                            unCheck(i);
                                bindView(viewHolders.get(i), beansMatrixList.get(i).get(j),
                                        check(viewHolders.get(i), i, j));

                            break allloop;
                        }
                    }
                }
            }

        }

        public ToggleButton findToggleButton(int group, int position) {
            return ((ToggleButton) radioGroups.get(group).getChildAt(position));
        }

        public void bindViewHolders(int position, MySelector.ViewHolder viewHolder) {
            if(this.viewHolders.size() <= position){
                this.viewHolders.add(position, viewHolder);
            }else{
                this.viewHolders.set(position, viewHolder);
            }
        }

        public double getWeightKeyboardValue() {
            return weightKeyBoard.getValue();
        }

        public WeightKeyboard getWeightKeyboard() {
            return weightKeyBoard;
        }

        public void setWeightKeyBoard(WeightKeyboard weightKeyBoard) {
            this.weightKeyBoard = weightKeyBoard;
        }

        public void setWeightValue(double value) {
            this.weightValue = value;
        }

        public void notifyTypeListChanged(MySelector.SelectorTypes type) {
            int position = -1;
            for (int i = 0; i < types.length; i++) {
                if (types[i] == type) {
                    position = i;
                    break;
                }
            }
            if (position != -1) {
                radioGroups.get(position).removeAllViews();
                toggleHolders.remove(position);
                mSelector.onCreateLayout(viewHolders.get(position));

            }

        }

        public void setSelector(MySelector selector) {
            this.mSelector = selector;
        }

        public FragmentManager getFm() {
            return fm;
        }

        public OnSelectorDialogListener getOnSelectorDialogListener() {
            return onSelectorDialogListener;
        }
    }


    public static class ViewHolder {
        private View v;
        private MySelectorOnBeansHolderChange mySelectorOnBeansHolderChange;
        private String val;
        private HorizontalScrollView sc;
        private MySelector.SelectorTypes type;
        private int position;
        private boolean initiated = false;


        public ViewHolder(View v, MySelectorOnBeansHolderChange mySelectorOnBeansHolderChange) {
            this.v = v;
            this.mySelectorOnBeansHolderChange = mySelectorOnBeansHolderChange;
        }

        public boolean isInitiated() {
            return initiated;
        }

        public void setInitiated(boolean initiated) {
            this.initiated = initiated;
        }

        public View getV() {
            return v;
        }

        public MySelector.SelectorTypes getType() {
            return type;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void setType(MySelector.SelectorTypes type) {
            this.type = type;
        }

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public HorizontalScrollView getSc() {
            return sc;
        }

        public MySelectorOnBeansHolderChange getMySelectorOnBeansHolderChange() {
            return mySelectorOnBeansHolderChange;
        }
    }

    public static class CheckedHolder {

        private int radioGroup;
        private int position;
        private boolean provide;
        private String value;
        private Beans bean;

        public CheckedHolder(int radioGroup, int position, String value, boolean provide) {

            this.radioGroup = radioGroup;
            this.position = position;
            this.value = value;
            this.provide = provide;
        }

        public CheckedHolder(Beans bean) {
            this.bean = bean;
        }

        public Beans getBean() {
            return bean;
        }

        public void setBean(Beans bean) {
            this.bean = bean;
        }

        public int getRadioGroup() {
            return radioGroup;
        }

        public int getPosition() {
            return position;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isProvide() {
            return provide;
        }
    }

    public static class ToggleHolder {

        private String name;
        private String description;

        public ToggleHolder(String name, String description) {
            this.name = name;

            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }
}
