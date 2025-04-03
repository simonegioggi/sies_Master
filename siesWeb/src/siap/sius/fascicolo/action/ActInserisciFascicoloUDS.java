package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.controller.IFascicoloSiusUDS;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * ActInserisciFascicoloUDS - Classe Azione di inserimento del Fascicolo SIUS
 *
 * @version 1.0
 */
public class ActInserisciFascicoloUDS extends ActionSiap implements ICostantiFascicoloSius {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Si Recupera l'utente e il Fascicolo SIEP dalla sessione.
		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		FascicoloSiepModel lFasSiepMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// 22/05/2009 Controllo Ufficio mittente.
		// Le EMA accettano mittenti di TDS o UDS; oppure PM o PGCAP in caso di arresti domiciliari.
		String aDescrUfficioMittente = DecodificheUtils.getDescbyCode(
				DecodificheManager.getInstance().getMittenteAtto(),
				getRequestStringParameter(CAMPO_COD_MITTENTE_ATTO));

		// TODO carmela commentato Controllo Ufficio mittente (verificare)
		// Modifica del 10/09/2013 mev "Revisione Misure di Sicurezza SIUS"
		// Eliminato controllo su ufficio mittente se il campo non è valorizzato
		// nel caso di "Iscrizione di una Esecuzione Misura di Sicurezza"
		String lCodTipoUfficioMittente = DecodificheUtils
				.getCodebyDesc(DecodificheManager.getInstance().getTipoUfficio(), aDescrUfficioMittente);
		String[] lArrayCodici = (getRequestStringParameters(CAMPO_COD_OGGETTO));
		String lCodiciOggetto = "";

		for (int i = 0; i < lArrayCodici.length; i++)
			lCodiciOggetto += lArrayCodici[i];

		// MEV10-s3: aggiunte or condition per gestire uffici minorenni
		if (((getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U019") == 0
				&& lCodTipoUfficioMittente.compareTo("UDS") != 0
				&& lCodTipoUfficioMittente.compareTo("UDSM") != 0
				&& lCodTipoUfficioMittente.compareTo("TDS") != 0
				&& lCodTipoUfficioMittente.compareTo("TDSM") != 0)
				|| (getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U004") == 0
						&& lCodTipoUfficioMittente.compareTo("TDS") != 0
						&& lCodTipoUfficioMittente.compareTo("TDSM") != 0
						&& lCodTipoUfficioMittente.compareTo("UDS") != 0
						&& lCodTipoUfficioMittente.compareTo("UDSM") != 0
						&& lCodTipoUfficioMittente.compareTo("-") != 0))
				&& (!(getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U004") == 0
						&& (lCodTipoUfficioMittente.compareTo("PM") == 0
								|| lCodTipoUfficioMittente.compareTo("PGCAP") == 0)
						&& lCodiciOggetto.indexOf("2368") >= 0)))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Ufficio Mittente non valido per il contenuto selezionato");

		// STUB 19/07/2007 Gestione dei procedimenti di EMA e ESS.
		// Recupero del Procedimento di Esecuzione della misura alternativa, nel caso fosse impostato nella
		// form di inserimento.
		// N.B. Se non esiste, il controller solleva un errore di eccezione sull'esistenza del procedimento.
		// 20180105: [SG] tolto il commento su questo controllo per segnalazione 01/01/2018 errore SIUS LECCE
		/*
		 * if ((getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22) != null) &&
		 * (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO) != null) &&
		 * (((getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S22") == 0) &&
		 * (getRequestStringParameter( CAMPO_COD_CONTENUTO).compareTo("U004") != 0)) ||
		 * ((getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S12") == 0) &&
		 * (getRequestStringParameter( CAMPO_COD_CONTENUTO).compareTo("U019") != 0)) ||
		 * ((getRequestStringParameter( CAMPO_COD_TIPO_REGISTRO).compareTo("S09") == 0) &&
		 * (getRequestStringParameter( CAMPO_COD_CONTENUTO).compareTo("U024") != 0)))) { String lCodContenuto
		 * = ((getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S22") == 0) ? "U004" : "U019");
		 * if (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S09") == 0) lCodContenuto =
		 * "U024"; IFascicoloSiusUDS lCtrl = SIUSLookupRemote.getFascicoloSiusUDSRemote();
		 * lCtrl.ExistProcedimentoEsecuzione(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22),
		 * getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22), lCodContenuto,
		 * getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO), getCodUfficioUtenteConnesso(),
		 * lFasSiepMod.getSoggetto().getIdSoggetto()); }
		 */

		// Ho selezionato un contenuto legato a un fascicolo di esecuzione (CAMPO_CHIAVE_ANNO_S22!=null &&
		// CAMPO_COD_TIPO_REGISTRO!=null)
		// Se il contenuto NON è quello del fascicolo di esecuzione ma di uno dei collegati, verifico che gli
		// estremi del fascicolo di esecuzione siano corretti (ricerco il fascicolo)
		if (getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22) != null
				&& getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO) != null
				&& ((getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S22") == 0
						&& getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U004") != 0)
						|| (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S12") == 0
								&& getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U019") != 0)
						|| (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S09") == 0
								&& getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U024") != 0)
						// MEV_2023-35 - si aggiunge il provvedimento di esecuzione pene sospese
						|| (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S30") == 0
								&& getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U126") != 0))) {
			String lCodContenutoFE = "";

			if (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S22") == 0)
				lCodContenutoFE = "U004";
			else if (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S12") == 0)
				lCodContenutoFE = "U019";
			else if (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S09") == 0)
				lCodContenutoFE = "U024";
			// MEV_2023-35: aggiunto controllo
			else if (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S30") == 0)
				lCodContenutoFE = "U126";

			IFascicoloSiusUDS lCtrl = SIUSLookupRemote.getFascicoloSiusUDSRemote();
			lCtrl.ExistProcedimentoEsecuzione(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22),
					getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22), lCodContenutoFE,
					getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO), getCodUfficioUtenteConnesso(),
					lFasSiepMod.getSoggetto().getIdSoggetto());
		}

		// Istanzio il Model che incapsula il FascicoloSIUS, il GeneraleProcedimento e il Tenore
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();

		// Caricamento Fascicolo SIUS
		// Anno corrente
		lFasGPMod.getFascicoloSiusModel().setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy")));
		// Ufficio dell'operatore che inserisce
		lFasGPMod.getFascicoloSiusModel().setChiaveUfficio(lUtenteMod.getUfficioUtente().getCodUfficio());
		// Il progressivo del fascicolo (in base all'anno all'ufficio) viene calcolato applicativamente nel
		// controller
		lFasGPMod.getFascicoloSiusModel().setDataInserimento(DateUtils.getSysDate());
		lFasGPMod.getFascicoloSiusModel().setDataIscrizione(DateUtils.getSysDate());
		// Stato Fascicolo SIUS settato ad aperto
		lFasGPMod.getFascicoloSiusModel().setCodStatoFascicolo("02");

		lFasGPMod.getFascicoloSiusModel().setCodOperatoreInserimento(lUtenteMod.getUserId());
		// Codice dell'ufficio inserimento
		lFasGPMod.getFascicoloSiusModel()
				.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

		lFasGPMod.getFascicoloSiusModel().setSogIdSoggetto(lFasSiepMod.getSoggetto().getIdSoggetto());
		lFasGPMod.getFascicoloSiusModel().setFasSieIdFascicoloSiep(lFasSiepMod.getIdFascicoloSiep());
		lFasGPMod.getFascicoloSiusModel().setChiaveAnnoSIEP(lFasSiepMod.getChiaveAnno());
		lFasGPMod.getFascicoloSiusModel().setChiaveProgrSIEP(lFasSiepMod.getChiaveProgr());
		lFasGPMod.getFascicoloSiusModel().setChiaveUfficioSIEP(lFasSiepMod.getChiaveUfficio());
		// Soggetto recuperato dal Fascicolo SIEP in sessione
		lFasGPMod.getFascicoloSiusModel().setSoggetto(lFasSiepMod.getSoggetto());

		ComuneModel lComMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_SEDE_MITTENTE)));

		// Caricamento Generale Procedimento
		lFasGPMod.getGeneraleProcedimentoModel().setCodSedeMittente(new String(lComMod.getCodComune()));
		lFasGPMod.getGeneraleProcedimentoModel()
				.setCodTipoRegistro(getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO));

		// Il progressivo S1 viene calcolato applicativamente nel controller, oppure viene impostato in base
		// al CAMPO_CHIAVE_PROGR_S22
		// STUB 11/02/2004 Il Campo ProgrS1 (da recenti disposizioni) viene SEMPRE impostato = al
		// CAMPO_CHIAVE_PROGR_S22.
		if (getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22) != null) {
			lFasGPMod.getGeneraleProcedimentoModel()
					.setAnnoS1(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22));
			lFasGPMod.getGeneraleProcedimentoModel()
					.setProgrS1(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22));
		} else
			lFasGPMod.getGeneraleProcedimentoModel().setAnnoS1(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lFasGPMod.getGeneraleProcedimentoModel()
				.setCodOggettoProcedimento(getRequestStringParameter(CAMPO_COD_CONTENUTO));
		lFasGPMod.getGeneraleProcedimentoModel().setDataRichiesta(
				getRequestDateParameter(CAMPO_ANNO_DATA_ATTO, CAMPO_MESE_DATA_ATTO, CAMPO_GIORNO_DATA_ATTO));
		lFasGPMod.getGeneraleProcedimentoModel().setDataArrivoCancelleria(getRequestDateParameter(
				CAMPO_ANNO_DATA_ARRIVO, CAMPO_MESE_DATA_ARRIVO, CAMPO_GIORNO_DATA_ARRIVO));
		lFasGPMod.getGeneraleProcedimentoModel()
				.setCodTipoAtto(getRequestStringParameter(CAMPO_COD_TIPO_ATTO));
		lFasGPMod.getGeneraleProcedimentoModel()
				.setCodTipoMittenteAtto(getRequestStringParameter(CAMPO_COD_MITTENTE_ATTO));
		if ((getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE)).equalsIgnoreCase(""))
			lFasGPMod.getGeneraleProcedimentoModel()
					.setCodSedeMittente(super.getCodComuneByDescr("-").getCodComune());
		else
			lFasGPMod.getGeneraleProcedimentoModel().setCodSedeMittente(
					super.getCodComuneByDescr(getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE))
							.getCodComune());
		lFasGPMod.getGeneraleProcedimentoModel().setCodSedeMittente(
				super.getCodComuneByDescr(getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE))
						.getCodComune());
		lFasGPMod.getGeneraleProcedimentoModel().setAnnotazione(getRequestStringParameter(CAMPO_NOTE));
		// Codice dell'ufficio dell'operatore che inserisce
		lFasGPMod.getGeneraleProcedimentoModel()
				.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());
		// Codice dell'operatore che inserisce
		lFasGPMod.getGeneraleProcedimentoModel().setCodOperatoreInserimento(lUtenteMod.getUserId());
		lFasGPMod.getGeneraleProcedimentoModel().setDataInserimento(DateUtils.getSysDate());
		lFasGPMod.getGeneraleProcedimentoModel().setDataFinePena(
				getRequestDateParameter(CAMPO_ANNO_FINE_PENA, CAMPO_MESE_FINE_PENA, CAMPO_GIORNO_FINE_PENA));
		lFasGPMod.getGeneraleProcedimentoModel()
				.setCodPosGiuridica(getRequestStringParameter(CAMPO_COD_POS_GIURIDICA));
		// 18/12/2003 Impostazione del Cod_Magistrato in CodAutoritaDelegata.
		lFasGPMod.getGeneraleProcedimentoModel()
				.setCodAutoritaDelegata(getRequestStringParameter(CAMPO_COD_MAGISTRATO));
		// 14/01/2004 Impostazione della Descrizione del Mittente.
		lFasGPMod.getGeneraleProcedimentoModel()
				.setDescrMittente(getRequestStringParameter(CAMPO_DESCR_MITTENTE));

		// 30/09/2004 Impostazione dell'Ufficio Mittente (solo in caso di EMA U004), Utilizzando in appoggio
		// il campo DescrDefinizione.
		// 24/07/2007 Impostazione dell'Ufficio Mittente *anche in caso di ESS U019), Utilizzando in appoggio
		// il campo DescrDefinizione.
		// Modifica del 23/10/2013 mev "Revisione Misure di Sicurezza SIUS"
		// Eliminato controllo sull' ufficio mittente (Ufficio Inesistente)
		// nel caso di "Iscrizione di una Esecuzione Misura di Sicurezza"
		String aCodUfficioMittente = "";
		if (getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U004") == 0
				|| getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U019") == 0
				// MEV_2023-35 - Si gestisce anche il cod delle Pene Sospese U126
				|| getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U126") == 0) {
			if (getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE) != "") {
				aCodUfficioMittente = getCodUfficioByCodTipoUfficioDescrComune(
						DecodificheUtils.getCodebyDesc(DecodificheManager.getInstance().getTipoUfficio(),
								aDescrUfficioMittente),
						getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE));
			}
			lFasGPMod.getGeneraleProcedimentoModel().setCodUfficioMittente(aCodUfficioMittente);
		} else {
			lFasGPMod.getGeneraleProcedimentoModel().setDescrDefinizione("");
		}

		// Il campo FasSiuIdFascicoloSius di Generale Procedimento viene impostato nel controller
		// Caricamento Tenore
		// Preleva dalla request i codici e descrizioni dei tenori, impipati rispettivamente con separatore
		// "|" e "\n".
		// Stabilisce la size dell'Array di Tenori da caricare in FascicoloGpModel.
		StringTokenizer lCodOggetto = new StringTokenizer(getRequestStringParameter(CAMPO_COD_OGGETTO), "|");
		StringTokenizer lDescrOggetto = new StringTokenizer(getRequestStringParameter(CAMPO_DESCR_OGGETTO),
				"\n");

		// Valorizzazione dei Codici Dettaglio Oggetti.
		String lStCodiceDet = new String(getRequestStringParameter(CAMPO_COD_DETTAGLIO_OGGETTO));

		int lSizeVector = lCodOggetto.countTokens();
		TenoreModel lTenori[] = new TenoreModel[lSizeVector];
		int lIndex = 0;

		while (lCodOggetto.hasMoreTokens()) {
			TenoreModel lTenModel = new TenoreModel();

			lTenModel.setCodOggettoTenore(lCodOggetto.nextToken());
			lTenModel.setDescrOggettoTenore(lDescrOggetto.nextToken());
			// Codice dell'ufficio dell'operatore che inserisce
			lTenModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			// Codice dell'operatore che inserisce
			lTenModel.setCodOperatoreInserimento(getCodUtenteConnesso());
			lTenModel.setDataInserimento(DateUtils.getSysDate());
			lTenModel.setCodMagistrato(getRequestStringParameter(CAMPO_COD_MAGISTRATO));
			lTenModel.setProgrTenore(new BigDecimal((double) (lIndex + 1)));
			lTenModel.setCodEsitoTenore("-");
			// Il campo Id_Generale_Procedimento di Tenore viene impostato nel controller

			// Valorizzazione dell'eventuale Dettaglio Oggetto.
			if ((lStCodiceDet).indexOf(lTenModel.getCodOggettoTenore() + "0") < 0) {
				lTenModel.setCodDettaglioOggetto("-");
			} else {
				String lCodDettaglioCorrente = lStCodiceDet.substring(
						lStCodiceDet.indexOf(lTenModel.getCodOggettoTenore() + "0") + 4,
						lStCodiceDet.indexOf(lTenModel.getCodOggettoTenore() + "0") + 8);
				lTenModel.setCodDettaglioOggetto(lCodDettaglioCorrente);
			}

			// MEV_39: modifica al codice per oggetto tenore 2422 --> 24221204|
			if ("2422".equals(lTenModel.getCodOggettoTenore())) {
				if (!lStCodiceDet.startsWith(lTenModel.getCodOggettoTenore()))
					lTenModel.setCodDettaglioOggetto("-");
				else
					lTenModel.setCodDettaglioOggetto(lStCodiceDet.substring(4, 8));
			}

			// Setto l'Array su GPtenoreModel
			lTenori[lIndex] = lTenModel;
			lIndex++;
		}
		lFasGPMod.setTenori(lTenori);

		// STUB 09/01/2004 Gestione del Luogo detenzione.(Rivista il 21/10/2010)
		if (!isRequestParameterNullObj(ID_LUOGO_DETENZIONE)
				&& getRequestStringParameter(ID_LUOGO_DETENZIONE).length() > 0
				&& getRequestStringParameter(LUOGO_DETENZIONE).length() > 0
				&& isRequestChecked(CAMPO_VALIDA_LUOGO_DET)) {
			/* Duplicazione del Luogo Detenzione per staccare da SIEP */
			LuogoDetenzioneModel luogoDetenzioneOrigine = new LuogoDetenzioneModel();
			LuogoDetenzioneModel luogoDetenzioneSIUS = new LuogoDetenzioneModel();
			ILuogoDetenzione lCtrlLuoDet = SIEPLookupRemote.getLuogoDetenzioneRemote();

			luogoDetenzioneOrigine = lCtrlLuoDet.ExRicercaLuogoDetenzioneByKey(
					new BigDecimal(getRequestStringParameter(ID_LUOGO_DETENZIONE)));
			luogoDetenzioneOrigine
					.setFasSieIdFascicoloSiep(null); /*
														 * Altrimenti SIEP lo vede tra le posiz. giuridiche
														 */
			luogoDetenzioneOrigine.setDataFineDetenzione(null);
			luogoDetenzioneOrigine.setPosGiuIdPosizioneGiuridica(null);
			luogoDetenzioneSIUS = lCtrlLuoDet.ExInserisciLuogoDetenzione(luogoDetenzioneOrigine);
			// lFasGPMod.getGeneraleProcedimentoModel().setIdLuogoDetenzione(getRequestStringParameter(
			// ID_LUOGO_DETENZIONE ));
			lFasGPMod.getGeneraleProcedimentoModel()
					.setIdLuogoDetenzione(luogoDetenzioneSIUS.getIdLuogoDetenzione().toString());
			lFasGPMod.getGeneraleProcedimentoModel()
					.setIdAltraCausa(getRequestStringParameter(ID_ALTRA_CAUSA));
		}

		// STUB 02/12/2004 Recupero FascicoloSiusOrigine. //01/03/2005 Controllo nullValue
		if (!isRequestParameterNullObj(ID_FASCICOLO_SIUS_ORIGINE))
			lFasGPMod.getFascicoloSiusModel()
					.setIdFascicoloSiusOrigine(getRequestBigDecimalParameter(ID_FASCICOLO_SIUS_ORIGINE));

		// FascicoloSiusController lCtrl = new FascicoloSiusController();
		IFascicoloSiusUDS lCtrlFS = SIUSLookupRemote.getFascicoloSiusUDSRemote();
		// STUB 08/05/2008 lFasGPMod = lCtrlFS.ExInserisciFascicoloSiusUDS(lFasGPMod);

		// Aggiunta eredità quantum pena per fascicoli ESS padre iscritti come collegati a
		// fascicoli Conversione PP o a fascicoli Applicazione SS
		int durataEsitoAnni = 0;
		int durataEsitoMesi = 0;
		int durataEsitoGiorni = 0;
		if ((!isRequestParameterNullObj(ID_FASCICOLO_SIUS_ORIGINE))
				&& (getRequestBigDecimalParameter(ID_FASCICOLO_SIUS_ORIGINE) != null)) {
			BigDecimal IdFascicoloSiusOrigine = getRequestBigDecimalParameter(ID_FASCICOLO_SIUS_ORIGINE);
			FascicoloGPModel lFascOrigine = lCtrlFS.ExRicercaFascicoloByKey(IdFascicoloSiusOrigine);

			// nel caso di CPP
			if ((getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U019") == 0)
					&& (lFascOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.compareTo("U070") == 0)
					&& (lFascOrigine.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("07") == 0)) {
				IRichiestaConversione lCtrlConv = SIEPLookupRemote.getRichiestaConversioneRemote();
				RichiestaConversioneModel aRichiestaConversione = new RichiestaConversioneModel();
				// aRichiestaConversione.setFasSiuIdFascicoloSius(getRequestBigDecimalParameter(
				// "ID_FASCICOLO_SIUS_ORIGINE" ));
				aRichiestaConversione.setFasSiuIdFascicoloSius(IdFascicoloSiusOrigine);
				Vector lRichiesteCPP = lCtrlConv
						.ExRicercaRichiesteConversionePenePecuniarieByIdFascicoloSius(IdFascicoloSiusOrigine);
				if (lRichiesteCPP.size() > 0) {
					Iterator itxCPP = lRichiesteCPP.iterator();
					while (itxCPP.hasNext()) {
						RichiestaConversioneModel richConversione = (RichiestaConversioneModel) itxCPP.next();
						if (richConversione != null && richConversione.getDurataEsitoAnni() != null)
							durataEsitoAnni += richConversione.getDurataEsitoAnni().intValue();
						if (richConversione != null && richConversione.getDurataEsitoMesi() != null)
							durataEsitoMesi += richConversione.getDurataEsitoMesi().intValue();
						if (richConversione != null && richConversione.getDurataEsitoGiorni() != null)
							durataEsitoGiorni += richConversione.getDurataEsitoGiorni().intValue();
					}
				}
			}

			// nel caso ASS
			if ((getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U019") == 0)
					&& (lFascOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.compareTo("U017") == 0)
					&& (lFascOrigine.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("07") == 0)) {

				// ATT: va presa l'ordinanza, non la ESS
				IDepositoOrdinanzaPc lCtrlDOP = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
				DepositoOrdinanzaPcModel aDepositoOrdinanzaPc = new DepositoOrdinanzaPcModel();
				aDepositoOrdinanzaPc = lCtrlDOP.ExRicercaDepositoOrdinanzaPcByGenProcTipoOrd(
						lFascOrigine.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(), "SS");

				if (aDepositoOrdinanzaPc != null) {
					if (aDepositoOrdinanzaPc.getNumAnniDetenzioneDom() != null)
						durataEsitoAnni = aDepositoOrdinanzaPc.getNumAnniDetenzioneDom().intValue();
					if (aDepositoOrdinanzaPc.getNumMesiDetenzioneDom() != null)
						durataEsitoMesi = aDepositoOrdinanzaPc.getNumMesiDetenzioneDom().intValue();
					if (aDepositoOrdinanzaPc.getNumGiorniDetenzioneDom() != null)
						durataEsitoGiorni = aDepositoOrdinanzaPc.getNumGiorniDetenzioneDom().intValue();
				}
			}

			// MEV_2023-35 nel caso ESS
			// Se sto iscrivendo il fascicolo di Esecuzione (U126) e il collegato è il fascicolo di
			// applicazione (U125)
			// in stato Emesso provvedimento (07), recupero le durate delle Pene sospese del Deposito Decreto
			// del fascicolo di applicazione
			if (getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U126") == 0
					&& lFascOrigine.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
							.compareTo("U125") == 0
					&& lFascOrigine.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("07") == 0) {
				// ATT: va presa l'ordinanza, non la EPS
				IDepositoOrdinanzaPc lCtrlDOP = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
				DepositoOrdinanzaPcModel aDepositoOrdinanzaPc = new DepositoOrdinanzaPcModel();
				// n.b. SP per le Pene Sost
				aDepositoOrdinanzaPc = lCtrlDOP.ExRicercaDepositoOrdinanzaPcByGenProcTipoOrd(
						lFascOrigine.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(), "SP");
				if (aDepositoOrdinanzaPc != null) {
					if (aDepositoOrdinanzaPc.getNumAnniDetenzioneDom() != null)
						durataEsitoAnni = aDepositoOrdinanzaPc.getNumAnniDetenzioneDom().intValue();
					if (aDepositoOrdinanzaPc.getNumMesiDetenzioneDom() != null)
						durataEsitoMesi = aDepositoOrdinanzaPc.getNumMesiDetenzioneDom().intValue();
					if (aDepositoOrdinanzaPc.getNumGiorniDetenzioneDom() != null)
						durataEsitoGiorni = aDepositoOrdinanzaPc.getNumGiorniDetenzioneDom().intValue();
				}
			}
			// MEV_2023-35 - FINE
		}
		// Fine aggiunta eredità quantum pena

		lFasGPMod = lCtrlFS.ExInserisciFascicoloSiusUDS(lFasGPMod, null, durataEsitoAnni, durataEsitoMesi,
				durataEsitoGiorni);
		// lFasGPMod = lCtrlFS.ExInserisciFascicoloSiusUDS(lFasGPMod, null);

		// I fascicoli di Applicazione Misura Sicurezza iscritti a partire da un siep ereditano le eventuali
		// misure già presenti in siep
		if (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U023") == 0
				|| lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
						.compareTo("U086") == 0) {
			if (lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
				IMisuraSicurezza lCtrlMS = SIEPLookupRemote.getMisuraSicurezzaRemote();

				// Paolo Cherubini 06/02/2012
				// relativamente alla segnalazione b2/rr/005 aggiungo il controllo che se non trovo la Misura
				// di Sicurezza
				// non genero l'errore ma proseguo nell'elaborazione
				try {
					// List lMisureSicurezzaSiep =
					// lCtrlMS.ExRicercaMisuraSicurezzaByIdFascicolo(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
					List lMisureSicurezzaSiep = lCtrlMS.ExRicercaMisuraSicurezzaByIdFascicoloOrd(
							lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
					Iterator itxMis = lMisureSicurezzaSiep.iterator();
					while (itxMis.hasNext()) {
						MisuraSicurezzaModel lMisSicuSius = (MisuraSicurezzaModel) itxMis.next();
						lMisSicuSius.setFasSiuIdFascicoloSius(
								lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

						// 16/12/2014 In caso di Iscrizione SIUS, la nuova Misura di Sicurezza perde il legame
						// al fascicolo SIEP.
						lMisSicuSius.setFasSieIdFascicoloSiep(null);
						lCtrlMS.ExInserisciMisuraSicurezza(lMisSicuSius);
					}
				} catch (F3BException e) {
					// misura sicurezza non trovata
				}
			}
		} // Sdoppiate Misure Sicurezza Siep ed aggiunte in sius

		// STUB 23/09/2004 Se il fascicolo Siep appartiene ad un ufficio fuori BDI, si trasferisce il
		// RIFERIMENTO_FASCICOLO_SIUS, e poi si passa al dettaglio.
		/*
		 * STUB 11/12/2007 Annullato il Trasferimento del RIFERIMENTO_FASCICOLO_SIUS. UfficioModel
		 * lBDIDestinataria = this.getUfficioByCodUfficio(lFasSiepMod.getChiaveUfficio()); UfficioModel
		 * lBDIMittente = this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso()); if
		 * (lBDIMittente.getCodDistretto().trim().compareTo( lBDIDestinataria.getCodDistretto().trim())==0 )
		 * return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		 * "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&"
		 * +CAMPO_ID_FASCICOLO_SIUS+"="+lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString();
		 * else { // STUB 23/03/2004 Metto in sessione il fascicolo SIUS per consentire la spedizione del
		 * riferimento Fascicolo Sius. setSessionAttribute("fascicoloSiusGP", lFasGPMod); //Prepara la
		 * "pagina" di destinAction RedirectTo lRedirigi = new RedirectTo(); lRedirigi.setPage(
		 * IWebConstants.PG_MAIN ); lRedirigi.setAction(
		 * "siap.sius.rifasius.action.ActTrasferisciRifFascicoloSius" );
		 *
		 * // STUB 23/09/2004 Se il fascicolo Siep appartiene ad un ufficio fuori BDI, si trasferisce il //
		 * RIFERIMENTO_FASCICOLO_SIUS, e poi si passa al dettaglio. /* STUB 11/12/2007 Annullato il
		 * Trasferimento del RIFERIMENTO_FASCICOLO_SIUS. UfficioModel lBDIDestinataria =
		 * this.getUfficioByCodUfficio(lFasSiepMod.getChiaveUfficio()); UfficioModel lBDIMittente =
		 * this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso()); if
		 * (lBDIMittente.getCodDistretto().trim().compareTo( lBDIDestinataria.getCodDistretto().trim())==0 )
		 * return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		 * "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&"
		 * +CAMPO_ID_FASCICOLO_SIUS+"="+lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString();
		 * else { // STUB 23/03/2004 Metto in sessione il fascicolo SIUS per consentire la spedizione del
		 * riferimento Fascicolo Sius. setSessionAttribute("fascicoloSiusGP", lFasGPMod); //Prepara la
		 * "pagina" di destinAction RedirectTo lRedirigi = new RedirectTo(); lRedirigi.setPage(
		 * IWebConstants.PG_MAIN ); lRedirigi.setAction(
		 * "siap.sius.rifasius.action.ActTrasferisciRifFascicoloSius" );
		 *
		 * return lRedirigi.toString(); }
		 */
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIUS + "="
				+ lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString();
	}

}