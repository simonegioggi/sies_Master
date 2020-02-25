package siap.siep.penaresidua.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.siep.penaresidua.model.PenaPrecedenteModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: IPenaResidua
 * </p>
 * <p>
 * Description: Classe Controller per PenaResidua
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
public interface IPenaResidua {

	/*****************************************************************************
	 * Effettua l'inserimento della pena residua
	 *
	 * @param aPenaResidua
	 *            - model contenete i dati da inserire
	 * @return model inserito con aggiunta dell'id del record
	 * @throws F3BException
	 */
	public PenaResiduaModel ExInserisciPenaResidua(PenaResiduaModel aPenaResidua) throws F3BException;

	public PenaResiduaModel ExInserisciAggiornaPenaResidua(PenaResiduaModel aPenaResidua) throws F3BException;

	/*****************************************************************************
	 * Se esiste un record PENA_RESIDUA non validato per il fascicolo corrente effettua l'update del record,
	 * altrimenti va in insert
	 */
	public PenaResiduaModel ExInsertOrUpdatePenaResidua(PenaResiduaModel aPenaResidua) throws F3BException;

	public PenaResiduaModel ExModificaPenaResidua(PenaResiduaModel aPenaResidua) throws F3BException;

	public void ExCancellaPenaResidua(PenaResiduaModel aPenaResidua) throws F3BException;

	/**
	 * Cancella tutti i record pena residua non validati associati al fascicolo
	 * 
	 * @param aFascID
	 * @throws F3BException
	 */
	public void ExCancellaPenaResiduaNonValidata(BigDecimal aFascID) throws F3BException;

	/*****************************************************************************
	 * Effettua la ricerca di tutti i record PENA_RESIDUA per il fascicolo specificato nel model (ricerca per
	 * id fascicolo)
	 */
	public Vector ExRicercaPenaResidua(PenaResiduaModel aPenaResidua) throws F3BException;

	public PenaResiduaModel ExRicercaPenaResiduaByKey(BigDecimal aKey) throws F3BException;

	public PenaResiduaModel ExRicercaPenaResiduaByIdEvento(BigDecimal aEvento) // ((((
			throws F3BException;

	/****************************************************************************
	 * Recupera il record PENA_RESIDUA con data inserimento più recente indipendentemente dallo stato (validao
	 * o meno).
	 *
	 * @param aKey
	 *            del Fascicolo SIEP
	 * @return PenaResiduaModel
	 * @throws F3BException
	 */
	public PenaResiduaModel ExRicercaPenaResiduaCorrenteByFascicoloSiep(BigDecimal aKeyFascicolo)
			throws F3BException;

	public PenaResiduaModel ExRicercaPenaResiduaCorrenteFlagPiuMenoByFascicoloSiep(BigDecimal aKeyFascicolo)
			throws F3BException;

	public PenaResiduaModel ExRicercaPenaResiduaUltima(BigDecimal aKey) throws F3BException;

	/*****************************************************************************
	 * Recupera l'ultimo record PENA_RESIDUA <b>VALIDATO</b> per il fascicolo passato in input
	 * 
	 * @param aKey
	 *            - id del fascicolo per cui fare la ricerca
	 * @return PenaResiduaModel
	 * @throws F3BException
	 */
	public PenaResiduaModel ExRicercaPenaResiduaUltimaByDate(BigDecimal aKey) throws F3BException;

	/*****************************************************************************
	 * Ricerca la pena residua NON VALIDATA ('N') inserita più recentemente. (ne può esistere più di una non
	 * validata?)
	 * 
	 * @param aKey
	 * @return pena residua con flagValidato = 'N' più recente
	 * @throws F3BException
	 */
	public PenaResiduaModel ExRicercaPenaResiduaCorrenteByFascicoloSiepFlagValidato(BigDecimal aKey)
			throws F3BException;

	/*****************************************************************************
	 * Restituisce l'ultima pena residua validata (data inserimento più recente)<br>
	 *
	 * @param aIdFascicolo
	 *            - id del fascicolo
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel ExRicercaPenaResiduaUltimaValidata(BigDecimal aIdFascicolo) throws F3BException;

	public PenaResiduaModel ExRicercaPenaResiduaUltimaValidataSospesa(BigDecimal aIdFascicolo)
			throws F3BException;

	public PenaResiduaModel ExRicercaPenaResiduaUltimaPerFascicolo(BigDecimal aKey) throws F3BException;

	/*****************************************************************************
	 * Recupera l'ultimo record PENA RESIDUA inserito indipendentemente dallo stato.
	 *
	 * @param aKey
	 *            - id fascicolo
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel ExRicercaPenaResiduaUltimaByDate_IgnoraValidazione(BigDecimal aKey)
			throws F3BException;

	public PenaResiduaModel ExRicercaPenaResiduaUltimaNonValidataSospesa(BigDecimal aIdFascicolo)
			throws F3BException;

	public PenaResiduaModel ExRicercaPenaResiduaUltimaValidataSospesaInterruzione(BigDecimal aIdFascicolo)
			throws F3BException;

	public String ExInserisciPenaResiduaWithoutSequence(PenaResiduaModel aPenaPresunta, Connection lConn)
			throws F3BException;

	public PenaResiduaModel ExAggiornaPenaResidua(PenaResiduaModel aPenaResidua) throws F3BException;

	/*****************************************************************************
	 * Effettua l'inserimento della pena residua e dello scadenzario. Utilizzata nella pena residua manuale
	 * per fascicolo migrati.
	 *
	 * @param aPenaResidua
	 *            - model contenete i dati da inserire
	 * @param aCodice
	 *            - codice TIPO_SCADENZARIO
	 * @return model inserito con aggiunta dell'id del record
	 * @throws F3BException
	 */
	public PenaResiduaModel ExInserisciPenaResiduaScadenzario(PenaResiduaModel aPenaResidua, String aCodice)
			throws F3BException;

	/*****************************************************************************
	 * Restituisce la PENA_RESIDUA VALIDATA con data inserimento più recente e Data Fine Pena valorizzata (not
	 * null)
	 * 
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	public PenaResiduaModel ExRicercaPenaResiduaUltimaValidataDataFinePena(BigDecimal aIdFascicolo)
			throws F3BException;

	/**
	 * Ricerco la penultima pena Residua Inserita a sistema (data inserimento) validata o meno per il
	 * fascicolo specificato
	 *
	 * @param aFascID
	 * @return PenaPrecedenteModel
	 * @throws F3BException
	 */
	public PenaPrecedenteModel ExRicercaPenaPrecedenteByKeyFascicolo(BigDecimal aFascID) throws F3BException;

	/**
	 * Ricerco la pena Residua Inserita subito prima dell'evento selezionato Questo metodo è indispensabile
	 * per il rework del dettaglio Alcuni provveidmenti tipo MA Prosecuzione nel momento in cui vengono
	 * inseriti fanno riferimento alla pena precedente validata, in seguito l'unico modo per ritrovarla è
	 * cercare la pena relativa all'evento e selezionare quella immediatamente precedente.
	 *
	 * @param aEveID
	 *            - Id dell'evento
	 * @return PenaResiduaModel
	 * @throws F3BException
	 */
	public PenaResiduaModel ExRicercaPenaPrecedenteByKeyEvento(BigDecimal aFascID, BigDecimal aEveID)
			throws F3BException;

	/**
	 * Inserisce i dati della pena Residua Manuale ed eventualmente aggiorna lo scadenzario.
	 * 
	 * @param aEvento
	 * @param aPenaResidua
	 * @param aLibAnt
	 *            null se non presenti
	 * @param aCodice
	 * @return
	 * @throws F3BException
	 */
	// public EventoModel ExInserisciPenaResiduaManuale(EventoModel aEvento,
	// PenaResiduaModel aPenaResidua,
	// LicenzaLibAnticipataModel aLibAnt,
	// BigDecimal ggLibAnt_Ord, BigDecimal ggLibAnt_Spe, BigDecimal ggLibAnt_Int, // 20/05/2014 Nuova L.A.
	// String aCodice)
	// throws F3BException;

	public EventoModel ExInserisciPenaResiduaManuale(EventoModel aEvento, PenaResiduaModel aPenaResidua,
			Vector<LicenzaLibAnticipataModel> aListaLicenze, String aCodTipoScadenzario) throws F3BException;

	public Vector ExRicercaPenaResiduaByIdFascicolo(BigDecimal aIdFascicolo) throws F3BException;

	public String ExInserisciPeneResidueWithoutSequence(ArrayList aPenaResidua, Connection lConn)
			throws F3BException;

}