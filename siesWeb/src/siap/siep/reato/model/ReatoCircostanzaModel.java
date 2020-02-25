package siap.siep.reato.model;

import f3b.model.GenericModel;
import f3b.util.DateUtils;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ReatoCircostanzaModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 2010780339972934004L;
	private ReatoModel mReato;
	private ReatoModel[] mCircostanze;

	public ReatoCircostanzaModel() {
		mReato = new ReatoModel();
	}

	public ReatoCircostanzaModel(ReatoModel aReato) {
		mReato = aReato;
	}

	public ReatoCircostanzaModel(ReatoCircostanzaModel aReato) {
		mReato = aReato.getReato();
		mCircostanze = aReato.getCircostanze();
	}

	public String[] getDescPopUp() {
		String[] coppia = new String[2];
		StringBuffer sbText = new StringBuffer();
		StringBuffer sbPopUp = new StringBuffer();

		boolean lFlagAnnoNumero = false;
		String lAppoggio = "";

		if (mReato.getAnnoFonte() != null && !"".equals(mReato.getAnnoFonte().toString())
				&& mReato.getNumeroFonte() != null && !mReato.getNumeroFonte().equals("")) {
			lFlagAnnoNumero = true;
		}

		if (mReato.getProgrNumeroManuale() != null && !mReato.getProgrNumeroManuale().equals("")) {
			lAppoggio = "Reato " + mReato.getProgrNumeroManuale() + ": ";
		} else {
			lAppoggio = "Reato " + mReato.getProgrReato() + ": ";
		}
		sbText.append(lAppoggio);
		sbPopUp.append("<font class=\"label\">" + lAppoggio + "</font>\n");

		sbPopUp.append("<font class=\"campo\">\n");
		lAppoggio = " ";
		if (lFlagAnnoNumero) {
			if (mReato.getDescrFonte() != null && !mReato.getDescrFonte().equals("")
					&& !mReato.getDescrFonte().equals("-"))
				lAppoggio += mReato.getDescrFonte() + " ";
			if (mReato.getAnnoFonte() != null && !"".equals(mReato.getAnnoFonte().toString()))
				lAppoggio += mReato.getAnnoFonte();
			if (mReato.getNumeroFonte() != null && !mReato.getNumeroFonte().equals(""))
				lAppoggio += "/" + mReato.getNumeroFonte();
		}

		if (mReato.getArticolo() != null && !mReato.getArticolo().equals(""))
			lAppoggio += " art." + mReato.getArticolo();
		if (mReato.getDescrSottonumerazione() != null && !mReato.getDescrSottonumerazione().equals("")
				&& !mReato.getDescrSottonumerazione().equals("-"))
			lAppoggio += " " + mReato.getDescrSottonumerazione();

		if (!lFlagAnnoNumero) {
			if (mReato.getDescrFonte() != null && !mReato.getDescrFonte().equals("")
					&& !mReato.getDescrFonte().equals("-"))
				lAppoggio += " " + mReato.getDescrFonte();
		}

		if (mReato.getComma() != null && !mReato.getComma().equals(""))
			lAppoggio += " c. " + mReato.getComma();
		if (mReato.getLettera() != null && !mReato.getLettera().equals(""))
			lAppoggio += " l. " + mReato.getLettera();
		if (mReato.getNumero() != null && !mReato.getNumero().equals(""))
			lAppoggio += " n. " + mReato.getNumero();

		// CIRCOSTANZE
		if (mCircostanze != null) {
			ReatoModel lCirc = null;
			for (int i = 0; i < mCircostanze.length; i++) {
				lCirc = mCircostanze[i];
				lAppoggio += ",";

				boolean lFlagAnnoNumeroCirc = false;
				if (lCirc.getAnnoFonte() != null && !"".equals(lCirc.getAnnoFonte().toString())
						&& lCirc.getNumeroFonte() != null && !lCirc.getNumeroFonte().equals("")) {
					lFlagAnnoNumeroCirc = true;
				}
				if (lFlagAnnoNumeroCirc) {
					if (lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("")
							&& !lCirc.getDescrFonte().equals("-"))
						lAppoggio += lCirc.getDescrFonte() + " ";
					if (lCirc.getAnnoFonte() != null && !"".equals(lCirc.getAnnoFonte().toString()))
						lAppoggio += lCirc.getAnnoFonte();
					if (lCirc.getNumeroFonte() != null && !lCirc.getNumeroFonte().equals(""))
						lAppoggio += "/" + lCirc.getNumeroFonte();
				}

				if (lCirc.getArticolo() != null && !lCirc.getArticolo().equals(""))
					lAppoggio += "art." + lCirc.getArticolo();
				if (lCirc.getDescrSottonumerazione() != null && !lCirc.getDescrSottonumerazione().equals("")
						&& !lCirc.getDescrSottonumerazione().equals("-"))
					lAppoggio += " " + lCirc.getDescrSottonumerazione();

				if (!lFlagAnnoNumeroCirc) {
					if (lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("")
							&& !lCirc.getDescrFonte().equals("-"))
						lAppoggio += lCirc.getDescrFonte();
				}

				if (lCirc.getComma() != null && !lCirc.getComma().equals(""))
					lAppoggio += " c. " + lCirc.getComma();
				if (lCirc.getLettera() != null && !lCirc.getLettera().equals(""))
					lAppoggio += " l. " + lCirc.getLettera();
				if (lCirc.getNumero() != null && !lCirc.getNumero().equals(""))
					lAppoggio += " n. " + lCirc.getNumero();
			}
		} // end if CIRCOSTANZE
		sbText.append(lAppoggio);
		sbPopUp.append(lAppoggio);
		sbPopUp.append("</font>\n");

		lAppoggio = "";
		if (mReato.getStringaConsumazione() != null) {
			lAppoggio = (mReato.getStringaConsumazione()) + ",";
			sbText.append(lAppoggio);
			sbPopUp.append("<font class=\"campo\">" + lAppoggio + "</font>\n");
		}

		if (mReato.getNote() != null && !mReato.getNote().equals("")) {
			lAppoggio = " " + (mReato.getNote()) + " ";
			sbText.append(lAppoggio);
			sbPopUp.append("<font class=\"campo\">" + lAppoggio + "</font>\n");
		}

		if (mReato.getDescLuogo() != null && !mReato.getDescLuogo().equals("")) {
			lAppoggio = (mReato.getDescLuogo());
			sbText.append(" Luogo: " + lAppoggio);

			sbPopUp.append("<font class=\"label\">Luogo</font>\n");
			sbPopUp.append("<font class=\"campo\">" + lAppoggio + "</font>\n");
		}

		if (mReato.getDataReato() != null) {
			lAppoggio = (DateUtils.getDateToString(mReato.getDataReato(), "dd-MM-yyyy"));
			sbText.append(" Data Reato: " + lAppoggio);

			sbPopUp.append("<font class=\"label\">Data Reato</font>\n");
			sbPopUp.append("<font class=\"campo\">" + lAppoggio + "</font>\n");
		} else if (mReato.getDataInizio() != null) {
			lAppoggio = (DateUtils.getDateToString(mReato.getDataInizio(), "dd-MM-yyyy"));
			sbText.append(" Data Reato: " + lAppoggio);
			sbPopUp.append("<font class=\"label\">Data Reato</font>\n");
			sbPopUp.append("<font class=\"campo\">" + lAppoggio + "</font>\n");
		}

		coppia[0] = sbText.toString();
		coppia[1] = sbPopUp.toString();
		return coppia;
	}

	public ReatoModel getReato() {
		return mReato;
	}

	public ReatoModel[] getCircostanze() {
		return mCircostanze;
	}

	public void setReato(ReatoModel aValore) {
		mReato = aValore;
	}

	public void setCircostanze(ReatoModel[] aValore) {
		mCircostanze = aValore;
	}
}