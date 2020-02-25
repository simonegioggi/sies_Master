package siap.sige.beneficio.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import siap.siep.beneficio.action.ICostantiBeneficio;
import siap.siep.beneficio.controller.IBeneficio;
import siap.siep.beneficio.model.BeneficioModel;
import siap.siep.beneficio.model.BeneficioPenaAccessoriaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.beneficio.model.BeneficioSigeModel;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActRicercaBeneficioSige
 * </p>
 * <p>
 * Description: Classe Action per la ricerca dei Benefici legati a Sentenza Sige
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActRicercaBeneficioSige extends ActionSige {

	public String processRequest() throws Exception {

		String lPage = ICostantiBeneficio.PG_RICERCABENEFICIO;

		// Gestione ritorno
		setLinkRitorno();

		// Lettura dalla request dell'ID dell'aggregato FascicoloSige_Sentenza dalla sessione
		BigDecimal lIdFasSigeSen = getIdFasSigeSentenzaInSessione();

		// Beneficio Sige per il passaggio della condizione di ricerca
		BeneficioSigeModel lBenMod = new BeneficioSigeModel();
		lBenMod.setFasSigeSenId(lIdFasSigeSen);
		// Ricerca dei Benefici
		IBeneficio lCtrl = SIEPLookupRemote.getBeneficioRemote();
		Vector lVect = lCtrl.ExRicercaBeneficio(lBenMod);
		// Costruzione del Vector <BeneficioPenaaccessoria>
		Vector lPenBenVec = new Vector();
		BeneficioPenaAccessoriaModel lBenPenMod = null;
		if (lVect != null) {
			Iterator itx = lVect.iterator();
			while (itx.hasNext()) {
				lBenPenMod = new BeneficioPenaAccessoriaModel();
				BeneficioModel lBenModel = (BeneficioModel) itx.next();
				lBenPenMod.setBeneficio(lBenModel);
				lPenBenVec.add(lBenPenMod);
			}
		}
		setRequestAttribute("beneficiopenaaccessoria", lPenBenVec);
		setRequestAttribute("modalita", "R");
		setRequestAttribute("modo", "SIGE");
		modificabilita();

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