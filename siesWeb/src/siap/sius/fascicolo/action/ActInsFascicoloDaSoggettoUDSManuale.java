package siap.sius.fascicolo.action;

import java.math.BigDecimal;
import java.util.StringTokenizer;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.fascicolo.controller.IFascicoloSiusUDS;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInsFascicoloDaSoggettoUDSManuale
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
public class ActInsFascicoloDaSoggettoUDSManuale extends ActionSiap implements ICostantiFascicoloSius {

	public String processRequest() throws Exception {

		// Recupero l'utente e il Soggetto dalla sessione
		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		SoggettoModel lSoggettoMod = (SoggettoModel) getSessionAttribute("soggetto");
		// FascicoloSiepModel lFasSiepMod = (FascicoloSiepModel)getSessionAttribute("fascicolo");

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

		// MEV10-s3: aggiunte or condition per gestire uffici minorenni
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

		// STUB 19/07/2007 Gestione dei procedimenti di EMA, ESS ed EMS
		// Recupero del Procedimento di Esecuzione della misura alternativa, nel caso fosse impostato nella
		// form di inserimento.
		// N.B. Se non esiste, il controller solleva un errore di eccezione sull'esistenza del procedimento.
		if ((getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22) != null)
				&& (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO) != null)
				&& (((getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S22") == 0) && (getRequestStringParameter(
						CAMPO_COD_CONTENUTO).compareTo("U004") != 0))
						|| ((getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S12") == 0) && (getRequestStringParameter(
								CAMPO_COD_CONTENUTO).compareTo("U019") != 0)) || ((getRequestStringParameter(
						CAMPO_COD_TIPO_REGISTRO).compareTo("S09") == 0) && (getRequestStringParameter(
						CAMPO_COD_CONTENUTO).compareTo("U024") != 0)))) {
			// String lCodContenuto =
			// ((getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S22")==0) ? "U004" : "U019");
			String lCodContenuto = "";

			if (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S22") == 0)
				lCodContenuto = "U004";
			else if (getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO).compareTo("S12") == 0)
				lCodContenuto = "U019";
			else
				lCodContenuto = "U024";

			IFascicoloSiusUDS lCtrl = SIUSLookupRemote.getFascicoloSiusUDSRemote();
			/* boolean esiste = */lCtrl.ExistProcedimentoEsecuzione(
					getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_S22),
					getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_S22), lCodContenuto,
					getRequestStringParameter(CAMPO_COD_TIPO_REGISTRO), getCodUfficioUtenteConnesso(),
					lSoggettoMod.getIdSoggetto());
		}

		// Istanzio il Model che incapsula il FascicoloSIUS e il GeneraleProcedimento
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

		// 30/09/2004 Impostazione dell'Ufficio Mittente (solo in caso di EMA U004), Utilizzando in appoggio
		// il campo DescrDefinizione.
		// 24/07/2007 Impostazione dell'Ufficio Mittente (anche in caso di ESS U019), Utilizzando in appoggio
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

		IFascicoloSiusUDS lCtrl = SIUSLookupRemote.getFascicoloSiusUDSRemote();
		lFasGPMod = lCtrl.ExInserisciFascicoloSiusUDSManuale(lFasGPMod);

		// restituisce la jsp di VIEW
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIUS + "="
				+ lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString();
	}

}