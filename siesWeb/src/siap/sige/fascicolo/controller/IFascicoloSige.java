package siap.sige.fascicolo.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.sige.detenzione.model.FasSigeDetenzioneModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.fascicolo.model.RicercaFascicoloSigeModel;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.richiesta.model.RichiestaSigeModel;
//import siap.sius.produzioneatti.model.ParereModel;
import siap.sige.richiestaatti.model.ParereModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: FascicoloSigeController
 * </p>
 * <p>
 * Description: Classe Controller per FascicoloSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 5.0
 */
@SuppressWarnings("rawtypes")
public interface IFascicoloSige {

	public FascicoloSigeModel ExInserisciFascicoloSige(FascicoloSigeModel aFascicoloSige,
			RichiestaSigeModel aRichiestaSige, MagistratoAssegnatarioModel aMagistrato,
			FasSigeDetenzioneModel lDetenzione) throws F3BException;

	public BigDecimal ExGetNumRicercaPareri(ParereModel aParereIn) throws F3BException;

	public Vector<ParereModel> ExRicercaPareriPaginata(ParereModel aParereIn, int aPageNum)
			throws F3BException;

	public FascicoloSigeModel ExRicercaFascicoloSigeByKey(BigDecimal aKey) throws F3BException;

	public FascicoloSigeEstesoModel ExRicercaEstesaFascicoloSigeByKey(BigDecimal aKey) throws F3BException;

	public FascicoloSigeEstesoModel ExRicercaEstesaFascicoloSigeByKey(BigDecimal aKey, boolean complete)
			throws F3BException;

	public FascicoloSigeEstesoModel ExRicercaFascicoloSigeByAnnoNumCodUfficio(FascicoloSigeModel aFascicolo)
			throws F3BException;

	public Vector ExRicercaSentenze(FascicoloSigeEstesoModel aFascicolo) throws F3BException;

	public Vector ExRicercaFascicoloSigeByEstremiPagina(RicercaFascicoloSigeModel aRicFascModel, int aPage)
			throws F3BException;

	public BigDecimal ExGetNumRicercaFascicoloSigeByEstremi(RicercaFascicoloSigeModel aRicFascModel)
			throws F3BException;

	// MEV_57: aggiunto parametro di passaggio
	public Vector ExRicercaFascicoliBySoggettoPagina(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodDistretto, int aPageNum, String majorOffice)
			throws F3BException;

	// MEV_57: aggiunto parametro di passaggio
	public BigDecimal ExGetNumRicercaFascicoliBySoggetto(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodDistretto, String majorOffice) throws F3BException;

	public Vector ExRicercaFascSigeDelSoggetto(SoggettoModel aSogModel, String strCodUfficioUtenteConnesso,
			String lCodDistretto) throws F3BException;

	public ByteArrayOutputStream ExStampaProcedimento(BigDecimal aIdFascicolo, String lTipoUfficio,
			UtenteModel aUtenteModel) throws F3BException;

	public ByteArrayOutputStream ExStampaAvvocato(BigDecimal aIdFascicolo, String lTipoUfficio,
			UtenteModel aUtenteModel) throws F3BException;

	public ByteArrayOutputStream ExStampaFoglioComplementare(BigDecimal aIdFascicolo, String lTipoUfficio,
			UtenteModel aUtenteModel, String templateName) throws F3BException;

	public ResidenzaAssociataModel ExRicercaResidenzaFascicoloSigeCorrente(BigDecimal aIdFascicolo)
			throws F3BException;

	public ResidenzaAssociataModel ExRicercaDomicilioFascicoloSigeCorrente(BigDecimal aIdFascicolo)
			throws F3BException;

	public ResidenzaAssociataModel ExModificaResidenzaFascicoloSige(ResidenzaAssociataModel aResidenza)
			throws F3BException;

	public ResidenzaAssociataModel ExInserisciResidenzaFascicoloSige(ResidenzaAssociataModel aResidenza)
			throws F3BException;

	public void ExCancellaResidenzaProcedimentoSige(BigDecimal IdResidenza, BigDecimal IdFascicolo)
			throws F3BException;

	public void ExCancellaDomicilioProcedimentoSige(BigDecimal IdResidenza, BigDecimal IdFascicolo)
			throws F3BException;

	public FascicoloSigeEstesoModel ExRicercaEstesaFascicoloSigeByAnnoNumCodUfficio(
			FascicoloSigeModel aFascicolo) throws F3BException;

	public FascicoloSigeEstesoModel ExRicercaEstesaFascicoloSigeByAnnoNumCodUfficio(
			FascicoloSigeModel aFascicolo, boolean complete) throws F3BException;

	public FascicoloSigeModel ExRicercaFascicoloSigeByAnnoProgrCodUfficio(FascicoloSigeModel aFascicolo)
			throws F3BException;

	public void ExModificaFascicoloSige(FascicoloSigeModel aFascicoloSige, RichiestaSigeModel aRichiesta)
			throws F3BException;

	public void ExModificaFascicoloSige(FascicoloSigeModel aFascicoloSige) throws F3BException;

	public Vector ExRicercaFascicoloSige(FascicoloSigeModel aFascicoloSige) throws F3BException;

	public Vector ExRicercaFascicoloSigeBySentenza(BigDecimal aIdSentenza) throws F3BException;

	public FascicoloSigeModel ExInserisciFascicoloSigeManuale(FascicoloSigeModel aFascicoloSige,
			RichiestaSigeModel aRichiestaSige, MagistratoAssegnatarioModel aMagistrato,
			FasSigeDetenzioneModel lDetenzione) throws F3BException;

	public void ExDefinizioneManualeFascicoloSige(ProvvedimentoSigeModel aProvvedimentoSige,
			EventoModel aEvento, FascicoloSigeModel aFascicoloSige) throws F3BException;

	public void ExModificaDefinizioneManualeFascicoloSige(ProvvedimentoSigeModel aProvvedimentoSige,
			EventoModel aEvento, FascicoloSigeModel aFascicoloSige) throws F3BException;

	public void ExAnnullaDefinizioneManualeFascicoloSige(ProvvedimentoSigeModel aProvvedimentoSige,
			EventoModel aEvento, FascicoloSigeModel aFascicoloSige) throws F3BException;

	// MEV_57: aggiunto parametro di passaggio nella Ricerca
	public Vector ExRicercaFascicoloSigeEstesaPagina(RicercaFascicoloSigeModel aRicFascModel, int aPage,
			String majorOffice) throws F3BException;

	// MEV_57: aggiunto parametro di passaggio nella Ricerca
	public BigDecimal ExGetNumRicercaFascicoloSigeEstesa(RicercaFascicoloSigeModel aRicFascModel,
			String majorOffice) throws F3BException;

	// 20181031: aggiunto parametro di passaggio
	public Vector ExRicercaFasSigePerLaSentenza(SentenzaModel aSenModel, String majorOffice)
			throws F3BException;

	public Vector ExRicercaFascicoloSigeByFasSiep(BigDecimal aIdFasSiep, String strCodUfficioUtenteConnesso)
			throws F3BException;

	// 20181031: aggiunto parametro di passaggio
	public Vector ExRicercaFasSigeByDatiFasSiep(FascicoloSiepModel aFasSiep,
			String strCodUfficioUtenteConnesso, String majorOffice) throws F3BException;

	public Vector ExRicercaElencoFascicoliUnificati(FascicoloSigeModel aModel) throws F3BException;

	public EventoModel ExUpdateValidaDefinizioneProcedimento(EventoModel aEvento,
			FascicoloSigeModel aFascicolo) throws F3BException;

	public FascicoloSigeModel ExInserisciFascicoloSigeSoggettoIgnoto(FascicoloSigeModel aFascicoloSige,
			RichiestaSigeModel aRichiestaSige, MagistratoAssegnatarioModel aMagistrato,
			FasSigeDetenzioneModel lDetenzione) throws F3BException;

	public Vector<FascicoloSigeEstesoModel> ExRicercaStatisticaFascicoloSigeByEstremi(
			RicercaFascicoloSigeModel aRicFascModel) throws F3BException;

	public EventoModel ExInserisciDataInvioAtti(ProvvedimentoSigeModel lProMod, EventoModel aEventoModel)
			throws F3BException;

	public EventoModel ExModificaDataInvioAtti(EventoModel aEventoModel) throws F3BException;

	public EventoModel ExRicercaDataInvioAtti(BigDecimal idFascicolo) throws F3BException;

	public void ExCancellaDataInvioAttiInArchivio(BigDecimal idEvento) throws F3BException;

	public void ExUpdateDataDefinizione(FascicoloSigeModel aFascicolo, Connection lConn) throws F3BException;

	public FascicoloSigeModel ExModificaIdFascicoloSigeOrigine(FascicoloSigeModel aFascicoloSigeModel)
			throws F3BException;

	public FascicoloSigeModel ExRicercaFascicoloCollegato(BigDecimal idFascicolo) throws F3BException;

	public Vector ExRicercaSentenzeAssegnateFascicolo(BigDecimal aIdFascicoloSige) throws F3BException;

	// MEV_65: aggiunti metodi per gestire nuove funzionalita'
	public Vector<FascicoloSigeEstesoModel> ExRicercaSoggettiSigePerPosizioneGiuridica(
			RicercaFascicoloSigeModel rfsm, int pagine) throws F3BException;

	public BigDecimal ExGetNumRicercaSoggettiSigePerPosizioneGiuridica(RicercaFascicoloSigeModel rfsm,
			int pagine) throws F3BException;

	public Vector<FascicoloSigeEstesoModel> ExRicercaProcedimentiSigeConRicorsoOpposizione(
			RicercaFascicoloSigeModel rfsm, int pagine) throws F3BException;

	public BigDecimal ExGetNumRicercaProcedimentiSigeConRicorsoOpposizione(RicercaFascicoloSigeModel rfsm,
			int pagine) throws F3BException;
	// FINE MEV_65

}