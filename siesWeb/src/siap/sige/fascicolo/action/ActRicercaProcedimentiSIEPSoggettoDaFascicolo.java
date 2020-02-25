package siap.sige.fascicolo.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.soggetto.model.SoggettoModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Ricerca dei Procedimenti SIEP del Soggetto a Partire dal Dettaglio Fascicolo (in Sessione)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Engineering
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaProcedimentiSIEPSoggettoDaFascicolo extends ActionSige
		implements ICostantiFascicoloSige {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Lettura soggetto.
		SoggettoModel lSogMod = (SoggettoModel) getSessionAttribute("soggetto");
		setRequestAttribute("soggetto", lSogMod);
		// lSogMod.setIdSoggetto(getFascicoloSigeInSessione().getSogIdSoggetto());
		// ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
		// lSogMod = lSogCtrl.ExRicercaSoggettoByKey(getFascicoloSigeInSessione().getSogIdSoggetto());
		lSogMod.setIdSoggetto(new BigDecimal(0));

		// if(lFasEstMod != null && lFasEstMod.getFascicoloSige() != null){
		// lSogMod.setIdSoggetto(lFasEstMod.getFascicoloSige().getSogIdSoggetto());
		// } else {
		// lSogMod.setIdSoggetto(lsoggettoMod.getIdSoggetto());
		// }

		// Parametri di ricerca.
		String ufUtConnesso = getUfficioUtenteConnesso().getCodUfficio();
		String codDistretto = getUfficioUtenteConnesso().getCodDistretto();

		// Si chiama il FascicoloSiepController.
		IFascicoloSiep lFascSogCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

		// Vector lFascicoliSoggetto = lFascSogCtrl.ExRicercaFascSIEPDelSoggetto(lSogMod, ufUtConnesso,
		// codDistretto);
		// Vector lFascicoliSoggetto = lFascSogCtrl.ExRicercaFascicoloSiepBySuperSoggettoPaged(lSogMod,1);

		Vector lFascicoliSoggetti = lFascSogCtrl.ExRicercaFascicoloSiepBySuperSoggettoPaged(lSogMod, 1);

		String lReturnPage = "";
		// Collection di decodifica del COD_STATO
		Collection lCol = DecodificheManager.getInstance().getStatoProcedimento();
		setRequestAttribute("CodStato", lCol);

		// Settaggio dei criteri di ricerca.
		setRequestAttribute("IdSoggetto", lSogMod.getIdSoggetto());
		setRequestAttribute("ufUtConnesso", ufUtConnesso);
		setRequestAttribute("codDistretto", codDistretto);

		// Setta la risposta nella request
		setRequestAttribute("fascicoli", lFascicoliSoggetti);

		lReturnPage = ICostantiFascicoloSige.PG_ELENCO_FASCICOLISIEPDELSOGGETTO;

		return lReturnPage; // restituisce la jsp di VIEW
	}

}