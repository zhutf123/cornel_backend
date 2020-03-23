package com.demai.cornel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.ref.PhantomReference;
import java.util.List;

/**
 * @Author binz.zhang
 * @Date: 2020-03-23    17:06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewOptResp {
    private List<String> errTag;// 错误标签
    private String desc;// 错误详情描述
}
