package com.sparktest.autotestapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sparktest.autotestapp.cases.TestCaseAudioCall;
import com.sparktest.autotestapp.cases.TestCaseAudioCallUnmuteVideo;
import com.sparktest.autotestapp.cases.TestCaseCallRejectWhenInit;
import com.sparktest.autotestapp.cases.TestCaseCallRejectWhenRinging;
import com.sparktest.autotestapp.cases.TestCaseCallSequence_1;
import com.sparktest.autotestapp.cases.TestCaseCallSequence_2;
import com.sparktest.autotestapp.cases.TestCaseCallWhenConnected;
import com.sparktest.autotestapp.cases.TestCaseCallWhenRinging;
import com.sparktest.autotestapp.cases.TestCaseHangUpDisconnectedCall;
import com.sparktest.autotestapp.cases.TestCaseKeepCall;
import com.sparktest.autotestapp.cases.TestCaseMuteAudioVideo;
import com.sparktest.autotestapp.cases.TestCaseTeamAndMemberShip;
import com.sparktest.autotestapp.cases.TestCaseWebhooks;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall1;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall10;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall11;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall12;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall13;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall14;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall15;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall16;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall17;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall18;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall19;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall2;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall20;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall21;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall22;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall23;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall3;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall4;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall5;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall6;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall7;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall8;
import com.sparktest.autotestapp.cases.roomCallCases.TestCaseSpaceCall9;
import com.sparktest.autotestapp.framework.Test;
import com.sparktest.autotestapp.framework.TestCase;
import com.sparktest.autotestapp.framework.TestRunner;
import com.sparktest.autotestapp.framework.TestState;
import com.sparktest.autotestapp.framework.TestSuite;
import com.sparktest.autotesteapp.R;
import com.webex.wseclient.WseSurfaceView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class TestActivity extends Activity {

    public ListView mListView;
    public WseSurfaceView mRemoteSurface;
    public WseSurfaceView mLocalSurface;

    public Handler mHandler;
    public List<TestSuite> mSuites;
    private static final int FINISH = 1;

    @Inject
    public TestRunner mRunner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);
        mListView = (ListView) findViewById(R.id.testcaseListView);
        mRemoteSurface = (WseSurfaceView) findViewById(R.id.remoteView);
        mLocalSurface = (WseSurfaceView) findViewById(R.id.localView);
        Handler.Callback callback = (msg) -> {
            if (msg.what == FINISH) update();
            return true;
        };
        mHandler = new Handler(callback);

        ObjectGraph objectGraph = ObjectGraph.create(new TestModule(this));
        objectGraph.inject(this);
        Log.d("TestActivity", mRunner.getClass().getName());
        ((AppTestRunner) mRunner).setInjector(objectGraph);

        mSuites = new ArrayList<>();

        mSuites.add(new TestCaseSpaceCall1());
        mSuites.add(new TestCaseSpaceCall2());
        mSuites.add(new TestCaseSpaceCall3());
        mSuites.add(new TestCaseSpaceCall4());
        mSuites.add(new TestCaseSpaceCall5());
        mSuites.add(new TestCaseSpaceCall6());
        mSuites.add(new TestCaseSpaceCall7());
        mSuites.add(new TestCaseSpaceCall8());
        mSuites.add(new TestCaseSpaceCall9());
        mSuites.add(new TestCaseSpaceCall10());
        mSuites.add(new TestCaseSpaceCall11());
        mSuites.add(new TestCaseSpaceCall12());
        mSuites.add(new TestCaseSpaceCall13());
        mSuites.add(new TestCaseSpaceCall14());
        mSuites.add(new TestCaseSpaceCall15());
        mSuites.add(new TestCaseSpaceCall16());
        mSuites.add(new TestCaseSpaceCall17());
        mSuites.add(new TestCaseSpaceCall18());
        mSuites.add(new TestCaseSpaceCall19());
        mSuites.add(new TestCaseSpaceCall20());
        mSuites.add(new TestCaseSpaceCall21());
        mSuites.add(new TestCaseSpaceCall22());
        mSuites.add(new TestCaseSpaceCall23());

        mSuites.add(new TestCaseCallWhenRinging());
        mSuites.add(new TestCaseCallWhenConnected());
        mSuites.add(new TestCaseCallRejectWhenRinging());
        mSuites.add(new TestCaseCallRejectWhenInit());
        mSuites.add(new TestCaseHangUpDisconnectedCall());
        mSuites.add(new TestCaseAudioCall());
        mSuites.add(new TestCaseAudioCallUnmuteVideo());
        mSuites.add(new TestCaseMuteAudioVideo());
        //mSuites.add(new TestCaseRoom());
        mSuites.add(new TestCaseWebhooks());
        mSuites.add(new TestCaseTeamAndMemberShip());
        //mSuites.add(new TestCaseMultiParticipants_1());
        //mSuites.add(new TestCaseMultiParticipants_2());
        mSuites.add(new TestCaseCallSequence_1());
        mSuites.add(new TestCaseCallSequence_2());
        mSuites.add(new TestCaseKeepCall());

        TestCaseAdapter adapter = new TestCaseAdapter(this, mSuites);
        mListView.setAdapter(adapter);

        android.media.AudioManager am = (android.media.AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        am.setMode(android.media.AudioManager.MODE_IN_COMMUNICATION);
        am.setSpeakerphoneOn(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //requestPermissions();
        new Handler(Looper.myLooper()).postDelayed(() -> requestPermissions(), 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }

    public void requestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int permissionAudio = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);

        if (permissionCamera != PackageManager.PERMISSION_GRANTED
                || permissionAudio != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
            };
            ActivityCompat.requestPermissions(this, permissions, 0);
        }
    }


    public void update() {
        ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
    }

    public void runTest(View v) {
        int pos = mListView.getPositionForView(v);
        Test test = mSuites.get(pos);
        if (test instanceof TestSuite) {
            ViewGroup parent = (ViewGroup) v.getParent();
            ViewGroup parent_parent = (ViewGroup) parent.getParent();
            int index = parent_parent.indexOfChild(parent);
            mRunner.run((TestCase) ((TestSuite) test).get(index));
        } else {
            mRunner.run((TestCase) test);
        }

        this.update();
        moveToSuite(v);
    }

    public void moveToSuite(View v) {
        int pos = mListView.getPositionForView(v);
        mListView.smoothScrollToPositionFromTop(pos, 0, 500);
    }

    private class TestCaseAdapter extends ArrayAdapter<TestSuite> {

        public TestCaseAdapter(Context context, List<TestSuite> objects) {
            super(context, R.layout.listview_testsuite, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            TestSuite testSuite = getItem(position);

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_testsuite, null);
            String desc = String.format("%d", position);
            convertView.setContentDescription(desc);
            desc = String.format("%d. %s", position + 1, testSuite.getDescription());
            ((TextView) convertView.findViewById(R.id.textView)).setText(desc);
            LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.subListView);
            for (TestCase t : testSuite.cases()) {
                View child = getLayoutInflater().inflate(R.layout.listview_testcase, null);
                desc = String.format("%d", testSuite.cases().indexOf(t));
                child.setContentDescription(desc);
                ViewHolder holder = ViewHolder.createInstance(child);
                ViewHolder.updateViewHolder(holder, t);
                layout.addView(child);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        Button button;
        TextView textView;
        ProgressBar progress;
        View indicator;

        static ViewHolder createInstance(View root) {
            ViewHolder holder = new ViewHolder();

            holder.button = (Button) root.findViewById(R.id.run);
            holder.textView = (TextView) root.findViewById(R.id.textView);
            holder.progress = (ProgressBar) root.findViewById(R.id.progressBar);
            holder.indicator = root.findViewById(R.id.indicator);

            return holder;
        }

        static void updateViewHolder(ViewHolder holder, TestCase testCase) {
            holder.textView.setText(testCase.getDescription());
            holder.textView.setContentDescription(testCase.getTestClass().getName());

            TestState state = testCase.getState();
            switch (state) {
                case NotRun:
                    holder.progress.setVisibility(View.INVISIBLE);
                    holder.button.setText("RUN");
                    holder.indicator.setBackgroundResource(android.R.drawable.presence_away);
                    break;
                case Running:
                    holder.progress.setVisibility(View.VISIBLE);
                    holder.button.setEnabled(false);
                    holder.button.setText("RUNNING");
                    holder.indicator.setBackgroundResource(android.R.drawable.presence_online);
                    break;
                case Success:
                    holder.progress.setVisibility(View.INVISIBLE);
                    holder.button.setEnabled(true);
                    holder.button.setTextColor(Color.parseColor("#008800"));
                    holder.button.setText("PASSED");
                    holder.indicator.setBackgroundResource(android.R.drawable.presence_online);
                    break;
                case Failed:
                    holder.progress.setVisibility(View.INVISIBLE);
                    holder.button.setEnabled(true);
                    holder.button.setText("FAILED");
                    holder.button.setTextColor(Color.parseColor("#880000"));
                    holder.indicator.setBackgroundResource(android.R.drawable.presence_busy);
                    break;
            }
        }
    }
}
