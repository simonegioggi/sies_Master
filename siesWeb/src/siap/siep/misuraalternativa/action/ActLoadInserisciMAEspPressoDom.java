package siap.siep.misuraalternativa.action;

import java.util.Collection;
import java.util.Iterator;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * <p>
 * Title: ActLoadInserisciMAEspPressoDom
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di Concessione Esecuzione Detenzione presso Domicilo
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadInserisciMAEspPressoDom extends ActConcessione implements ICostantiMisuraAlternativa {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// tutti i controlli e la maggior parte delle request si trovano nella classe madre
		// passo la posizione precedente per vedere se esiste il verbale o no
		String lRitorno = this.getConcessione(ICostantiMisuraAlternativa.ESP_PRESSO_DOM_POS_GIU);
		if (!lRitorno.equals(""))
			return lRitorno;

		// ==========================================================================
		// MEV29 - 09/2015 provo a recuperare il provvedimento di ammissione provvisoria
		// se Posizione Giuridica in ammissione Provvisoria
		String lCodPosizione = (String) getRequestAttribute("lcodicePosizione");

		if (lCodPosizione != null && (lCodPosizione.equals("29") || lCodPosizione.equals("54"))) {
			EventoModel lEvent = new EventoModel();
			lEvent.setFasSieIdFascicoloSiep(
					((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
			lEvent.setCodTipoEvento("01");
			lEvent.setFlagDocumentoRegistrato("S");
			String[] lProvv = { "04", "09", "12" };
			String[] lMotivo = null;

			if ("29".equals(lCodPosizione)) // Pos Giu 29 Detenzione Domiciliare Provvisoria
			{
				// carica nel model il tipo motivo
				lMotivo = new String[] { "2005" };
			} else {
				// carica nel model il tipo motivo
				lMotivo = new String[] { "2006", "2008" };
			}

			// Ricerca eventuale Provvedimeto di Concessione Misura Provvisoria (affidamento/Detenzione)
			IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
			EventoModel lEve = lCtrlEvento.ExRicercaEventoPerMotivoPerProvv(lMotivo, lProvv, lEvent);

			// setta la risposta della ricerca nella request
			setRequestAttribute("eventoammissioneprovvisoriaaffidamento", lEve);

			if (lEve != null) {
				// Ricerca nella tabella MISURA ALTERNATIVA
				IMisuraAlternativa lCtrlMA = SICOLookupRemote.getMisuraAlternativaRemote();
				MisuraAlternativaModel lMA = lCtrlMA
						.ExRicercaMisuraAlternativaByIdEvento(lEve.getEveIdEvento());

				// setta la risposta della ricerca nella request
				setRequestAttribute("maammissioneprovvisoria", lMA);
			}
		}

		// setto il campo codice motivo
		Collection lmotivo = DecodificheManager.getInstance().getMotivoProvvedimentoMAEspPressoDom();

		String lDesMotivo = "";
		String lCodiceMotivo = "";
		if (lmotivo != null && !lmotivo.isEmpty()) {
			Iterator lIter = lmotivo.iterator();
			if (lIter.hasNext()) {
				DecodificheModel lDecMod = (DecodificheModel) lIter.next();
				lDesMotivo = lDecMod.getDescription();
				lCodiceMotivo = lDecMod.getCode();
			}
		}
		setRequestAttribute("motivoProvv", lDesMotivo);
		setRequestAttribute("codicemotivo", lCodiceMotivo);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS(), "UDS");
		setRequestAttribute("tipoUfficioSIUS", "" + lOption);

		setRequestAttribute("tipoMisura", ICostantiMisuraAlternativa.ESP_PRESSO_DOM);

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_MA_CONCESSIONE;
	}
}