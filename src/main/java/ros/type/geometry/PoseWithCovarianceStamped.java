package ros.type.geometry;


import org.json.JSONObject;
import ros.type.RosMessage;
import ros.type.std.Header;

public class PoseWithCovarianceStamped extends RosMessage {
    public static final String FIELD_HEADER = "header";
    public static final String FIELD_POSE = "pose";

    public static final String TYPE = "geometry_msgs/PoseWithCovarianceStamped";

    private final Header header;
    private final PoseWithCovariance pose;


    public PoseWithCovarianceStamped() {
        this(new Header(), new PoseWithCovariance());
    }

    /**
     * Create a new PoseWithCovarianceStamped with the given values.
     *
     * @param header The header value of the pose.
     * @param pose   The pose value of the pose.
     */
    public PoseWithCovarianceStamped(Header header, PoseWithCovariance pose) {
        // build the JSON object
        super(builder().put(PoseWithCovarianceStamped.FIELD_HEADER, header.toJSONObject()).put(PoseWithCovarianceStamped.FIELD_POSE, pose.toJSONObject()), PoseWithCovarianceStamped.TYPE);
        this.header = header;
        this.pose = pose;
    }

    public static PoseWithCovarianceStamped fromJsonString(String jsonString) {
        // convert to a message
        return PoseWithCovarianceStamped.fromMessage(new RosMessage(jsonString));
    }

    public static PoseWithCovarianceStamped fromMessage(RosMessage m) {
        // get it from the JSON object
        return PoseWithCovarianceStamped.fromJSONObject(m.toJSONObject());
    }


    public static PoseWithCovarianceStamped fromJSONObject(JSONObject jsonObject) {
        // check the fields
        Header header = jsonObject.has(PoseWithCovarianceStamped.FIELD_HEADER) ? Header.fromJSONObject(jsonObject.getJSONObject(PoseWithCovarianceStamped.FIELD_HEADER)) : new Header();
        PoseWithCovariance pose = jsonObject.has(PoseWithCovarianceStamped.FIELD_POSE) ? PoseWithCovariance.fromJSONObject(jsonObject.getJSONObject(PoseWithCovarianceStamped.FIELD_POSE)) : new PoseWithCovariance();
        return new PoseWithCovarianceStamped(header, pose);
    }

    public Header getHeader() {
        return this.header;
    }

    public PoseWithCovariance getPose() {
        return this.pose;
    }

    @Override
    public PoseWithCovarianceStamped clone() {
        return new PoseWithCovarianceStamped(this.header, this.pose);
    }
}
