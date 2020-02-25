package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.fascicolo.controller.IFascicoloSiusUDS;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInsFascicoloDaSiepUDS
 * </p>
 * <p>
 * Description: Classe Azione di inserimento del Fascicolo SIUS da Atto Pervenuto da Siep
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia (Bull)
 * </p>
 */
public class ActInsFascicoloDaSiepUDS extends ActionSiap implements ICostantiFascicoloSius {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Si Recupera l'utente e il Fascicolo SIEP dalla sessione.
		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		FascicoloSiepModel lFasSiepMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// 22/05/2009 Controllo Ufficio mittente.
		// Le EMA accettano mittenti di TDS o UDS; oppure PM o PGCAP in caso di arresti domiciliari.
		String aDescrUfficioMittente = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
				.getMittenteAtto(), getRequestStringParameter(CAMPO_COD_MITTENTE_ATTO));

		String lCodTipoUfficioMittente = DecodificheUtils.getCodebyDesc(DecodificheManager.getInstance()
				.getTipoUfficio(), aDescrUfficioMittente);
		String[] lArrayCodici = (getRequestStringParameters(ICostantiFascicoloSius.CAMPO_COD_OGGETTO));
		String lCodiciOggetto = "";

		for (int i = 0; i < lArrayCodici.length; i++)
			lCodiciOggetto += lArrayCodici[i];

		// MERGE v10: aggiunti controlli sugli uffici minori
		if (((CAMPO_COD_CONTENUTO.compareTo("U019") == 0 && lCodTipoUfficioMittente.compareTo("UDS") != 0
				&& lCodTipoUfficioMittente.compareTo("UDSM") != 0
				&& lCodTipoUfficioMittente.compareTo("TDS") != 0 && lCodTipoUfficioMittente.compareTo("TDSM") != 0) || (CAMPO_COD_CONTENUTO
				.compareTo("U004") == 0
				&& lCodTipoUfficioMittente.compareTo("TDS") != 0
				&& lCodTipoUfficioMittente.compareTo("TDSM") != 0
				&& lCodTipoUfficioMittente.compareTo("UDS") != 0
				&& lCodTipoUfficioMittente.compareTo("UDSM") != 0 && lCodTipoUfficioMittente.compareTo("-") != 0))
				&& (!(CAMPO_COD_CONTENUTO.compareTo("U004") == 0
						&& (lCodTipoUfficioMittente.compareTo("PM") == 0 || lCodTipoUfficioMittente
								.compareTo("PGCAP") == 0) && lCodiciOggetto.indexOf("2368") >= 0)))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Ufficio Mittente non valido per il contenuto selezionato");

		// Gestione dei procedimenti di EMA e ESS.
		// Recupero del Procedimento di Esecuzione della misura alternativa, nel caso fosse impostato nella
		// form di inserimento.
		// N.B. Se non esiste, il controller solleva un errore di eccezione sull'esistenza del procedimento.
		if ((getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22) != null)
				&& (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO) != null)
				&& (((getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S22") == 0) && (getRequestStringParameter(
						CAMPO_COD_CONTENUTO).compareTo("U004") != 0)) || ((getRequestStringParameter(
						CAMPO_COD_TIPO_REGISTRO).compareTo("S12") == 0) && (getRequestStringParameter(
						CAMPO_COD_CONTENUTO).compareTo("U019") != 0)))) {
			String lCodContenuto = ((getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S22") == 0) ? "U004"
					: "U019");
			IFascicoloSiusUDS lCtrl = SIUSLookupRemote.getFascicoloSiusUDSRemote();
			/* boolean esiste = */lCtrl.ExistProcedimentoEsecuzione(
					getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22),
					getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22), lCodContenuto,
					getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO), getCodUfficioUtenteConnesso(),
					lFasSiepMod.getSoggetto().getIdSoggetto());
		}

		// Istanzio il Model che incapsula il FascicoloSIUS e il GeneraleProcedimento
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();

		// Caricamento Fascicolo SIUS
		lFasGPMod.getFascicoloSiusModel().setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy"))); // Anno
																										// corrente
		lFasGPMod.getFascicoloSiusModel().setChiaveUfficio(lUtenteMod.getUfficioUtente().getCodUfficio()); // Ufficio
																											// dell'operatore
																											// che
																											// inserisce
		// Il progressivo del fascicolo (in base all'anno e all'ufficio) viene calcolato applicativamente nel
		// controller
		lFasGPMod.getFascicoloSiusModel().setDataInserimento(DateUtils.getSysDate());
		lFasGPMod.getFascicoloSiusModel().setDataIscrizione(DateUtils.getSysDate());
		lFasGPMod.getFascicoloSiusModel().setCodStatoFascicolo("02"); // Stato Fascicolo SIUS settato ad
																		// aperto
		lFasGPMod.getFascicoloSiusModel().setCodOperatoreInserimento(lUtenteMod.getUserId()); // Codice
																								// dell'operatore
																								// che
																								// inserisce
		lFasGPMod.getFascicoloSiusModel().setCodUfficioInserimento(
				lUtenteMod.getUfficioUtente().getCodUfficio()); // Codice dell'operatore che inserisce
		lFasGPMod.getFascicoloSiusModel().setSogIdSoggetto(lFasSiepMod.getSoggetto().getIdSoggetto()); // Foreign
																										// KEY
																										// del
																										// soggetto.
		lFasGPMod.getFascicoloSiusModel().setFasSieIdFascicoloSiep(lFasSiepMod.getIdFascicoloSiep()); // Foreign
																										// KEY
																										// del
																										// fascicolo
																										// SIEP.
		lFasGPMod.getFascicoloSiusModel().setChiaveAnnoSIEP(lFasSiepMod.getChiaveAnno());
		lFasGPMod.getFascicoloSiusModel().setChiaveProgrSIEP(lFasSiepMod.getChiaveProgr());
		lFasGPMod.getFascicoloSiusModel().setChiaveUfficioSIEP(lFasSiepMod.getChiaveUfficio());
		lFasGPMod.getFascicoloSiusModel().setSoggetto(lFasSiepMod.getSoggetto()); // Soggetto recuperato dalla
																					// sessione

		ComuneModel lComMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_SEDE_MITTENTE)));

		// Caricamento Generale Procedimento
		lFasGPMod.getGeneraleProcedimentoModel().setCodSedeMittente(new String(lComMod.getCodComune()));
		lFasGPMod.getGeneraleProcedimentoModel().setCodTipoRegistro(
				getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO));

		// Il progressivo S1 viene calcolato applicativamente nel controller, oppure viene impostato in base
		// al CAMPO_CHIAVE_PROGR_S22
		// Il Campo ProgrS1 (da recenti disposizioni) viene SEMPRE impostato = al CAMPO_CHIAVE_PROGR_S22.
		if (getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22) != null) {
			lFasGPMod.getGeneraleProcedimentoModel().setAnnoS1(
					getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22));
			lFasGPMod.getGeneraleProcedimentoModel().setProgrS1(
					getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22));
		} else
			lFasGPMod.getGeneraleProcedimentoModel().setAnnoS1(new BigDecimal(DateUtils.getSysDate("yyyy"))); // Anno
																												// corrente

		lFasGPMod.getGeneraleProcedimentoModel().setCodOggettoProcedimento(
				getRequestStringParameter(CAMPO_COD_CONTENUTO));
		lFasGPMod.getGeneraleProcedimentoModel().setDataRichiesta(
				getRequestDateParameter(CAMPO_ANNO_DATA_ATTO, CAMPO_MESE_DATA_ATTO, CAMPO_GIORNO_DATA_ATTO));
		lFasGPMod.getGeneraleProcedimentoModel().setDataArrivoCancelleria(
				getRequestDateParameter(CAMPO_ANNO_DATA_ARRIVO, CAMPO_MESE_DATA_ARRIVO,
						CAMPO_GIORNO_DATA_ARRIVO));
		lFasGPMod.getGeneraleProcedimentoModel().setCodTipoAtto(
				getRequestStringParameter(CAMPO_COD_TIPO_ATTO));
		lFasGPMod.getGeneraleProcedimentoModel().setCodTipoMittenteAtto(
				getRequestStringParameter(CAMPO_COD_MITTENTE_ATTO));
		if ((getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE)).equalsIgnoreCase(""))
			lFasGPMod.getGeneraleProcedimentoModel().setCodSedeMittente(
					super.getCodComuneByDescr("-").getCodComune());
		else
			lFasGPMod.getGeneraleProcedimentoModel().setCodSedeMittente(
					super.getCodComuneByDescr(getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE))
							.getCodComune());
		lFasGPMod.getGeneraleProcedimentoModel().setCodSedeMittente(
				super.getCodComuneByDescr(getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE))
						.getCodComune());
		lFasGPMod.getGeneraleProcedimentoModel().setAnnotazione(getRequestStringParameter(CAMPO_NOTE));
		lFasGPMod.getGeneraleProcedimentoModel().setCodUfficioInserimento(
				lUtenteMod.getUfficioUtente().getCodUfficio()); // Codice dell'operatore che inserisce
		lFasGPMod.getGeneraleProcedimentoModel().setCodOperatoreInserimento(lUtenteMod.getUserId()); // Codice
																										// dell'operatore
																										// che
																										// inserisce
		lFasGPMod.getGeneraleProcedimentoModel().setDataInserimento(DateUtils.getSysDate());
		lFasGPMod.getGeneraleProcedimentoModel().setDataFinePena(
				getRequestDateParameter(CAMPO_ANNO_FINE_PENA, CAMPO_MESE_FINE_PENA, CAMPO_GIORNO_FINE_PENA));
		lFasGPMod.getGeneraleProcedimentoModel().setCodPosGiuridica(
				getRequestStringParameter(CAMPO_COD_POS_GIURIDICA));
		// Impostazione del Cod_Magistrato in CodAutoritaDelegata.
		lFasGPMod.getGeneraleProcedimentoModel().setCodAutoritaDelegata(
				getRequestStringParameter(CAMPO_COD_MAGISTRATO));
		// Impostazione della Descrizione del Mittente.
		lFasGPMod.getGeneraleProcedimentoModel().setDescrMittente(
				getRequestStringParameter(CAMPO_DESCR_MITTENTE));

		// Impostazione dell'Ufficio Mittente (solo in caso di EMA U004), Utilizzando in appoggio il campo
		// DescrDefinizione.
		// Impostazione dell'Ufficio Mittente (anche in caso di ESS U019), Utilizzando in appoggio il campo
		// DescrDefinizione.
		String aCodUfficioMittente = "";
		if (getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U004") == 0
				|| getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U019") == 0) {
			aDescrUfficioMittente = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
					.getMittenteAtto(), getRequestStringParameter(CAMPO_COD_MITTENTE_ATTO));
			aCodUfficioMittente = getCodUfficioByCodTipoUfficioDescrComune(DecodificheUtils.getCodebyDesc(
					DecodificheManager.getInstance().getTipoUfficio(), aDescrUfficioMittente),
					getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE));

			lFasGPMod.getGeneraleProcedimentoModel().setCodUfficioMittente(aCodUfficioMittente);
		} else {
			lFasGPMod.getGeneraleProcedimentoModel().setDescrDefinizione("");
		}
		// Il campo FasSiuIdFascicoloSius di Generale Procedimento viene impostato nel controller.

		// Caricamento Tenore.
		// Preleva dalla request i codici e descrizioni dei tenori, impipati rispettivamente con separatore
		// "|" e "\n".
		// Stabilisce la size dell'Array di Tenori da caricare in FascicoloGpModel.
		StringTokenizer lCodOggetto = new StringTokenizer(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_OGGETTO), "|");
		StringTokenizer lDescrOggetto = new StringTokenizer(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO), "\n");

		// Aggiunti i Codici Dettaglio Oggetti.
		String lStCodiceDet = new String(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO));

		int lSizeVector = lCodOggetto.countTokens();
		TenoreModel lTenori[] = new TenoreModel[lSizeVector];
		int lIndex = 0;

		while (lCodOggetto.hasMoreTokens()) {
			TenoreModel lTenModel = new TenoreModel();

			lTenModel.setCodOggettoTenore(lCodOggetto.nextToken());
			lTenModel.setDescrOggettoTenore(lDescrOggetto.nextToken());
			lTenModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso()); // Codice dell'ufficio
																				// dell'operatore che
																				// inserisce
			lTenModel.setCodOperatoreInserimento(getCodUtenteConnesso()); // Codice dell'operatore che
																			// inserisce
			lTenModel.setDataInserimento(DateUtils.getSysDate());
			lTenModel.setCodMagistrato(getRequestStringParameter(CAMPO_COD_MAGISTRATO));
			lTenModel.setProgrTenore(new BigDecimal((double) (lIndex + 1)));
			lTenModel.setCodEsitoTenore("-");
			// Il campo Id_Generale_Procedimento di Tenore viene impostato nel controller.

			// Aggiunta la valorizzazione dell'eventuale Dettaglio Oggetto.
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
					lTenModel.setCodDettaglioOggetto(lStCodiceDet.substring(4,8));
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
			lFasGPMod.getGeneraleProcedimentoModel().setIdLuogoDetenzione(
					getRequestStringParameter(ID_LUOGO_DETENZIONE));
			lFasGPMod.getGeneraleProcedimentoModel().setIdAltraCausa(
					getRequestStringParameter(ID_ALTRA_CAUSA));
		}

		IFascicoloSiusUDS lCtrl = SIUSLookupRemote.getFascicoloSiusUDSRemote();

		// Si esegue il metodo di inserimento Fascicolo SIUS a seguito della presa in carico.
		// lFasGPMod = lCtrl.ExInserisciFascicoloDaSiusUDS(lFasGPMod,
		// ((BigDecimal)getSessionAttribute("IdEventoInviato")+"") );
		lFasGPMod = lCtrl.ExInserisciFascicoloDaSiusUDS(lFasGPMod,
				((BigDecimal) getSessionAttribute("IdEventoInviato") + ""), 0, 0, 0);

		// I fascicoli di Applicazione Misura Sicurezza iscritti a partire da un siep ereditano le eventuali
		// misure già presenti in siep
		if (lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U023") == 0
				|| lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U086") == 0) {
			if (lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null) {
				IMisuraSicurezza lCtrlMS = SIEPLookupRemote.getMisuraSicurezzaRemote();
				// Paolo Cherubini 06/02/2012
				// relativamente alla segnalazione b2/rr/005 aggiungo il controllo che se non trovo la Misura
				// di Sicurezza
				// non genero l'errore ma proseguo nell'elaborazione
				try {
					// List lMisureSicurezzaSiep =
					// lCtrlMS.ExRicercaMisuraSicurezzaByIdFascicolo(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
					List lMisureSicurezzaSiep = lCtrlMS.ExRicercaMisuraSicurezzaByIdFascicoloOrd(lFasGPMod
							.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
					Iterator itxMis = lMisureSicurezzaSiep.iterator();
					while (itxMis.hasNext()) {
						MisuraSicurezzaModel lMisSicuSius = (MisuraSicurezzaModel) itxMis.next();
						lMisSicuSius.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel()
								.getIdFascicoloSius());
						// 16/12/2014 In caso di Presa in Carica da SIEP, la nuova Misura di Sicurezza perde
						// il legame al fascicolo SIEP.
						lMisSicuSius.setFasSieIdFascicoloSiep(null);

						lCtrlMS.ExInserisciMisuraSicurezza(lMisSicuSius);
					}
				} catch (F3BException e) {
					// misura sicurezza non trovata
				}
			}
		} // Sdoppiate Misure Sicurezza Siep ed aggiunte in sius

		// Se il fascicolo Siep appartiene ad un ufficio fuori BDI, si trasferisce il
		// RIFERIMENTO_FASCICOLO_SIUS, e poi si passa al dettaglio.
		/*
		 * Annullato il Trasferimento del RIFERIMENTO_FASCICOLO_SIUS. UfficioModel lBDIDestinataria =
		 * this.getUfficioByCodUfficio(lFasSiepMod.getChiaveUfficio()); UfficioModel lBDIMittente =
		 * this.getUfficioByCodUfficio(this.getCodUfficioUtenteConnesso()); if
		 * (lBDIMittente.getCodDistretto().trim().compareTo( lBDIDestinataria.getCodDistretto().trim())==0 )
		 * return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		 * "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&"
		 * +CAMPO_ID_FASCICOLO_SIUS+"="+lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString();
		 * else { // Metto in sessione il fascicolo SIUS per consentire la spedizione del riferimento
		 * Fascicolo Sius. setSessionAttribute("fascicoloSiusGP", lFasGPMod); //Prepara la "pagina" di
		 * destinAction RedirectTo lRedirigi = new RedirectTo(); lRedirigi.setPage( IWebConstants.PG_MAIN );
		 * lRedirigi.setAction( "siap.sius.rifasius.action.ActTrasferisciRifFascicoloSius" );
		 * 
		 * return lRedirigi.toString(); }
		 */
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIUS + "="
				+ lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString();
	}

}