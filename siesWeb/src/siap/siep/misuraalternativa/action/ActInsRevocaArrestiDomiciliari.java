package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

/**
 * <p>
 * Title: ActInserisciRevoca
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di MisuraAlternativa
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
public class ActInsRevocaArrestiDomiciliari extends ActMisuraAlternativa
		implements ICostantiMisuraAlternativa {

	/**
	 * Azione di Inserimento del MisuraAlternativa
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();

		// setto la natura della MA per chiamare due metodi diversi
		String tipoMisura = getRequestStringParameter("tipomisura");
		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		// String lCodiceOperatore = this.getCodUtenteConnesso();
		// String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		String PosizioneGiu = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		this.setRequestAttribute("posizionegiuridica", PosizioneGiu);

		String lPage = null;

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAlModSospesa = null;
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
		// Il campo lMisAlModSospesa è NOT NULL, quando l'ordinanza arriva dalla Sorveglianza
		if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
			lMisAlModSospesa = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

		// inizio controllo date
		// controllo DATA_ISCRIZIONE<= dataEmissioneOrdinanza <= data sistema
		Date lDataEmisTras = getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
				ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
				ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE);
		Date dataIscrizioneFascicolo = lFascicoloModel.getDataIscrizione();
		Date sysDate = DateUtils.getSysDate();
		if (lDataEmisTras != null && !DateUtils.isEquals(lDataEmisTras, dataIscrizioneFascicolo)
				&& !DateUtils.isEquals(lDataEmisTras, sysDate)) {// restituisce true se le date sono uguali
			if (lDataEmisTras != null && !DateUtils.isGreater(lDataEmisTras, dataIscrizioneFascicolo)) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"La data emissione Ordinanza non può precedere la data Iscrizione Fascicolo!");
			}
			// 20170831: [SG] modificato il controllo (invertiti i campi e variato il msg)
			if (lDataEmisTras != null && DateUtils.isGreater(lDataEmisTras, sysDate)) {
				throw new F3BException(F3BException.USER_MESSAGE,
						// "La data emissione Ordinanza non deve essere precedente della data odierna!");
						"La data emissione Ordinanza non deve essere superiore alla data odierna!");
			}
		}

		// controllo DATA_ISCRIZIONE <= dataEmissione <= data sistema
		Date lDataEmis = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		if (lDataEmis != null && !DateUtils.isEquals(lDataEmis, dataIscrizioneFascicolo)
				&& !DateUtils.isEquals(lDataEmis, sysDate)) {// restituisce true se le date sono uguali
			if (lDataEmis != null && !DateUtils.isGreater(lDataEmis, dataIscrizioneFascicolo)) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"La data emissione non può precedere la data Iscrizione Fascicolo!");
			}
			// 20170831: [SG] modificato il controllo (invertiti i campi e variato il msg)
			if (lDataEmis != null && !DateUtils.isGreater(sysDate, lDataEmis)) {
				throw new F3BException(F3BException.USER_MESSAGE,
						// "La data emissione non deve essere precedente della data odierna!");
						"La data emissione non deve essere superiore alla data odierna!");
			}
		}

		// controllo DATA_ISCRIZIONE <= dataTrasmissione <= data sistema
		Date dataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		if (dataTrasmissione != null && !DateUtils.isEquals(dataTrasmissione, dataIscrizioneFascicolo)
				&& !DateUtils.isEquals(dataTrasmissione, sysDate)) {// restituisce true se le date sono uguali
			if (dataTrasmissione != null && !DateUtils.isGreater(dataTrasmissione, dataIscrizioneFascicolo)) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"La data Trasmissione non può precedere la data Iscrizione Fascicolo!");
			}
			// 20170831: [SG] modificato il controllo (invertiti i campi e variato il msg)
			if (dataTrasmissione != null && !DateUtils.isGreater(sysDate, dataTrasmissione)) {
				throw new F3BException(F3BException.USER_MESSAGE,
						// "La data Trasmissione non deve essere precedente della data odierna!");
						"La data Trasmissione non deve essere superiore alla data odierna!");
			}
		}
		// fine controllo date

		String revocaDopoSospensioneProvvisoria = getRequestStringParameter(
				"revocaDopoSospensioneProvvisoria");
		if (revocaDopoSospensioneProvvisoria != null && !revocaDopoSospensioneProvvisoria.equals(""))
			this.setRequestAttribute("revocaDopoSospensioneProvvisoria", revocaDopoSospensioneProvvisoria);

		// non c'è un decreto di sospensione in misura alternativa
		// il campo lMisAlModSospesa è NULL solo se NON ESISTE L'ORDINANZA emessa dalla SORVEGLIANZA,
		// quindi la sta inserendo la PROCURA (iscrizione a mano dell'Ordinanza)
		if (lMisAlModSospesa == null) {
			// INSERISCO EVENTO E NOTIFICA DEL MDS
			EventoNotificaModel lEveMod = new EventoNotificaModel();
			lEveMod.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			String lCodiceUffEmi = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT));
			ComuneModel lComModAutEmi = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT)));
			lDataEmisTras = getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE);

			// la Procura effettua l'inserimento dell'evento ordinanza
			lEveMod.setEvento(setEventoOrdinazaDecretoMisuraAlternativa(lEveMod.getEvento(), "03",
					lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			// setto il deposito decreto
			// DepositoDecretoModel lDepDecMod = setDepositoDecreto(lCodiceUffEmi);
			DepositoOrdinanzaPcModel lDepOrdMod = setDepositoOrdinanzaPc(lCodiceUffEmi);

			// setto il tenore
			TenoreModel lTenMod = setTenore(new BigDecimal(1), "0006");

			// ricerco Pena residua
			// PenaResiduaModel lPenaResMod = new PenaResiduaModel();
			// IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			// lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel
			// .getIdFascicoloSiep());

			// misura alternativa
			lMisMod = setMisuraAlternativa("03", "RE", lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), "-");
			lMisMod.setDataInizioMisura(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

			// MEV10-s3: anticipo questo metodo per prevenire errore inserimento dati ufficio UDS / TDS
			NotificaModel[] lNotificheMod = this.setNotificheRevocaArrestiDomiciliariMisuraAlternativa();

			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisuraModel = lCtrlMisura
					.ExInserisciMisuraAlternativaEventoNotifica(lEveMod, lDepOrdMod, lTenMod, lMisMod);

			// INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE
			EventoNotificaModel lEveNot = new EventoNotificaModel();

			// CAMPO_COD_MOTIVO da Oggetto Ordinanza
			lEveNot.getEvento().setCodTipoProvvedimento("06");
			lEveNot.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			// if(PosizioneGiu.equals("29") || PosizioneGiu.equals("31") || PosizioneGiu.equals("33") ||
			// PosizioneGiu.equals("36") || PosizioneGiu.equals("38") || PosizioneGiu.equals("12") ||
			// PosizioneGiu.equals("14"))
			// lEveNot.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			// else
			// lEveNot.getEvento().setCodMotivo("0000");

			lEveNot.setEvento(setEventoProvvedimentoMisuraAlternativa(lEveNot.getEvento()));
			lEveNot.getEvento().setEveIdEvento(lMisuraModel.getEveIdEvento());
			lEveNot.getMagistrato()
					.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

			// NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();
			// NotificaModel[] lNotificheMod = this.setNotificheRevocaArrestiDomiciliariMisuraAlternativa();
			lEveNot.setNotifiche(lNotificheMod);

			BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
			PenaResiduaModel lPenaRes = new PenaResiduaModel();
			lPenaRes.setIdPenaResidua(lIdPenaRes);
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

			IMisuraAlternativa lCtrlMisuraAlt = SICOLookupRemote.getMisuraAlternativaRemote();
			EventoNotificaModel lEveNotModel = lCtrlMisuraAlt.ExInserisciOModificaMANotifica(lEveNot,
					lPenaRes, null, null);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioRevocaArrestiDomiciliari&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNotModel.getEvento().getIdEvento();
		} else {
			// nell'else entro quando l'ordinanza arriva già dalla SORVEGLIANZA,
			// quindi la PROCURA DEVE inserire SOLO L'ORDINE DI ESECUZIONE!!! E NON L'EVENTO ORDINANZA!!!
			EventoNotificaModel lEve = new EventoNotificaModel();
			this.setRequestAttribute("tipoMisura", tipoMisura);
			// @emma 09072018 intervento post COLLAUDO 11.2
			// MEV_59 - modifica post collaudo per ID-TEST 0001F008-TF01 08 (il CodTipoProvvedimento deve
			// essere 06), prima era 03
			lEve.getEvento().setCodTipoProvvedimento("06");
			lEve.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			// if(PosizioneGiu.equals("29") || PosizioneGiu.equals("31") || PosizioneGiu.equals("33") ||
			// PosizioneGiu.equals("36") || PosizioneGiu.equals("38")|| PosizioneGiu.equals("12") ||
			// PosizioneGiu.equals("14"))
			// lEve.getEvento().setCodMotivo(codiceMotivo);
			// else
			// lEve.getEvento().setCodMotivo("0000");

			// lEve = getCodiceMotivoTipoEventoSospensioni(codiceMotivo,tipoMisura,lProcSorv);
			lEve.setEvento(setEventoProvvedimentoMisuraAlternativa(lEve.getEvento()));
			lEve.getMagistrato().setCodMagistrato(this.calcolaMagistrato());
			lEve.getEvento().setEveIdEvento(lIdOrdinanza);

			// Inserisco l'array di Notifiche nell'Evento
			// NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
			NotificaModel[] lNotifiche = this.setNotificheRevocaArrestiDomiciliariMisuraAlternativa();
			lEve.setNotifiche(lNotifiche);

			BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
			PenaResiduaModel lPenaRes = new PenaResiduaModel();
			lPenaRes.setIdPenaResidua(lIdPenaRes);
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE))
				lMisAlModSospesa.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

			EventoNotificaModel lRetModel = new EventoNotificaModel();
			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			lRetModel = lCtrlMisura.ExInserisciOModificaMANotifica(lEve, lPenaRes, lMisAlModSospesa, null);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioRevocaArrestiDomiciliari&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		}
		return lPage;
	}

}