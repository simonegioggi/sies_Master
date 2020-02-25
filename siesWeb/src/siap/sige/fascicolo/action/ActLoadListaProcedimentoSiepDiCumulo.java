package siap.sige.fascicolo.action;

import java.util.Vector;

import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

public class ActLoadListaProcedimentoSiepDiCumulo extends ActionSige implements ICostantiFascicoloSige {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String strFormName = getRequestStringParameter("formname");

		String strFieldCode = "";
		if (!(isRequestParameterNullObj("fieldcode")))
			strFieldCode = getRequestStringParameter("fieldcode");

		IFasSigeSentenza lFasSenCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();

		ICumulo lCtrCum = SIEPLookupRemote.getCumuloRemote();
		CumuloModel aModelCum = new CumuloModel();
		if (getFascicoloSigeEstesoInSessione().getFascicoloSiep() != null
				&& getFascicoloSigeEstesoInSessione().getFascicoloSiep().getIdFascicoloSiep() != null)
			aModelCum.setFasSieIdFascicoloSiep(
					getFascicoloSigeEstesoInSessione().getFascicoloSiep().getIdFascicoloSiep());

		// Vector lProcedimentoSiepDiCumulo = lCtrCum.ExRicercaCumulo(aModelCum);

		// Modifica del 28/11/2016 MEV_15_S4
		// La modifica si è resa necessaria per integrare la funzionalità
		// alla "Nuova Gestione del Cumulo" introdotta con la MEV_26
		// Vengono estratti dalla tabella Evento, tutti gli eventi legati al Fascicolo Siep,
		// che hanno COD_MOTIVO legati al cumulo
		Vector lProcedimentoSiepDiCumulo = lCtrCum.ExRicercaEventoCumulo(
				getFascicoloSigeEstesoInSessione().getFascicoloSiep().getIdFascicoloSiep());

		// CumuloModel aCumulo = new CumuloModel();
		setRequestAttribute("ListaProcedimentoSiepDiCumulo", lProcedimentoSiepDiCumulo);

		// Modifica del 28/11/2016 MEV_15_S4
		// Estraggo la sentenza legata al Fascicolo Siep
		ISentenza lSenCtrl = SIEPLookupRemote.getSentenzaRemote();
		SentenzaModel sentenzaModel = null;
		if (getFascicoloSigeEstesoInSessione().getFascicoloSiep().getSenIdSentenza() != null) {
			sentenzaModel = lSenCtrl.ExRicercaSentenzaByKey(
					getFascicoloSigeEstesoInSessione().getFascicoloSiep().getSenIdSentenza());
		}

		setRequestAttribute("SentenzaSiep", sentenzaModel);

		Vector lSentenze = lFasSenCtrl.ExRicercaSentenzeAssegnateFascicolo(
				getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdFascicoloSige());
		// Imposta la risposta nella request.
		setRequestAttribute("ListaTitoliEsecutivi", lSentenze);

		setRequestAttribute("fieldcode", strFieldCode);
		setRequestAttribute("formname", strFormName);

		return PG_LISTA_PROCEDIMENTI_DI_CUMULO_SIGE; // restituisce la jsp di VIEW
	}

}