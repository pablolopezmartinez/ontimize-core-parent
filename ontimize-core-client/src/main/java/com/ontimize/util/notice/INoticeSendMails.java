package com.ontimize.util.notice;

import com.ontimize.db.EntityResult;

public interface INoticeSendMails {

	/**
	 * This method allows to get all e-mail notices not yet sent
	 *
	 * @return
	 * @throws Exception
	 */
	public EntityResult getNewMailNotice() throws Exception;

	/**
	 * This method marks the specified notice as sent
	 *
	 * @param noticeKey
	 *            Notice key
	 * @throws Exception
	 */
	public void checkNoticeAsSend(Object noticeKey) throws Exception;

}
