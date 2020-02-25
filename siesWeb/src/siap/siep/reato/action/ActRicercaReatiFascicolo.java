package siap.siep.reato.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.util.StringUtils;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.FascicoloSiepController;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.reato.controller.ReatoController;
import siap.siep.reato.model.ReatoModel;

/**
 * <p>
 * Title: ActRicercaReato
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Reato
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
public class ActRicercaReatiFascicolo extends ActionSiap
		implements ICostantiReato, siap.siep.fascicolo.action.ICostantiFascicoloSiep {

	public String processRequest() throws Exception {

		// FascicoloSiepModel lFasMod = new FascicoloSiepModel();
		// IFascicoloSiep lCtrlF = SIEPLookupRemote.getFascicoloSiepRemote();
		// lFasMod = lCtrlF.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

		FascicoloSiepModel lFasMod = new FascicoloSiepModel();
		BigDecimal idFascicolo = null;

		lFasMod.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));
		lFasMod.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));
		lFasMod.setChiaveUfficio(this.getCodUfficioUtenteConnesso());

		FascicoloSiepController lCtrlF = new FascicoloSiepController();
		idFascicolo = lCtrlF.ExRicercaIDFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

		String notFound = "";

		if (idFascicolo == null) {
			notFound = "F";
			setRequestAttribute("notFound", notFound);
		} else {

			Vector lVect = new Vector();

			try {

				// IReato lCtrl = SIEPLookupRemote.getReatoRemote();
				ReatoController lCtrl = new ReatoController();
				lVect = lCtrl.ExRicercaReatiNoCircostanzaByFascicolo(idFascicolo);

			} catch (F3BException e) {

				if (e.getMessage().equalsIgnoreCase("Nessun Elemento trovato")) {

					notFound = "R";
				} else {

					throw e;
				}
			}

			setRequestAttribute("notFound", notFound);
			String formName = getRequestStringParameter("formname");
			if (notFound.equals("")) {

				setRequestAttribute("Formdipartenza", formName);
				setRequestAttribute("stringacampi", getPopupCampi(lVect));
				setRequestAttribute("stringareati", getPopupReati(lVect));
				setRequestAttribute("reati", lVect);
			}
		}

		return PG_RICERCAREATIFASCICOLO;
	}

	private Vector getPopupCampi(Vector inVect) {

		Vector outVect = new Vector();
		String str = new String("");

		Iterator itx = inVect.iterator();
		while (itx.hasNext()) {

			ReatoModel lReato = (ReatoModel) itx.next();

			if (lReato.getProgrCircostanza().intValue() == 1) {

				if (str.length() != 0) {

					str = str.substring(0, str.length() - SEP_NORME.length());
					outVect.addElement(str);
					str = "";
				}
			}

			str += lReato.getCodFonte();
			str += SEP_CAMPI + StringUtils.toStringJSP(lReato.getAnnoFonte());
			str += SEP_CAMPI + StringUtils.toStringJSP(lReato.getNumeroFonte());
			str += SEP_CAMPI + StringUtils.toStringJSP(lReato.getArticolo());
			str += SEP_CAMPI + lReato.getCodSottonumerazione();
			str += SEP_CAMPI + StringUtils.toStringJSP(lReato.getComma());
			// ***************************************************************
			// Federica - a9-rr-078
			// aggiunto campo Comma-Qualificante
			str += SEP_CAMPI + lReato.getCommaQualificante();
			// ***************************************************************
			str += SEP_CAMPI + StringUtils.toStringJSP(lReato.getLettera());
			str += SEP_CAMPI + StringUtils.toStringJSP(lReato.getNumero());
			str += SEP_NORME;
		}

		str = str.substring(0, str.length() - SEP_NORME.length());
		outVect.addElement(str);

		return outVect;
	}

	private Vector getPopupReati(Vector inVect) {

		Vector outVect = new Vector();
		String strReato = new String("");
		boolean lFlagAnnoNumero;

		Iterator itx = inVect.iterator();
		while (itx.hasNext()) {

			ReatoModel lReato = (ReatoModel) itx.next();

			if (lReato.getProgrCircostanza().intValue() == 1) {

				if (strReato.length() != 0) {

					outVect.addElement(strReato);
					strReato = "";
				}
			}

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

			strReato += ", ";
		}

		outVect.addElement(strReato);

		return outVect;
	}

}