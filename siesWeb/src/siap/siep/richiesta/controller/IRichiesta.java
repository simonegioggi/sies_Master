package siap.siep.richiesta.controller;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.dao.DAOException;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: RichiestaController
 * </p>
 * <p>
 * Description: Classe Controller per Richiesta
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
public interface IRichiesta {

	public OutputStream ExStampaRichiesta(TreeModel aTreeModel, int aTypeReport) throws F3BException;
  
	public EventoModel ExUpdateValidaEmissioneComunicazioni(EventoModel aEventoRid, EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException;

  /**
   * Inserisce/Aggiorna l'evento e le notifiche
	 * 
   * @param aEvento 
   * @return
   * @throws F3BException
   */
  public EventoNotificaModel ExInserisciOModificaNotifica(EventoNotificaModel aEvento) throws F3BException;
  
	public EventoModel ExUpdateValidaRichiestaAccReato(EventoModel aEventoRid, EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException;
  
	public EventoModel ExUpdateValidaRichiesteConCodice(EventoModel aEventoRid, EventoModel aEvento,
			String lCodiceNomeProvv, FascicoloSiepModel aFascicolo, BigDecimal aGiorniLA) throws F3BException;
  
  /**
	 * <p>
	 * Valida la Richiesta al GE di <i>'quantificare gli aumenti per i singoli reati posti in continuazione
	 * con il reato principale'</i> nel caso di Depenalizzazione e Incostituzionalità (artt 671 e 673 cpp).
	 * </p>
   * Se ancora presente l'evento di appoggio (aEventoRid):<br>
   * - sgancia le annotazioni da tale evento e le riaggancia all'evento da validare<br>
   * - sgancia la fungibilità da tale evento e la riaggancia all'evento da validare<br>
   * - sgancia la pena residua da tale evento e la riaggancia all'evento da validare (??verificare??)<br>
   * - elimina l'evento di appoggio <br>
	 * 
	 * @param aEventoRid
	 *            - Evento di appoggio a cui è collegata l'annotazione (se presente)
	 * @param aEvento
	 *            - Evento corrente da validare
   * @param aFascicolo
   * @return
   * @throws F3BException
   */
	public EventoModel ExUpdateValidaRichDetPenAboReato(EventoModel aEventoRid, EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException;

	public EventoModel ExUpdateValidaRichiestaGenerica(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExUpdateValidaRichiestaEsitoEspulsione(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException;

	public void ExCancellaTrasmissioneCompetenza(EventoNotificaModel lEveModel) throws DAOException,
			F3BException;
  
	public void ExUpdStatoProcPresaincaricoStessaBDI(FascicoloSiepModel aFascicolo, Date dataEmissione,
			Date dataInserimento, String codOperatore, String codUfficio) throws DAOException, F3BException;
  
  // 01/2014 AMBROS	- MISURE DI SIICUREZZA SIEP
  /**
	 * <p>
	 * Valida la Annotazione della SORVEGLIANZA
	 * </p>
	 * <p>
	 * su DECISIONE di Applicazione Misure Sicurezza
	 * </p>
  */
	public EventoModel ExValidaAnnotazioneDecisioneDellaSorveglianza(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException;
  
	public EventoModel ExValidaAnnotazioneOComunicazioneApplicazioneMS(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException;
  
	// 09-2015 MEV_2 (Misure Sicurezza) - STEP_2 
	//	Validazione Archiviazione per Provv Giudice/Cassazione (Mis Sic Provvisoria o disposta Fuori Sentenza) 
	public EventoModel ExValidaArchiviazionePerProvvGiudiceEsecuzione(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException;

	// Validazione del provvedimento di Trasmissione Atti per Competenza ai fini di Emissione Provvedimento di
	// Cumulo
	public EventoModel ExValidaTrasmissioneAttiPerCompetenza(EventoModel aEvento,
			FascicoloSiepModel aFascicolo, CompetenzaModel aCompetenza) throws F3BException;

	/**
	 * MEV_39: aggiunto metodo per la validazione di un evento di archiviazione provvedimento di cumulo
	 * 
	 * @param em
	 * @param fsm
	 * @throws F3BException
	 */
	public void ExValidaArchiviazionePerProvvCumulo(EventoModel em, FascicoloSiepModel fsm)
			throws F3BException;

	//Validazione del provvedimento di 'Comunicazione Rigetto Richiesta Atti per Competenza' ai fini di Emissione Provvedimento di Cumulo
  	public EventoModel ExValidaRigettoRichiestaAttiPerCompetenza(EventoModel aEvento, FascicoloSiepModel aFascicolo, CompetenzaModel aCompetenza) throws F3BException;

}