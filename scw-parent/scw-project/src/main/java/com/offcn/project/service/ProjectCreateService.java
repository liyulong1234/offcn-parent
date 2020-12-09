package com.offcn.project.service;

import com.offcn.dycommon.ProjectStatusEnume;
import com.offcn.project.vo.req.ProjectRedisStorageVo;

/**
 * @Auther: lhq
 * @Date: 2020/12/2 09:40
 * @Description:   发布、创建项目接口
 */
public interface ProjectCreateService {


    /**
     * 初始化创建项目
     * @param memberId    用户ID
     * @return
     */
    public String initCreateProject(Integer memberId);

    /**
     * 保存项目信息
     * @param auth
     * @param projectRedisStorageVo
     */
    public void saveProjectInfo(ProjectStatusEnume auth, ProjectRedisStorageVo projectRedisStorageVo);
}
