package com.sparktest.autotesteapp.cases.roomCallCases;

import com.ciscospark.androidsdk.Result;
import com.ciscospark.androidsdk.phone.Call;
import com.ciscospark.androidsdk.phone.CallMembership;
import com.ciscospark.androidsdk.phone.CallObserver;
import com.ciscospark.androidsdk.phone.MediaOption;
import com.github.benoitdion.ln.Ln;
import com.sparktest.autotesteapp.framework.TestSuite;
import com.sparktest.autotesteapp.framework.Verify;
import com.sparktest.autotesteapp.framework.annotation.Description;
import com.sparktest.autotesteapp.framework.annotation.Test;
import com.sparktest.autotesteapp.utils.TestActor;

/**
 * Created by panzh on 21/12/2017.
 */

public class TestCaseSpaceCall3 extends TestSuite {
    /**
     step 1:P1,P2,P3 call the same room \n
     step 2: P2 mute \n
     step 3:P3 mute \n
     step 4: P2 unmute \n
     step 5: P1 left \n
     step 6: P2 left \n
     step 7: P3 left
     */
    public TestCaseSpaceCall3() {
        this.add(TestCaseSpaceCall3.TestActorCall3Person1.class);
        this.add(TestCaseSpaceCall3.TestActorCall3Person2.class);
        this.add(TestCaseSpaceCall3.TestActorCall3Person3.class);
    }

    @Description("TestActorCall3Person1")
    public static class TestActorCall3Person1 extends RoomCallingTestActor{
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
                actor.getPhone().dial(actor.SPARK_ROOM_CALL_ROOM_ID, MediaOption.audioVideo(activity.mLocalSurface, activity.mRemoteSurface),
                        this::onCallSetup);
            } else {
                Verify.verifyTrue(false);
            }
        }

        @Override
        protected void onCallMembershipChanged(CallObserver.CallEvent event){
            super.onCallMembershipChanged(event);
            if (event instanceof CallObserver.MembershipSendingVideoEvent) {
                CallMembership membership = ((CallObserver.MembershipSendingVideoEvent) event).getCallMembership();
                if (membership.getPersonId().equalsIgnoreCase(actor.sparkUserID2)
                        && this.person2Joined && this.person3Joined
                        && membership.isSendingVideo() == true) {
                    Ln.d("Call: Person2 Unmuted Detected");
                    hangupCall(event.getCall());
                }
            }
        }

        @Override
        protected void onDisconnected(CallObserver.CallEvent event) {
            super.onDisconnected(event);
            actor.logout();
        }

    }

    @Description("TestActorCall3Person2")
    public static class TestActorCall3Person2 extends RoomCallingTestActor {

        @Test
        /**
         * Main test entrance
         */
        public void run() {
            super.run();
            actor = TestActor.SparkUser(activity, runner);
            actor.loginBySparkId(TestActor.sparkUser2,TestActor.SPARK_USER_PASSWORD,this::onRegistered);
        }

        /**
         *  Waiting for incoming call register complete
         */
        @Override
        protected void onRegistered(Result result) {
            Ln.d("Caller onRegistered result: %b" , result.isSuccessful());
            if (result.isSuccessful()) {
                actor.getPhone().dial(actor.SPARK_ROOM_CALL_ROOM_ID,MediaOption.audioVideo(activity.mLocalSurface, activity.mRemoteSurface),
                        this::onCallSetup);
            } else {
                Verify.verifyTrue(false);
            }
        }

        @Override
        protected void onCallMembershipChanged(CallObserver.CallEvent event){
            super.onCallMembershipChanged(event);
            if (event instanceof CallObserver.MembershipSendingVideoEvent) {
                CallMembership membership = ((CallObserver.MembershipSendingVideoEvent) event).getCallMembership();
                if(membership.getPersonId().equalsIgnoreCase(actor.sparkUserID3)
                        && membership.isSendingVideo() == false
                        && this.person1Joined && this.person3Joined){
                    event.getCall().setSendingVideo(true);
                }
            } else if (event instanceof CallObserver.MembershipJoinedEvent
                    && this.person1Joined && this.person3Joined) {
                event.getCall().setSendingVideo(false);
            } else if (event instanceof CallObserver.MembershipLeftEvent
                    && ((CallObserver.MembershipLeftEvent) event).getCallMembership().getPersonId().equalsIgnoreCase(actor.sparkUserID1)) {
                if (this.person1Joined && this.person3Joined ) {
                    Ln.d("Call: Person2 Start Leaving");
                    hangupCall(event.getCall());
                }
            }
        }

        @Override
        protected void onDisconnected(CallObserver.CallEvent event) {
            super.onDisconnected(event);
            actor.logout();
        }

        @Override
        protected void checkMemberships(Call call) {
            super.checkMemberships(call);
            if (this.person1Joined && this.person3Joined) {
                call.setSendingVideo(false);
            }
            for(CallMembership membership : call.getMemberships()) {
                if (membership.getPersonId().equalsIgnoreCase(actor.sparkUserID1)
                        && this.person1Joined && this.person3Joined
                        && membership.getState() == CallMembership.State.LEFT) {
                    Ln.d("Call: Person2 hangup");
                    hangupCall(call);
                }
            }
        }
    }

    @Description("TestActorCall3Person3")
    public static class TestActorCall3Person3 extends RoomCallingTestActor {

        /**
         * Main test entrance
         */
        @Test
        public void run() {
            super.run();
            actor = TestActor.SparkUser(activity, runner);
            actor.loginBySparkId(TestActor.sparkUser3,TestActor.SPARK_USER_PASSWORD,this::onRegistered);
        }

        /**
         *  Dial room when register complete
         */
        @Override
        protected void onRegistered(Result result) {
            Ln.d("Caller onRegistered result: %b" , result.isSuccessful());
            if (result.isSuccessful()) {
                actor.getPhone().dial(actor.SPARK_ROOM_CALL_ROOM_ID,MediaOption.audioVideo(activity.mLocalSurface, activity.mRemoteSurface),
                        this::onCallSetup);
            } else {
                Verify.verifyTrue(false);
            }
        }

        @Override
        protected void onCallMembershipChanged(CallObserver.CallEvent event){
            super.onCallMembershipChanged(event);
            if (event instanceof CallObserver.MembershipSendingVideoEvent) {
                CallMembership membership = ((CallObserver.MembershipSendingVideoEvent) event).getCallMembership();
                if(membership.getPersonId().equalsIgnoreCase(actor.sparkUserID2)
                        && membership.isSendingVideo() == false) {
                    event.getCall().setSendingVideo(false);
                }
            }
            if (event instanceof CallObserver.MembershipLeftEvent) {
                if (((CallObserver.MembershipLeftEvent) event).getCallMembership().getPersonId().equalsIgnoreCase(actor.sparkUserID2)
                        && this.person1Joined && this.person2Joined ) {
                    Ln.d("Call: Person2 Left Detected");
                    hangupCall(event.getCall());
                }
            }
        }

        @Override
        protected void onDisconnected(CallObserver.CallEvent event) {
            super.onDisconnected(event);
            actor.logout();
        }

        @Override
        protected void checkMemberships(Call call) {
            super.checkMemberships(call);
            for(CallMembership membership:call.getMemberships()) {
                if (membership.getPersonId().equalsIgnoreCase(actor.sparkUserID2)) {
                    if (membership.getState() == CallMembership.State.LEFT) {
                        hangupCall(call);
                    }
                    else if(membership.isSendingVideo() == false && this.person1Joined && this.person2Joined){
                        call.setSendingVideo(false);
                    }
                }
            }
        }
    }

}
