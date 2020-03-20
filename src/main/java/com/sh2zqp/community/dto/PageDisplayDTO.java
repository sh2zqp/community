package com.sh2zqp.community.dto;

import lombok.Data;

import java.util.*;

@Data
public class PageDisplayDTO {
    private List<QuestionDTO> questionDTOS;
    private boolean showPrevPage;
    private boolean showNextPage;
    private boolean showFirstPage;
    private boolean showLastPage;
    private Integer currentPage;
    private Integer totalPage;
    private List<Integer> pages = new ArrayList<>();

    /**
     *
     * @param totalCount 总的问题个数
     * @param totalPage 总的页面数
     * @param page 查询的当前页码
     */
    public void sePageDisplayDTO(Integer totalCount, Integer totalPage, Integer page) {
        this.currentPage = page; // 设置当前页
        this.totalPage = totalPage; // 设置总页数

        int currentPageLRCountMax = 3; // 当前页面的左右最多只有3个元素
        pages.add(currentPage); // 先把当前页面加入到页面集合中
        for (int i = 1; i <= currentPageLRCountMax; i++) {
            // 当前页左边的页面添加，最多currentPageLRCountMax个
            if (currentPage - i > 0) {
                pages.add(currentPage - i);
            }
            // 当前页右边的页面添加，最多currentPageLRCountMax个
            if (currentPage + i <= totalPage) {
                pages.add(currentPage + i);
            }
        }
        pages.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        }); // 升序排序

        showPrevPage = currentPage != 1;  // 是否展示上一页
        showNextPage = !currentPage.equals(totalPage);  // 是否展示下一页
        showFirstPage = !pages.contains(1); // 是否展示第一页
        showLastPage = !pages.contains(totalPage); // 是否展示最后一页
    }
}
