package com.demo.proworks.github.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;
import java.util.List;

/**
 * GitHub 브랜치 목록 VO
 */
@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "GitHub 브랜치 목록")
public class GitHubBranchListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "GitHubBranchVoList", physicalName = "GitHubBranchVoList", type = "java.util.List", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private List<GitHubBranchVo> GitHubBranchVoList;

    public GitHubBranchListVo() {}

    @ElVoField(physicalName = "GitHubBranchVoList")
    public List<GitHubBranchVo> getGitHubBranchVoList() {
        return GitHubBranchVoList;
    }

    public void setGitHubBranchVoList(List<GitHubBranchVo> GitHubBranchVoList) {
        this.GitHubBranchVoList = GitHubBranchVoList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GitHubBranchListVo [");
        if (GitHubBranchVoList != null) {
            sb.append("GitHubBranchVoList.size()=").append(GitHubBranchVoList.size());
        } else {
            sb.append("GitHubBranchVoList=null");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean isFixedLengthVo() {
        return false;
    }
}