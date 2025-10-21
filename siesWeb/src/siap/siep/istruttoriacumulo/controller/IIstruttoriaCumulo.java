package siap.siep.istruttoriacumulo.controller;

/**
* <p>Title: IstruttoriaCumuloController</p>
* <p>Description: Classe Controller per IstruttoriaCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import f3b.util.F3BException;

import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.model.EsitoArchiviazioniCumuloModel;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.model.RichiesteInviateCumModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.modulocumulo.util.CalcoloPenaCumuloModel;

@SuppressWarnings("rawtypes")
public interface IIstruttoriaCumulo {

	public IstruttoriaCumuloModel ExInserisciIstruttoriaCumulo(IstruttoriaCumuloModel aIstruttoriaCumulo,
			FascicoloSiepModel aFascicoloSiep) throws F3BException;

	public BigDecimal ExCountIstruttoriaCumuloPaged(IstruttoriaCumuloModel aIstruttoriaCumulo)
			throws F3BException;

	public Vector<IstruttoriaCumuloModel> ExRicercaIstruttoriaCumulo(
			IstruttoriaCumuloModel aIstruttoriaCumulo) throws F3BException;

	public void ExModificaIstruttoriaCumulo(IstruttoriaCumuloModel aIstruttoriaCumulo) throws F3BException;

	public void ExCancellaIstruttoriaCumulo(IstruttoriaCumuloModel aIstruttoriaCumulo) throws F3BException;

	public BigDecimal ExGetCountIstruttoriaCumulo(IstruttoriaCumuloModel aIstruttoriaCumulo)
			throws F3BException;

	public IstruttoriaCumuloModel ExRicercaIstruttoriaCumuloById(BigDecimal aIdIstruttoriaCumulo)
			throws F3BException;

	public Vector<IstruttoriaCumuloModel> ExRicercaIstruttoriaCumuloPaged(
			IstruttoriaCumuloModel aIstruttoriaCumulo, int aPage) throws F3BException;

	public void ExAnnullaIstruttoriaCumulo(IstruttoriaCumuloModel aIstruttoriaCumulo) throws F3BException;

	public BigDecimal ExCountIstruttoriaPerFasSIEP(FascicoloSiepModel aFasSiep, String ufficio)
			throws F3BException;

	public Vector<IstruttoriaCumuloModel> ExRicercaIstruttoriaPerTitoloCumulatoPaged(
			IstruttoriaCumuloModel aIstruttoriaCumulo, int aPage) throws F3BException;

	public BigDecimal ExCountIstruttoriaPerTitoloCumulato(IstruttoriaCumuloModel aIstruttoriaCumulo)
			throws F3BException;

	public Vector<IstruttoriaCumuloModel> ExRicercaIstruttoriaPerFasSIEPpaged(
			FascicoloSiepModel aFascicoloSiep, String ufficio, int aPage) throws F3BException;

	/**
	 * Recupera l'elenco dei fascicoli trasmessi per competenza sul fascicolo corrente sia presi in carico che
	 * non ancora presi in carico purché non ancora iscritti in cumulo.
	 * 
	 * @param aChiaveAnno
	 * @param aChiaveProgr
	 * @param aChiaveUfficio
	 * @param aFlagInCarico
	 *            (S,N) S - già presi in carico, N - non ancora presi in carico null = tutti
	 * @throws F3BException
	 */
	public Vector<MessaggioModel> ExRicercaFascicoliTrasmessi(BigDecimal aChiaveAnno, BigDecimal aChiaveProgr,
			String aChiaveUfficio, String aFlagInCarico) throws F3BException;

	public Vector<TitoloCumulatoModel> ExRicercaTitoliByIstruttoria(BigDecimal aIdIstruttoriaCumulo)
			throws F3BException;

	public Vector<TitoloCumulatoModel> ExRicercaTitoliByIstruttoriaOrderBy(BigDecimal aIdIstruttoriaCumulo,
			String aOrdinamento) throws F3BException;

	public BigDecimal ExInserisciAnnotazioneEsitoTrasmComp(EventoNotificaModel aEventoNotifica,
			AnnotazioneEsitoTrasmissioneModel aAnnotazioneEsitoModel, BigDecimal aIdMess) throws F3BException;

	public void ExUpdateOrdinamentoTitoli(IstruttoriaCumuloModel aIstruttoriaCumulo) throws F3BException;

	public ByteArrayOutputStream ExStampaProspettoTitoliCumulo(IstruttoriaCumuloModel aIstruttoriaCumulo,
			Vector aListaTitoli, FascicoloSiepModel lFascicoloModel, UtenteModel aUtenteMod,
			UfficioModel lUfficioMod, String aIdTemplate) throws F3BException;

	public ByteArrayOutputStream ExStampaProspettoPropostaCumulo(IstruttoriaCumuloModel aIstruttoriaCumulo,
			Vector aListaTitoli, FascicoloSiepModel lFascicoloModel, UtenteModel aUtenteMod,
			UfficioModel lUfficioMod, String aIdTemplate) throws F3BException;

	public Vector<TitoloCumulatoModel> ExRicercaTitoliValidiByIstruttoriaOrderBy(
			BigDecimal aIdIstruttoriaCumulo, String aOrdinamento) throws F3BException;

	// MEV 26
	public IstruttoriaCumuloModel ExCercaIsruttoriaPerAltriDatiCumulo(BigDecimal aIdFascicoloSiep,
			BigDecimal aIdEvento, Connection lConn) throws F3BException;

	public String ExInserisciIstruttoriaCumuloWithoutSequence(IstruttoriaCumuloModel aIstruttoriaCumModel,
			Connection lConn) throws F3BException;

	// MEV 42 (prova)
	public ByteArrayOutputStream ExStampaRichiestaDelPMCumulo(IstruttoriaCumuloModel aIstruttoriaCumulo,
			Vector aListaTitoli, FascicoloSiepModel lFascicoloModel, UtenteModel aUtenteMod,
			UfficioModel lUfficioMod, String aIdTemplate, RichiesteInviateCumModel lRichMod)
			throws F3BException;

	public Vector<TitoloCumulatoModel> ExRicercaTitoliByIstruttoriaDataReatoCumOrderBy(
			BigDecimal aIdIstruttoriaCumulo, String aOrdinamento, Date lDataRea) throws F3BException;

	// La Ricerca è la stessa di ExRicercaTitoliByIstruttoriaOrderBy con in più La join con Beneficio_Cumulo
	public Vector<TitoloCumulatoModel> ExRicercaTitoliBeneficiCumByIstruttoriaOrderBy(
			BigDecimal aIdIstruttoriaCumulo, String aOrdinamento, String aCodNaturaBen,
			Vector<String> aCodTipiBen) throws F3BException;

	// La Ricerca è la stessa di ExRicercaTitoliByIstruttoriaOrderBy con in più La join con Sanzione_Sost_Cum
	public Vector<TitoloCumulatoModel> ExRicercaTitoliSanzioneSostCumByIstruttoriaOrderBy(
			BigDecimal aIdIstruttoriaCumulo, String aOrdinamento, String[] lIdTitoliSelezionati)
			throws F3BException;

	// La Ricerca è la stessa di ExRicercaTitoliByIstruttoriaOrderBy con in più La join con
	// Pena_Accessoria_Cum
	public Vector<TitoloCumulatoModel> ExRicercaTitoliPenaAccCumByIstruttoriaOrderBy(
			BigDecimal aIdIstruttoriaCumulo, String aOrdinamento, String[] lIdTitoliSelezionati)
			throws F3BException;

	public Vector<TitoloCumulatoModel> ExRicercaTitoliMisureSicCumByIstruttoriaOrderBy(
			BigDecimal aIdIstruttoriaCumulo, String aOrdinamento) throws F3BException;

	public Vector<TitoloCumulatoModel> ExRicercaTitoliStatoEsecTitoloCumByIstruttoriaOrderBy(
			BigDecimal aIdIstruttoriaCumulo, String aOrdinamento, Vector<String> lCodMA,
			String[] lIdTitoliSelezionati) throws F3BException;
  // 23/04/2019	MEV70
	public BigDecimal ExCountPresenzeTitoloNellaStessaIstruttoria(BigDecimal aIdIstruttoria,
			FascicoloSiepModel aFasSiep, String ufficio) throws F3BException;

	/**
	 * Metodo x il calcolo pena a partire dai dati dell'istruttoria
	 * 
	 * @param aIdIstruttoriaCumulo
	 * @return
	 * @throws F3BException
	 */
	// public CalcoloPenaCumuloModel ExCalcolaPenaCumuloByIstruttoria (BigDecimal aIdIstruttoriaCumulo) throws
	// F3BException;
  
	public CalcoloPenaCumuloModel ExCalcolaPenaCumuloByIstruttoria(BigDecimal aIdIstruttoriaCumulo,
			BigDecimal aIdTitolo, boolean aComputaRichieste) throws F3BException;

	/* MEV_2025-52 - Aggiunta ricerca per chiaveAnno/chiaveProgr e ufficio accorpato: Aggiunto FascicoloSiepModel */
//	public BigDecimal ExCountFascicoliBySoggettoProprioUfficioPaged(SoggettoModel aSogModel,
//			String lCodUfficioUtenteConnesso, int aPage) throws F3BException;
	public BigDecimal ExCountFascicoliBySoggettoProprioUfficioPaged(SoggettoModel aSogModel,
			FascicoloSiepModel aFascModel,
			String lCodUfficioUtenteConnesso, int aPage) throws F3BException;

	/* MEV_2025-52 - Aggiunta ricerca per chiaveAnno/chiaveProgr e ufficio accorpato: Aggiunto FascicoloSiepModel */
//	public Vector<FascicoloSiepModel> ExRicercaFascicoliBySoggettoProprioUfficioPaged(SoggettoModel aSogModel,
//			String lCodUfficioUtenteConnesso, int aPage, BigDecimal lIdIstru) throws F3BException;
	public Vector<FascicoloSiepModel> ExRicercaFascicoliBySoggettoProprioUfficioPaged(SoggettoModel aSogModel,
			FascicoloSiepModel aFascModel,
			String lCodUfficioUtenteConnesso, int aPage, BigDecimal lIdIstru) throws F3BException;

	public Vector<EsitoArchiviazioniCumuloModel> ExRicercaEsitoArchiviazionideiCumulatiByIdEvento(
			BigDecimal aIdEvento) throws F3BException;

	public IstruttoriaCumuloModel ExRicercaIstruttoriaCumuloApertaByIdFasSiep(BigDecimal aIdFascicoloSiep)
			throws F3BException;

	public Vector<TitoloCumulatoModel> titoloDoppioInIstruttoria(BigDecimal aIdIstruttoria)
			throws F3BException;
  
}
