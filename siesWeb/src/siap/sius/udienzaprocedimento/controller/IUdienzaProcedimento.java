package siap.sius.udienzaprocedimento.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import siap.sico.evento.model.XModel;
import siap.sico.utente.model.UtenteModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: UdienzaProcedimentoController
 * </p>
 * <p>
 * Description: Classe Controller per UdienzaProcedimento
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
public interface IUdienzaProcedimento {

	public UdienzaProcedimentoModel ExInserisciUdienzaProcedimento(
			UdienzaProcedimentoModel aUdienzaProcedimento) throws F3BException;

	public Vector ExRicercaUdienzaProcedimento(UdienzaProcedimentoModel aUdienzaProcedimento)
			throws F3BException;

	public UdienzaProcedimentoModel ExRicercaUdienzaProcedimentoByKey(BigDecimal aKey) throws F3BException;

	public UdienzaProcedimentoModel ExModificaUdienzaProcedimento(
			UdienzaProcedimentoModel aUdienzaProcedimento) throws F3BException;

	public void ExCancellaUdienzaProcedimento(UdienzaProcedimentoModel aUdienzaProcedimento)
			throws F3BException;

	public Vector ExRicercaProcedimentixUdienza(BigDecimal aIdUdienza, String aOrderBy) throws F3BException;

	public Vector ExRicercaProcedimentixUdienza(BigDecimal aIdUdienza, String aOrderBy,
			String aStatoProcedimento, String aTipoProc) throws F3BException;

	public Vector ExRicercaProcedimentixDataUdienza(Date aDataUdienza, String aOrderBy,
			String aStatoProcedimento, String aTipoProc, String aCodUfficioConnesso) throws F3BException;

	public ByteArrayOutputStream ExStampaProcedimentixUdienza(BigDecimal aIdUdienza, String aCodMagistrato,
			BigDecimal aIdEsperto, XModel aStampa, String aIdDocumento, String lOrderBy,
			UtenteModel aUtenteModel, String aStatoProcedimento, String aTipoProc, String aCodUfficioConnesso)
			throws F3BException;

	public ByteArrayOutputStream ExStampaProcedimentixDataUdienza(Date aDataUdienza, String aCodMagistrato,
			BigDecimal aIdEsperto, XModel aStampa, String aIdDocumento, String lOrderBy,
			UtenteModel aUtenteModel, String aStatoProcedimento, String aTipoProc,
			String aUfficioUtenteConnesso) throws F3BException;

	public Vector ExRicercaUdienzaProcedimentoByGeneraleProcedimento(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaUdienzaProcedimentoUdiByGeneraleProcedimento(BigDecimal aKey) throws F3BException;

	public UdienzaProcedimentoModel ExRicercaUdienzaProcedimentoByEve(BigDecimal aKey) throws F3BException;

	public Collection ExRicercaUdienzeMagistratiProcedimentiByDate(UdienzaModel aUdienza) throws F3BException;

	public void ExAggiornaFissazioneUdienza(UdienzaProcedimentoModel aUdienzaProcedimento)
			throws F3BException;

	public void ExCancellaFissazioneUdienza(UdienzaProcedimentoModel aUdienzaProcedimento)
			throws F3BException;

	public void ExCancellaRinvioUdienza(UdienzaProcedimentoModel aUdienzaProcedimento, BigDecimal aIdFasSius)
			throws F3BException;

	public UdienzaProcedimentoModel ExRicercaUdienzaProcedimentoByGenProFlagRinviata(BigDecimal aKey,
			String aFilter) throws F3BException;

	public UdienzaProcedimentoModel ExInserisciPreFissazioneUdienza(
			UdienzaProcedimentoModel aNuovaUdienzaProc, GeneraleProcedimentoModel aGenProc,
			UdienzaProcedimentoModel aVecchiaUdienzaProc) throws F3BException;

}