package io.github.twinklekhj.ros.ws;

import io.github.twinklekhj.ros.op.RosSubscription;
import io.github.twinklekhj.ros.op.RosTopic;
import io.github.twinklekhj.ros.type.actionlib.Goal;
import io.github.twinklekhj.ros.type.actionlib.GoalID;
import io.github.twinklekhj.ros.type.actionlib.GoalStatus;
import io.github.twinklekhj.ros.type.actionlib.GoalStatusArray;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ActionClient {
    @NonNull
    private final RosBridge bridge;
    @NonNull
    private String serverName;
    @NonNull
    private String actionName;
    @Builder.Default
    private int timeout = 0;
    @Builder.Default
    private boolean omitFeedback = false;
    @Builder.Default
    private boolean omitStatus = false;
    @Builder.Default
    private boolean omitResult = false;
    private Map<String, Goal> goals = new HashMap<>();

    // flag to check if a status has been received
    private boolean receivedStatus = false;

    private static ActionClientBuilder builder() {
        return new ActionClientBuilder();
    }

    public static ActionClientBuilder builder(RosBridge bridge, String serverName, String actionName) {
        return builder().bridge(bridge).serverName(serverName).actionName(actionName);
    }

    private String getTopicName(String topic) {
        return String.format("%s/%s", serverName, topic);
    }

    private String getActionName(String action) {
        return String.format("%s/%s", actionName, action);
    }

    public RosTopic getGoalTopic(){
        return RosTopic.builder(getTopicName("/goal"), getActionName("Goal")).build();
    }

    public RosTopic getCancelTopic(){
        return RosTopic.builder(getTopicName("/cancel"), GoalID.TYPE).build();
    }

    public void init() {
        EventBus bus = bridge.getBus();

        RosTopic goal = getGoalTopic();
        RosTopic cancel = getCancelTopic();

        // advertise the goal and cancel topics
        bridge.advertise(goal);
        bridge.advertise(cancel);

        RosSubscription feedback = RosSubscription.builder(getTopicName("/feedback"), getActionName("Feedback")).build();
        RosSubscription status = RosSubscription.builder(getTopicName("/status"), GoalStatusArray.TYPE).build();
        RosSubscription result = RosSubscription.builder(getTopicName("/result"), getActionName("Result")).build();

        if (!this.omitStatus) {
            bridge.subscribe(status, message -> {
                GoalStatusArray goalStatusArray = GoalStatusArray.fromJsonObject(message.body().getJsonObject("msg"));
                this.receivedStatus = true;

                for (GoalStatus goalStatus : goalStatusArray.getGoalStatus()) {
                    String id = goalStatus.getGoalID().getId();
                    if(this.goals.containsKey(id)){
                        this.goals.get(id).setStatus(goalStatus);
                    }
                }
            });
        }

        if(!this.omitResult){
            bridge.subscribe(result, message -> {
                JsonObject obj = message.body();
                GoalStatus resultStatus = GoalStatus.fromJsonObject(obj.getJsonObject("status"));
                JsonObject resultResult = obj.getJsonObject("result");

                String id = resultStatus.getGoalID().getId();
                if(this.goals.containsKey(id)){
                    this.goals.get(id).setStatus(resultStatus);
                    this.goals.get(id).setResult(resultResult);
                }
            });
        }

        if(!this.omitFeedback){
            bridge.subscribe(feedback, message -> {
                JsonObject obj = message.body();
                GoalStatus resultStatus = GoalStatus.fromJsonObject(obj.getJsonObject("status"));
                JsonObject resultFeedback = obj.getJsonObject("feedback");

                String id = resultStatus.getGoalID().getId();
                if(this.goals.containsKey(id)){
                    this.goals.get(id).setStatus(resultStatus);
                    this.goals.get(id).setFeedback(resultFeedback);
                }
            });
        }
    }

    public void destroy(){
        RosTopic goal = getGoalTopic();
        RosTopic cancel = getCancelTopic();

        // advertise the goal and cancel topics
        bridge.unadvertise(goal);
        bridge.unadvertise(cancel);
    }

    public void send(JsonObject msg){
        RosTopic goalTopic = getGoalTopic();
        Goal goal = new Goal();
        goal.setGoal(msg);

        this.goals.put(goal.getGoalID().getId(), goal);
        bridge.publish(goalTopic);
    }

    public void cancel(Goal goal){
        RosTopic cancel = getCancelTopic();
        cancel.setMsg(new JsonObject().put("id", goal.getGoalID()));

        bridge.publish(cancel);
    }
}
