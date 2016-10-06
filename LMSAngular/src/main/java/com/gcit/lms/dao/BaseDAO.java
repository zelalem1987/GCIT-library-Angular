package com.gcit.lms.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class BaseDAO {
	
	@Autowired
	public JdbcTemplate template;

	private Integer pageNo;

	private Integer pageSize = 10;
	
	public String addLimitToQuery(Integer pageNum, Integer pageSizes, String sql){
		if(pageSizes != null){
			if(pageNum != null){
				int index = (pageNum - 1) * pageSizes;
				sql += " LIMIT " + index + ", " + pageSizes;
			}
		}
		return sql;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	
}
