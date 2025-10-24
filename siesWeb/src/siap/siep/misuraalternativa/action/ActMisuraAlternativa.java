package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sospensione.action.ICostantiSospensione;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

/**
 * ActMisuraAlternativa - Azione Generalizzata di tutte le Action delle Misure Alternative
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActMisuraAlternativa extends ActSIESDettaglioProvvedimento
		implements ICostantiMisuraAlternativa {

	/**
	 * Imposta tutti gli attributi dell'Evento
	 *
	 * @param aEvento
	 * @return
	 * @throws F3BException
	 */
	protected EventoModel setEventoProvvedimentoMisuraAlternativa(EventoModel aEvento) throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		EventoModel lEve = new EventoModel(aEvento);
		lEve.setCodTipoEvento("01");
		lEve.setFlagStampaSiep("S");
		lEve.setFlagVideoSiep("S");
		lEve.setCodUfficioEmittente(lCodiceUfficio);
		lEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.setDataEmissione(lDataEmissione);
		lEve.setCodOperatoreInserimento(lCodiceOperatore);
		lEve.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEve.setDataInserimento(DateUtils.getSysDate());
		lEve.setCodUfficioInserimento(lCodiceUfficio);
		lEve.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEve.setFlagDocumentoRegistrato(null);
		if (aEvento.getCodEsito() != null)
			lEve.setCodEsito(aEvento.getCodEsito());
		else
			lEve.setCodEsito("-");

		lEve.setCodLuogoDestinatario("-");
		lEve.setCodTipoUfficioDestinatario("-");
		lEve.setCodMagistrato(calcolaMagistrato());

		return lEve;
	}

	/**
	 * setta Evento Ordinaza Decreto MisuraAlternativa
	 *
	 * @param aEvento
	 * @param aTipoprovv
	 * @param aCodiceUffEmi
	 * @param aComMod
	 * @param aDataEmiTra
	 * @return EventoModel
	 * @throws F3BException
	 */
	protected EventoModel setEventoOrdinanzaDecretoMisuraAlternativa(EventoModel aEvento, String aTipoprovv,
			String aCodiceUffEmi, ComuneModel aComMod, Date aDataEmiTra) throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		EventoModel lEve = new EventoModel(aEvento);
		lEve.setCodTipoEvento("01");
		lEve.setCodTipoProvvedimento(aTipoprovv);
		lEve.setFlagStampaSiep("S");
		lEve.setFlagVideoSiep("S");
		lEve.setCodUfficioEmittente(aCodiceUffEmi);
		lEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lEve.setDataEmissione(aDataEmiTra);
		lEve.setDataTrasmissioneAtti(aDataEmiTra);
		lEve.setCodOperatoreInserimento(lCodiceOperatore);
		lEve.setCodLuogoEmittente(aComMod.getCodComune());
		lEve.setDataInserimento(DateUtils.getSysDate());
		lEve.setCodUfficioInserimento(lCodiceUfficio);
		lEve.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEve.setFlagDocumentoRegistrato("N");
		if (aEvento.getCodEsito() != null)
			lEve.setCodEsito(aEvento.getCodEsito());
		else
			lEve.setCodEsito("-");

		lEve.setCodLuogoDestinatario("-");
		lEve.setCodTipoUfficioDestinatario("-");

		return lEve;
	}

	/**
	 * Imposta tutte le notifiche associate all'evento prodotto
	 *
	 * @param
	 * @return NotificaModel[]
	 * @throws F3BException
	 */
	protected NotificaModel[] setNotificheMisuraAlternativa() throws F3BException {

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		String posizionegiuridica = getRequestStringParameter("posizionegiuridica");

		ArrayList lNotifiche = new ArrayList();

		// SETTO NOTIFICA AUTORITA E
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E)
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E) != null
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E).equals("-")) {
			String lPolizia = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E);
			String lSedePolizia = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E);
			NotificaModel lNotModPol = new NotificaModel();
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E)) {
				String lNotePolizia = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E);
				lNotModPol.setNote(lNotePolizia);
			}

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("E");
			lNotModPol.setDataInvio(lDataEmissione);
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();

			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPol);
		}

		// SETTO ISTITUTO DETENZIONE
		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
				&& getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE) != null
				&& !getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
						.equals("")) {

			NotificaModel lNotModIst = new NotificaModel();
			String lIstituto = getRequestStringParameter(
					ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_IST)) {
				String lNoteIstituto = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_IST);
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

		// SETTO UDS ad E
		if (!isRequestParameterNullObj("UDSE") && getRequestStringParameter("UDSE") != null
				&& getRequestStringParameter("UDSE").equals("S")) {
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS)
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS) != null
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS).equals("")) {
				NotificaModel lNotModTDS = new NotificaModel();

				String lTipoUfficio = "UDS";
				if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO)
						&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO) != null
						&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO)
								.equals("")) {
					lTipoUfficio = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO);
				}
				String lSedeUfficio = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS);
				String lUDS = getCodUfficioByCodTipoUfficioDescrComune(lTipoUfficio, lSedeUfficio);

				if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_UDS)) {
					String lNoteUDS = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_UDS);
					lNotModTDS.setNote(lNoteUDS);
				}
				lNotModTDS.setCodEsito("-");
				lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
				lNotModTDS.setDataInserimento(DateUtils.getSysDate());
				lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
				lNotModTDS.setCodTipoNotifica("E");
				lNotModTDS.setDataInvio(lDataEmissione);
				lNotModTDS.setUffCodUfficio(lUDS);
				lNotifiche.add(lNotModTDS);
			}
		}

		// SETTO NOTIFICA AUTORITA C solo per ripristino semilibertà
		if (!isRequestParameterNullObj("notificaSemiliberta")
				&& getRequestStringParameter("notificaSemiliberta").equals("S")) {
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C)
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C) != null
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C).equals("-"))

			{
				String lPolizia = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C);
				String lSedePolizia = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C);
				NotificaModel lNotModPol = new NotificaModel();
				if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C)) {
					String lNotePolizia = getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C);
					lNotModPol.setNote(lNotePolizia);
				}
				lNotModPol.setCodEsito("-");
				lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
				lNotModPol.setDataInserimento(DateUtils.getSysDate());
				lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
				lNotModPol.setCodTipoNotifica("C");
				lNotModPol.setDataInvio(lDataEmissione);
				AutoritaEsternaModel lAut = new AutoritaEsternaModel();
				lAut.setCodTipoAutorita(lPolizia);

				ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedePolizia));
				lAut.setCodSede(lComMod.getCodComune());
				lAut.setCodOperatoreInserimento(lCodiceOperatore);
				lAut.setCodUfficioInserimento(lCodiceUfficio);
				lAut.setDataInserimento(DateUtils.getSysDate());
				lNotModPol.setAutoritaEsterna(lAut);

				lNotifiche.add(lNotModPol);
			}
		}

		// SETTO CSSA
		if (!isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA)
				&& getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA) != null
				&& getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA)
						.compareTo(new BigDecimal(0)) != 0
				&& !getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA).toString().equals("-")) {
			NotificaModel lNotModCSSA = new NotificaModel();
			BigDecimal lCssa = getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA);

			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA)) {
				String lNoteCssa = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA);
				lNotModCSSA.setNote(lNoteCssa);
			}
			lNotModCSSA.setCodEsito("-");
			lNotModCSSA.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModCSSA.setDataInserimento(DateUtils.getSysDate());
			lNotModCSSA.setCodUfficioInserimento(lCodiceUfficio);
			if (isRequestParameterNullObj("cssaDetenuto") && !isRequestParameterNullObj("cssaE")
					&& getRequestStringParameter("cssaE") != null
					&& getRequestStringParameter("cssaE").equals("S")) {
				lNotModCSSA.setCodTipoNotifica("E");

			} else {
				lNotModCSSA.setCodTipoNotifica("C");
			}
			lNotModCSSA.setDataInvio(lDataEmissione);
			lNotModCSSA.setCssIdCssa(lCssa);
			lNotifiche.add(lNotModCSSA);

		}

		// SETTO UDS
		if (isRequestParameterNullObj("UDSE")) {
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS)
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS) != null
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS).equals("")) {
				NotificaModel lNotModTDS = new NotificaModel();

				String lTipoUfficio = "UDS";
				if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO)
						&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO) != null
						&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO)
								.equals("")) {
					lTipoUfficio = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO);
				}
				String lSedeUfficio = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS);
				String lUDS = getCodUfficioByCodTipoUfficioDescrComune(lTipoUfficio, lSedeUfficio);

				if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_UDS)) {
					String lNoteUDS = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_UDS);
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
		}

		// SETTO TDS
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE)
				&& !isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS)
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS) != null
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS).equals("")) {

			NotificaModel lNotModTDS = new NotificaModel();
			String lTribunale = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE);
			String lSedeTribunale = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS);
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_TDS)) {
				String lNoteTribunale = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_TDS);
				lNotModTDS.setNote(lNoteTribunale);
			}
			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModTDS.setCodTipoNotifica("C");
			lNotModTDS.setDataInvio(lDataEmissione);
			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(lTribunale, lSedeTribunale);
			lNotModTDS.setUffCodUfficio(lCodiceUff);

			lNotifiche.add(lNotModTDS);
		}

		// SETTO NOTIFICA AUTORITA N
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_N)) {

			String lPolizia = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_N);
			String lSedePolizia = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_N);
			NotificaModel lNotModPol = new NotificaModel();
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_N)) {
				String lNotePolizia = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_N);
				lNotModPol.setNote(lNotePolizia);
			}

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("N");
			lNotModPol.setDataInvio(lDataEmissione);
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPol);
		}

		// SETTO NOTIFICA AUTORITA C
		if (isRequestParameterNullObj("notificaSemiliberta")) {
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C)
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C) != null
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C)
							.equals("-")) {

				String lPolizia = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C);
				String lSedePolizia = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C);
				NotificaModel lNotModPol = new NotificaModel();
				if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C)) {
					String lNotePolizia = getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C);
					lNotModPol.setNote(lNotePolizia);
				}
				lNotModPol.setCodEsito("-");
				lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
				lNotModPol.setDataInserimento(DateUtils.getSysDate());
				lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
				lNotModPol.setCodTipoNotifica("C");
				lNotModPol.setDataInvio(lDataEmissione);
				AutoritaEsternaModel lAut = new AutoritaEsternaModel();
				lAut.setCodTipoAutorita(lPolizia);

				ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedePolizia));
				lAut.setCodSede(lComMod.getCodComune());
				lAut.setCodOperatoreInserimento(lCodiceOperatore);
				lAut.setCodUfficioInserimento(lCodiceUfficio);
				lAut.setDataInserimento(DateUtils.getSysDate());
				lNotModPol.setAutoritaEsterna(lAut);

				lNotifiche.add(lNotModPol);

			}

		}

		// SETTO NOTIFICA AUTORITA preposta al controllo
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
				&& getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C) != null
				&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C)
						.equals("-")) {
			String lPolizia = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C);
			String lSedePolizia = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C);
			NotificaModel lNotModPol = new NotificaModel();
			if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)) {
				String lNotePolizia = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE);
				lNotModPol.setNote(lNotePolizia);
			}

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("PC");
			lNotModPol.setDataInvio(lDataEmissione);
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();

			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPol);
		}

		// SETTO AUTORITA ESTERNA PER OS
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E)) {
			if ((!posizionegiuridica.equals("03") || !posizionegiuridica.equals("14"))
					&& (!isRequestParameterNullObj(
							ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))) {
				NotificaModel lNotModIst = new NotificaModel();
				String lIstituto = getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
				if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_IST)) {
					String lNoteIstituto = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_IST);
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
			String lTipoAutoritaEsternaE = getRequestStringParameter(
					ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E);
			String lSedeAutoritaEsternaE = getRequestStringParameter(
					ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E);
			NotificaModel lNotModPolE = new NotificaModel();
			if (!isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE_E)) {
				String lSedeNoteE = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E);
				lNotModPolE.setNote(lSedeNoteE);
			}

			lNotModPolE.setCodEsito("-");
			lNotModPolE.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPolE.setDataInserimento(DateUtils.getSysDate());
			lNotModPolE.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPolE.setCodTipoNotifica("E");
			lNotModPolE.setDataInvio(lDataEmissione);
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lTipoAutoritaEsternaE);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedeAutoritaEsternaE));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setDescrSede(lSedeAutoritaEsternaE);
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPolE.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPolE);
		}

		// SETTO PARAMETRI DELLA MASCHERA CON FUNGIBILITA
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_FUNGI)) {
			String lDestinatario_F = getRequestStringParameter(
					ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_FUNGI);
			String lSedeDestinatario_F = getRequestStringParameter(
					ICostantiAutoritaEsterna.CAMPO_COD_SEDE_FUNGI);
			String lNote_F = getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_FUNGI);

			NotificaModel lNotModFungi = new NotificaModel();

			lNotModFungi.setNote(lNote_F);
			lNotModFungi.setCodEsito("-");
			lNotModFungi.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModFungi.setDataInserimento(DateUtils.getSysDate());
			lNotModFungi.setCodUfficioInserimento(lCodiceUfficio);
			lNotModFungi.setCodTipoNotifica("E");
			lNotModFungi.setDataInvio(lDataEmissione);
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
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

		// ==========================================================================
		// Destinatari per la Restituzione OE.
		// Aggiunto a seguito modifiche della 'Ammissione Provvisoria Affidamento in
		// Prova' DL 146 20/01/2014 d.f.
		// ==========================================================================
		if (!isRequestParameterNullObj(ICostantiSospensione.CAMPO_RESTITUZIONE_OE)) {
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R)
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R) != null
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R).equals("")
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R)
							.equals("-")) {

				NotificaModel lNotModPol = new NotificaModel();

				if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R)) {
					String lNotePolizia = getRequestStringParameter(
							ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R);
					lNotModPol.setNote(lNotePolizia);
				}

				lNotModPol.setCodEsito("-");
				lNotModPol.setCodTipoNotifica("R");
				lNotModPol.setDataInvio(
						getRequestDateParameter(ICostantiEvento.CAMPO_DATA_EMISSIONE, "dd-MM-yyyy"));

				lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
				lNotModPol.setDataInserimento(DateUtils.getSysDate());
				lNotModPol.setCodUfficioInserimento(lCodiceUfficio);

				AutoritaEsternaModel lAut = new AutoritaEsternaModel();
				String lPolizia = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R);
				String lSedePolizia = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_R);

				lAut.setCodTipoAutorita(lPolizia);

				ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
				lAut.setCodSede(lComMod.getCodComune());

				lAut.setCodOperatoreInserimento(lCodiceOperatore);
				lAut.setCodUfficioInserimento(lCodiceUfficio);
				lAut.setDataInserimento(DateUtils.getSysDate());

				lNotModPol.setAutoritaEsterna(lAut);

				lNotifiche.add(lNotModPol);
			}
		}

		//
		// ==========================================================================

		// AVVOCATI
		int lIndex = 0;
		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)) {
			String[] lAvvocati = getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
			for (lIndex = 0; lIndex < lAvvocati.length; lIndex++) {
				if ("".equals(lAvvocati[lIndex])) {
					// 23-01-2014 d.f. per gestire il caso di Avvocati presenti in maschera
					// ma in DIV a scomparsa. Se l'utente decide di non inviare la notifica,
					// sulla submit il campo ICostantiAvvocato.CAMPO_ID_AVVOCATO viene
					// 'sbiancato' e passa comunque come parameter ma vuoto ('').
					continue;
				}
				NotificaModel lNotAvv = new NotificaModel();

				lNotAvv.setCodTipoNotifica("N");
				lNotAvv.setDataInvio(lDataEmissione);
				lNotAvv.setCodEsito("-");
				lNotAvv.setCodOperatoreInserimento(lCodiceOperatore);
				lNotAvv.setDataInserimento(DateUtils.getSysDate());
				lNotAvv.setCodUfficioInserimento(lCodiceUfficio);
				lNotAvv.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(lAvvocati[lIndex]));

				if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)) {
					String[] lTipoAutoritaEsternaAvvocato = getRequestStringParameters(
							ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
					String[] lSedeAutoritaEsternaAvvocato = getRequestStringParameters(
							ICostantiAutoritaEsterna.CAMPO_COD_SEDE);

					String[] lNoteAvvocato = getRequestStringParameters(
							ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI);
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

	/**
	 * Imposta tutte le notifiche associate all'evento prodotto
	 *
	 * @param
	 * @return NotificaModel[]
	 * @throws F3BException
	 */
	protected NotificaModel[] setNotificheRipristinoArrestiDomiciliariMisuraAlternativa()
			throws F3BException {

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		// String posizionegiuridica = getRequestStringParameter("posizionegiuridica");

		ArrayList lNotifiche = new ArrayList();

		String lUfficioScarc = "-";
		if (getRequestStringParameter("tipo").equals("mds"))
			lUfficioScarc = "SORV"; // Eseguita da Magistrato di Sorveglianza
		else if (getRequestStringParameter("tipo").equals("procura"))
			lUfficioScarc = "PROC";// Esegue Procura

		NotificaModel lNotMod = new NotificaModel();

		// INIZIO NOTIFICA AUTORITA E
		if (lUfficioScarc.equalsIgnoreCase("PROC")) {
			lNotMod = new NotificaModel();
			// COD_TIPO_NOTIFICA
			lNotMod.setCodTipoNotifica("E");
			// AUT_EST_ID_AUTORITA_ESTERNA(Autorità competente per territorio)
			lNotMod.setCodEsito("-");
			lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(lCodiceUfficio);
			lNotMod.setDataInvio(lDataEmissione);
			lNotMod.setAutoritaEsterna(null);
			// AVV_ID_AVVOCATO_FASCICOLO_SIEP
			lNotMod.setAvvIdAvvocatoFascicoloSiep(null);
			// CSS_ID_CSSA
			lNotMod.setCssIdCssa(null);
			// IST_DET_ID_ISTITUTO_DETENZIONE
			if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
				String istituto = getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
				lNotMod.setIstDetIdIstitutoDetenzione(istituto);
			} else {
				lNotMod.setIstDetIdIstitutoDetenzione(null);
			}
			// UFF_COD_UFFICIO
			lNotMod.setUffCodUfficio(null);
			lNotMod.setNote(null);
			lNotifiche.add(lNotMod);
		} else if (lUfficioScarc.equalsIgnoreCase("SORV")) {
			lNotMod = new NotificaModel();
			// COD_TIPO_NOTIFICA
			lNotMod.setCodTipoNotifica("E");
			lNotMod.setCodEsito("-");
			lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(lCodiceUfficio);
			lNotMod.setDataInvio(lDataEmissione);
			// AUT_EST_ID_AUTORITA_ESTERNA(Autorità competente per territorio)
			AutoritaEsternaModel aut = new AutoritaEsternaModel();
			String tipoAutoritaEsternaE = "";
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E)) {
				tipoAutoritaEsternaE = getRequestStringParameter(
						ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E);
			} else {
				tipoAutoritaEsternaE = "-";
			}
			String sedeAutoritaEsternaE = "";
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_SEDE_E)) {
				sedeAutoritaEsternaE = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_SEDE_E);
			} else {
				sedeAutoritaEsternaE = "-";
			}
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E)) {
				String lSedeNoteE = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E);
				lNotMod.setNote(lSedeNoteE);// Indirizzo
			}
			aut = new AutoritaEsternaModel();
			aut.setCodTipoAutorita(tipoAutoritaEsternaE);
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(sedeAutoritaEsternaE));
			aut.setCodSede(lComMod.getCodComune());
			aut.setDescrSede(sedeAutoritaEsternaE);
			aut.setCodOperatoreInserimento(lCodiceOperatore);
			aut.setCodUfficioInserimento(lCodiceUfficio);
			aut.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setAutoritaEsterna(aut);
			// AVV_ID_AVVOCATO_FASCICOLO_SIEP
			lNotMod.setAvvIdAvvocatoFascicoloSiep(null);
			// CSS_ID_CSSA
			lNotMod.setCssIdCssa(null);
			// IST_DET_ID_ISTITUTO_DETENZIONE
			lNotMod.setIstDetIdIstitutoDetenzione(null);
			// UFF_COD_UFFICIO
			lNotMod.setUffCodUfficio(null);
			lNotifiche.add(lNotMod);
		}
		// FINE NOTIFICA AUTORITA E

		// INIZIO NOTIFICA AUTORITA I C
		lNotMod = new NotificaModel();
		// COD_TIPO_NOTIFICA
		lNotMod.setCodTipoNotifica("C");
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setDataInvio(lDataEmissione);
		lNotMod.setAutoritaEsterna(null);
		// AVV_ID_AVVOCATO_FASCICOLO_SIEP
		lNotMod.setAvvIdAvvocatoFascicoloSiep(null);
		// CSS_ID_CSSA(UEPE)
		BigDecimal lCssa = null;
		if (!isRequestParameterNullObj(ICostantiCSSA.CAMPO_ID_CSSA)) {
			lCssa = getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA);
		}
		lNotMod.setCssIdCssa(lCssa);
		// IST_DET_ID_ISTITUTO_DETENZIONE
		lNotMod.setIstDetIdIstitutoDetenzione(null);
		// UFF_COD_UFFICIO
		lNotMod.setUffCodUfficio(null);
		lNotMod.setNote(null);
		lNotifiche.add(lNotMod);
		// FINE NOTIFICA AUTORITA I C

		// INIZIO NOTIFICA AUTORITA II C
		lNotMod = new NotificaModel();
		// COD_TIPO_NOTIFICA
		lNotMod.setCodTipoNotifica("C");
		// AUT_EST_ID_AUTORITA_ESTERNA
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setDataInvio(lDataEmissione);
		lNotMod.setAutoritaEsterna(null);
		// AVV_ID_AVVOCATO_FASCICOLO_SIEP
		lNotMod.setAvvIdAvvocatoFascicoloSiep(null);
		// CSS_ID_CSSA
		lNotMod.setCssIdCssa(null);
		// IST_DET_ID_ISTITUTO_DETENZIONE
		lNotMod.setIstDetIdIstitutoDetenzione(null);
		// Magistrato di Sorveglianza
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS)
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS) != null
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS).equals("")) {
			// NotificaModel lNotModTDS = new NotificaModel();
			String lTipoUfficio = "UDS";
			// MEV10-s3: aggiunto controllo altrimenti invia sempre UDS
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO)
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO) != null
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO).equals("")) {
				lTipoUfficio = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO);
			}
			String lSedeUfficio = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS);
			String lUDS = getCodUfficioByCodTipoUfficioDescrComune(lTipoUfficio, lSedeUfficio);
			lNotMod.setUffCodUfficio(lUDS);
		}
		lNotMod.setNote(null);
		lNotifiche.add(lNotMod);
		// FINE NOTIFICA AUTORITA II C

		// INIZIO NOTIFICA AUTORITA III C
		lNotMod = new NotificaModel();
		// COD_TIPO_NOTIFICA
		lNotMod.setCodTipoNotifica("C");
		// AUT_EST_ID_AUTORITA_ESTERNA
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setDataInvio(lDataEmissione);
		lNotMod.setAutoritaEsterna(null);
		// AVV_ID_AVVOCATO_FASCICOLO_SIEP
		lNotMod.setAvvIdAvvocatoFascicoloSiep(null);
		// CSS_ID_CSSA
		lNotMod.setCssIdCssa(null);
		// IST_DET_ID_ISTITUTO_DETENZIONE
		lNotMod.setIstDetIdIstitutoDetenzione(null);
		// Tribunale di Sorveglianza
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE)
				&& !isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS)
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS) != null
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS).equals("")) {
			// NotificaModel lNotModTDS = new NotificaModel();
			String lTribunale = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE);
			String lSedeTribunale = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS);
			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(lTribunale, lSedeTribunale);
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE)) {
				String lSedeNoteE = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE);
				lNotMod.setNote(lSedeNoteE);
			}
			lNotMod.setUffCodUfficio(lCodiceUff);
		}
		lNotMod.setNote(null);
		lNotifiche.add(lNotMod);
		// FINE NOTIFICA AUTORITA III C

		// INIZIO NOTIFICA AUTORITA IV C
		if (lUfficioScarc.equalsIgnoreCase("PROC")) {
			lNotMod = new NotificaModel();
			// COD_TIPO_NOTIFICA
			lNotMod.setCodTipoNotifica("C");
			// AUT_EST_ID_AUTORITA_ESTERNA
			String tipoAutoritaEsternaE = getRequestStringParameter(
					ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E);
			String sedeAutoritaEsternaE = getRequestStringParameter(
					ICostantiMisuraAlternativa.CAMPO_COD_SEDE_E);
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E)) {
				String lSedeNoteE = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E);
				lNotMod.setNote(lSedeNoteE);// Indirizzo
			}
			lNotMod.setCodEsito("-");
			lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(lCodiceUfficio);
			lNotMod.setDataInvio(lDataEmissione);
			AutoritaEsternaModel aut = new AutoritaEsternaModel();
			aut = new AutoritaEsternaModel();
			aut.setCodTipoAutorita(tipoAutoritaEsternaE);
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(sedeAutoritaEsternaE));
			aut.setCodSede(lComMod.getCodComune());
			aut.setDescrSede(sedeAutoritaEsternaE);
			aut.setCodOperatoreInserimento(lCodiceOperatore);
			aut.setCodUfficioInserimento(lCodiceUfficio);
			aut.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setAutoritaEsterna(aut);
			// AVV_ID_AVVOCATO_FASCICOLO_SIEP
			lNotMod.setAvvIdAvvocatoFascicoloSiep(null);
			// CSS_ID_CSSA
			lNotMod.setCssIdCssa(null);
			// IST_DET_ID_ISTITUTO_DETENZIONE
			lNotMod.setIstDetIdIstitutoDetenzione(null);
			// UFF_COD_UFFICIO
			lNotMod.setUffCodUfficio(null);
			lNotifiche.add(lNotMod);
		}
		// FINE NOTIFICA AUTORITA IV C

		return (NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]);
	}

	/**
	 * Imposta tutte le notifiche associate all'evento prodotto
	 *
	 * @param
	 * @return NotificaModel[]
	 * @throws F3BException
	 */
	protected NotificaModel[] setNotificheSospensioneArrestiDomiciliariMisuraAlternativa()
			throws F3BException {

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		// String posizionegiuridica = getRequestStringParameter("posizionegiuridica");

		ArrayList lNotifiche = new ArrayList();
		String lUfficioScarc = "-";
		if (getRequestStringParameter("tipo").equals("mds"))
			lUfficioScarc = "SORV"; // Eseguita da Magistrato di Sorveglianza
		else if (getRequestStringParameter("tipo").equals("procura"))
			lUfficioScarc = "PROC";// Esegue Procura

		// INIZIO NOTIFICA AUTORITA E
		NotificaModel lNotMod = new NotificaModel();
		// COD_TIPO_NOTIFICA
		lNotMod.setCodTipoNotifica("E");
		// AUT_EST_ID_AUTORITA_ESTERNA
		if (lUfficioScarc.equalsIgnoreCase("PROC")) {
			AutoritaEsternaModel aut = new AutoritaEsternaModel();
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E)) {
				String lSedeNoteE = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E);
				lNotMod.setNote(lSedeNoteE);
			}
			lNotMod.setCodEsito("-");
			lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(lCodiceUfficio);
			lNotMod.setDataInvio(lDataEmissione);
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E)) {
				aut.setCodTipoAutorita(
						getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E));
			} else {
				aut.setCodTipoAutorita("-");
			}
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E)));
			aut.setCodSede(lComMod.getCodComune());
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E)) {
				aut.setDescrSede(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E));
			} else {
				aut.setDescrSede(null);
			}
			aut.setCodOperatoreInserimento(lCodiceOperatore);
			aut.setCodUfficioInserimento(lCodiceUfficio);
			lNotMod.setDataInvio(lDataEmissione);
			aut.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setAutoritaEsterna(aut);
		} else if (lUfficioScarc.equalsIgnoreCase("SORV")) {
			lNotMod.setNote(null);
			lNotMod.setCodEsito("-");
			lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(lCodiceUfficio);
			lNotMod.setDataInvio(lDataEmissione);
			lNotMod.setAutoritaEsterna(null);
		}
		// AVV_ID_AVVOCATO_FASCICOLO_SIEP
		lNotMod.setAvvIdAvvocatoFascicoloSiep(null);
		// CSS_ID_CSSA
		lNotMod.setCssIdCssa(null);
		// IST_DET_ID_ISTITUTO_DETENZIONE
		String istituto = "";
		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
			istituto = getRequestStringParameter(
					ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
			lNotMod.setIstDetIdIstitutoDetenzione(istituto);
		} else {
			lNotMod.setIstDetIdIstitutoDetenzione(null);
		}

		// lNotMod.setIstDetIdIstitutoDetenzione(istituto);
		// UFF_COD_UFFICIO
		lNotMod.setUffCodUfficio(null);

		lNotifiche.add(lNotMod);
		// FINE NOTIFICA AUTORITA E

		// INIZIO NOTIFICA AUTORITA I C
		lNotMod = new NotificaModel();
		// COD_TIPO_NOTIFICA
		lNotMod.setCodTipoNotifica("C");
		// AUT_EST_ID_AUTORITA_ESTERNA
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setDataInvio(lDataEmissione);
		lNotMod.setAutoritaEsterna(null);
		// AVV_ID_AVVOCATO_FASCICOLO_SIEP
		lNotMod.setAvvIdAvvocatoFascicoloSiep(null);
		// CSS_ID_CSSA
		lNotMod.setCssIdCssa(null);
		// IST_DET_ID_ISTITUTO_DETENZIONE
		lNotMod.setIstDetIdIstitutoDetenzione(null);
		// Magistrato di Sorveglianza
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS)
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS) != null
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS).equals("")) {
			// NotificaModel lNotModTDS = new NotificaModel();
			String lTipoUfficio = "UDS";
			// MEV10-s3: aggiunto controllo altrimenti invia sempre UDS
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO)
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO) != null
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO).equals("")) {
				lTipoUfficio = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO);
			}
			String lSedeUfficio = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS);
			String lUDS = getCodUfficioByCodTipoUfficioDescrComune(lTipoUfficio, lSedeUfficio);
			lNotMod.setUffCodUfficio(lUDS);
		}
		lNotMod.setNote(null);
		lNotifiche.add(lNotMod);
		// FINE NOTIFICA AUTORITA I C

		// INIZIO NOTIFICA AUTORITA II C
		lNotMod = new NotificaModel();
		// COD_TIPO_NOTIFICA
		lNotMod.setCodTipoNotifica("C");
		// AUT_EST_ID_AUTORITA_ESTERNA
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setDataInvio(lDataEmissione);
		lNotMod.setAutoritaEsterna(null);
		// AVV_ID_AVVOCATO_FASCICOLO_SIEP
		lNotMod.setAvvIdAvvocatoFascicoloSiep(null);
		// CSS_ID_CSSA
		lNotMod.setCssIdCssa(null);
		// IST_DET_ID_ISTITUTO_DETENZIONE
		lNotMod.setIstDetIdIstitutoDetenzione(null);
		// Tribunale di Sorveglianza
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE)
				&& !isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS)
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS) != null
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS).equals("")) {
			// NotificaModel lNotModTDS = new NotificaModel();
			String lTribunale = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE);
			String lSedeTribunale = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS);
			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(lTribunale, lSedeTribunale);
			lNotMod.setUffCodUfficio(lCodiceUff);
		}
		lNotMod.setNote(null);
		lNotifiche.add(lNotMod);
		// FINE NOTIFICA AUTORITA II C

		return (NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]);
	}

	/**
	 * Imposta tutte le notifiche associate all'evento prodotto
	 *
	 * @param
	 * @return NotificaModel[]
	 * @throws F3BException
	 */
	protected NotificaModel[] setNotificheRevocaArrestiDomiciliariMisuraAlternativa() throws F3BException {

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		Date lDataEmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		// String posizionegiuridica = getRequestStringParameter("posizionegiuridica");

		// revocaDopoSospensioneProvvisoria = "dopoSospensione";//indiretta
		// revocaDopoSospensioneProvvisoria = "direttamente";
		String revocaDopoSospensioneProvvisoria = getRequestStringParameter(
				"revocaDopoSospensioneProvvisoria");

		ArrayList lNotifiche = new ArrayList();

		// INIZIO NOTIFICA AUTORITA E
		NotificaModel lNotMod = new NotificaModel();
		// COD_TIPO_NOTIFICA
		lNotMod.setCodTipoNotifica("E");
		// AUT_EST_ID_AUTORITA_ESTERNA(Autorità competente per territorio)
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E)) {
			String lSedeNoteE = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E);
			lNotMod.setNote(lSedeNoteE);
		}
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setDataInvio(lDataEmissione);
		// lNotMod.setAutoritaEsterna(aut);
		if (revocaDopoSospensioneProvvisoria.equalsIgnoreCase("direttamente")) {
			// AUT_EST_ID_AUTORITA_ESTERNA
			String tipoAutoritaEsternaE;
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E)) {
				tipoAutoritaEsternaE = getRequestStringParameter(
						ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E);
			} else {
				tipoAutoritaEsternaE = "-";
			}
			String sedeAutoritaEsternaE = "-";
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E)) {
				sedeAutoritaEsternaE = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E);
			}
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E)) {
				String lSedeNoteE = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E);
				lNotMod.setNote(lSedeNoteE);
			}
			lNotMod.setCodEsito("-");
			lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(lCodiceUfficio);
			lNotMod.setDataInvio(lDataEmissione);
			AutoritaEsternaModel aut = new AutoritaEsternaModel();
			aut.setCodTipoAutorita(tipoAutoritaEsternaE);
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(sedeAutoritaEsternaE));
			aut.setCodSede(lComMod.getCodComune());
			aut.setDescrSede(sedeAutoritaEsternaE);
			aut.setCodOperatoreInserimento(lCodiceOperatore);
			aut.setCodUfficioInserimento(lCodiceUfficio);
			aut.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setAutoritaEsterna(aut);
		} else if (revocaDopoSospensioneProvvisoria.equalsIgnoreCase("dopoSospensione")) {
			lNotMod.setAutoritaEsterna(null);
		}
		// AVV_ID_AVVOCATO_FASCICOLO_SIEP
		lNotMod.setAvvIdAvvocatoFascicoloSiep(null);
		// CSS_ID_CSSA
		lNotMod.setCssIdCssa(null);
		// IST_DET_ID_ISTITUTO_DETENZIONE
		String istituto = "";
		if (revocaDopoSospensioneProvvisoria.equalsIgnoreCase("dopoSospensione")) {
			if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
				istituto = getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
				lNotMod.setIstDetIdIstitutoDetenzione(istituto);
			}
		} else {
			lNotMod.setIstDetIdIstitutoDetenzione(null);
		}
		// UFF_COD_UFFICIO
		lNotMod.setUffCodUfficio(null);

		lNotifiche.add(lNotMod);
		// FINE NOTIFICA AUTORITA E

		// INIZIO NOTIFICA AUTORITA I C
		lNotMod = new NotificaModel();
		// COD_TIPO_NOTIFICA
		lNotMod.setCodTipoNotifica("C");
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setDataInvio(lDataEmissione);
		// AUT_EST_ID_AUTORITA_ESTERNA
		lNotMod.setAutoritaEsterna(null);
		// AVV_ID_AVVOCATO_FASCICOLO_SIEP
		lNotMod.setAvvIdAvvocatoFascicoloSiep(null);
		// CSS_ID_CSSA
		lNotMod.setCssIdCssa(null);
		// IST_DET_ID_ISTITUTO_DETENZIONE
		lNotMod.setIstDetIdIstitutoDetenzione(null);
		// Tribunale di Sorveglianza
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE)
				&& !isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS)
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS) != null
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS).equals("")) {
			// NotificaModel lNotModTDS = new NotificaModel();
			String lTribunale = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE);
			String lSedeTribunale = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS);
			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(lTribunale, lSedeTribunale);
			lNotMod.setUffCodUfficio(lCodiceUff);
		}
		lNotMod.setNote(null);
		lNotifiche.add(lNotMod);
		// FINE NOTIFICA AUTORITA I C

		//==============================================================================================================
		// INIZIO MAC non segnalata scoperta in fase di intervento MEV 21 Avvocati. Il codice non gestisce in inserimento
		//            più avvocati ma carica solo il primo avvocato della form anche se ne sono presenti 2.
		// Codice sostituito con quello recuperato dal metodo setNotificheMisuraAlternativa()
/*		
		// INIZIO NOTIFICA AUTORITA N
		lNotMod = new NotificaModel();
		// COD_TIPO_NOTIFICA
		lNotMod.setCodTipoNotifica("N");
		// AUT_EST_ID_AUTORITA_ESTERNA
		// nel caso"N" l'autorita' esterna è "Ufficio di Destinazione"
		String tipoAutoritaEsternaE;
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)) {
			tipoAutoritaEsternaE = getRequestStringParameter(
					ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
		} else {
			tipoAutoritaEsternaE = "-";
		}
		String sedeAutoritaEsternaE = "-";
		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_SEDE)) {
			sedeAutoritaEsternaE = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
		}
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI)) {
			String lSedeNoteE = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI);
			lNotMod.setNote(lSedeNoteE);
		}
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setDataInvio(lDataEmissione);
		AutoritaEsternaModel aut = new AutoritaEsternaModel();
		aut.setCodTipoAutorita(tipoAutoritaEsternaE);
		ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(sedeAutoritaEsternaE));
		aut.setCodSede(lComMod.getCodComune());
		aut.setDescrSede(sedeAutoritaEsternaE);
		aut.setCodOperatoreInserimento(lCodiceOperatore);
		aut.setCodUfficioInserimento(lCodiceUfficio);
		aut.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setAutoritaEsterna(aut);
		// AVV_ID_AVVOCATO_FASCICOLO_SIEP
		String avvocato = getRequestStringParameter(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
		lNotMod.setAvvIdAvvocatoFascicoloSiep(new BigDecimal(avvocato));
		// CSS_ID_CSSA
		lNotMod.setCssIdCssa(null);
		// IST_DET_ID_ISTITUTO_DETENZIONE
		lNotMod.setIstDetIdIstitutoDetenzione(null);
		// UFF_COD_UFFICIO
		lNotMod.setUffCodUfficio(null);
		lNotifiche.add(lNotMod);
		// FINE NOTIFICA AUTORITA N
		*/ 
		 
		int lIndex = 0;
		if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)) {
			String[] lAvvocati = this.getRequestStringParameters(ICostantiAvvocato.CAMPO_ID_AVVOCATO);
			for (lIndex = 0; lIndex < lAvvocati.length; lIndex++) {
				if ("".equals(lAvvocati[lIndex])) {
					// 23-01-2014 d.f. per gestire il caso di Avvocati presenti in maschera
					// ma in DIV a scomparsa. Se l'utente decide di non inviare la notifica,
					// sulla submit il campo ICostantiAvvocato.CAMPO_ID_AVVOCATO viene
					// 'sbiancato' e passa comunque come parameter ma vuoto ('').
					continue;
				}
				NotificaModel lNotAvv = new NotificaModel();

				lNotAvv.setCodTipoNotifica("N");
				lNotAvv.setDataInvio(lDataEmissione);
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

					String[] lNoteAvvocato = this
							.getRequestStringParameters(ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI);
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
		// FINE INTERVENTO MAC (MEV_21)
		//==============================================================================================================
		
		
		// INIZIO NOTIFICA AUTORITA II C
		lNotMod = new NotificaModel();
		// COD_TIPO_NOTIFICA
		lNotMod.setCodTipoNotifica("C");
		// AUT_EST_ID_AUTORITA_ESTERNA
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodUfficioInserimento(lCodiceUfficio);
		lNotMod.setDataInvio(lDataEmissione);
		lNotMod.setAutoritaEsterna(null);
		// AVV_ID_AVVOCATO_FASCICOLO_SIEP
		lNotMod.setAvvIdAvvocatoFascicoloSiep(null);
		// CSS_ID_CSSA
		lNotMod.setCssIdCssa(null);
		// IST_DET_ID_ISTITUTO_DETENZIONE
		lNotMod.setIstDetIdIstitutoDetenzione(null);
		// Magistrato di Sorveglianza
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS)
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS) != null
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS).equals("")) {
			// NotificaModel lNotModTDS = new NotificaModel();
			String lTipoUfficio = "UDS";
			// MEV10-s3: aggiunto controllo altrimenti invia sempre UDS
			if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO)
					&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO) != null
					&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO).equals("")) {
				lTipoUfficio = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS_TIPO);
			}
			String lSedeUfficio = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UDS);
			String lUDS = getCodUfficioByCodTipoUfficioDescrComune(lTipoUfficio, lSedeUfficio);
			lNotMod.setUffCodUfficio(lUDS);
		}
		lNotMod.setNote(null);
		lNotifiche.add(lNotMod);
		// FINE NOTIFICA AUTORITA II C
		return (NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]);
	}

	/**
	 * ricerca notifiche
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
			}
			// autorità esterna N
			if (aNotifiche[i].getAutEstIdAutoritaEsterna() != null
					&& aNotifiche[i].getCodTipoNotifica().equals("N")) {
				NotificaModel lNotAutoritaN = aNotifiche[i];
				lTable.put("AutN", lNotAutoritaN);
			}

			// autorità esterna C
			if (aNotifiche[i].getAutEstIdAutoritaEsterna() != null
					&& aNotifiche[i].getCodTipoNotifica().equals("C")) {
				NotificaModel lNotAutoritaC = aNotifiche[i];
				lTable.put("AutC", lNotAutoritaC);
			}
			// autorità esterna PC
			if (aNotifiche[i].getAutEstIdAutoritaEsterna() != null
					&& aNotifiche[i].getCodTipoNotifica().equals("PC")) {
				NotificaModel lNotAutoritaPC = aNotifiche[i];
				lTable.put("AutPC", lNotAutoritaPC);
			}

			// usata solo nell'espulsione
			if (aNotifiche[i].getCodTipoNotifica().equals("C") && aNotifiche[i].getUffCodUfficio() == null
					&& aNotifiche[i].getNote() != null && aNotifiche[i].getAutoritaEsterna() == null) {
				NotificaModel lNotAutoritaTL = aNotifiche[i];
				lTable.put("AutTL", lNotAutoritaTL);
			}

			// ufficio uds e tds
			if (aNotifiche[i].getUffCodUfficio() != null) {
				NotificaModel lNotUfficio = aNotifiche[i];
				if (lNotUfficio.getUfficio().getCodTipoUfficio().equals("UDS")
						|| lNotUfficio.getUfficio().getCodTipoUfficio().equals("UDSM")) {
					NotificaModel lNotUfficioUDS = aNotifiche[i];
					lTable.put("UffUDS", lNotUfficioUDS);
				}

				if (lNotUfficio.getUfficio().getCodTipoUfficio().equals("TDS")
						|| lNotUfficio.getUfficio().getCodTipoUfficio().equals("TDSM")) {
					NotificaModel lNotUfficioTDS = aNotifiche[i];
					lTable.put("UffTDS", lNotUfficioTDS);
				}

				// usata solo nell'espulsione
				if (!lNotUfficio.getUfficio().getCodTipoUfficio().equals("UDS")
						&& !lNotUfficio.getUfficio().getCodTipoUfficio().equals("UDSM")
						&& !lNotUfficio.getUfficio().getCodTipoUfficio().equals("TDS")
						&& !lNotUfficio.getUfficio().getCodTipoUfficio().equals("TDSM")) {
					NotificaModel lNotUfficioURC = aNotifiche[i];
					lTable.put("UffURC", lNotUfficioURC);
				}

			}

		}
		return lTable;
	}

	/**
	 * set DepositoOrdinanzaPc
	 *
	 * @param aUfficioEmittente
	 * @return
	 * @throws F3BException
	 */
	protected DepositoOrdinanzaPcModel setDepositoOrdinanzaPc(String aUfficioEmittente) throws F3BException {

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		DepositoOrdinanzaPcModel lDepOrdMod = new DepositoOrdinanzaPcModel();
		lDepOrdMod.setAnnoS3(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO));
		lDepOrdMod.setNumS3(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO));

		lDepOrdMod
				.setDataUdienza(getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
						ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
						ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE));

		lDepOrdMod.setCodUfficioInserimento(lCodiceUfficio);
		lDepOrdMod.setCodOperatoreInserimento(lCodiceOperatore);
		lDepOrdMod.setDataInserimento(DateUtils.getSysDate());
		lDepOrdMod.setCodUfficioMagistratoComp(aUfficioEmittente);

		return lDepOrdMod;
	}

	/**
	 * Recupera dalla FORM i dati del Deposito Decreto e li restituisce nel Model completandolo con i dati
	 * passati in input.
	 *
	 * @param aUfficioEmittente
	 *            -
	 * @return
	 * @throws F3BException
	 */
	protected DepositoDecretoModel setDepositoDecreto(String aUfficioEmittente) throws F3BException {

		// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		DepositoDecretoModel lDepDecMod = new DepositoDecretoModel();
		lDepDecMod.setAnnoS72(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO));
		lDepDecMod.setNumS72(getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO));
		lDepDecMod.setDataEmissione(
				getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
						ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
						ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE));
		lDepDecMod.setCodUfficioInserimento(lCodiceUfficio);
		lDepDecMod.setCodOperatoreInserimento(lCodiceOperatore);
		lDepDecMod.setDataInserimento(DateUtils.getSysDate());
		lDepDecMod.setCodUfficioCompetente(aUfficioEmittente);

		return lDepDecMod;
	}

	/**
	 * Recupera dalla FORM i dati del Tenore e li restituisce nel Model completandolo con i dati passati in
	 * input.
	 *
	 * @param aProgre
	 * @param aEsito
	 * @return
	 * @throws F3BException
	 */
	protected TenoreModel setTenore(BigDecimal aProgre, String aEsito) throws F3BException {

		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		TenoreModel lTenMod = new TenoreModel();
		lTenMod.setCodEsitoTenore(aEsito);

		lTenMod.setData(getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
				ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
				ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE));

		lTenMod.setCodOggettoTenore(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
		lTenMod.setProgrTenore(aProgre);
		lTenMod.setCodUfficioInserimento(lCodiceUfficio);
		lTenMod.setCodOperatoreInserimento(lCodiceOperatore);
		lTenMod.setDataInserimento(DateUtils.getSysDate());

		return lTenMod;
	}

	/**
	 * Recupera dalla FORM i dati del Tenore e li restituisce nel Model completandolo con i dati passati in
	 * input.
	 *
	 * @param aProgre
	 * @param aEsito
	 * @return
	 * @throws F3BException
	 */
	protected TenoreModel setTenoreRipristinoArrestoDom(BigDecimal aProgre, String aEsito)
			throws F3BException {

		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		TenoreModel lTenMod = new TenoreModel();
		lTenMod.setCodEsitoTenore(aEsito);

		lTenMod.setData(getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
				ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
				ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE));

		// lTenMod.setCodOggettoTenore(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_MOTIVO_TRIBUNALE)
				&& getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO_TRIBUNALE) != null)
			lTenMod.setCodOggettoTenore(
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO_TRIBUNALE));
		else if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_MOTIVO_MAGISTRATO)
				&& getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO_MAGISTRATO) != null)
			lTenMod.setCodOggettoTenore(
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO_MAGISTRATO));

		lTenMod.setProgrTenore(aProgre);
		lTenMod.setCodUfficioInserimento(lCodiceUfficio);
		lTenMod.setCodOperatoreInserimento(lCodiceOperatore);
		lTenMod.setDataInserimento(DateUtils.getSysDate());

		return lTenMod;
	}

	/**
	 * Recupera dalla FORM i dati della Misura Alternativa e li restituisce nel Model completandolo con i dati
	 * passati in input.
	 *
	 * @param aTipoDecisione
	 * @param aNaturaDecisione
	 * @param aChiaveUff
	 * @param aMotivo
	 * @param aUfficioScarcerazione
	 * @return
	 * @throws F3BException
	 */
	protected MisuraAlternativaModel setMisuraAlternativa(String aTipoDecisione, String aNaturaDecisione,
			String aChiaveUff, String aMotivo, String aUfficioScarcerazione) throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();

		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();

		lMisMod.setCodTipoDecisione(aTipoDecisione);
		lMisMod.setCodNaturaDecisione(aNaturaDecisione);
		lMisMod.setCodTipoMisura(aMotivo);
		lMisMod.setDataDecisione(getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
				ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
				ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE));
		lMisMod.setChiaveUfficioFascicoloSius(aChiaveUff);
		lMisMod.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));
		lMisMod.setAnnoRegistro(
				getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO));
		lMisMod.setNumeroRegistro(
				getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO));
		lMisMod.setChiaveProgrFascicoloSius(
				getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS));
		lMisMod.setChiaveAnnoFascicoloSius(
				getRequestBigDecimalParameter(ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS));
		lMisMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		lMisMod.setCodUfficioInserimento(lCodiceUfficio);
		lMisMod.setCodOperatoreInserimento(lCodiceOperatore);
		lMisMod.setCodTipoUfficioScarcerazione(aUfficioScarcerazione);
		lMisMod.setDataInserimento(DateUtils.getSysDate());
		lMisMod.setFlagUfficioInserimento("P");

		// MEV_2019-09 - SI aggiunge la data Esecutività
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_ESECUTIVITA)
				&& getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_ESECUTIVITA) != null
				&& !getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_ESECUTIVITA)
						.equals("-"))
			lMisMod.setDataEsecutivita(
					getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_ESECUTIVITA,
							ICostantiMisuraAlternativa.CAMPO_MESE_DATA_ESECUTIVITA,
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_ESECUTIVITA));

		return lMisMod;
	}

}