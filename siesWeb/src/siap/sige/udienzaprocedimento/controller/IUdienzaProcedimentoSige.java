package siap.sige.udienzaprocedimento.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.XModel;
import siap.sico.utente.model.UtenteModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.model.ProcedimentixUdienzaModel;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: UdienzaProcedimentoSigeController
 * </p>
 * <p>
 * Description: Classe Controller per UdienzaProcedimentoSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IUdienzaProcedimentoSige {

	public UdienzaProcedimentoSigeModel ExInserisciUdienzaProcedimentoSige(
			UdienzaProcedimentoSigeModel aUdienzaProcedimentoSige) throws F3BException;

	public Vector ExRicercaUdienzaProcedimentoByIdFascicoloSige(BigDecimal aKey) throws F3BException;

	public UdienzaProcedimentoSigeModel ExRicercaUdienzaProcedimentoByEve(BigDecimal aKey)
			throws F3BException;

	public UdienzaProcedimentoSigeModel ExRicercaUdienzaProcedimentoSigeByKey(BigDecimal aKey)
			throws F3BException;

	public void ExCancellaFissazioneUdienza(UdienzaProcedimentoSigeModel aUdienzaProcedimento)
			throws F3BException;

	public UdienzaProcedimentoSigeModel ExRicercaUltimaUdienzaProcedimentoSigeByFascicoloByFlagRinviata(
			BigDecimal aKey, String aFilter) throws F3BException;

	public UdienzaProcedimentoSigeModel ExRicercaUltimaUdienzaProcedimentoSigeByFascicoloByFlagRinviata(
			BigDecimal aKey, String aFilter, boolean complete) throws F3BException;

	public EventoModel ExInserisciRinvioUdienza(UdienzaSigeModel aUdienza, FascicoloSigeModel aFascSige,
			ProvvedimentoSigeEventoModel aProvvEvento) throws F3BException;

	public void ExCancellaRinvioUdienza(UdienzaProcedimentoSigeModel aUdienzaProcedimento,
			BigDecimal aIdFasSige) throws F3BException;

	public ByteArrayOutputStream ExStampaOrdinanzaRinvioUdienza(BigDecimal aIdFascicolo, EventoModel lEvento,
			String aCodUff, UtenteModel aUtenteModel) throws F3BException;

	public Collection<Object> ExRicercaUdienzeMagistratiProcedimentiByDate(UdienzaSigeModel aUdienza)
			throws F3BException;

	public Collection<ProcedimentixUdienzaModel> ExRicercaProcedimentixDataUdienza(Date aDataUdienza,
			String aOrderBy, String aStatoProcedimento, String aTipoProc, String aCodUfficioConnesso)
			throws F3BException;

	public Collection<ProcedimentixUdienzaModel> ExRicercaProcedimentixUdienza(BigDecimal aIdUdienza,
			String aOrderBy) throws F3BException;

	public Collection<ProcedimentixUdienzaModel> ExRicercaProcedimentixUdienza(BigDecimal aIdUdienza,
			String aOrderBy, String aStatoProcedimento, String aTipoProc, String flagModifBlocco, String codMagistrato) throws F3BException;

	public ByteArrayOutputStream ExStampaProcedimentixDataUdienza(Date aDataUdienza, BigDecimal aIdFascicolo,
			String aCodMagistrato, BigDecimal aIdEsperto, XModel aStampa, String aIdDocumento,
			String lOrderBy, UtenteModel aUtenteModel, String aStatoProcedimento, String aTipoProc,
			String aCodUfficioConnesso) throws F3BException;

	public ByteArrayOutputStream ExStampaProcedimentixUdienza(BigDecimal aIdUdienza, BigDecimal aIdFascicolo,
			String aCodMagistrato, BigDecimal aIdEsperto, XModel aStampa, String aIdDocumento,
			String lOrderBy, UtenteModel aUtenteModel, String aStatoProcedimento, String aTipoProc,
			String aCodUfficioConnesso) throws F3BException;

	public UdienzaProcedimentoSigeModel ExRicercaUdienzaProcedimentoByIdUdienza(BigDecimal idUdienza)
			throws F3BException;

	public BigDecimal countUdienzeByIdFascicolo(BigDecimal idFascicolo) throws F3BException;

	public void ExModificaUdienzaProcedimento(UdienzaProcedimentoSigeModel model) throws F3BException;

	// MERGE v10: aggiunto metodo di controllo
	public BigDecimal contaUdienzeProcedimentoSigeByIdUdienza(BigDecimal idUdienza) throws F3BException;
	
	// intervento per versione 11.2.1
	public Collection<ProcedimentixUdienzaModel> ExRicercaProcedimentiPerUdienza(BigDecimal aIdUdienza, String statoFascicolo, String flagModifBlocco) throws F3BException;
	
	// intervento per versione 11.2.1
	public Collection<ProcedimentixUdienzaModel> ExRicercaProcedimentixUdienzaOrOrdinanza(BigDecimal aIdUdienza,
			String aOrderBy, String aStatoProcedimento, String aTipoProc, String flagModifBlocco, String codMagistrato) throws F3BException;
		

}