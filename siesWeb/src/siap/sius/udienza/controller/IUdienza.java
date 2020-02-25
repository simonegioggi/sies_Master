package siap.sius.udienza.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sius.avvocatura.model.AvvisiAvvocatoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: UdienzaController
 * </p>
 * <p>
 * Description: Classe Controller per Udienza
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
public interface IUdienza {

	public UdienzaModel ExInserisciUdienza(UdienzaModel aUdienza) throws F3BException;

	public Vector ExRicercaUdienza(UdienzaModel aUdienza) throws F3BException;

	public Vector ExRicercaUdienzaNumProc(UdienzaModel aUdienza) throws F3BException;

	public Vector ExRicercaUdienzaUDS(UdienzaModel aUdienza) throws F3BException;

	public String ExRicercaUdienzePrecedenti(UdienzaModel aUdienza,
			GeneraleProcedimentoModel aGeneraleProcedimento) throws F3BException;

	public Vector ExRicercaUdienza(UdienzaModel aUdienza, int aNumOccorrenze) throws F3BException;

	public Vector ExRicercaUdienzaGenerale(UdienzaModel aUdienza, int aNumOccorrenze) throws F3BException;

	public Vector ExRicercaNuoveUdienze(UdienzaModel lUdienzaMod,
			GeneraleProcedimentoModel aGeneraleProcedimento) throws F3BException;

	public UdienzaModel ExRicercaUdienzaByKey(BigDecimal aKey) throws F3BException;

	public UdienzaModel ExRicercaUdienzaByKeyUDS(BigDecimal aKey) throws F3BException;

	public UdienzaModel ExRicercaUdienzaByDate(Date aDate, String aCodUfficio) throws F3BException;

	public UdienzaModel ExModificaUdienza(UdienzaModel aUdienza) throws F3BException;

	public UdienzaModel ExModificaUdienzaUDS(UdienzaModel aUdienza) throws F3BException;

	public void ExCancellaUdienza(UdienzaModel aUdienza) throws F3BException;

	public UdienzaModel ExRinvioUdienzaVerbale(UdienzaModel aUdienza,
			GeneraleProcedimentoModel aGeneraleProcedimento, EventoModel aEvento, TenoreModel[] aTenori,
			Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato) throws F3BException;

	public UdienzaProcedimentoModel ExInserisciFissazioneUdienza(UdienzaProcedimentoModel aNuovaUdienzaProc,
			FascicoloGPModel aFasc, EventoNotificaModel aEve, TenoreModel[] aTenori,
			UdienzaProcedimentoModel aVecchiaUdienzaProc) throws F3BException;

	// genny 27/02/2004
	public EventoModel ExInserisciOrdinanzaRinvioUdienza(UdienzaModel aUdienza, FascicoloGPModel aFasc,
			EventoModel aEve, TenoreModel[] aTenori, DepositoOrdinanzaPcModel lDepositoOrdinanzaPc,
			GeneraleProcedimentoModel aGeneraleProcedimentoold) throws F3BException;

	public ByteArrayOutputStream ExStampaVerbaleUdienza(EventoModel lEvento, UfficioModel lUfficio,
			UtenteModel aUtenteModel) throws F3BException;

	public ByteArrayOutputStream ExStampaFissazioneUdienza(EventoModel lEvento, UfficioModel lUfficio,
			UtenteModel aUtenteModel) throws F3BException;

	public EventoModel ExInserisciSentenzaRinvioUdienza(UdienzaModel aUdienza, FascicoloGPModel aFasc,
			EventoModel aEve, TenoreModel[] aTenori, DepositoSentenzaModel lDepositoSentenza,
			GeneraleProcedimentoModel aGeneraleProcedimentoold) throws F3BException;

}