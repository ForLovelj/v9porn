package com.u9porn.data.model.kedouwo;

import java.util.List;

public class KeDouRelated {

    private String videoUrl;

    private List<KeDouModel> relatedList;

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public List<KeDouModel> getRelatedList() {
        return relatedList;
    }

    public void setRelatedList(List<KeDouModel> relatedList) {
        this.relatedList = relatedList;
    }
}
