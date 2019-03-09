package com.fournotfive.autoproducerconsumer;

import java.util.List;

import com.fournotfive.autoproducerconsumer.consumer.ConsumerBean;
import com.fournotfive.autoproducerconsumer.producer.ProducerBean;

public class ConfigBean {
	private String kafkabrokers;
	private int poll;
	private String offsetlatest;
	private String offsetearliest;
	private List<ProducerBean> pjobs;
	private List<ConsumerBean> cjobs;
	public String getKafkabrokers() {
		return kafkabrokers;
	}
	public void setKafkabrokers(String kafkabrokers) {
		this.kafkabrokers = kafkabrokers;
	}
	public int getPoll() {
		return poll;
	}
	public void setPoll(int poll) {
		this.poll = poll;
	}
	public String getOffsetlatest() {
		return offsetlatest;
	}
	public void setOffsetlatest(String offsetlatest) {
		this.offsetlatest = offsetlatest;
	}
	public String getOffsetearliest() {
		return offsetearliest;
	}
	public void setOffsetearliest(String offsetearliest) {
		this.offsetearliest = offsetearliest;
	}
	public List<ProducerBean> getPjobs() {
		return pjobs;
	}
	public void setPjobs(List<ProducerBean> pjobs) {
		this.pjobs = pjobs;
	}
	public List<ConsumerBean> getCjobs() {
		return cjobs;
	}
	public void setCjobs(List<ConsumerBean> cjobs) {
		this.cjobs = cjobs;
	}

}
