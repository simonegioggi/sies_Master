package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.StringTokenizer;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.fascicolo.controller.IFascicoloSiusUDS;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciFascicoloUDSManuale
 * </p>
 * <p>
 * Description: Classe Azione di inserimento del Fascicolo SIUS per UDS
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
public class ActInserisciFascicoloUDSManuale extends ActionSiap implements ICostantiFascicoloSius {

	public String processRequest() throws Exception {

		// Si Recupera l'utente e il Fascicolo SIEP dalla sessione.
		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		FascicoloSiepModel lFasSiepMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// 22/05/2009 Controllo Ufficio mittente.
		// Le EMA accettano mittenti di TDS o UDS; oppure PM o PGCAP in caso di arresti domiciliari.
		String aDescrUfficioMittente = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
				.getMittenteAtto(), getRequestStringParameter(CAMPO_COD_MITTENTE_ATTO));

		// TODO carmela commentato Controllo Ufficio mittente (verificare)
		// Modifica del 10/09/2013 mev "Revisione Misure di Sicurezza SIUS"
		// Eliminato controllo su ufficio mittente se il campo non è valorizzato
		// nel caso di "Iscrizione di una Esecuzione Misura di Sicurezza"
		String lCodTipoUfficioMittente = DecodificheUtils.getCodebyDesc(DecodificheManager.getInstance()
				.getTipoUfficio(), aDescrUfficioMittente);
		String[] lArrayCodici = (getRequestStringParameters(ICostantiFascicoloSius.CAMPO_COD_OGGETTO));
		String lCodiciOggetto = "";

		for (int i = 0; i < lArrayCodici.length; i++)
			lCodiciOggetto += lArrayCodici[i];

		// MERGE v10: aggiunti controlli per uffici minori
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

		// STUB 19/07/2007 Gestione dei procedimenti di EMA, ESS ed EMS
		// Recupero del Procedimento di Esecuzione della misura alternativa, nel caso fosse impostato nella
		// form di inserimento.
		// N.B. Se non esiste, il controller solleva un errore di eccezione sull'esistenza del procedimento.
		if (   getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22) != null
		    && getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO) != null
		    && (   (   getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S22") == 0
		            && getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U004") != 0)
		        || (   getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S12") == 0
		            && getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U019") != 0) 
		        || (   getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S09") == 0
		            && getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U024") != 0)
		            // MEV_2023-35 - si aggiunge il provvedimento di esecuzione pene sospese
		        || (   getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S30") == 0
		            && getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U126") != 0)
		            // MEV_2023-35
		       )
		   ) 
		{
		  //String lCodContenuto = ((getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S22") == 0) ? "U004"
		  //    : "U019");
		  String lCodContenuto = "";
		  if (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S22") == 0)
		    lCodContenuto = "U004";
		  else if (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S12") == 0)
		    lCodContenuto = "U019";
		  else if (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S09") == 0)
		    lCodContenuto = "U024";
		  else if (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S30") == 0)
		    lCodContenuto = "U126";
		  
		  IFascicoloSiusUDS lCtrl = SIUSLookupRemote.getFascicoloSiusUDSRemote();
		  /* boolean esiste = */lCtrl.ExistProcedimentoEsecuzione(
		      getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22),
		      getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22), lCodContenuto,
		      getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO), getCodUfficioUtenteConnesso(),
		      lFasSiepMod.getSoggetto().getIdSoggetto());
		}  

		// Istanzio il Model che incapsula il FascicoloSIUS, il GeneraleProcedimento e il Tenore
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();

		// Caricamento Fascicolo SIUS
		lFasGPMod.getFascicoloSiusModel().setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO)); // Anno
																											// impostato
																											// nella
																											// form
		lFasGPMod.getFascicoloSiusModel().setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR)); // Progressivo
																												// impostato
																												// nella
																												// form
		lFasGPMod.getFascicoloSiusModel().setChiaveUfficio(lUtenteMod.getUfficioUtente().getCodUfficio()); // Ufficio
																											// dell'operatore
																											// che
																											// inserisce
		lFasGPMod.getFascicoloSiusModel().setDataInserimento(DateUtils.getSysDate());
		lFasGPMod.getFascicoloSiusModel().setDataIscrizione(DateUtils.getSysDate());
		lFasGPMod.getFascicoloSiusModel().setCodStatoFascicolo("02"); // Stato Fascicolo SIUS settato ad
																		// aperto
		lFasGPMod.getFascicoloSiusModel().setCodOperatoreInserimento(lUtenteMod.getUserId()); // Codice
																								// dell'operatore
																								// che
																								// inserisce
		lFasGPMod.getFascicoloSiusModel().setCodUfficioInserimento(
				lUtenteMod.getUfficioUtente().getCodUfficio()); // Codice dell'ufficio inserimento
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
		lFasGPMod.getFascicoloSiusModel().setSoggetto(lFasSiepMod.getSoggetto()); // Soggetto recuperato dal
																					// Fascicolo SIEP in
																					// sessione

		ComuneModel lComMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_SEDE_MITTENTE)));

		// Caricamento Generale Procedimento
		lFasGPMod.getGeneraleProcedimentoModel().setCodSedeMittente(new String(lComMod.getCodComune()));
		lFasGPMod.getGeneraleProcedimentoModel().setCodTipoRegistro(
				getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO));

		// Il progressivo S1 viene calcolato applicativamente nel controller, oppure viene impostato in base
		// al CAMPO_CHIAVE_PROGR_S22
		// STUB 11/02/2004 Il Campo ProgrS1 (da recenti disposizioni) viene SEMPRE impostato = al
		// CAMPO_CHIAVE_PROGR_S22.
		if (getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22) != null) {
			lFasGPMod.getGeneraleProcedimentoModel().setAnnoS1(
					getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22));
			lFasGPMod.getGeneraleProcedimentoModel().setProgrS1(
					getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22));
		} else
			lFasGPMod.getGeneraleProcedimentoModel().setAnnoS1(new BigDecimal(DateUtils.getSysDate("yyyy")));

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
				lUtenteMod.getUfficioUtente().getCodUfficio()); // Codice dell'ufficio dell'operatore che
																// inserisce
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

		// 30/09/2004 Impostazione dell'Ufficio Mittente (solo in caso di EMA U004), Utilizzando in appoggio
		// il campo DescrDefinizione.

		// TODO
		// Modifica del 23/10/2013 mev "Revisione Misure di Sicurezza SIUS"
		// Eliminato controllo sull' ufficio mittente (Ufficio Inesistente)
		// nel caso di "Iscrizione di una Esecuzione Misura di Sicurezza"
		String aCodUfficioMittente = "";
        if (   getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U004") == 0
            || getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U019") == 0
            // MEV_2023-35 - si aggiunge U126
            || getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U126") == 0
           ) 
        {// In caso di ESS, EPS e EMA decodifico l'ufficio Mittente e lo salvo su GP.COD_UFF_MITT
			aDescrUfficioMittente = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
					.getMittenteAtto(), getRequestStringParameter(CAMPO_COD_MITTENTE_ATTO));
			aCodUfficioMittente = getCodUfficioByCodTipoUfficioDescrComune(DecodificheUtils.getCodebyDesc(
					DecodificheManager.getInstance().getTipoUfficio(), aDescrUfficioMittente),
					getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE));

			lFasGPMod.getGeneraleProcedimentoModel().setCodUfficioMittente(aCodUfficioMittente);
		} else {
			lFasGPMod.getGeneraleProcedimentoModel().setDescrDefinizione("");
		}

		// Il campo FasSiuIdFascicoloSius di Generale Procedimento viene impostato nel controller

		// Caricamento Tenore
		// Preleva dalla request i codici e descrizioni dei tenori, impipati rispettivamente con separatore
		// "|" e "\n".
		// Stabilisce la size dell'Array di Tenori da caricare in FascicoloGpModel.
		StringTokenizer lCodOggetto = new StringTokenizer(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_OGGETTO), "|");
		StringTokenizer lDescrOggetto = new StringTokenizer(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO), "\n");

		// Valorizzazione dei Codici Dettaglio Oggetti.
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
			/* Duplicazione del Luogo Detenzione per staccare da SIEP */
			LuogoDetenzioneModel luogoDetenzioneOrigine = new LuogoDetenzioneModel();
			LuogoDetenzioneModel luogoDetenzioneSIUS = new LuogoDetenzioneModel();
			ILuogoDetenzione lCtrlLuoDet = SIEPLookupRemote.getLuogoDetenzioneRemote();

			luogoDetenzioneOrigine = lCtrlLuoDet.ExRicercaLuogoDetenzioneByKey(new BigDecimal(
					getRequestStringParameter(ID_LUOGO_DETENZIONE)));
			luogoDetenzioneOrigine.setFasSieIdFascicoloSiep(null); /*
																	 * Altrimenti SIEP lo vede tra le posiz.
																	 * giuridiche
																	 */
			luogoDetenzioneOrigine.setDataFineDetenzione(null);
			luogoDetenzioneOrigine.setPosGiuIdPosizioneGiuridica(null);
			luogoDetenzioneSIUS = lCtrlLuoDet.ExInserisciLuogoDetenzione(luogoDetenzioneOrigine);
			// lFasGPMod.getGeneraleProcedimentoModel().setIdLuogoDetenzione(getRequestStringParameter(
			// ID_LUOGO_DETENZIONE ));
			lFasGPMod.getGeneraleProcedimentoModel().setIdLuogoDetenzione(
					luogoDetenzioneSIUS.getIdLuogoDetenzione().toString());
			lFasGPMod.getGeneraleProcedimentoModel().setIdAltraCausa(
					getRequestStringParameter(ID_ALTRA_CAUSA));
		}

		// FascicoloSiusController lCtrl = new FascicoloSiusController();
		IFascicoloSiusUDS lCtrlFS = SIUSLookupRemote.getFascicoloSiusUDSRemote();
		lFasGPMod = lCtrlFS.ExInserisciFascicoloSiusUDSManuale(lFasGPMod);

		// restituisce la jsp di VIEW
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIUS + "="
				+ lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString();
	}

}