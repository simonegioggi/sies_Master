package siap.sige.collegio.action;

import java.math.BigDecimal;
import java.util.ArrayList;

import siap.sico.web.ActionSiap;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.collegioesperto.model.CollegioEspertoModel;
import siap.sige.collegiogiudicepopolare.model.CollegioGiudicePopolareModel;
import siap.sige.collegiomagistrato.model.CollegioMagistratoModel;
import siap.sige.giudicepopolare.action.ICostantiGiudicePopolare;
import siap.sige.magistrato.action.ICostantiMagistrato;
import siap.sius.esperto.action.ICostantiEsperto;
import f3b.util.DateUtils;
import f3b.util.F3BException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActionCollegio extends ActionSiap implements ICostantiCollegio {

	CollegioModel lColMod = new CollegioModel();

	/**
	 * Lettura dei dati Magistrati dalla request.
	 * <p>
	 * 
	 * @throws F3BException
	 *             Propaga errore di eccezione.
	 */
	protected void letturaDatiMagistrati() throws F3BException {

		if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO)) {
			String[] lCodMagistrati = getRequestStringParameters(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);

			ArrayList lArrayList = new ArrayList();
			for (int i = 0; i < lCodMagistrati.length; i++) {
				if (!lCodMagistrati[i].equals("")) {
					CollegioMagistratoModel lModel = new CollegioMagistratoModel();
					lModel.setColIdCollegio(lColMod.getIdCollegio());
					lModel.setMagCodMagistrato(lCodMagistrati[i]);
					lModel.setCodOperatoreInserimento(getCodUtenteConnesso());
					lModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					lModel.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
					lModel.setDataInserimento(DateUtils.getSysDate());

					lArrayList.add(lModel);
				}
			}

			if (lArrayList.size() != 0)
				lColMod.setCollegioMagistrati(
						(CollegioMagistratoModel[]) lArrayList.toArray(new CollegioMagistratoModel[0]));

		} // end first if
	}

	/**
	 * Lettura dei dati GiudiciPopolari dalla request.
	 * <p>
	 * 
	 * @throws F3BException
	 *             Propaga errore di eccezione.
	 */
	protected void letturaDatiGiudiciPopolari() throws F3BException {

		if (!isRequestParameterNullObj(ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE)) {
			String[] lIdGiudiciPopolari = getRequestStringParameters(
					ICostantiGiudicePopolare.CAMPO_ID_GIUDICE_POPOLARE);

			ArrayList lArrayList = new ArrayList();
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
				lColMod.setCollegioGiudiciPopolari((CollegioGiudicePopolareModel[]) lArrayList
						.toArray(new CollegioGiudicePopolareModel[0]));

		} // end first if
	}

	/**
	 * Lettura dei dati Esperti dalla request.
	 * <p>
	 * 
	 * @throws F3BException
	 *             Propaga errore di eccezione.
	 */
	protected void letturaDatiEsperti() throws F3BException {

		if (!isRequestParameterNullObj(ICostantiEsperto.CAMPO_ID_ESPERTO)) {
			String[] lIdEsperti = getRequestStringParameters(ICostantiEsperto.CAMPO_ID_ESPERTO);

			ArrayList lArrayList = new ArrayList();
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
				lColMod.setCollegioEsperti(
						(CollegioEspertoModel[]) lArrayList.toArray(new CollegioEspertoModel[0]));

		} // end first if
	}

	//
	//
	//
	public String getInsViewJSP() throws Exception {

		String lPage = "";
		String uff = getUfficioUtenteConnesso().getCodTipoUfficio();

		if (uff.equalsIgnoreCase("CAP"))
			lPage = PG_LOAD_INSERISCICOLLEGIOCAP;
		else if (uff.equalsIgnoreCase("CAS"))
			lPage = PG_LOAD_INSERISCICOLLEGIOCAS;
		else if (uff.equalsIgnoreCase("CASAP"))
			lPage = PG_LOAD_INSERISCICOLLEGIOCASAP;
		else if (uff.equalsIgnoreCase("DIB"))
			lPage = PG_LOAD_INSERISCICOLLEGIODIB;
		else if (uff.equalsIgnoreCase("DIBM"))
			lPage = PG_LOAD_INSERISCICOLLEGIODIBM;
		else if (uff.equalsIgnoreCase("CAPSM"))
			lPage = PG_LOAD_INSERISCICOLLEGIOCAPSM;
		else
			throw new F3BException(F3BException.USER_MESSAGE,
					"Funzione non gestita dal tipo ufficio di competenza.");

		return lPage;
	}

}