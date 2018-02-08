package com.strongest.savingdata.createProgramFragments.Unused;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;


public class InstallFragment extends BaseCreateProgramFragment {

    private LinearLayout layout;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InstallFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InstallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InstallFragment newInstance(String param1, String param2) {
        InstallFragment fragment = new InstallFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_install, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View v) {
        layout = (LinearLayout) v.findViewById(R.id.install_fragment_layout);
        layout.setBackgroundColor(Color.BLACK);
        new ShowMeSomeData().start();

    }

    private TextView makeMeSomeData() {
        TextView t = new TextView(getContext());
        t.setTextColor(Color.parseColor("#FFFFFF"));
        return t;
    }

    public class ShowMeSomeData extends Thread {
        Handler handler = new Handler();
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int x;
        @Override
        public void run() {
         final   String[] arr = getContext().getResources().getStringArray(R.array.chest_exercises);
            for (int i = 0; i < arr.length; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i % 2 == 0) {
                     x = i;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView t = new TextView(getContext());
                            t.setTextColor(Color.parseColor("#FFFFFF"));
                            t.setText(arr[x]);
                            layout.addView(t);
                        }
                    });

                }
                if (i % 5 == 0) {
                        x = i;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView t = new TextView(getContext());
                            t.setTextColor(Color.parseColor("#FFFFFF"));
                            t.setText(arr[x]);
                            layout.addView(t);
                        }
                    });
                }
            }
            switchFragment(new CreatingViewPagerFragment(), null);
        }
    }
}
