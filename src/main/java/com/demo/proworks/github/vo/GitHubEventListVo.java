package com.demo.proworks.github.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;
import java.util.List;

/**
 * GitHub 이벤트 목록 VO
 */
@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "GitHub 이벤트 목록")
public class GitHubEventListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "GitHubEventVoList", physicalName = "GitHubEventVoList", type = "java.util.List", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private List<GitHubEventVo> GitHubEventVoList;

    public GitHubEventListVo() {}

    @ElVoField(physicalName = "GitHubEventVoList")
    public List<GitHubEventVo> getGitHubEventVoList() {
        return GitHubEventVoList;
    }

    public void setGitHubEventVoList(List<GitHubEventVo> GitHubEventVoList) {
        this.GitHubEventVoList = GitHubEventVoList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GitHubEventListVo [");
        if (GitHubEventVoList != null) {
            sb.append("GitHubEventVoList.size()=").append(GitHubEventVoList.size());
        } else {
            sb.append("GitHubEventVoList=null");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean isFixedLengthVo() {
        return false;
    }
}