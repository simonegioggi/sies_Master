package siap.siep.sanzionesostitutiva.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.sanzionesostitutiva.model.SanzioneSostResiduaModel;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.verbale.model.VerbaleModel;

/**
 * Title: ISanzioneSostitutiva
 * Description: Classe Controller per Le Sanzioni Sostitutive
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface ISanzioneSostitutiva {

	/**
	 * Metodo per registrare l'annotazione dell'avvenuta espulsione. Inserisce un evento Verbale, il verbale e
	 * l'evento di Comunicazione/Annotazione Inserisce la PENA_RESIDUA il record SOSPENSIONE.
	 *
	 * @param aEvVerbale
	 * @param aEvComunicazione
	 * @param aVerbale
	 * @param aPenaResiduaMod
	 *            - Pena residua calcolata al momento della sospensione
	 * @param aSospMod
	 *            - pena espiata e pena residua
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel exInserisciAnnotazioneEspulsione(EventoModel aEvVerbale,
			EventoNotificaModel aEvComunicazione, VerbaleModel aVerbale, PenaResiduaModel aPenaResiduaMod,
			SospensioneModel aSospMod) throws F3BException;

	/**
	 * Inserisce Evento Annotazione Mancata Espulsione e Notifiche Comunicazione Sollecito
	 *
	 * @param aEvNotModel
	 * @param aVerbaleMod
	 *            - Verbale con i dati della nato mancata espulsione
	 * @param aCampoNota
	 *            - Eventuale nota scritta dall'utente
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel exInserisciMancataEspulsione(EventoNotificaModel aEvNotModel,
			VerbaleModel aVerbaleMod, CampoNotaModel aCampoNota) throws F3BException;

	/**
	 * Inserisce Evento Richieste Revoca Espulsione e Notifiche al GE
	 *
	 * @param aEvNotModel
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel exInserisciRichiestaRevocaEspulsione(EventoNotificaModel aEvNotModel)
			throws F3BException;

	/**
	 * Effettua la validazione della Comunicazione Scadenza Termini Espulsione e contestualmente del Verbale
	 * di Avvenuta Espulsione
	 *
	 * @param aEvComunicazione
	 * @return
	 * @throws F3BException
	 */
	public EventoModel exUpdateAnnotazioneEspulsione(EventoModel aEvComunicazione) throws F3BException;

	/**
	 * Effettua la validazione della Annotazione Mancata Espulsione
	 *
	 * @param aEvAnnotazione
	 * @return
	 * @throws F3BException
	 */
	public EventoModel exUpdateMancataEspulsione(EventoModel aEvAnnotazione) throws F3BException;

	/**
	 * Effettua la validazione della Richiesta Revoca Espulsione
	 *
	 * @param aEvRichiesta
	 * @return
	 * @throws F3BException
	 */
	public EventoModel exUpdateRichiestaRevocaEspulsione(EventoModel aEvRichiesta) throws F3BException;

	/**
	 * Inserisce la comunicazione per il nuovo residuo pena
	 *
	 * @param aEvNotModel
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel exInserisciComunicazioneNuovoResiduoPena(EventoNotificaModel aEvNotModel)
			throws F3BException;

	/**
	 * Effettua la validazione della Comunicazione Nuovo Residuo Pena
	 *
	 * @param aEvComunicazione
	 * @return
	 * @throws F3BException
	 */
	public EventoModel exUpdateComunicazioneNuovoResiduoPena(EventoModel aEvComunicazione)
			throws F3BException;

	/**
	 * Effettua l'inserimento dell'OE a seguito revoca/conversione SS su fascicolo con cumulo
	 *
	 * @param aEvNotModel
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel exInserisciOENuovoResiduoPena(EventoNotificaModel aEvNotModel,
			PenaResiduaModel aPenResMod) throws F3BException;

	/**
	 * Effettua la validazione dell'OE a seguito revoca/conversione SS su fascicolo con cumulo
	 *
	 * @param aEventoModel
	 * @return
	 * @throws F3BException
	 */
	public EventoModel exUpdateOENuovoResiduoPena(EventoModel aEventoModel) throws F3BException;

	/**
	 * Restituisce l'ultima Sanzione Sostitutiva Residua per il fascicolo passato in input se esiste
	 *
	 * @param aIdFascicoloSiep
	 * @param aFlagValidata.
	 *            Se 'S' recupera l'ultima validata, se 'N' l'ultima non validata, se null l'ultima in
	 *            assoluto
	 * @return SanzioneSostResiduaModel o null se non presente una SS residua
	 * @throws F3BException
	 */
	public SanzioneSostResiduaModel getUltimaSSResidua(BigDecimal aIdFascicoloSiep, String aFlagValidata)
			throws F3BException;

	/**
	 * Restituisce la SS residua collegata alla pena residua passata in input
	 *
	 * @param aIdPenaResidua
	 * @return
	 * @throws F3BException
	 */
	public SanzioneSostResiduaModel getSSByIdPenaResidua(BigDecimal aIdPenaResidua) throws F3BException;

	public Vector ExRicercaSanzioneSostResiduaByIdFascicolo(BigDecimal aIdFascicolo) throws F3BException;

	public String ExInserisciSanzioniSostResidueWithoutSequence(ArrayList aSanzioneSostResidua,
			Connection lConn) throws F3BException;

	/**
	 * Produce la stampa per le Sanzioni Sostitutive
	 *
	 * @param aEvento
	 * @param aUtente
	 * @return
	 * @throws F3BException
	 */
	public ByteArrayOutputStream exStampaSS(EventoNotificaModel aEvento, UtenteModel aUtente)
			throws F3BException;

	/**
	 * exUpdateRichiestaRevocaSS
	 *
	 * @param aEvRichiesta
	 * @return
	 * @throws F3BException
	 */
	public EventoModel exUpdateRichiestaRevocaSS(EventoModel aEvRichiesta) throws F3BException;

	/**
	 * MEV_2023-13
	 *
	 * @param aEvNotModel
	 * @return
	 * @throws F3BException
	 */
	public EventoNotificaModel exInserisciOrdineIngiunzione(EventoNotificaModel aEvNotModel,
			String[] lArrayIdRate) throws F3BException;

	public EventoModel exUpdateOrdineIngiunzione(EventoModel aEvento) throws F3BException;

	public void exAggiornaNotificheOrdineIngiunzione(EventoModel aEvento,
			Vector<NotificaModel> listaNotDaAggiornare) throws F3BException;

	public EventoNotificaModel exModificaOrdineIngiunzione(EventoNotificaModel aEvNotModel,
			String[] lArrayIdRate) throws F3BException;

	// MEV_2023-33
	public BigDecimal ExGetCountRicercaFascicoliPerStatoPagamento(FascicoloSiepModel aFasMod,
			String aTipoRicerca) throws F3BException;

	public Vector ExRicercaFascicoliPerStatoPagamentoPaged(FascicoloSiepModel aFasMod, int aPagina,
			String aTipoRicera) throws F3BException;

  public EventoNotificaModel exInserisciNotaTrasmissione(EventoNotificaModel aEvNotModel) throws F3BException;
  public EventoNotificaModel exModificaNotaTrasmissione(EventoNotificaModel aEvNotModel) throws F3BException;
  public EventoModel exUpdateNotaTrasmissione (EventoModel aEvento) throws F3BException;
  public EventoNotificaModel exInserisciProvvedimentoEstinzione(EventoNotificaModel aEvNotModel) throws F3BException;
	public EventoNotificaModel exModificaProvvedimentoEstinzione(EventoNotificaModel aEvNotModel) throws F3BException;
}