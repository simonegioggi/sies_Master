package siap.bdmc.notifichesies.controller;

/**
* <p>Title: NotificheSiesController</p>
* <p>Description: Classe Controller per NotificheSies</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.bdmc.notifichesies.model.NotificheSiesModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface INotificheSies {

	public NotificheSiesModel ExInserisciNotificheSies(NotificheSiesModel aNotificheSies) throws F3BException;

	public Vector ExRicercaNotificheSies(NotificheSiesModel aNotificheSies) throws F3BException;

	public void ExModificaNotificheSies(NotificheSiesModel aNotificheSies) throws F3BException;

	public void ExModificaNotificheSiesNoCommit(Connection lConn, NotificheSiesModel aNotificheSies)
			throws F3BException;

	public void ExCancellaNotificheSies(NotificheSiesModel aNotificheSies) throws F3BException;

	public BigDecimal ExGetCountNotificheSies(NotificheSiesModel aNotificheSies) throws F3BException;

	public NotificheSiesModel ExRicercaNotificheSiesById(BigDecimal aIdNotificheSies) throws F3BException;

	public Vector ExRicercaNotificheSiesPaged(NotificheSiesModel aNotificheSies, int aPage)
			throws F3BException;

}