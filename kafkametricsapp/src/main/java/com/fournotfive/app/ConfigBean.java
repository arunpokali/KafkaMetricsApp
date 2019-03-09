package com.fournotfive.app;

import java.util.List;

public class ConfigBean {
	private String brokers;
	private String zookeeper;
	private List<String> topics;
	private List<String> groups;
	private List<String> httpreporter;
	private boolean httpenabled;



	public List<String> getHttpreporter() {
		return httpreporter;
	}

	public void setHttpreporter(List<String> httpreporter) {
		this.httpreporter = httpreporter;
	}

	public String getBrokers() {
		return brokers;
	}

	public void setBrokers(String brokers) {
		this.brokers = brokers;
	}

	public String getZookeeper() {
		return zookeeper;
	}

	public void setZookeeper(String zookeeper) {
		this.zookeeper = zookeeper;
	}

	public List<String> getTopics() {
		return topics;
	}

	public void setTopics(List<String> topics) {
		this.topics = topics;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public boolean isHttpenabled() {
		return httpenabled;
	}

	public void setHttpenabled(boolean httpenabled) {
		this.httpenabled = httpenabled;
	}
}
