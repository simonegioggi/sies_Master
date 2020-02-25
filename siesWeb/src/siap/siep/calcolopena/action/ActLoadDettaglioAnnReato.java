package siap.siep.calcolopena.action;

/**
* <p>Title: ActLoadDettaglioOEAltraCausa</p>
* <p>Description: Classe Action per la load dettaglio di Ordine Esecuzione per Altra Causa</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.reato.action.ICostantiReato;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadDettaglioAnnReato extends ActionSiap implements ICostantiAnnotazioneManuale {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascID = ((FascicoloSiepModel) getSessionAttribute("fascicolo"));

		String lId = getRequestStringParameter(ICostantiReato.CAMPO_ID_REATO);

		Vector lAnnotazioni = new Vector();

		IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
		lAnnotazioni = lCtrlAnn.ExRicercaAnnotazioniManualiByIdReato(new BigDecimal(lId));

		PenaComplessivaModel lPenaMod = new PenaComplessivaModel();

		IPenaComplessiva lCtrPena = SIEPLookupRemote.getPenaComplessivaRemote();
		lPenaMod = lCtrPena.ExRicercaPenaComplessivaByIdFascicolo(lFascID.getIdFascicoloSiep());

		this.setRequestAttribute("annotazioni", lAnnotazioni);
		this.setRequestAttribute("penacomplessiva", lPenaMod);

		return PG_LOAD_DETTAGLIO_ANNOTAZIONI_REATO;

	}
}
