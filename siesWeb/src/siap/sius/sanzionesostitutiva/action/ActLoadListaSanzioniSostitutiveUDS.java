package siap.sius.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.List;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadListaSanzioniSostitutiveUDS extends ActSanzioneSostitutiva
		implements ICostantiSanzioneSostitutiva {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		List lListaSanzioniSius = null;
		IPeriodoAltraSanzione lCtrl = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();

		// 14/05/2008 Se provengo dal DettaglioEsecuzioneSS, ho il parametro "IdFascicoloSius" nella request.
		String idFascicoloSius = null;
		if (!this.isRequestParameterNullObj("IdFascicoloSius"))
			idFascicoloSius = getRequestStringParameter("IdFascicoloSius");

		if (idFascicoloSius != null && idFascicoloSius.length() > 1)
			lListaSanzioniSius = lCtrl
					.ExRicercaSanzioneSostitutivaByIdFascicolo(new BigDecimal(idFascicoloSius));
		else

		if (isSessionAttributeNullObj("fascicoloSiusGP")) {
			FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
			BigDecimal lIdFasSiep = lFascMod.getIdFascicoloSiep();
			lListaSanzioniSius = lCtrl.ExRicercaSanzioneSostitutivaByIdSiep(lIdFasSiep);

		} else {

			// ricerca del fascicolo
			BigDecimal lIdFasSius = recuperoIdFascicolo();
			lListaSanzioniSius = lCtrl.ExRicercaSanzioneSostitutivaByIdFascicolo(lIdFasSius);
		}
		// 16/05/2008 Preleva l'evento collegato all'ultimo elemento di lListaSanzioniSius
		// per controllare se si riferisce a un provvedimento validato.
		if (lListaSanzioniSius.size() > 0) {
			PeriodoAltraSanzioneModel ultimoPAS = (PeriodoAltraSanzioneModel) (lListaSanzioniSius
					.get(lListaSanzioniSius.size() - 1));
			IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
			EventoModel lEveMod = lCtrlEve.ExRicercaEventoByKey(ultimoPAS.getEveIdEvento());
			setRequestAttribute("eventoUltimoPAS", lEveMod);
		}
		setRequestAttribute("listaSanzioniSius", lListaSanzioniSius);
		return PG_LOAD_LISTASANZIONISOSTITUTIVE;
	}

}