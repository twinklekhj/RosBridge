package io.github.twinklekhj.ros.ws;

import io.github.twinklekhj.ros.op.RosResponse;
import io.github.twinklekhj.ros.op.RosService;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class RosApi {
    private static final Logger logger = LoggerFactory.getLogger(RosApi.class);

    /**
     * [RosApi] ROS Topic 목록 조회
     *
     * @param bridge RosBridge
     * @return callbacks
     */
    public static Promise<List<String>> getTopics(RosBridge bridge) {
        return getSimpleList(bridge, Type.TOPICS);
    }

    /**
     * [RosApi] Topic 목록 조회
     *
     * @param bridge RosBridge
     * @param type 타입유형
     * @return callbacks
     */
    public static Promise<List<String>> getTopicsForType(RosBridge bridge, String type) {
        return getSimpleList(bridge, Type.TOPICS_FOR_TYPE, type);
    }

    /**
     * [RosApi] Topic Publisher 목록 조회
     *
     * @param bridge RosBridge
     * @param topic 토픽명
     * @return callbacks
     */
    public static Promise<List<String>> getPublishers(RosBridge bridge, String topic) {
        return getSimpleList(bridge, Type.PUBLISHERS, topic);
    }

    /**
     * [RosApi] Topic Subscriber 목록 조회
     *
     * @param bridge RosBridge
     * @param topic 토픽명
     * @return callbacks
     */
    public static Promise<List<String>> getSubscribers(RosBridge bridge, String topic) {
        return getSimpleList(bridge, Type.SUBSCRIBERS, topic);
    }

    /**
     * [RosApi] Service 목록 조회
     *
     * @param bridge RosBridge
     * @return 결과
     */
    public static Promise<List<String>> getServices(RosBridge bridge) {
        return getSimpleList(bridge, Type.SERVICES);
    }

    /**
     * [RosApi] Service 목록 조회
     *
     * @param bridge RosBridge
     * @param type 타입명
     * @return 결과
     */
    public static Promise<List<String>> getServicesForType(RosBridge bridge, String type) {
        return getSimpleList(bridge, Type.SERVICES_FOR_TYPE, type);
    }

    /**
     * [RosApi] Service Provider 목록 조회
     *
     * @param bridge RosBridge
     * @param service 서비스명
     * @return 결과
     */
    public static Promise<List<String>> getServiceProviders(RosBridge bridge, String service) {
        return getSimpleList(bridge, Type.SERVICE_PROVIDERS, service);
    }

    /**
     * [RosApi] Service Type 조회
     *
     * @param bridge RosBridge
     * @param service 서비스명
     * @return 결과
     */
    public static Promise<String> getServiceType(RosBridge bridge, String service) {
        return getSimpleValue(bridge, Type.SERVICE_TYPE, service);
    }

    /**
     * [RosApi] Service Host 조회
     *
     * @param bridge RosBridge
     * @param service 서비스명
     * @return 결과
     */
    public static Promise<String> getServiceHost(RosBridge bridge, String service) {
        return getSimpleValue(bridge, Type.SERVICE_HOST, service);
    }

    /**
     * [RosApi] Service Node 조회
     *
     * @param bridge RosBridge
     * @param service 서비스명
     * @return 결과
     */
    public static Promise<String> getServiceNode(RosBridge bridge, String service) {
        return getSimpleValue(bridge, Type.SERVICE_NODE, service);
    }

    /**
     * [RosApi] Action Server 목록 조회
     *
     * @param bridge RosBridge
     * @return 결과
     */
    public static Promise<List<String>> getActionServers(RosBridge bridge) {
        return getSimpleList(bridge, Type.ACTION_SERVERS);
    }

    /**
     * [RosApi] Node 목록 조회
     *
     * @param bridge RosBridge
     * @return 결과
     */
    public static Promise<List<String>> getNodes(RosBridge bridge) {
        return getSimpleList(bridge, Type.NODES);
    }

    /**
     * [RosApi] ROS API Simple 목록 조회
     *
     * @return 결과
     */
    private static Promise<List<String>> getSimpleList(RosBridge bridge, RosService service, String prop) {
        Promise<List<String>> promise = Promise.promise();

        bridge.callService(service).future().compose(response -> {
            List<String> result = (List<String>) response.getValues().get(prop);
            if (result == null) {
                result = Collections.emptyList();
            }
            return Future.succeededFuture(result);
        }).onSuccess(promise::complete).onFailure(promise::fail);

        return promise;
    }

    private static Promise<List<String>> getSimpleList(RosBridge bridge, Type api, Object... args) {
        RosService service = RosService.builder(api.getService()).args(args).build();
        return getSimpleList(bridge, service, api.getProp());
    }

    private static Promise<String> getSimpleValue(RosBridge bridge, Type api, Object... args) {
        Promise<String> promise = Promise.promise();
        RosService service = RosService.builder(api.getService()).args(args).build();

        bridge.callService(service).future().compose(response -> {
            String result = response.getValues().get(api.getProp()).toString();
            return Future.succeededFuture(result);
        }).onSuccess(promise::complete).onFailure(promise::fail);

        return promise;
    }

    /**
     * [RosApi] Node 상세 정보 조회
     *
     * @param bridge RosBridge
     * @param node 찾을 노드명
     * @return 콜백함수
     */
    public static Promise<RosResponse> getNodeDetails(RosBridge bridge, String node) {
        RosService service = RosService.builder("/rosapi/node_details").args(node).build();
        return bridge.callService(service);
    }


    public enum Type {
        NODES("/rosapi/nodes", "nodes"),
        TOPICS("/rosapi/topics", "topics"),
        TOPICS_FOR_TYPE("/rosapi/topics_for_type", "topics"),
        PUBLISHERS("/rosapi/publishers", "publishers"),
        SUBSCRIBERS("/rosapi/subscribers", "subscribers"),
        SERVICES("/rosapi/services", "services"),
        SERVICE_TYPE("/rosapi/service_type", "type"),
        SERVICE_HOST("/rosapi/service_host", "host"),
        SERVICE_NODE("/rosapi/service_node", "node"),
        SERVICE_PROVIDERS("/rosapi/service_providers", "providers"),
        SERVICES_FOR_TYPE("/rosapi/services_for_type", "services"),
        ACTION_SERVERS("/rosapi/get_action_servers", "action_servers"),
        ;

        private final String service;
        private final String[] props;

        Type(String service, String... props) {
            this.service = service;
            this.props = props;
        }

        public String getService() {
            return service;
        }

        public String getProp() {
            return props.length > 0 ? props[0] : "";
        }
    }
}
