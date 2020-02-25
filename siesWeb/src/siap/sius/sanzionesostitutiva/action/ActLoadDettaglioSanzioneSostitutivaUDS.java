package siap.sius.sanzionesostitutiva.action;
/**
* <p>Title: ActLoadDettaglioSanzioneSostitutiva</p>
* <p>Description: Classe Action per la load dettaglio di PeriodoAltraSanzione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadDettaglioSanzioneSostitutivaUDS extends ActionSius
		implements ICostantiSanzioneSostitutiva {

	/*****************************************************************************
	 * Azione di caricamento della pagina di Dettaglio dei dati.
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws Exception {

		// Recupera la key del record da Visualizzare
		// BigDecimal lIdPeriodoAltraSanzione = getRequestBigDecimalParameter (
		// ICostantiSanzioneSostitutiva.CAMPO_ID_PERIODO_ALTRA_SANZIONE) ;
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		// Chiama il controller.
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNot = lCtrlEve.ExRicercaEventoNotificaByKeyForRichiestaAtti(lIdEvento);
		if (lEveNot == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Dato Assente !");

		// setLinkRitorno();

		// Recupera i dati del record da modificare
		IPeriodoAltraSanzione lCtrl = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
		PeriodoAltraSanzioneModel lPerMod = lCtrl.ExRicercaSanzioneSostitutivaByIdEvento(lIdEvento);

		// ===========================================================
		// Restituisce la msg se i dati non sono stati recuperati.
		// ===========================================================
		if (lPerMod == null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "I dati richiesti non sono stati trovati");
			// Specificare eventualmente la jump page dove verrà ridirezionata la
			// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
			// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
			// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
			// della root_dir es /siap/frame.htm
			setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR);
			return IWebConstants.PG_MESSAGE;
		}

		// ====================================================
		// Passa il Model alla componente di visualizzazione
		// ====================================================
		setRequestAttribute("PeriodoAltraSanzione", lPerMod);

		// =========================================================
		// Restituisce la pagina di Visualizzazione del Dettaglio.
		// =========================================================
		// Imposta Modalità.
		setRequestAttribute("modalita", "D");

		// model Istituto Detenzione
		IstitutoDetenzioneModel lIstMod = null;
		if (lPerMod != null && lPerMod.getIstDetIdIstitutoDetenzione() != null
				&& !lPerMod.getIstDetIdIstitutoDetenzione().equals("-")) {
			IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
			lIstMod = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lPerMod.getIstDetIdIstitutoDetenzione());
		}

		setRequestAttribute("istitutodetenzione", lIstMod);

		// Modificabilità
		String lModificabile = "SI";
		// String lStampabile = "NO";

		setRequestAttribute("Modificabile", lModificabile);

		return PG_LOAD_DETTAGLIOINIZIOSANZIONESOSTITUTIVA;
	}

}