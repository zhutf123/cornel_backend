package com.demai.cornel.dao;

import com.demai.cornel.model.LoanInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
* @Author binz.zhang
* @Date: 2020-03-17    09:49
*/
public interface LoanInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(String loanId);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);

    int updatePrice(@Param("price") BigDecimal price,@Param("loanId") String loanId);

    List<LoanInfo> getLoanByLoanIds(@Param("loanIds") Set<String> loanId);
}