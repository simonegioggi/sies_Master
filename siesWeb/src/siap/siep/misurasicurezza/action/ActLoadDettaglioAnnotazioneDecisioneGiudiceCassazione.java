package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ActLoadDettaglioAnnotazioneDecisioneGiudiceCassazione
 * </p>
 * <p>
 * Description: Classe Action per il dettaglio dell' iscrizione Annotazione del provvedimento
 * </p>
 * <p>
 * di annotazione del Giudice su faascicoli di Misure di Sicurezza Provvisorie o Fuori sentenza
 * </p>
 */
public class ActLoadDettaglioAnnotazioneDecisioneGiudiceCassazione extends ActSIESDettaglioProvvedimento
		implements ICostantiMisuraSicurezza {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Evento (Provvedimento di annotazione Inserito)
		EventoModel lEveMod = new EventoModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrl.ExRicercaEventoByKey(lIdEvento);
		setRequestAttribute("evento", lEveMod);

		// Evento (Provvedimento del Giudice Cassazione/Riesame
		EventoModel lEveOrd = null;
		lEveOrd = (EventoModel) lCtrl.ExRicercaEventoByKey(lEveMod.getEveIdEvento());
		setRequestAttribute("eventoGiudice", lEveOrd);

		// Decreto / Ordinanza
		DecretoOrdinanzaSiepModel lDepDecMod = new DecretoOrdinanzaSiepModel();
		IDecretoOrdinanzaSiep lCtrlDec = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
		lDepDecMod = lCtrlDec.ExRicercaDecretoOrdinanzaSiepByIdEventoSemplice(lEveOrd.getIdEvento());
		setRequestAttribute("decretoordinanza", lDepDecMod);

		// Posizione Giuridica
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod
				.getIdFascicoloSiep());
//		String lCodPosGiu = lPos.getPosizioneGiuridica().getCodPosizioneGiuridica();
		setRequestAttribute("posizioneluogoaltra", lPos);

		// PenaResidua
		PenaResiduaModel lPenaResidua = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", lPenaResidua);

		// Misure Sicurezza
		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		Vector MisSicVec = null;
		List lListMis = new ArrayList();

		MisSicVec = new Vector(lMisCtrl.ExRicercaMisuraSicurezzaByEventoKey(lIdEvento));
		if (MisSicVec != null && MisSicVec.size() > 0) {
			setRequestAttribute("VecMisureSic", MisSicVec);
		} else {
			lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFascMod.getIdFascicoloSiep());
			if (lListMis != null) {
				setRequestAttribute("listaMisureSic", lListMis);
			}
		}

		return PG_DETTAGLIO_ANNOTAZIONE_DECISIONE_GIUDICE_CASSAZIONE;
	} // chiude processRequest

} // Chiude Classe