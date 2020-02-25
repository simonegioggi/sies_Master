package siap.siep.calcolopena.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.utente.model.DatiOperazioneModel;
import siap.siep.calcolopena.model.CalcoloPenaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.fungibilita.model.FungibilitaModel;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ICalcoloPena
 * </p>
 * <p>
 * Description: Classe Interfaccia del Controller per Calcolo Pena
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
public interface ICalcoloPena {

	/*****************************************************************************
	 * Effettua la ricerca dei benefici Concessi per Reclusione per il fascicolo corrente. Somma i valori e
	 * restituisce il totale in un CalendarModel
	 *
	 * @param BigDecimal
	 *            - id del fascicolo
	 * @return CalendarModel - somma dei benefici
	 */
	public CalendarModel exGetBeneficiConcessiReclusione(BigDecimal lFascID) throws F3BException;

	/*****************************************************************************
	 * Effettua la ricerca dei benefici Concessi per Arresto per il fascicolo corrente. Somma i valori e
	 * restituisce il totale in un CalendarModel
	 *
	 * @param BigDecimal
	 *            - id del fascicolo
	 * @return CalendarModel - somma dei benefici
	 * @throws F3BException
	 */
	public CalendarModel exGetBeneficiConcessiArresto(BigDecimal lFascID) throws F3BException;

	/*****************************************************************************
	 * Effettua la ricerca dei benefici Revocati per Reclusione per il fascicolo corrente. Somma i valori e
	 * restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - id del fascicolo
	 * @return CalendarModel - somma dei benefici
	 * @throws F3BException
	 */
	public CalendarModel exGetBeneficiRevocatiReclusione(BigDecimal lFascID) throws F3BException;

	/*****************************************************************************
	 * Effettua la ricerca dei benefici Revocati per Arresto per il fascicolo corrente. Somma i valori e
	 * restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - id del fascicolo
	 * @return CalendarModel - somma dei benefici
	 * @throws F3BException
	 */
	public CalendarModel exGetBeneficiRevocatiArresto(BigDecimal lFascID) throws F3BException;

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari computabili ai fini della Reclusione per il fascicolo
	 * corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariComputabiliReclusione(BigDecimal lFascID) throws F3BException;

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari computabili ai fini dell' Arresto per il fascicolo corrente.
	 * Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariComputabiliArresto(BigDecimal lFascID) throws F3BException;

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari NON computabili ai fini della Reclusione per il fascicolo
	 * corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariNONComputabiliReclusione(BigDecimal lFascID) throws F3BException;

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari NON computabili ai fini dell' Arresto per il fascicolo
	 * corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariNONComputabiliArresto(BigDecimal lFascID) throws F3BException;

	/*****************************************************************************
	 * Effettua la ricerca dei periodi di misura cautelare Fungibili. Somma i valori e restituisce il totale
	 * in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma dei periodi Fungibili
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetFungibilita(BigDecimal lFascID) throws F3BException;

	/*****************************************************************************
	 * Metodo che calcola il solo QUANTUM di pena residua scorporando dalla pena complessiva a sistema (in
	 * sentenza) le parti dovute ai Benefici e alle Misure Cautelari.<br>
	 * Questo metodo va in update del record PENA_RESIDUA con flag validato = N se presente, altrimenti va in
	 * insert.<br>
	 * <br>
	 *
	 * n.b. il metodo non agisce sulle date (inizio, fine..) ma solo su gg, mm, aa e importi (multa e ammenda)
	 *
	 * @param lFascID
	 *            - id fascicolo
	 * @param lPenMod
	 *            - model pena complessiva punto di partenza per il calcolo
	 * @param lBenConcessiReclusione
	 *            - Tot Benefici concessi reclusione
	 * @param lBenRevocatiReclusione
	 *            - Tot Benefici revocati reclusione
	 * @param lBenConcessiArresto
	 *            - Tot Benefici concessi Arresto
	 * @param lBenRevocatiArresto
	 *            - Tot Benefici revocati Arresto
	 * @param lMCTotRec
	 *            - Tot Misure Cautelari Reclusione
	 * @param lMCTotArr
	 *            - Tot Misure Cautelari Arresto
	 * @param CodOperatore
	 * @param CodUffOperatore
	 * @return Model della PENA_RESIDUA aggiornata o inserita
	 * @throws F3BException
	 */
	public PenaResiduaModel exCalcolaQuantumPenaComplessivaIniziale(BigDecimal lFascID,
			PenaComplessivaModel lPenMod, CalendarModel lBenConcessiReclusione,
			CalendarModel lBenRevocatiReclusione, CalendarModel lBenConcessiArresto,
			CalendarModel lBenRevocatiArresto, CalendarModel lMCTotRec, CalendarModel lMCTotArr,
			String CodOperatore, String CodUffOperatore) throws F3BException;

	/*****************************************************************************
	 * Metodo che calcola la pena residua scorporando dalla pena complessiva a sistema (in sentenza o residua)
	 * le parti dovute ai benefici e alle misure cautelari.<br>
	 * Questo metodo va in update del record PENA_RESIDUA con flag validato = N se presente, altrimenti va in
	 * insert.<br>
	 * <br>
	 * Se ForzaFungibilita = true, prima di inserire la pena residua calcolata, verifica se <>0 e > della pena
	 * già espiata. In questo caso non inserisce la pena residua calcolata bensì la differenza tra questo
	 * valore e la pena già espiata. Altrimenti non inserisce nulla.
	 *
	 * n.b. Il metodo non viene mai chiamato con ForzaFungibilita = true
	 *
	 * n.b. il metodo non agisce sulle date (inizio, fine..) ma solo su gg, mm, aa e importi (multa e ammenda)
	 * 
	 * @param lFascID
	 * @param lPenMod
	 *            - Pena (Quantum + importi) utilizzata come punto di partenza per i calcoli
	 * @param lBenConcessiReclusione
	 * @param lBenRevocatiReclusione
	 * @param lBenConcessiArresto
	 * @param lBenRevocatiArresto
	 * @param lMCTotRec
	 * @param lMCTotArr
	 * @param CodOperatore
	 * @param CodUffOperatore
	 * @param ForzaFungibilita
	 *            -
	 * @param lPenaGiaEspiata
	 *            - Usato solo se ForzaFungibilita = true
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel exCalcolaQuantumPenaComplessivaNuovo(BigDecimal lFascID,
			PenaComplessivaModel lPenMod, CalendarModel lBenConcessiReclusione,
			CalendarModel lBenRevocatiReclusione, CalendarModel lBenConcessiArresto,
			CalendarModel lBenRevocatiArresto, CalendarModel lMCTotRec, CalendarModel lMCTotArr,
			String CodOperatore, String CodUffOperatore, boolean ForzaFungibilita,
			CalendarModel lPenaGiaEspiata) throws F3BException;

	/*****************************************************************************
	 * Calcola la data fine pena a partire dalla data inizio e dalla durata della pena.<br>
	 * Restituisce un vettore di Date contenente 0,1 o 2 record:<br>
	 * - 0 record se il quantum di reclusione o arresti risulta non positivo (minore o uguale a 0)<br>
	 * - 1 record = data fine pena se è previsto un solo periodo (Reclusione o Arresti)<br>
	 * - 2 record: se previsto sia Reclusione che Arresti con:<br>
	 * -- record 1 = DataFineReclusione <br>
	 * -- record 2 = DataFinePena (Reclusione+Arresti)<br>
	 *
	 * @param aDataInizio
	 * @param aPenResMod
	 * @param diesaquo
	 *            - indica se calcolare nel computo anche la datainizio
	 * @return
	 * @throws F3BException
	 */
	public Vector exCalcolaDataFinePena(Date aDataInizio, PenaResiduaModel aPenResMod, boolean diesaquo)
			throws F3BException;

	/*****************************************************************************
	 * Calcola la data fine a partire dalla data inizio e dalla durata. <br>
	 * La data inizio viene passata come CalendarModel e specificata nei campi mGG, mMM, mAA, <b>NON nel campo
	 * DataInizio</b><br>
	 * Il quantum <b>deve essere normalizzato</b> altrimenti il risultato finale potrebbe non essere corretto
	 * 
	 * @param aDataInizio
	 *            - CalendarModel con data inizio specificata come mGG, mMM, mAA non vengono presi in
	 *            considerazione gli altri campi del model (DataInizio e DataFine)
	 * @param aDurata
	 *            - specificata come mGG, mMM e mAA
	 * @param diesaquo
	 *            - se true viene considerato anche il gg data inizio come facente parte del periodo, se false
	 *            no.<br>
	 *            ES: data inizio 10/04/2006, durata 6gg<br>
	 *            se diesaquo = true allora data fine 15/04/2006 (10,11,12,13,14,15)<br>
	 *            se diesaquo = false allora data fine 16/04/2006 (11,12,13,14,15,16)<br>
	 *
	 * @return data fine calcolata
	 * @throws F3BException
	 */
	public Date exCalcolaNuovaDataFine(CalendarModel aDataInizio, CalendarModel aDurata, boolean diesaquo)
			throws F3BException;

	/*****************************************************************************
	 * Calcola la data fine a partire dalla data inizio e dalla durata. <br>
	 * Il quantum <b>deve essere normalizzato</b> altrimenti il risultato finale potrebbe non essere corretto
	 * 
	 * @param aDataInizio
	 *            -
	 * @param aDurata
	 *            - specificata come mGG, mMM e mAA
	 * @param diesaquo
	 *            - se true viene considerato anche il gg data inizio come facente parte del periodo <br>
	 *            ES: data inizio 10/04/2006, durata 6gg<br>
	 *            se diesaquo = true allora data fine 15/04/2006 (10,11,12,13,14,15)<br>
	 *            se diesaquo = false allora data fine 16/04/2006 (11,12,13,14,15,16)<br>
	 *
	 * @return data fine calcolata
	 * @throws F3BException
	 */
	public Date exCalcolaNuovaDataFine(Date aDataInizio, CalendarModel aDurata, boolean diesaquo)
			throws F3BException;

	/*****************************************************************************
	 *
	 * @param lFascID
	 * @param aCodTipoAnnotazione
	 * @param isAbInitio
	 * @return
	 * @throws F3BException
	 */
	public CalendarModel exGetAnnotazioniManualiConcessiReclusione(BigDecimal lFascID,
			String aCodTipoAnnotazione, boolean isAbInitio) throws F3BException;

	public CalendarModel exGetAnnotazioniManualiConcessiAnticipazioneReclusione(BigDecimal lFascID,
			String aCodTipoAnnotazione, boolean isAbInitio) throws F3BException;

	public CalendarModel exGetAnnotazioniManualiConcessiArresto(BigDecimal lFascID,
			String aCodTipoAnnotazione, boolean isAbInitio) throws F3BException;

	/**
	 * Recupera il quantum totale ARRESTO delle annotazioni manuali concesse e Richieste con anticipazione
	 * degli effetti. Se isAbInitio = true, recupera tutte le annotazioni Se isAbInitio = false, recupera solo
	 * quelle non validata. Le altre sono già stete computate nella pena residua validata da cui partiranno i
	 * calcoli
	 * 
	 * @param lFascID
	 * @param aCodTipoAnnotazione
	 * @param aCodTipoAnnotazione
	 * @return
	 * @throws F3BException
	 */
	public CalendarModel exGetAnnotazioniManualiConcessiAnticipazioneArresto(BigDecimal lFascID,
			String aCodTipoAnnotazione, boolean isAbInitio) throws F3BException;

	public CalendarModel exGetAnnotazioniManualiRevocatiReclusione(BigDecimal lFascID,
			String aCodTipoAnnotazione, boolean isAbInitio) throws F3BException;

	public CalendarModel exGetAnnotazioniManualiRevocatiAnticipazioneReclusione(BigDecimal lFascID,
			String aCodTipoAnnotazione, boolean isAbInitio) throws F3BException;

	public CalendarModel exGetAnnotazioniManualiRevocatiArresto(BigDecimal lFascID,
			String aCodTipoAnnotazione, boolean isAbInitio) throws F3BException;

	public CalendarModel exGetAnnotazioniManualiRevocatiAnticipazioneArresto(BigDecimal lFascID,
			String aCodTipoAnnotazione, boolean isAbInitio) throws F3BException;

	public CalcoloPenaModel exCalcoloRevocheMisureAlternative(BigDecimal lFascID, Date aDataDAL,
			CalendarModel aQuantumRevocato, CalendarModel aQuantumRevocatoArresto, String a30giorni,
			int aStatoDetenuto, Date aNuovoInizioPena, String aDetenuto, CalcoloPenaModel aCalcoloPenaModel)
			throws F3BException;

	/*****************************************************************************
	 * Aggiorna/Inserisce la data fine pena della pena residua sottraendo i giorni di liberazione dalla data
	 * fine a sistema. Vengono aggiornate anche le date intermedie di Reclusione/Arresto. Non aggiorna il
	 * quantum. La sottrazione viene fatta in modo 'esatto' (data fine - giorni) senza le approssimazioni
	 * legate ai quantum.<br>
	 * <br>
	 *
	 * Se data fine ricalcolata minore data inserimento viene calcolata e inserita la fungibilità.<br>
	 * <br>
	 *
	 * Aggiorna il flag dei record Liberazione Anticipata presi in considerazione nel calcolo
	 * 
	 * n.b. dal 10/2014 aggiunta anche la gestione dei giorni concessi con DL92 i Rimedi Risarcitori
	 *
	 * @param aCalcoloPenaModel
	 * @param aFascId
	 * @param aIdEventoOrdinanza
	 * @param aDatiOperazione
	 * @param aTipoLicenza
	 *            = RD per RImediRisarcitori, null negli altri casi
	 * @return
	 * @throws F3BException
	 */
	public CalcoloPenaModel exCalcoloLiberazioneAnticipata(CalcoloPenaModel aCalcoloPenaModel,
			BigDecimal aFascId, BigDecimal aIdEventoOrdinanza, Date aDataSistemaPerCalcoli,
			Date aDataFinePena, String aTipoLicenza, DatiOperazioneModel aDatiOperazione) throws F3BException;

	/*****************************************************************************
	 * Calcola la data fine pena della pena residua sottraendo i giorni di liberazione dalla data fine a
	 * l'ultima pena residua VALIDATA presente sul DB.
	 * 
	 * @param aTotLA
	 * @param aFascId
	 * @return Data Fine ricalcolata
	 * @throws F3BException
	 ****************************************************************************/
	public Date exCalcoloLiberazioneAnticipataSuDataFineUltimaPena(int aTotLA, BigDecimal aFascId)
			throws F3BException;

	/*****************************************************************************
	 * Questo metodo verifica se, per il fascicolo passato in input, va effettuato il calcolo abInizio. In
	 * questo caso, per il calcolo della pena, si partirà dalla pena complessiva in sentenza e si terrà conto
	 * di tutti gli 'eventi' che comportano una modifica della pena complessiva da espiare (benefici, misure
	 * cautelari, annotazioni manuali....). In caso contrario il calcolo verra effettuato partendo dall'ultima
	 * pena residua validata, e aggiungendo solamente l'ultima annotazione manuale inserita a sistema e non
	 * ancora validata. In questo modo si evita di effettuare ogni volta il calcolo dall'inizio. <br>
	 * <b>Il calcolo NON è abInizio se:</b> <br>
	 * - esiste una sospensione <br>
	 * - esiste una liberazione anticipata <br>
	 * - esiste un cumulo <br>
	 * - esiste una misura alternativa <br>
	 * - fascicolo migrato da RES
	 *
	 *
	 * @param aIdFascicoloSiep
	 * @return true se il calcolo va effettuato ab inizio, false altrimenti
	 * @throws F3BException
	 */
	public boolean ExIsCalcoloPenaAbInizio(BigDecimal aIdFascicoloSiep) throws F3BException;

	public PenaResiduaModel exCalcolaPenaResiduaAl(PenaResiduaModel aPenaResidua, Date aDataComputo)
			throws Exception;

	public PenaResiduaModel exCalcolaPenaEspiataAl(PenaResiduaModel aPenaResidua, Date aDataComputo)
			throws Exception;

	/**
	 * Effettua l'inserimento delle comunicazione nuovo residuo pena nel caso di Ridetermiazione Pena - ALtro
	 * Duplica la pena rideterminata sul provvedimento di computo.
	 * 
	 * @param aEveNotModel
	 * @return Model Inserito
	 * @since 4.0
	 * @throws F3BException
	 */
	public EventoModel ExInserisciCOMRidetPenaAltro(EventoNotificaModel aEveNotModel) throws F3BException;

	/**
	 * Metodo che effettua la validazione della Comunicazione nuovo residuo pena a seguito di rideterminazione
	 * pena altro ed eventuale validazione del computo
	 * 
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaComNuovoResPenaRidetPenaAltro(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException;

	/**
	 * Effettua l'inserimento dell'Ordine di Scarcerazione nuovo residuo pena nel caso di Ridetermiazione Pena
	 * - ALtro Duplica la pena rideterminata sul provvedimento di computo.
	 * 
	 * @param aEveNotModel
	 * @return Model Inserito
	 * @since 4.0
	 * @throws F3BException
	 */
	public EventoModel ExInserisciOSRidetPenaAltro(EventoNotificaModel aEveNotModel) throws F3BException;

	/**
	 * Metodo che effettua la validazione della Rideterminazione Pena nuovo residuo pena a seguito di
	 * rideterminazione pena altro ed eventuale validazione del computo
	 * 
	 * @param aEvento
	 * @param aFascicolo
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaOSNuovoResPenaRidetPenaAltro(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException;

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari computabili ai fini della Misura di Sicurezza Applicata in
	 * via Provvisoria per il fascicolo corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariComputabiliMisSicApplicata(BigDecimal lFascID)
			throws F3BException;

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari computabili ai fini degli Arresti Domiciliari per il
	 * fascicolo corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariComputabiliArrestiDomiciliari(BigDecimal lFascID)
			throws F3BException;

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari computabili ai fini della Permanenza in Casa per il
	 * fascicolo corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariComputabiliPermanenzaInCasa(BigDecimal lFascID)
			throws F3BException;

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari computabili ai fini del Collocamento in Comunità per il
	 * fascicolo corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariComputabiliCollocamentoInComunita(BigDecimal lFascID)
			throws F3BException;

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari computabili ai fini della Camera di Sicurezza per il
	 * fascicolo corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariComputabiliCameraDiSicurezza(BigDecimal lFascID)
			throws F3BException;

	/*****************************************************************************
	 * Effettua la ricerca delle Misure Cautelari computabili ai fini del Periodo di Messa alla Prova per il
	 * fascicolo corrente. Somma i valori e restituisce il totale in un CalendarModel
	 *
	 * @param lFascID
	 *            - Id del fascicolo
	 * @return CalendarModel - somma delle Misure Cautelari
	 * @throws F3BException
	 ****************************************************************************/
	public CalendarModel exGetMisureCautelariComputabiliPeriodoMessaAllaProva(BigDecimal lFascID)
			throws F3BException;

	public void ExInserisciAggiornaPenaResiduaFungibilita(PenaResiduaModel aPenaResidua,
			FungibilitaModel aFungModel) throws F3BException;

}