package siap.siep.istanza.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istanza.controller.IIstanza;
import siap.siep.istanza.model.IstanzaModel;
import siap.siep.istanza.model.IstanzaSoggettoEventoFascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadModificaIstanzaAnnTrasmissione
 * </p>
 * <p>
 * Description: Classe Action per la load modifica di Posizione Giuridica
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
public class ActLoadModificaIstanzaAnnTrasmissione extends ActionSiap implements ICostantiIstanza {

	/**
	 * Azione di Load Inserisci Istanza
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("istanza", getRequestStringParameter(CAMPO_ID_ISTANZA),
				getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "L'" + lck.getEntity()
					+ " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}
		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_ISTANZA);
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		setRequestAttribute("fascicolo", lFascicoloModel);

		IIstanza lCtrlIst = SIEPLookupRemote.getIstanzaRemote();
		IstanzaSoggettoEventoFascicoloSiepModel lMod = lCtrlIst
				.ExRicercaIstanzaSoggettoEventoFascicoloSiepByKey(lId);

		IstanzaModel lIstMod = lMod.getIstanza();

		if (lIstMod == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Nessun Elemento trovato");

		// Oggetto dell'istanza
		Option lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoTDS(),
				lIstMod.getCodMotivo());
		setRequestAttribute("contenuto", "" + lOption);

		// Esito dell'istanza
		/*
		 * Poichè in modifica è possibile solo selezionare 'Trasferimento per competenza' e 'Archiviato'
		 * vengono tolti dalla collection delle Decodifiche
		 */
		/*
		 * Collection lEsitoIstanza = DecodificheManager.getInstance().getEsitoProvvedimentoIstanza();
		 * DecodificheModel lIscritta = new DecodificheModel(); lIscritta.setContesto("ESITO_PROVVEDIMENTO");
		 * lIscritta.setCode("I"); DecodificheModel lRiferita = new DecodificheModel();
		 * lRiferita.setContesto("ESITO_PROVVEDIMENTO"); lRiferita.setCode("R");
		 * lEsitoIstanza.remove(lIscritta); lEsitoIstanza.remove(lRiferita); String lSelezionato = "-"; String
		 * lCodEsito = lIstMod.getCodEsito(); if( lCodEsito != null && !lCodEsito.equals("I") &&
		 * !lCodEsito.equals("R")) lSelezionato = lCodEsito; lOption = new Option( lEsitoIstanza, lSelezionato
		 * ); setRequestAttribute("esitoProvvedimentoIstanza", "" + lOption );
		 */
		setRequestAttribute("modalita", "M");

		setRequestAttribute("istanzaSoggettoEventoFascicoloSiep", lMod);

		return PG_LOAD_INSERISCIISTANZA_ANN_TRASMISSIONE;
	}

}