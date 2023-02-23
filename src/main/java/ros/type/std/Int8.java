package ros.type.std;

import org.json.JSONObject;
import ros.type.RosMessage;

public class Int8 extends RosMessage {
	public static final String FIELD_DATA = "data";

	public static final String TYPE = "std_msgs/Int8";

	private final byte data;

	/**
	 * Create a new Int8 with a default of 0.
	 */
	public Int8() {
		this((byte) 0);
	}

	/**
	 * Create a new Int8 with the given data value.
	 * 
	 * @param data
	 *            The data value of the byte.
	 */
	public Int8(byte data) {
		// build the JSON object
		super(builder().put(Int8.FIELD_DATA, data),
				Int8.TYPE);
		this.data = data;
	}

	public byte getData() {
		return this.data;
	}

	@Override
	public Int8 clone() {
		return new Int8(this.data);
	}

	public static Int8 fromJsonString(String jsonString) {
		return Int8.fromMessage(new RosMessage(jsonString));
	}

	public static Int8 fromMessage(RosMessage m) {
		return Int8.fromJSONObject(m.toJSONObject());
	}

	public static Int8 fromJSONObject(JSONObject jsonObject) {
		// check the fields
		byte data = jsonObject.has(Int8.FIELD_DATA) ? (byte) jsonObject
				.getInt(Int8.FIELD_DATA) : 0;
		return new Int8(data);
	}
}
