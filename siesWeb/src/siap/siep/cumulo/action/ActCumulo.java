package siap.siep.cumulo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;

/**
 * Azione Helper di tutte le Action del Cumulo.
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActCumulo extends ActionSiap implements ICostantiCumulo {

	/**
	 * Imposta tutti gli attributi della Notifica
	 * 
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */

	protected NotificaModel[] setNotificheCumuloStampa(String aTipologia) throws F3BException {

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		Date lDatTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		ArrayList lNotifiche = new ArrayList();

		// SETTO ISTITUTO DETENZIONE
		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
				&& getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE) != null
				&& !getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
						.equals("")) {
			NotificaModel lNotModIst = new NotificaModel();
			String lIstituto = getRequestStringParameter(
					ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			lNotModIst.setCodEsito("-");
			lNotModIst.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModIst.setDataInserimento(DateUtils.getSysDate());
			lNotModIst.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNotModIst.setCodTipoNotifica("E");
			lNotModIst.setDataInvio(lDatTrasmissione);
			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);
			lNotifiche.add(lNotModIst);
		}

		// SETTO CSSA
		if (!isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA)
				&& getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA) != null
				&& getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA)
						.compareTo(new BigDecimal(0)) != 0
				&& !"-".equals(getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA).toString())) {
			NotificaModel lNotModCSSA = new NotificaModel();
			BigDecimal lCssa = getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA);

			// String lSedeCssa = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_CSSA);
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA)) {
				String lNoteCssa = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA);
				lNotModCSSA.setNote(lNoteCssa);
			}

			lNotModCSSA.setCodEsito("-");
			lNotModCSSA.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModCSSA.setDataInserimento(DateUtils.getSysDate());
			lNotModCSSA.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			lNotModCSSA.setCodTipoNotifica("NC"); // N
			lNotModCSSA.setDataInvio(lDatTrasmissione);
			lNotModCSSA.setCssIdCssa(lCssa);
			lNotifiche.add(lNotModCSSA);
		}

		// SETTO TDS
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE)
				&& !isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS)
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS) != null
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS).equals("")) {
			NotificaModel lNotModTDS = new NotificaModel();
			// String lTribunale = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE);
			// String lSedeTribunale = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS);
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_TDS)) {
				String lNoteTribunale = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_TDS);
				lNotModTDS.setNote(lNoteTribunale);
			}
			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			lNotModTDS.setCodTipoNotifica("T"); // E
			lNotModTDS.setDataInvio(lDatTrasmissione);
			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS));
			lNotModTDS.setUffCodUfficio(lCodiceUff);
			lNotifiche.add(lNotModTDS);
		}

		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS)
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS) != null
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS).equals("")) {
			NotificaModel lNotModUDS = new NotificaModel();
			String lUDS = getCodUfficioByCodTipoUfficioDescrComune("UDS",
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS));

			// String lSedeMagistrato =
			// getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_MAG);
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_UDS)) {
				String lNoteUDS = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_UDS);
				lNotModUDS.setNote(lNoteUDS);
			}

			lNotModUDS.setCodEsito("-");
			lNotModUDS.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModUDS.setDataInserimento(DateUtils.getSysDate());
			lNotModUDS.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			lNotModUDS.setCodTipoNotifica("MS");
			lNotModUDS.setDataInvio(lDatTrasmissione);
			lNotModUDS.setUffCodUfficio(lUDS);
			lNotifiche.add(lNotModUDS);
		}

		// SETTO NOTIFICA AUTORITA N
		if ((!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_N)
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_N).equals("-"))
				&& (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_N)
						&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_N)
								.equals("-"))) {
			String lPolizia = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_N);
			String lSedePolizia = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_N);
			NotificaModel lNotModPol = new NotificaModel();

			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_N)) {
				String lNotePolizia = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_N);
				lNotModPol.setNote(lNotePolizia);
			}

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(getCodUtenteConnesso());
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

			if (aTipologia.equals("2") || aTipologia.equals("3")) // Misura Alternativa e Generico
				lNotModPol.setCodTipoNotifica("C");
			else
				lNotModPol.setCodTipoNotifica("N");

			lNotModPol.setDataInvio(lDatTrasmissione);

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAut.setDataInserimento(DateUtils.getSysDate());
			lAut.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lNotModPol.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPol);
		}

		// Foglio Complementare
		if (!isRequestParameterNullObj(ICostantiOrdineEsecuzione.FOGLIO_COMPLEMENTARE)) {
			if (isRequestChecked(ICostantiOrdineEsecuzione.FOGLIO_COMPLEMENTARE)) {
				NotificaModel lNotFoglio = new NotificaModel();

				lNotFoglio.setCodTipoNotifica("FC");
				lNotFoglio.setDataInvio(lDatTrasmissione);
				lNotFoglio.setCodEsito("-");
				lNotFoglio.setCodOperatoreInserimento(getCodUtenteConnesso());
				lNotFoglio.setDataInserimento(DateUtils.getSysDate());
				lNotFoglio.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

				ComuneModel lComCasellarioMod = new ComuneModel(getCodComuneByDescr(
						getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS)));
				/*
				 * SedeGiudiziariaModel lSedeGiuMod = null; String lCodCas =
				 * lFascicoloModel.getSoggetto().getCodComuneCasellario(); ISedeGiudiziaria lCtrl =
				 * SIEPLookupRemote.getSedeGiudiziariaRemote(); SedeGiudiziariaModel lSedGiuMod =
				 * lCtrl.ExRicercaSedeGiudiziariaByKey(lCodCas);
				 */

				AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

				lAutMod.setCodTipoAutorita("24");

				// lAutMod.setCodSede(lSedGiuMod.getCodComune());
				lAutMod.setCodSede(lComCasellarioMod.getCodComune());
				lAutMod.setCodOperatoreInserimento(getCodUtenteConnesso());
				lAutMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lAutMod.setDataInserimento(DateUtils.getSysDate());

				// Setto l'Autorita Esterna per la notifica corrente
				lNotFoglio.setAutoritaEsterna(lAutMod);

				lNotifiche.add(lNotFoglio);
			}
		}

		// SETTO AVVOCATI
		// if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)) {
		// String[] lAvvocati = getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
		// }

		int lIndex = 0;
		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)) {
			String[] lAvvocati = getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
			for (lIndex = 0; lIndex < lAvvocati.length; lIndex++) {
				NotificaModel lNotAvv = new NotificaModel();
				if (aTipologia.equals("2") || aTipologia.equals("3")) // Misura Alternativa e Generico
				{
					lNotAvv.setCodTipoNotifica("ND");
				} else {
					lNotAvv.setCodTipoNotifica("N");
				}

				lNotAvv.setDataInvio(lDatTrasmissione);
				lNotAvv.setCodEsito("-");
				lNotAvv.setCodOperatoreInserimento(getCodUtenteConnesso());
				lNotAvv.setDataInserimento(DateUtils.getSysDate());
				lNotAvv.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
				lNotAvv.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndex]));

				if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)) {
					String[] lTipoAutoritaEsternaAvvocato = getRequestStringParameters(
							ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
					String[] lSedeAutoritaEsternaAvvocato = getRequestStringParameters(
							ICostantiAutoritaEsterna.CAMPO_COD_SEDE);

					AutoritaEsternaModel lAut = new AutoritaEsternaModel();
					lAut = new AutoritaEsternaModel();
					lAut.setCodTipoAutorita(lTipoAutoritaEsternaAvvocato[lIndex]);

					//INIZIO: MEV_21 (avvocati) - si inibisce la selezione di comuni non validi (tipo NAPOLI NORD)
//					ComuneModel lComMod = new ComuneModel(
//							getCodComuneByDescr(lSedeAutoritaEsternaAvvocato[lIndex]));
					ComuneModel lComMod = new ComuneModel(
							getCodComuneByDescrFlagVal(lSedeAutoritaEsternaAvvocato[lIndex]));
					//FINE: MEV_21
					
					lAut.setCodSede(lComMod.getCodComune());
					lAut.setCodOperatoreInserimento(getCodUtenteConnesso());
					lAut.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
					lAut.setDataInserimento(DateUtils.getSysDate());
					lNotAvv.setAutoritaEsterna(lAut);
				}

				lNotifiche.add(lNotAvv);
			}
		}

		return (NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]);
	}

	/**
	 * 
	 * @param aNotifiche
	 * @return
	 */
	protected Hashtable ricercaNotifiche(NotificaModel[] aNotifiche) {

		Hashtable lTable = new Hashtable();

		for (int i = 0; i < aNotifiche.length; i++) {
			// cssa
			if (aNotifiche[i].getCssIdCssa() != null) {
				NotificaModel lNotCssa = aNotifiche[i];
				lTable.put("NotCssa", lNotCssa);
			}

			// istituto
			if (aNotifiche[i].getIstitutoDetenzione() != null) {
				NotificaModel lNotIstituto = aNotifiche[i];
				lTable.put("lNotIstituto", lNotIstituto);
			}

			// autorità esterna E
			if (aNotifiche[i].getAutEstIdAutoritaEsterna() != null
					&& aNotifiche[i].getCodTipoNotifica().equals("E")) {
				NotificaModel lNotAutoritaE = aNotifiche[i];
				lTable.put("AutE", lNotAutoritaE);
			} // autorità esterna N o C

			if (aNotifiche[i].getAutEstIdAutoritaEsterna() != null
					&& (aNotifiche[i].getCodTipoNotifica().equals("N")
							|| aNotifiche[i].getCodTipoNotifica().equals("C"))
					&& aNotifiche[i].getAvvIdAvvocatoFascicoloSiep() == null) {
				NotificaModel lNotAutoritaN = aNotifiche[i];
				lTable.put("AutN", lNotAutoritaN);
			}

			// autorità esterna C
			if (aNotifiche[i].getAutEstIdAutoritaEsterna() != null
					&& aNotifiche[i].getCodTipoNotifica().equals("C")) {
				NotificaModel lNotAutoritaC = aNotifiche[i];
				lTable.put("AutC", lNotAutoritaC);
			}

			// istituto
			if (aNotifiche[i].getIstitutoDetenzione() != null
					&& aNotifiche[i].getCodTipoNotifica().equals("E")) {
				NotificaModel lNotIst = aNotifiche[i];
				lTable.put("Ist", lNotIst);
			}

			// Ufficio TDS

			/*
			 * if (aNotifiche[i].getUffCodUfficio() != null) { NotificaModel lNotUfficioTDS = aNotifiche[i];
			 * lTable.put("UffTDS", lNotUfficioTDS); } //Ufficio UDS if (aNotifiche[i].getUffCodUfficio() !=
			 * null) { NotificaModel lNotUfficioUDS = aNotifiche[i]; lTable.put("UffUDS", lNotUfficioUDS); }
			 */

			if (aNotifiche[i].getUffCodUfficio() != null) {
				NotificaModel lNotUfficio = aNotifiche[i];
				if (lNotUfficio.getUfficio().getCodTipoUfficio().equals("UDS")) {
					NotificaModel lNotUfficioUDS = aNotifiche[i];
					lTable.put("UffUDS", lNotUfficioUDS);
				}
				if (lNotUfficio.getUfficio().getCodTipoUfficio().equals("TDS")) {
					NotificaModel lNotUfficioTDS = aNotifiche[i];
					lTable.put("UffTDS", lNotUfficioTDS);
				}
			}
		}

		return lTable;
	}

	/*
	 * metodo specializzato per gli eventi del fascicolo cumulato -- 19-05-05 -- Dario -- Luciana SE
	 * CodTipoEvento = 05(Richiesta Istruttoria) NON DEVE FARE IL CONTROLLO
	 */
	protected void isEventoNonValidatoPerFascicoloCumulato(BigDecimal aIdFascCumulato) throws F3BException {
		IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
		EventoModel lEveMod = new EventoModel();
		lEveMod = lCtrl.ExRicercaEventoByFascicoloSiepDescUfficioConnesso(aIdFascCumulato,
				getCodUfficioUtenteConnesso());

		if (lEveMod != null && lEveMod.getCodTipoEvento() != null
				&& !lEveMod.getCodTipoEvento().equals("05")) {
			if (lEveMod.getFlagDocumentoRegistrato() == null
					|| "N".equals(lEveMod.getFlagDocumentoRegistrato())) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"Esiste un evento NON validato del fascicolo cumulato. Validarlo o cancellarlo e rieseguire la funzione.");
			}
		}
	}

}