package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.collaboratore.controller.ICollaboratore;
import siap.sius.collaboratore.model.CollaboratoreModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.controller.IFascicoloSiusUDS;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInsFascicoloDaSoggettoUDS
 * </p>
 * <p>
 * Description: Classe Azione di inserimento del Fascicolo SIUS da Soggetto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
public class ActInsFascicoloDaSoggettoUDS extends ActionSiap implements ICostantiFascicoloSius {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Recupero l'utente e il Soggetto dalla sessione
		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		SoggettoModel lSoggettoMod = (SoggettoModel) getSessionAttribute("soggetto");
		// FascicoloSiepModel lFasSiepMod = (FascicoloSiepModel)getSessionAttribute("fascicolo");

		// 22/05/2009 Controllo Ufficio mittente.
		// Le EMA accettano mittenti di TDS o UDS; oppure PM o PGCAP in caso di arresti domiciliari.
		String aDescrUfficioMittente = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
				.getMittenteAtto(), getRequestStringParameter(CAMPO_COD_MITTENTE_ATTO));
		BigDecimal IdFascicoloPadreEsecuzione = null;
		BigDecimal IdFascicoloSiusPadre = null;
		FascicoloGPModel lFasPadreGPMod = null;

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

		// MEV10-s3: aggiunte or condition per gestire uffici minorenni
		if (((CAMPO_COD_CONTENUTO.compareTo("U019") == 0 && lCodTipoUfficioMittente.compareTo("UDS") != 0
				&& lCodTipoUfficioMittente.compareTo("UDSM") != 0
				&& lCodTipoUfficioMittente.compareTo("TDS") != 0 && lCodTipoUfficioMittente.compareTo("TDSM") != 0)
				|| (CAMPO_COD_CONTENUTO.compareTo("U004") == 0
				&& lCodTipoUfficioMittente.compareTo("TDS") != 0
				&& lCodTipoUfficioMittente.compareTo("TDSM") != 0
				&& lCodTipoUfficioMittente.compareTo("UDS") != 0
				&& lCodTipoUfficioMittente.compareTo("UDSM") != 0 && lCodTipoUfficioMittente.compareTo("-") != 0))
				&& (!(CAMPO_COD_CONTENUTO.compareTo("U004") == 0
						&& (lCodTipoUfficioMittente.compareTo("PM") == 0 || lCodTipoUfficioMittente
								.compareTo("PGCAP") == 0) && lCodiciOggetto.indexOf("2368") >= 0)))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Ufficio Mittente non valido per il contenuto selezionato");
		/*
		 * if ((((getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U019")==0 ) &&
		 * (lCodTipoUfficioMittente.compareTo("UDS")!=0) ) ||
		 * ((getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U004")==0 ) &&
		 * ((lCodTipoUfficioMittente.compareTo("TDS")!=0) && (lCodTipoUfficioMittente.compareTo("UDS")!=0) )
		 * )) && ( !((getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U004")==0 ) &&
		 * ((lCodTipoUfficioMittente.compareTo("PM")==0) || (lCodTipoUfficioMittente.compareTo("PGCAP")==0) )
		 * && lCodiciOggetto.toString().indexOf("2368")>=0 ) ) ) throw new SIUSException(
		 * SIUSException.USER_MESSAGE, "Ufficio Mittente non valido per il contenuto selezionato" );
		 */
		// STUB 19/07/2007 Gestione dei procedimenti di EMA e ESS e EMS.
		// Recupero del Procedimento di Esecuzione della MA-SS-MS, nel caso fosse impostato nella form di
		// inserimento.
		// N.B. Se non esiste, il controller solleva un errore di eccezione sull'esistenza del procedimento.
		/*if ((getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22) != null)
				&& (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO) != null)
				&& (((getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S22") == 0) && (getRequestStringParameter(
						CAMPO_COD_CONTENUTO).compareTo("U004") != 0))
						|| ((getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S12") == 0) && (getRequestStringParameter(
								CAMPO_COD_CONTENUTO).compareTo("U019") != 0)) || ((getRequestStringParameter(
						CAMPO_COD_TIPO_REGISTRO).compareTo("S09") == 0) && (getRequestStringParameter(
						CAMPO_COD_CONTENUTO).compareTo("U024") != 0)))) {*/
		  
		if (   getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22) != null
		    && getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO) != null
		    && (   (   getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S22") == 0 
		            && getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U004") != 0)
		        || (   getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S12") == 0 
		            && getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U019") != 0) 
		        || (   getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S09") == 0 
		            && getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U024") != 0)
		        // MEV_2023-35- si aggiunge la gestione delle EPS
                || (   getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S30") == 0 
                    && getRequestStringParameter(CAMPO_COD_CONTENUTO).compareTo("U126") != 0)	
                // MEV_2023-35 - FINE
		      )
		   ) 
		{  
			// String lCodContenuto =
			// ((getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S22")==0) ? "U004" : "U019");
			String lCodContenuto = "";

			if (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S22") == 0)
				lCodContenuto = "U004";
			else if (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S12") == 0)
				lCodContenuto = "U019";
			else if (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S09") == 0)
			  lCodContenuto = "U024";
			// MEV_2023-35- si aggiunge la gestione delle EPS
			else if (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S30") == 0)
				lCodContenuto = "U126";
			// MEV_2023-35 - FINE

			IFascicoloSiusUDS lCtrl = SIUSLookupRemote.getFascicoloSiusUDSRemote();
			/* boolean esiste = */lCtrl.ExistProcedimentoEsecuzione(
					getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22),
					getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22), lCodContenuto,
					getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO), getCodUfficioUtenteConnesso(),
					lSoggettoMod.getIdSoggetto());

			if (!isRequestParameterNullObj("CampoIdFascicoloPadreEsecuzioneHidden")) {
				if (getRequestBigDecimalParameter("CampoIdFascicoloPadreEsecuzioneHidden") != null) {
					IdFascicoloPadreEsecuzione = getRequestBigDecimalParameter("CampoIdFascicoloPadreEsecuzioneHidden");
					lFasPadreGPMod = lCtrl.ExRicercaFascicoloByKey(IdFascicoloPadreEsecuzione);

				}
			} else {
				// Recupero l'Id del fascicolo SIUS padre
				// Al momento gli attributi del fascicolo padre vengono ereditati dal figlio
				// solo quando l'iscrizione viene fatta a partire dal fascicolo contenitore
			}
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
		lFasGPMod.getFascicoloSiusModel().setSogIdSoggetto(lSoggettoMod.getIdSoggetto()); // Foreign KEY del
																							// soggetto.
		lFasGPMod.getFascicoloSiusModel().setFasSieIdFascicoloSiep(null); // Foreign KEY del fascicolo SIEP.
		lFasGPMod.getFascicoloSiusModel().setChiaveAnnoSIEP(null);
		lFasGPMod.getFascicoloSiusModel().setChiaveProgrSIEP(null);
		lFasGPMod.getFascicoloSiusModel().setChiaveUfficioSIEP(null);
		lFasGPMod.getFascicoloSiusModel().setSoggetto(lSoggettoMod); // Soggetto recuperato dalla sessione

		ComuneModel lComMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_SEDE_MITTENTE)));

		// Caricamento Generale Procedimento
		lFasGPMod.getGeneraleProcedimentoModel().setCodSedeMittente(new String(lComMod.getCodComune()));
		lFasGPMod.getGeneraleProcedimentoModel().setCodTipoRegistro(
				getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO));

		// Il progressivo S1 viene calcolato applicativamente nel controller, oppure viene impostato in base
		// al CAMPO_CHIAVE_PROGR_S22
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
		// 18/12/2003 Impostazione del Cod_Magistrato in CodAutoritaDelegata.
		lFasGPMod.getGeneraleProcedimentoModel().setCodAutoritaDelegata(
				getRequestStringParameter(CAMPO_COD_MAGISTRATO));
		// 14/01/2004 Impostazione della Descrizione del Mittente.
		lFasGPMod.getGeneraleProcedimentoModel().setDescrMittente(
				getRequestStringParameter(CAMPO_DESCR_MITTENTE));

		// if (getRequestStringParameter( LUOGO_DETENZIONE ).length()>0 )
		if (!isRequestParameterNullObj(LUOGO_DETENZIONE)) {
			if (getRequestStringParameter(LUOGO_DETENZIONE).length() > 0
					&& isRequestChecked(CAMPO_VALIDA_LUOGO_DET)) {
				lFasGPMod.getGeneraleProcedimentoModel().setIdLuogoDetenzione(
						getRequestStringParameter(ID_LUOGO_DETENZIONE));
			}
		}
		// 30/09/2004 Impostazione dell'Ufficio Mittente (solo in caso di EMA U004), Utilizzando in appoggio
		// il campo DescrDefinizione.
		// 24/07/2007 Impostazione dell'Ufficio Mittente (anche in caso di ESS U019), Utilizzando in appoggio
		// il campo DescrDefinizione.
		// 21/04/2011 Impostazione dell'Ufficio Mittente (anche in caso di EMS U024), Utilizzando in appoggio
		// il campo DescrDefinizione.
		// TODO
		// Modifica del 23/10/2013 mev "Revisione Misure di Sicurezza SIUS"
		// Eliminato controllo sull' ufficio mittente (Ufficio Inesistente)
		// nel caso di "Iscrizione di una Esecuzione Misura di Sicurezza"
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

		// STUB 28/12/2003 Gestione del Luogo detenzione.
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

		// STUB 02/12/2004 Recupero FascicoloSiusOrigine. //01/03/2005 Controllo nullValue
		if (!isRequestParameterNullObj(ID_FASCICOLO_SIUS_ORIGINE))
			lFasGPMod.getFascicoloSiusModel().setIdFascicoloSiusOrigine(
					getRequestBigDecimalParameter(ID_FASCICOLO_SIUS_ORIGINE));

		IFascicoloSiusUDS lCtrl = SIUSLookupRemote.getFascicoloSiusUDSRemote();

		// Aggiunta eredità quantum pena per fascicoli ESS padre iscritti come collegati a
		// fascicoli Conversione PP o a fascicoli Applicazione SS

		int durataEsitoAnni = 0;
		int durataEsitoMesi = 0;
		int durataEsitoGiorni = 0;
		if ((!isRequestParameterNullObj(ID_FASCICOLO_SIUS_ORIGINE))
				&& (getRequestBigDecimalParameter(ID_FASCICOLO_SIUS_ORIGINE) != null)) {
			BigDecimal IdFascicoloSiusOrigine = getRequestBigDecimalParameter(ID_FASCICOLO_SIUS_ORIGINE);
			IdFascicoloSiusPadre = IdFascicoloSiusOrigine;
			// FascicoloGPModel lFascOrigine =
			// lCtrl.ExRicercaFascicoloByKey(getRequestBigDecimalParameter(ID_FASCICOLO_SIUS_ORIGINE ));
			FascicoloGPModel lFascOrigine = lCtrl.ExRicercaFascicoloByKey(IdFascicoloSiusOrigine);

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
				aDepositoOrdinanzaPc = lCtrlDOP.ExRicercaDepositoOrdinanzaPcByGenProcTipoOrd(lFascOrigine
						.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(), "SS");

				if (aDepositoOrdinanzaPc != null) {
					if (aDepositoOrdinanzaPc.getNumAnniDetenzioneDom() != null)
						durataEsitoAnni = aDepositoOrdinanzaPc.getNumAnniDetenzioneDom().intValue();
					if (aDepositoOrdinanzaPc.getNumMesiDetenzioneDom() != null)
						durataEsitoMesi = aDepositoOrdinanzaPc.getNumMesiDetenzioneDom().intValue();
					if (aDepositoOrdinanzaPc.getNumGiorniDetenzioneDom() != null)
						durataEsitoGiorni = aDepositoOrdinanzaPc.getNumGiorniDetenzioneDom().intValue();
				}
			}
		}

		// Fine aggiunta eredità quantum pena

		// Per i fascicoli figli di EMA-ESS-EMS carico l'eventuale Titolo Esecutivo del padre
		if ((lFasPadreGPMod != null)
				&& (lFasPadreGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep() != null))
			lFasGPMod.getFascicoloSiusModel().setFasSieIdFascicoloSiep(
					lFasPadreGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

		// Se si sta iscrivendo un procedimento collegato bisogna ereditare residenza e domicilio
		// se invece è un EMA figlio si eredita da EMA
		if (IdFascicoloPadreEsecuzione != null)
			IdFascicoloSiusPadre = IdFascicoloPadreEsecuzione;

		// STUB 08/05/2008 Implementato parametro IdEventoInviato.
		if (this.isSessionAttributeNullObj("IdEventoInviato"))
			lFasGPMod = lCtrl.ExInserisciFascicoloSiusUDS(lFasGPMod, null, IdFascicoloSiusPadre,
					durataEsitoAnni, durataEsitoMesi, durataEsitoGiorni);
		// lFasGPMod = lCtrl.ExInserisciFascicoloSiusUDS(lFasGPMod, null, IdFascicoloPadreEsecuzione);
		else
			// lFasGPMod = lCtrl.ExInserisciFascicoloSiusUDS(lFasGPMod,
			// ((BigDecimal)getSessionAttribute("IdEventoInviato")+""), IdFascicoloPadreEsecuzione);
			lFasGPMod = lCtrl.ExInserisciFascicoloSiusUDS(lFasGPMod,
					((BigDecimal) getSessionAttribute("IdEventoInviato") + ""), IdFascicoloSiusPadre,
					durataEsitoAnni, durataEsitoMesi, durataEsitoGiorni);

		// Per fascicoli figli di EMA, ESS, EMS eredita Titolo Esecutivo e Collaboratore di Giustizia
		if (IdFascicoloPadreEsecuzione != null) {
			boolean isColla = false;

			ICollaboratore lCtrlColla = SIUSLookupRemote.getCollaboratoreRemote();
			if (lCtrlColla.ExIsPackage())
				isColla = lCtrlColla.ExIsCollaboratore(IdFascicoloPadreEsecuzione,
						getCodUfficioUtenteConnesso());

			// Se il fascicolo padre esecuzione è relativo ad un Collaboratore di Giustizia
			// associo al fascicolo figlio l'eventuale ultima collaborazione attiva
			if (isColla) {
				CollaboratoreModel lCollMod = new CollaboratoreModel();
				Vector lElencoColl = null;

				lCollMod.setIdFascicoloSius(IdFascicoloPadreEsecuzione);
				lCollMod.setCodUfficio((getCodUfficioUtenteConnesso()));

				lElencoColl = lCtrlColla.ExRicercaCollaboratore(lCollMod);
				// Vettore ordinato per data fine Desc
				CollaboratoreModel lCollModCur = null;
				Iterator itxColl = lElencoColl.iterator();
				while (itxColl.hasNext()) {
					lCollModCur = (CollaboratoreModel) itxColl.next();
					if (lCollModCur.getDataFine() == null)
						break;
				}

				lCollMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lCollMod.setCodOperatoreInserimento(getCodUtenteConnesso());
				if ((lCollModCur != null) && (lCollModCur.getDataInizio() != null))
					lCollMod.setDataInizio(lCollModCur.getDataInizio());
				// if ((lCollModCur != null) && (lCollModCur.getDataFine() != null))
				// lCollMod.setDataFine(lCollModCur.getDataFine());
				// Se data fine non è null non effettuo l'associazione
				if ((lCollModCur != null) && (lCollModCur.getDataFine() == null)) {
					lCollMod.setIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
					lCtrlColla.ExInserisciCollaboratore(lCollMod);
				}
			}
		}
		// restituisce la jsp di VIEW
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIUS + "="
				+ lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString();
	}

}