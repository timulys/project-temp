package com.kep.legacy.repository;

import com.kep.legacy.config.datasource.ConnectBranchMapper;
import com.kep.legacy.model.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Mapper
@ConnectBranchMapper
public interface CustomerRepository {

	Customer findById(@NotNull Long id);

	Customer findOne(@NotNull Map<String, Object> sqlParams);

	List<Customer> findAll(@NotNull Map<String, Object>sqlParams);
}
