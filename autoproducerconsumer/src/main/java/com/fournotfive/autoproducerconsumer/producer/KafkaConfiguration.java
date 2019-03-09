package com.fournotfive.autoproducerconsumer.producer;

import com.fournotfive.autoproducerconsumer.consumer.ConsumerBean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class KafkaConfiguration {

    private String kafkaBroker;
    private String poll;
    private String offsetLatest;
    private String offsetEarliest;
    private List<ProducerBean> pjobs = new ArrayList<ProducerBean>();
    private List<ConsumerBean> cjobs = new ArrayList<ConsumerBean>();

    public String getKafkaBroker() {
        return kafkaBroker;
    }

    public void setKafkaBroker(String kafkaBroker) {
        this.kafkaBroker = kafkaBroker;
    }

    public String getPoll() {
        return poll;
    }

    public void setPoll(String poll) {
        this.poll = poll;
    }

    public String getOffsetLatest() {
        return offsetLatest;
    }

    public void setOffsetLatest(String offsetLatest) {
        this.offsetLatest = offsetLatest;
    }

    public String getOffsetEarliest() {
        return offsetEarliest;
    }

    public void setOffsetEarliest(String offsetEarliest) {
        this.offsetEarliest = offsetEarliest;
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
