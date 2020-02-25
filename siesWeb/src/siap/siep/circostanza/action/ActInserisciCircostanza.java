package siap.siep.circostanza.action;

/**
 * <p>Title: ActInserisciCircostanza</p>
 * <p>Description: Classe Action per l'inserimento di Circostanza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.circostanza.controller.ICircostanza;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
//import siap.siep.sentenza.action.ICostantiSentenza;
//import siap.siep.sentenza.controller.ISentenza;
//import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciCircostanza extends ActionSiap implements ICostantiCircostanza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// Attributi di classe
	protected Vector VCRModel = new Vector();
	protected boolean flagAgg = false;
	protected String flagGiudizio = "";
	protected String flagSentenza = "";
	protected String codBil = "";
	protected String noteBil = "";

	/**
	 * Azione di Inserimento del Circostanza
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		if (!isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di iscrizione
															// guidata
		{
			setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		FascicoloSiepModel lFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"));

		letturaDati(lFascicolo.getIdFascicoloSiep());

		ICircostanza lCtrl = SIEPLookupRemote.getCircostanzaRemote();
		lCtrl.ExInserisciCircostanze(VCRModel, flagAgg, flagGiudizio, flagSentenza, codBil, noteBil,
				lFascicolo.getIdFascicoloSiep(), null);

		// setta la risposta nella request
		setRequestAttribute("ComingFromInsert", "YES");

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.circostanza.action.ActRicercaCircostanza&"
				+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
				+ ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();

		return lPage;
	}

	/**
	 * Metodo verifyExCampiSentenza()
	 * 
	 * @param aCirMod
	 *            CircostanzaModel
	 * 
	 * 
	 *            Il metodo effettua dei controlli sui campi FLAG_SENTENZA_APPLICAZ_PENA
	 *            COD_BILANCIAMENTO_CIRCOSTANZE NOTE_BILANCIAMENTO FLAG_GIUDIZIO_ABBREVIATO prima di
	 *            effettuare l'inserimento nel db
	 * 
	 *            inserito a seguito dell'attività a7-rr-165 (Bilanciamento delle Circostanze)
	 * 
	 */
	private void verifyExCampiSentenza(CircostanzaModel aCirMod) throws Exception {
		// Flag Applicazione Pena
		if (!isRequestParameterNullObj(CAMPO_FLAG_SENTENZA_APPLICAZ_PENA)) {
			aCirMod.setFlagSentenzaApplicazPena("S");
		} else if (!isRequestParameterNullObj("FlagSentenzaApplicazPena")) {
			aCirMod.setFlagSentenzaApplicazPena(getRequestStringParameter("FlagSentenzaApplicazPena"));
		} else {
			aCirMod.setFlagSentenzaApplicazPena("N");
		}

		// Cod Bilanciamento Circostanze
		if (!isRequestParameterNullObj(CAMPO_COD_BILANCIAMENTO_CIRCOSTANZE)) {
			aCirMod.setCodBilanciamentoCircostanze(
					getRequestStringParameter(CAMPO_COD_BILANCIAMENTO_CIRCOSTANZE));
		} else if (!isRequestParameterNullObj("CodBilanciamentoCircostanze")) {
			aCirMod.setCodBilanciamentoCircostanze(getRequestStringParameter("CodBilanciamentoCircostanze"));
		} else {
			aCirMod.setCodBilanciamentoCircostanze("-");
		}

		// Note Bilanciamento getRequestStringParameter(CAMPO_NOTE_BILANCIAMENTO)
		if (!isRequestParameterNullObj(CAMPO_NOTE_BILANCIAMENTO)) {
			aCirMod.setNoteBilanciamento(getRequestStringParameter(CAMPO_NOTE_BILANCIAMENTO));
		}

		// Flag Giudizio Abbreviato
		if (!isRequestParameterNullObj(CAMPO_FLAG_GIUDIZIO_ABBREVIATO)) {
			aCirMod.setFlagGiudizioAbbreviato("S");
		} else if (!isRequestParameterNullObj("FlagGiudizioAbbreviato")) {
			aCirMod.setFlagSentenzaApplicazPena(getRequestStringParameter("FlagGiudizioAbbreviato"));
		} else {
			aCirMod.setFlagGiudizioAbbreviato("N");
		}

	}

	protected void letturaDati(BigDecimal aIdFascicoloSiep) throws Exception {

		CircostanzaModel lCirMod = new CircostanzaModel();

		VCRModel = new Vector();

		String[] CodFonti = getRequestStringParameters(CAMPO_COD_FONTE);
		String[] AnnoFonti = getRequestStringParameters(CAMPO_ANNO_FONTE);
		String[] NumeroFonti = getRequestStringParameters(CAMPO_NUMERO_FONTE);
		String[] SottoNum = getRequestStringParameters(CAMPO_COD_SOTTONUMERAZIONE);
		String[] Commi = getRequestStringParameters(CAMPO_COMMA);
		// ***********************************************************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		String[] CommiQualif = getRequestStringParameters(CAMPO_COMMA_QUALIFICANTE);
		// ***********************************************************************************
		String[] Lettere = getRequestStringParameters(CAMPO_LETTERA);
		String[] Numeri = getRequestStringParameters(CAMPO_NUMERO);
		String[] Articoli = getRequestStringParameters(CAMPO_ARTICOLO);
		String[] Cablati = { "" };

		if (getRequestStringParameters("cablati") != null)
			Cablati = getRequestStringParameters("cablati");

		if (CodFonti.length > 0) {
			for (int i = 0; i < 5; i++) {
				if (CodFonti[i] != null && !CodFonti[i].equals("-")) {
					lCirMod = new CircostanzaModel();
					lCirMod.setCodFonte(CodFonti[i]);

					if (AnnoFonti[i] != null && !AnnoFonti[i].equals(""))
						lCirMod.setAnnoFonte(new BigDecimal(AnnoFonti[i]));

					lCirMod.setNumeroFonte(NumeroFonti[i]);
					lCirMod.setCodSottonumerazione(SottoNum[i]);
					lCirMod.setComma(Commi[i]);
					// *******************************************
					// Federica - a9-rr-078
					// aggiunto campo Comma-Qualificante
					lCirMod.setCommaQualificante(CommiQualif[i]);
					// *******************************************
					lCirMod.setLettera(Lettere[i]);
					lCirMod.setNumero(Numeri[i]);
					lCirMod.setArticolo(Articoli[i]);
					lCirMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lCirMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lCirMod.setDataInserimento(DateUtils.getSysDate());
					lCirMod.setFasSieIdFascicoloSiep(aIdFascicoloSiep);

					VCRModel.add(lCirMod);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("lCirMod0" + lCirMod);

				}
			}
		}
		String fonte = new String("");
		String articolo = new String("");
		String comma = new String("");
		String numero = new String("");
		String sottoNum = new String("-");
		String commaqualif = new String("-");

		// Cablati sono le circostanze fisse
		if (getRequestStringParameters("cablati") != null) {
			CircostanzaModel lCirMod2;
			for (int i = 0; i < Cablati.length - 1; i++) // -1 perchè c'è sempre almeno un cablati in hidden
			{
				lCirMod2 = new CircostanzaModel();
				lCirMod2.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lCirMod2.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lCirMod2.setDataInserimento(DateUtils.getSysDate());
				lCirMod2.setFasSieIdFascicoloSiep(aIdFascicoloSiep);

				articolo = "";
				numero = "";
				comma = "";
				fonte = "";

				StringTokenizer st = new StringTokenizer(Cablati[i]);
				// Prima di tutto l'articolo
				if (st.hasMoreTokens()) {
					articolo = st.nextToken();
				}
				// Secondo la fonte
				if (st.hasMoreTokens()) {
					fonte = st.nextToken();
				}
				// Terzo pezzo dipende
				if (st.hasMoreTokens()) {
					String lTerzoPezzo = st.nextToken();
					if (lTerzoPezzo.startsWith("C")) {
						comma = lTerzoPezzo.substring(1, 2);
					}
					if (lTerzoPezzo.startsWith("N")) {
						numero = lTerzoPezzo.substring(1, 2);
					}
					if (lTerzoPezzo.startsWith("B")) {
						sottoNum = "02";
					}
				}
				// Quarto Pezzo
				if (st.hasMoreTokens()) {
					String lQuartoPezzo = st.nextToken();
					if (lQuartoPezzo.startsWith("N")) {
						numero = lQuartoPezzo.substring(1, 2);
					}
				}
				lCirMod2.setCodFonte(fonte);
				lCirMod2.setComma(comma);
				lCirMod2.setArticolo(articolo);
				lCirMod2.setCodSottonumerazione(sottoNum);
				lCirMod2.setNumero(numero);
				// *******************************************
				// Federica - a9-rr-078
				// aggiunto campo Comma-Qualificante
				lCirMod2.setCommaQualificante(commaqualif);
				// *******************************************

				// CAMPI EX SENTENZA
				this.verifyExCampiSentenza(lCirMod2);

				VCRModel.add(lCirMod2);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("lCirMod1" + lCirMod2);

			}
		}
		// *******************************************
		// Federica - a9-rr-078
		flagGiudizio = "";
		flagSentenza = "";
		// *******************************************

		// Fabio - REWORK Bilanciamento Circostanze (a7-rr-165)
		if (!isRequestParameterNullObj(CAMPO_FLAG_GIUDIZIO_ABBREVIATO)
				&& getRequestStringParameter(CAMPO_FLAG_GIUDIZIO_ABBREVIATO) != null
				&& getRequestStringParameter(CAMPO_FLAG_GIUDIZIO_ABBREVIATO).equals("S")) {
			lCirMod = new CircostanzaModel();
			lCirMod.setCodFonte("25");

			// lCirMod.setComma("2");
			lCirMod.setArticolo("442");
			lCirMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lCirMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lCirMod.setDataInserimento(DateUtils.getSysDate());
			lCirMod.setFasSieIdFascicoloSiep(aIdFascicoloSiep);
			lCirMod.setCodSottonumerazione("-");
			// *******************************************
			// Federica - a9-rr-078
			// aggiunto campo Comma-Qualificante
			lCirMod.setCommaQualificante("-");
			flagGiudizio = "S";
			// *******************************************

			VCRModel.add(lCirMod);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("INSERITO 442 " + lCirMod);
		}
		// *********************************************************************************
		// Federica - a9-rr-078 -- il valore "V" significa che è stato inserito precedentemente
		// un articolo 442 e sulla maschera c'è la spunta nel flag
		if (!isRequestParameterNullObj(CAMPO_FLAG_GIUDIZIO_ABBREVIATO)
				&& getRequestStringParameter(CAMPO_FLAG_GIUDIZIO_ABBREVIATO) != null
				&& getRequestStringParameter(CAMPO_FLAG_GIUDIZIO_ABBREVIATO).equals("V")) {
			flagGiudizio = "S";
		}
		// *********************************************************************************

		if (!isRequestParameterNullObj(CAMPO_FLAG_SENTENZA_APPLICAZ_PENA)
				&& getRequestStringParameter(CAMPO_FLAG_SENTENZA_APPLICAZ_PENA) != null
				&& getRequestStringParameter(CAMPO_FLAG_SENTENZA_APPLICAZ_PENA).equals("S")) {
			lCirMod = new CircostanzaModel();
			lCirMod.setCodFonte("25");
			// lCirMod.setComma("2");
			lCirMod.setArticolo("444");
			lCirMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lCirMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lCirMod.setDataInserimento(DateUtils.getSysDate());
			lCirMod.setFasSieIdFascicoloSiep(aIdFascicoloSiep);
			lCirMod.setCodSottonumerazione("-");
			// *******************************************
			// Federica - a9-rr-078
			// aggiunto campo Comma-Qualificante
			lCirMod.setCommaQualificante("-");
			flagSentenza = "S";
			// *******************************************

			VCRModel.add(lCirMod);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("INSERITO 444 " + lCirMod);
		}

		// *********************************************************************************
		// Federica - a9-rr-078 -- il valore "V" significa che è stato inserito precedentemente
		// un articolo 444 e sulla maschera c'è la spunta nel flag
		if (!isRequestParameterNullObj(CAMPO_FLAG_SENTENZA_APPLICAZ_PENA)
				&& getRequestStringParameter(CAMPO_FLAG_SENTENZA_APPLICAZ_PENA) != null
				&& getRequestStringParameter(CAMPO_FLAG_SENTENZA_APPLICAZ_PENA).equals("V")) {
			flagSentenza = "S";
		}
		// *********************************************************************************

		// CAMPI EX SENTENZA
		this.verifyExCampiSentenza(lCirMod);

		// VALORI CHE DEVONO ESSERE PASSATI PER AGGIORNAMENTO A TAPPETO
		CircostanzaModel circTest = new CircostanzaModel();
		this.verifyExCampiSentenza(circTest);

		flagAgg = false;
		// *******************************************
		// Federica - a9-rr-078
		// String flagGiudizio= "";
		// String flagSentenza= "";
		// *******************************************
		codBil = "";
		noteBil = "";
		if (!getRequestStringParameter("verifyCampiComuni").equals("0"))
			flagAgg = true;

		if (flagAgg) {
			// *******************************************
			// Federica - a9-rr-078
			/*
			 * if(circTest.getFlagGiudizioAbbreviato()!=null &&
			 * circTest.getFlagGiudizioAbbreviato().equalsIgnoreCase("S")){ flagGiudizio= "S"; } else{
			 * flagGiudizio= "N"; } if(circTest.getFlagSentenzaApplicazPena()!=null &&
			 * circTest.getFlagSentenzaApplicazPena().equalsIgnoreCase("S")){ flagSentenza= "S"; } else{
			 * flagSentenza= "N"; }
			 */
			if (circTest.getCodBilanciamentoCircostanze() != null)
				codBil = circTest.getCodBilanciamentoCircostanze();
			if (circTest.getNoteBilanciamento() != null)
				noteBil = circTest.getNoteBilanciamento();
		}
	}

}