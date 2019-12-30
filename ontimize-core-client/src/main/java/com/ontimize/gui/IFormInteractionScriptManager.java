package com.ontimize.gui;

import java.util.List;

public interface IFormInteractionScriptManager {

	public Boolean checkQuery();

	public boolean isCheckQueryScript();

	public Boolean checkDelete();

	public boolean isCheckDeleteScript();

	public Boolean checkInsert();

	public boolean isCheckInsertScript();

	public Boolean checkUpdate();

	public boolean isCheckUpdateScript();

	public boolean setQueryMode();

	public boolean isQueryModeScript();

	public boolean setInsertMode();

	public boolean isInsertModeScript();

	public boolean setUpdateMode();

	public boolean isUpdateModeScript();

	public boolean setQueryInsertMode();

	public boolean isQueryInsertModeScript();

	public void registerListeners(Form f);

	public List getScripts(String action, String componentKey);

	public boolean checkActionScript(List scripts);

	public void executeScripts(List scripts) throws Exception;
}
