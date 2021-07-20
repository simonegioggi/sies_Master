package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import f3b.util.DateUtils;
import f3b.util.F3BException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActRidetPena extends ActionSiap implements ICostantiNotifica {

	/**
	 * Imposta tutte le notifiche associate all'evento prodotto
	 * 
	 * @param lKey
	 * @return
	 * @throws F3BException
	 */
	protected NotificaModel[] setNotificheMisuraAlternativa() throws F3BException {

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);

		ArrayList lNotifiche = new ArrayList();

		// Foglio Complementare
		if (!this.isRequestParameterNullObj(ICostantiOrdineEsecuzione.FOGLIO_COMPLEMENTARE)) {
			if (this.isRequestChecked(ICostantiOrdineEsecuzione.FOGLIO_COMPLEMENTARE)) {
				NotificaModel lNotFoglio = new NotificaModel();

				lNotFoglio.setCodTipoNotifica("FC");
				lNotFoglio.setDataInvio(lDataTrasmissione);
				lNotFoglio.setCodEsito("-");
				lNotFoglio.setCodOperatoreInserimento(lCodiceOperatore);
				lNotFoglio.setDataInserimento(DateUtils.getSysDate());
				lNotFoglio.setCodUfficioInserimento(lCodiceUfficio);

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
				lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
				lAutMod.setCodUfficioInserimento(lCodiceUfficio);
				lAutMod.setDataInserimento(DateUtils.getSysDate());

				// Setto l'Autorita Esterna per la notifica corrente
				lNotFoglio.setAutoritaEsterna(lAutMod);

				lNotifiche.add(lNotFoglio);
			}
		}

		// SETTO NOTIFICA AUTORITA E
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
				&& this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E) != null
				&& !this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)
						.equals("-")) {
			String lPolizia = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			String lSedePolizia = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
			NotificaModel lNotModPol = new NotificaModel();
			if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E)) {
				String lNotePolizia = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);
				lNotModPol.setNote(lNotePolizia);
			}

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("E");
			lNotModPol.setDataInvio(lDataTrasmissione);
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();

			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPol);
		}

		// SETTO ISTITUTO DETENZIONE
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
				&& getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE) != null
				&& !getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
						.equals("")) {

			NotificaModel lNotModIst = new NotificaModel();
			String lIstituto = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			lNotModIst.setCodEsito("-");
			lNotModIst.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModIst.setDataInserimento(DateUtils.getSysDate());
			lNotModIst.setCodUfficioInserimento(lCodiceUfficio);
			lNotModIst.setCodTipoNotifica("E");
			lNotModIst.setDataInvio(lDataTrasmissione);

			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);
			lNotifiche.add(lNotModIst);

		}

		// SETTO NOTIFICA AUTORITA C
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
				&& this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C) != null
				&& !this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
						.equals("-")) {
			String lPolizia = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C);
			String lSedePolizia = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C);
			NotificaModel lNotModPolizia = new NotificaModel();
			if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_C)) {
				String lNotePolizia = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_C);
				lNotModPolizia.setNote(lNotePolizia);
			}

			lNotModPolizia.setCodEsito("-");
			lNotModPolizia.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPolizia.setDataInserimento(DateUtils.getSysDate());
			lNotModPolizia.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPolizia.setCodTipoNotifica("AA");
			lNotModPolizia.setDataInvio(lDataTrasmissione);
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();

			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPolizia.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPolizia);
		}

		// SETTO CSSA
		if (!this.isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA)) {

			NotificaModel lNotModCSSA = new NotificaModel();
			BigDecimal lCssa = this.getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA);

			lNotModCSSA.setCodEsito("-");
			lNotModCSSA.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModCSSA.setDataInserimento(DateUtils.getSysDate());
			lNotModCSSA.setCodUfficioInserimento(lCodiceUfficio);

			lNotModCSSA.setCodTipoNotifica("CS");

			lNotModCSSA.setDataInvio(lDataTrasmissione);
			lNotModCSSA.setCssIdCssa(lCssa);
			lNotifiche.add(lNotModCSSA);

		}

		// MAGISTRATO DI SORVEGLIANZA
		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_MDS)) {

			NotificaModel lNotModUDS = new NotificaModel();
			String lUDS = this.getCodUfficioByCodTipoUfficioDescrComune("UDS",
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_MDS));

			lNotModUDS.setCodEsito("-");
			lNotModUDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModUDS.setDataInserimento(DateUtils.getSysDate());
			lNotModUDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModUDS.setCodTipoNotifica("MS");
			lNotModUDS.setDataInvio(lDataTrasmissione);
			lNotModUDS.setUffCodUfficio(lUDS);
			lNotifiche.add(lNotModUDS);

		}

		// TRIBUNALE DI SORVEGLIANZA
		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_SEDE_TDS)) {

			NotificaModel lNotModTDS = new NotificaModel();
			String lTDS = this.getCodUfficioByCodTipoUfficioDescrComune("TDS",
					getRequestStringParameter(ICostantiNotifica.CAMPO_SEDE_TDS));

			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModTDS.setCodTipoNotifica("TS");
			lNotModTDS.setDataInvio(lDataTrasmissione);
			lNotModTDS.setUffCodUfficio(lTDS);
			lNotifiche.add(lNotModTDS);

		}

		// AVVOCATI
		int lIndex = 0;
		if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)) {
			String[] lAvvocati = this.getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
			for (lIndex = 0; lIndex < lAvvocati.length; lIndex++) {
				NotificaModel lNotAvv = new NotificaModel();

				lNotAvv.setCodTipoNotifica("N");
				lNotAvv.setDataInvio(lDataTrasmissione);
				lNotAvv.setCodEsito("-");
				lNotAvv.setCodOperatoreInserimento(lCodiceOperatore);
				lNotAvv.setDataInserimento(DateUtils.getSysDate());
				lNotAvv.setCodUfficioInserimento(lCodiceUfficio);
				lNotAvv.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndex]));

				if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)) {
					String[] lTipoAutoritaEsternaAvvocato = this
							.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
					String[] lSedeAutoritaEsternaAvvocato = this
							.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);

					String[] lNoteAvvocato = this.getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);
					lNotAvv.setNote(lNoteAvvocato[lIndex]);

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
					lAut.setCodOperatoreInserimento(lCodiceOperatore);
					lAut.setCodUfficioInserimento(lCodiceUfficio);
					lAut.setDataInserimento(DateUtils.getSysDate());
					lNotAvv.setAutoritaEsterna(lAut);
				}
				lNotifiche.add(lNotAvv);
			}
		}
		return (NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]);
	}

}