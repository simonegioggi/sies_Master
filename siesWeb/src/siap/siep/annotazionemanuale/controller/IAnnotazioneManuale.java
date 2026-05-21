package siap.siep.annotazionemanuale.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import f3b.dao.DAOException;
import f3b.util.F3BException;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaModel;
import siap.siep.annotazionemanuale.model.AnnotazioneOrdinanzaSigeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * Title: AnnotazioneManualeController Description: Classe Controller per AnnotazioneManuale
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IAnnotazioneManuale {

	public AnnotazioneManualeModel ExInserisciAnnotazioneManuale(AnnotazioneManualeModel aAnnotazioneManuale)
			throws F3BException;

	/**
	 * Inserisce l'evento e l'annotazione manuale agganciandogliela. Invocata nel caso di inserimento:
	 * Richieste e decisioni del GE<br>
	 * - depenalizazione<br>
	 * - incostituzionalità<br>
	 * - Amnistia/Indulto<br>
	 * Rideterminazione pena<br>
	 * - Presofferto e fungibilità<br>
	 *
	 * !!! Aggancia all'evento anche tutte le annotazioni trovate a sistema per lo stesso fascicolo, non
	 * ancora validate, e dello stesso tipo di quella che si sta inserendo ma con FLAG_APP_PROVVISORIA='-' (?)
	 *
	 * @param aAnnotazioneManuale
	 *            - annotazione
	 * @param aEvento
	 *            Evento della richiesta
	 * @return
	 */
	public AnnotazioneManualeModel ExInserisciAnnotazioneManualeEvento(
			AnnotazioneManualeModel aAnnotazioneManuale, EventoModel aEvento) throws F3BException;

	/**
	 * Come la funzione precedente, ma oltre a quella effettua la validazione dell'Annotazione Manuale passata
	 * tramite ID e che rappresenta la decisione del GE:
	 *
	 * @param aAnnotazioneManuale
	 * @param aEvento
	 * @param aIdAnnotazioneManuale
	 * @return
	 * @throws F3BException
	 */
	public AnnotazioneManualeModel ExInserisciAnnotazioneManualeEvento(
			AnnotazioneManualeModel aAnnotazioneManuale, EventoModel aEvento,
			BigDecimal aIdAnnotazioneManuale) throws F3BException;

	// MEV9
	/**
	 * Come la funzione precedente, ma poi lega l'annotazione inserita alla Richieta iniziale tramite ID e che
	 * rappresenta la decisione del GE:
	 *
	 * @param aAnnotazioneManuale
	 * @param aEvento
	 * @param aIdAnnotazioneManuale
	 * @return
	 * @throws F3BException
	 */
	public AnnotazioneManualeModel ExInserisciAnnotazioneManualeEventoUpd(
			AnnotazioneManualeModel aAnnotazioneManuale, EventoModel aEvento,
			BigDecimal aIdAnnotazioneRichiesta, BigDecimal aIdAnnotazioneManuale) throws F3BException;
	// End MEV 9

	/**
	 * ************************************************************************** Inserisce le decisioni del
	 * GE (depenalizzazione, incostituzionalità, amnistia/indulto). Inserisce il provvedimento Inserisce
	 * l'annotazione Inserisce/Aggiorna l'Ordinanza del GE Inserisce l'annotazione dell'ordinanza Aggiorna
	 * Tutte le annotazioni dello stesso tipo con decisione agganciandole al provvedimento corrente.
	 *
	 * Aggiorna Tutte la richieste dello stesso tipo validandole
	 *
	 * @param aAnnotazioneManuale
	 * @param aEveOrdinanzaMod
	 * @param aEveProvvedimentoMod
	 * @param aAnnotazioneOrdinanza
	 */
	public AnnotazioneManualeModel ExInserisciAnnotazioneManualeProvvedimentoRichiesta(
			AnnotazioneManualeModel aAnnotazioneManuale, EventoModel aEveOrdinanzaMod,
			EventoModel aEveProvvedimentoMod, AnnotazioneManualeModel aAnnotazioneOrdinanza,
			Vector aListaRichieste) throws F3BException;

	/**
	 * Inserisce l'evento e le Annotazioni manuali ad esso associate ed eventualmente il campo nota.
	 *
	 * @param aEvento
	 * @param aListaAnnotazioni
	 * @param aCampoNote
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExInserisciEventoAnnotazioni(EventoModel aEvento, Vector aListaAnnotazioni,
			CampoNotaModel aCampoNote, EventoModel aEveAltraAutorita,
			LicenzaLibAnticipataModel aLicLibAntModel) throws F3BException;

	// AMBROSINO - Avvenuto pagamento PP
	public EventoModel ExInserisciEventoAnnotazioniCampoNota(EventoModel aEvento, Vector aListaAnnotazioni,
			Vector aListaCampoNote, EventoModel aEveAltraAutorita) throws F3BException;

	public Vector ExRicercaAnnotazioneManuale(AnnotazioneManualeModel aAnnotazioneManuale)
			throws F3BException;

	public Vector ExRicercaAnnotazioneManualeByIdFascicolo(BigDecimal aKey) throws F3BException;

	public AnnotazioneManualeModel ExRicercaAnnotazioneManualeByKey(BigDecimal aKey) throws F3BException;

	public AnnotazioneManualeModel ExModificaAnnotazioneManuale(AnnotazioneManualeModel aAnnotazioneManuale)
			throws F3BException;

	public void ExCancellaAnnotazioneManuale(AnnotazioneManualeModel aAnnotazioneManuale) throws F3BException;

	/**
	 * Effettua la cancellazione delle annotazioni manuali <b>non ancora validate</b> dello stesso tipo di
	 * quelle passate in input. In funzione del tipo di annotazione cancella anche l'evento associato, e in
	 * caso di decisioni del GE anche l'ordinanza e relativa annotazione associata.
	 *
	 *
	 * @param aIdAnnotazioneManuale
	 * @param aFlagRichieste
	 *            - indica se trattasi di richieste al GE o decisioni/ computi. aFlagRichieste = true se
	 *            trattasi di richieste, false altrimenti
	 */
	public AnnotazioneManualeModel ExCancellaAnnotazioneManualeComputo(BigDecimal aIdAnnotazioneManuale,
			boolean aFlagRichieste) throws F3BException;

	public AnnotazioneManualeModel ExRicercaAnnotazioneManualeByIdReato(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaAnnotazioneManualeNoErrorByIdReato(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaAnnotazioniManualiByIdReato(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaAnnotazioniManuali_non_richieste_ByIdFascicolo(BigDecimal aKey)
			throws F3BException;

	/**
	 * Ricerca le annotazioni manuali per il fascicolo specificato e il tipo. Scarta le richieste al GE (A e
	 * R) ORDER BY DATA_INSERIMENTO
	 *
	 * @param aIdFascicolo
	 * @param aCodTipoAnnotazione
	 * @param aFlagValidazione
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaAnnotazioniManualiByIdFascicoloNonRichiesteTipoAnn(BigDecimal aIdFascicolo,
			String aCodTipoAnnotazione, String aFlagValidazione) throws F3BException;

	/**
	 * Ricerca le annotazioni di tipo Amnistia o Indulto legate a Richieste ('R') <b>non validate</b> senza
	 * anticipazione
	 *
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaAnnotazioneManualeRichiesteAministiaIndultoByIdFascicolo(BigDecimal aKey)
			throws F3BException;

	/**
	 * Recupera tutte le annotazioni manuali legate a Richieste con anticipazione 'A' ma non ancora validate
	 *
	 * @param aKey
	 * @throws DAOException
	 */
	public Vector ExRicercaAnnotazioneManualeRichiesteAnticipazioneAministiaIndultoByIdFascicolo(
			BigDecimal aKey) throws F3BException;

	/**
	 * Ricerca tutte le annotazioni manuali con FLAG_APP_PROVVISORIA<>A e R associate al reato validate o meno
	 *
	 * @param aKey
	 *            id del reato
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaAnnotazioniManualiNonRichiesteByIdReato(BigDecimal aKey) throws F3BException;

	/**
	 * Recupero una annotazione associata all'evento. n.b. se ne è presente più di una ne viene recuperata una
	 * a caso
	 *
	 * @param aKeyEvento
	 * @return
	 * @throws F3BException
	 */
	public AnnotazioneManualeModel ExRicercaAnnotazioniManualiByIdEvento(BigDecimal aKeyEvento)
			throws F3BException;

	//
	public AnnotazioneOrdinanzaModel ExRicercaUltimaAnnotazioneManualeOrdinanzaByIdFascicolo(
			BigDecimal aKeyFascicolo) throws F3BException;

	public AnnotazioneOrdinanzaModel ExRicercaUltimaAnnotazioneManualeOrdinanzaByIdFascicolo(
			EventoModel aEvento) throws F3BException;

	/**
	 * Ricerca tutte le annotazioni e i reati associati legati all'ultimo Provvedimento (04) o Richiesta (26)
	 * con codice motivo passato in input. @param aKeyFascicolo id del fascicolo @param aCodMotivo - Se
	 * specificato richerca l'evento con codice motivo passato in input. Se non specificato... @return Vettore
	 * di AnnotazioneReatoModel @throws
	 */
	public Vector ExRicercaUltimeAnnotazioniManualiReatiByIdFascicolo(BigDecimal aKeyFascicolo,
			String aCodMotivo) throws F3BException;

	/**
	 * Ricerca TUTTE le annotazioni manuali legate all'evento indipendentemente dallo stato di validazione e
	 * dal tipo di annotazione
	 *
	 * @param aKey
	 *            = id dell'evento
	 * @return vettore di AnnotazioneManualeModel
	 * @throws F3BException
	 *             se annotazione non trovata o errore
	 */
	public Vector ExRicercaAnnotazioneManualeByIdEvento(BigDecimal aKey) throws F3BException;

	public ByteArrayOutputStream ExStampaDocumentoXAnnotazioni(EventoNotificaModel aEvento,
			UtenteModel aUtenteModel) throws F3BException;

	public EventoModel ExUpdatePeneEspiateSenzaTitolo(EventoModel aEvento, String codicePosizione,
			FascicoloSiepModel aFascicolo) throws F3BException;

	public EventoModel ExUpdateFungibilita(EventoModel aEvento, String codicePosizione,
			FascicoloSiepModel aFascicolo) throws F3BException;

	public Vector ExRicercaAnnotazioneManualeGenerico(AnnotazioneManualeModel aAnnotazioneManuale)
			throws F3BException;

	public Vector ExRicercaRichieste(AnnotazioneManualeModel aAnnotazioneManuale) throws F3BException;

	/**
	 * Inserisce l'annotazione Manuale, l'Evento e le notifiche
	 *
	 * @param AnnotazioneManualeModel
	 *            model dell'annotazione manuale
	 * @param EventoNotificaModel
	 *            model contenente l'evento e le notifiche
	 * @return il model dell'annotazione con valorizzato l'id di inserimento
	 */
	public AnnotazioneManualeModel ExInserisciAnnotazioneManualeEventoNotifica(
			AnnotazioneManualeModel aAnnotazioneManuale, EventoNotificaModel aEventoNotifica)
			throws F3BException;

	public EventoModel ExUpdateRideterminazionePenaAltro(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	/**
	 * Funzione per la validazione diretta del Provvedimento di Rideterminazione Pena - Altro nella nuova
	 * versione 4.0.
	 *
	 * @param aEvento
	 * @param aFascicolo
	 * @param aConn
	 *            = eventuale connessione si cui lavorare. Se null ne crea una sua andando in commiti o
	 *            rollback. Se <> null lavora sulla connessione in input senza committare. Serve per
	 *            consentire la validazione contestuale ai provvedimenti correlati (OE, OECS, OS, CNRP)
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExValidaRideterminazionePenaAltro(EventoModel aEvento, FascicoloSiepModel aFascicolo,
			Connection aConn) throws F3BException;

	/**
	 * Aggiorna il flagComputabile sull'annotazione
	 *
	 * Utilizzata per le decisioni del GE
	 *
	 * @param aIdAnnotazione
	 * @throws F3BException
	 */
	public void ExUpdateAnnotazioneFlagComputabile(BigDecimal aIdAnnotazione) throws F3BException;

	/**
	 * Inserisce l'AnnotazioneManuale nella fase di PresaInCarico
	 *
	 * @param aAnnManuali
	 * @param lConn
	 * @return String
	 * @throws F3BException
	 */
	public String ExInserisciAnnManualiWithoutSequence(ArrayList aAnnManuali, Connection lConn)
			throws F3BException;

	public AnnotazioneOrdinanzaSigeModel ExRicercannotazioneManualeOrdinanzaSigeByIdFascicolo(
			EventoModel aEvento) throws F3BException;

	public AnnotazioneOrdinanzaSigeModel ExRicercannotazioneManualeOrdinanzaSigeByIdAnnMan(
			BigDecimal aIdAnnotazioneManuale) throws F3BException;

	public AnnotazioneManualeModel ExRicercaAnnotazioneManualeByIdSentenza(BigDecimal aIdSentenza)
			throws F3BException;

	public AnnotazioneManualeModel ExRicercaAnnotazioneManualeByIdSentenzaIdTenoreSige(BigDecimal aIdSentenza,
			BigDecimal aIdTenoreSige) throws F3BException;

	public Vector ExRicercaRichiesteTenoriSige(AnnotazioneManualeModel aAnnotazioneManuale)
			throws F3BException;

	public void ExAggiornaFlagSelQuantum(String ids, String flag) throws F3BException;

	/**
	 * Aggiunto metodo di ricerca puntuale
	 * 
	 * @author sgioggi
	 * @since MEV_2023-33
	 * 
	 * @param idEvento
	 * @param idFascicoloSiep
	 * @return AnnotazioneManualeModel
	 */
	public AnnotazioneManualeModel ExRicercaAnnotazioneManualeByIdEventoIdFascicolo(BigDecimal idEvento,
			BigDecimal idFascicoloSiep) throws F3BException;

}