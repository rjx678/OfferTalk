package com.offertalk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.offertalk.entity.Company;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CompanyMapper extends BaseMapper<Company> {
}
