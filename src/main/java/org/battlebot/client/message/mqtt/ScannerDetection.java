package org.battlebot.client.message.mqtt;

import java.util.List;

public class ScannerDetection {
	private String msg_type;
	private String source;
	private List<Data> data;

	public String getMsg_type() {
		return msg_type;
	}

	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public static class Data {
		private float from;
		private float to;
		private String object_type;
		private String name;
		private float distance;

		public Data() {};
		
		public float getFrom() {
			return from;
		}

		public void setFrom(float from) {
			this.from = from;
		}

		public float getTo() {
			return to;
		}

		public void setTo(float to) {
			this.to = to;
		}

		public String getObject_type() {
			return object_type;
		}

		public void setObject_type(String object_type) {
			this.object_type = object_type;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public float getDistance() {
			return distance;
		}

		public void setDistance(float distance) {
			this.distance = distance;
		}

		@Override
		public String toString() {
			return name + "[" + object_type +"] - dist : " + distance + " - angle (" + from + "," + to + ")";
		}

	}

	@Override
	public String toString() {
		return "Scanner : " + data;
	}
}
