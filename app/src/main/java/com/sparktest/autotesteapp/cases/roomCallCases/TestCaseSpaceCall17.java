package com.sparktest.autotesteapp.cases.roomCallCases;

import com.ciscospark.androidsdk.CompletionHandler;
import com.ciscospark.androidsdk.Result;
import com.ciscospark.androidsdk.membership.Membership;
import com.ciscospark.androidsdk.phone.Call;
import com.ciscospark.androidsdk.phone.CallObserver;
import com.ciscospark.androidsdk.phone.MediaOption;
import com.github.benoitdion.ln.Ln;
import com.sparktest.autotesteapp.framework.TestSuite;
import com.sparktest.autotesteapp.framework.Verify;
import com.sparktest.autotesteapp.framework.annotation.Description;
import com.sparktest.autotesteapp.framework.annotation.Test;
import com.sparktest.autotesteapp.utils.TestActor;

import me.helloworld.utils.Checker;

/**
 * Created by panzh on 27/12/2017.
 */

public class TestCaseSpaceCall17 extends TestSuite {

    /**
     step 1: P1 call space (contain P1, P2) \n
     step 2: P2 call P3 \n
     step 3:space add P3 \n
     step 4:P3 answer P2 hangup\n
     step 5:  answer space calling \n
     */

    public TestCaseSpaceCall17() {
        this.add(TestCaseSpaceCall17.TestActorCall17Person1.class);
        this.add(TestCaseSpaceCall17.TestActorCall17Person2.class);
        this.add(TestCaseSpaceCall17.TestActorCall17Person3.class);
    }

    @Description("TestActorCall17Person1")
    public static class TestActorCall17Person1 extends RoomCallingTestActor{
        private String personThreeMembershipID;
        private boolean personThreeInvited = false;
        /**
         * Main test entrance
         */
        @Test
        public void run() {
            super.run();
            actor = TestActor.SparkUser(activity, runner);
            actor.loginBySparkId(TestActor.sparkUser1,TestActor.SPARK_USER_PASSWORD,this::onRegistered);
        }

        /**
         *  Dial room when register complete
         */
        @Override
        protected void onRegistered(Result result) {
            Ln.d("Caller onRegistered result: %b" , result.isSuccessful());
            if (result.isSuccessful()) {
                actor.getPhone().dial(actor.SPARK_ROOM_CALL_ROOM_ID2, MediaOption.audioVideo(activity.mLocalSurface, activity.mRemoteSurface),
                        this::onCallSetup);
            } else {
                Verify.verifyTrue(false);
            }
        }

        @Override
        protected void onConnected(Call call) {
            super.onConnected(call);
            mHandler.postDelayed(() -> {
                this.invitePersonThree(call);
            },5000);
        }

        @Override
        protected void onCallMembershipChanged(CallObserver.CallEvent event){
            super.onCallMembershipChanged(event);
            if (event instanceof CallObserver.MembershipLeftEvent
                    && ((CallObserver.MembershipLeftEvent) event).getCallMembership().getPersonId().equalsIgnoreCase(actor.sparkUserID3)) {
                removePersonThreeFromRoom(event.getCall());
            }
        }

        @Override
        protected void onDisconnected(CallObserver.CallEvent event) {
            super.onDisconnected(event);
            actor.logout();
        }


        protected void invitePersonThree(Call call) {
            if (personThreeInvited) {
                return;
            }
            personThreeInvited = true;
            actor.getSpark().memberships().create(actor.SPARK_ROOM_CALL_ROOM_ID2, actor.sparkUserID3, null, false, new CompletionHandler<Membership>() {
                @Override
                public void onComplete(Result<Membership> result) {
                    Ln.d("onTeamMemberShipCreated: %b" , result.isSuccessful());
                    if (result.isSuccessful()) {
                        if (result.getData().getPersonEmail().equalsIgnoreCase(actor.sparkUser3)) {
                            personThreeMembershipID = result.getData().getId();
                            personThreeInvited = true;
                        }
                    } else {
                        personThreeInvited = false;
                        Verify.verifyTrue(false);
                    }
                }
            });
        }

        protected void removePersonThreeFromRoom(Call call) {
            if (!personThreeInvited || Checker.isEmpty(personThreeMembershipID)) {

                Verify.verifyTrue(false);
                mHandler.postDelayed(()->{
                    hangupCall(call);
                },25000);
                return;
            }

            actor.getSpark().memberships().delete(personThreeMembershipID, new CompletionHandler<Void>() {
                @Override
                public void onComplete(Result<Void> result) {
                    if (result.isSuccessful()) {
                        mHandler.postDelayed(()->{
                            hangupCall(call);
                        },25000);
                    }
                    else {
                        mHandler.postDelayed(()->{
                            removePersonThreeFromRoom(call);
                        },2000);
                    }
                }
            });
        }
    }

    @Description("TestActorCall17Person2")
    public static class TestActorCall17Person2 extends RoomCallingTestActor{
        /**
         * Main test entrance
         */
        @Test
        public void run() {
            super.run();
            actor = TestActor.SparkUser(activity, runner);
            actor.loginBySparkId(TestActor.sparkUser2,TestActor.SPARK_USER_PASSWORD,this::onRegistered);
        }

        /**
         *  Dial room when register complete
         */
        @Override
        protected void onRegistered(Result result) {
            Ln.d("Caller onRegistered result: %b" , result.isSuccessful());
            if (result.isSuccessful()) {
                actor.getPhone().dial(actor.sparkUser3, MediaOption.audioVideo(activity.mLocalSurface, activity.mRemoteSurface),
                        this::onCallSetup);
            } else {
                Verify.verifyTrue(false);
            }
        }

        @Override
        protected void onDisconnected(CallObserver.CallEvent event) {
            super.onDisconnected(event);
            if(event instanceof CallObserver.RemoteLeft) {
                actor.logout();
            }
        }
    }

    @Description("TestActorCall17Person3")
    public static class TestActorCall17Person3 extends RoomCallingTestActor {
        private Call roomCall = null;
        private Call personCall = null;
        private boolean leftOnce = false;
        @Test
        /**
         * Main test entrance
         */
        public void run() {
            super.run();
            actor = TestActor.SparkUser(activity, runner);
            actor.loginBySparkId(TestActor.sparkUser3,TestActor.SPARK_USER_PASSWORD,this::onRegistered);
        }

        /**
         *  Waiting for incoming call register complete
         */
        @Override
        protected void onRegistered(Result result) {
            Ln.d("Caller onRegistered result: %b" , result.isSuccessful());
            actor.getPhone().setIncomingCallListener(call -> {
                Ln.e("Incoming call");
                actor.onConnected(this::onConnected);
                actor.onMediaChanged(this::onMediaChanged);
                actor.onCallMembershipChanged(this::onCallMembershipChanged);
                actor.onDisconnected(this::onDisconnected);
                actor.setDefaultCallObserver(call);
                if(call.getFrom().getPersonId().equalsIgnoreCase(actor.sparkUserID2)) {
                    personCall = call;
                } else {
                    roomCall = call;
                }

                if (personCall != null && roomCall != null){
                    personCall.answer(MediaOption.audioVideo(activity.mLocalSurface, activity.mRemoteSurface), new CompletionHandler<Void>() {
                        @Override
                        public void onComplete(Result<Void> result) {
                            if (result.isSuccessful()) {
                                Ln.d("Call: Incoming call Detected");
                                Verify.verifyTrue(true);
                            } else {
                                Ln.d("Call: Answer call fail");
                                Verify.verifyTrue(false);
                                actor.logout();
                            }
                        }
                    });
                }
            });
        }

        @Override
        protected void onConnected(Call call) {
            super.onConnected(call);
            mHandler.postDelayed(()->{
                hangupCall(call);
            },5000);
        }

        @Override
        protected void onDisconnected(CallObserver.CallEvent event) {
            super.onDisconnected(event);
            if(event instanceof CallObserver.LocalLeft && !leftOnce) {
                leftOnce = true;
                roomCall.answer(MediaOption.audioVideo(activity.mLocalSurface, activity.mRemoteSurface), new CompletionHandler<Void>() {
                    @Override
                    public void onComplete(Result<Void> result) {
                        if (result.isSuccessful()) {
                            Ln.d("Call: Incoming call Detected");
                            Verify.verifyTrue(true);
                        } else {
                            Ln.d("Call: Answer call fail");
                            Verify.verifyTrue(false);
                            actor.logout();
                        }
                    }
                });
            }else if (event instanceof CallObserver.LocalLeft && leftOnce) {
                actor.logout();
            }
        }
    }
}
