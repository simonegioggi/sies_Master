package siap.siep.modulocumulo.action;

/**
* <p>Title: ActInserisciReatoCumulo</p>
* <p>Description: Classe Action per l'inserimento di Reato</p>
* <p>      in ambito Cumulo (tabelle Reato_Cumulo)</p>
*/

import java.math.BigDecimal;
import java.util.ArrayList;

import siap.siep.cumulo.action.ICostantiCumulo;
import siap.siep.modulocumulo.controller.IReatoCumulo;
import siap.siep.modulocumulo.model.ReatoCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciReatoCumulo extends ActionModuloCumulo
		implements ICostantiReatoCumulo, ICostantiCumulo {

	/**
	 * Elenco Reati da inserire.
	 */
	protected ArrayList lReati = new ArrayList();

	/**
	 * Azione di Inserimento del Reato legato al titolo Cumulato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		// Funzione di lettura dei dati di input
		letturaDati();

		if (lReati.size() == 0)
			throw new F3BException(F3BException.USER_MESSAGE, "Specificare almeno un reato!");

		IReatoCumulo lCtrl = SIEPLookupRemote.getReatoCumuloRemote();
		/* ReatoCumuloModel lReatoPrincipale = */lCtrl.ExInserisciReatiCumulo(lReati);

		// setta la risposta nella request
		setRequestAttribute("ComingFromInsert", "YES");

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActRicercaReatoCumulo";

		return lPage;
	}

	/**
	 * Lettura dei dati dalla form di input e valorizzazione dell'elenco lReati.
	 * 
	 * @param aIdFascicoloSiep
	 * @throws Exception
	 */
	protected void letturaDati() throws Exception {
		BigDecimal lIdTitolo = getRequestBigDecimalParameter(
				ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);

		String fonte = new String("");
		String articolo = new String("");
		String comma = new String("");
		String numero = new String("");

		int spacePos1;
		int spacePos2;
		ReatoCumuloModel lReaMod = new ReatoCumuloModel();

		String[] CodFonti = getRequestStringParameters(CAMPO_COD_FONTE);
		String[] AnnoFonti = getRequestStringParameters(CAMPO_ANNO_FONTE);
		String[] NumeroFonti = getRequestStringParameters(CAMPO_NUMERO_FONTE);
		String[] Articoli = getRequestStringParameters(CAMPO_ARTICOLO);
		String[] SottoNum = getRequestStringParameters(CAMPO_COD_SOTTONUMERAZIONE);
		String[] Commi = getRequestStringParameters(CAMPO_COMMA);
		String[] CommaQual = getRequestStringParameters(CAMPO_COMMA_QUALIFICANTE);
		String[] Lettere = getRequestStringParameters(CAMPO_LETTERA);
		String[] Numeri = getRequestStringParameters(CAMPO_NUMERO);

		String[] Cablati = { "" };
		String[] Cablati2 = { "" };
		if (getRequestStringParameters("cablati") != null)
			Cablati = getRequestStringParameters("cablati");
		if (getRequestStringParameters("cablati2") != null)
			Cablati2 = getRequestStringParameters("cablati2");

		// ========================================================================
		// 110CP - 56CP - 81CPC1 - 81CPC2
		// ========================================================================
		if (getRequestStringParameters("cablati2") != null) {
			ReatoCumuloModel lReaMod2;
			for (int i = 0; i < Cablati2.length; i++) // -1 perchè c'è sempre almeno un cablati in hidden
			{
				if (!"".equals(Cablati2[i])) // Salto quelli hidden vuoto
				{
					lReaMod2 = new ReatoCumuloModel();

					comma = "";
					spacePos1 = Cablati2[i].indexOf(" ");
					articolo = Cablati2[i].substring(0, spacePos1).trim();

					spacePos2 = Cablati2[i].lastIndexOf(" ");
					if (spacePos2 == spacePos1) {
						fonte = Cablati2[i].substring(spacePos1).trim();
						comma = "";
					} else {
						fonte = Cablati2[i].substring(spacePos1, spacePos2).trim();
						char c = 'C';
						char nullChar = ' ';
						comma = Cablati2[i].substring(spacePos2).replace(c, nullChar).trim();
					}

					lReaMod2.setCodFonte(fonte);
					lReaMod2.setComma(comma);
					lReaMod2.setCommaQualificante("-");
					lReaMod2.setArticolo(articolo);
					lReaMod2.setCodSottonumerazione("-");

					// * Parte comune *
					lReaMod2.setProgrNumeroManuale(getRequestStringParameter(CAMPO_PROGR_NUMERO_MANUALE));

					lReaMod2.setCodTipoReato(getRequestStringParameter(CAMPO_COD_TIPO_REATO));

					// ?-- lReaMod2.setDataReato( getRequestDateParameter(
					// CAMPO_ANNO_DATA_REATO,CAMPO_MESE_DATA_REATO,CAMPO_GIORNO_DATA_REATO) );

					lReaMod2.setCodPeriodoConsumazione(
							getRequestStringParameter(CAMPO_COD_PERIODO_CONSUMAZIONE));
					lReaMod2.setDescLuogo(getRequestStringParameter(CAMPO_DESC_LUOGO).toUpperCase());

					// <Data1>
					if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_INIZIO)
							&& getRequestStringParameter(CAMPO_ANNO_DATA_INIZIO) != null) {
						lReaMod2.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO,
								CAMPO_MESE_DATA_INIZIO, CAMPO_GIORNO_DATA_INIZIO));
						lReaMod2.setAnnoInizio(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_INIZIO));
						lReaMod2.setMeseInizio(getRequestBigDecimalParameter(CAMPO_MESE_DATA_INIZIO));
						lReaMod2.setGiornoInizio(getRequestBigDecimalParameter(CAMPO_GIORNO_DATA_INIZIO));
					}

					// <Data2>
					if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_FINE)
							&& getRequestStringParameter(CAMPO_ANNO_DATA_FINE) != null) {
						lReaMod2.setDataFine(getRequestDateParameter(CAMPO_ANNO_DATA_FINE,
								CAMPO_MESE_DATA_FINE, CAMPO_GIORNO_DATA_FINE));
						lReaMod2.setAnnoFine(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_FINE));
						lReaMod2.setMeseFine(getRequestBigDecimalParameter(CAMPO_MESE_DATA_FINE));
						lReaMod2.setGiornoFine(getRequestBigDecimalParameter(CAMPO_GIORNO_DATA_FINE));
					}

					lReaMod2.setCodTipoPenaDetentiva("-"); // Per le join
					lReaMod2.setCodTipoSanzione("-"); // Per le join

					lReaMod2.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lReaMod2.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lReaMod2.setDataInserimento(DateUtils.getSysDate());

					lReaMod2.setNote(getRequestStringParameter(ICostantiReatoCumulo.CAMPO_NOTE));

					// Parte Strettamente Cumulo
					lReaMod2.setFlagStato("I");
					lReaMod2.setMotivoModificaNote(
							getRequestStringParameter(ICostantiReatoCumulo.CAMPO_MOTIVO_MODIFICA));
					lReaMod2.setTitIdTitoloCumulato(lIdTitolo);

					lReati.add(lReaMod2);
				}
			} // chiude for

		} // chiude if cablati2

		// ========================================================================
		// Dati inseriti nella sezione con le 5 fonti
		// ========================================================================
		if (CodFonti.length > 0) {
			for (int i = 0; i < 5; i++) {
				if (CodFonti[i] != null && !CodFonti[i].equals("-")) {
					lReaMod = new ReatoCumuloModel();

					lReaMod.setCodFonte(CodFonti[i]);
					if (AnnoFonti[i] != null && !AnnoFonti[i].equals("")) {
						lReaMod.setAnnoFonte(new BigDecimal(AnnoFonti[i]));
					}

					lReaMod.setNumeroFonte(NumeroFonti[i]);
					lReaMod.setCodSottonumerazione(SottoNum[i]);
					lReaMod.setComma(Commi[i]);
					lReaMod.setCommaQualificante(CommaQual[i]);
					lReaMod.setLettera(Lettere[i]);
					lReaMod.setNumero(Numeri[i]);
					lReaMod.setArticolo(Articoli[i]);

					// * Parte comune *
					lReaMod.setProgrNumeroManuale(getRequestStringParameter(CAMPO_PROGR_NUMERO_MANUALE));

					lReaMod.setCodTipoReato(getRequestStringParameter(CAMPO_COD_TIPO_REATO));

					// ?-- lReaMod.setDataReato( getRequestDateParameter(
					// CAMPO_ANNO_DATA_REATO,CAMPO_MESE_DATA_REATO,CAMPO_GIORNO_DATA_REATO) );

					lReaMod.setCodPeriodoConsumazione(
							getRequestStringParameter(CAMPO_COD_PERIODO_CONSUMAZIONE));
					lReaMod.setDescLuogo(getRequestStringParameter(CAMPO_DESC_LUOGO).toUpperCase());

					// <Data1>
					if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_INIZIO)
							&& getRequestStringParameter(CAMPO_ANNO_DATA_INIZIO) != null) {
						lReaMod.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO,
								CAMPO_MESE_DATA_INIZIO, CAMPO_GIORNO_DATA_INIZIO));
						lReaMod.setAnnoInizio(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_INIZIO));
						lReaMod.setMeseInizio(getRequestBigDecimalParameter(CAMPO_MESE_DATA_INIZIO));
						lReaMod.setGiornoInizio(getRequestBigDecimalParameter(CAMPO_GIORNO_DATA_INIZIO));
					}

					// <Data2>
					if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_FINE)
							&& getRequestStringParameter(CAMPO_ANNO_DATA_FINE) != null) {
						lReaMod.setDataFine(getRequestDateParameter(CAMPO_ANNO_DATA_FINE,
								CAMPO_MESE_DATA_FINE, CAMPO_GIORNO_DATA_FINE));
						lReaMod.setAnnoFine(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_FINE));
						lReaMod.setMeseFine(getRequestBigDecimalParameter(CAMPO_MESE_DATA_FINE));
						lReaMod.setGiornoFine(getRequestBigDecimalParameter(CAMPO_GIORNO_DATA_FINE));
					}

					lReaMod.setNote(getRequestStringParameter(ICostantiReatoCumulo.CAMPO_NOTE));

					lReaMod.setCodTipoPenaDetentiva("-"); // Per le join
					lReaMod.setCodTipoSanzione("-"); // Per le join

					lReaMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lReaMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lReaMod.setDataInserimento(DateUtils.getSysDate());

					// Parte Strettamente Cumulo
					lReaMod.setFlagStato("I");
					lReaMod.setMotivoModificaNote(
							getRequestStringParameter(ICostantiReatoCumulo.CAMPO_MOTIVO_MODIFICA));
					lReaMod.setTitIdTitoloCumulato(lIdTitolo);

					lReati.add(lReaMod);
				}
			}
		}

		// ========================================================================
		// Sezione con gli altri reati: 61CP - 112CP - 625CP
		// ========================================================================
		fonte = new String("");
		articolo = new String("");
		comma = new String("");
		numero = new String("");

		if (getRequestStringParameters("cablati") != null) {
			ReatoCumuloModel lReaMod2;
			for (int i = 0; i < Cablati.length; i++) {
				if (!"".equals(Cablati[i])) // Salto quello hidden vuoto
				{
					lReaMod2 = new ReatoCumuloModel();

					comma = "";
					spacePos1 = Cablati[i].indexOf(" ");
					articolo = Cablati[i].substring(0, spacePos1).trim();

					spacePos2 = Cablati[i].lastIndexOf(" ");
					if (spacePos2 == spacePos1) {
						fonte = Cablati[i].substring(spacePos1).trim();
						comma = "";
					} else {
						fonte = Cablati[i].substring(spacePos1, spacePos2).trim();
						if (Cablati[i].substring(spacePos2, spacePos2 + 2).trim().equals("N")) {
							char n = 'N';
							char nullChar = ' ';
							numero = Cablati[i].substring(spacePos2).replace(n, nullChar).trim();

						} else if (Cablati[i].substring(spacePos2, spacePos2 + 2).trim().equals("C")) {
							char c = 'C';
							char nullChar = ' ';
							comma = Cablati[i].substring(spacePos2).replace(c, nullChar).trim();

						}
					}

					lReaMod2.setCodFonte(fonte);
					lReaMod2.setComma(comma);
					lReaMod2.setCommaQualificante("-");
					lReaMod2.setNumero(numero);
					lReaMod2.setArticolo(articolo);
					lReaMod2.setCodSottonumerazione("-");

					// * Parte comune *
					lReaMod2.setProgrNumeroManuale(getRequestStringParameter(CAMPO_PROGR_NUMERO_MANUALE));

					lReaMod2.setCodTipoReato(getRequestStringParameter(CAMPO_COD_TIPO_REATO));

					// ?-- lReaMod2.setDataReato( getRequestDateParameter(
					// CAMPO_ANNO_DATA_REATO,CAMPO_MESE_DATA_REATO,CAMPO_GIORNO_DATA_REATO) );

					lReaMod2.setCodPeriodoConsumazione(
							getRequestStringParameter(CAMPO_COD_PERIODO_CONSUMAZIONE));
					lReaMod2.setDescLuogo(getRequestStringParameter(CAMPO_DESC_LUOGO).toUpperCase());

					// <Data1>
					if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_INIZIO)
							&& getRequestStringParameter(CAMPO_ANNO_DATA_INIZIO) != null) {
						lReaMod2.setDataInizio(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO,
								CAMPO_MESE_DATA_INIZIO, CAMPO_GIORNO_DATA_INIZIO));
						lReaMod2.setAnnoInizio(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_INIZIO));
						lReaMod2.setMeseInizio(getRequestBigDecimalParameter(CAMPO_MESE_DATA_INIZIO));
						lReaMod2.setGiornoInizio(getRequestBigDecimalParameter(CAMPO_GIORNO_DATA_INIZIO));
					}

					// <Data2>
					if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_FINE)
							&& getRequestStringParameter(CAMPO_ANNO_DATA_FINE) != null) {
						lReaMod2.setDataFine(getRequestDateParameter(CAMPO_ANNO_DATA_FINE,
								CAMPO_MESE_DATA_FINE, CAMPO_GIORNO_DATA_FINE));
						lReaMod2.setAnnoFine(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_FINE));
						lReaMod2.setMeseFine(getRequestBigDecimalParameter(CAMPO_MESE_DATA_FINE));
						lReaMod2.setGiornoFine(getRequestBigDecimalParameter(CAMPO_GIORNO_DATA_FINE));
					}

					lReaMod2.setNote(getRequestStringParameter(ICostantiReatoCumulo.CAMPO_NOTE));

					lReaMod2.setCodTipoPenaDetentiva("-"); // Per le join
					lReaMod2.setCodTipoSanzione("-"); // Per le join

					lReaMod2.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lReaMod2.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lReaMod2.setDataInserimento(DateUtils.getSysDate());

					// Parte Strettamente Cumulo
					lReaMod2.setFlagStato("I");
					lReaMod2.setMotivoModificaNote(
							getRequestStringParameter(ICostantiReatoCumulo.CAMPO_MOTIVO_MODIFICA));
					lReaMod2.setTitIdTitoloCumulato(lIdTitolo);

					lReati.add(lReaMod2);
				}
			} // chiude for
		} // chiude if cablati
	} // chiude void letturadati

} // chiude classe