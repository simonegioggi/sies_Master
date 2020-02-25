package siap.sige.udienzamonocratica.action;

import java.util.HashSet;
import java.util.Set;

import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.web.ActionSige;

/**
* <p>Title: ActLoadInserisciUdienzaMonocraticaSige</p>
* <p>Description: Classe Action per la load inserisci di UdienzaMonocraticaSige</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActUdienzaMonocraticaSige extends ActionSige implements ICostantiUdienzaMonocraticaSige, ICostantiCollegio {

	protected String getInsViewJSP() throws Exception {
		String lPage = PG_LOAD_INSERISCIUDIENZAMONOCRATICASIGE;

		if (!isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
			// link dalla pagina di fissazione udienza
			lPage = PG_LOAD_INSERISCIUDIENZAMONOCRATICASIGE_FIX;
		}

		return lPage;
	}

	protected String getTipoUfficio() throws Exception {
		String lTipoUfficio = new String("");

		Set<String> filterPGCAP = new HashSet<String>();
		Set<String> filterGUP = new HashSet<String>();
		Set<String> filterPM = new HashSet<String>();
		Set<String> filterPMM = new HashSet<String>();

		filterPGCAP.add("CAP");// CAP = Corte appello
		filterPGCAP.add("CASAP");// CASAP = Corte Assise Appello
		filterPGCAP.add("CAPSM"); // CAPSM = Sezione per i Minorenni Corte di appello

		filterGUP.add("GUPM");// GUPM = GUP Tribunale per i Minorenni

		filterPM.add("CAS"); // CAS = Corte Assise
		filterPM.add("DIB"); // DIB = Tribunale
		filterPM.add("GIP"); // GIP = Gip Presso il Tribunale Ordinario
		// filterPM.add("GIPMI"); // GIPMI = Gip presso il Tribunale Militare
		// filterPM.add("GIPP"); // GIPP = Gip Pretura Circondariale
		// filterPM.add("GIPPSD"); // GIPPSD = Gip presso Sezione Distaccara della Pretura Circondariale
		filterPM.add("GIPM");// GIPM = GIP Tribunale per i Minorenni
		filterPM.add("TRIBSD");// ???

		filterPMM.add("DIBM");// DIBM = Tribunale per i Minorenni

		if (filterPGCAP.contains(getUfficioUtenteConnesso().getCodTipoUfficio())) {
			lTipoUfficio = "PGCAP";
		} else if (filterGUP.contains(getUfficioUtenteConnesso().getCodTipoUfficio())) {
			// lCodTipoUfficio = "GUP";
		} else if (filterPM.contains(getUfficioUtenteConnesso().getCodTipoUfficio())) {
			lTipoUfficio = "PM";
		} else if (filterPMM.contains(getUfficioUtenteConnesso().getCodTipoUfficio())) {
			lTipoUfficio = "PMM";
		}

		return lTipoUfficio;
	}

}