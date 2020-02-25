package siap.siep.calcolopena.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ICalcoloPenaF5
 * </p>
 * <p>
 * Description: Classe Interfaccia del Controller per Calcolo Pena F5
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
public interface ICalcoloPenaF5 {

	/**
	 * Recupera la Pena Iniziale e valorizza se possibile l'intervallo di date nel quale recuperare gli eventi
	 * che concorrono al calcolo della pena. L'intervallo di date è compreso tra: dataDal = la data
	 * validazione dell'evento a cui è associata la pena iniziale (tranne Pena in sentenza per la quale è
	 * null, non ha senso vanno presi in considerazione tutti i dati) dataAl = data validazione dell'evento in
	 * input (se passato)
	 * 
	 * @param aFascID
	 * @param aIdEvento
	 * @return
	 * @throws F3BException
	 */
	public CalcoloPenaModel exGetPenaIniziale(BigDecimal aFascID, BigDecimal aIdEvento) throws F3BException;

	/**
	 * Recupera i benefici in sentenza
	 * 
	 * @param aBeneficio
	 * @return
	 * @throws F3BException
	 */
	public Vector exGetBenefici(BeneficioModel aBeneficio) throws F3BException;

	/**
	 * Ritorna la Sanzione Sostitutiva Disposta dal Giudice in sentenza se presente
	 * 
	 * @param aPenaComplID
	 *            - Id Della Pena Complessiva
	 * @return la SS o null se non esiste
	 * @throws F3BException
	 */
	public SanzioneSostitutivaModel exGetSanzioneSostitutiva(BigDecimal aPenaComplID) throws F3BException;

	/**
	 * Recupera tutte le Annotazioni Manuali inserite in fase di <b>Richiesta al GE</b> solo quelle con
	 * anticipazione degli effetti. Vengono recuperati:<br>
	 * 
	 * - Depenalizzazione (004)<br>
	 * - Incostituzionalità (013)<br>
	 * - Amnistia (003)<br>
	 * - Indulto (002)<br>
	 *
	 * Per distinguere le Richieste (con o senza anticipazione) dalle Decisioni viene utilizzato il
	 * FlagApprovazioneProvvisoria che nel caso delle Richieste vale R o A mentre nelle decisioni vale '-'
	 * 
	 * Attenzione!! Una richiesta con anticipazione degli effetti resta sempre in gioco anche se interviene
	 * una decisione. In questo caso infatti i quantum da inserire con la decisione sono solo quelli necessari
	 * a correggere la richiesta s ela decisione non è conforma. I dati richiesti con anticipazione vengono
	 * considerati concessi.
	 * 
	 * @param aFascID
	 * @param aDataDal
	 * @param aDataAl
	 * @return Vettore di AnnotazioniManualiModel
	 * @throws F3BException
	 */
	public Vector exGetRichiesteAlGE(BigDecimal aFascID, Date aDataDal, Date aDataAl) throws F3BException;

	/**
	 * Recupera tutte le Annotazioni Manuali inserite con le <b>Decisioni Del GE</b> Vengono recuperati:<br>
	 * 
	 * - Depenalizzazione (004)<br>
	 * - Incostituzionalità (013)<br>
	 * - Amnistia (003)<br>
	 * - Indulto (002)<br>
	 * 
	 * Per distinguere le Richieste (con o senza anticipazione) dalle Decisioni viene utilizzato il
	 * FlagApprovazioneProvvisoria che nel caso delle Richieste vale R o A mentre nelle decisioni vale '-'
	 * 
	 * @param aFascID
	 * @param aDataDal
	 * @param aDataAl
	 * @return
	 * @throws F3BException
	 */
	public Vector exGetDecisioniDelGE(BigDecimal aFascID, Date aDataDal, Date aDataAl) throws F3BException;

	/**
	 * Recupera tutte le Annotazioni Manuali inserite con gli <b>Indulti Migrati RES</b> Vengono
	 * recuperati:<br>
	 * 
	 * - richieste con anticipazione (evento 0161 tipo annotazione 002, flag app_provv = '-') - decisioni
	 * (evento 0294 tipo annotazione 002 flag app_provv = '-')<br>
	 * 
	 * n.b. vengono recuperati solo i dati iscritti res in ordine cronologico dal più recente
	 * 
	 * @param aFascID
	 * @param aDataDal
	 * @param aDataAl
	 * @return
	 * @throws F3BException
	 */
	public Vector exGetIndultiRES(BigDecimal aFascID, Date aDataDal, Date aDataAl) throws F3BException;

	/**
	 * Recupera i computi iscritti e validati nell'intervallo di date specificato: - Presofferto Altro Reato
	 * (005-Pena Espiata per lo Stesso Titolo) - Fungibilità altro Reato - Misura Cautelare (006-Pena Espiata
	 * per Altro Titolo) - Pena Detentiva (007-Pena Espiata Senza Titolo) - Altro (014-Altro)
	 * 
	 * n.b. la data validazione coincide con la data di aggiornamento
	 * 
	 * @param aFascID
	 * @param aDataDal
	 * @param aDataAl
	 * @return
	 * @throws F3BException
	 */
	public Vector exGetComputi(BigDecimal aFascID, Date aDataDal, Date aDataAl) throws F3BException;

	/**
	 * Recupera le LA iscritte nell'intervallo di date specificato: n.b. le LA computabili sono solo quelle
	 * collegate a un evento SIEP, vale: - a dire a una Comunicazione (12) nel caso in cui la LA sia stata
	 * acquisita per in condannato libero - a un ordine di scarcerazione per rideterminazione pena, nel caso
	 * in cui le LA sono state concesse a un condannato detenuto, per cui sono state utilizzate per anticipare
	 * il fine pena - a un cumulo - a una pena residua manuale - Liberazione Manuale Anticipata ????
	 * 
	 * @param aFascID
	 * @param aDataDal
	 * @param aDataAl
	 * @return Vettore di LicenzaLibAnticipataModel
	 * @throws F3BException
	 */
	public Vector exGetLiberazioneAnticipata(BigDecimal aFascID, Date aDataDal, Date aDataAl)
			throws F3BException;

	/**
	 * Restituisce un vettore di SospensioneModel contenente tutte le sospensioni inserite e VALIDATE nel
	 * periodo specificato @param aFascID id del Fascicolo @param aDataDal data dal @param aDataAl data
	 * al @return vettore di SospensioneModel @throws
	 */
	public Vector exGetPeneEspiate(BigDecimal aFascID, Date aDataDal, Date aDataAl) throws F3BException;

	/**
	 * Restituisce un vettore di FungibilitaModel contenente tutte le pene espiate in eccesso inserite e
	 * VALIDATE nel periodo specificato @param aFascID id del Fascicolo @param aDataDal data dal @param
	 * aDataAl data al @return vettore di FungibilitaModel @throws
	 */
	public Vector exGetPeneEspiateInEccesso(BigDecimal aFascID, Date aDataDal, Date aDataAl)
			throws F3BException;

	/**
	 * Recupera tutte le annotazioni manuali inserite su un certo fascicolo applicando i filtri specificati in
	 * input. Le Annotazioni sono quelle utilizzabili ai fini del calcolo della pena Richieste al GE (con
	 * anticipazione) aFlagAppProvvisoria = 'A' - Depenalizzazione (004)<br>
	 * - Incostituzionalità (013)<br>
	 * - Amnistia/Indulto (003/002)<br>
	 * 
	 * Decisioni del GE - Depenalizzazione (004)<br>
	 * - Incostituzionalità (013)<br>
	 * - Amnistia/Indulto (003/002)<br>
	 * 
	 * Computi - Presofferto Altro Reato (005-Pena Espiata per lo Stesso Titolo) - Fungibilità altro Reato -
	 * Misura Cautelare (006-Pena Espiata per Altro Titolo) - Pena Detentiva (007-Pena Espiata Senza Titolo) -
	 * Altro (014-Altro)
	 *
	 * @param aFascID
	 * @param aCodTipoAnnotazione
	 * @param aFlagAppProvvisoria
	 *            ('A' = richieste al GE, '-' altro)
	 * @param aFlagConcesseRevocate
	 *            ('+' = revocate, '-' = concesse) per ora non usato
	 * 
	 * @return
	 * @throws F3BException
	 */
	public Vector exGetAnnotazioniManualiDaComputare(BigDecimal aFascID, String aCodTipoAnnotazione,
			String aFlagAppProvvisoria, String aFlagConcesseRevocate) throws F3BException;

	/**
	 * Restituisce l'elenco di tutte le pene residue VALIDATE per il fascicolo ordinate per data inserimento
	 * decrescente ma inserite prima della data passata in input
	 * 
	 * @param aFascID
	 * @param aDataIns
	 * @return
	 * @throws F3BException
	 */
	public Vector getElencoPeneResidueDataInsDesc(BigDecimal aFascID, Date aDataIns) throws F3BException;

	/**
	 * Restituisce la data di scarcerazione legata a una richiesta di Indulto con anticipazione degli effetti.
	 * n.b. la data di scarcerazione viene ricostruita in quanto non viene salvata in alcun modo sul DB. Tale
	 * data viene utilizzata per i calcoli della fungibilità e della pena espiata nel caso di Amnistia/Indulto
	 * (richieste e decisioni).
	 * 
	 * @return la presunta data di scarcerazione o null se non presente o non determinabile
	 */
	public Date getDataScarcerazione(BigDecimal aFascID) throws F3BException;

	/**
	 * Restituisce l'elenco delle richieste con o senza anticipazione collegate a una decisione
	 * 
	 * @param aIdDecisione
	 *            id dell'annotazione che rappresenta la decisione
	 * @return vettore di AnnotazioneManualeModel
	 * @throws F3BException
	 */
	public Vector exGetRichiesteAlGEbyIdDecisione(BigDecimal aIdDecisione) throws F3BException;

	public Vector exGetFascicoliPerCheckPena(String aChiaveUfficio, String aIscritto, String aProgrAnno)
			throws F3BException;

	// public Vector exGetEventiPerCalcoloPena (BigDecimal aIdFascicoloSiep) throws F3BException;

	/**
	 * Verifica se l'Evento passato è di tipo interrutivo
	 * 
	 * @param aEvento
	 *            - Evento da verificare
	 * @return boolean - Restituisce vero se è un evento di tipo interruttivo
	 */
	public boolean isInterruzionePerStatoEsecuzione(EventoModel aEveModel) throws F3BException;

	public EventoModel exRicercaUltimoEvento(BigDecimal aFascID) throws F3BException;

}