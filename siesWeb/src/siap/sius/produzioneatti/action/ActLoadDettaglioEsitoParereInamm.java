package siap.sius.produzioneatti.action;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
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

public class ActLoadDettaglioEsitoParereInamm extends ActionSius implements ICostantiProduzioneAtti {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio ");

		this.setLinkRitorno();

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Chiama il controller.
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEveNot = lCtrl.ExRicercaEventoEsitoParereInamm(lIdEvento);

		setRequestAttribute("evento", lEveNot);

		// 04/04/2007 Corretta valorizzazione Tipo Ufficio Destinatario.
		EventoNotificaModel lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);
		if (lEveNotMod == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "EventoNotificaModel Assente !");

		String lUfficio = lEveNotMod.getNotifiche()[0].getUffCodUfficio();
		UfficioModel lUffMod = new UfficioModel();
		Collection lCol = (DecodificheManager.getInstance()).getTipoUfficio();

		if (lUfficio != null) {
			IUfficio lUff = SICOLookupRemote.getUfficioRemote();
			lUffMod = lUff.getUfficioByKey(lEveNotMod.getNotifiche()[0].getUffCodUfficio().toUpperCase());
			setRequestAttribute("codTipoUfficioS", lUffMod.getCodTipoUfficio());
			setRequestAttribute("descTipoUfficioS",
					DecodificheUtils.getDescbyCode(lCol, lUffMod.getCodTipoUfficio()));
		} else
			throw new SIUSException(SIUSException.USER_MESSAGE, "Ufficio Destinatario non valorizzato !");

		Collection lColMot = (DecodificheManager.getInstance()).getMotivoProvvedimento();
		setRequestAttribute("codMotivo", DecodificheUtils.getDescbyCode(lColMot, lEveNot.getCodMotivo()));

		// Modificabilità
		String lModificabile = "NO";
		// String lStampabile = "NO";
		if (IsFascicoloSiusModificabile()) {
			// Stampabilità
			if (lEveNot.getFlagDocumentoRegistrato() == null
					|| lEveNot.getFlagDocumentoRegistrato().compareTo("N") == 0) {
				// Modificabilità e Stampabilità coincidono
				// lStampabile = "SI";
				lModificabile = "SI";
			}
		}
		setRequestAttribute("Modificabile", lModificabile);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Modificabile: " + lModificabile);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: fine ");

		return PG_DETTAGLIO_ESITOPAREREINAMM;
	}

}