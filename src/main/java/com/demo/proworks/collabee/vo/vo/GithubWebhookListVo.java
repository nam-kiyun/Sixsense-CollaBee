package com.demo.proworks.collabee.vo.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "깃허브_웹훅")
public class GithubWebhookListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "깃허브_웹훅List", physicalName = "githubWebhookVoList", type = "com.demo.proworks.collabee.vo.GithubWebhookVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.collabee.vo.vo.GithubWebhookVo> githubWebhookVoList;

    public java.util.List<com.demo.proworks.collabee.vo.vo.GithubWebhookVo> getGithubWebhookVoList(){
        return githubWebhookVoList;
    }

    public void setGithubWebhookVoList(java.util.List<com.demo.proworks.collabee.vo.vo.GithubWebhookVo> githubWebhookVoList){
        this.githubWebhookVoList = githubWebhookVoList;
    }

    @Override
    public String toString() {
        return "GithubWebhookListVo [githubWebhookVoList=" + githubWebhookVoList+ "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
