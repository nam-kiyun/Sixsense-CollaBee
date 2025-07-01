package com.demo.proworks.collabee.vo.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "깃허브앱토큰")
public class GithubAppTokenListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "깃허브앱토큰List", physicalName = "githubAppTokenVoList", type = "com.demo.proworks.collabee.vo.GithubAppTokenVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.collabee.vo.vo.GithubAppTokenVo> githubAppTokenVoList;

    public java.util.List<com.demo.proworks.collabee.vo.vo.GithubAppTokenVo> getGithubAppTokenVoList(){
        return githubAppTokenVoList;
    }

    public void setGithubAppTokenVoList(java.util.List<com.demo.proworks.collabee.vo.vo.GithubAppTokenVo> githubAppTokenVoList){
        this.githubAppTokenVoList = githubAppTokenVoList;
    }

    @Override
    public String toString() {
        return "GithubAppTokenListVo [githubAppTokenVoList=" + githubAppTokenVoList+ "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
