package com.sensordata.demo.model;

import javax.validation.constraints.NotEmpty;

import java.io.Serializable;

public class SensorInput implements Serializable {

	@NotEmpty
	private Integer co2;
	private String time;

	public SensorInput() {
	}

	public SensorInput(@NotEmpty Integer co2, String time) {
		this.co2 = co2;
		this.time = time;
	}

	public Integer getCo2() {
		return co2;
	}

	public void setCo2(Integer co2) {
		this.co2 = co2;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
