package com.dianping.polestar.store.mysql.dao.impl;

import java.sql.Types;

import org.springframework.jdbc.core.JdbcTemplate;

import com.dianping.polestar.store.mysql.dao.QueryDAO;
import com.dianping.polestar.store.mysql.domain.QueryInfo;

public class QueryDAOImp implements QueryDAO {
	private JdbcTemplate jdbcTemplate;

	public QueryDAOImp() {
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void insert(QueryInfo queryInfo) {
		jdbcTemplate
				.update("insert into QueryInfo(username, sql, mode, addtime, exectime, path) values(?,?,?,?,?,?)",
						new Object[] {
								queryInfo.getUsername(),
								queryInfo.getSql(), 
								queryInfo.getMode(),
								queryInfo.getAddtime(),
								queryInfo.getExecTime(),
								queryInfo.getResultFilePath() }, 
						new int[] {
								Types.VARCHAR, 
								Types.VARCHAR, 
								Types.VARCHAR,
								Types.DATE, 
								Types.BIGINT, 
								Types.VARCHAR }
				);
	}

	@Override
	public QueryInfo findByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}
}
