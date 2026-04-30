package com.offertalk.controller.dto.response;

import lombok.Data;

@Data
public class AppVersionResponse {
    private String version;
    private String versionName;
    private Boolean hasUpdate;
    private String updateContent;
    private String downloadUrl;
}
