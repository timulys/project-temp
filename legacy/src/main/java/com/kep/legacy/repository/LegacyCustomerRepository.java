package com.kep.legacy.repository;

import com.kep.legacy.config.datasource.LegacyMapper;
import com.kep.legacy.model.entity.LegacyCustomer;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Mapper
@LegacyMapper
public interface LegacyCustomerRepository {

	LegacyCustomer findById(@NotNull Long id);

	LegacyCustomer findOne(@NotNull Map<String, Object>sqlParams);

	List<LegacyCustomer> findAll(@NotNull Map<String, Object>sqlParams);
}
