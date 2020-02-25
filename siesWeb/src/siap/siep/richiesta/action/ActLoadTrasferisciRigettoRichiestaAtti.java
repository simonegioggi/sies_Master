package siap.siep.richiesta.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.competenza.controller.ICompetenza;
import siap.siep.competenza.model.CompetenzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadTrasferisciRigettoRichiestaAtti
 * </p>
 * <p>
 * Description: prepara la pagina per Trasferire il Messaggio
 * </p>
 * <p>
 * di RIGETTO Trasmissione Atti per Competenza
 * </p>
 */

public class ActLoadTrasferisciRigettoRichiestaAtti extends ActionSiap implements ICostantiRichiesta {

	public String processRequest() throws Exception {
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// ricerca evento competenza
		ICompetenza lCompCtrl = SIEPLookupRemote.getCompetenzaRemote();
		CompetenzaModel mComp = lCompCtrl.ExRicercaCompetenzaByEveIdEvento(lIdEvento);
		setRequestAttribute("competenza", mComp);

		// Ricerca EventoNotifiche per le Motivazioni presenti in CampoNote
		IEvento lctrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = lctrl.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("eventonotifica", lEveMod);

		// Dati del Fascicolo che avrebbe determinato la competenza (Cumulante)
		/*
		 * FascicoloSiepModel mFascComp = new FascicoloSiepModel();
		 * mFascComp.setChiaveUfficio(mComp.getChiaveUfficio());
		 * mFascComp.setChiaveAnno(mComp.getChiaveAnno()); mFascComp.setChiaveProgr(mComp.getChiaveProgr());
		 * 
		 * IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote(); FascicoloSiepModel findedFasc
		 * = null; if(mComp.getFasSieIdFascicoloSiep()!=null){ findedFasc =
		 * lCtrlFas.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(mFascComp);
		 * setRequestAttribute("fascCompetenza", findedFasc); }
		 */
		setRequestAttribute("IDEvento", lIdEvento.toString());

		return PG_LOAD_TRASFERISCI_RIGETTO_RICHIESTA_ATTI;
	}
}
