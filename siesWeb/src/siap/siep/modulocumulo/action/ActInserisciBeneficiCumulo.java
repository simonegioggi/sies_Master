package siap.siep.modulocumulo.action;

/**
* <p>Title: ActInserisciBeneficiCumulo</p>
* <p>Description: Classe Action per l'inserimento di Benefici	</p>
* <p>			 Disposti in Sentenza (titolo CUMULATO)			</p>
*
* @author d.fiorletta
*/

import java.math.BigDecimal;
import java.util.ArrayList;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.modulocumulo.controller.IBeneficioCumulo;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.tipologiaorario.action.ICostantiTipologiaOrario;
import siap.siep.tipologiaorario.model.TipologiaOrarioModel;
import siap.siep.util.SIEPLookupRemote;

public class ActInserisciBeneficiCumulo extends ActionSiap
		implements ICostantiBeneficiCumulo, ICostantiTipologiaOrario {

	protected ArrayList<TipologiaOrarioModel> mTipologie = null;
	// Beneficio
	protected BeneficioCumuloModel mBenMod = null;
	// Beneficio Non Menzione
	protected BeneficioCumuloModel mBenNMMod = null;
	// Pene Accessorie
	protected String[] mListaIdPeneAcc = null;

	/**
	 * Azione di Inserimento del Beneficio Cumulato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		// BigDecimal lIdTitolo = getRequestBigDecimalParameter(
		// ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
		String lModo = getRequestStringParameter(ICostantiModuloCumulo.MODALITA);
		String TipoBeneficio = getRequestStringParameter(ICostantiBeneficiCumulo.TIPO_FORM_BENEFICIO);

		if (lModo.compareTo("M") == 0) {
			BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_BENEFICIO_CUMULO);
			IBeneficioCumulo lCtrl = SIEPLookupRemote.getBeneficioCumuloRemote();
			BeneficioCumuloModel lBene = lCtrl.ExRicercaBeneficioCumuloByKey(lId);
			if (lBene != null && lBene.getIdBeneficioCumulo() != null) {
				mBenMod = new BeneficioCumuloModel(lBene);
			}
		} else {
			mBenMod = new BeneficioCumuloModel();
		}

		// lettura dati dalla form
		if (TipoBeneficio.equals("01"))
			letturaBeneficio(lModo);
		else if (TipoBeneficio.equals("02"))
			letturaBeneficioIndulto(lModo);

		// INSERIMENTO/MODIFICA BENEFICI
		IBeneficioCumulo lCtrl = SIEPLookupRemote.getBeneficioCumuloRemote();
		if (lModo.equals("I")) {
			if (TipoBeneficio.equals("01"))
				mBenMod = lCtrl.ExInserisciBeneficioCumuloTipOrario(mBenMod, mTipologie, null, mBenNMMod);
			else if (TipoBeneficio.equals("02"))
				mBenMod = lCtrl.ExInserisciBeneficioCumuloTipOrario(mBenMod, null, mListaIdPeneAcc, null);
		} else if (lModo.equals("M")) {
			if (TipoBeneficio.equals("01"))
				mBenMod = lCtrl.ExModificaBeneficioCumuloTipologiaOrario(mBenMod, mTipologie, null,
						mBenNMMod);
			else if (TipoBeneficio.equals("02"))
				mBenMod = lCtrl.ExModificaBeneficioCumuloTipologiaOrario(mBenMod, null, mListaIdPeneAcc,
						null);
		}

		// Prepara la destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.modulocumulo.action.ActDettaglioBeneficioCumulo&" + CAMPO_ID_BENEFICIO_CUMULO
				+ "=" + mBenMod.getIdBeneficioCumulo().toString();
		return lPage;

	}

	protected void letturaBeneficio(String aModalita) throws F3BException {
		// BENEFICI SOSPENSIONE - NON MENZIONE
		if (aModalita.compareTo("M") == 0) {
			mBenMod.setCodSottotipoBeneficio(getRequestStringParameter(CAMPO_COD_SOTTOTIPO_BENEFICIO));
			if (!"-".equals(mBenMod.getCodSottotipoBeneficio())) {
				mBenMod.setCodTipoBeneficio("01");
				mBenMod.setCodNaturaBeneficio("C");
				mBenMod.setCodDpr("-");
			} else {
				mBenMod.setCodTipoBeneficio("02");
			}

			mBenMod.setNumAnniSospensione(this.getRequestBigDecimalParameter(CAMPO_NUM_ANNI_SOSPENSIONE));

		} else {
			mBenMod.setCodTipoBeneficio("01");
			mBenMod.setCodSottotipoBeneficio(getRequestStringParameter(CAMPO_COD_SOTTOTIPO_BENEFICIO));
			mBenMod.setCodNaturaBeneficio("C");
			mBenMod.setCodDpr("-");

			if (getRequestStringParameter(CAMPO_NUM_ANNI_SOSPENSIONE) != null
					&& !getRequestStringParameter(CAMPO_NUM_ANNI_SOSPENSIONE).equals(""))
				mBenMod.setNumAnniSospensione(this.getRequestBigDecimalParameter(CAMPO_NUM_ANNI_SOSPENSIONE));
			else
				mBenMod.setNumAnniSospensione(new BigDecimal(0));

		}

		if (aModalita.compareTo("M") == 0) {
			mBenMod.setIdBeneficioCumulo(getRequestBigDecimalParameter(CAMPO_ID_BENEFICIO_CUMULO));
			mBenMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
			mBenMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
			mBenMod.setDataAggiornamento(DateUtils.getSysDate());

			if (getRequestStringParameter(CAMPO_FLAG_STATO).compareTo("I") != 0)
				mBenMod.setFlagStato("M");
		} else if (aModalita.compareTo("I") == 0) {
			mBenMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			mBenMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			mBenMod.setDataInserimento(DateUtils.getSysDate());

			mBenMod.setFlagStato("I");
		}

		mBenMod.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
		mBenMod.setMotivoModifica(getRequestStringParameter(ICostantiBeneficiCumulo.CAMPO_MOTIVO_MODIFICA));
		///////////
		String TipoSub = "";
		if (!this.isRequestParameterNullObj(CAMPO_COD_TIPO_SOSP_SUBORDINATA)) {
			TipoSub = getRequestStringParameter(CAMPO_COD_TIPO_SOSP_SUBORDINATA);
			mBenMod.setCodTipoSospSubordinata(getRequestStringParameter(CAMPO_COD_TIPO_SOSP_SUBORDINATA));
			mBenMod.setNote(getRequestStringParameter(CAMPO_NOTE));

			mBenMod.setNumAnniAdempimento(getRequestBigDecimalParameter(CAMPO_NUM_ANNI_ADEMPIMENTO));
			mBenMod.setNumMesiAdempimento(getRequestBigDecimalParameter(CAMPO_NUM_MESI_ADEMPIMENTO));
			mBenMod.setNumGiorniAdempimento(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_ADEMPIMENTO));
		} else {
			if (aModalita.compareTo("M") == 0) {
				mBenMod.setCodTipoSospSubordinata("-");
				mBenMod.setNote(null);
				mBenMod.setNumAnniAdempimento(null);
				mBenMod.setNumMesiAdempimento(null);
				mBenMod.setNumGiorniAdempimento(null);
			} else
				mBenMod.setCodTipoSospSubordinata("-");
		}

		mBenMod.setRifCodTipoAutoEmittente("-");
		mBenMod.setRifCodTipoProvvedimento("-");

		// creazione beneficio per non menzione
		if (isRequestChecked(CAMPO_FLAG_NON_MENZIONE)) {
			mBenNMMod = new BeneficioCumuloModel();
			mBenNMMod.setCodTipoBeneficio("02");
			mBenNMMod.setCodSottotipoBeneficio("-");
			mBenNMMod.setCodNaturaBeneficio("C");
			mBenNMMod.setCodDpr("-");

			mBenNMMod.setCodTipoSospSubordinata("-");

			////// colonne specifiche del cumulo
			mBenNMMod.setFlagStato("I");
			mBenNMMod.setTitIdTitoloCumulato(
					getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
			mBenNMMod.setMotivoModifica(
					getRequestStringParameter(ICostantiBeneficiCumulo.CAMPO_MOTIVO_MODIFICA));
			//////

			mBenNMMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			mBenNMMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			mBenNMMod.setDataInserimento(DateUtils.getSysDate());

			mBenNMMod.setRifCodTipoAutoEmittente("-");
			mBenNMMod.setRifCodTipoProvvedimento("-");
		}

		if (TipoSub.compareTo("08") == 0) {
			mBenMod.setNumGiorniPrestazione(getRequestBigDecimalParameter(CAMPO_NUM_GIORNI_PRESTAZIONE));
			mBenMod.setNumMesiPrestazione(getRequestBigDecimalParameter(CAMPO_NUM_MESI_PRESTAZIONE));
			mBenMod.setNumOreSettimanali(getRequestBigDecimalParameter(CAMPO_NUM_ORE_SETTIMANALI));
			mBenMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));

			if (!isRequestParameterNullObj(CAMPO_FLAG_FREQUENZA_SETTIMANALE)
					&& getRequestStringParameter(CAMPO_FLAG_FREQUENZA_SETTIMANALE) != null
					&& (getRequestStringParameter(CAMPO_FLAG_FREQUENZA_SETTIMANALE)).equals("1")) {
				mBenMod.setFlagFrequenzaSettimanale("N"); // Non determinata
			} else if (!isRequestParameterNullObj(CAMPO_FLAG_FREQUENZA_SETTIMANALE)
					&& getRequestStringParameter(CAMPO_FLAG_FREQUENZA_SETTIMANALE) != null
					&& (getRequestStringParameter(CAMPO_FLAG_FREQUENZA_SETTIMANALE)).equals("2")) {

				mBenMod.setFlagFrequenzaSettimanale("D"); // Determinata
			}

			if (mBenMod.getFlagFrequenzaSettimanale().equals("D")) {
				// TIPOLOGIA ORARIO
				mTipologie = new ArrayList<>();
				// Lunedì
				if (!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_LUN)
						&& getRequestStringParameter(CAMPO_COD_NUM_GIORNO_LUN) != null) {
					TipologiaOrarioModel lTipOrLunMod = new TipologiaOrarioModel();
					lTipOrLunMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_LUN));
					lTipOrLunMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_LUN));
					lTipOrLunMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_LUN));
					lTipOrLunMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
					lTipOrLunMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lTipOrLunMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lTipOrLunMod.setDataInserimento(DateUtils.getSysDate());

					mTipologie.add(lTipOrLunMod);
				}

				// Martedì
				if (!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_MAR)
						&& getRequestStringParameter(CAMPO_COD_NUM_GIORNO_MAR) != null) {
					TipologiaOrarioModel lTipOrMarMod = new TipologiaOrarioModel();
					lTipOrMarMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_MAR));
					lTipOrMarMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_MAR));
					lTipOrMarMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_MAR));
					lTipOrMarMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
					lTipOrMarMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lTipOrMarMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lTipOrMarMod.setDataInserimento(DateUtils.getSysDate());

					mTipologie.add(lTipOrMarMod);
				}

				// Mercoledì
				if (!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_MER)
						&& getRequestStringParameter(CAMPO_COD_NUM_GIORNO_MER) != null) {
					TipologiaOrarioModel lTipOrMerMod = new TipologiaOrarioModel();
					lTipOrMerMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_MER));
					lTipOrMerMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_MER));
					lTipOrMerMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_MER));
					lTipOrMerMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
					lTipOrMerMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lTipOrMerMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lTipOrMerMod.setDataInserimento(DateUtils.getSysDate());

					mTipologie.add(lTipOrMerMod);
				}

				// Giovedì
				if (!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_GIOV)
						&& getRequestStringParameter(CAMPO_COD_NUM_GIORNO_GIOV) != null) {
					TipologiaOrarioModel lTipOrGiovMod = new TipologiaOrarioModel();
					lTipOrGiovMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_GIOV));
					lTipOrGiovMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_GIOV));
					lTipOrGiovMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_GIOV));
					lTipOrGiovMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
					lTipOrGiovMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lTipOrGiovMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lTipOrGiovMod.setDataInserimento(DateUtils.getSysDate());

					mTipologie.add(lTipOrGiovMod);
				}

				// Venerdì
				if (!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_VEN)
						&& getRequestStringParameter(CAMPO_COD_NUM_GIORNO_VEN) != null) {
					TipologiaOrarioModel lTipOrVenMod = new TipologiaOrarioModel();
					lTipOrVenMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_VEN));
					lTipOrVenMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_VEN));
					lTipOrVenMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_VEN));
					lTipOrVenMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
					lTipOrVenMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lTipOrVenMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lTipOrVenMod.setDataInserimento(DateUtils.getSysDate());

					mTipologie.add(lTipOrVenMod);
				}

				// Sabato
				if (!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_SAB)
						&& getRequestStringParameter(CAMPO_COD_NUM_GIORNO_SAB) != null) {
					TipologiaOrarioModel lTipOrSabMod = new TipologiaOrarioModel();
					lTipOrSabMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_SAB));
					lTipOrSabMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_SAB));
					lTipOrSabMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_SAB));
					lTipOrSabMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
					lTipOrSabMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lTipOrSabMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lTipOrSabMod.setDataInserimento(DateUtils.getSysDate());

					mTipologie.add(lTipOrSabMod);
				}

				// Domenica
				if (!this.isRequestParameterNullObj(CAMPO_COD_NUM_GIORNO_DOM)
						&& getRequestStringParameter(CAMPO_COD_NUM_GIORNO_DOM) != null) {
					TipologiaOrarioModel lTipOrDomMod = new TipologiaOrarioModel();
					lTipOrDomMod.setCodNumGiorno(getRequestStringParameter(CAMPO_COD_NUM_GIORNO_DOM));
					lTipOrDomMod.setDalleOre(getRequestStringParameter(CAMPO_DALLE_ORE_DOM));
					lTipOrDomMod.setAlleOre(getRequestStringParameter(CAMPO_ALLE_ORE_DOM));
					lTipOrDomMod.setEnteIncaricato(getRequestStringParameter(CAMPO_ENTE_INCARICATO));
					lTipOrDomMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
					lTipOrDomMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
					lTipOrDomMod.setDataInserimento(DateUtils.getSysDate());

					mTipologie.add(lTipOrDomMod);
				}
			}
		} else {
			if (aModalita.compareTo("M") == 0) {
				mBenMod.setNumGiorniPrestazione(null);
				mBenMod.setNumMesiPrestazione(null);
				mBenMod.setNumOreSettimanali(null);
				mBenMod.setFlagFrequenzaSettimanale(null);
			}
		}

	} // Chiude LetturaBeneficio()

	protected void letturaBeneficioIndulto(String aModalita) throws F3BException {
		// BENEFICI INDULTO - AMNISTIA
		mBenMod.setCodNaturaBeneficio("C");
		mBenMod.setCodTipoBeneficio(getRequestStringParameter(CAMPO_COD_TIPO_BENEFICIO));
		mBenMod.setCodSottotipoBeneficio(getRequestStringParameter(CAMPO_COD_SOTTOTIPO_BENEFICIO));

		mBenMod.setCodDpr(getRequestStringParameter(CAMPO_COD_DPR));
		mBenMod.setNote(getRequestStringParameter(CAMPO_NOTE));

		mBenMod.setCodTipoSospSubordinata("-");
		// ===========================================
		// reclusione e multa
		// ===========================================
		String GRec = getRequestStringParameter("GRec");
		String MRec = getRequestStringParameter("MRec");
		String ARec = getRequestStringParameter("ARec");
		String Multa = getRequestStringParameter("Multa");
		String Multa_dec = getRequestStringParameter("Mul_dec");

		if (aModalita.compareTo("I") == 0) {
			if (!ARec.equals(""))
				mBenMod.setNumAnniReclusione(new BigDecimal(ARec));
			if (!MRec.equals(""))
				mBenMod.setNumMesiReclusione(new BigDecimal(MRec));
			if (!GRec.equals(""))
				mBenMod.setNumGiorniReclusione(new BigDecimal(GRec));
			if (!Multa.equals("")) {
				if (!Multa_dec.equals(""))
					mBenMod.setImportoMulta(new BigDecimal(Multa + "." + Multa_dec));
				else
					mBenMod.setImportoMulta(new BigDecimal(Multa));
			} else if (!Multa_dec.equals(""))
				mBenMod.setImportoMulta(new BigDecimal("0." + Multa_dec));
		} else if (aModalita.compareTo("M") == 0) {
			if (!ARec.equals(""))
				mBenMod.setNumAnniReclusione(new BigDecimal(ARec));
			else
				mBenMod.setNumAnniReclusione(null);

			if (!MRec.equals(""))
				mBenMod.setNumMesiReclusione(new BigDecimal(MRec));
			else
				mBenMod.setNumMesiReclusione(null);

			if (!GRec.equals(""))
				mBenMod.setNumGiorniReclusione(new BigDecimal(GRec));
			else
				mBenMod.setNumGiorniReclusione(null);

			if (!Multa.equals("")) {
				if (!Multa_dec.equals(""))
					mBenMod.setImportoMulta(new BigDecimal(Multa + "." + Multa_dec));
				else
					mBenMod.setImportoMulta(new BigDecimal(Multa));
			} else if (!Multa_dec.equals(""))
				mBenMod.setImportoMulta(new BigDecimal("0." + Multa_dec));
			else
				mBenMod.setImportoMulta(null);
		}

		// ===========================================
		// arresto e ammenda
		// ===========================================
		String GArr = getRequestStringParameter("GArr");
		String MArr = getRequestStringParameter("MArr");
		String AArr = getRequestStringParameter("AArr");
		String Ammenda = getRequestStringParameter("Ammenda");
		String Ammenda_dec = getRequestStringParameter("Amm_dec");

		if (aModalita.compareTo("I") == 0) {
			if (!AArr.equals(""))
				mBenMod.setNumAnniArresto(new BigDecimal(AArr));
			if (!MArr.equals(""))
				mBenMod.setNumMesiArresto(new BigDecimal(MArr));
			if (!GArr.equals(""))
				mBenMod.setNumGiorniArresto(new BigDecimal(GArr));

			if (!Ammenda.equals("")) {
				if (!Ammenda_dec.equals(""))
					mBenMod.setImportoAmmenda(new BigDecimal(Ammenda + "." + Ammenda_dec));
				else
					mBenMod.setImportoAmmenda(new BigDecimal(Ammenda));
			} else if (!Ammenda_dec.equals(""))
				mBenMod.setImportoAmmenda(new BigDecimal("0." + Ammenda_dec));
		} else if (aModalita.compareTo("M") == 0) {
			if (!AArr.equals(""))
				mBenMod.setNumAnniArresto(new BigDecimal(AArr));
			else
				mBenMod.setNumAnniArresto(null);

			if (!MArr.equals(""))
				mBenMod.setNumMesiArresto(new BigDecimal(MArr));
			else
				mBenMod.setNumMesiArresto(null);

			if (!GArr.equals(""))
				mBenMod.setNumGiorniArresto(new BigDecimal(GArr));
			else
				mBenMod.setNumGiorniArresto(null);

			if (!Ammenda.equals("")) {
				if (!Ammenda_dec.equals(""))
					mBenMod.setImportoAmmenda(new BigDecimal(Ammenda + "." + Ammenda_dec));
				else
					mBenMod.setImportoAmmenda(new BigDecimal(Ammenda));
			} else if (!Ammenda_dec.equals(""))
				mBenMod.setImportoAmmenda(new BigDecimal("0." + Ammenda_dec));
			else
				mBenMod.setImportoAmmenda(null);
		}

		if (aModalita.compareTo("I") == 0) {
			mBenMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			mBenMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			mBenMod.setDataInserimento(DateUtils.getSysDate());
			mBenMod.setFlagStato("I");
		} else if (aModalita.compareTo("M") == 0) {
			mBenMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
			mBenMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
			mBenMod.setDataAggiornamento(DateUtils.getSysDate());

			mBenMod.setIdBeneficioCumulo(getRequestBigDecimalParameter(CAMPO_ID_BENEFICIO_CUMULO));
			if (getRequestStringParameter(CAMPO_FLAG_STATO).compareTo("I") != 0)
				mBenMod.setFlagStato("M");
		}

		mBenMod.setRifCodTipoAutoEmittente("-");
		mBenMod.setRifCodTipoProvvedimento("-");

		mBenMod.setTitIdTitoloCumulato(
				getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO));
		mBenMod.setMotivoModifica(getRequestStringParameter(ICostantiBeneficiCumulo.CAMPO_MOTIVO_MODIFICA));

		if (!"07".equals(mBenMod.getCodSottotipoBeneficio()))
			if (!isRequestParameterNullObj(ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO))
				mListaIdPeneAcc = getRequestStringParameters(
						ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO);

	} // Chiude letturaBeneficioIndulto()

} // chiude Classe ActInserisciBeneficiCumulo()