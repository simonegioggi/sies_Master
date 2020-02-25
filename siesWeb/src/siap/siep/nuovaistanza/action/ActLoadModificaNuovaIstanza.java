package siap.siep.nuovaistanza.action;

/**
* <p>Title: ActLoadModificaNuovaIstanza</p>
* <p>Description: Classe Action per la load modifica di NuovaIstanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadModificaNuovaIstanza extends ActionSiap implements ICostantiNuovaIstanza {

	/*****************************************************************************
	 * Azione di caricamento della pagina di Modifica dei dati. Si occupa anche di precaricare tutti i dati da
	 * visualizzare i tale pagina (es: combo)
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lIdNuovaIstanza = getRequestBigDecimalParameter(CAMPO_ID_NUOVA_ISTANZA);
		INuovaIstanza lCtrl = SIEPLookupRemote.getNuovaIstanzaRemote();
		NuovaIstanzaModel lNuoMod = lCtrl.ExRicercaNuovaIstanzaById(lIdNuovaIstanza);

		if (lNuoMod.getAvvIdAvvocato() != null) {
			AvvocatoModel lAvvMod = new AvvocatoModel();
			lAvvMod.setIdAvvocato(lNuoMod.getAvvIdAvvocato());

			IAvvocato lCtrlAvv = SIEPLookupRemote.getAvvocatoRemote();
			Vector lVect = lCtrlAvv.ExRicercaAvvocatoPerInserimento(lAvvMod);
			AvvocatoModel lAvv = new AvvocatoModel((AvvocatoModel) lVect.get(0));
			lNuoMod.setAvvocato(lAvv);
		}

		if (lNuoMod.getAvvIdAvvocatoPresentante() != null) {
			AvvocatoModel lAvvModel = new AvvocatoModel();
			lAvvModel.setIdAvvocato(lNuoMod.getAvvIdAvvocatoPresentante());

			IAvvocato lCtrlAvv = SIEPLookupRemote.getAvvocatoRemote();
			Vector lAvvocati = lCtrlAvv.ExRicercaAvvocatoPerInserimento(lAvvModel);
			AvvocatoModel lAvvocato = new AvvocatoModel((AvvocatoModel) lAvvocati.get(0));
			lNuoMod.setAvvocatoPresentante(lAvvocato);
		}
		IFascicoloSiep lCtrlFs = SIEPLookupRemote.getFascicoloSiepRemote();
		FascicoloSiepModel fascSiepMod = lCtrlFs.ExRicercaFascicoloByKey(lNuoMod.getFasSieIdFascicoloSiep());
		setSessionAttribute("fascicolo", fascSiepMod);
		setSessionAttribute("sentenza", fascSiepMod.getSentenza());
		setSessionAttribute("soggetto", fascSiepMod.getSoggetto());

		setRequestAttribute("nuovaistanza", lNuoMod);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaMittenteIstanza(),
				lNuoMod.getCodAutoritaMittente());
		setRequestAttribute("autorita", "" + lOption);

		Option lOptionTA = new Option(DecodificheManager.getInstance().getTipoAvvocato(),
				lNuoMod.getTipoAvvocato());
		setRequestAttribute("tipoAvvocato", "" + lOptionTA);

		Option lOptionCI = new Option(DecodificheManager.getInstance().getTipoContenutoIstanza(),
				lNuoMod.getCodContenuto());
		setRequestAttribute("contenuto", "" + lOptionCI);

		// Imposta Modalità.
		setRequestAttribute("modalita", "M");

		return PG_LOAD_MODIFICA_NUOVA_ISTANZA;
	}

}