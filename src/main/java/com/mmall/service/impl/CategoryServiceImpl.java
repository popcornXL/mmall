package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("iCategoryService")

public class CategoryServiceImpl implements ICategoryService {

            private Logger logger = LoggerFactory.getLogger(Category.class);

            @Autowired
            private CategoryMapper categoryMapper;

            public ServerResponse addCategory(String categoryName,Integer parentId){
                if(parentId == null || StringUtils.isBlank(categoryName)){
                    return  ServerResponse.createByErrorMessage("添加種類參數錯誤");
                }
                Category category = new Category();
                category.setName(categoryName);
                category.setParentId(parentId);
                category.setStatus(true);//這個分類是可用的

                int rowCount = categoryMapper.insert(category);
                if(rowCount > 0){
                    return  ServerResponse.createBySuccess("添加種類成功");

                }
                return  ServerResponse.createByErrorMessage("添加種類失敗");
            }

            public  ServerResponse updateCategoryName(Integer categoryId,String categoryName){
                if(categoryId == null || StringUtils.isBlank(categoryName)){
                    return  ServerResponse.createByErrorMessage("添加種類參數錯誤");
                }
                Category category = new Category();
                category.setId(categoryId);
                category.setName(categoryName);
                int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
                if(rowCount > 0){
                    return  ServerResponse.createBySuccess("更新種類名稱成功");
                }
                return  ServerResponse.createByErrorMessage("更新種類名稱失敗");
            }

            public  ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
                List<Category> categoryList = categoryMapper.selectChildrenParallelCategory(categoryId);
                if(CollectionUtils.isEmpty(categoryList)){
                    logger.info("未找到子分類");
                }
                return  ServerResponse.createBySuccess(categoryList);
            }
            // 遞迴查詢本節點與子節點id
            public  ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
                Set<Category> categorySet = Sets.newHashSet(); //Guavar提供的方法
                findChildCategory(categorySet,categoryId);

                List<Integer> categoryList = Lists.newArrayList();
                if(categoryId != null){
                    for(Category categoryItem : categorySet){
                        categoryList.add(categoryItem.getId());
                    }
                }
                return ServerResponse.createBySuccess(categoryList);
            }
            //遞迴 算出子節點
            private Set<Category> findChildCategory(Set<Category> categorySet ,Integer categoryId){
                Category category = categoryMapper.selectByPrimaryKey(categoryId);
                if(category != null){
                    categorySet.add(category);
                }
                //找子節點，遞迴一定要有一個退出的條件
                List<Category> categoryList = categoryMapper.selectChildrenParallelCategory(categoryId);
                //mybatis 沒查到時 不會返回空集合 so 不用判斷
                for(Category categoryItem : categoryList){
                    findChildCategory(categorySet,categoryItem.getId());
                }
                return  categorySet;
            }

}
