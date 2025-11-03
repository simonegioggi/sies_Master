package siap.siep.modulocumulo.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import f3b.util.F3BException;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.DatiOperazioneModel;
import siap.sico.webservice.model.DatiNscToSiesModel;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.sentenza.model.SentenzaSoggettoFascicoloModel;
import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;

/**
 * <p>
 * Title: ModuloCumuloController
 * </p>
 * <p>
 * Description: Classe Controller con i metodi di gestione del ModuloCumulo SIEP in particolare espone i
 * metodi per effettuare le estrazioni dei dati analitici
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
public interface IModuloCumulo {

	public void ExInserisciTitoloInIstruttoria(BigDecimal aIdIstruttoriaCumulo, BigDecimal aIdFascicoloSiep,
			DatiOperazioneModel aDatoOpModel, Connection aDBConnection, BigDecimal aIdMessaggio,
			String aTipoIscrizione) throws F3BException;

	public void ExInserisciTitoliProprioUfficioInIstruttoria(BigDecimal aIdIstruttoriaCumulo,
			String[] lIdFascicoliMioUfficio, DatiOperazioneModel aDatoOpModel, Connection aDBConnection, // null
			BigDecimal aIdFasCumulante, String aTipoIscrizione) throws F3BException;

	/**
	 * Metodo che pilota l'estrazione dei dati analitici di un dato fascicolo agganciadoli al CUMULO.
	 *
	 * @param aIdFascicoloCumulato
	 * @param aCumulo
	 * @param aDBConnection
	 * @throws F3BException
	 */
	public void ExEstraiDatiAnalitici(BigDecimal aIdFascicoloCumulato, TitoloCumulatoModel aTitolo,
			Connection aDBConnection) throws F3BException;

	/**
	 * Metodo che estrae i dati delle Misure_sicurezza del fascicolo da cumulare e li carica nella tabella
	 * MISURA_SICUREZZA_CUMULO agganciandoli al record CUMULO dell'istruttoria
	 *
	 * @param aIdFascicoloCumulato
	 *            = id del fascicolo da cui estrarre i dati
	 * @param aTitolo
	 *            = model del TITOLO_CUMULATO a cui agganciare i dati
	 * @param aDBConnection
	 *            = eventuale connessione su cui lavorare per garantire la transazione. Se = null il metodo
	 *            apre una nuova connessione.
	 * @throws F3BException
	 */
	public void ExEstraiMisuraSicurezzaPerCumulo(BigDecimal aIdFascicoloCumulato, TitoloCumulatoModel aTitolo,
			Connection aDBConnection) throws F3BException;

	/**
	 * Metodo che estrae i dati relativi ai Reati del fascicolo da cumulare e li carica nella tabella
	 * REATO_CUMULO agganciandoli al record CUMULO dell'istruttoria
	 *
	 * @param aIdFascicoloCumulato
	 *            = id del fascicolo da cui estrarre i dati
	 * @param aCumulo
	 *            = model del CUMULO a cui agganciare i dati
	 * @param aDBConnection
	 *            = eventuale connessione su cui lavorare per garantire la transazione. Se = null il metodo
	 *            apre una nuova connessione.
	 * @throws F3BException
	 */
	// public void ExEstraiReatiPerCumulo (BigDecimal aIdFascicoloCumulato, CumuloModel aCumulo, Connection
	// aDBConnection ) throws F3BException ;

	public EventoModel ExUpdateValidaRichiestaTrasmissioneAtti(EventoModel aEventoModel) throws F3BException;

	/**
	 *
	 * @param aDeliveryMode
	 * @param aCodTipoMessaggio
	 * @param aListaTipoOperazione
	 * @param aListaEsiti
	 * @param aFlagVisto
	 * @param aChiaveAnnoSiep
	 * @param aChiaveProgrSiep
	 * @param aChiaveUfficioSiep
	 * @param aCodUfficioMitt
	 * @param aCodUfficioDest
	 * @param aDataTrasmissioneDal
	 * @param aDataTrasmissioneAl
	 * @param aPage
	 * @return
	 * @throws F3BException
	 */
	public Vector<MessaggioModel> ExRicercaMessaggi(String aDeliveryMode, String aCodTipoMessaggio,
			Vector<String> aListaTipoOperazione, Vector<String> aListaEsiti, String aFlagVisto,
			BigDecimal aChiaveAnnoSiep, BigDecimal aChiaveProgrSiep, String aChiaveUfficioSiep,
			String aCodUfficioMitt, String aCodUfficioDest, Date aDataTrasmissioneDal,
			Date aDataTrasmissioneAl, String aCognome, String aNome, BigDecimal aChiaveAnnoFasCumulante,
			BigDecimal aChiaveProgrFasCumulante, String aChiaveUfficioFasCumulante, int aPage)
			throws F3BException;

	public BigDecimal ExCountRicercaMessaggi(String aDeliveryMode, String aCodTipoMessaggio,
			Vector<String> aListaTipoOperazione, Vector<String> aListaEsiti, String aFlagVisto,
			BigDecimal aChiaveAnnoSiep, BigDecimal aChiaveProgrSiep, String aChiaveUfficioSiep,
			String aCodUfficioMitt, String aCodUfficioDest, Date aDataTrasmissioneDal,
			Date aDataTrasmissioneAl, String aCognome, String aNome, BigDecimal aChiaveAnnoFasCumulante,
			BigDecimal aChiaveProgrFasCumulante, String aChiaveUfficioFasCumulante) throws Exception;

	/**
	 *
	 * @param aDeliveryMode
	 * @param aCodTipoMessaggio
	 * @param aListaTipoOperazione
	 * @param aListaEsiti
	 * @param aFlagVisto
	 * @param aChiaveAnnoSiep
	 * @param aChiaveProgrSiep
	 * @param aChiaveUfficioSiep
	 * @param aCodUfficioMitt
	 * @param aCodUfficioDest
	 * @param aDataTrasmissioneDal
	 * @param aDataTrasmissioneAl
	 * @param aPage
	 * @return
	 * @throws F3BException
	 */
	public Vector<MessaggioModel> ExRicercaMessaggi(String aDeliveryMode, Vector<String> aListaTipoMessaggio,
			Vector<String> aListaTipoOperazione, Vector<String> aListaEsiti, String aFlagVisto,
			BigDecimal aChiaveAnnoSiep, BigDecimal aChiaveProgrSiep, String aChiaveUfficioSiep,
			String aCodUfficioMitt, String aCodUfficioDest, Date aDataTrasmissioneDal,
			Date aDataTrasmissioneAl, String aCognome, String aNome, BigDecimal aChiaveAnnoFasCumulante,
			BigDecimal aChiaveProgrFasCumulante, String aChiaveUfficioFasCumulante, int aPage)
			throws F3BException;

	public BigDecimal ExCountRicercaMessaggi(String aDeliveryMode, Vector<String> aListaTipoMessaggio,
			Vector<String> aListaTipoOperazione, Vector<String> aListaEsiti, String aFlagVisto,
			BigDecimal aChiaveAnnoSiep, BigDecimal aChiaveProgrSiep, String aChiaveUfficioSiep,
			String aCodUfficioMitt, String aCodUfficioDest, Date aDataTrasmissioneDal,
			Date aDataTrasmissioneAl, String aCognome, String aNome, BigDecimal aChiaveAnnoFasCumulante,
			BigDecimal aChiaveProgrFasCumulante, String aChiaveUfficioFasCumulante) throws Exception;

	/**
	 *
	 * @param aSentenzaModel
	 * @param aSoggettoModel
	 * @param aFascicoloSiepModel
	 * @param aPage
	 * @return
	 * @throws Exception
	 */
	// public Vector <FascicoloSiepModel> ExRicercaProcedimentiPerTitoloSoggetto (SentenzaModel aSentenzaModel
	public Vector<SentenzaSoggettoFascicoloModel> ExRicercaProcedimentiPerTitoloSoggetto(
			SentenzaModel aSentenzaModel, SoggettoModel aSoggettoModel,
			FascicoloSiepModel aFascicoloSiepModel, int aPage) throws Exception;

	public BigDecimal ExCountProcedimentiPerTitoloSoggetto(SentenzaModel aSentenzaModel,
			SoggettoModel aSoggettoModel, FascicoloSiepModel aFascicoloSiepModel) throws Exception;

	public BigDecimal ExInserisciSollecitoRichiestaAtti(EventoNotificaModel lEveNotModel,
			SollecitoEsitoTrasmissioneModel lSolMod, CompetenzaModel lCompetenza) throws Exception;

	// MEV 16 CUMULO: aggiunto metodo
	public void ExInserisciTitoloInIstruttoria(BigDecimal idIstruttoriaCumulo, DatiNscToSiesModel dntsm,
			DatiOperazioneModel dom, String tipoIscrizione, Connection aDBConnection) throws Exception;

	/* MEV_2025-48 – 2.14 Caricamento Istruttoria Annullata
     * il metodo diventa public per essere richiamato anche da IStruttoriaCumuloController */
    public void EstraiDaPrecedenteCumulo(IstruttoriaCumuloModel aUltimaIstruttoria,
            BigDecimal aIdIstruttoriaCumulo, FascicoloSiepModel aFascicoloSiepCumulato,
            DatiOperazioneModel aDatoOpModel, Connection aDBConnection, String aTipoIscrizione)
            throws F3BException;
}