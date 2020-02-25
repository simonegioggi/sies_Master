package siap.sige.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.SIUSException;

/**
 * <p>
 * Title: ActLoadDettaglioRichiestaParere
 * </p>
 * <p>
 * Description: Classe Responsabile della visualizzazione dati di dettaglio di un evento generato dalla
 * funzionalità della richiesta parere.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class ActLoadDettaglioEsitoParere extends ActSIESDettaglioProvvedimento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio ");

		this.setLinkRitorno();

		// NOOOOOOOOOOOOBigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// ***********************************************
		BigDecimal lIdProvvedimento = null;
		lIdProvvedimento = getRequestBigDecimalParameter(
				ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE);

		// Ricerca Provvedimento dalla chiave
		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvEvento = lCtrlProv.ExRicercaProvvedimentoById(lIdProvvedimento);

		setRequestAttribute("ProvvedimentoEvento", lProvEvento);

		BigDecimal lId = lProvEvento.getEventoNotifica().getEvento().getIdEvento();

		// riempie il model
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		// chiama il controller
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lId);

		INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
		Vector lNots = lCtrlNot
				.ExRicercaNotificaByKeyEvento(lProvEvento.getEventoNotifica().getEvento().getIdEvento());

		lEveMod.setNotifiche((NotificaModel[]) lNots.toArray(new NotificaModel[0]));

		setRequestAttribute("evento", lEveMod);

		String lUfficio = null;
		if (lEveMod.getNotifiche().length > 0) {
			lUfficio = lEveMod.getNotifiche()[0].getUffCodUfficio();
		}

		UfficioModel lUffMod = new UfficioModel();
		Collection lCol = (DecodificheManager.getInstance()).getTipoUfficio();

		if (lUfficio != null) {
			IUfficio lUff = SICOLookupRemote.getUfficioRemote();
			lUffMod = lUff.getUfficioByKey(lEveMod.getNotifiche()[0].getUffCodUfficio().toUpperCase());
			setRequestAttribute("codTipoUfficioS", lUffMod.getCodTipoUfficio());
			setRequestAttribute("descTipoUfficioS",
					DecodificheUtils.getDescbyCode(lCol, lUffMod.getCodTipoUfficio()));
		} else
			throw new SIUSException(SIUSException.USER_MESSAGE, "Ufficio Destinatario non valorizzato !");

		Collection lColMot = (DecodificheManager.getInstance()).getMotivoProvvedimento();
		setRequestAttribute("codMotivo",
				DecodificheUtils.getDescbyCode(lColMot, lEveMod.getEvento().getCodMotivo()));

		// ANGELA
		// Modificabilità
		String lModificabile = "NO";
		String lStampabile = "NO";

		// Stampabilità
		if (lEveMod.getEvento().getFlagDocumentoRegistrato() == null
				|| lEveMod.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
			// Modificabilità e Stampabilità coincidono
			lStampabile = "SI";
			lModificabile = "SI";
		}
		setRequestAttribute("Modificabile", lModificabile);
		setRequestAttribute("Stampabile", lStampabile);
		setRequestAttribute("ufficio", lUffMod);

		// *************************************************

		// Chiama il controller.
		// IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		// EventoModel lEveNot = lCtrl.ExRicercaEventoEsitoParereInamm(lIdEvento);
		// setRequestAttribute( "evento", lEveNot );

		return ICostantiRichiestaAtti.PG_DETTAGLIO_ESITOPARERE;
	}

}