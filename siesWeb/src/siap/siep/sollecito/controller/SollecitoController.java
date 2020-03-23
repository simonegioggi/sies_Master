package siap.siep.sollecito.controller;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.controller.SiapController;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.model.TemplateModel;
import siap.sico.util.SICOLookupRemote;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SollecitoController extends SiapController implements ISollecito {

	private int mEventiSize = 0;

	public int getEventiSize() {
		return this.mEventiSize;
	}

	public Vector ExRicercaEventiPerFascicolo(BigDecimal aFascicoloId, int aNumPage) throws F3BException {

		int lIndex = (aNumPage - 1) * 2;
		// int i;
		Vector aVect = null;
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		Vector lVect = lCtrlEve.ExRicercaEventoStatoEsecuzioneByFascicoloSiep(aFascicoloId);
		/* loop su lVect e per ogni evento */
		if (lVect != null && lVect.size() != 0) {
			mEventiSize = lVect.size();
			aVect = new Vector();
			if (lIndex <= lVect.size()) {
				EventoNotificaModel lEveNotMod = (lCtrlEve
						.ExRicercaEventoNotificaByKey(((EventoModel) lVect.get(lIndex)).getIdEvento()));
				if (lEveNotMod != null) {
					aVect.add(lEveNotMod);
				}
			}
			if (lIndex + 1 <= lVect.size()) {
				EventoNotificaModel lEveNotMod = (lCtrlEve
						.ExRicercaEventoNotificaByKey(((EventoModel) lVect.get(lIndex + 1)).getIdEvento()));
				if (lEveNotMod != null) {
					aVect.add(lEveNotMod);
				}
			}
		}

		return aVect;
	}

	public EventoNotificaModel ExStampaSollecito(EventoModel aEvento) throws F3BException {

		// ByteArrayOutputStream lByteArrayOut = null;
		EventoNotificaModel lEveNotifica = null;

		// Invoca il Report generator.
		// ReportGenerator lReport = new ReportGenerator();

		// Preleva dalla tabella Template il nome del template RTF.
		IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
		TemplateModel lTemplateMod = lEventoCtrl.ExRicercaTemplateByCodMotivo(aEvento.getCodMotivo());

		if (lTemplateMod != null) {
			String lNomeTemplate = lTemplateMod.getPathRicerca() + lTemplateMod.getNomeTemplate();
			lEveNotifica = new EventoNotificaModel();
			lEveNotifica.setNomeTemplate(lNomeTemplate);
		}

		return lEveNotifica;
	}

}