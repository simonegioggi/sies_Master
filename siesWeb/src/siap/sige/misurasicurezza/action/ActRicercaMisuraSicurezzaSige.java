package siap.sige.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.SIGEException;
import siap.sige.misurasicurezza.model.MisuraSicurezzaSigeModel;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActRicercaMisuraSicurezzaSige
 * </p>
 * <p>
 * Description: Classe Action per la ricerca delle Misure di Sicurezza legata a Sentenza Sige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Agile
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActRicercaMisuraSicurezzaSige extends ActionSige {

	public String processRequest() throws Exception {

		List lListMis = new ArrayList();
		String lPage = ICostantiMisuraSicurezza.PG_RICERCAMISURASICUREZZA;

		// Gestione ritorno
		setLinkRitorno();

		// Lettura dalla request dell'ID dell'aggregato FascicoloSige_Sentenza dalla sessione
		BigDecimal lIdFasSigeSen = getIdFasSigeSentenzaInSessione();

		// MisuraSicurezza Sige per il passaggio della condizione di ricerca
		MisuraSicurezzaSigeModel lMisura = new MisuraSicurezzaSigeModel();
		lMisura.setFasSigeSenId(lIdFasSigeSen);
		IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();

		try {
			lListMis = lCtrl.ExRicercaMisuraSicurezza(lMisura);
		} catch (Exception e) {
			throw new SIGEException(SIGEException.USER_MESSAGE, e.getMessage());
		}
		setRequestAttribute("modalita", "R");
		setRequestAttribute("modo", "SIGE");
		modificabilita();
		setRequestAttribute("misurasicurezza", lListMis);

		return lPage;
	}

	/**
	 * La modificabilità del Fascicolo Sige in sessione abilità la funzione di cancellazione e di inserimento.
	 * 
	 * @throws Exception
	 */
	private void modificabilita() throws Exception {

		// Modificabilità della Pena Accessoria
		String lModificabile = "SI";

		if (IsFascicoloSigeModificabile())
			lModificabile = "SI";
		else
			lModificabile = "NO";

		setRequestAttribute("Cancellabile", lModificabile);
		setRequestAttribute("Modificabile", lModificabile);

	}

}