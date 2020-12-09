package com.offcn.project.service.impl;

import com.offcn.project.mapper.*;
import com.offcn.project.pojo.*;
import com.offcn.project.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: lhq
 * @Date: 2020/12/3 09:48
 * @Description:
 */
@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {

    @Autowired
    private TReturnMapper returnMapper;

    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectImagesMapper projectImagesMapper;


    @Autowired
    private TTagMapper tTagMapper;

    @Autowired
    private TTypeMapper typeMapper;

    /**
     * 根据项目编号查询回报增量列表
     *
     * @param projectId
     * @return
     */
    @Override
    public List<TReturn> getReturnList(Integer projectId) {
        TReturnExample tReturnExample = new TReturnExample();
        TReturnExample.Criteria criteria = tReturnExample.createCriteria();
        criteria.andProjectidEqualTo(projectId);
        return returnMapper.selectByExample(tReturnExample);
    }

    /**
     * 查询所有项目列表
     *
     * @return
     */
    @Override
    public List<TProject> findProjectAll() {
        return projectMapper.selectByExample(null);
    }

    /**
     * 查询项目图片列表
     *
     * @param projectId
     * @return
     */
    @Override
    public List<TProjectImages> findImageList(Integer projectId) {
        TProjectImagesExample tProjectImagesExample = new TProjectImagesExample();
        TProjectImagesExample.Criteria criteria = tProjectImagesExample.createCriteria();
        criteria.andProjectidEqualTo(projectId);

        return projectImagesMapper.selectByExample(tProjectImagesExample);
    }

    /**
     * 根据项目编号查询项目详情
     *
     * @param projectId
     * @return
     */
    @Override
    public TProject findProjectInfo(Integer projectId) {
        return projectMapper.selectByPrimaryKey(projectId);
    }

    /**
     * 查询所有标签
     *
     * @return
     */
    @Override
    public List<TTag> findAllTag() {
        return tTagMapper.selectByExample(null);
    }

    /**
     * 查询所有分类
     *
     * @return
     */
    @Override
    public List<TType> findAllType() {
        return typeMapper.selectByExample(null);
    }

    /**
     * 根据回报增量编号查询详情
     *
     * @param returnId
     * @return
     */
    @Override
    public TReturn findReturnInfo(Integer returnId) {
        return returnMapper.selectByPrimaryKey(returnId);
    }


}
