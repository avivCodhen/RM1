package com.strongest.savingdata.Activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.strongest.savingdata.ArtificialInteligence.ArtificialIntelligenceObserver;
import com.strongest.savingdata.MyViews.TimeLineTabLayout;
import com.strongest.savingdata.R;
import com.strongest.savingdata.createProgramFragments.Create.ArtificialIntelligenceDialog;
import com.strongest.savingdata.createProgramFragments.CreateProgram.BaseCreateProgramFragment;
import com.strongest.savingdata.createProgramFragments.CreateProgram.CreateFragment;
import com.strongest.savingdata.createProgramFragments.CreateProgram.DetailsFragment;
import com.strongest.savingdata.createProgramFragments.CreateProgram.LimitFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Observer;

public class CreateWorkoutActivity extends BaseActivity implements View.OnClickListener, OnDoneListener {

    private final String ROUTINE = "routine";
    private RecyclerView recyclerView;
    private CreateworkoutActivityAdapter adapter;
    private int fragmentPosition;
    private TimeLineTabLayout t;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //recyclerView = (RecyclerView) findViewById(R.id.activity_createworkout_recycler);

        fab = (FloatingActionButton) findViewById(R.id.create_program_ai);
        fab.setOnClickListener(this);
        t = (TimeLineTabLayout) findViewById(R.id.timelineTabLayout);
        t.initiateViewLayout("Limit", R.drawable.ball_green_24px);
        t.initiateViewLayout("Limit", R.drawable.ball_green_24px);
        t.initiateViewLayout("Limit", R.drawable.ball_green_24px);
        t.initiateViewLayout("Limit", R.drawable.ball_green_24px);

        DetailsFragment fragment = new DetailsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.create_activity_frameLayout, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*if (id == R.id.home) {
            return false;
        }*/
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        FragmentManager fm = getSupportFragmentManager();
        CreateFragment fragment = new CreateFragment();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        } else {
            /*Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);*/
            finish();
        }

    }



    @Subscribe
    public void event(BaseCreateProgramFragment f){
        if(f instanceof LimitFragment){
            Toast.makeText(this, "yohooo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        ArtificialIntelligenceDialog csf = new ArtificialIntelligenceDialog();
        csf.setParentFab(fab);
        //notifyAI(workoutView.getWorkoutPosition());
        csf.setAi(ai);
        csf.show(getSupportFragmentManager(), csf.getTag());

    }

    @Override
    public void onDone(){

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void attachAI(ArtificialIntelligenceObserver o) {

    }

    @Override
    public void dettachAI(ArtificialIntelligenceObserver o) {

    }

    @Override
    public void notifyAI(int workoutPosition) {
        ai.updatePosition(workoutPosition);
    }
}
