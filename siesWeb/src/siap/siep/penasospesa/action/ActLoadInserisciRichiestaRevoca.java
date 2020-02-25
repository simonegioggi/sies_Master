package siap.siep.penasospesa.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.action.ICostantiPenaComplessiva;
import siap.siep.penacomplessiva.model.DettaglioPenaComplessivaModel;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.reato.action.ICostantiReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciRicEstinzioneReato
 * </p>
 * <p>
 * Description: Classe Action per la Load Inserisci di una Richiesta Revoca Beneficio ex art.168 c.p. - 674
 * c.p.p.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: Agile
 * </p>
 * <p>
 * @author: Luigi
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciRichiestaRevoca extends ActLoadInserisciRichiesta
		implements ICostantiReato, ICostantiPenaComplessiva, ICostantiSanzioneSostitutiva {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	// protected ArrayList lReati = new ArrayList();
	protected java.util.Vector lReati = new java.util.Vector();

	protected PenaComplessivaModel mPenaCompMod = new PenaComplessivaModel();
	// SANZIONE SOSTITUTIVA
	protected SanzioneSostitutivaModel mSanzSostMod = null;
	// CONTINUAZIONE CON ALTRI REATI
	protected List mContList = new ArrayList();

	public String processRequest() throws Exception {

		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setCodiceAlternativo("REVOCA");
		Collection lColMotivo = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);
		setRequestAttribute("oggetto", lColMotivo);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		setRequestAttribute("autoritaSentenza", "" + lOption);

		// Esclusione di "CSS" e "PM" da TipoUfficioS.
		Collection lColl = DecodificheUtils.getDecodesWithoutCodes(
				DecodificheManager.getInstance().getTipoUfficioS(), new String[] { "CSS", "PM" });
		// Collection lColl2 = DecodificheUtils.getDecodesWithCodes(
		// DecodificheManager.getInstance().getTipoUfficio(), new String[] { "GIPMI", "TMI" });

		lOption = new Option(lColl, "-");
		setRequestAttribute("autoritaOrdinanza", "" + lOption);

		// ufficio giudice dell'esecuzione
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioGE());
		setRequestAttribute("ufficioge", "" + lOption);

		Vector vetart_qual = new Vector(DecodificheManager.getInstance().getSottonumerazione());
		setRequestAttribute("TipiSottonumerazione", "" + vetart_qual);

		mNomeAction = "siap.siep.penasospesa.action.ActLoadInserisciRichiestaRevoca";
		mNomeJsp = IWebConstants.ROOT_DIR + "files/siap/siep/penasospesa/LoadInserisciRichiestaRevoca.jsp"; // PG_LOAD_INSERISCIRICHIESTAREVOCA;

		if (!isRequestParameterNullObj("TipoRevo"))
			setRequestAttribute("TipoRevo", "noObblighi");
		else
			setRequestAttribute("TipoRevo", "");

		if (!isRequestParameterNullObj("dareati")) {
			FascicoloSiepModel lFascicolo = (FascicoloSiepModel) getSessionAttribute("fascicolo");
			letturaDati(lFascicolo.getIdFascicoloSiep());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("2!! Attivata da InserimentoREATI LEN aReati -> " + lReati.size());
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Inserimento Avvenuto Correttamente!");
			mNomeJsp = IWebConstants.PG_MESSAGE;
			this.setSessionAttribute("reati", lReati);
		}

		DettaglioPenaComplessivaModel dettpenacompl = new DettaglioPenaComplessivaModel();
		if (!isRequestParameterNullObj("dapenacomplessiva")) {
//			FascicoloSiepModel lFascicolo = (FascicoloSiepModel) getSessionAttribute("fascicolo");
			letturaPenaCompl();
			// DettaglioPenaComplessivaModel dettpenacompl=new DettaglioPenaComplessivaModel();
			PenaComplessivaSanzioneSostitutivaModel pcss = new PenaComplessivaSanzioneSostitutivaModel();
			pcss.setPenaComplessiva(mPenaCompMod);
			pcss.setSanzioneSostitutiva(mSanzSostMod);

			dettpenacompl.setPenaComplessivaSanzioneSostitutiva(pcss);
			this.setSessionAttribute("dettaglioPenaComplessiva", dettpenacompl);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Penacomplessiva -> " + pcss.getPenaComplessiva());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Penacomplessiva -> " + pcss.getSanzioneSostitutiva());
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Inserimento Avvenuto Correttamente!");
			mNomeJsp = IWebConstants.PG_MESSAGE;

		}

		return super.processRequest();

	}

	/**
	 * Lettura dei dati dalla form di input e valorizzazione dell'elenco lReati.
	 * 
	 * @param aIdFascicoloSiep
	 * @throws Exception
	 */
	protected void letturaDati(BigDecimal aIdFascicoloSiep) throws Exception {
		String fonte = new String("");
		String articolo = new String("");
		String comma = new String("");
		String numero = new String("");

		int spacePos1;
		int spacePos2;
		ReatoModel lReaMod = new ReatoModel();

		String[] CodFonti = getRequestStringParameters(ICostantiReato.CAMPO_COD_FONTE);
		String[] AnnoFonti = getRequestStringParameters(CAMPO_ANNO_FONTE);
		String[] NumeroFonti = getRequestStringParameters(CAMPO_NUMERO_FONTE);
		String[] SottoNum = getRequestStringParameters(CAMPO_COD_SOTTONUMERAZIONE);
		String[] Commi = getRequestStringParameters(CAMPO_COMMA);
		// ***********************************************************************************
		// Federica - a9-rr-078
		// aggiunto campo Comma-Qualificante
		String[] CommaQualif = getRequestStringParameters(CAMPO_COMMA_QUALIFICANTE);
		// ***********************************************************************************
		String[] Lettere = getRequestStringParameters(CAMPO_LETTERA);
		String[] Numeri = getRequestStringParameters(CAMPO_NUMERO);
		String[] Articoli = getRequestStringParameters(CAMPO_ARTICOLO);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("FONTI -> " + CodFonti.toString());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Articoli -> " + Articoli.toString());

		String[] Cablati = { "" };
		String[] Cablati2 = { "" };
		if (getRequestStringParameters("cablati") != null)
			Cablati = getRequestStringParameters("cablati");
		if (getRequestStringParameters("cablati2") != null)
			Cablati2 = getRequestStringParameters("cablati2");

		if (getRequestStringParameters("cablati2") != null) {
			ReatoModel lReaMod2;
			for (int i = 0; i < Cablati2.length - 1; i++) // -1 perchè c'è sempre almeno un cablati in hidden
			{
				lReaMod2 = new ReatoModel();
				lReaMod2.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lReaMod2.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lReaMod2.setDataInserimento(DateUtils.getSysDate());

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
				lReaMod2.setArticolo(articolo);
				lReaMod2.setCodSottonumerazione("-");
				// *******************************************
				// Federica - a9-rr-078
				// aggiunto campo Comma-Qualificante
				lReaMod2.setCommaQualificante("-");
				// *******************************************

				// * Parte comune *
				lReaMod2.setProgrNumeroManuale(getRequestStringParameter(CAMPO_PROGR_NUMERO_MANUALE));

				lReaMod2.setCodTipoReato(getRequestStringParameter(CAMPO_COD_TIPO_REATO));

				// ?-- lReaMod2.setDataReato( getRequestDateParameter(
				// CAMPO_ANNO_DATA_REATO,CAMPO_MESE_DATA_REATO,CAMPO_GIORNO_DATA_REATO) );

				lReaMod2.setCodPeriodoConsumazione(getRequestStringParameter(CAMPO_COD_PERIODO_CONSUMAZIONE));
				lReaMod2.setDescLuogo(getRequestStringParameter(CAMPO_DESC_LUOGO).toUpperCase());

				// <Data1>

				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_INIZIO))
					lReaMod2.setAnnoInizio(
							getRequestBigDecimalParameter(ICostantiReato.CAMPO_ANNO_DATA_INIZIO));
				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_MESE_DATA_INIZIO))
					lReaMod2.setMeseInizio(
							getRequestBigDecimalParameter(ICostantiReato.CAMPO_MESE_DATA_INIZIO));
				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_GIORNO_DATA_INIZIO))
					lReaMod2.setGiornoInizio(
							getRequestBigDecimalParameter(ICostantiReato.CAMPO_GIORNO_DATA_INIZIO));

				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_INIZIO)) {
					lReaMod2.setDataInizio(getRequestDateParameter(ICostantiReato.CAMPO_ANNO_DATA_INIZIO,
							ICostantiReato.CAMPO_MESE_DATA_INIZIO, ICostantiReato.CAMPO_GIORNO_DATA_INIZIO));
				}

				// <Data2>

				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_FINE))
					lReaMod2.setAnnoFine(getRequestBigDecimalParameter(ICostantiReato.CAMPO_ANNO_DATA_FINE));
				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_MESE_DATA_FINE))
					lReaMod2.setMeseFine(getRequestBigDecimalParameter(ICostantiReato.CAMPO_MESE_DATA_FINE));
				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_GIORNO_DATA_FINE))
					lReaMod2.setGiornoFine(
							getRequestBigDecimalParameter(ICostantiReato.CAMPO_GIORNO_DATA_FINE));

				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_FINE)) {
					lReaMod2.setDataFine(getRequestDateParameter(ICostantiReato.CAMPO_ANNO_DATA_FINE,
							ICostantiReato.CAMPO_MESE_DATA_FINE, ICostantiReato.CAMPO_GIORNO_DATA_FINE));
				}

				//

				lReaMod2.setNote(getRequestStringParameter(ICostantiReato.CAMPO_NOTE));

				lReaMod2.setCodTipoPenaDetentiva("-"); // Per le join
				lReaMod2.setCodTipoSanzione("-"); // Per le join

				lReaMod2.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lReaMod2.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lReaMod2.setDataInserimento(DateUtils.getSysDate());

				lReaMod2.setFasSieIdFascicoloSiep(aIdFascicoloSiep);

				lReati.add(lReaMod2);
			}
		}

		if (CodFonti.length > 0) {
			for (int i = 0; i < 5; i++) {
				if (CodFonti[i] != null && !CodFonti[i].equals("-")) {
					lReaMod = new ReatoModel();
					lReaMod.setCodFonte(CodFonti[i]);
					String titi = "descrfonte" + (i + 1);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("titi=" + titi);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("getRequestStringParameter(=" + getRequestStringParameter(titi));
					lReaMod.setDescrFonte(getRequestStringParameter(titi));
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("fonte=" + CodFonti[i]);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("articolo=" + Articoli[i]);
					if (AnnoFonti[i] != null && !AnnoFonti[i].equals("")) {
						lReaMod.setAnnoFonte(new BigDecimal(AnnoFonti[i]));
					}
					lReaMod.setNumeroFonte(NumeroFonti[i]);
					lReaMod.setCodSottonumerazione(SottoNum[i]);
					lReaMod.setComma(Commi[i]);
					// *******************************************
					// Federica - a9-rr-078
					// aggiunto campo Comma-Qualificante
					lReaMod.setCommaQualificante(CommaQualif[i]);
					// *******************************************
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

					if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_INIZIO))
						lReaMod.setAnnoInizio(
								getRequestBigDecimalParameter(ICostantiReato.CAMPO_ANNO_DATA_INIZIO));
					if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_MESE_DATA_INIZIO))
						lReaMod.setMeseInizio(
								getRequestBigDecimalParameter(ICostantiReato.CAMPO_MESE_DATA_INIZIO));
					if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_GIORNO_DATA_INIZIO))
						lReaMod.setGiornoInizio(
								getRequestBigDecimalParameter(ICostantiReato.CAMPO_GIORNO_DATA_INIZIO));

					if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_INIZIO)) {
						lReaMod.setDataInizio(getRequestDateParameter(ICostantiReato.CAMPO_ANNO_DATA_INIZIO,
								ICostantiReato.CAMPO_MESE_DATA_INIZIO,
								ICostantiReato.CAMPO_GIORNO_DATA_INIZIO));
					}

					// <Data2>

					if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_FINE))
						lReaMod.setAnnoFine(
								getRequestBigDecimalParameter(ICostantiReato.CAMPO_ANNO_DATA_FINE));
					if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_MESE_DATA_FINE))
						lReaMod.setMeseFine(
								getRequestBigDecimalParameter(ICostantiReato.CAMPO_MESE_DATA_FINE));
					if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_GIORNO_DATA_FINE))
						lReaMod.setGiornoFine(
								getRequestBigDecimalParameter(ICostantiReato.CAMPO_GIORNO_DATA_FINE));

					if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_FINE)) {
						lReaMod.setDataFine(getRequestDateParameter(ICostantiReato.CAMPO_ANNO_DATA_FINE,
								ICostantiReato.CAMPO_MESE_DATA_FINE, ICostantiReato.CAMPO_GIORNO_DATA_FINE));
					}

					//

					lReaMod.setNote(getRequestStringParameter(ICostantiReato.CAMPO_NOTE));

					lReaMod.setCodTipoPenaDetentiva("-"); // Per le join
					lReaMod.setCodTipoSanzione("-"); // Per le join

					lReaMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lReaMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lReaMod.setDataInserimento(DateUtils.getSysDate());

					lReaMod.setFasSieIdFascicoloSiep(aIdFascicoloSiep);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug("REATO=" + lReaMod);

					lReati.add(lReaMod);
				}
			}
		}

		fonte = new String("");
		articolo = new String("");
		comma = new String("");
		numero = new String("");

		if (getRequestStringParameters("cablati") != null) {
			ReatoModel lReaMod2;
			for (int i = 0; i < Cablati.length - 1; i++) // -1 perchè c'è sempre almeno un cablati in hidden
			{
				lReaMod2 = new ReatoModel();
				lReaMod2.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lReaMod2.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lReaMod2.setDataInserimento(DateUtils.getSysDate());

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
				lReaMod2.setNumero(numero);

				lReaMod2.setArticolo(articolo);
				lReaMod2.setCodSottonumerazione("-");
				// *******************************************
				// Federica - a9-rr-078
				// aggiunto campo Comma-Qualificante
				lReaMod2.setCommaQualificante("-");
				// *******************************************

				// * Parte comune *
				lReaMod2.setProgrNumeroManuale(getRequestStringParameter(CAMPO_PROGR_NUMERO_MANUALE));

				lReaMod2.setCodTipoReato(getRequestStringParameter(CAMPO_COD_TIPO_REATO));

				// ?-- lReaMod2.setDataReato( getRequestDateParameter(
				// CAMPO_ANNO_DATA_REATO,CAMPO_MESE_DATA_REATO,CAMPO_GIORNO_DATA_REATO) );

				lReaMod2.setCodPeriodoConsumazione(getRequestStringParameter(CAMPO_COD_PERIODO_CONSUMAZIONE));
				lReaMod2.setDescLuogo(getRequestStringParameter(CAMPO_DESC_LUOGO).toUpperCase());

				// <Data1>

				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_INIZIO))
					lReaMod2.setAnnoInizio(
							getRequestBigDecimalParameter(ICostantiReato.CAMPO_ANNO_DATA_INIZIO));
				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_MESE_DATA_INIZIO))
					lReaMod2.setMeseInizio(
							getRequestBigDecimalParameter(ICostantiReato.CAMPO_MESE_DATA_INIZIO));
				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_GIORNO_DATA_INIZIO))
					lReaMod2.setGiornoInizio(
							getRequestBigDecimalParameter(ICostantiReato.CAMPO_GIORNO_DATA_INIZIO));

				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_INIZIO)) {
					lReaMod2.setDataInizio(getRequestDateParameter(ICostantiReato.CAMPO_ANNO_DATA_INIZIO,
							ICostantiReato.CAMPO_MESE_DATA_INIZIO, ICostantiReato.CAMPO_GIORNO_DATA_INIZIO));
				}

				// <Data2>

				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_FINE))
					lReaMod2.setAnnoFine(getRequestBigDecimalParameter(ICostantiReato.CAMPO_ANNO_DATA_FINE));
				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_MESE_DATA_FINE))
					lReaMod2.setMeseFine(getRequestBigDecimalParameter(ICostantiReato.CAMPO_MESE_DATA_FINE));
				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_GIORNO_DATA_FINE))
					lReaMod2.setGiornoFine(
							getRequestBigDecimalParameter(ICostantiReato.CAMPO_GIORNO_DATA_FINE));

				if (!this.isRequestParameterNullObj(ICostantiReato.CAMPO_ANNO_DATA_FINE)) {
					lReaMod2.setDataFine(getRequestDateParameter(ICostantiReato.CAMPO_ANNO_DATA_FINE,
							ICostantiReato.CAMPO_MESE_DATA_FINE, ICostantiReato.CAMPO_GIORNO_DATA_FINE));
				}

				//
				lReaMod2.setNote(getRequestStringParameter(ICostantiReato.CAMPO_NOTE));

				lReaMod2.setCodTipoPenaDetentiva("-"); // Per le join
				lReaMod2.setCodTipoSanzione("-"); // Per le join

				lReaMod2.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lReaMod2.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lReaMod2.setDataInserimento(DateUtils.getSysDate());

				lReaMod2.setFasSieIdFascicoloSiep(aIdFascicoloSiep);

				lReati.add(lReaMod2);
			}
		}
	}

	protected void letturaPenaCompl() throws Exception {
		mPenaCompMod.setCodTipoPenaDetentiva(
				getRequestStringParameter(ICostantiPenaComplessiva.CAMPO_COD_TIPO_PENA_DETENTIVA));
		mPenaCompMod.setNumAnniReclusione(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_RECLUSIONE));
		mPenaCompMod.setNumMesiReclusione(getRequestBigDecimalParameter(CAMPO_NUM_MESI_RECLUSIONE));
		mPenaCompMod.setNumGiorniReclusione(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_RECLUSIONE));

		mPenaCompMod
				.setNumAnniIsolamentoDiurno(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_ISOLAMENTO_DIURNO));
		mPenaCompMod
				.setNumMesiIsolamentoDiurno(getRequestBigDecimalParameter(CAMPO_NUM_MESI_ISOLAMENTO_DIURNO));
		mPenaCompMod.setNumGiorniIsolamentoDiurno(
				getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_ISOLAMENTO_DIURNO));

		if ((getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA) != null
				&& !(getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA)).equals(""))
				|| (getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_MULTA) != null
						&& !(getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_MULTA)).equals(""))) {
			if (getRequestStringParameter(CAMPO_VALUTA_IMPORTO_MULTA).equals("LIT")) {
				mPenaCompMod
						.setImportoMulta(Utils.toEuro(getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA)));
			} else {
				mPenaCompMod
						.setImportoMulta(new BigDecimal(getRequestStringParameter(CAMPO_INTERO_IMPORTO_MULTA)
								+ "." + getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_MULTA)));
			}
		}

		mPenaCompMod.setNumAnniArresto(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_ARRESTO));
		mPenaCompMod.setNumMesiArresto(getRequestBigDecimalParameter(CAMPO_NUM_MESI_ARRESTO));
		mPenaCompMod.setNumGiorniArresto(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_ARRESTO));
		if ((getRequestStringParameter(CAMPO_INTERO_IMPORTO_AMMENDA) != null
				&& !(getRequestStringParameter(CAMPO_INTERO_IMPORTO_AMMENDA)).equals(""))
				|| (getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_AMMENDA) != null
						&& !(getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_AMMENDA)).equals(""))) {
			if (getRequestStringParameter(CAMPO_VALUTA_IMPORTO_AMMENDA).equals("LIT")) {
				mPenaCompMod.setImportoAmmenda(
						Utils.toEuro(getRequestStringParameter(CAMPO_INTERO_IMPORTO_AMMENDA)));
			} else {
				mPenaCompMod.setImportoAmmenda(
						new BigDecimal(getRequestStringParameter(CAMPO_INTERO_IMPORTO_AMMENDA) + "."
								+ getRequestStringParameter(CAMPO_DECIMALE_IMPORTO_AMMENDA)));
			}
		}

		// ? mPenaCompMod.setDataInizio( getRequestDateParameter(
		// CAMPO_ANNO_DATA_INIZIO,CAMPO_MESE_DATA_INIZIO,CAMPO_GIORNO_DATA_INIZIO) );
		// ? mPenaCompMod.setDataFine( getRequestDateParameter(
		// CAMPO_ANNO_DATA_FINE,CAMPO_MESE_DATA_FINE,CAMPO_GIORNO_DATA_FINE) );
		mPenaCompMod.setCodTipoRito("-");

		/*
		 * mPenaCompMod.setDataInizioIsolamentoDiurno( getRequestDateParameter(
		 * CAMPO_ANNO_DATA_INIZIO_ISOLAMENTO_DIURNO, CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO,
		 * CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO) ); mPenaCompMod.setDataFineIsolamentoDiurno(
		 * getRequestDateParameter( CAMPO_ANNO_DATA_FINE_ISOLAMENTO_DIURNO,
		 * CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO, CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO) );
		 */

		mPenaCompMod.setDataPrescrizione(getRequestDateParameter(CAMPO_ANNO_DATA_PRESCRIZIONE,
				CAMPO_MESE_DATA_PRESCRIZIONE, CAMPO_GIORNO_DATA_PRESCRIZIONE));

		// SANZIONE SOSTITUTIVA
		// SanzioneSostitutivaModel mSanzSostMod = null;

		boolean flagSanzioneSostitutiva = isRequestChecked(CAMPO_FLAG_SANZIONE_SOSTITUTIVA);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Penacomplessiva ->>>flagSanzioneSostitutiva=" + flagSanzioneSostitutiva);
		if (flagSanzioneSostitutiva) {
			mSanzSostMod = new SanzioneSostitutivaModel();

			mSanzSostMod.setCodTipoSanzione(
					getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_SANZIONE));
			if (!this.isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_NUM_ANNI))
				mSanzSostMod.setNumAnni(
						getRequestBigDecimalParameter(ICostantiSanzioneSostitutiva.CAMPO_NUM_ANNI));

			if (!this.isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_NUM_MESI))
				mSanzSostMod.setNumMesi(
						getRequestBigDecimalParameter(ICostantiSanzioneSostitutiva.CAMPO_NUM_MESI));

			if (!this.isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_NUM_GIORNI))
				mSanzSostMod.setNumGiorni(
						getRequestBigDecimalParameter(ICostantiSanzioneSostitutiva.CAMPO_NUM_GIORNI));

			if (!this.isRequestParameterNullObj(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA)
					&& ((getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA) != null
							&& !(getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA))
									.equals(""))
							|| (getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA) != null
									&& !(getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA))
											.equals("")))) {
				if (getRequestStringParameter(CAMPO_VALUTA_SANZIONE_PECUNIARIA).equals("LIT")) {
					mSanzSostMod.setSanzionePecuniariaMulta(
							Utils.toEuro(getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA)));
				} else {
					mSanzSostMod.setSanzionePecuniariaMulta(new BigDecimal(
							getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_MULTA) + "."
									+ getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_MULTA)));
				}
			}

			if (!this.isRequestParameterNullObj(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA)
					&& ((getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA) != null
							&& !(getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA))
									.equals(""))
							|| (getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA) != null
									&& !(getRequestStringParameter(
											CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA)).equals("")))) {
				if (getRequestStringParameter(CAMPO_VALUTA_SANZIONE_PECUNIARIA).equals("LIT")) {
					mSanzSostMod.setSanzionePecuniariaAmmenda(Utils
							.toEuro(getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA)));
				} else {
					mSanzSostMod.setSanzionePecuniariaAmmenda(new BigDecimal(
							getRequestStringParameter(CAMPO_INTERO_SANZIONE_PECUNIARIA_AMMENDA) + "."
									+ getRequestStringParameter(CAMPO_DECIMALE_SANZIONE_PECUNIARIA_AMMENDA)));
				}
			}

			mSanzSostMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			mSanzSostMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			mSanzSostMod.setDataInserimento(DateUtils.getSysDate());
		}

		mPenaCompMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		mPenaCompMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		mPenaCompMod.setDataInserimento(DateUtils.getSysDate());
		// mPenaCompMod.setFasSieIdFascicoloSiep(aIdFascicoloSiep);

		mPenaCompMod.setFlagPenaInContinuazione("N");

	}

}