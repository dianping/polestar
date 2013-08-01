package com.dianping.polestar.store.mysql.dao.impl;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.dianping.polestar.EnvironmentConstants;
import com.dianping.polestar.store.mysql.dao.QueryDAO;
import com.dianping.polestar.store.mysql.domain.QueryInfo;

public class QueryDAOImp implements QueryDAO {
	public final static Log LOG = LogFactory.getLog(QueryDAOImp.class);

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
				.update("insert into QueryInfo(`username`, `sql`, `mode`, `addtime`, `exectime`, `path`) values(?,?,?,?,?,?)",
						new Object[] { queryInfo.getUsername(),
								queryInfo.getSql(), queryInfo.getMode(),
								queryInfo.getAddtime(),
								queryInfo.getExecTime(),
								queryInfo.getResultFilePath() }, new int[] {
								Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
								Types.VARCHAR, Types.BIGINT, Types.VARCHAR });
	}

	@Override
	public List<QueryInfo> findByUsername(String username) {
		List<QueryInfo> querys = new ArrayList<QueryInfo>();
		if (!StringUtils.isBlank(username)) {
			List<Map<String, Object>> rows = jdbcTemplate
					.queryForList(
							"SELECT `username`, `sql`, `mode`, `addtime`, `exectime`, `path` FROM QueryInfo WHERE username = ? ORDER BY id DESC",
							new Object[] { username });
			if (rows != null && rows.size() > 0) {
				for (Map<String, Object> row : rows) {
					QueryInfo q = new QueryInfo();
					q.setUsername((String) row.get("username"));
					q.setSql((String) row.get("sql"));
					q.setMode((String) row.get("mode"));
					q.setAddtime(EnvironmentConstants.DATE_FORMAT.format(row
							.get("addtime")));
					q.setExecTime(((Long) row.get("exectime")).longValue());
					q.setResultFilePath((String) row.get("path"));
					querys.add(q);
				}
			}
		}
		LOG.info("find " + querys.size() + " query records by user:" + username);
		return querys;
	}
}
