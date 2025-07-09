package com.demo.proworks.github.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;
import java.util.List;

/**
 * GitHub 레포지토리 목록 VO
 */
@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "GitHub 레포지토리 목록")
public class GitHubRepositoryListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "GitHubRepositoryVoList", physicalName = "GitHubRepositoryVoList", type = "java.util.List", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private List<GitHubRepositoryVo> GitHubRepositoryVoList;

    public GitHubRepositoryListVo() {}

    @ElVoField(physicalName = "GitHubRepositoryVoList")
    public List<GitHubRepositoryVo> getGitHubRepositoryVoList() {
        return GitHubRepositoryVoList;
    }

    public void setGitHubRepositoryVoList(List<GitHubRepositoryVo> GitHubRepositoryVoList) {
        this.GitHubRepositoryVoList = GitHubRepositoryVoList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GitHubRepositoryListVo [");
        if (GitHubRepositoryVoList != null) {
            sb.append("GitHubRepositoryVoList.size()=").append(GitHubRepositoryVoList.size());
        } else {
            sb.append("GitHubRepositoryVoList=null");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean isFixedLengthVo() {
        return false;
    }
}