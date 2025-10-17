package siap.sius.fascicolo.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.evento.model.XModel;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sius.cancassfascsius.model.CancAssFascSiusModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.produzioneatti.model.ParereModel;

/**
 * IFascicoloSIUS - Classe di Interfaccia per Controller Fascicolo SIUS
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IFascicoloSius {
	public FascicoloGPModel ExInserisciFascicoloSius(FascicoloGPModel aFascicoloGPModel) throws F3BException;

	public FascicoloGPModel ExInserisciFascicoloDaSius(FascicoloGPModel aFascicoloGPModel,
			String aIdEventoInviato) throws F3BException;

	public FascicoloGPModel ExRicercaFascicoloByKey(BigDecimal aIdFascicoloSius) throws F3BException;

	public Vector ExRicercaFascicoloSiusBySoggetto(SoggettoModel aSogModel) throws F3BException;

	public Vector ExRicercaResidenzaByProcedimentoSius(BigDecimal aIdFasSius, char aTipoResidenza)
			throws F3BException;

	public FascicoloGPModel ExRicercaFascicoloByAnnoProgrCodUfficio(BigDecimal aChiaveAnno,
			BigDecimal aChiaveProgr, String ufficioUtenteConnesso) throws F3BException;

	public FascicoloGPModel ExRicercaFascicoloByAnnoProgrCodUfficioNoControl(BigDecimal aChiaveAnno,
			BigDecimal aChiaveProgr, String ufficioUtenteConnesso) throws F3BException;

	public FascicoloGPModel ExRicercaFascicoloByAnnoProgrCodUfficioFast(BigDecimal aChiaveAnno,
			BigDecimal aChiaveProgr, String ufficioUtenteConnesso, Connection lConn) throws F3BException;

	public Vector ExRicercaFascicoloSiusPagina(FascicoloGPModel aProgSiusModel, int aPageNum)
			throws F3BException;

	public Vector ExRicercaFascicoloSiusPaginaMinori(FascicoloGPModel aProgSiusModel, int aPageNum,
			String majorOffice) throws F3BException;

	public BigDecimal ExGetNumRicercaFascicoloSius(FascicoloGPModel aProgSiusModel) throws F3BException;

	public ByteArrayOutputStream ExStampaProcedimento(BigDecimal aIdFascicolo, String lTipoUfficio,
			UtenteModel aUtenteModel) throws F3BException;

	public ByteArrayOutputStream ExStampaProcedimentiDelSoggetto(BigDecimal aIdSoggetto, String aIdDocumento,
			XModel aStampa, String aCodUff, UtenteModel aUtenteModel) throws F3BException;

	// 20131206 - ricerca non paginata utile per la reportistica.
	public Collection<FascicoloGPModel> ExRicercaFascicoloSiusByEstremi(FascicoloSiusModel aFascModel,
			String aTipoAtto, CancAssFascSiusModel aCancAssFascSius, String aFiltroCollaboratore)
			throws F3BException;

	// 20131206 - generazione foglio xls.
	public ByteArrayOutputStream ExReportFascicoloSiusByEstremiXLS(FascicoloSiusModel aFascModel,
			String aTipoAtto, CancAssFascSiusModel aCancAssFascSius, String aFiltroCollaboratore,
			UfficioModel aUfficio, HashMap<String, Object> aParams) throws F3BException;

	public Vector ExRicercaFascicoloSiusByEstremiPagina(FascicoloSiusModel aFascModel, String sTipoAtto,
			CancAssFascSiusModel aCancAssFascSius, String aFiltroCollaboratore, int aPage)
			throws F3BException;

	public BigDecimal ExGetNumRicercaFascicoloSiusByEstremi(FascicoloSiusModel aFascModel, String sTipoAtto,
			CancAssFascSiusModel aCancAssFascSius, String aFiltroCollaboratore) throws F3BException;

	public FascicoloGPModel ExModificaFascicoloSius(FascicoloGPModel aFascicoloGPModel) throws F3BException;

	public void ExCancellaResidenzaProcedimentoSius(BigDecimal aIdResidenza, BigDecimal aIdFascicolo)
			throws F3BException;

	public void ExCancellaDomicilioProcedimentoSius(BigDecimal aIdResidenza, BigDecimal aIdFascicolo)
			throws F3BException;

	public ResidenzaAssociataModel ExInserisciResidenzaFascicoloSius(ResidenzaAssociataModel aResidenza)
			throws F3BException;

	public ResidenzaAssociataModel ExModificaResidenzaFascicoloSius(ResidenzaAssociataModel aResidenza)
			throws F3BException;

	public ResidenzaAssociataModel ExRicercaResidenzaFascicoloSiusCorrente(BigDecimal aIdFascicolo)
			throws F3BException;

	public Vector ExRicercaFascicoliBySoggettoPagina(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodUffOTrib, String lCodDistretto,
			String lIncludeArchiviati, String lCodContenuto, Date dataDalInCanc, Date dataAlInCanc,
			int aPageNum, String majorOffice) throws F3BException;

	public BigDecimal ExGetNumRicercaFascicoliBySoggetto(SoggettoModel aSogModel,
			String lCodUfficioUtenteConnesso, String lCodUffOTrib, String lCodDistretto,
			String lIncludeArchiviati, String lCodContenuto, Date dataDalInCanc, Date dataAlInCanc)
			throws F3BException;

	public Vector ExRicercaFascicoliDelSoggetto(SoggettoModel aSogModel, String strCodUfficioUtenteConnesso,
			String strCodUffOTrib, String lCodDistretto, String lIncludeArchiviati, String lCodContenuto,
			Date dataDalInCanc, Date dataAlInCanc) throws F3BException;

	public Vector ExRicercaFascicoliPerNumeroSIEP(BigDecimal lId_FascicoloSiep) throws F3BException;

	public Vector ExRicercaFascicoliPerDataFinePena(String aCodUfficioUtenteConnesso, Date dataDalIscrizione,
			Date dataAlIscrizione, Date dataDalFinePena, Date dataAlFinePena, String lIncludeArchiviati,
			String lCodPosGiuridica, String lCodContenuto, int aPageNum) throws F3BException;

	public ByteArrayOutputStream ExReportFascicoliPerDataFinePenaExcel(UfficioModel aUfficioUtenteConnesso,
			Date aDataDalIscrizione, Date aDataAlIscrizione, Date aDataDalFinePena, Date aDataAlFinePena,
			String aIncludeArchiviati, String aCodPosGiuridica, String aCodContenuto,
			String aDescrPosGiuridica, String aDescrContenuto) throws F3BException;

	public BigDecimal ExGetNumRicercaFascicoliPerDataFinePena(String aCodUfficioUtenteConnesso,
			Date dataDalIscrizione, Date dataAlIscrizione, Date dataDalFinePena, Date dataAlFinePena,
			String lIncludeArchiviati, String lCodPosGiuridica, String lCodContenuto) throws F3BException;

	public FascicoloGPModel ExInserisciFascicoloSiusManuale(FascicoloGPModel aFascicoloGPModel)
			throws F3BException;

	public Vector ExRicercaElencoFascicoliUnificati(FascicoloSiusModel aModel) throws F3BException;

	public Vector ExRicercaFascicoliXIdOrigine(BigDecimal aIdFascicoloOrigine) throws F3BException;

	public FascicoloGPModel ExRicercaFascicoloByGenProc(BigDecimal aIdGenProc) throws F3BException;

	public Vector ExRicercaFascicoloSiusBySoggettoForStorico(SoggettoModel aSogModel) throws F3BException;

	public void ExInserisciDefinizioneFascicoloSius(FascicoloGPModel aFasGPMod) throws F3BException;

	public Vector ExRicercaPareriPaginata(ParereModel aParereIn, int aPageNum) throws F3BException;

	public BigDecimal ExGetNumRicercaPareri(ParereModel aParereIn) throws F3BException;

	public Vector ExRicercaFascicoliByIdSoggettoCodOggetto(BigDecimal aIdSoggetto, String aCodOggetto)
			throws F3BException;

	public FascicoloSiusModel ExModificaFascicoloSius(FascicoloSiusModel aFascicoloSius) throws F3BException;

	public FascicoloSiusModel ExModificaIdFascicoloSiusOrigine(FascicoloSiusModel aFascicoloSiusModel)
			throws F3BException;

	public FascicoloGPModel ExRicercaFascicoloCollegato(BigDecimal aIdFascicoloSius) throws F3BException;

	// 20251010 [SG]: paginata la ricerca
	public Vector ExRicercaFascicoliByMagistratoSorvAssegnatarioPaged(String aCodMagistrato, String aCodUfficio,
			String[] aStato, int aPage) throws F3BException;

	public Vector ExRicercaFascicoliByMagistratoSorvAssegnatario(String aCodMagistrato, String aCodUfficio,
			String[] aStato) throws F3BException;

	public BigDecimal ExGetCountProcedimenti(String lCodMagistrato, String lCodUfficio, String[] lStato)
			throws F3BException;

	public Date ExGetDataDefinizineFinale(BigDecimal aIdFascicoloSius, Connection aConn) throws Exception;

	public ByteArrayOutputStream ExGetCertificatoPenale(BigDecimal aKey) throws F3BException;

	public BigDecimal ExGetLengthCertPenaleByIdFascicolo(BigDecimal aIdFascicoloSius) throws F3BException;

	// MEV10-s3: aggiunto metodo per gestire passaggio alla maggiore età del soggetto
	public void ExModificaVisibilitaMinoreFascicoloSius(FascicoloGPModel lFasGPMod) throws F3BException;

	// AVVOCATURA: aggiunto metodo di ricerca
	public Vector ricercaSoggettiConProcedimenti(SoggettoModel sm, String codDistretto,
			String codFiscaleAvvocato, String codTipoUfficio) throws F3BException;

	// AVVOCATURA: aggiunto metodo di ricerca
	public Vector elencoProcedimentiDelSoggetto(BigDecimal idSoggetto, String codDistretto,
			String codFiscaleAvvocato, String codTipoUfficio) throws F3BException;

	// AVVOCATURA: aggiunto metodo di ricerca
	public ByteArrayOutputStream richiestaStampa(BigDecimal idFascicoloSius, String codDistretto,
			String codiceFiscaleAvvocato, String codTipoUfficio, String codUfficio) throws F3BException;

	// AVVOCATURA: aggiunto metodo di ricerca
	public String ricercaCodUfficioAppartenenza(BigDecimal idFascicoloSius, String codiceFiscaleAvvocato,
			String codDistretto, String codTipoUfficio) throws F3BException;

}