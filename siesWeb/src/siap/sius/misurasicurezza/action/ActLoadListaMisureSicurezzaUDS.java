package siap.sius.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.List;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadListaMisureSicurezzaUDS extends ActMisuraSicurezza
		implements ICostantiSiusMisuraSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		List lListaMisureSius = null;
		IPeriodoAltraMisura lCtrl = SIUSLookupRemote.getPeriodoAltraMisuraRemote();

		// 14/05/2008 Se provengo dal DettaglioEsecuzioneMS, ho il parametro "IdFascicoloSius" nella request.
		String idFascicoloSius = null;
		if (!this.isRequestParameterNullObj("IdFascicoloSius"))
			idFascicoloSius = getRequestStringParameter("IdFascicoloSius");

		if (idFascicoloSius != null && idFascicoloSius.length() > 1)
			lListaMisureSius = lCtrl.ExRicercaMisuraSicurezzaByIdFascicolo(new BigDecimal(idFascicoloSius));
		else

		if (isSessionAttributeNullObj("fascicoloSiusGP")) {
			FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
			BigDecimal lIdFasSiep = lFascMod.getIdFascicoloSiep();
			lListaMisureSius = lCtrl.ExRicercaMisuraSicurezzaByIdSiep(lIdFasSiep);

		} else {

			// ricerca del fascicolo
			BigDecimal lIdFasSius = recuperoIdFascicolo();
			lListaMisureSius = lCtrl.ExRicercaMisuraSicurezzaByIdFascicolo(lIdFasSius);
		}
		// 16/05/2008 Preleva l'evento collegato all'ultimo elemento di lListaSanzioniSius
		// per controllare se si riferisce a un provvedimento validato.
		if (lListaMisureSius.size() > 0) {
			PeriodoAltraMisuraModel ultimoPAS = (PeriodoAltraMisuraModel) (lListaMisureSius
					.get(lListaMisureSius.size() - 1));
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			EventoModel lEveMod = lCtrlEve.ExRicercaEventoByKey(ultimoPAS.getEveIdEvento());
			setRequestAttribute("eventoUltimoPAS", lEveMod);
		}
		setRequestAttribute("listaMisureSius", lListaMisureSius);
		return PG_LOAD_LISTAMISURESICUREZZA;
	}

}