package siap.siep.verbale.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.verbale.model.VerbaleDataInizioModel;
import siap.siep.verbale.model.VerbaleModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: VerbaleController
 * </p>
 * <p>
 * Description: Classe Controller per Verbale
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
public interface IVerbale {

	/**
	 * Effettua l'inserimento di un Verbale di Arresto registrando: - l'Evento - il Verbale -
	 * aggiornando/inserendo la Posizione Giuridica - inserrendo il Luogo di Detenzione - aggiornando la
	 * Notifica (???) - cancellando le voci dallo scadenzario relative al verbale di arresto - aggiorna lo
	 * stato del procedimento (???) - aggiornando la pena residua (con eventuali gg di lib anticipata)
	 *
	 * @param aKeyFasc
	 * @param aVerbale
	 * @param aPosMod
	 * @param aLuoDetMod
	 * @param aNotMod
	 * @param aScaMod
	 * @param aPenMod
	 * @return
	 * @throws F3BException
	 */
	public VerbaleModel ExInserisciVerbale(BigDecimal aKeyFasc, VerbaleModel aVerbale,
			PosizioneGiuridicaModel aPosMod, LuogoDetenzioneModel aLuoDetMod, NotificaModel aNotMod,
			ScadenzarioModel aScaMod, PenaResiduaModel aPenMod) throws F3BException;

	public Vector ExRicercaVerbale(VerbaleModel aVerbale) throws F3BException;

	public VerbaleModel ExRicercaVerbaleByKey(BigDecimal aKey) throws F3BException;

	public VerbaleModel ExModificaVerbale(VerbaleModel aVerbale) throws F3BException;

	public void ExCancellaVerbale(VerbaleModel aVerbale, Connection lConn) throws F3BException;

	public EventoNotificaModel ExRicercaEventoVerbale(BigDecimal aKeyFasc, String userMsg)
			throws F3BException;

	public VerbaleModel ExInserisciVerbaleVaneRicerche(BigDecimal aKeyFasc, VerbaleModel aVerbale,
			NotificaModel aNotMod, ScadenzarioModel aScaMod, EventoNotificaModel aEveNot) throws F3BException;

	public VerbaleModel ExInserisciVerbaleSottoscrizione(BigDecimal aKeyFasc, VerbaleModel aVerbale)
			throws F3BException;

	/**
	 * Ricerca il Verbale Obblighi (03) collegato all'evento specificato
	 * 
	 * @param aIdEvento
	 * @return
	 * @throws F3BException
	 */
	public VerbaleModel ExRicercaVerbaleObblighiByIdEvento(BigDecimal aIdEvento) throws F3BException;

	/**
	 * Ricerca un Verbale Generico collegato all'evento passato in input
	 * 
	 * @param aIdEvento
	 * @return
	 * @throws F3BException
	 */
	public VerbaleModel ExRicercaVerbaleByIdEvento(BigDecimal aIdEvento) throws F3BException;

	public VerbaleModel ExInserisciDataInizioMisuraAlternativa(BigDecimal aKeyFasc, VerbaleModel aVerbale)
			throws F3BException;

	public VerbaleModel ExRicercaVerbaleByIdFascicolo(BigDecimal aKey) throws F3BException;

	public void ExCancellaDataInizioMisuraAlternativa(BigDecimal aKeyFasc, VerbaleModel aVerbale)
			throws F3BException;

	public VerbaleModel ExInserisciDataInizioMisuraAlternativaUDS(BigDecimal aKeyFasc, VerbaleModel aVerbale)
			throws F3BException;

	public void ExRegistraPenaVerbaleArresto(PenaResiduaModel lPenMod) throws F3BException;

	public VerbaleModel ExRicercaVerbaleByCodTipoIdEvento(BigDecimal aKey, String aCodTipo)
			throws F3BException;

	public PenaResiduaModel ExAggiornaPenaVerbale(PenaResiduaModel aPenaResidua) throws F3BException;

	public VerbaleModel ExInserisciRipristinoDetCarc(BigDecimal aKeyFasc, VerbaleModel aVerbale,
			PosizioneGiuridicaModel aPosMod, LuogoDetenzioneModel aLuoDetMod) throws F3BException;

	/**
	 * Effettua l'inserimento del Verbale di Notifica scadenza pena altra causa proveniente dal carcere. Viene
	 * inserito un EVENTO verbale, un VERBALE, la PENA_RESIDUA aggiornata
	 * 
	 * @param aKeyFasc
	 * @param aVerbale
	 * @param aPenaResidua
	 * @return
	 * @throws F3BException
	 */
	public VerbaleModel ExInserisciNotificaCarcere(BigDecimal aKeyFasc, VerbaleModel aVerbale,
			PenaResiduaModel aPenaResidua) throws F3BException;

	public VerbaleDataInizioModel ExRicercaVerbaleByIdFascicoloSiep(BigDecimal aKey) throws F3BException;

	public EventoModel ExInserisciVariazioneVerbaleSottoscrizione(BigDecimal aKeyFasc, VerbaleModel aVerbale)
			throws F3BException;

	/**
	 * Effettua l'inserimento dell'Annotazione Designazione Istituto da parte del DAP nella'ambito della
	 * esecuzione Misure Sicurezza. La Dinamica della designazione dell'Istituto è simile a quella del
	 * pervenimento ed inserimento Verbale, quindi viene utilizzata la tabella VERBALE. Viene inserito un
	 * EVENTO provvedimento Annotazione , un VERBALE, (la PENA_RESIDUA aggiornata ??)
	 * 
	 * @param aEvento
	 * @param aVerbale
	 * @param aPenaResidua
	 *            ?
	 * @return IdEvento
	 * @throws F3BException
	 */
	public BigDecimal ExInserisciEventoVerbale(EventoModel aEvento, VerbaleModel aVerbale)
			throws F3BException;

}