package com.strongest.savingdata.createProgramFragments.CreateProgram;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.strongest.savingdata.Activities.OnCreateProgramListener;
import com.strongest.savingdata.BaseWorkout.ProgramTemplate;
import com.strongest.savingdata.Unused.AlertPopUp;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.Create.CreateProgramFragment;

/**
 * Created by Cohen on 4/28/2017.
 */


public class RoutinesFragment extends BaseCreateProgramFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private Button buildBtn;
    private Context context;
    private String[] routines;
    private String[] desRoutines;
    private int tag;
    private RadioGroup radioGroup;
    private TextView descriptionTV;
    private static final int[] routinesBtnArray = new int[]{R.id.fragment_routine_fbwBtn, R.id.fragment_routine_abBtn
            , R.id.fragment_routine_abcBtn, R.id.fragment_routine_abcdeBtn, R.id.fragment_routine_fbwBtn};

    private OnCreateProgramListener onCreateProgramListener;

    public static RoutinesFragment getInstance(OnCreateProgramListener onCreateProgramListener) {
        RoutinesFragment f = new RoutinesFragment();
        f.setOnCreateProgramListener(onCreateProgramListener);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       /* getActionBar().setTitle(R.string.title_routine);
        getActionBar().show();
        setHasOptionsMenu(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);*/
        return inflater.inflate(R.layout.fragment_routine, container, false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Bundle bundle = new Bundle();
        int id = R.id.action_next;
        if (item.getItemId() == id) {
            if (radioGroup.getCheckedRadioButtonId() == -1) {
                AlertPopUp alert = new AlertPopUp(getContext());
                alert.createAlert(getString(R.string.Error_routine), false);
            } else {
                if (tag == routinesBtnArray.length - 1) {
                    getPrefsEditor().putBoolean("custom", true).commit();
                    bundle.putBoolean(MODE_CUSTOM, true);
                    onCreateProgramListener.createProgramUI(new ProgramTemplateFragment());
                    //switchFragment(new ProgramTemplateFragment(), bundle);
                } else {
                    getPrefsEditor().putBoolean("custom", false).commit();
                    getPrefsEditor().putInt(ROUTINE, tag).commit();

                    bundle.putInt(ROUTINE, tag);
                    bundle.putBoolean(MODE_GENERATED_PROGRAM, false);
                    bundle.putBoolean(MODE_CUSTOM, false);
                    getPrefsEditor().putBoolean(MODE_GENERATED_PROGRAM, false).commit();
                    onCreateProgramListener.createProgramUI(new CreateFragment().newInstance(onCreateProgramListener,
                            bundle));
                    //switchFragment(new CreateFragment(), bundle);
                }
                return true;

            }

        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.next_button, menu);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    public void initView(View v) {
        routines = getContext().getResources().getStringArray(R.array.routines_names);
        desRoutines = getContext().getResources().getStringArray(R.array.routines_description);
        descriptionTV = (TextView) v.findViewById(R.id.fragment_routine_textView2);
        radioGroup = (RadioGroup) v.findViewById(R.id.fragment_routine_radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        buildBtn = (Button) v.findViewById(R.id.fragment_routine_btn);
        buildBtn.setOnClickListener(new View.OnClickListener() {
            Bundle bundle = new Bundle();
            @Override
            public void onClick(View v) {
                if (tag == routinesBtnArray.length - 1) {

                    getPrefsEditor().putBoolean("custom", true).commit();
                    bundle.putBoolean(MODE_CUSTOM, true);
                    onCreateProgramListener.createProgramUI(ProgramTemplateFragment.getInstance(onCreateProgramListener));
                    //switchFragment(new ProgramTemplateFragment(), bundle);
                } else {
                    getPrefsEditor().putBoolean("custom", false).commit();
                    getPrefsEditor().putInt(ROUTINE, tag).commit();

                    bundle.putInt(ROUTINE, tag);
                    bundle.putBoolean(MODE_GENERATED_PROGRAM, false);
                    bundle.putBoolean(MODE_CUSTOM, false);
                    getPrefsEditor().putBoolean(MODE_GENERATED_PROGRAM, false).commit();
                    onCreateProgramListener.createProgramUI(new CreateFragment().newInstance(onCreateProgramListener,
                            bundle));
                    //switchFragment(new CreateFragment(), bundle);
                }
            }
        });
        //recommendRoutines();
    }


/*    public String getRecommendations(int pos) {
        int days = -1;
        int level = -1;
        int sum;
        String text = "";
        SharedPreferences prefs = getPrefs();
        if (prefs != null) {
            //SharedPreferences preffs = getContext().getSharedPreferences(PREFS, getContext().MODE_PRIVATE);
            days = getPrefs().getInt(DAYS_INT, -1);
            level = getPrefs().getInt(LEVEL_INT, -1);
            if (days != -1 && level != -1) {
                sum = days + level;
                if (pos == 0) {
                    if (level == 0) {
                        return "";
                    } else {
                        return getString(R.string.routine_notrecommend);
                    }
                }
                if (pos == 1) {
                    if (sum >= 3 && sum <= 5) {
                        return "";
                    } else {
                        return getString(R.string.routine_notrecommend);
                    }
                }
                if (pos == 2) {
                    if (sum >= 4 && sum < 7) {
                        return "";
                    } else {
                        return getString(R.string.routine_notrecommend);
                    }
                }

                if (pos == 3) {
                    if (sum >= 5 && sum <= 7) {
                        return "";
                    } else {
                        return getString(R.string.routine_notrecommend);
                    }
                }

                if (pos == 4) {
                    if (sum >= 7) {
                        return "";
                    } else {
                        return getString(R.string.routine_notrecommend);
                    }
                }
                if (pos == 5) {
                    if (sum >= 7) {
                        return "";
                    } else {
                        return getString(R.string.routine_notrecommend);
                    }
                }
                if (pos == 6) {
                    if (sum > 7) {
                        return "";
                    } else {
                        return getString(R.string.routine_notrecommend);
                    }
                }
            }
        } else {
        }
        return text;
    }*/

  /*  public void recommendRoutines() {
        int level = getPrefs().getInt(LEVEL_INT, -1);
        int days = getPrefs().getInt(DAYS_INT, -1);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            if (level == 0 || level == 1) {
                setFalseEnableRadioButton(2);
                setFalseEnableRadioButton(3);
            }
            if (level == 3) {
                setFalseEnableRadioButton(0);
            }
        }
    }*/


    public void setFalseEnableRadioButton(int num) {
        RadioButton rb = (RadioButton) radioGroup.getChildAt(num);
        rb.setEnabled(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            if (group.getChildAt(i).getId() == checkedId) {
                tag = i;
            }
        }

        //descriptionTV.setText(ProgHelper.determineString(checkedId, getContext(), routinesBtnArray, R.array.routines_description));

       /* for (int i = 0; i <levelsBtnArray.length ; i++) {
            if(checkedId == levelsBtnArray[i])
                tag = i;
        }*/
        /*switch (checkedId) {
            case R.id.fragment_routine_fbwBtn:
                tag = 0;
                break;
            case R.id.fragment_routine_abBtn:
                tag = 1;
                break;
            case R.id.fragment_routine_abcBtn:
                tag = 2;
                break;
            case R.id.fragment_routine_abcdeBtn:
                tag = 3;
                break;
        }*/
    }

    @Override
    public void onClick(View v) {
        switchFragment(new CreateProgramFragment(), null);
    }

    public void setOnCreateProgramListener(OnCreateProgramListener onCreateProgramListener) {
        this.onCreateProgramListener = onCreateProgramListener;
    }
}
