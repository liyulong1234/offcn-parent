package com.offcn.project.service;

import com.offcn.project.pojo.*;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/12/3 09:46
 * @Description: 项目信息接口
 */
public interface ProjectInfoService {


    /**
     * 根据项目编号查询回报增量列
     *
     * @param projectId
     * @return
     */
    public List<TReturn> getReturnList(Integer projectId);

    /**
     * 查询所有项目列表
     *
     * @return
     */
    public List<TProject> findProjectAll();

    /**
     * 查询项目图片列表
     *
     * @param projectId
     * @return
     */
    public List<TProjectImages> findImageList(Integer projectId);

    /**
     * 根据项目编号查询项目详情
     *
     * @param projectId
     * @return
     */
    public TProject findProjectInfo(Integer projectId);

    /**
     * 查询所有标签
     *
     * @return
     */
    public List<TTag> findAllTag();

    /**
     * 查询所有分类
     *
     * @return
     */
    public List<TType> findAllType();

    /**
     * 根据回报增量编号查询详情
     * @param returnId
     * @return
     */
    public TReturn findReturnInfo(Integer returnId);
}
