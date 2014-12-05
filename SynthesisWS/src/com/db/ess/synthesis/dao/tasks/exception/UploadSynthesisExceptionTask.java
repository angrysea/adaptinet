package com.db.ess.synthesis.dao.tasks.exception;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.db.ess.synthesis.dao.tasks.BaseExecuteDaoTask;
import com.db.ess.synthesis.dvo.SynthesisException;
import com.db.ess.synthesis.dvo.UploadExceptionRequest;
import com.db.ess.synthesis.dvo.UploadExceptionResponse;
import com.db.ess.synthesis.statement.NewPendingCreateStatement;
import com.db.ess.synthesis.statement.UpdatePendingCreateStatement;
import com.db.ess.synthesis.util.SQLUtils;

import java.util.concurrent.PriorityBlockingQueue;

public class UploadSynthesisExceptionTask extends
		BaseExecuteDaoTask<UploadExceptionResponse> {
	private static final Logger logger = Logger
			.getLogger(UploadSynthesisExceptionTask.class.getName());
	List<SynthesisException> reqList = null;
	boolean requestHasError;

	public UploadSynthesisExceptionTask(UploadExceptionRequest req, int location) {
		super(location);
		requestHasError = false;
		reqList = new ArrayList<SynthesisException>();

		if (req.getexceptionsIterator() != null) {
			ExceptionsValidator excepValid = null;
			Iterator itr = req.getexceptionsIterator();
			while (itr.hasNext()) {
				excepValid = new ExceptionsValidator(
						(SynthesisException) itr.next(), location,
						req.getisTrader());
				excepValid.performValidation();
				if (!requestHasError)
					requestHasError = excepValid.exceptionHasError();

				reqList.add(excepValid.getExceptionObject());
			}

		}

	}

	@Override
	protected PreparedStatement[] createStatement(Connection c)
			throws SQLException {
		if (requestHasError)
			return null;
		List<PreparedStatement> psList = new ArrayList<PreparedStatement>();
		List<SynthesisException> updateList = new ArrayList<SynthesisException>();
		List<SynthesisException> createList = new ArrayList<SynthesisException>();
		for (SynthesisException se : reqList) {
			// Add the pend clause when the reqs ask for creating pending with
			// different ExceptIds
			if (se.getexceptUpdateTblName() != null)// &&
													// se.getexceptUpdateTblName().contains("Pend")
				updateList.add(se);
			else {

				createList.add(se);
			}

		}
		List<Integer> exceptIdList = new IdGeneratorTask(location, 1, 600,
				createList.size()).run();
		PriorityBlockingQueue idQ = new PriorityBlockingQueue(exceptIdList);
		setUpExceptIds(createList, idQ);

		psList.addAll(new NewPendingCreateStatement(c, createList)
				.createStatement());
		psList.addAll(new UpdatePendingCreateStatement(c, updateList)
				.createStatement());

		return (PreparedStatement[]) psList
				.toArray(new PreparedStatement[psList.size()]);
	}

	public void run(UploadExceptionResponse res) throws Exception {
		if (requestHasError) {
			for (SynthesisException se : reqList) {
				res.setexceptions(se);
			}
			res.setisUploadSuccess(false);
			return;
		}

		Connection conn = null;
		PreparedStatement[] stmts = null;
		long time = System.currentTimeMillis();
		try {
			conn = SQLUtils.getConnection(location);
			// Setting it to false to maintain atomicity of the transaction
			conn.setAutoCommit(false);
			stmts = createStatement(conn);
			for (PreparedStatement s : stmts) {
				s.executeUpdate();
			}
			conn.commit();
			res.setisUploadSuccess(true);
			logger.info("Ellapsed time: " + (System.currentTimeMillis() - time));
			logger.info(">>> Successfully INSERT/UPDATE/DELETE in " + location);
		} catch (Exception ex) {
			logger.error("Failed to execute query: ", ex);
			conn.rollback();
			res.setisUploadSuccess(false);
			throw ex;
		} finally {
			SQLUtils.closeResources(conn, stmts, null);
		}
		// Handle cases where database might throw errors
		processResult(res);
	}

	public void processResult(UploadExceptionResponse res) {
		// res.setisUploadSuccess(!requestHasError);
		if (!res.getisUploadSuccess() || requestHasError) {
			for (SynthesisException se : reqList) {
				res.setexceptions(se);
			}
		}
	}

	public void setUpExceptIds(List<SynthesisException> listE,
			PriorityBlockingQueue idQ) {
		if (listE != null && !listE.isEmpty() && idQ != null && !idQ.isEmpty()) {
			for (SynthesisException se : listE) {
				// if(!(se.getexceptId()>0))
				se.setexceptId((Integer) idQ.poll());
			}
			logger.info("Successfully updated exceptionIds: ");
		}
	}

}
