package siap.bdmc.sbviewnotifiche.controller;

/**
* <p>Title: SbViewNotificheController</p>
* <p>Description: Classe Controller per SbViewNotifiche</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.bdmc.sbviewnotifiche.model.SbViewNotificheModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface ISbViewNotifiche {

	public SbViewNotificheModel ExInserisciSbViewNotifiche(SbViewNotificheModel aSbViewNotifiche)
			throws F3BException;

	public Vector ExRicercaSbViewNotifiche(SbViewNotificheModel aSbViewNotifiche) throws F3BException;

	public void ExModificaSbViewNotifiche(SbViewNotificheModel aSbViewNotifiche) throws F3BException;

	public void ExCancellaSbViewNotifiche(SbViewNotificheModel aSbViewNotifiche) throws F3BException;

	public void ExChiudiSbViewNotifiche(SbViewNotificheModel aSbViewNotifiche) throws F3BException;

	public BigDecimal ExGetCountSbViewNotifiche(SbViewNotificheModel aSbViewNotifiche) throws F3BException;

	public SbViewNotificheModel ExRicercaSbViewNotificheById(BigDecimal aProgNoti) throws F3BException;

	public Vector ExRicercaSbViewNotifichePaged(SbViewNotificheModel aSbViewNotifiche, int aPage)
			throws F3BException;

}