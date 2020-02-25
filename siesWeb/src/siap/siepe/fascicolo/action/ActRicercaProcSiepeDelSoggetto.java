package siap.siepe.fascicolo.action;

import java.util.Vector;

import f3b.util.DateUtils;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siepe.fascicolo.controller.IFascicoloSiepe;
import siap.siepe.fascicolo.model.FascicoloSoggAttModel;
import siap.siepe.util.SIEPELookupRemote;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Ricerca dei Procedimenti SIEPE (rispondenti ai parametri selezionati) del soggetto individuato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class ActRicercaProcSiepeDelSoggetto extends ActionSiap implements ICostantiFascicoloSiepe {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		SoggettoModel lSogMod = new SoggettoModel();

		// Recupero dei parametri di ricerca.
		// Predisposizione alla chiamata diretta: preimpostazione dei parametri di filtro.
		String ufUtConnesso = "";
		String lIncludeArchiviati = "S";
		String codIncarico = "-";
		String descrIncarico = "-";
		String tipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		String dataDal = "";
		String dataAl = "";

		if (!isRequestParameterNullObj("hufUtConnesso"))
			ufUtConnesso = getRequestStringParameter("hufUtConnesso");
		if (!isRequestParameterNullObj("hlIncludeArchiviati"))
			lIncludeArchiviati = getRequestStringParameter("hlIncludeArchiviati");
		if (!isRequestParameterNullObj("hcodIncarico"))
			codIncarico = getRequestStringParameter("hcodIncarico");
		if (!isRequestParameterNullObj("hdescrIncarico"))
			descrIncarico = getRequestStringParameter("hdescrIncarico");
		if (!isRequestParameterNullObj("htipoUfficio"))
			tipoUfficio = getRequestStringParameter("htipoUfficio");
		if (!isRequestParameterNullObj("hdataDal"))
			dataDal = getRequestStringParameter("hdataDal");
		if (!isRequestParameterNullObj("hdataAl"))
			dataAl = getRequestStringParameter("hdataAl");

		// riempie il model
		lSogMod.setIdSoggetto(getRequestBigDecimalParameter("IdSoggetto"));

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		lSogMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

		// Si chiama il FascicoloSiusController.
		IFascicoloSiepe lFascSogCtrl = SIEPELookupRemote.getFascicoloSiepeRemote();
		Vector lFascicoliSoggetti = lFascSogCtrl.ExRicercaFascSiepeDelSoggetto(lSogMod, ufUtConnesso,
				lIncludeArchiviati, codIncarico, DateUtils.getDate(dataDal, "dd/MM/yyyy"),
				DateUtils.getDate(dataAl, "dd/MM/yyyy"));

		String lReturnPage = "";

		// Estrazione model Soggetto e relativo inserimento nella request.
		// Utile per la JSP SintesiSoggetto.jsp
		if (lFascicoliSoggetti != null) {
			SoggettoModel lSoggetto = ((FascicoloSoggAttModel) lFascicoliSoggetti.get(0)).getSoggettoModel();
			setRequestAttribute("soggetto", lSoggetto);
		}

		// Settaggio dei criteri di ricerca.
		setRequestAttribute("ufUtConnesso", ufUtConnesso);
		setRequestAttribute("lIncludeArchiviati", lIncludeArchiviati);
		setRequestAttribute("codIncarico", codIncarico);
		setRequestAttribute("descrIncarico", descrIncarico);
		setRequestAttribute("dataDal", dataDal);
		setRequestAttribute("dataAl", dataAl);
		setRequestAttribute("tipoUfficio", tipoUfficio);

		// Setta la risposta nella request
		setRequestAttribute("fascicoli", lFascicoliSoggetti);

		lReturnPage = ICostantiFascicoloSiepe.PG_RICERCAPROCSIEPEDELSOGGETTO;

		// Bottone di ritorno
		setLinkRitorno();

		return lReturnPage; // restituisce la jsp di VIEW
	}
}
