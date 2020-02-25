package siap.siep.altrigradigiudizio.action;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.agdgfascicolosiep.model.AgdgFascicoloSiepModel;
import siap.siep.altrigradigiudizio.controller.IAltriGradiGiudizio;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActAssociaFascicoloSentenzaRiunita
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento della relazione tra Fascicolo e SentenzaRiunita
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActAssociaFascicoloAltriGradiGiudizio extends ActionSiap implements ICostantiAltriGradiGiudizio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento della relazione tra Fascicolo e AltroGradoGiudizio
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFasc = new FascicoloSiepModel(
				(FascicoloSiepModel) getSessionAttribute("fascicolo"));
//		SentenzaModel lSen = new SentenzaModel((SentenzaModel) getSessionAttribute("sentenza"));

//		AltriGradiGiudizioModel lSenRiu = new AltriGradiGiudizioModel();
//		AgdgFascicoloSiepModel lagdgFascMod = new AgdgFascicoloSiepModel();

		int numRighe = this.getRequestIntParameter("numagdgfasc");
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.error("TOTALE ALTRI GRADI GIUDIZIO= " + numRighe);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.error("VALORE DI FLAGCOLLEGATO=" + this.getParameterValues("flagcollegato"));

		String[] agdgSelezionate = null;
		HashSet agdgScelti = new HashSet();

		if (!isRequestParameterNullObj("flagcollegato")) {
			agdgSelezionate = this.getParameterValues("flagcollegato");
			for (int x = 0; x < agdgSelezionate.length; x++) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.error("CONTENUTO DEI CHECK!!!" + agdgSelezionate[x].toString());
				agdgScelti.add(agdgSelezionate[x]);
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.error("DOPO if e FOR dentro");

		String idagdg = "";
		Vector idDaAssociare = new Vector();
		Vector idDaDissociare = new Vector();
		for (int i = 0; i < numRighe; i++) {
			idagdg = this.getRequestStringParameter("IdAltriGradiGiudizio" + i);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("CONTENUTO DI idSenRiu=" + idagdg);
			if (agdgScelti.contains(String.valueOf(i))) {
				if (this.getRequestStringParameter("associato" + i).compareTo("checked") != 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.error("RECORD NUOVO da ASSOCIARE");
					idDaAssociare.addElement(idagdg);
				}
			} else {
				if (this.getRequestStringParameter("associato" + i).compareTo("checked") == 0) {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.error("RECORD VECCHIO da DE-ASSOCIARE");
					idagdg = this.getRequestStringParameter("IdAgdgFascicoloSiep" + i);
					idDaDissociare.addElement(idagdg);
				}
			}
		}

		IAltriGradiGiudizio lCtrl = SIEPLookupRemote.getAltriGradiGiudizioRemote();

		Iterator itx = idDaAssociare.iterator();
//		BigDecimal idagdgFasc = null;
		AgdgFascicoloSiepModel lagdgFasSiep = new AgdgFascicoloSiepModel();
		lagdgFasSiep.setFasSieIdFascicoloSiep(lFasc.getIdFascicoloSiep());

		while (itx.hasNext()) {
			idagdg = itx.next().toString();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("CONTENUTO DI idSenRiu per settare il model=" + idagdg);
			if (!idagdg.equals(null) && !idagdg.equals(""))
				lagdgFasSiep.setAgdgIdAltrigradigiudizio(new BigDecimal(idagdg));
			lCtrl.ExAggiornaAltriGradiGiudizioFascicolo(lagdgFasSiep, 1);
		}

		Iterator itx2 = idDaDissociare.iterator();
		while (itx2.hasNext()) {
			idagdg = itx2.next().toString();
			if (!idagdg.equals(null) && !idagdg.equals(""))
				lagdgFasSiep.setIdAgdgFascicoloSiep(new BigDecimal(idagdg));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.error("2 CONTENUTO DI idSenRiu per settare il model=" + idagdg);
			lCtrl.ExAggiornaAltriGradiGiudizioFascicolo(lagdgFasSiep, 2);
		}
		// setRequestAttribute("sentenzariunita", lSenRet);

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
				+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
				+ lFasc.getIdFascicoloSiep().toString();

		return lPage;
	}

}