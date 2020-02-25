package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 *
 * <p>
 * Title: ActInserisciVariazioneDecorrenzaScadenza
 * </p>
 * <p>
 * Description: Inserimento della Variazione decorrenza e scadenza pena
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
public class ActInserisciVariazioneDecorrenzaScadenza extends ActOrdineEsecuzione
		implements ICostantiOrdineEsecuzione {
	/**
	 * Azione di Inserimento del Evento
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		EventoNotificaModel lEve = new EventoNotificaModel();
		// lEve.getEvento().setDescrMotivo("SC");
		lEve.getEvento().setCodMotivo("0249"); // 0249 - Variazione decorrenza e scadenza pena
		// lEve.getEvento().setFlagDocumentoRegistrato("N");
		lEve = getEventoVariazioneDecorrenzaScadenza(lEve.getEvento());

		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheVariazioneDecorrenzaScadenza();

		lEve.setNotifiche(lNotifiche);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);

		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE)) {
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));
		}

		// ==========================================================================
		// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti
		// viene modificato
		// n.b. in realtà noi stiamo inserendo una comunicazione (01-12-0249) e non
		// un OE.
		// ==========================================================================
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActDettaglioVariazioneDecorrenzaScadenza&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	}

}