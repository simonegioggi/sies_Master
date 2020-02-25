package siap.siep.posizione.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadModificaPosizioneGiuridica</p>
* <p>Description: Classe Action per la load modifica di Posizione Giuridica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadModificaPosizioneGiuridica extends ActLoadGestionePosizioneGiuridica implements ICostantiPosizioneGiuridica {

	/**
	 * Azione di Load Inserisci Posizione Giuridica
	 * @return Nome della pagina JSP su cui posizionarsi
	 * al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {
		FascicoloSiepModel lFasMod = null;

		if (isSessionAttributeNullObj("fascicolo")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicolo");
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			throw new SIEPException(SIEPException.USER_MESSAGE, "Selezionare un procedimento.");
		} else {
			lFasMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

			if ("01".equals(lFasMod.getCodMotivoArchiviazione())) {
				// COS_STATO_FASCICOLO = ARCHIVIATO/DEFINITO
				RedirectTo lRedirigi = new RedirectTo();

				lRedirigi.setPage(IWebConstants.PG_MAIN);
				lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo");
				lRedirigi.setParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP, lFasMod.getIdFascicoloSiep().toString());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

				throw new F3BException(F3BException.USER_MESSAGE, "Il fascicolo risulta archiviato");
			}
		}

		// Controlla se per il fascicolo selezionato esiste un ordine di esecuzione (o legge simeone)
		IOrdineEsecuzione lCtrl = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		boolean lFlagOrdineEsecuzione = lCtrl.ExEsisteOrdineEsecuzioneByFascicoloSiep(lFasMod.getIdFascicoloSiep());

		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_POSIZIONE_GIURIDICA);

		IPosizioneGiuridica lCtrlPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneModel lPosLuoMod = lCtrlPos.ExRicercaPosizioneGiuridicaLuogoDetenzioneByKey(lId);

		PosizioneGiuridicaModel lPosMod = lPosLuoMod.getPosizioneGiuridica();
		LuogoDetenzioneModel lLuoMod = lPosLuoMod.getLuogoDetenzione();

		// Imposta Combo posizione Giuridica
		Option lOption = null;
		if (!lFlagOrdineEsecuzione)
			lOption = new Option(DecodificheManager.getInstance().getPosizioneGiuridicaIscrizione(), lPosMod.getCodPosizioneGiuridica());
		else
			lOption = new Option(DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione(), lPosMod.getCodPosizioneGiuridica());

		setRequestAttribute("posizioneGiuridica", "" + lOption);

		// Imposta Stato posizione Processuale
		lOption = new Option(DecodificheManager.getInstance().getPosizioneProcessuale(), lPosMod.getCodPosizioneProcessuale());
		setRequestAttribute("posizioneProcessuale", "" + lOption);

		// Imposta Tipo Istituto
		if (lLuoMod != null) {
			if (lFasMod.getFlagAltraCausa() != null && lFasMod.getFlagAltraCausa().equals("S")) {
				// modifica relativa al tipo istituto
				// lOption = new Option( DecodificheManager.getInstance().getTipoIstituto(), lLuoMod.getCodTipoIstituto() );
				// setRequestAttribute("tipoIstitutoAltraCausa", "" + lOption );
				lOption = new Option(DecodificheManager.getInstance().getTipoIstituto());
				setRequestAttribute("tipoIstitutoNormale", "" + lOption);
			} else {
				lOption = new Option(DecodificheManager.getInstance().getTipoIstituto());
				setRequestAttribute("tipoIstitutoAltraCausa", "" + lOption);
				// modifica relativa al tipo istituto
				// lOption = new Option( DecodificheManager.getInstance().getTipoIstituto(), lLuoMod.getCodTipoIstituto() );
				// setRequestAttribute("tipoIstitutoNormale", "" + lOption );
			}
		} else {
			lOption = new Option(DecodificheManager.getInstance().getTipoIstituto());
			setRequestAttribute("tipoIstitutoNormale", "" + lOption);
			setRequestAttribute("tipoIstitutoAltraCausa", "" + lOption);
		}

		setRequestAttribute("modalita", "M");

		setRequestAttribute("PosizioneGiuridicaLuogoDetenzioneModel", lPosLuoMod);

		return PG_LOAD_INSERISCIPOSIZIONEGIURIDICA;
	}
}