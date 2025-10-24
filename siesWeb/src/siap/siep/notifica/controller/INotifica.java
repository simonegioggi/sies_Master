package siap.siep.notifica.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import siap.siep.notifica.model.NotificaModel;
import siap.siep.notifica.model.RicercaNotificheSiusModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: NotificaController
 * </p>
 * <p>
 * Description: Classe Controller per Notifica
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface INotifica {

	public NotificaModel ExInserisciNotifica(NotificaModel aNotifica) throws F3BException;

	public Vector ExRicercaNotifica(NotificaModel aNotifica) throws F3BException;

	public Vector ExRicercaNotificaAvvocatoNonAvvenuta(NotificaModel aNotifica) throws F3BException;

	public NotificaModel ExRicercaNotificaByKey(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaNotificaByKeyEvento(BigDecimal aKey) throws F3BException;

	public Vector<NotificaModel> ExRicercaEstesaNotificaByKeyEvento(BigDecimal aKey) throws F3BException;

	public NotificaModel ExRicercaNotificaCompByFascicolo(BigDecimal aFascicolo) throws F3BException;

	public NotificaModel ExRicercaNotificaPolByFascicolo(BigDecimal aFascicolo) throws F3BException;

	public NotificaModel ExRicercaNotificaUffByFascicolo(BigDecimal aFascicolo) throws F3BException;

	public NotificaModel ExModificaNotifica(NotificaModel aNotifica) throws F3BException;

	public void ExCancellaNotifica(NotificaModel aNotifica) throws F3BException;

	public void ExAggiornaDateNotifica(NotificaModel[] aIdNotifiche) throws F3BException;

	public boolean ExSonoNotificate(BigDecimal aKey) throws F3BException;

	public boolean ExSonoNotificateSige(BigDecimal aKey) throws F3BException;

	public Date ExRicercaDataNotifica(BigDecimal aIdEve) throws F3BException;

	public Date ExRicercaDataNotificaSige(BigDecimal aIdEve) throws F3BException;

	public Vector ExRicercaNotificheByFascicoloSius(BigDecimal aFascKey, String aTipoEvento)
			throws F3BException;

	public NotificaModel ExRicercaNotificaTipoNotCByFascicolo(BigDecimal aFascicolo) throws F3BException;

	public NotificaModel ExRicercaNotificaTipoNotNByFascicolo(BigDecimal aFascicolo) throws F3BException;

	public Vector ExAggiornaRegistrazioneNotificaDecretoIrreeribilita(NotificaModel[] IdNotifiche,
			BigDecimal aFasc) throws F3BException;

	public ArrayList ExInserisciNotifiche(ArrayList aNotifica) throws F3BException;

	public Vector ExRicercaEstesaNotificaDataAvvNotificaNullByKeyEvento(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaPaginataNotificheXFasSius(RicercaNotificheSiusModel aModel, int aPageNum)
			throws F3BException;

	public BigDecimal ExGetNumRicercaNotificheXFasSius(RicercaNotificheSiusModel aModel) throws F3BException;

	public Date ExRicercaDataInvioCertCasellario(BigDecimal aFascKey, String aTipoEvento, String aCodMotivo)
			throws F3BException;

	public Vector ExRicercaEstesaNotificaByIdParteUdienza(BigDecimal aIdParteUdienza) throws F3BException;

	public ArrayList ExInserisciNotifiche(ArrayList aNotifica, Connection aConn) throws F3BException;

	public Vector ExRicercaNotificaByIdParteUdienza(BigDecimal aIdParteUdienza) throws F3BException;

}