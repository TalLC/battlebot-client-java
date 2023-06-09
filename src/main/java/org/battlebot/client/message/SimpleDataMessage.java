package org.battlebot.client.message;

public class SimpleDataMessage<T> extends CoreDataMessage{
	private Data<T> data;

	public Data<T> getData() {
		return data;
	}

	public void setData(Data<T> data) {
		this.data = data;
	}
	
	
	
	@Override
	public String toString() {
		return "Message [" + getMsg_type() +"] : "+ data.getValue();
	}
}
