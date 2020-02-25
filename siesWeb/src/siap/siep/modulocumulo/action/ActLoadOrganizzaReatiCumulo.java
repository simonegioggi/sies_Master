package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import siap.siep.cumulo.action.ICostantiCumulo;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadOrganizzaReatiCumulo
 * </p>
 * <p>
 * Description: Classe Action per la load Organizzazione Reati
 * </p>
 * <p>
 * in ambito Cumulo (legato al titolo Cumulato)
 * </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadOrganizzaReatiCumulo extends ActionModuloCumulo
		implements ICostantiReatoCumulo, ICostantiCumulo {

	public String processRequest() throws Exception {

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		BigDecimal lIdTitolo = getRequestBigDecimalParameter(
				ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);

		ReatoCumuloModel lReaCumMod = new ReatoCumuloModel();
		lReaCumMod.setTitIdTitoloCumulato(lIdTitolo);
		IReatoCumulo lCtrl = SIEPLookupRemote.getReatoCumuloRemote();
		Vector lVect = lCtrl.ExRicercaReatoCumulo(lReaCumMod);

		setRequestAttribute("reaticum", lVect);
		setRequestAttribute("stringanorma", getStringheNorme(lVect));
		setRequestAttribute("stringareatocum", getStringheReato(lVect));

		return PG_ORGANIZZAREATI_CUM;
	}

	/**
	 * FIXME verificare se fa le stesso cose di ReatoContinuazioneCumuloController.getStringheReati
	 * 
	 * @param inVect
	 * @return
	 */
	public Vector getStringheNorme(Vector inVect) {

		Vector outVect = new Vector();
		String strReato = new String("");
		boolean lFlagAnnoNumero;

		Iterator itx = inVect.iterator();
		while (itx.hasNext()) {
			strReato = "";

			ReatoCumuloModel lReato = (ReatoCumuloModel) itx.next();

			lFlagAnnoNumero = false;
			if (lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().toString().equals("")
					&& lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals("")) {
				lFlagAnnoNumero = true;
			}

			if (lFlagAnnoNumero) {
				if (lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("")
						&& !lReato.getDescrFonte().equals("-"))
					strReato += lReato.getDescrFonte() + " ";
				if (lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().toString().equals(""))
					strReato += lReato.getAnnoFonte();
				if (lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
					strReato += "/" + lReato.getNumeroFonte();
			}

			if (lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
				strReato += " art." + lReato.getArticolo();
			if (lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("")
					&& !lReato.getDescrSottonumerazione().equals("-"))
				strReato += " " + lReato.getDescrSottonumerazione();

			if (!lFlagAnnoNumero) {
				if (lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("")
						&& !lReato.getDescrFonte().equals("-"))
					strReato += " " + lReato.getDescrFonte();
			}

			if (lReato.getComma() != null && !lReato.getComma().equals(""))
				strReato += " c. " + lReato.getComma();
			if (lReato.getLettera() != null && !lReato.getLettera().equals(""))
				strReato += " l. " + lReato.getLettera();
			if (lReato.getNumero() != null && !lReato.getNumero().equals(""))
				strReato += " n. " + lReato.getNumero();

			outVect.addElement(strReato.trim());

		} // Chiude While

		return outVect;

	} // Chiude Vector getStringheNorme

	public Vector getStringheReato(Vector inVect) {

		Vector outVect = new Vector();
		String str = new String("");

		Iterator itx = inVect.iterator();
		while (itx.hasNext()) {
			ReatoCumuloModel reato = (ReatoCumuloModel) itx.next();

			if (reato.getProgrCircostanza().intValue() == 1) {
				str = "";
				if (reato.getDescrTipoReato() != null && !reato.getDescrTipoReato().equals("")
						&& !reato.getDescrTipoReato().equals("-"))
					str += "Tipo: " + reato.getDescrTipoReato() + " - ";

				if (reato.getDescLuogo() != null && !reato.getDescLuogo().equals(""))
					str += "Luogo: " + reato.getDescLuogo() + " - ";

				if (reato.getCodPeriodoConsumazione() != null && !reato.getCodPeriodoConsumazione().equals("")
						&& !reato.getCodPeriodoConsumazione().equals("-"))
					str += reato.getStringaConsumazione() + " - ";

				if (reato.getNote() != null && !reato.getNote().equals(""))
					str += "Note: " + reato.getNote();

				if (str.endsWith(" - "))
					str = str.substring(0, str.length() - 3);

				if (str == "")
					str = " ";

				outVect.addElement(str);
			}

		} // chiude Vector getStringheReato(Vector inVect)

		return outVect;
	}

}