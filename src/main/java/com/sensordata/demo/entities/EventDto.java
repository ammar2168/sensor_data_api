package com.sensordata.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EventDto {

    private String uuid;
    private int level;
    private String createdAt;

    public EventDto() {
    }

    public EventDto(String uuid, int level, String createdAt) {
        this.uuid = uuid;
        this.level = level;
        this.createdAt = createdAt;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
