package siap.siep.modulocumulo.action;

/**
 * <p>Title: ActInserisciCircostanzaCumulo</p>
 * <p>Description: classe action di Inserimento di Circostanza_Cumulo (Aggravanti/Attenuati relative al titolo Cumulato)</p>
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
import siap.siep.modulocumulo.controller.ICircostanzaCumulo;
import siap.siep.modulocumulo.model.CircostanzaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciCircostanzaCumulo extends ActionModuloCumulo implements ICostantiCircostanzaCumulo {

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
	 * Azione di Inserimento della Circostanza_Cumulo (Aggravanti/Attenuati relative al titolo Cumulato)
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		BigDecimal lIdTitolo = getRequestBigDecimalParameter(
				ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
		letturaDati(lIdTitolo);

		ICircostanzaCumulo lCtrl = SIEPLookupRemote.getCircostanzaCumuloRemote();
		lCtrl.ExInserisciCircostanzeCumulo(VCRModel, flagAgg, flagGiudizio, flagSentenza, codBil, noteBil,
				lIdTitolo);

		// setta la risposta nella request
		setRequestAttribute("ComingFromInsert", "YES");

		// Prepara la pagina di destinazione
		String lPage = "";
		// lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		// "=siap.siep.modulocumulo.action.ActRicercaCircostanzaCumulo&" +
		// ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" +lIdTitolo;
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActRicercaReatoCumulo&"
				+ ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO + "=" + lIdTitolo;
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
	 */
	private void verifyExCampiSentenza(CircostanzaCumuloModel aCirMod) throws Exception {
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

	protected void letturaDati(BigDecimal aIdTitoCum) throws Exception {
		// siesLogger.debug("Start letturaDati");
		// ====================================================================================================
		// Circostanze Principali: Fino ad un max di 5 Righe nella form
		// ====================================================================================================
		CircostanzaCumuloModel lCirMod = new CircostanzaCumuloModel();

		VCRModel = new Vector();

		String[] CodFonti = getRequestStringParameters(CAMPO_COD_FONTE);
		String[] AnnoFonti = getRequestStringParameters(CAMPO_ANNO_FONTE);
		String[] NumeroFonti = getRequestStringParameters(CAMPO_NUMERO_FONTE);
		String[] SottoNum = getRequestStringParameters(CAMPO_COD_SOTTONUMERAZIONE);
		String[] Commi = getRequestStringParameters(CAMPO_COMMA);
		String[] CommiQualif = getRequestStringParameters(CAMPO_COMMA_QUALIFICANTE);
		String[] Lettere = getRequestStringParameters(CAMPO_LETTERA);
		String[] Numeri = getRequestStringParameters(CAMPO_NUMERO);
		String[] Articoli = getRequestStringParameters(CAMPO_ARTICOLO);
		String[] Cablati = { "" };

		String lMotivo = "";
		lMotivo = getRequestStringParameter(ICostantiTitoloCumulato.CAMPO_MOTIVO_MODIFICA);

		if (getRequestStringParameters("cablati") != null)
			Cablati = getRequestStringParameters("cablati");

		if (CodFonti.length > 0) {
			for (int i = 0; i < 5; i++) {
				if (CodFonti[i] != null && !CodFonti[i].equals("-")) {
					lCirMod = new CircostanzaCumuloModel();
					lCirMod.setCodFonte(CodFonti[i]);

					if (AnnoFonti[i] != null && !AnnoFonti[i].equals(""))
						lCirMod.setAnnoFonte(new BigDecimal(AnnoFonti[i]));

					lCirMod.setNumeroFonte(NumeroFonti[i]);
					lCirMod.setArticolo(Articoli[i]);
					lCirMod.setCodSottonumerazione(SottoNum[i]);
					lCirMod.setComma(Commi[i]);
					lCirMod.setCommaQualificante(CommiQualif[i]);
					lCirMod.setLettera(Lettere[i]);
					lCirMod.setNumero(Numeri[i]);

					lCirMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lCirMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lCirMod.setDataInserimento(DateUtils.getSysDate());

					lCirMod.setFlagStato("I");
					lCirMod.setTitIdTitoloCumulato(aIdTitoCum);
					if (!lMotivo.equals("") && !lMotivo.equals(null))
						lCirMod.setMotivoModifica(lMotivo);

					VCRModel.add(lCirMod);
					siesLogger.debug("Circostanza_Cumulo = " + lCirMod);

					// CAMPI EX SENTENZA
					// this.verifyExCampiSentenza(lCirMod);

				}
			}
		}

		String fonte = new String("");
		String articolo = new String("");
		String comma = new String("");
		String numero = new String("");
		String sottoNum = new String("-");
		String commaqualif = new String("-");

		// ====================================================================================================
		// Cablati: sono le circostanze fisse scolpite nella form
		// ====================================================================================================

		if (getRequestStringParameters("cablati") != null) {
			CircostanzaCumuloModel lCirMod2;
			// for (int i = 0; i < Cablati.length - 1; i++) //-1 perchè c'è sempre almeno un cablati in hidden
			// da NON Considerare
			for (int i = 1; i < Cablati.length; i++) // Inizia con i=1 perchè c'è sempre un elemento [0] dei
														// cablati in hidden da NON Considerare
			{
				lCirMod2 = new CircostanzaCumuloModel();
				lCirMod2.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lCirMod2.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lCirMod2.setDataInserimento(DateUtils.getSysDate());

				lCirMod2.setFlagStato("I");
				lCirMod2.setTitIdTitoloCumulato(aIdTitoCum);
				if (!lMotivo.equals("") && !lMotivo.equals(null))
					lCirMod2.setMotivoModifica(lMotivo);

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
				lCirMod2.setCommaQualificante(commaqualif);

				// CAMPI EX SENTENZA
				this.verifyExCampiSentenza(lCirMod2);

				VCRModel.add(lCirMod2);
				siesLogger.debug("Circostanza_Cumulo Cablati = " + lCirMod2);

			}
		}

		// ====================================================================================================
		// Spunta inserita nella form su: APPLICAZIONE PENA (ART.444) e GIUDIZIO ABBREVIATO (ART. 442)
		// ====================================================================================================
		flagGiudizio = "";
		flagSentenza = "";

		if (!isRequestParameterNullObj(CAMPO_FLAG_GIUDIZIO_ABBREVIATO)
				&& getRequestStringParameter(CAMPO_FLAG_GIUDIZIO_ABBREVIATO) != null
				&& getRequestStringParameter(CAMPO_FLAG_GIUDIZIO_ABBREVIATO).equals("S")) {
			flagGiudizio = "S";

			lCirMod = new CircostanzaCumuloModel();
			lCirMod.setCodFonte("25");

			// lCirMod.setComma("2");
			lCirMod.setArticolo("442");
			lCirMod.setCodSottonumerazione("-");
			lCirMod.setCommaQualificante("-");

			lCirMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lCirMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lCirMod.setDataInserimento(DateUtils.getSysDate());

			lCirMod.setFlagStato("I");
			lCirMod.setTitIdTitoloCumulato(aIdTitoCum);
			if (!lMotivo.equals("") && !lMotivo.equals(null))
				lCirMod.setMotivoModifica(lMotivo);

			VCRModel.add(lCirMod);
			siesLogger.debug("Circostanza_Cumulo GIUDIZIO_ABBREVIATO INSERITO 442 = " + lCirMod);
		}

		if (!isRequestParameterNullObj(CAMPO_FLAG_GIUDIZIO_ABBREVIATO)
				&& getRequestStringParameter(CAMPO_FLAG_GIUDIZIO_ABBREVIATO) != null
				&& getRequestStringParameter(CAMPO_FLAG_GIUDIZIO_ABBREVIATO).equals("V")) {
			flagGiudizio = "S";
		}

		// ----

		if (!isRequestParameterNullObj(CAMPO_FLAG_SENTENZA_APPLICAZ_PENA)
				&& getRequestStringParameter(CAMPO_FLAG_SENTENZA_APPLICAZ_PENA) != null
				&& getRequestStringParameter(CAMPO_FLAG_SENTENZA_APPLICAZ_PENA).equals("S")) {
			flagSentenza = "S";

			lCirMod = new CircostanzaCumuloModel();
			lCirMod.setCodFonte("25");
			// lCirMod.setComma("2");
			lCirMod.setArticolo("444");
			lCirMod.setCodSottonumerazione("-");
			lCirMod.setCommaQualificante("-");

			lCirMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lCirMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lCirMod.setDataInserimento(DateUtils.getSysDate());

			lCirMod.setFlagStato("I");
			lCirMod.setTitIdTitoloCumulato(aIdTitoCum);
			if (!lMotivo.equals("") && !lMotivo.equals(null))
				lCirMod.setMotivoModifica(lMotivo);

			VCRModel.add(lCirMod);
			siesLogger.debug("Circostanza_Cumulo APPLICAZ_PENA INSERITO 444 " + lCirMod);
		}

		if (!isRequestParameterNullObj(CAMPO_FLAG_SENTENZA_APPLICAZ_PENA)
				&& getRequestStringParameter(CAMPO_FLAG_SENTENZA_APPLICAZ_PENA) != null
				&& getRequestStringParameter(CAMPO_FLAG_SENTENZA_APPLICAZ_PENA).equals("V")) {
			flagSentenza = "S";
		}

		// ----

		// CAMPI EX SENTENZA
		this.verifyExCampiSentenza(lCirMod);

		// VALORI CHE DEVONO ESSERE PASSATI PER AGGIORNAMENTO A TAPPETO
		CircostanzaCumuloModel circTest = new CircostanzaCumuloModel();
		this.verifyExCampiSentenza(circTest);

		flagAgg = false;
		codBil = "";
		noteBil = "";

		if (!getRequestStringParameter("verifyCampiComuni").equals("0"))
			flagAgg = true;

		if (flagAgg) {
			if (circTest.getCodBilanciamentoCircostanze() != null)
				codBil = circTest.getCodBilanciamentoCircostanze();

			if (circTest.getNoteBilanciamento() != null)
				noteBil = circTest.getNoteBilanciamento();
		}
	} // CHIUDE letturaDati()

} // CHIUDE Classe()