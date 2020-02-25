package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.util.CalendarUtil;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.circostanza.action.ICostantiCircostanza;
import siap.siep.circostanza.controller.ICircostanza;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.ICircostanzaCumulo;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;
import siap.siep.modulocumulo.model.ReatoCircostanzaCumuloModel;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.penacumulo.controller.IPenaCumulo;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.reato.action.ICostantiReato;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;
import siap.siep.statoprocedimento.controller.IStatoProcedimento;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActElencoProcReato
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
 * @author unascribed
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActElencoProcReato extends ActionSiap
		implements ICostantiAnnotazioneManuale, ICostantiReato, ICostantiCircostanza {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	protected void estrapolaDescrizioneReato(ReatoCircostanzaModel lReatoCircostanza) {
		StringBuffer reati = new StringBuffer();

		ReatoModel lReato = lReatoCircostanza.getReato();
		ReatoModel[] lCircostanze = lReatoCircostanza.getCircostanze();

		boolean lFlagAnnoNumero = false;
		if (lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().toString().equals("")
				&& lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals("")) {
			lFlagAnnoNumero = true;
		}

		if (lFlagAnnoNumero) {
			if (lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("")
					&& !lReato.getDescrFonte().equals("-"))
				reati.append(lReato.getDescrFonte() + " ");
			if (lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().toString().equals(""))
				reati.append(lReato.getAnnoFonte());
			if (lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
				reati.append("/" + lReato.getNumeroFonte());
			reati.append(" ");
		}

		if (lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
			reati.append("ART." + lReato.getArticolo());
		if (lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("")
				&& !lReato.getDescrSottonumerazione().equals("-"))
			reati.append(" " + lReato.getDescrSottonumerazione());
		reati.append(" ");

		if (!lFlagAnnoNumero) {
			if (lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("")
					&& !lReato.getDescrFonte().equals("-"))
				reati.append(lReato.getDescrFonte() + " ");
		}

		if (lReato.getComma() != null && !lReato.getComma().equals(""))
			reati.append("C. " + lReato.getComma() + " ");

		if (lReato.getDescrCommaQualificante() != null && !lReato.getDescrCommaQualificante().equals("")
				&& !lReato.getDescrCommaQualificante().equals("-"))
			reati.append(lReato.getDescrCommaQualificante() + " ");

		if (lReato.getLettera() != null && !lReato.getLettera().equals(""))
			reati.append(" L. " + lReato.getLettera() + " ");
		if (lReato.getNumero() != null && !lReato.getNumero().equals(""))
			reati.append(" N. " + lReato.getNumero() + " ");

		// CIRCOSTANZE
		if (lCircostanze != null) {
			for (int i = 0; i < lCircostanze.length; i++) {
				ReatoModel lCirc = lCircostanze[i];
				reati.append(", ");

				boolean lFlagAnnoNumeroCirc = false;
				if (lCirc.getAnnoFonte() != null && !lCirc.getAnnoFonte().toString().equals("")
						&& lCirc.getNumeroFonte() != null && !lCirc.getNumeroFonte().equals("")) {
					lFlagAnnoNumeroCirc = true;
				}

				if (lFlagAnnoNumeroCirc) {
					if (lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("")
							&& !lCirc.getDescrFonte().equals("-"))
						reati.append(lCirc.getDescrFonte() + " ");
					if (lCirc.getAnnoFonte() != null && !lCirc.getAnnoFonte().toString().equals(""))
						reati.append(lCirc.getAnnoFonte());
					if (lCirc.getNumeroFonte() != null && !lCirc.getNumeroFonte().equals(""))
						reati.append("/" + lCirc.getNumeroFonte());
					reati.append(" ");
				}

				if (lCirc.getArticolo() != null && !lCirc.getArticolo().equals(""))
					reati.append("ART." + lCirc.getArticolo() + " ");
				if (lCirc.getDescrSottonumerazione() != null && !lCirc.getDescrSottonumerazione().equals("")
						&& !lCirc.getDescrSottonumerazione().equals("-"))
					reati.append(lCirc.getDescrSottonumerazione() + " ");

				if (!lFlagAnnoNumeroCirc) {
					if (lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("")
							&& !lCirc.getDescrFonte().equals("-"))
						reati.append(lCirc.getDescrFonte() + " ");
				}

				if (lCirc.getComma() != null && !lCirc.getComma().equals(""))
					reati.append("C. " + lCirc.getComma() + " ");

				if (lCirc.getDescrCommaQualificante() != null && !lCirc.getDescrCommaQualificante().equals("")
						&& !lCirc.getDescrCommaQualificante().equals("-"))
					reati.append(lCirc.getDescrCommaQualificante() + " ");

				if (lCirc.getLettera() != null && !lCirc.getLettera().equals(""))
					reati.append("L. " + lCirc.getLettera() + " ");
				if (lCirc.getNumero() != null && !lCirc.getNumero().equals(""))
					reati.append("N. " + lCirc.getNumero() + " ");
			}
		} // end if CIRCOSTANZE

		String descPeriodoConsumazione = "";
		if (lReato.getStringaConsumazione() != null) {
			descPeriodoConsumazione = StringUtils.toStringJSP(lReato.getStringaConsumazione());
		}
		lReatoCircostanza.getReato().setDescrPeriodoConsumazione(descPeriodoConsumazione);

		if (lReato.getNote() != null && !lReato.getNote().equals("")) {
			reati.append(", " + StringUtils.toStringJSP(lReato.getNote()));
		}

		String descReato = reati.toString().replaceAll("\\s+", " ");// rimuove eventuali spazi doppi
		lReatoCircostanza.getReato().setDescrTipoReato(descReato);
	}

	protected void estrapolaDescrizioneCirco(CircostanzaModel circostanza) {

		StringBuffer Ciaggra = new StringBuffer();
		boolean lFlagAnnoNumero = false;
		if (circostanza.getAnnoFonte() != null && !circostanza.getAnnoFonte().toString().equals("")
				&& circostanza.getNumeroFonte() != null && !circostanza.getNumeroFonte().equals("")) {
			lFlagAnnoNumero = true;
		}

		if (lFlagAnnoNumero) {
			if (circostanza.getDescrFonte() != null && !circostanza.getDescrFonte().equals("")
					&& !circostanza.getDescrFonte().equals("-"))
				Ciaggra.append(circostanza.getDescrFonte() + " ");
			if (circostanza.getAnnoFonte() != null && !circostanza.getAnnoFonte().toString().equals(""))
				Ciaggra.append(circostanza.getAnnoFonte());
			if (circostanza.getNumeroFonte() != null && !circostanza.getNumeroFonte().equals(""))
				Ciaggra.append("/" + circostanza.getNumeroFonte());
		}

		if (circostanza.getArticolo() != null && !circostanza.getArticolo().equals(""))
			Ciaggra.append("art." + circostanza.getArticolo());
		if (circostanza.getDescrSottonumerazione() != null
				&& !circostanza.getDescrSottonumerazione().equals("")
				&& !circostanza.getDescrSottonumerazione().equals("-"))
			Ciaggra.append(" " + circostanza.getDescrSottonumerazione());

		if (!lFlagAnnoNumero) {
			if (circostanza.getDescrFonte() != null && !circostanza.getDescrFonte().equals("")
					&& !circostanza.getDescrFonte().equals("-"))
				Ciaggra.append(circostanza.getDescrFonte());
		}

		if (circostanza.getComma() != null && !circostanza.getComma().equals(""))
			Ciaggra.append(" c. " + circostanza.getComma());

		if (circostanza.getDescrCommaQualificante() != null
				&& !circostanza.getDescrCommaQualificante().equals("")
				&& !circostanza.getDescrCommaQualificante().equals("-"))
			Ciaggra.append(" " + circostanza.getDescrCommaQualificante());

		if (circostanza.getLettera() != null && !circostanza.getLettera().equals(""))
			Ciaggra.append(" l. " + circostanza.getLettera());
		if (circostanza.getNumero() != null && !circostanza.getNumero().equals(""))
			Ciaggra.append(" n. " + circostanza.getNumero());

		if (circostanza.getNote() != null && !circostanza.getNote().equals("")) {
			Ciaggra.append(", " + StringUtils.toStringJSP(circostanza.getNote()));
		}

		String descCircoAggravante = Ciaggra.toString().replaceAll("\\s+", " ");// rimuove eventuali spazi
																				// doppi
		circostanza.setDescrTipoCircostanza(descCircoAggravante);
	}

	// MEV 26 CUMULO Step 2 - Estrapolo la descrizione del REATO_CUMULO e delle CIRCOSTANZE_CUMULO
	protected void estrapolaDescrizioneReatoCumulo(ReatoCircostanzaCumuloModel lReatoCircostanzaCumulo) {
		StringBuffer reati = new StringBuffer();

		ReatoCumuloModel lReato = lReatoCircostanzaCumulo.getReatoCum();
		ReatoCumuloModel[] lCircostanze = lReatoCircostanzaCumulo.getCircostanzeCum();

		boolean lFlagAnnoNumero = false;
		if (lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().toString().equals("")
				&& lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals("")) {
			lFlagAnnoNumero = true;
		}

		if (lFlagAnnoNumero) {
			if (lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("")
					&& !lReato.getDescrFonte().equals("-"))
				reati.append(lReato.getDescrFonte() + " ");
			if (lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().toString().equals(""))
				reati.append(lReato.getAnnoFonte());
			if (lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
				reati.append("/" + lReato.getNumeroFonte());
			reati.append(" ");
		}

		if (lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
			reati.append("ART." + lReato.getArticolo());
		if (lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("")
				&& !lReato.getDescrSottonumerazione().equals("-"))
			reati.append(" " + lReato.getDescrSottonumerazione());
		reati.append(" ");

		if (!lFlagAnnoNumero) {
			if (lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("")
					&& !lReato.getDescrFonte().equals("-"))
				reati.append(lReato.getDescrFonte() + " ");
		}

		if (lReato.getComma() != null && !lReato.getComma().equals(""))
			reati.append("C. " + lReato.getComma() + " ");

		if (lReato.getDescrCommaQualificante() != null && !lReato.getDescrCommaQualificante().equals("")
				&& !lReato.getDescrCommaQualificante().equals("-"))
			reati.append(lReato.getDescrCommaQualificante() + " ");

		if (lReato.getLettera() != null && !lReato.getLettera().equals(""))
			reati.append(" L. " + lReato.getLettera() + " ");
		if (lReato.getNumero() != null && !lReato.getNumero().equals(""))
			reati.append(" N. " + lReato.getNumero() + " ");

		// CIRCOSTANZE
		if (lCircostanze != null) {
			for (int i = 0; i < lCircostanze.length; i++) {
				ReatoCumuloModel lCirc = lCircostanze[i];
				reati.append(", ");

				boolean lFlagAnnoNumeroCirc = false;
				if (lCirc.getAnnoFonte() != null && !lCirc.getAnnoFonte().toString().equals("")
						&& lCirc.getNumeroFonte() != null && !lCirc.getNumeroFonte().equals("")) {
					lFlagAnnoNumeroCirc = true;
				}

				if (lFlagAnnoNumeroCirc) {
					if (lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("")
							&& !lCirc.getDescrFonte().equals("-"))
						reati.append(lCirc.getDescrFonte() + " ");
					if (lCirc.getAnnoFonte() != null && !lCirc.getAnnoFonte().toString().equals(""))
						reati.append(lCirc.getAnnoFonte());
					if (lCirc.getNumeroFonte() != null && !lCirc.getNumeroFonte().equals(""))
						reati.append("/" + lCirc.getNumeroFonte());
					reati.append(" ");
				}

				if (lCirc.getArticolo() != null && !lCirc.getArticolo().equals(""))
					reati.append("ART." + lCirc.getArticolo() + " ");
				if (lCirc.getDescrSottonumerazione() != null && !lCirc.getDescrSottonumerazione().equals("")
						&& !lCirc.getDescrSottonumerazione().equals("-"))
					reati.append(lCirc.getDescrSottonumerazione() + " ");

				if (!lFlagAnnoNumeroCirc) {
					if (lCirc.getDescrFonte() != null && !lCirc.getDescrFonte().equals("")
							&& !lCirc.getDescrFonte().equals("-"))
						reati.append(lCirc.getDescrFonte() + " ");
				}

				if (lCirc.getComma() != null && !lCirc.getComma().equals(""))
					reati.append("C. " + lCirc.getComma() + " ");

				if (lCirc.getDescrCommaQualificante() != null && !lCirc.getDescrCommaQualificante().equals("")
						&& !lCirc.getDescrCommaQualificante().equals("-"))
					reati.append(lCirc.getDescrCommaQualificante() + " ");

				if (lCirc.getLettera() != null && !lCirc.getLettera().equals(""))
					reati.append("L. " + lCirc.getLettera() + " ");
				if (lCirc.getNumero() != null && !lCirc.getNumero().equals(""))
					reati.append("N. " + lCirc.getNumero() + " ");
			}
		} // end if CIRCOSTANZE

		String descPeriodoConsumazione = "";
		if (lReato.getStringaConsumazione() != null) {
			descPeriodoConsumazione = StringUtils.toStringJSP(lReato.getStringaConsumazione());
		}
		lReatoCircostanzaCumulo.getReatoCum().setDescrPeriodoConsumazioneCum(descPeriodoConsumazione);

		if (lReato.getNote() != null && !lReato.getNote().equals("")) {
			reati.append(", " + StringUtils.toStringJSP(lReato.getNote()));
		}

		String descReato = reati.toString().replaceAll("\\s+", " ");// rimuove eventuali spazi doppi
		lReatoCircostanzaCumulo.getReatoCum().setDescrTipoReato(descReato);

	} // Chiude estrapolaDescrizioneReatoCumulo()

	protected void estrapolaDescrizioneCircoCumulo(CircostanzaCumuloModel circostanza) {

		StringBuffer Ciaggra = new StringBuffer();
		boolean lFlagAnnoNumero = false;
		if (circostanza.getAnnoFonte() != null && !circostanza.getAnnoFonte().toString().equals("")
				&& circostanza.getNumeroFonte() != null && !circostanza.getNumeroFonte().equals("")) {
			lFlagAnnoNumero = true;
		}

		if (lFlagAnnoNumero) {
			if (circostanza.getDescrFonte() != null && !circostanza.getDescrFonte().equals("")
					&& !circostanza.getDescrFonte().equals("-"))
				Ciaggra.append(circostanza.getDescrFonte() + " ");
			if (circostanza.getAnnoFonte() != null && !circostanza.getAnnoFonte().toString().equals(""))
				Ciaggra.append(circostanza.getAnnoFonte());
			if (circostanza.getNumeroFonte() != null && !circostanza.getNumeroFonte().equals(""))
				Ciaggra.append("/" + circostanza.getNumeroFonte());
		}

		if (circostanza.getArticolo() != null && !circostanza.getArticolo().equals(""))
			Ciaggra.append("art." + circostanza.getArticolo());
		if (circostanza.getDescrSottonumerazione() != null
				&& !circostanza.getDescrSottonumerazione().equals("")
				&& !circostanza.getDescrSottonumerazione().equals("-"))
			Ciaggra.append(" " + circostanza.getDescrSottonumerazione());

		if (!lFlagAnnoNumero) {
			if (circostanza.getDescrFonte() != null && !circostanza.getDescrFonte().equals("")
					&& !circostanza.getDescrFonte().equals("-"))
				Ciaggra.append(circostanza.getDescrFonte());
		}

		if (circostanza.getComma() != null && !circostanza.getComma().equals(""))
			Ciaggra.append(" c. " + circostanza.getComma());

		if (circostanza.getDescrCommaQualificante() != null
				&& !circostanza.getDescrCommaQualificante().equals("")
				&& !circostanza.getDescrCommaQualificante().equals("-"))
			Ciaggra.append(" " + circostanza.getDescrCommaQualificante());

		if (circostanza.getLettera() != null && !circostanza.getLettera().equals(""))
			Ciaggra.append(" l. " + circostanza.getLettera());
		if (circostanza.getNumero() != null && !circostanza.getNumero().equals(""))
			Ciaggra.append(" n. " + circostanza.getNumero());

		if (circostanza.getNote() != null && !circostanza.getNote().equals("")) {
			Ciaggra.append(", " + StringUtils.toStringJSP(circostanza.getNote()));
		}

		String descCircoAggravante = Ciaggra.toString().replaceAll("\\s+", " ");// rimuove eventuali spazi
																				// doppi
		circostanza.setDescrTipoCircostanza(descCircoAggravante);

	} // Chiude estrapolaDescrizioneCircoCumulo()

	// END MEV 26 Cumulo - step2

	/**
	 * Ricava la descrizione dello Stato del Procedimento
	 *
	 * @param aIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	protected String getStatoProcedimento(BigDecimal aIdFascicolo) throws F3BException {
		String lDescrStatoProcedimento = "";

		IStatoProcedimento lStatProcCtrl = SIEPLookupRemote.getStatoProcedimentoRemote();
		Vector lStati = lStatProcCtrl.ExRicercaStatoProcedimentoByFascicoloSiep(aIdFascicolo);

		// MEV Agosto 2014 - Aggiunti parametri di ricerca - Metto solo il PRIMO StatoProc senza data (Michele
		// testa)

		// Iterator itx = lStati.iterator();
		// while (itx.hasNext()) {
		// StatoProcedimentoModel lStatoProc = (StatoProcedimentoModel) itx.next();
		if (lStati.size() > 0) {
			StatoProcedimentoModel lStatoProc = (StatoProcedimentoModel) lStati.get(0);
			if (lStatoProc.getDescrStatoProcedimento() != null
					&& lStatoProc.getDescrStatoProcedimento().trim().length() > 0) {
				if (lStatoProc.getCodStatoProcedimento().equals("0315")
						|| lStatoProc.getCodStatoProcedimento().equals("0316")) {
					int chindex = lStatoProc.getDescrStatoProcedimento().indexOf("<");
					lDescrStatoProcedimento += lStatoProc.getDescrStatoProcedimento().substring(0, chindex);
				} else
					lDescrStatoProcedimento += lStatoProc.getDescrStatoProcedimento();
			} else
				lDescrStatoProcedimento = " - ";
		} else {
			lDescrStatoProcedimento = " - ";
		}
		// if (lStatoProc.getData() != null)
		// {
		// lDescrStatoProcedimento += ": ";
		// lDescrStatoProcedimento += DateUtils.getDateToString(lStatoProc.getData(), "dd-MM-yyyy");
		// lDescrStatoProcedimento += " ";
		// }
		// }
		// }

		return lDescrStatoProcedimento;
	}

	protected Vector<DettaglioFascicoloModel> getElencoFascicoli(ReatoModel lReaMod, CircostanzaModel lCirMod,
			String TipoRic, Boolean solocumulati, String lPagina) throws F3BException {

		IFascicoloSiep lCtrFasc = SIEPLookupRemote.getFascicoloSiepRemote();
		// MEV Agosto 2014 - Aggiungo Criterio SOLO CUMULATI
		Vector lFascicoli = lCtrFasc.ExRicercaFascicoloByReatoPaged(lReaMod, lCirMod, TipoRic, solocumulati,
				Integer.parseInt(lPagina));

		// ==========================================================================
		// Per ogni fascicolo da visualizzare carico:
		// - Posizione Giuridica Corrente
		// - Ultima Pena Residua
		// ==========================================================================
		PenaResiduaModel lPenMod = null;
		FascicoloSiepModel lFascMod = null;
		DettaglioFascicoloModel lFascDetMod = null;
		PosizioneGiuridicaModel lPosGiuMod = null;
		CalendarModel lDiff = null;

		Vector<DettaglioFascicoloModel> lDettaglioFascicoli = new Vector<DettaglioFascicoloModel>();

		IPenaResidua lCtrlPen = SIEPLookupRemote.getPenaResiduaRemote();
		IPosizioneGiuridica lCtrPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();

		Iterator iter = lFascicoli.iterator();
		while (iter.hasNext()) {
			lFascMod = (FascicoloSiepModel) iter.next();

			lFascDetMod = new DettaglioFascicoloModel();

			// POSIZIONE GIURIDICA
			// lPosGiuMod = new PosizioneGiuridicaModel();
			lPosGiuMod = lCtrPos
					.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
			lFascDetMod.setPosizioneGiuridica(lPosGiuMod);

			String descrStatoProc = getStatoProcedimento(lFascMod.getIdFascicoloSiep());
			lFascMod.setDescrStatoProcedimento(descrStatoProc);
			lFascDetMod.setFascicoloSiep(lFascMod);

			// PENA COMPLESSIVA
			// MEV Agosto 2014 - Se Fascicolo con cumulo deve andare su PENA CUMULO

			if (lFascMod.getFlagCumulante() != null && lFascMod.getFlagCumulante().compareTo("S") == 0) {
				ICumulo lCtrCum = SIEPLookupRemote.getCumuloRemote();
				CumuloModel aModelCum = new CumuloModel();
				aModelCum.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
				Vector VCumuli = lCtrCum.ExRicercaCumulo(aModelCum);
				// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				// siesLogger.debug(" CUMULO ------------> "+VCumuli.toString());
				int nn = VCumuli.size();
				if (nn > 0) {
					CumuloModel CumMod = (CumuloModel) VCumuli.get(nn - 1);
					IPenaCumulo lCtrPCum = SIEPLookupRemote.getPenaCumuloRemote();
					PenaCumuloModel PenCumMod = lCtrPCum.ExRicercaPenaCumuloByIdCumulo(CumMod.getIdCumulo());
					lFascDetMod.setPenaCumulo(PenCumMod);
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug(" PENA CUMULO ------------> "+PenCumMod);
				}
			} else {
				IPenaComplessiva lCtrPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
				PenaComplessivaSanzioneSostitutivaModel lPenCompMod = lCtrPenCom
						.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(
								lFascMod.getIdFascicoloSiep());
				lFascDetMod.setPenaComplessivaSanzioneSostitutiva(lPenCompMod);
			}

			// PENA RESIDUA
			// MEV Agosto 2014: - Creco l'ultima Pena Residua VALIDATA
			lPenMod = lCtrlPen.ExRicercaPenaResiduaUltimaValidata(lFascMod.getIdFascicoloSiep());
			lFascDetMod.setPenaResidua(lPenMod);

			if (lPenMod != null && lPenMod.getDataFine() != null && lPosGiuMod != null
					&& lPosGiuMod.getCodPosizioneGiuridica() != null
					&& !lPosGiuMod.getCodPosizioneGiuridica().equals("10")
					&& !lPosGiuMod.getCodPosizioneGiuridica().equals("07")) {
				CalendarUtil lCalCon = new CalendarUtil();
				lDiff = new CalendarModel();
				CalendarModel lCalMod = new CalendarModel();

				lCalMod.setDataFine(lPenMod.getDataFine());
				lCalMod.setDataInizio(DateUtils.getSysDate());

				lDiff = lCalCon.CalcolaNumGiorniMesiAnni(lCalMod);
				// Data Fine servirà nel foglio xls per sapere se è maggiore o minore della data Odierna
				lDiff.setDataFine(lPenMod.getDataFine());

				lFascDetMod.setCalendario(lDiff);
			}

			// REATI
			IReato lReaCtr = SIEPLookupRemote.getReatoRemote();
			Vector<ReatoCircostanzaModel> lReatiVe = lReaCtr
					.ExRicercaReatoCircostanzaByFascicolo(lFascMod.getIdFascicoloSiep());
			for (ReatoCircostanzaModel rcMod : lReatiVe) {
				estrapolaDescrizioneReato(rcMod);
			}
			lFascDetMod.setReatiCircostanze(lReatiVe);

			// Aggravanti
			ICircostanza lCirCtrl = SIEPLookupRemote.getCircostanzaRemote();
			Vector<CircostanzaModel> lCircVec = lCirCtrl
					.ExRicercaCircostanzaByIdFascicolo(lFascMod.getIdFascicoloSiep());
			for (CircostanzaModel CirMod : lCircVec) {
				estrapolaDescrizioneCirco(CirMod);
			}
			lFascDetMod.setCircostanze(lCircVec);

			// MEV 26 Step 2 - Aggiungo I ReatiCumulo e le Circostanze_Cumulo alla 'Ricerca Procedimenti by
			// Reato'
			// REATI_CUMULO
			Vector<TitoloCumulatoModel> lListaTitoli = new Vector();
			IFascicoloSiep lFasCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			BigDecimal IdIstruttoria = null;

			// Per cercare eventuali Reati_Cumulo e Circostanze_Cumulo, bisogna che ci sia una
			// ISTRUTTORIA_CUMULO
			IdIstruttoria = lFasCtrl
					.ExRicercaIstruttoriaCumuloByIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
			if (IdIstruttoria != null) {
				// Legati all'ISTRUTTORIA ci sono i TITOLO_CUMULATO
				IIstruttoriaCumulo lCtrlIstru = SIEPLookupRemote.getIstruttoriaCumuloRemote();
				lListaTitoli = lCtrlIstru.ExRicercaTitoliByIstruttoria(IdIstruttoria);

				Vector<ReatoCircostanzaCumuloModel> lReatiCumVec = new Vector(); // Contenitore di tutti i
																					// Reati_Cumulo del TITOLO
				Vector<ReatoCircostanzaCumuloModel> lReatiCumIstruVec = new Vector(); // Contenitore di tutti
																						// i Reati_Cumulo del
																						// ISTRUTTORIA

				if (lListaTitoli != null && lListaTitoli.size() > 0) {
					Iterator iterTit = lListaTitoli.iterator();
					while (iterTit.hasNext()) {
						// I REATO_CUMULO sono legati al Titolo_Cumulato
						TitoloCumulatoModel lTito = (TitoloCumulatoModel) iterTit.next();
						if (lTito != null && lTito.getIdTitoloCumulato() != null) {
							IReatoCumulo lCtrlRC = SIEPLookupRemote.getReatoCumuloRemote();
							lReatiCumVec = lCtrlRC
									.ExRicercaReatoCircostanzaCumByTitoloCum(lTito.getIdTitoloCumulato());
						}

						lReatiCumIstruVec.addAll(lReatiCumVec);
					}
				}

				// LogF3B.getLogger().debug("--XX-- Ciclo Titoli terminato: Totale Reati in ISTRUTTORIA in N.
				// di >"+lReatiCumIstruVec.size()+"<");

				for (ReatoCircostanzaCumuloModel ReaCirCumMod : lReatiCumIstruVec) {
					estrapolaDescrizioneReatoCumulo(ReaCirCumMod);
				}

				// Aggiungo tutti i Reati_Cumulo dell'Istruttora al Contenitore dei Fascicoli
				// (DETTAGLIO_FASCICOLO_MODEL)
				lFascDetMod.setReatoCircoCumulo(lReatiCumIstruVec);
				lFascDetMod.setFlagIstruttoriaPresente("S");

			}

			// AGGRAVANTI_CUMULO
			if (IdIstruttoria != null) {
				Vector<CircostanzaCumuloModel> lCirCumVec = new Vector(); // Contenitore di tutte le
																			// Circostanze_Cumulo del TITOLO
				Vector<CircostanzaCumuloModel> lCirCumIstru = new Vector(); // Contenitore di tutte le
																			// Circostanze_Cumulo del
																			// ISTRUTTORIA

				if (lListaTitoli != null && lListaTitoli.size() > 0) {
					Iterator iterTit = lListaTitoli.iterator();
					while (iterTit.hasNext()) {
						// LE CIRCOSTANZE_CUMULO sono legati al Titolo_Cumulato
						TitoloCumulatoModel lTito = (TitoloCumulatoModel) iterTit.next();
						if (lTito != null && lTito.getIdTitoloCumulato() != null) {
							ICircostanzaCumulo lCtrlCC = SIEPLookupRemote.getCircostanzaCumuloRemote();
							lCirCumVec = lCtrlCC
									.ExRicercaCircostanzaCumulobyTitolo(lTito.getIdTitoloCumulato());

						}

						lCirCumIstru.addAll(lCirCumVec);
					}
				}

				for (CircostanzaCumuloModel CirCumMod : lCirCumIstru) {
					estrapolaDescrizioneCircoCumulo(CirCumMod);
				}

				// Aggiungo tutte le circostanze_Cumulo dell'Istruttora al Contenitore dei Fascicoli
				// (DETTAGLIO_FASCICOLO_MODEL)
				lFascDetMod.setCircostanzaCumulo(lCirCumIstru);
			}

			// END MEV 26 Step 2 -

			// aggiunge elemento all'elenco
			lDettaglioFascicoli.addElement(lFascDetMod);

		}

		return lDettaglioFascicoli;
	}

	/**
	 * Effettua la ricerca paginata dei Procedimenti che hanno almeno un reato che ricade nei criteri
	 * impostati La ricerca viene effettuata sui Procedimenti dell'ufficio dell'utente in sessione
	 */
	public String processRequest() throws Exception {

		String lRetPage = PG_ELENCO_PROVV_REATO;
		// if (isRequestParameterNullObj("vai"))
		// {
		// // Pagina di attesa (rotelline)
		// lRetPage = PG_ATTESA_CON_ROTELLA;
		// setRequestAttribute("titolo", "ELENCO PROCEDIMENTI PER REATO");
		// setRequestAttribute("next_action", getClass().getName());
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("Attesa :" + getClass().getName());
		// }
		// else
		// {

		// REATO
		ReatoModel lReaMod = new ReatoModel();
		String lPagina = "1";
		String fonte = "";
		String annoFonte = "";
		String numeroFonte = "";
		String codSott = "";
		String comma = "";
		String commaQual = "";
		String lettera = "";
		String numero = "";
		String articolo = "";

		// CIRCOSTANZA Aggravante bilanciamento
		CircostanzaModel lCirMod = new CircostanzaModel();
		String Cfonte = "";
		String CannoFonte = "";
		String CnumeroFonte = "";
		String CcodSott = "";
		String Ccomma = "";
		String CcommaQual = "";
		String Clettera = "";
		String Cnumero = "";
		String Carticolo = "";

		Date reatoDal = null;
		Date reatoAl = null;
		String nazione = "";

		Boolean solocumulati = false;
		String cumulati = "";
		String tipoProc = "";
		String DescNazio = "";

		String DescFonte = "";
		String DescBTQ = "";
		String DescCommaBTQ = "";

		String CirDescFonte = "";
		String CirDescBTQ = "";
		String CirDescCommaBTQ = "";
		// String Bilancia = "";

		// ==========================================================================
		// Recupero parametri di ricerca
		// ==========================================================================
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_COD_FONTE)) {
			lReaMod.setCodFonte(getRequestStringParameter(ICostantiReato.CAMPO_COD_FONTE));
			fonte = getRequestStringParameter(ICostantiReato.CAMPO_COD_FONTE);
		}

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_FONTE))
			if (!getRequestStringParameter(ICostantiReato.CAMPO_ANNO_FONTE).equals("")) {
				lReaMod.setAnnoFonte(
						new BigDecimal(getRequestStringParameter(ICostantiReato.CAMPO_ANNO_FONTE)));
				annoFonte = getRequestStringParameter(ICostantiReato.CAMPO_ANNO_FONTE);
			}

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_NUMERO_FONTE)) {
			lReaMod.setNumeroFonte(getRequestStringParameter(ICostantiReato.CAMPO_NUMERO_FONTE));
			numeroFonte = getRequestStringParameter(ICostantiReato.CAMPO_NUMERO_FONTE);
		}

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE)) {
			lReaMod.setCodSottonumerazione(
					getRequestStringParameter(ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE));
			codSott = getRequestStringParameter(ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE);
		}

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_COMMA)) {
			lReaMod.setComma(getRequestStringParameter(ICostantiReato.CAMPO_COMMA));
			comma = getRequestStringParameter(ICostantiReato.CAMPO_COMMA);
		}

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_COMMA_QUALIFICANTE)) {
			lReaMod.setCommaQualificante(getRequestStringParameter(ICostantiReato.CAMPO_COMMA_QUALIFICANTE));
			commaQual = getRequestStringParameter(ICostantiReato.CAMPO_COMMA_QUALIFICANTE);
		}

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_LETTERA)) {
			lReaMod.setLettera(getRequestStringParameter(ICostantiReato.CAMPO_LETTERA));
			lettera = getRequestStringParameter(ICostantiReato.CAMPO_LETTERA);
		}

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_NUMERO)) {
			lReaMod.setNumero(getRequestStringParameter(ICostantiReato.CAMPO_NUMERO));
			numero = getRequestStringParameter(ICostantiReato.CAMPO_NUMERO);
		}

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_ARTICOLO)) {
			lReaMod.setArticolo(getRequestStringParameter(ICostantiReato.CAMPO_ARTICOLO));
			articolo = getRequestStringParameter(ICostantiReato.CAMPO_ARTICOLO);
		}

		// CIRCOSTANZE AGGRAVANTI

		if (!isRequestParameterNullObj(ICostantiCircostanza.CAMPO_COD_FONTE)) {
			lCirMod.setCodFonte(getRequestStringParameter(ICostantiCircostanza.CAMPO_COD_FONTE));
			Cfonte = getRequestStringParameter(ICostantiCircostanza.CAMPO_COD_FONTE);
		}

		if (!isRequestParameterNullObj(ICostantiCircostanza.CAMPO_ANNO_FONTE))
			if (!getRequestStringParameter(ICostantiCircostanza.CAMPO_ANNO_FONTE).equals("")) {
				lCirMod.setAnnoFonte(
						new BigDecimal(getRequestStringParameter(ICostantiCircostanza.CAMPO_ANNO_FONTE)));
				CannoFonte = getRequestStringParameter(ICostantiCircostanza.CAMPO_ANNO_FONTE);
			}

		if (!isRequestParameterNullObj(ICostantiCircostanza.CAMPO_NUMERO_FONTE)) {
			lCirMod.setNumeroFonte(getRequestStringParameter(ICostantiCircostanza.CAMPO_NUMERO_FONTE));
			CnumeroFonte = getRequestStringParameter(ICostantiCircostanza.CAMPO_NUMERO_FONTE);
		}

		if (!isRequestParameterNullObj(ICostantiCircostanza.CAMPO_COD_SOTTONUMERAZIONE)) {
			lCirMod.setCodSottonumerazione(
					getRequestStringParameter(ICostantiCircostanza.CAMPO_COD_SOTTONUMERAZIONE));
			CcodSott = getRequestStringParameter(ICostantiCircostanza.CAMPO_COD_SOTTONUMERAZIONE);
		}

		if (!isRequestParameterNullObj(ICostantiCircostanza.CAMPO_COMMA)) {
			lCirMod.setComma(getRequestStringParameter(ICostantiCircostanza.CAMPO_COMMA));
			Ccomma = getRequestStringParameter(ICostantiCircostanza.CAMPO_COMMA);
		}

		if (!isRequestParameterNullObj(ICostantiCircostanza.CAMPO_COMMA_QUALIFICANTE)) {
			lCirMod.setCommaQualificante(
					getRequestStringParameter(ICostantiCircostanza.CAMPO_COMMA_QUALIFICANTE));
			CcommaQual = getRequestStringParameter(ICostantiCircostanza.CAMPO_COMMA_QUALIFICANTE);
		}

		if (!isRequestParameterNullObj(ICostantiCircostanza.CAMPO_LETTERA)) {
			lCirMod.setLettera(getRequestStringParameter(ICostantiCircostanza.CAMPO_LETTERA));
			Clettera = getRequestStringParameter(ICostantiCircostanza.CAMPO_LETTERA);
		}

		if (!isRequestParameterNullObj(ICostantiCircostanza.CAMPO_NUMERO)) {
			lCirMod.setNumero(getRequestStringParameter(ICostantiCircostanza.CAMPO_NUMERO));
			Cnumero = getRequestStringParameter(ICostantiCircostanza.CAMPO_NUMERO);
		}

		if (!isRequestParameterNullObj(ICostantiCircostanza.CAMPO_ARTICOLO)) {
			lCirMod.setArticolo(getRequestStringParameter(ICostantiCircostanza.CAMPO_ARTICOLO));
			Carticolo = getRequestStringParameter(ICostantiCircostanza.CAMPO_ARTICOLO);
		}

		if (!isRequestParameterNullObj(ICostantiCircostanza.CAMPO_COD_BILANCIAMENTO_CIRCOSTANZE)
				&& getRequestStringParameter(ICostantiCircostanza.CAMPO_COD_BILANCIAMENTO_CIRCOSTANZE) != null
				&& !getRequestStringParameter(ICostantiCircostanza.CAMPO_COD_BILANCIAMENTO_CIRCOSTANZE)
						.equals("-")) {
			lCirMod.setCodBilanciamentoCircostanze(
					getRequestStringParameter(ICostantiCircostanza.CAMPO_COD_BILANCIAMENTO_CIRCOSTANZE));
			// Bilancia = getRequestStringParameter(CAMPO_COD_BILANCIAMENTO_CIRCOSTANZE);

		} else {
			// Bilancia = "-";
			lCirMod.setCodBilanciamentoCircostanze("-");
		}

		//
		// -----
		//
		if (!isRequestParameterNullObj(CAMPO_GIORNO_DATA_INIZIO)
				&& !isRequestParameterNullObj(CAMPO_MESE_DATA_INIZIO)
				&& !isRequestParameterNullObj(CAMPO_ANNO_DATA_INIZIO)) {
			reatoDal = getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO, CAMPO_MESE_DATA_INIZIO,
					CAMPO_GIORNO_DATA_INIZIO);
			lReaMod.setDataInizio(reatoDal);
		}

		if (!isRequestParameterNullObj(CAMPO_GIORNO_DATA_FINE)
				&& !isRequestParameterNullObj(CAMPO_MESE_DATA_FINE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_DATA_FINE)) {
			reatoAl = getRequestDateParameter(CAMPO_ANNO_DATA_FINE, CAMPO_MESE_DATA_FINE,
					CAMPO_GIORNO_DATA_FINE);
			lReaMod.setDataFine(reatoAl);
		}

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_COD_NAZIONE)) {
			lReaMod.setNazionalita(getRequestStringParameter(ICostantiReato.CAMPO_COD_NAZIONE));
			nazione = getRequestStringParameter(ICostantiReato.CAMPO_COD_NAZIONE);
		}

		// Mev Agosto 2014 - Aggiunti criteri di ricerca
		// procedimenti: Archiviati/Definiti AD , Iscritti/Validati NA , Tutti TT

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_CERCA_DEFINITI)) {
			if (getRequestStringParameter(ICostantiReato.CAMPO_CERCA_DEFINITI).toString()
					.equals("DEFINITI")) {
				lReaMod.setCodStatoFascicolo("AD");
				tipoProc = "Tipologia Procedimenti : DEFINITI";
			} else if (getRequestStringParameter(ICostantiReato.CAMPO_CERCA_DEFINITI).toString()
					.equals("PENDENTI")) {
				lReaMod.setCodStatoFascicolo("NA");
				tipoProc = "Tipologia Procedimenti : IN CORSO";
			} else {
				lReaMod.setCodStatoFascicolo("TT");
				tipoProc = "Tipologia Procedimenti : TUTTI";
			}
		}

		// procedimenti: Solo Cumulati (true) , tutti (false)
		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_CERCA_CUMULATI)) {
			if (getRequestStringParameter(ICostantiReato.CAMPO_CERCA_CUMULATI).toString()
					.equals("CUMULATI")) {
				solocumulati = true;
				cumulati = "SI";
			}
		}

		// Stringhe per Intestazione REATO in Elenco
		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_DESC_FONTE))
			DescFonte = getRequestStringParameter(ICostantiReato.CAMPO_DESC_FONTE);

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_DESC_SOTTONUMERAZIONE))
			DescBTQ = getRequestStringParameter(ICostantiReato.CAMPO_DESC_SOTTONUMERAZIONE);

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_DESC_COMMA_QUAL))
			DescCommaBTQ = getRequestStringParameter(ICostantiReato.CAMPO_DESC_COMMA_QUAL);

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_DESC_NAZIONE)
				&& !getRequestStringParameter(ICostantiReato.CAMPO_DESC_NAZIONE).equals("-"))
			DescNazio = getRequestStringParameter(ICostantiReato.CAMPO_DESC_NAZIONE);

		String Intestazione = "";
		if (annoFonte.equals("") && numeroFonte.equals("")) {
			Intestazione += "ART. " + articolo + " ";
			if (!DescBTQ.equals("") && !DescBTQ.equals("-")) {
				Intestazione += DescBTQ + " ";
			}
			Intestazione += DescFonte + " ";
		} else {
			Intestazione += DescFonte + " " + annoFonte + "/" + numeroFonte + " ";
			if (!articolo.equals("") && !articolo.equals("-")) {
				Intestazione += "ART. " + articolo + " ";
			}
			if (!DescBTQ.equals("") && !DescBTQ.equals("-")) {
				Intestazione += DescBTQ + " ";
			}
		}

		if (!comma.equals("")) {
			Intestazione += "C. " + comma + " ";
		}

		if (!DescCommaBTQ.equals("") && !DescCommaBTQ.equals("-")) {
			Intestazione += DescCommaBTQ + " ";
		}

		if (!lettera.equals("")) {
			Intestazione += "L. " + lettera + " ";
		}

		if (!numero.equals("")) {
			Intestazione += "N. " + numero + " ";
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" ----------------> REATO : "+Intestazione);
		setRequestAttribute("IntestazioneReato", Intestazione);
		setRequestAttribute("cumulatiSINO", cumulati);
		setRequestAttribute("tipologiaProc", tipoProc);
		setRequestAttribute("DescrizioNazione", DescNazio);

		String DataIniRea = "";
		String DataFineRea = "";
		String IntestaDate = "";

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_GIORNO_DATA_INIZIO)
				&& getRequestStringParameter(ICostantiReato.CAMPO_GIORNO_DATA_INIZIO) != null
				&& !getRequestStringParameter(ICostantiReato.CAMPO_GIORNO_DATA_INIZIO).equals("")) {
			String gg = getRequestStringParameter(ICostantiReato.CAMPO_GIORNO_DATA_INIZIO);
			String mm = getRequestStringParameter(ICostantiReato.CAMPO_MESE_DATA_INIZIO);
			String aaaa = getRequestStringParameter(ICostantiReato.CAMPO_ANNO_DATA_INIZIO);
			DataIniRea = gg + "/" + mm + "/" + aaaa;
		}
		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_GIORNO_DATA_FINE)
				&& !getRequestStringParameter(ICostantiReato.CAMPO_GIORNO_DATA_FINE).equals("")
				&& getRequestStringParameter(ICostantiReato.CAMPO_GIORNO_DATA_FINE) != null) {
			String ggF = getRequestStringParameter(ICostantiReato.CAMPO_GIORNO_DATA_FINE);
			String mmF = getRequestStringParameter(ICostantiReato.CAMPO_MESE_DATA_FINE);
			String aaaaF = getRequestStringParameter(ICostantiReato.CAMPO_ANNO_DATA_FINE);
			DataFineRea = ggF + "/" + mmF + "/" + aaaaF;

		}

		if (!DataIniRea.equals(""))
			IntestaDate += "Ricerca DAL " + DataIniRea + " ";

		if (!DataFineRea.equals(""))
			IntestaDate += "AL " + DataFineRea + " ";

		setRequestAttribute("IntestazioneDate", IntestaDate);
		// ----------------------
		// Stringhe per Intestazione CIRCOSTANZE AGGRAVANTI in Elenco
		if (!isRequestParameterNullObj(ICostantiCircostanza.CAMPO_DESC_FONTE))
			CirDescFonte = getRequestStringParameter(ICostantiCircostanza.CAMPO_DESC_FONTE);

		if (!isRequestParameterNullObj(ICostantiCircostanza.CAMPO_DESC_SOTTONUMERAZIONE))
			CirDescBTQ = getRequestStringParameter(ICostantiCircostanza.CAMPO_DESC_SOTTONUMERAZIONE);

		if (!isRequestParameterNullObj(ICostantiCircostanza.CAMPO_DESC_COMMA_QUAL))
			CirDescCommaBTQ = getRequestStringParameter(ICostantiCircostanza.CAMPO_DESC_COMMA_QUAL);

		String IntestazioneCirco = "";
		if (CannoFonte.equals("") && CnumeroFonte.equals("")) {
			IntestazioneCirco += "ART. " + Carticolo + " ";
			if (!CirDescBTQ.equals("") && !CirDescBTQ.equals("-")) {
				IntestazioneCirco += CirDescBTQ + " ";
			}
			IntestazioneCirco += CirDescFonte + " ";
		} else {
			IntestazioneCirco += CirDescFonte + " " + CannoFonte + "/" + CnumeroFonte + " ";
			if (!Carticolo.equals("") && !Carticolo.equals("-")) {
				IntestazioneCirco += "ART. " + Carticolo + " ";
			}
			if (!CirDescBTQ.equals("") && !CirDescBTQ.equals("-")) {
				IntestazioneCirco += CirDescBTQ + " ";
			}
		}

		if (!Ccomma.equals("")) {
			IntestazioneCirco += "C. " + Ccomma + " ";
		}

		if (!CirDescCommaBTQ.equals("") && !CirDescCommaBTQ.equals("-")) {
			IntestazioneCirco += CirDescCommaBTQ + " ";
		}

		if (!Clettera.equals("")) {
			IntestazioneCirco += "L. " + Clettera + " ";
		}

		if (!Cnumero.equals("")) {
			IntestazioneCirco += "N. " + Cnumero + " ";
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// LogF3B.getLogger().debug(" ----------------> CIRCO AGGRAVANTI : "+IntestazioneCirco);
		setRequestAttribute("IntestazioneAggravanti", IntestazioneCirco);

		// ==========================================================================
		//
		// ==========================================================================
		lReaMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lCirMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		// MEV 17
		// Metto in sessione il modello di ricerca per utilizzarlo nella stampa excel
		setSessionAttribute("ReatoRicercaModel", lReaMod);
		setSessionAttribute("CircoAggrRicercaModel", lCirMod);

		IFascicoloSiep lCtrFasc = SIEPLookupRemote.getFascicoloSiepRemote();
		BigDecimal CountRisultati = null;
		/*
		 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		 * LogF3B.getLogger() siesLogger.debug(" ---------------->  REATO : fonte = "+fonte); // [FT] -
		 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.debug(" ---------------->  REATO : annoFonte = "+annoFonte); // [FT] - 03/08/2016 -
		 * MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.debug(" ---------------->  REATO : numeroFonte = "+numeroFonte); // [FT] - 03/08/2016 -
		 * MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.debug(" ---------------->  REATO : articolo = "+articolo);
		 *
		 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		 * LogF3B.getLogger() siesLogger.debug(" ---------------->  CIRCO : Cfonte = "+Cfonte); // [FT] -
		 * 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.debug(" ---------------->  CIRCO : CannoFonte = "+CannoFonte); // [FT] - 03/08/2016 -
		 * MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.debug(" ---------------->  CIRCO : CnumeroFonte = "+CnumeroFonte); // [FT] - 03/08/2016
		 * - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.debug(" ---------------->  CIRCO : Carticolo = "+Carticolo); // [FT] - 03/08/2016 -
		 * MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		 * siesLogger.debug(" ---------------->  CIRCO : Bilancia = "+Bilancia);
		 */
		// ==============================================================================================
		// Decido quale Tipo di Ricerca Effettuare

		String QualeRicerca = "";

		if (((Cfonte.equals("") || Cfonte.equals("-")) && CannoFonte.equals("") && CnumeroFonte.equals("")
				&& Carticolo.equals("") && (CcodSott.equals("") || CcodSott.equals("-")) && Ccomma.equals("")
				&& Clettera.equals("") && Cnumero.equals("")
				&& (CcommaQual.equals("") || CcommaQual.equals("-")))
				&& ((!fonte.equals("") && !fonte.equals("-")) || !annoFonte.equals("")
						|| !numeroFonte.equals("") || !articolo.equals("")
						|| (!codSott.equals("") && !codSott.equals("-")) || !comma.equals("")
						|| !lettera.equals("") || !numero.equals("")
						|| (!commaQual.equals("") && !commaQual.equals("-")))) {
			// Solo REATI
			QualeRicerca = "R";
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrFasc.ExgetCountReati(lReaMod, solocumulati);
			} else {
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");
			}
		} else if (((fonte.equals("") || fonte.equals("-")) && annoFonte.equals("") && numeroFonte.equals("")
				&& articolo.equals("") && (codSott.equals("") || codSott.equals("-")) && comma.equals("")
				&& lettera.equals("") && numero.equals("") && (commaQual.equals("") || commaQual.equals("-")))
				&& ((!Cfonte.equals("") && !Cfonte.equals("-")) || !CannoFonte.equals("")
						|| !CnumeroFonte.equals("") || !Carticolo.equals("")
						|| (!CcodSott.equals("") && !CcodSott.equals("-")) || !Ccomma.equals("")
						|| !Clettera.equals("") || !Cnumero.equals("")
						|| (!CcommaQual.equals("") && !CcommaQual.equals("-")))) {
			// Solo AGGRAVANTI
			QualeRicerca = "A";
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrFasc.ExgetCountCircostanzeAggr(lReaMod, lCirMod, solocumulati);
			} else {
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");
			}
		} else {
			// REATO + CIRCOSTANZE AGGRAVANTI
			QualeRicerca = "RA";
			if (isRequestParameterNullObj("CountRisultati")) {
				CountRisultati = lCtrFasc.ExgetCountReatiCircostanzeAggr(lReaMod, lCirMod, solocumulati);
			} else {
				CountRisultati = getRequestBigDecimalParameter("CountRisultati");
			}
		}

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" ---------- Tipo Ricerca = "+QualeRicerca );
		setRequestAttribute("ReatooCircostanze", QualeRicerca);
		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		// ==========================================================================
		// Passo i dati alla form di visulaizzazione dei risultati
		// ==========================================================================
		Vector lDettaglioFascicoli = getElencoFascicoli(lReaMod, lCirMod, QualeRicerca, solocumulati,
				lPagina);
		this.setRequestAttribute("fascicoli", lDettaglioFascicoli);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		String lAzione = "siap.siep.calcolopena.action.ActElencoProcReato";
		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE, lAzione);

		StringBuffer sb = new StringBuffer();
		sb.append(ICostantiReato.CAMPO_COD_FONTE + "=" + fonte);
		sb.append("&" + ICostantiReato.CAMPO_ANNO_FONTE + "=" + annoFonte);
		sb.append("&" + ICostantiReato.CAMPO_NUMERO_FONTE + "=" + numeroFonte);
		sb.append("&" + ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE + "=" + codSott);
		sb.append("&" + ICostantiReato.CAMPO_COMMA + "=" + comma);
		sb.append("&" + ICostantiReato.CAMPO_LETTERA + "=" + lettera);
		sb.append("&" + ICostantiReato.CAMPO_NUMERO + "=" + numero);
		sb.append("&" + ICostantiReato.CAMPO_ARTICOLO + "=" + articolo);
		sb.append("&" + ICostantiReato.CAMPO_COD_NAZIONE + "=" + nazione);

		String stringaDiRicerca = sb.toString();
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug(" STRINGA REATO -----------> = "+stringaDiRicerca);
		setRequestAttribute("stringaDiRicerca", stringaDiRicerca);

		// return PG_ELENCO_PROVV_REATO; // restituisce la jsp di VIEW
		// }

		return lRetPage;

	} // chiude processRequest()

} // Chiude classe
