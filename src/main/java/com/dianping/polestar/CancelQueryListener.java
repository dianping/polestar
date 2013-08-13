package com.dianping.polestar;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dianping.polestar.jobs.Job;
import com.dianping.polestar.jobs.JobManager;
import com.dianping.polestar.store.mysql.dao.QueryDAO;
import com.dianping.polestar.store.mysql.dao.impl.QueryDAOFactory;
import com.dianping.polestar.store.mysql.domain.QueryCancel;

public class CancelQueryListener implements ServletContextListener {
	private final static Log LOG = LogFactory.getLog(CancelQueryListener.class);

	private final static long CHECK_DB_INTERVAL_IN_MILLISECONDS = 3000L;
	private final QueryDAO queryDao = QueryDAOFactory.getInstance();

	private CancelQueryThread cancelQueryThread;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		cancelQueryThread = new CancelQueryThread();
		cancelQueryThread.setDaemon(true);
		cancelQueryThread.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (cancelQueryThread != null) {
			cancelQueryThread.interrupt();
		}
	}

	class CancelQueryThread extends Thread {

		@Override
		public void run() {
			LOG.info("start to launch CancelQuery Thread");
			List<QueryCancel> queryCancels = null;
			while (true) {
				try {
					queryCancels = queryDao
							.findQueryCancelWithouHost(EnvironmentConstants.LOCAL_HOST_ADDRESS);
					if (queryCancels != null && queryCancels.size() > 0) {
						for (QueryCancel qc : queryCancels) {
							Job job = JobManager.getJobById(qc.getId());
							if (job != null) {
								LOG.info("start to cancel job from "
										+ qc.getHost());
								job.cancel();
								if (job.isCanceled()) {
									JobManager.removeJob(qc.getId());
									queryDao.deleteQueryCancelById(qc.getId());
								}
							}
						}
					}
					Thread.sleep(CHECK_DB_INTERVAL_IN_MILLISECONDS);
				} catch (InterruptedException e) {
					LOG.error("cancel query thread interrupted, " + e);
				}
			}
		}
	}
}
