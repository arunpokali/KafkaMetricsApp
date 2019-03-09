package com.fournotfive.app.beans;

public class ConsumerGroupMetricBean {
	private String consumerGroupId;
	private String topicName;
	private int partitionNo;
	private long currentOffset; // Consumer
	private long endOffset; // Producer
	private long lag;

	public ConsumerGroupMetricBean() {
	}

	public ConsumerGroupMetricBean(String consumerGroupId, String topicName, int partitionNo, long currentOffset,
			long endOffset) {
		super();
		this.consumerGroupId = consumerGroupId;
		this.topicName = topicName;
		this.partitionNo = partitionNo;
		this.currentOffset = currentOffset;
		this.endOffset = endOffset;
		lag=endOffset-currentOffset;
	}

	public String getConsumerGroupId() {
		return consumerGroupId;
	}

	public void setConsumerGroupId(String consumerGroupId) {
		this.consumerGroupId = consumerGroupId;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public int getPartitionNo() {
		return partitionNo;
	}

	public void setPartitionNo(int partitionNo) {
		this.partitionNo = partitionNo;
	}

	public long getCurrentOffset() {
		return currentOffset;
	}

	public void setCurrentOffset(int currentOffset) {
		this.currentOffset = currentOffset;
	}

	public long getEndOffset() {
		return endOffset;
	}

	public void setEndOffset(int endOffset) {
		this.endOffset = endOffset;
	}

	public long getLag() {
		return lag;
	}

	public void setLag(int lag) {
		this.lag = lag;
	}
	
	public String getProducerOffsetKeyValue() {
		return toString()+endOffset;
	}
	public String getConsumerOffsetKeyValue() {
		return toString()+currentOffset;
	}
	public String getConsumerLagKeyValue() {
		return toString()+lag;
	}
	
	public String getKeyTypeA() {
		return "{consumergroup=\""+consumerGroupId+"\",partition=\""+partitionNo+"\",topic=\""+topicName+"\"}";
	}
	
	@Override
	public String toString() {
		return consumerGroupId+"__"+topicName+"__"+partitionNo+"__";
	}

}
