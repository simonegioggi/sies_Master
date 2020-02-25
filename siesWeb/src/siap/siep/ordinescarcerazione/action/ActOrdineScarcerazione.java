package siap.siep.ordinescarcerazione.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.altracausa.action.ICostantiAltraCausa;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.misuraalternativa.action.ActMisuraAlternativa;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

/**
 * <p>
 * Title: ActOrdineScarcerazione
 * </p>
 * <p>
 * Description: Ordine Scarcerazione
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
public class ActOrdineScarcerazione extends ActMisuraAlternativa implements ICostantiOrdineScarcerazione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Imposta tutti gli attributi dell'Evento
	 * 
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 *             protected EventoModel setEventoOrdineScarcerazione(EventoModel aEvento) throws F3BException
	 *             { EventoModel lEve = new EventoModel(aEvento); lEve.setCodTipoEvento("01"); //Tipo Evento =
	 *             PROVVEDIMENTO lEve.setCodTipoProvvedimento("09"); //Tipo Provvedimento = ORDINE ESECUZIONE
	 *             lEve.setFlagStampaSiep("S"); lEve.setFlagVideoSiep("S"); //--- IDecodifiche lDec =
	 *             SICOLookupRemote.getDecodificheRemote(); //--- DecodificheModel lDecMod =
	 *             lDec.ExRicercaDecodificheByHighValue(aEvento.getDescrMotivo());
	 *             lEve.setCodMotivo(aEvento.getCodEsito()); FascicoloSiepModel lFascicoloModel =
	 *             (FascicoloSiepModel) getSessionAttribute("fascicolo");
	 *             lEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep()); Date lDataEmissione =
	 *             getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
	 *             ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
	 *             lEve.setDataEmissione(lDataEmissione); Date lTrasmissione = lDataEmissione; if
	 *             (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO) ) lTrasmissione =
	 *             getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
	 *             ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
	 *             lEve.setDataTrasmissioneAtti(lTrasmissione); // UtenteModel lUtenteMod = new
	 *             UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
	 *             String lCodiceOperatore = this.getCodUtenteConnesso(); String lCodiceUfficio =
	 *             this.getCodUfficioUtenteConnesso(); lEve.setCodOperatoreInserimento(lCodiceOperatore);
	 *             lEve.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
	 *             lEve.setCodUfficioEmittente(lCodiceUfficio);
	 *             lEve.setDataInserimento(DateUtils.getSysDate());
	 *             lEve.setCodUfficioInserimento(lCodiceUfficio);
	 *             lEve.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
	 *             lEve.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
	 *             lEve.setCodOperatoreAggiornamento(lCodiceOperatore);
	 *             lEve.setCodUfficioAggiornamento(lCodiceUfficio);
	 *             lEve.setDataAggiornamento(DateUtils.getSysDate()); lEve.setCodEsito("-");
	 *             //lEve.setCodMotivo("-"); lEve.setCodLuogoDestinatario("-");
	 *             lEve.setCodTipoUfficioDestinatario("-"); lEve.setCodMagistrato(this.calcolaMagistrato());
	 *             return lEve; }
	 */

	/**
	 * Imposta tutte le notifiche associate all'evento prodotto
	 * 
	 * @param lKey
	 * @return
	 * @throws F3BException
	 */
	protected NotificaModel[] setNotificheOrdineScarcerazione() throws F3BException {

		// Avvocati
		// AvvocatoModel lAvvMod = new AvvocatoModel() ;
		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		Date lTrasmissione = lDataEmissione;

		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO))
			lTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
					ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		String[] lArrayDestinatari = this
				.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
		String[] lArraySedeDestinatari = this
				.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
		String[] lArrayNote = this.getRequestStringParameters(ICostantiNotifica.CAMPO_NOTE);
		String[] lAvvocati = this.getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);

		// modifica relativa al tipo istituto
		String lSedeDestinatario_E = null;
		String lDestinatario_E = null;

		String lSedeDestinatario_C = null;
		String lDestinatario_C = null;
		String lNote_C = null;

		String lDestinatario_EAE = null;
		String lNote_E = null;

		String lDestinatario_F = null;
		String lNote_F = null;
		String lSedeDestinatario_F = null;

		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_FUNGI)) {
			lDestinatario_F = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_FUNGI);
			lSedeDestinatario_F = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_FUNGI);
		}

		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_FUNGI)) {
			lNote_F = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_FUNGI);
		}

		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)) {
			lDestinatario_EAE = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			lSedeDestinatario_E = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
		}

		if (!this.isRequestParameterNullObj(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
			lDestinatario_E = this
					.getRequestStringParameter(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
		}

		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
			lDestinatario_E = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

		}

		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E)) {
			lNote_E = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);
		}

		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)) {
			lDestinatario_C = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C);
			lSedeDestinatario_C = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C);
		}

		if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_C)) {
			lNote_C = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_C);
		}

		// fine modifica relativa al tipo istituto

		for (int i = 0; i < lArraySedeDestinatari.length; i++) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lArraySEDEDestinatari PRIMA@@@@@@@@@@@@@@@" + lArraySedeDestinatari[i]);
		}
		for (int y = 0; y < lArrayDestinatari.length; y++) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lArrayDestinatari PRIMA@@@@@@@@@@@@@@@" + lArrayDestinatari[y]);
		}

		// modifica relativa al tipo istituto
		int lIndMisura = 0;

		// Dimensione dell'Array di Notifiche...
		// Gli Avvocati più Una Notifica di Esecuzione
		// Il caso dell'inserimento del Foglio Complementare!

		// modifica relativa al tipo istituto
		// int lNumNotifiche = lAvvocati.length + 1;
		int lNumNotifiche = lAvvocati.length;

		// L'autorità di Notifica non c'e'
		// modifica relativa al tipo istituto
		// if (lArrayDestinatari[1].compareTo("-") == 0)
		if (lArrayDestinatari[0].compareTo("-") == 0)
			lNumNotifiche -= 1;
		// modifica relativa al tipo istituto
		NotificaModel lNotifiche[] = new NotificaModel[lNumNotifiche];

		if (lDestinatario_C != null && lDestinatario_F != null)
			lNotifiche = new NotificaModel[lNumNotifiche + 3];
		else if ((lDestinatario_C == null && lDestinatario_F != null)
				|| (lDestinatario_C != null && lDestinatario_F == null))
			lNotifiche = new NotificaModel[lNumNotifiche + 2];
		else
			lNotifiche = new NotificaModel[lNumNotifiche + 1];

		// Per ora se non hanno selezionato le Autorità io inserisco comunque la notifica
		// while (lIndMisura < lArrayDestinatari.length)

		// modifica relativa al tipo istituto
		// if(lDestinatario_E != null)
		// {
		NotificaModel lNotMod = new NotificaModel();
		lNotMod.setCodTipoNotifica("E");
		lNotMod.setDataInvio(lTrasmissione);
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setNote(lNote_E);

		if (lDestinatario_E != null) {
			lNotMod.setIstDetIdIstitutoDetenzione(lDestinatario_E);
		}
		if (lDestinatario_EAE != null) {
			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

			lAutMod.setCodTipoAutorita(lDestinatario_EAE);
			ComuneModel lComModel = new ComuneModel(getCodComuneByDescr(lSedeDestinatario_E));
			lAutMod.setCodSede(lComModel.getCodComune());
			lAutMod.setDescrSede(lSedeDestinatario_E);
			lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
			lAutMod.setCodUfficioInserimento(lCodiceUfficio);
			lAutMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setIstDetIdIstitutoDetenzione("");

			lNotMod.setAutoritaEsterna(lAutMod);
		}

		lNotifiche[0] = lNotMod;
		// }

		// fine modifica relativa al tipo istituto

		while (lIndMisura < lNumNotifiche) {
			NotificaModel lNot = new NotificaModel();

			lNot.setCodTipoNotifica("N");

			lNot.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndMisura]));

			lNot.setNote(lArrayNote[lIndMisura]);
			// lNot.setDataInvio(lDataEmissione);
			lNot.setDataInvio(lTrasmissione);
			lNot.setCodEsito("-");
			lNot.setCodOperatoreInserimento(lCodiceOperatore);
			lNot.setDataInserimento(DateUtils.getSysDate());
			lNot.setCodUfficioInserimento(lCodiceUfficio);

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();

			// getCodUfficioByCodTipoUfficioDescrComune(lArrayDestinatari[lIndMisura],lArraySedeDestinatari[lIndMisura]);

			lAut.setCodTipoAutorita(lArrayDestinatari[lIndMisura]);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lArraySedeDestinatari[lIndMisura]));

			lAut.setCodSede(lComMod.getCodComune());
			lAut.setDescrSede(lArraySedeDestinatari[lIndMisura]);
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());

			// Setto l'Autorita Esterna per la notifica corrente
			lNot.setAutoritaEsterna(lAut);

			lNotifiche[lIndMisura + 1] = lNot;
			lIndMisura++;
		}

		if (lDestinatario_C != null) {
			NotificaModel lNotModC = new NotificaModel();
			lNotModC.setCodTipoNotifica("C");
			lNotModC.setDataInvio(lTrasmissione);
			lNotModC.setCodEsito("-");
			lNotModC.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModC.setDataInserimento(DateUtils.getSysDate());
			lNotModC.setCodUfficioInserimento(lCodiceUfficio);
			lNotModC.setNote(lNote_C);

			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

			lAutMod.setCodTipoAutorita(lDestinatario_C);
			ComuneModel lComModel = new ComuneModel(getCodComuneByDescr(lSedeDestinatario_C));
			lAutMod.setCodSede(lComModel.getCodComune());
			lAutMod.setDescrSede(lSedeDestinatario_C);
			lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
			lAutMod.setCodUfficioInserimento(lCodiceUfficio);
			lAutMod.setDataInserimento(DateUtils.getSysDate());
			lNotModC.setIstDetIdIstitutoDetenzione("");

			lNotModC.setAutoritaEsterna(lAutMod);
			lNotifiche[lIndMisura + 1] = lNotModC;
		}

		if (lDestinatario_F != null) {
			NotificaModel lNotModC = new NotificaModel();
			lNotModC.setCodTipoNotifica("C");
			lNotModC.setDataInvio(lTrasmissione);
			lNotModC.setCodEsito("-");
			lNotModC.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModC.setDataInserimento(DateUtils.getSysDate());
			lNotModC.setCodUfficioInserimento(lCodiceUfficio);
			lNotModC.setNote(lNote_F);

			AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

			lAutMod.setCodTipoAutorita(lDestinatario_F);
			ComuneModel lComModel = new ComuneModel(getCodComuneByDescr(lSedeDestinatario_F));
			lAutMod.setCodSede(lComModel.getCodComune());
			lAutMod.setDescrSede(lSedeDestinatario_F);
			lAutMod.setCodOperatoreInserimento(lCodiceOperatore);
			lAutMod.setCodUfficioInserimento(lCodiceUfficio);
			lAutMod.setDataInserimento(DateUtils.getSysDate());
			lNotModC.setIstDetIdIstitutoDetenzione("");

			lNotModC.setAutoritaEsterna(lAutMod);

			if (lDestinatario_C != null)
				lNotifiche[lIndMisura + 2] = lNotModC;
			else
				lNotifiche[lIndMisura + 1] = lNotModC;
		}

		// String lPosGiurid = "00";
		// if (!this.isRequestParameterNullObj(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA))
		// lPosGiurid = getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);

		return lNotifiche;
	}

	/**
	 *
	 * @return
	 * @throws F3BException
	 */
	protected String calcolaMagistrato() throws F3BException {

		String lCodiceMagistrato = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);

		if (lCodiceMagistrato.compareTo("") == 0) {
			MagistratoModel lMagMod = new MagistratoModel();
			lMagMod.setCognome(getRequestStringParameter(ICostantiMagistrato.CAMPO_COGNOME));
			lMagMod.setNome(getRequestStringParameter(ICostantiMagistrato.CAMPO_NOME));

			IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
			Vector lVect = new Vector();
			try {
				lVect = lCtrl.ExRicercaMagistrato(lMagMod);
			} catch (F3BException exF3b) {
				throw new F3BException(F3BException.USER_MESSAGE, "Magistrato Inesistente");
			}

			lMagMod = (MagistratoModel) lVect.firstElement();
			lCodiceMagistrato = lMagMod.getCodMagistrato();
		}

		return lCodiceMagistrato;
	}

	protected DepositoOrdinanzaPcModel getDepositoOrdinanza(EventoModel aEvento) throws F3BException {

		DepositoOrdinanzaPcModel lDepOrdMod = new DepositoOrdinanzaPcModel();
		lDepOrdMod.setAnnoS3(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO));
		lDepOrdMod.setNumS3(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO));
		lDepOrdMod.setDataUdienza(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		// lDepOrdMod.setIdCssaComp(getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA));
		lDepOrdMod.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());
		lDepOrdMod.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
		lDepOrdMod.setDataInserimento(DateUtils.getSysDate());
		String lCodiceUffEmitt = getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE),
				getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS));
		lDepOrdMod.setCodUfficioMagistratoComp(lCodiceUffEmitt);
		lDepOrdMod.setLuogoSvolgimentoProva(
				getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));

		return lDepOrdMod;
	}

	protected TenoreModel[] getTenoreModel(EventoModel aEvento) throws F3BException {

		TenoreModel lTenMod = new TenoreModel();
		Vector lTenori = new Vector();

		lTenMod.setCodEsitoTenore("0001");
		lTenMod.setData(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		lTenMod.setCodOggettoTenore("0081");
		lTenMod.setProgrTenore(new BigDecimal(1));
		lTenMod.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());
		lTenMod.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
		lTenMod.setDataInserimento(DateUtils.getSysDate());

		lTenori.add(lTenMod);

		return (TenoreModel[]) lTenori.toArray(new TenoreModel[0]);
	}

	protected MisuraAlternativaModel getMisuraAlternativaModel(EventoModel aEvento) throws F3BException {

		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();

		lMisMod.setCodTipoDecisione("03");
		lMisMod.setCodNaturaDecisione("CO");
		lMisMod.setCodTipoMisura("0081");
		lMisMod.setDataDecisione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		lMisMod.setDescrLuogoProva(
				getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));
		lMisMod.setAnnoRegistro(
				getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO));
		lMisMod.setNumeroRegistro(
				getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO));
		lMisMod.setChiaveProgrFascicoloSius(
				getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS));
		lMisMod.setChiaveAnnoFascicoloSius(
				getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS));
		lMisMod.setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
		lMisMod.setNumGiorniMisura(
				getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA));
		lMisMod.setDataInizioMisura(DateUtils.getSysDate());
		lMisMod.setCodUfficioInserimento(aEvento.getCodUfficioInserimento());
		lMisMod.setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
		lMisMod.setDataInserimento(DateUtils.getSysDate());
		lMisMod.setCodTipoUfficioScarcerazione("-");

		lMisMod.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

		return lMisMod;

	}

	protected EventoNotificaModel getEventoMASimulata(EventoModel aEvento) throws F3BException {

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod.getEvento()
				.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		lEveMod.getEvento()
				.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		lEveMod.getEvento().setCodTipoEvento("01");
		lEveMod.getEvento().setCodTipoProvvedimento("03");
		lEveMod.getEvento().setCodEsito("-");
		lEveMod.getEvento().setFlagStampaSiep("S");
		lEveMod.getEvento().setFlagVideoSiep("S");
		lEveMod.getEvento().setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
		lEveMod.getEvento().setCodMotivo("0081");
		lEveMod.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodOperatoreInserimento(aEvento.getCodOperatoreInserimento());
		lEveMod.getEvento().setCodUfficioInserimento(aEvento.getCodUfficioInserimento());
		lEveMod.getEvento().setCodLuogoDestinatario("-");
		lEveMod.getEvento().setCodTipoUfficioDestinatario("-");
		lEveMod.getEvento()
				.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		// lEveMod.setNotifiche(lNotifiche);
		return lEveMod;
	}

	protected EventoNotificaModel getEventoOrdineScarcerazione(EventoModel aEvento) throws F3BException {

		EventoNotificaModel lEveModNew = new EventoNotificaModel();

		lEveModNew.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lEveModNew.getEvento().setCodMotivo(aEvento.getCodMotivo());
		lEveModNew.getEvento().setCodTipoEvento("01");
		lEveModNew.getEvento().setCodTipoProvvedimento("09");
		lEveModNew.getEvento().setCodEsito("-");
		lEveModNew.getEvento().setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lEveModNew.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEveModNew.getEvento().setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lEveModNew.getEvento().setCodLuogoDestinatario("-");
		lEveModNew.getEvento().setCodTipoUfficioDestinatario("-");
		lEveModNew.getEvento()
				.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		lEveModNew.getEvento()
				.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		// lEveModNew.getEvento().setFasSieIdFascicoloSiep(aEvento.getFasSieIdFascicoloSiep());
		lEveModNew.getEvento()
				.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lEveModNew.getEvento().setFlagStampaSiep("S");
		lEveModNew.getEvento().setFlagVideoSiep("S");
		lEveModNew.getEvento().setFlagPiuMeno("-");
		// ---- lEveModNew.getEvento().setFlagDocumentoRegistrato("N");

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		lEveModNew.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEveModNew.getEvento().setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEveModNew.getEvento().setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());
		lEveModNew.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		return lEveModNew;
	}

	protected String controllaFascicolo() throws F3BException {

		String lNomeClasse = getClass().getName();
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + lNomeClasse;
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " non è stato Validato. Impossibile inserire l'Ordine di Scarcerazione!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + lNomeClasse);
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}
		// Controllo se il fascicolo è ARCHIVIATO o DEFINITO
		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " Il fascicolo risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + lNomeClasse);
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		this.isFascicoloSiepDiCompetenza();

		return null;
	}

	protected void controllaPenaResiduaAvvocato(BigDecimal lKeyFascicolo) throws F3BException {

		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lKeyFascicolo);

		if (lPenaResMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE, "Eseguire prima il calcolo della pena. "
					+ "Impossibile eseguire l'Ordine di Scarcerazione.");

		// Controllo esistenza almeno un avvocato per fascicolo.
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		try {
			/* Vector lAvvVect = */lAvv.ExRicercaAvvocatiByFascicolo(lKeyFascicolo);
		} catch (SIEPException e) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					e.getMessage() + " Impossibile eseguire l'Ordine di Scarcerazione.");
		}
	}

	/**
	 * Imposta tutte le notifiche associate all'evento prodotto
	 * 
	 * @param lKey
	 * @return
	 * @throws F3BException
	 */
	protected NotificaModel[] setNotificheOS() throws F3BException {

		// Avvocati
		// AvvocatoModel lAvvMod = new AvvocatoModel() ;
		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		// String posizionegiuridica = getRequestStringParameter("posizionegiuridica");
		// String[] lAvvocati = this.getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
		ArrayList lNotifiche = new ArrayList();

		AutoritaEsternaModel lAut = new AutoritaEsternaModel();

		// SETTO CSSA
		if (!this.isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA)) {

			NotificaModel lNotModCSSA = new NotificaModel();
			BigDecimal lCssa = this.getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA);

			// String lSedeCssa = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_CSSA);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA)) {
				String lNoteCssa = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA);
				lNotModCSSA.setNote(lNoteCssa);
			}
			lNotModCSSA.setCodEsito("-");
			lNotModCSSA.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModCSSA.setDataInserimento(DateUtils.getSysDate());
			lNotModCSSA.setCodUfficioInserimento(lCodiceUfficio);
			if ((this.getRequestStringParameter("posizionegiuridica").equals("07")
					|| this.getRequestStringParameter("posizionegiuridica").equals("10")
							&& (!this.isRequestParameterNullObj("verbale")
									&& this.getRequestStringParameter("verbale").equals("N")))
					&& (isRequestParameterNullObj("cssaDetenuto"))) {
				lNotModCSSA.setCodTipoNotifica("E");
			} else {
				lNotModCSSA.setCodTipoNotifica("C");
			}
			lNotModCSSA.setDataInvio(lDataEmissione);
			lNotModCSSA.setCssIdCssa(lCssa);
			lNotifiche.add(lNotModCSSA);

		}
		// SETTO UDS
		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS)) {

			NotificaModel lNotModTDS = new NotificaModel();
			String lUDS = this.getCodUfficioByCodTipoUfficioDescrComune("UDS",
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS));
			// String lSedeMagistrato =
			// this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_MAG);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_UDS)) {
				String lNoteUDS = this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_UDS);
				lNotModTDS.setNote(lNoteUDS);
			}
			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModTDS.setCodTipoNotifica("C");
			lNotModTDS.setDataInvio(lDataEmissione);
			lNotModTDS.setUffCodUfficio(lUDS);
			lNotifiche.add(lNotModTDS);

		}
		// SETTO TDS

		if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE)) {

			NotificaModel lNotModTDS = new NotificaModel();
			// String lTribunale = this
			// .getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE);
			// String lSedeTribunale =
			// this.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_TDS)) {
				String lNoteTribunale = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_TDS);
				lNotModTDS.setNote(lNoteTribunale);
			}
			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModTDS.setCodTipoNotifica("C");
			lNotModTDS.setDataInvio(lDataEmissione);
			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS));
			lNotModTDS.setUffCodUfficio(lCodiceUff);

			lNotifiche.add(lNotModTDS);

		}

		// SETTO ISTITUTO DETENZIONE
		if ((!this
				.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))) {

			NotificaModel lNotModIst = new NotificaModel();
			String lIstituto = this
					.getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_IST)) {
				String lNoteIstituto = this
						.getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_IST);
				lNotModIst.setNote(lNoteIstituto);
			}
			lNotModIst.setCodEsito("-");
			lNotModIst.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModIst.setDataInserimento(DateUtils.getSysDate());
			lNotModIst.setCodUfficioInserimento(lCodiceUfficio);
			lNotModIst.setCodTipoNotifica("E");
			lNotModIst.setDataInvio(lDataEmissione);
			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);
			lNotifiche.add(lNotModIst);
		}

		// SETTO AUTORITA ESTERNA PER OS
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)) {
			String lTipoAutoritaEsternaE = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			String lSedeAutoritaEsternaE = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
			String lSedeNoteE = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);

			NotificaModel lNotModPolE = new NotificaModel();

			lNotModPolE.setNote(lSedeNoteE);
			lNotModPolE.setCodEsito("-");
			lNotModPolE.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPolE.setDataInserimento(DateUtils.getSysDate());
			lNotModPolE.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPolE.setCodTipoNotifica("E");
			lNotModPolE.setDataInvio(lDataEmissione);

			lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lTipoAutoritaEsternaE);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedeAutoritaEsternaE));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setDescrSede(lSedeAutoritaEsternaE);
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPolE.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPolE);
		}

		// SETTO PARAMETRI DELLA MASCHERA CON FUNGIBILITA
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_FUNGI)) {
			String lDestinatario_F = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_FUNGI);
			String lSedeDestinatario_F = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_FUNGI);
			String lNote_F = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_FUNGI);

			NotificaModel lNotModFungi = new NotificaModel();

			lNotModFungi.setNote(lNote_F);
			lNotModFungi.setCodEsito("-");
			lNotModFungi.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModFungi.setDataInserimento(DateUtils.getSysDate());
			lNotModFungi.setCodUfficioInserimento(lCodiceUfficio);
			lNotModFungi.setCodTipoNotifica("E");
			lNotModFungi.setDataInvio(lDataEmissione);

			lAut = new AutoritaEsternaModel();

			lAut.setCodTipoAutorita(lDestinatario_F);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedeDestinatario_F));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModFungi.setAutoritaEsterna(lAut);
			lNotifiche.add(lNotModFungi);
		}
		// AVVOCATI
		/*
		 * int lIndex = 0;
		 * 
		 * for (lIndex = 0; lIndex < lAvvocati.length; lIndex++) { NotificaModel lNotAvv = new
		 * NotificaModel();
		 * 
		 * lNotAvv.setCodTipoNotifica("N"); //lNot.setNote(lArrayNote[lIndMisura]);
		 * lNotAvv.setDataInvio(lDataEmissione); lNotAvv.setCodEsito("-");
		 * lNotAvv.setCodOperatoreInserimento(lCodiceOperatore);
		 * lNotAvv.setDataInserimento(DateUtils.getSysDate());
		 * lNotAvv.setCodUfficioInserimento(lCodiceUfficio); lNotAvv.setAvvIdAvvocatoFascicoloSiep(new
		 * BigDecimal(lAvvocati[lIndex]));
		 * 
		 * if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna. CAMPO_COD_TIPO_AUTORITA)) { String[]
		 * lTipoAutoritaEsternaAvvocato = this.getRequestStringParameters(
		 * ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA); String[] lSedeAutoritaEsternaAvvocato =
		 * this.getRequestStringParameters( ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
		 * 
		 * String[] lNoteAvvocato =
		 * this.getRequestStringParameters(ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI);
		 * lNotAvv.setNote(lNoteAvvocato[lIndex]); //String lNoteAvvocato =
		 * this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE); //lNotAvv.setNote(lNoteAvvocato);
		 * 
		 * lAut = new AutoritaEsternaModel(); lAut.setCodTipoAutorita(lTipoAutoritaEsternaAvvocato[lIndex]);
		 * 
		 * ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedeAutoritaEsternaAvvocato[lIndex]));
		 * lAut.setCodSede(lComMod.getCodComune()); lAut.setCodOperatoreInserimento(lCodiceOperatore);
		 * lAut.setCodUfficioInserimento(lCodiceUfficio); lAut.setDataInserimento(DateUtils.getSysDate());
		 * lNotAvv.setAutoritaEsterna(lAut); } lNotifiche.add(lNotAvv); }
		 */
		return (NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]);
	}

}