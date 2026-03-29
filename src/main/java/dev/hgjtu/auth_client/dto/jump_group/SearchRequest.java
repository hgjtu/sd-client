package dev.hgjtu.auth_client.dto.jump_group;

import java.time.LocalDateTime;
import java.util.List;

public class SearchRequest {
    Integer page = 0;
    Integer size = 10;
    String sort = "jumpDateTime";
    String direction = "DESC";
    List<String> placesList;
    Short trainingLevel;
    LocalDateTime jumpDateTimeStart;
    LocalDateTime jumpDateTimeEnd;
    Boolean isParticipant = Boolean.FALSE;

    public SearchRequest() {
    }

    public SearchRequest(Integer page, Integer size, String sort, String direction, List<String> placesList, Short trainingLevel, LocalDateTime jumpDateTimeStart, LocalDateTime jumpDateTimeEnd, Boolean isParticipant) {
        this.page = page;
        this.size = size;
        this.sort = sort;
        this.direction = direction;
        this.placesList = placesList;
        this.trainingLevel = trainingLevel;
        this.jumpDateTimeStart = jumpDateTimeStart;
        this.jumpDateTimeEnd = jumpDateTimeEnd;
        this.isParticipant = isParticipant;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<String> getPlacesList() {
        return placesList;
    }

    public void setPlacesList(List<String> placesList) {
        this.placesList = placesList;
    }

    public Short getTrainingLevel() {
        return trainingLevel;
    }

    public void setTrainingLevel(Short trainingLevel) {
        this.trainingLevel = trainingLevel;
    }

    public LocalDateTime getJumpDateTimeStart() {
        return jumpDateTimeStart;
    }

    public void setJumpDateTimeStart(LocalDateTime jumpDateTimeStart) {
        this.jumpDateTimeStart = jumpDateTimeStart;
    }

    public LocalDateTime getJumpDateTimeEnd() {
        return jumpDateTimeEnd;
    }

    public void setJumpDateTimeEnd(LocalDateTime jumpDateTimeEnd) {
        this.jumpDateTimeEnd = jumpDateTimeEnd;
    }

    public Boolean getParticipant() {
        return isParticipant;
    }

    public void setParticipant(Boolean participant) {
        isParticipant = participant;
    }
}
