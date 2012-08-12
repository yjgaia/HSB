package kr.swmaestro.hsb.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TestModel {

	private String test;

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

}
