package siap.siep.cumulo.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: IController
 * </p>
 * <p>
 * Description: Interfaccia della Classe Controller per Cumulo
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
public interface ICumulo {
	/**
	 * Funzione di inserimento dei dati dei fascicoli cumulati: cumulo, fascicolo, sentenza
	 *
	 * @param aCumulo
	 *            - Model contenete i riferimenti al titolo esecutivo da cumulate (Fascicolo - Sentenza)
	 * @param aFasMod
	 *            - Solo se CUMULATO di altra BDI contiene i dati del fascicolo da iscrivere
	 * @param aSenMod
	 *            - Solo se CUMULATO di altra BDI contiene i dati della Sentenza/Decreto/Cumulo
	 * @return il model del CUMULO inserito
	 * @throws F3BException
	 */
	public CumuloModel ExInserisciCumulo(CumuloModel aCumulo, FascicoloSiepModel aFasMod, SentenzaModel aSenMod,
			BigDecimal IdFas0) throws F3BException;

	/**
	 * Ricerca tutti i record CUMULO legati a fascicoli con FLAG_VALIDATO in (S,N null) ordinati per data inserimento
	 * asc (dal meno recente)
	 * 
	 * @param aCumulo
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaCumulo(CumuloModel aCumulo) throws F3BException;

	/**
	 * Ricerca tutti i record CUMULO legati a fascicoli sige tramite SEN_ID_SENTENZA_CUMULO con FLAG_VALIDATO
	 * in (S,N null) ordinati per data inserimento asc (dal meno recente)
	 * 
	 * @param aCumulo
	 * @return
	 * @throws F3BException
	 */
	public Vector ricercaCumuloSentenzaFascicoloSige(CumuloModel aCumulo) throws F3BException;

	/**
	 * Effettua la ricerca del cumulo per idCumulo
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public CumuloModel ExRicercaCumuloByKey(BigDecimal aKey) throws F3BException;

	public CumuloModel ExModificaCumulo(CumuloModel aCumulo) throws F3BException;

	public void ExCancellaCumulo(CumuloModel aCumulo) throws F3BException;

	/**
	 * Recupera i Fascicoli cumulati o in istruttoria cumulo legati al cumulante specificato in input. Recupera anche le
	 * sentenze associate.
	 *
	 * @param lFascID
	 *            - id Fascicoli cumulante
	 * @return Vector di FascicoloSiepModel con i fascicoli cumulati.
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoliCumulatiByIDFascicoloSiep(BigDecimal lFascID) throws F3BException;

	public void ExAggiornaCumuloFasc(CumuloModel aCumulo) throws F3BException;

	/**
	 * Effettua la validazione di un Provvedimento di Cumulo:<br>
	 * - aggiorna il flag cumulante sul fascicolo corrente<br>
	 * - aggiorna il flag cumulato sui fascicoli coinvolti nel cumulo<br>
	 * - valida il record CUMULO<br>
	 * - valida il record pena residua<br>
	 * - aggiorna le LA a S<br>
	 *
	 * @param aEvento
	 *            - Provvedimento di Cumulo da Validare
	 * @param aFascicolo
	 *            - fascicolo SIEP Cumulante
	 * @return EventoModel -
	 */
	public EventoModel ExUpdateValidaCumulo(EventoModel aEvento, FascicoloSiepModel aFascicolo) throws F3BException;

	public ByteArrayOutputStream ExStampaDocumentoXCumulo(EventoNotificaModel aEvento, UtenteModel aUtente)
			throws F3BException;

	/**
	 *
	 * @param aPenaCumulo
	 * @param aCumulo
	 * @param alibAntMod
	 * @param aEsisteGiorniLib
	 * @return
	 * @throws F3BException
	 */
	public PenaCumuloModel ExInserisciCumuloPenaCumuloLibAnt(PenaCumuloModel aPenaCumulo, CumuloModel aCumulo,
			LicenzaLibAnticipataModel alibAntMod, boolean aEsisteGiorniLib) throws F3BException;

	/**
	 * Ricerca i record CUMULO <b>non validati</b> collegati al fascicolo specificato. Ordinati per data inserimento
	 * ascendente (<b>dal meno recente</b>)
	 *
	 * @param aKey
	 *            - Id del fascicolo cumulante
	 * @return Vector - di CumuloModel con i CUMULI non validati
	 * @throws F3BException
	 */
	public Vector ExRicercaFascicoliCumulobyIdFascicoloSiepFlagValidato(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaFascicoliCumulobyIdFascicoloSiepValidatoDataCumuloNotNull(BigDecimal aKey)
			throws F3BException;

	public CumuloModel ExRicercaCumuloByEveIdEvento(BigDecimal aKey) throws F3BException;

	public String ExInserisciCumuloWithoutSequence(ArrayList aCumuli, Connection lConn) throws F3BException;

	/**
	 * Effettua la ricerca di tutti i record CUMULO associati ad una certa istruttoria
	 * 
	 * @param aIdIstruttoria
	 *            - Id Dell'istruttoria
	 * @return vettore di CumuloModel
	 * @throws F3BException
	 */
	public Vector ExRicercaCumuloByIstruttoria(BigDecimal aIdIstruttoria) throws F3BException;

	/**
	 * Effettua la Modifica di un Provvedimento di Cumulo:<br>
	 * - setta il flag Validato sul Valore "N" <br>
	 * - Per escluderlo dai titoli su cui si può lavorare<br>
	 *
	 * @param aEvento
	 *            - Provvedimento di Cumulo da Validare
	 * @param aFascicolo
	 *            - fascicolo SIEP Cumulante
	 * @return EventoModel -
	 */
	// public CumuloModel ExUpdateEscludiTitolo(BigDecimal aIdCumulo)
	// throws F3BException;

	public void ExCancellaFascCumulato(CumuloModel aCumulo) throws F3BException;

	public void ExCancellaFascCumulatoUnico(CumuloModel aCumulo, String aCodUtenteConnesso,
			String aCodUfficioUtenteConnesso) throws F3BException;

	public void ExCancellaFascCumulatoPrimoCumulo(CumuloModel aCumuloDaEliminare, CumuloModel aCumulo,
			PenaCumuloModel aPenaCumulo, Vector aUltSanzCumulo) throws F3BException;

	public CumuloModel ExRicercaCumuloByIdSentenza(BigDecimal aKey, String flagValidato) throws F3BException;

	public Vector ExRicercaEventoCumulo(BigDecimal aIdFascicoloSiep) throws F3BException;

}
