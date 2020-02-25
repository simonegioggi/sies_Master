package siap.sige.udienzacollegiale.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.collegioesperto.model.CollegioEspertoModel;
import siap.sige.collegiogiudicepopolare.model.CollegioGiudicePopolareModel;
import siap.sige.collegiomagistrato.model.CollegioMagistratoModel;
import siap.sige.giudicepopolare.action.ICostantiGiudicePopolare;
import siap.sige.magistrato.action.ICostantiMagistrato;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.web.ActionSige;
import siap.sius.esperto.action.ICostantiEsperto;
import f3b.util.DateUtils;
import f3b.util.F3BException;

public class ActUdienzaCollegiale extends ActionSige implements ICostantiUdienzaCollegiale, ICostantiCollegio {

	protected CollegioModel lColMod = new CollegioModel();

	/**
	 * Lettura dei dati Magistrati dalla request.
	 * <p>
	 * @throws F3BException Propaga errore di eccezione.
	 */
	protected void letturaDatiMagistrati(String modalita) throws F3BException {
		if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)) {
			String[] lCodMagistrati = getRequestStringParameters(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);

			ArrayList <CollegioMagistratoModel>lArrayList = new ArrayList<CollegioMagistratoModel>();
			for (int i = 0; i < lCodMagistrati.length; i++) {
				if (!lCodMagistrati[i].equals("")) {
					CollegioMagistratoModel lModel = new CollegioMagistratoModel();
					lModel.setColIdCollegio(lColMod.getIdCollegio());
					lModel.setMagCodMagistrato(lCodMagistrati[i]);
					lModel.setCodOperatoreInserimento(getCodUtenteConnesso());
					lModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					lModel.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
					lModel.setDataInserimento(DateUtils.getSysDate());
					lModel.setUdiIdUdienzaSige(getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE));

					lArrayList.add(lModel);
				}
			}

			if (lArrayList.size() != 0)
				lColMod.setCollegioMagistrati((CollegioMagistratoModel[]) lArrayList.toArray(new CollegioMagistratoModel[0]));

		} // end first if
	}

	/**
	 * Lettura dei dati GiudiciPopolari dalla request.
	 * <p>
	 * @throws F3BException Propaga errore di eccezione.
	 */
	protected void letturaDatiGiudiciPopolari() throws F3BException {
		if (!isRequestParameterNullObj(ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE)) {
			String[] lIdGiudiciPopolari = getRequestStringParameters(ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE);

			ArrayList <CollegioGiudicePopolareModel>lArrayList = new ArrayList<CollegioGiudicePopolareModel>();
			for (int i = 0; i < lIdGiudiciPopolari.length; i++) {
				if (!lIdGiudiciPopolari[i].equals("")) {
					CollegioGiudicePopolareModel lModel = new CollegioGiudicePopolareModel();
					lModel.setColIdCollegio(lColMod.getIdCollegio());
					lModel.setGiuPopIdGiudicePopolare(new BigDecimal(lIdGiudiciPopolari[i]));
					lModel.setCodOperatoreInserimento(getCodUtenteConnesso());
					lModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					lModel.setDataInserimento(DateUtils.getSysDate());

					lArrayList.add(lModel);
				}
			}

			if (lArrayList.size() != 0)
				lColMod.setCollegioGiudiciPopolari((CollegioGiudicePopolareModel[]) lArrayList.toArray(new CollegioGiudicePopolareModel[0]));

		}// end first if
	}

	/**
	 * Lettura dei dati Esperti dalla request.
	 * <p>
	 * @throws F3BException Propaga errore di eccezione.
	 */
	protected void letturaDatiEsperti() throws F3BException {
		if (!isRequestParameterNullObj(ICostantiEsperto.CAMPO_ID_ESPERTO)) {
			String[] lIdEsperti = getRequestStringParameters(ICostantiEsperto.CAMPO_ID_ESPERTO);

			ArrayList <CollegioEspertoModel>lArrayList = new ArrayList<CollegioEspertoModel>();
			for (int i = 0; i < lIdEsperti.length; i++) {
				if (!lIdEsperti[i].equals("")) {
					CollegioEspertoModel lModel = new CollegioEspertoModel();
					lModel.setColIdCollegio(lColMod.getIdCollegio());
					lModel.setEspIdEsperto(new BigDecimal(lIdEsperti[i]));
					lModel.setCodOperatoreInserimento(getCodUtenteConnesso());
					lModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					lModel.setDataInserimento(DateUtils.getSysDate());

					lArrayList.add(lModel);
				}
			}

			if (lArrayList.size() != 0)
				lColMod.setCollegioEsperti((CollegioEspertoModel[]) lArrayList.toArray(new CollegioEspertoModel[0]));

		}// end first if
	}

	//
	//
	//
	protected String getTipoUfficio() throws Exception {
		String lTipoUfficio = "";

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
		// filterPM.add("GIPM");// GIPM = GIP Tribunale per i Minorenni

		filterPMM.add("DIBM");// DIBM = Tribunale per i Minorenni

		if (filterPGCAP.contains(getUfficioUtenteConnesso().getCodTipoUfficio())) {
			lTipoUfficio = "PGCAP";
		} else if (filterGUP.contains(getUfficioUtenteConnesso().getCodTipoUfficio())) {
			// lCodTipoUfficio = "GUP";
			// Modifica del 29/11/2016 MEV_15_S4 (richiesta Michele)
			// l'ufficio GUPM deve essere collegato alla tabella Pubblici Ministeri della
			// Procura dei Minori (PMM)
			lTipoUfficio = "PMM";
		} else if (filterPM.contains(getUfficioUtenteConnesso().getCodTipoUfficio())) {
			lTipoUfficio = "PM";
		} else if (filterPMM.contains(getUfficioUtenteConnesso().getCodTipoUfficio())) {
			lTipoUfficio = "PMM";
		}

		return lTipoUfficio;
	}

	protected ArrayList<String> getMagistratiArray(int tipo) {
		ArrayList<String> ar = new ArrayList<String>();

		switch (tipo) {
		case 1:
			ar.add("Presidente");
			ar.add("Consigliere");
			ar.add("Consigliere");
			break;
		case 2:
			ar.add("Presidente");
			ar.add("Giudice Onorario");
			ar.add("Giudice Onorario");
			break;
		case 3:
			ar.add("Presidente");
			ar.add("Giudice Relatore");
			ar.add("Giudice Onorario");
			ar.add("Giudice Onorario");
			break;
		case 4:
			ar.add("Presidente");
			ar.add("Giudice Relatore");
			break;
		case 5:
			ar.add("Presidente");
			ar.add("Consigliere");
			ar.add("Consigliere");
			ar.add("Giudice Onorario");
			ar.add("Giudice Onorario");
			break;
		}

		return ar;
	}

	private String fixUdienza(String uff) throws Exception {
		String lPage = "";

		// PG_LOAD_INSERISCIUDIENZACOLLEGIALE_CAP - TA
		// PG_LOAD_INSERISCIUDIENZACOLLEGIALE_GUPM - TB
		// PG_LOAD_INSERISCIUDIENZACOLLEGIALE_DIBM - TC
		// PG_LOAD_INSERISCIUDIENZACOLLEGIALE_CAS - TD
		// PG_LOAD_INSERISCIUDIENZACOLLEGIALE_CAPSM - TE
		ArrayList<String> magArr = null;
		String procDesc = null;
		String giudPop = null;

		// *** tipo 1
		if (uff.equalsIgnoreCase("CAP")) {
			// CAP = Corte appello
			lPage = PG_LOAD_INSERISCIUDIENZACOLLEGIALE_CAP;
			magArr = getMagistratiArray(1);
			procDesc = "Procuratore Generale";
		} else if (uff.equalsIgnoreCase("DIB")) {
			// DIB = Tribunale
			lPage = PG_LOAD_INSERISCIUDIENZACOLLEGIALE_DIB;
			magArr = getMagistratiArray(1);
			procDesc = "Procuratore della Repubblica";

			// *** tipo 2
		} else if (uff.equalsIgnoreCase("GUPM")) {// GUPM = GUP Tribunale per i Minorenni
			lPage = PG_LOAD_INSERISCIUDIENZACOLLEGIALE_GUPM;
			magArr = getMagistratiArray(2);
			//procDesc = "Procuratore Generale";
			// Modifica del 29/11/2016 MEV_15_S4 (richiesto da Michele)
			procDesc = "Procuratore della Repubblica";

			// *** tipo 3
		} else if (uff.equalsIgnoreCase("DIBM")) {
			// DIBM = Tribunale per i Minorenni
			lPage = PG_LOAD_INSERISCIUDIENZACOLLEGIALE_DIBM;
			magArr = getMagistratiArray(3);
			procDesc = "Procuratore della Repubblica";

			// *** tipo 4
		} else if (uff.equalsIgnoreCase("CAS")) {
			// CAS = Corte Assise
			lPage = PG_LOAD_INSERISCIUDIENZACOLLEGIALE_CAS;
			magArr = getMagistratiArray(4);
			giudPop = "yes";

		} else if (uff.equalsIgnoreCase("CASAP")) {
			// CASAP = Corte Assise Appello
			lPage = PG_LOAD_INSERISCIUDIENZACOLLEGIALE_CASAP;
			magArr = getMagistratiArray(4);
			giudPop = "yes";

			// *** tipo 5
		} else if (uff.equalsIgnoreCase("CAPSM")) {
			// CAPSM = Sezione per i Minorenni Corte di appello
			lPage = PG_LOAD_INSERISCIUDIENZACOLLEGIALE_CAPSM;
			magArr = getMagistratiArray(5);
			procDesc = "Procuratore Generale";

		} else {
			throw new F3BException(F3BException.USER_MESSAGE, "Funzione non gestita dal tipo ufficio di competenza.");
		}

		setRequestAttribute("magistratiArray", magArr);
		setRequestAttribute("procuraDesc", procDesc);
		setRequestAttribute("giudiciPopolari", giudPop);
		return lPage;
	}

	private String funzAmm(String uff) throws Exception {
		String lPage = "";

		// PG_LOAD_INSERISCIUDIENZACOLLEGIALECAP --> CAP
		// PG_LOAD_INSERISCIUDIENZACOLLEGIALECASAP --> CASAP
		// PG_LOAD_INSERISCIUDIENZACOLLEGIALECAS --> CAS
		// PG_LOAD_INSERISCIUDIENZACOLLEGIALEDIB --> DIB
		// PG_LOAD_INSERISCIUDIENZACOLLEGIALECAPSM --> CAPSM
		// PG_LOAD_INSERISCIUDIENZACOLLEGIALEDIBM --> DIBM

		if (uff.equalsIgnoreCase("CAP"))// CAP = Corte appello
			lPage = PG_LOAD_INSERISCIUDIENZACOLLEGIALECAP;
		else if (uff.equalsIgnoreCase("DIB"))// DIB = Tribunale
			lPage = PG_LOAD_INSERISCIUDIENZACOLLEGIALEDIB;
		else if (uff.equalsIgnoreCase("DIBM")|| uff.equalsIgnoreCase("GUPM"))// DIBM = Tribunale per i Minorenni
			lPage = PG_LOAD_INSERISCIUDIENZACOLLEGIALEDIBM;
		else if (uff.equalsIgnoreCase("CAS"))// CAS = Corte Assise
			lPage = PG_LOAD_INSERISCIUDIENZACOLLEGIALECAS;
		else if (uff.equalsIgnoreCase("CASAP"))// CASAP = Corte Assise Appello
			lPage = PG_LOAD_INSERISCIUDIENZACOLLEGIALECASAP;
		else if (uff.equalsIgnoreCase("CAPSM"))// CAPSM = Sezione per i Minorenni Corte di appello
			lPage = PG_LOAD_INSERISCIUDIENZACOLLEGIALECAPSM;
		else
			throw new F3BException(F3BException.USER_MESSAGE, "Funzione non gestita dal tipo ufficio di competenza.");

		return lPage;
	}

	//
	//
	//
	protected String getInsViewJSP() throws Exception {
		String lPage = "";

		if (!isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
			// link dalla pagina di fissazione udienza
			lPage = fixUdienza(getUfficioUtenteConnesso().getCodTipoUfficio());
		} else {
			// link dalla pagina delle funzioni amministrative
			lPage = funzAmm(getUfficioUtenteConnesso().getCodTipoUfficio());
		}

		return lPage;
	}
	
	
	/**
	 * Lettura dei dati Magistrati Assegnatari dei fascicoli 
	 * metodo introdotto per 11.2.1
	 * 
	 * <p>
	 * @throws F3BException Propaga errore di eccezione.
	 */
	protected Map<String, String> letturaDatiMagistratiAssegnatari() throws F3BException {
		
		Map<String, String> mapFascMagAss = new HashMap<String, String>();
		String[] lCodFascicoliChecked = null;
		String[] lCodMagAss  = null;
		
		if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO_ASS)) {
			lCodMagAss = getRequestStringParameters(ICostantiMagistrato.CAMPO_COD_MAGISTRATO_ASS);
		}
		
		if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_CHECK_MAGISTRATO_ASS)) {
			lCodFascicoliChecked = getRequestStringParameters(ICostantiMagistrato.CAMPO_CHECK_MAGISTRATO_ASS);
			
			for (int i = 0; i < lCodFascicoliChecked.length; i++) {
				
				if (!lCodFascicoliChecked[i].equals("")) {
					String idFascicoloSige = lCodFascicoliChecked[i];
					String codMag = lCodMagAss[i];
					mapFascMagAss.put(idFascicoloSige, codMag);
				}
			}
		}			
		return mapFascMagAss;			
	}

}