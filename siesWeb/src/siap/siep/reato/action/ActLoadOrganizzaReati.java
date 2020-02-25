package siap.siep.reato.action;

import java.util.Iterator;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.reato.controller.ReatoContinuazioneController;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActLoadModificaContinuazioneReati
 * </p>
 * <p>
 * Description: Classe Action per la load di Modifica Continuazione Reati
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadOrganizzaReati extends ActionSiap implements ICostantiReato {

	public String processRequest() throws Exception {

		boolean proceed = true;
		FascicoloSiepModel lFascMod = new FascicoloSiepModel();

		lFascMod.setIdFascicoloSiep(((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep());
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		lFascMod = lCtrl.ExRicercaFascicoloByKey(lFascMod.getIdFascicoloSiep());
		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			proceed = false;
			// setta la risposta nella request
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il fascicolo è in stato di ARCHIVIATO/DEFINITO! Impossibile modificare i Reati");
		}
		if (lFascMod.getFlagValidato().equalsIgnoreCase("S")) {
			proceed = false;
			// setta la risposta nella request
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il fascicolo è stato validato! Impossibile modificare i Reati");
		}

//		ReatoModel lMod = new ReatoModel();

		if (proceed) {

			// IReato lRCtrl = SIEPLookupRemote.getReatoRemote();

			// Vector lVect = lRCtrl.ExRicercaReatiNoCircostanzaByFascicolo(lFascMod.getIdFascicoloSiep());

			ReatoContinuazioneController lRCtrl = new ReatoContinuazioneController();
			Vector lVect = lRCtrl.ExRicercaReatiNoCircostanzaByFascicolo(lFascMod.getIdFascicoloSiep());

			setRequestAttribute("reati", lVect);

			setRequestAttribute("stringanorma", getStringheNorme(lVect));
			setRequestAttribute("stringareato", getStringheReato(lVect));
			// setRequestAttribute("table", lRCtrl.getTableContinuazioni(lVect));

			return PG_ORGANIZZAREATI; // restituisce la jsp di VIEW
		} else {
			// Prepara la "pagina" di destinAction
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
					+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE; // restituisce la jsp di VIEW
		}
	}

	public Vector getStringheNorme(Vector inVect) {

		Vector outVect = new Vector();
		String strReato = new String("");
		boolean lFlagAnnoNumero;

		Iterator itx = inVect.iterator();
		while (itx.hasNext()) {

			strReato = "";

			ReatoModel lReato = (ReatoModel) itx.next();

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
			// ***********************************************************************************
			// Federica - a9-rr-078
			// aggiunto campo Comma-Qualificante
			if (lReato.getDescrCommaQualificante() != null && !lReato.getDescrCommaQualificante().equals("")
					&& !lReato.getDescrCommaQualificante().equals("-"))
				strReato += " " + lReato.getDescrCommaQualificante();
			// ***********************************************************************************
			if (lReato.getLettera() != null && !lReato.getLettera().equals(""))
				strReato += " l. " + lReato.getLettera();
			if (lReato.getNumero() != null && !lReato.getNumero().equals(""))
				strReato += " n. " + lReato.getNumero();

			outVect.addElement(strReato.trim());
		}

		return outVect;
	}

	public Vector getStringheReato(Vector inVect) {

		Vector outVect = new Vector();
		String str = new String("");

		Iterator itx = inVect.iterator();
		while (itx.hasNext()) {

			ReatoModel reato = (ReatoModel) itx.next();

			if (reato.getProgrCircostanza().intValue() == 1) {

				str = "";

				if (reato.getDescrTipoReato() != null && !reato.getDescrTipoReato().equals("")
						&& !reato.getDescrTipoReato().equals("-")) {
					str += "Tipo: " + reato.getDescrTipoReato() + " - ";
				}
				if (reato.getDescLuogo() != null && !reato.getDescLuogo().equals("")) {
					str += "Luogo: " + reato.getDescLuogo() + " - ";
				}

				if (reato.getCodPeriodoConsumazione() != null
						&& !reato.getCodPeriodoConsumazione().equals("")
						&& !reato.getCodPeriodoConsumazione().equals("-")) {
					str += reato.getStringaConsumazione() + " - ";
				}

				if (reato.getNote() != null && !reato.getNote().equals("")) {
					str += "Note: " + reato.getNote();
				}

				if (str.endsWith(" - "))
					str = str.substring(0, str.length() - 3);

				if (str == "")
					str = " ";

				outVect.addElement(str);
			}
		}

		return outVect;
	}

}