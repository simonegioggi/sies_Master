package siap.siep.avvocato.controller;

/**
 * <p>Title: AvvocatoController</p>
 * <p>Description: Classe Controller per Avvocato</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IAvvocato {

	// ============================================================================
	// FUNZIONI CHE LAVORANO SULLA SOLA TABELLA AVVOCATO ED UTILIZZATE PER:
	// INSERIMENTO, MODIFICA, CANCELLAZIONE, RICERCA
	// ============================================================================

	public BigDecimal ExInserisciAvvocato(AvvocatoModel aAvvocato, AvvocatoFascicoloSiepModel aAvvFascMod)
			throws F3BException;

	public BigDecimal ExInserisciAvvocato(EventoNotificaModel lEveNot, AvvocatoModel aAvvocato,
			AvvocatoFascicoloSiepModel aAvvFascMod) throws F3BException;

	public Vector ExRicercaAvvocato(AvvocatoModel aAvvocato, AvvocatoFascicoloSiepModel aAvvFascMod)
			throws F3BException;

	// ============================================================================
	// FUNZIONI CHE LAVORANO SULLA TABELLA DI RELAZIONE AVVOCATO_FASCICOLO_SIEP
	// E CHE CONSENTONO DI: INSERIRE, MODIFICARE, DEASSEGNARE, SOSTITUIRE
	// UN AVVOCATO RELATIVAMENTE A UN FASCICOLO
	// ============================================================================
	/*****************************************************************************
	 * Ricerca gli avvocati ATTUALMENTE assegnati al fascicolo in input la condizione è per id avvocato o nome
	 *
	 * @param aAvvocato
	 * @param aAvvFascMod
	 * @return vettore di AvvocatoModel (tab AVVOCATO + mDataInizioValidita + tipo)
	 * @throws F3BException
	 */
	public Vector ExRicercaAvvocatiAttualiFascicolo(AvvocatoModel aAvvocato,
			AvvocatoFascicoloSiepModel aAvvFascMod) throws F3BException;

	public List ExRicercaStoricoAvvocatiFascicolo(AvvocatoModel aAvvocato,
			AvvocatoFascicoloSiepModel aAvvFascMod) throws F3BException;

	public Vector ExRicercaAvvocato(AvvocatoModel aAvvocato) throws F3BException;

	public AvvocatoModel ExModificaAvvocato(AvvocatoModel aAvvocato) throws F3BException;

	public void ExCancellaAvvocato(AvvocatoModel aAvvocato) throws F3BException;

	/*****************************************************************************
	 * Recupera l'elenco degli avvocati ATTUALMENTE assegnati a al fascicolo specificato in input
	 * 
	 * @param aKey
	 *            - id del fascicolo
	 * @return vettore di AvvocatoModel
	 * @throws F3BException
	 *             - se avvocati non trovati o altro errore
	 */
	public Vector ExRicercaAvvocatiByFascicolo(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaAvvocatiByFascicoloNoError(BigDecimal aKey) throws F3BException;

	public AvvocatoSiepModel ExRicercaAvvocatoByKeyAvvocatoFasSiep(BigDecimal aKey) throws F3BException;

	/**
	 * 
	 * @param aAvvocato
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaAvvocatoPerInserimento(AvvocatoModel aAvvocato) throws F3BException;

	public AvvocatoModel ExInserisciAvvocato(AvvocatoModel aAvvocato) throws F3BException;

	public Vector ExRicercaAvvocatoPaged(AvvocatoModel aAvvocato, int aPage) throws F3BException;

	public BigDecimal ExGetCountAvvocati(AvvocatoModel aAvvocato) throws F3BException;

	public AvvocatoModel ExModificaStoricizzaAvvocato(AvvocatoModel aAvvocato, StoricoAvvocatoModel aStorico)
			throws F3BException;

	/*****************************************************************************
	 * 
	 * @param aKey
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaAvvocatoFascicoloSiepByKeyAvvocato(BigDecimal aKey) throws F3BException;

	public AvvocatoModel ExRicercaAvvocatoByKey(BigDecimal aKey) throws F3BException;

	/*****************************************************************************
	 * Recupera i dati dell'avvocato specificato sul fascicolo specificato
	 * 
	 * @param aIdAvvocato
	 * @param aIdFascicolo
	 * @return AvvocatoSiepModel
	 * @throws F3BException
	 */
	public AvvocatoSiepModel ExRicercaAvvocatoFascicoloSiepByIdAvvocatoIdFascicolo(BigDecimal aIdAvvocato,
			BigDecimal aIdFascicolo) throws F3BException;

	public void ExCancellaAvvocatoFascicoloSiepbyKey(BigDecimal aKey) throws F3BException;

	public AvvocatoModel ExCancellaStoricizzaAvvocato(AvvocatoModel aAvvocato, StoricoAvvocatoModel aStorico)
			throws F3BException;

	public Vector ExRicercaAvvocatoPerUffApparteneza(AvvocatoModel aAvvocato) throws F3BException;

	public AvvocatoFascicoloSiepModel ExDeassegnaAvvocato(AvvocatoFascicoloSiepModel aAvvocato)
			throws F3BException;

	/**
	 * 
	 * @param aAvvUp
	 * @param aAvvocatoIns
	 * @return
	 * @throws F3BException
	 */
	public AvvocatoFascicoloSiepModel ExSostituzioneAvvocato(AvvocatoFascicoloSiepModel aAvvUp,
			AvvocatoFascicoloSiepModel aAvvocatoIns) throws F3BException;

	public AvvocatoFascicoloSiepModel ExSostituzioneAvvocato(EventoNotificaModel lEveNot,
			AvvocatoFascicoloSiepModel aAvvUp, AvvocatoFascicoloSiepModel aAvvocatoIns) throws F3BException;

	public ByteArrayOutputStream ExStampaDocumentoAvvocato(EventoModel aEvento,
			AvvocatoFascicoloSiepModel aAvvocatoSiep, String aNomeTemplate, UtenteModel aUtenteModel)
			throws F3BException;

	public Vector ExRicercaForiCaricati() throws F3BException;

	public Vector ExRicercaForiDisponibili() throws F3BException;

}