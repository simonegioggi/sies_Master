package siap.siep.calcolopena.action;

/**
 * <p>Title: ActLoadDettaglioElencoProcReato</p>
 * <p>Description: Classe Action per la </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.model.ReatoCircostanzaCumuloModel;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.reato.action.ICostantiReato;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoCircostanzaModel;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadDettaglioElencoProcReato extends ActionSiap implements ICostantiAnnotazioneManuale {

	/**
	 * Action per caricare e visualizzare il dettaglio dei reati che ricadono nei criteri di ricerca impostati
	 * per il fascicolo passato in input Vedi: Ricerche -> Procedimento per Reato
	 */
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);

		PenaResiduaModel lPenMod = null;

		IPenaResidua lCtrlPen = SIEPLookupRemote.getPenaResiduaRemote();

		FascicoloSiepModel lFascMod = new FascicoloSiepModel();

		// ==========================================================================
		// Carica il fascicolo in sessione
		// ==========================================================================
		IFascicoloSiep lCtrlFasc = SIEPLookupRemote.getFascicoloSiepRemote();
		lFascMod = lCtrlFasc.ExRicercaFascicoloByKey(new BigDecimal(lId));
		setSessionAttribute("fascicolo", lFascMod);

		IReato lCtrl = SIEPLookupRemote.getReatoRemote();

		// ============================================
		// Carica i dati del reato (???)
		// ============================================
		ReatoModel lReaModel = new ReatoModel();

		String fonte = "";
		String annoFonte = "";
		String numeroFonte = "";
		String codSott = "";
		String comma = "";
		String lettera = "";
		String numero = "";
		String articolo = "";

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_COD_FONTE)) {
			lReaModel.setCodFonte(getRequestStringParameter(ICostantiReato.CAMPO_COD_FONTE));
			fonte = getRequestStringParameter(ICostantiReato.CAMPO_COD_FONTE);
		}

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_FONTE))
			if (!getRequestStringParameter(ICostantiReato.CAMPO_ANNO_FONTE).equals("")) {
				lReaModel.setAnnoFonte(
						new BigDecimal(getRequestStringParameter(ICostantiReato.CAMPO_ANNO_FONTE)));
				annoFonte = getRequestStringParameter(ICostantiReato.CAMPO_ANNO_FONTE);
			}

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_NUMERO_FONTE)) {
			lReaModel.setNumeroFonte(getRequestStringParameter(ICostantiReato.CAMPO_NUMERO_FONTE));
			numeroFonte = getRequestStringParameter(ICostantiReato.CAMPO_NUMERO_FONTE);
		}

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE)) {
			lReaModel.setCodSottonumerazione(
					getRequestStringParameter(ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE));
			codSott = getRequestStringParameter(ICostantiReato.CAMPO_COD_SOTTONUMERAZIONE);
		}

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_COMMA)) {
			lReaModel.setComma(getRequestStringParameter(ICostantiReato.CAMPO_COMMA));
			comma = getRequestStringParameter(ICostantiReato.CAMPO_COMMA);
		}

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_LETTERA)) {
			lReaModel.setLettera(getRequestStringParameter(ICostantiReato.CAMPO_LETTERA));
			lettera = getRequestStringParameter(ICostantiReato.CAMPO_LETTERA);
		}

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_NUMERO)) {
			lReaModel.setNumero(getRequestStringParameter(ICostantiReato.CAMPO_NUMERO));
			numero = getRequestStringParameter(ICostantiReato.CAMPO_NUMERO);
		}

		if (!isRequestParameterNullObj(ICostantiReato.CAMPO_ARTICOLO)) {
			lReaModel.setArticolo(getRequestStringParameter(ICostantiReato.CAMPO_ARTICOLO));

			articolo = getRequestStringParameter(ICostantiReato.CAMPO_ARTICOLO);
		}

		lReaModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lReaModel = " + lReaModel);
		// ==========================================================================
		// Effettua la ricerca di tutti i reati e le circostanze (aggravanti/attenuanti)
		// per il fascicolo (??))
		// ==========================================================================
		Vector lReati = new Vector();
		lReati = lCtrl.ExRicercaReatoCircostanzaByFascicolo(lFascMod.getIdFascicoloSiep());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lReati.size() = " + lReati.size());

		ReatoModel Circ = null;
		String fonteFlag = "";
		String annoFonteFlag = "";
		String numeroFonteFlag = "";
		String codSottFlag = "";
		String commaFlag = "";
		String letteraFlag = "";
		String numeroFlag = "";
		String articoloFlag = "";

		// ==========================================================================
		// Seleziono i reati del fascicolo che coincidono con i criteri di ricerca
		// per passarli alla form di visualizzazione
		// ==========================================================================
		Vector reatiFiltrati = new Vector();
		ReatoCircostanzaModel lReatoCircostanza = null;
		for (int i = 0; i < lReati.size(); i++) {
			// R E A T O
			lReatoCircostanza = (ReatoCircostanzaModel) lReati.get(i);

			ReatoModel lReato = lReatoCircostanza.getReato();
			// siesLogger.debug("lReato = "+lReato);

			if (lReato.getCodFonte() != null && !lReato.getCodFonte().equals("-") && !fonte.equals("-")) {
				fonteFlag = "N";

				if (lReato.getCodFonte().equals(fonte)) {
					fonteFlag = "S";
				}
			}

			if ((lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().toString().equals("-"))
					&& !annoFonte.equals("")) {
				annoFonteFlag = "N";

				// if (lReato.getAnnoFonte().equals(annoFonte))
				if (lReato.getAnnoFonte().compareTo(new BigDecimal(annoFonte)) == 0) {
					annoFonteFlag = "S";
				}
			}

			if ((lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
					&& !numeroFonte.equals("")) {
				numeroFonteFlag = "N";
				if (lReato.getNumeroFonte().equals(numeroFonte)) {
					numeroFonteFlag = "S";
				}
			}

			if ((lReato.getCodSottonumerazione() != null && !lReato.getCodSottonumerazione().equals("-"))
					&& (!codSott.equals("") && !codSott.equals("-"))) {
				codSottFlag = "N";
				if (lReato.getCodSottonumerazione().equals(codSott)) {
					codSottFlag = "S";
				}
			}

			if ((lReato.getComma() != null && !lReato.getComma().equals("")) && !comma.equals("")) {
				commaFlag = "N";
				if (lReato.getComma().equals(comma)) {
					commaFlag = "S";
				}
			}

			if ((lReato.getNumero() != null && !lReato.getNumero().equals("")) && !numero.equals("")) {
				numeroFlag = "N";
				if (lReato.getNumero().equals(numero)) {
					numeroFlag = "S";
				}
			}

			if ((lReato.getLettera() != null && !lReato.getLettera().equals("")) && !lettera.equals("")) {
				letteraFlag = "N";
				if (lReato.getLettera().equals(lettera)) {
					letteraFlag = "S";
				}
			}

			if ((lReato.getArticolo() != null && !lReato.getArticolo().equals("")) && !articolo.equals("")) {
				articoloFlag = "N";
				if (lReato.getArticolo().equals(articolo)) {
					articoloFlag = "S";
				}
			}

			// ------------------------------------------------------------------------
			// siesLogger.debug("fonteFlag = "+fonteFlag);
			// siesLogger.debug("annoFonteFlag = "+annoFonteFlag);
			// siesLogger.debug("numeroFonteFlag = "+numeroFonteFlag);
			// siesLogger.debug("codSottFlag = "+codSottFlag);
			// siesLogger.debug("commaFlag = "+commaFlag);
			// siesLogger.debug("letteraFlag = "+letteraFlag);
			// siesLogger.debug("numeroFlag = "+numeroFlag);
			// siesLogger.debug("articoloFlag = "+articoloFlag);
			// -------------------------------------------------------------------------

			// ========================================================================
			// C I R C O S T A N Z A
			// ========================================================================
			String fonteCirc = "";
			String annoFonteCirc = "";
			String numeroFonteCirc = "";
			String codSottCirc = "";
			String commaCirc = "";
			String letteraCirc = "";
			String numeroCirc = "";
			String articoloCirc = "";

			ReatoModel[] lCircostanze = lReatoCircostanza.getCircostanze();
			if (lCircostanze != null) {
				for (int y = 0; y < lCircostanze.length; y++) {
					Circ = lCircostanze[y];

					// siesLogger.debug("lCircostanzaReato = "+Circ);

					if (!fonteCirc.equals("S")) // Se ha trovato un fonteCirc che metcha con il filtro, non
												// serve più che entri nel test
					{
						if ((Circ.getCodFonte() != null && !Circ.getCodFonte().equals("-"))
								&& (!fonte.equals("") && fonte.equals("-"))) {
							fonteCirc = "N";

							if (Circ.getCodFonte().equals(fonte)) {
								fonteCirc = "S";
							}
						}
					}

					if (!annoFonteCirc.equals("S")) // Se ha trovato un annoFonteCirc che metcha con il
													// filtro, non serve più che entri nel test
					{
						if ((Circ.getAnnoFonte() != null && !Circ.getAnnoFonte().toString().equals(""))
								&& !annoFonte.equals("")) {
							annoFonteCirc = "N";
							// if (Circ.getAnnoFonte().equals(annoFonte))
							if (Circ.getAnnoFonte().compareTo(new BigDecimal(annoFonte)) == 0) {
								annoFonteCirc = "S";
							}
						}
					}

					if (!numeroFonteCirc.equals("S")) // Se ha trovato un numeroFonteCirc che metcha con il
														// filtro, non serve più che entri nel test
					{
						if ((Circ.getNumeroFonte() != null && !Circ.getNumeroFonte().equals(""))
								&& !numeroFonte.equals("")) {
							numeroFonteCirc = "N";

							if (Circ.getNumeroFonte().equals(numeroFonte)) {
								numeroFonteCirc = "S";
							}
						}
					}

					if (!codSottCirc.equals("S")) // Se ha trovato un codSottCirc che metcha con il filtro,
													// non serve più che entri nel test
					{
						if ((Circ.getCodSottonumerazione() != null
								&& !Circ.getCodSottonumerazione().equals("-"))
								&& (!codSott.equals("") && !codSott.equals("-"))) {
							codSottCirc = "N";

							if (Circ.getCodSottonumerazione().equals(codSott)) {
								codSottCirc = "S";
							}
						}
					}

					if (!commaCirc.equals("S")) // Se ha trovato un commaCirc che metcha con il filtro, non
												// serve più che entri nel test
					{
						if ((Circ.getComma() != null && !Circ.getComma().equals("")) && !comma.equals("")) {
							commaCirc = "N";

							if (Circ.getComma().equals(comma)) {
								commaCirc = "S";
							}
						}
					}

					if (!numeroCirc.equals("S")) // Se ha trovato un numeroCirc che metcha con il filtro, non
													// serve più che entri nel test
					{
						if ((Circ.getNumero() != null && !Circ.getNumero().equals(""))
								&& !numero.equals("")) {
							numeroCirc = "N";

							if (Circ.getNumero().equals(numero)) {
								numeroCirc = "S";
							}
						}
					}

					if (!letteraCirc.equals("S")) // Se ha trovato un letteraCirc che metcha con il filtro,
													// non serve più che entri nel test
					{
						if ((Circ.getLettera() != null && !Circ.getLettera().equals(""))
								&& !lettera.equals("")) {
							letteraCirc = "N";

							if (Circ.getLettera().equals(lettera)) {
								letteraCirc = "S";
							}
						}
					}

					if (!articoloCirc.equals("S")) // Se ha trovato un ArtcoloCirc che metcha il filtro, non
													// serve più che entri nel test
					{
						if ((Circ.getArticolo() != null && !Circ.getArticolo().equals(""))
								&& !articolo.equals("")) {
							articoloCirc = "N";

							if (Circ.getArticolo().equals(articolo)) {
								articoloCirc = "S";
							}
						}
					}

				} // Chiude Ciclo for (int y = 0; y < lCircostanze.length; y++)

				// -----------------------------------------------------------------
				// siesLogger.debug("fonteCirc = "+fonteCirc);
				// siesLogger.debug("annoFonteCirc = "+annoFonteCirc);
				// siesLogger.debug("numeroFonteCirc = "+numeroFonteCirc);
				// siesLogger.debug("codSottCirc = "+codSottCirc);
				// siesLogger.debug("commaCirc = "+commaCirc);
				// siesLogger.debug("letteraCirc = "+letteraCirc);
				// siesLogger.debug("numeroCirc = "+numeroCirc);
				// siesLogger.debug("articoloCirc = "+articoloCirc);
				// -----------------------------------------------------------------

			} // Chiude if (lCircostanze != null)

			// ========================================================================
			// Se il reato del fascicolo coincide con quello della ricerca lo inserisco
			// tra quelli da passare alla jsp di visualizzazione
			// ========================================================================

			if (fonteCirc.equals("S") || annoFonteCirc.equals("S") || numeroFonteCirc.equals("S")
					|| codSottCirc.equals("S") || commaCirc.equals("S") || letteraCirc.equals("S")
					|| numeroCirc.equals("S") || articoloCirc.equals("S") || fonteFlag.equals("S")
					|| annoFonteFlag.equals("S") || numeroFonteFlag.equals("S") || codSottFlag.equals("S")
					|| commaFlag.equals("S") || letteraFlag.equals("S") || numeroFlag.equals("S")
					|| articoloFlag.equals("S")) {
				siesLogger.debug("reatiFiltrati.add");
				reatiFiltrati.add(lReatoCircostanza);
			}

			// Ripulitura campi per confronto
			Circ = null;
			fonteFlag = "";
			annoFonteFlag = "";
			numeroFonteFlag = "";
			codSottFlag = "";
			commaFlag = "";
			letteraFlag = "";
			numeroFlag = "";
			articoloFlag = "";

			fonteCirc = "";
			annoFonteCirc = "";
			numeroFonteCirc = "";
			codSottCirc = "";
			commaCirc = "";
			letteraCirc = "";
			numeroCirc = "";
			articoloCirc = "";

		} // Chiude Ciclo for (int i = 0; i < lReati.size(); i++)i

		/*
		 * AnnotazioneManualeModel lAnnMod = null;
		 *
		 * IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote(); if (lReati != null)
		 * { Iterator iter = lReati.iterator(); while (iter.hasNext()) { ReatoModel lReaMod = (ReatoModel)
		 * iter.next(); lAnnMod = new AnnotazioneManualeModel(); lAnnMod =
		 * lCtrlAnn.ExRicercaAnnotazioneManualeByIdReato(lReaMod.getIdReato()); if (lAnnMod != null) {
		 * lReaMod.setFlagVisto("S"); } else lReaMod.setFlagVisto("N"); } }
		 */

		// ==
		if (!isRequestParameterNullObj("NomeAzione")) {
			setRequestAttribute("NomeAzione", getRequestStringParameter("NomeAzione"));
		}
		// PENA RESIDUA
		lPenMod = new PenaResiduaModel();
		lPenMod = lCtrlPen.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		setRequestAttribute("penaresidua", lPenMod);

		setRequestAttribute("reati", reatiFiltrati);

		// MEV 26 CUMULO STEP 2
		// aggiungo i ReatiCumulo e le Circostanze_Cumulo alla 'Ricerca Procedimenti by Reato'

		// REATI_CIRCOSTANZE_CUMULO
		Vector reatiFiltratiCumulo = new Vector();

		Vector<TitoloCumulatoModel> lListaTitoli = new Vector();
		BigDecimal IdIstruttoria = null;

		// Per cercare eventuali Reati_Cumulo e Circostanze_Cumulo, bisogna che ci sia una ISTRUTTORIA_CUMULO
		IdIstruttoria = lCtrlFasc.ExRicercaIstruttoriaCumuloByIdFascicoloSiep(new BigDecimal(lId));
		if (IdIstruttoria != null) {
			// Legati all'ISTRUTTORIA ci sono i TITOLO_CUMULATO
			IIstruttoriaCumulo lCtrlIstru = SIEPLookupRemote.getIstruttoriaCumuloRemote();
			lListaTitoli = lCtrlIstru.ExRicercaTitoliByIstruttoria(IdIstruttoria);

			Vector<ReatoCircostanzaCumuloModel> lReatiCumVec = new Vector(); // Contenitore di tutti i
																				// Reati_Cumulo del TITOLO
			Vector<ReatoCircostanzaCumuloModel> lReatiCumIstruVec = new Vector(); // Contenitore di tutti i
																					// Reati_Cumulo del
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

			// ============================================
			// REATI CUMULO
			// ============================================
			String fonteCum = "";
			String annoFonteCum = "";
			String numeroFonteCum = "";
			String codSottoCum = "";
			String commaCum = "";
			String letteraCum = "";
			String numeroCum = "";
			String articoloCum = "";

			ReatoCircostanzaCumuloModel ReatiCircoCumulo = null;

			for (int i = 0; i < lReatiCumIstruVec.size(); i++) {
				ReatiCircoCumulo = lReatiCumIstruVec.get(i);

				ReatoCumuloModel lReato = ReatiCircoCumulo.getReatoCum();
				// siesLogger.debug("lReatoCumulo = "+lReato);

				if (lReato.getCodFonte() != null && !lReato.getCodFonte().equals("-") && !fonte.equals("-")) {
					fonteCum = "N";
					if (lReato.getCodFonte().equals(fonte)) {
						fonteCum = "S";
					}
				}

				if ((lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().toString().equals("-"))
						&& !annoFonte.equals("")) {
					annoFonteCum = "N";
					if (lReato.getAnnoFonte().compareTo(new BigDecimal(annoFonte)) == 0) {
						annoFonteCum = "S";
					}
				}

				if ((lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
						&& !numeroFonte.equals("")) {
					numeroFonteCum = "N";
					if (lReato.getNumeroFonte().equals(numeroFonte)) {
						numeroFonteCum = "S";
					}
				}

				if ((lReato.getCodSottonumerazione() != null && !lReato.getCodSottonumerazione().equals("-"))
						&& (!codSott.equals("") && !codSott.equals("-"))) {
					codSottoCum = "N";
					if (lReato.getCodSottonumerazione().equals(codSott)) {
						codSottoCum = "S";
					}
				}

				if ((lReato.getComma() != null && !lReato.getComma().equals("")) && !comma.equals("")) {
					commaCum = "N";
					if (lReato.getComma().equals(comma)) {
						commaCum = "S";
					}
				}

				if ((lReato.getNumero() != null && !lReato.getNumero().equals("")) && !numero.equals("")) {
					numeroCum = "N";
					if (lReato.getNumero().equals(numero)) {
						numeroCum = "S";
					}
				}

				if ((lReato.getLettera() != null && !lReato.getLettera().equals("")) && !lettera.equals("")) {
					letteraCum = "N";
					if (lReato.getLettera().equals(lettera)) {
						letteraCum = "S";
					}
				}

				if ((lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
						&& !articolo.equals("")) {
					articoloCum = "N";
					if (lReato.getArticolo().equals(articolo)) {
						articoloCum = "S";
					}
				}

				// ------------------------------------------------------------------------
				// siesLogger.debug("fonteCum = "+fonteCum);
				// siesLogger.debug("annoFonteCum = "+annoFonteCum);
				// siesLogger.debug("numeroFonteCum = "+numeroFonteCum);
				// siesLogger.debug("codSottoCum = "+codSottoCum);
				// siesLogger.debug("commaCum = "+commaCum);
				// siesLogger.debug("letteraCum = "+letteraCum);
				// siesLogger.debug("numeroCum = "+numeroCum);
				// siesLogger.debug("articoloCum = "+articoloCum);
				// -------------------------------------------------------------------------

				// ========================================================================
				// C I R C O S T A N Z A
				// ========================================================================

				ReatoCumuloModel CircosCum = null;
				String fonteCircCum = "";
				String annoFonteCircCum = "";
				String numeroFonteCircCum = "";
				String codSottoCircCum = "";
				String commaCircCum = "";
				String letteraCircCum = "";
				String numeroCircCum = "";
				String articoloCircCum = "";

				ReatoCumuloModel[] lCircostanzeCum = ReatiCircoCumulo.getCircostanzeCum();
				if (lCircostanzeCum != null) {
					for (int y = 0; y < lCircostanzeCum.length; y++) {
						CircosCum = lCircostanzeCum[y];
						// siesLogger.debug("lCircostanzaReato Cumulo = "+CircosCum);

						if (!fonteCircCum.equals("S")) // Se ha trovato un fonteCircCum che metcha con il
														// filtro, non serve più che entri nel test
						{
							if ((CircosCum.getCodFonte() != null && !CircosCum.getCodFonte().equals("-"))
									&& (!fonte.equals("") && fonte.equals("-"))) {
								fonteCircCum = "N";

								if (CircosCum.getCodFonte().equals(fonte)) {
									fonteCircCum = "S";
								}
							}
						}

						if (!annoFonteCircCum.equals("S")) // Se ha trovato un annoFonteCircCum che metcha con
															// il filtro, non serve più che entri nel test
						{
							if ((CircosCum.getAnnoFonte() != null
									&& !CircosCum.getAnnoFonte().toString().equals(""))
									&& !annoFonte.equals("")) {
								annoFonteCircCum = "N";
								if (CircosCum.getAnnoFonte().toString().equals(annoFonte)) {
									annoFonteCircCum = "S";
								}
							}
						}

						if (!numeroFonteCircCum.equals("S")) // Se ha trovato un numeroFonteCircCum che metcha
																// con il filtro, non serve più che entri nel
																// test
						{
							if ((CircosCum.getNumeroFonte() != null && !CircosCum.getNumeroFonte().equals(""))
									&& !numeroFonte.equals("")) {
								numeroFonteCircCum = "N";

								if (CircosCum.getNumeroFonte().equals(numeroFonte)) {
									numeroFonteCircCum = "S";
								}
							}
						}

						if (!codSottoCircCum.equals("S")) // Se ha trovato un codSottoCircCum che metcha con
															// il filtro, non serve più che entri nel test
						{
							if ((CircosCum.getCodSottonumerazione() != null
									&& !CircosCum.getCodSottonumerazione().equals("-"))
									&& (!codSott.equals("") && !codSott.equals("-"))) {
								codSottoCircCum = "N";

								if (CircosCum.getCodSottonumerazione().equals(codSott)) {
									codSottoCircCum = "S";
								}
							}
						}

						if (!commaCircCum.equals("S")) // Se ha trovato un commaCircCum che metcha con il
														// filtro, non serve più che entri nel test
						{
							if ((CircosCum.getComma() != null && !CircosCum.getComma().equals(""))
									&& !comma.equals("")) {
								commaCircCum = "N";

								if (CircosCum.getComma().equals(comma)) {
									commaCircCum = "S";
								}
							}
						}

						if (!numeroCircCum.equals("S")) // Se ha trovato un numeroCircCum che metcha con il
														// filtro, non serve più che entri nel test
						{
							if ((CircosCum.getNumero() != null && !CircosCum.getNumero().equals(""))
									&& !numero.equals("")) {
								numeroCircCum = "N";

								if (CircosCum.getNumero().equals(numero)) {
									numeroCircCum = "S";
								}
							}
						}

						if (!letteraCircCum.equals("S")) // Se ha trovato un letteraCircCum che metcha con il
															// filtro, non serve più che entri nel test
						{
							if ((CircosCum.getLettera() != null && !CircosCum.getLettera().equals(""))
									&& !lettera.equals("")) {
								letteraCircCum = "N";

								if (CircosCum.getLettera().equals(lettera)) {
									letteraCircCum = "S";
								}
							}
						}

						if (!articoloCircCum.equals("S")) // Se ha trovato un ArtcoloCircCum che metcha il
															// filtro, non serve più che entri nel test
						{
							if ((CircosCum.getArticolo() != null && !CircosCum.getArticolo().equals(""))
									&& !articolo.equals("")) {
								articoloCircCum = "N";

								if (CircosCum.getArticolo().equals(articolo)) {
									articoloCircCum = "S";
								}
							}
						}

					} // Chiude Ciclo for (int y = 0; y < lCircostanzeCum.length; y++)

					// -----------------------------------------------------------------
					// siesLogger.debug("fonteCircCum = "+fonteCircCum);
					// siesLogger.debug("annoFonteCircCum = "+annoFonteCircCum);
					// siesLogger.debug("numeroFonteCircCum = "+numeroFonteCircCum);
					// siesLogger.debug("codSottoCircCum = "+codSottoCircCum);
					// siesLogger.debug("commaCircCum = "+commaCircCum);
					// siesLogger.debug("letteraCircCum = "+letteraCircCum);
					// siesLogger.debug("numeroCircCum = "+numeroCircCum);
					// siesLogger.debug("articoloCircCum = "+articoloCircCum);
					// -----------------------------------------------------------------

				} // Chiude if (lCircostanzeCum != null)

				// ========================================================================
				// Se il reato_Cumulo del Titolo coincide con quello della ricerca lo inserisco
				// tra quelli da passare alla jsp di visualizzazione
				// ========================================================================

				if (fonteCircCum.equals("S") || annoFonteCircCum.equals("S") || numeroFonteCircCum.equals("S")
						|| codSottoCircCum.equals("S") || commaCircCum.equals("S")
						|| letteraCircCum.equals("S") || numeroCircCum.equals("S")
						|| articoloCircCum.equals("S") || fonteCum.equals("S") || annoFonteCum.equals("S")
						|| numeroFonteCum.equals("S") || codSottoCum.equals("S") || commaCum.equals("S")
						|| letteraCum.equals("S") || numeroCum.equals("S") || articoloCum.equals("S")) {
					siesLogger.debug(" --XX-- -------->>   reatiFiltratiCumulo.add");
					reatiFiltratiCumulo.add(ReatiCircoCumulo);
				}

				// Ripulitura campi per confronto
				CircosCum = null;

				fonteCum = "";
				annoFonteCum = "";
				numeroFonteCum = "";
				codSottoCum = "";
				commaCum = "";
				letteraCum = "";
				numeroCum = "";
				articoloCum = "";

				fonteCircCum = "";
				annoFonteCircCum = "";
				numeroFonteCircCum = "";
				codSottoCircCum = "";
				commaCircCum = "";
				letteraCircCum = "";
				numeroCircCum = "";
				articoloCircCum = "";

			} // CHIUDE for (int i = 0; i < lReatiCumIstruVec.size(); i++)

		} // CHIUDE if(IdIstruttoria != null)

		/*
		 * // AGGRAVANTI_CUMULO====================================================== if(IdIstruttoria!=null)
		 * { Vector<CircostanzaCumuloModel> lCirCumVec = new Vector(); // Contenitore di tutte le
		 * Circostanze_Cumulo del TITOLO Vector<CircostanzaCumuloModel> lCirCumIstru = new Vector(); //
		 * Contenitore di tutte le Circostanze_Cumulo del ISTRUTTORIA if(lListaTitoli!=null &&
		 * lListaTitoli.size()>0) { Iterator iterTit = lListaTitoli.iterator(); while (iterTit.hasNext()) { //
		 * LE CIRCOSTANZE_CUMULO sono legati al Titolo_Cumulato TitoloCumulatoModel lTito =
		 * (TitoloCumulatoModel)iterTit.next(); if(lTito!=null && lTito.getIdTitoloCumulato()!=null) {
		 * ICircostanzaCumulo lCtrlCC = SIEPLookupRemote.getCircostanzaCumuloRemote(); lCirCumVec =
		 * lCtrlCC.ExRicercaCircostanzaCumulobyTitolo(lTito.getIdTitoloCumulato()); }
		 * lCirCumIstru.addAll(lCirCumVec); } } }
		 * //=========================================================================
		 */
		// END MEV 26 Step 2 -

		setRequestAttribute("reatiCumulo", reatiFiltratiCumulo);

		return PG_LOAD_DETTAGLIO_ELENCO_PROC_REATO;
	}

}