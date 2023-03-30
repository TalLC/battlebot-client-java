package org.battlebot.client.message.command;

public class ErrorResponse {

	private Detail detail;

	public class Detail {
		private String name;
		private int internal_code;
		private String label;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getInternal_code() {
			return internal_code;
		}

		public void setInternal_code(int internal_code) {
			this.internal_code = internal_code;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String toString() {
			return internal_code + " - " + name + " : " + label;
		}
	}

	public Detail getDetail() {
		return detail;
	}

	public void setDetail(Detail detail) {
		this.detail = detail;
	}
	
	public String toString() {
		return getDetail().toString();
	}
}
