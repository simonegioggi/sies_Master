package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.cssa.action.ICostantiCSSA;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
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
 * Title: ActInserisciRipristino
 * Description: Classe Action per l'inserimento di MisuraAlternativa
 *
 * @version 1.0
 */
public class ActInsRipristinoArrestiDomiciliari extends ActRipristino {

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
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		String PosizioneGiu = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		this.setRequestAttribute("posizionegiuridica", PosizioneGiu);

		String ufficioEmittente = getRequestStringParameter(
				ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA);
		this.setRequestAttribute("ufficioEmittente", ufficioEmittente);

		String lPage = null;

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAlModSospesa = null;
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
		if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
			lMisAlModSospesa = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

		// inizio controllo date
		// controllo DATA_ISCRIZIONE <= dataEmissioneDecisione <= data sistema
		Date sysDate = DateUtils.getSysDate();
		Date lDataEmisTras = getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
				ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
				ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE);
		Date dataIscrizioneFascicolo = lFascicoloModel.getDataIscrizione();
		if (lDataEmisTras != null && !DateUtils.isEquals(lDataEmisTras, dataIscrizioneFascicolo)
				&& !DateUtils.isEquals(lDataEmisTras, sysDate)) {// restituisce true se le date sono uguali
			if (lDataEmisTras != null && !DateUtils.isGreater(lDataEmisTras, dataIscrizioneFascicolo)) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"La data emissione decreto non può precedere la data Iscrizione Fascicolo!");
			}
			// 20170831: [SG] modificato il controllo (invertiti i campi e variato il msg)
			if (lDataEmisTras != null && DateUtils.isGreater(lDataEmisTras, sysDate)) {
				throw new F3BException(F3BException.USER_MESSAGE,
						// "La data emissione decreto non deve essere precedente della data odierna!");
						"La data emissione decisione non deve essere superiore alla data odierna!");
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

		// controllo DATA_ISCRIZIONE <= dataScarcerazione <= data sistema
		Date dataScarcerazione = getRequestDateParameter(
				ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
				ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
				ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE);
		if (dataScarcerazione != null && !DateUtils.isEquals(dataScarcerazione, dataIscrizioneFascicolo)
				&& !DateUtils.isEquals(dataScarcerazione, sysDate)) {
			// restituisce true se le date sono uguali
			if (dataScarcerazione != null
					&& !DateUtils.isGreater(dataScarcerazione, dataIscrizioneFascicolo)) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"La data di Scarcerazione non può precedere la data Iscrizione Fascicolo!");
			}
			// 20170831: [SG] modificato il controllo (invertiti i campi e variato il msg)
			if (dataScarcerazione != null && !DateUtils.isGreater(sysDate, dataScarcerazione)) {
				throw new F3BException(F3BException.USER_MESSAGE,
						// "La data di Scarcerazione non deve essere precedente della data odierna!");
						"La data di Scarcerazione non deve essere superiore alla data odierna!");
			}
		}
		// fine controllo date

		// non c'è un decreto di sospensione in misura alternativa
		if (lMisAlModSospesa == null) {
			// INSERISCO EVENTO E NOTIFICA DEL MDS
			EventoNotificaModel lEveMod = new EventoNotificaModel();
			String campoCodMotivo = "";
			if (!this.isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_MOTIVO_TRIBUNALE)
					&& getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO_TRIBUNALE) != null)
				campoCodMotivo = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO_TRIBUNALE);
			else if (!this.isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_MOTIVO_MAGISTRATO)
					&& getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO_MAGISTRATO) != null)
				campoCodMotivo = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO_MAGISTRATO);

			// lEveMod.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			lEveMod.getEvento().setCodMotivo(campoCodMotivo);
			String lCodiceUffEmi = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT));
			ComuneModel lComModAutEmi = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT)));
			lDataEmisTras = getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE);
			// lEveMod.setEvento(setEventoOrdinanzaDecretoMisuraAlternativa(lEveMod.getEvento(),"03",
			// lCodiceUffEmi,lComModAutEmi,lDataEmisTras));

			String lTipoProvv = "";
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE))
				lTipoProvv = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE);
			lEveMod.setEvento(setEventoOrdinanzaDecretoMisuraAlternativa(lEveMod.getEvento(), lTipoProvv,
					lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			// setto il deposito decreto
			// DepositoDecretoModel lDepDecMod = setDepositoDecreto(lCodiceUffEmi);

			// setto il tenore
			TenoreModel lTenMod = setTenoreRipristinoArrestoDom(new BigDecimal(1), "0002");

			// ricerco Pena residua
			// PenaResiduaModel lPenaResMod = new PenaResiduaModel();
			// IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			// lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel
			// .getIdFascicoloSiep());

			// misura aletrantiva
			Date lDataInizioMisura = null;
			String lUfficioScarc = "-";
			if (!this.isRequestParameterNullObj("tipo")) {
				if (this.getRequestStringParameter("tipo").equals("mds")) {
					lUfficioScarc = "SORV";// Eseguita da Magistrato di Sorveglianza
					lDataInizioMisura = getRequestDateParameter(
							ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
							ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
							ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE);
				} else if (this.getRequestStringParameter("tipo").equals("procura")) {
					lUfficioScarc = "PROC";// Esegue Procura
					lDataInizioMisura = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
							ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
							ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
				}
			}

			// lMisMod = setMisuraAlternativa(lTipoProvv,"SP",lCodiceUffEmi,campoCodMotivo,lUfficioScarc);
			lMisMod = setMisuraAlternativa(lTipoProvv, "RG", lCodiceUffEmi, campoCodMotivo, lUfficioScarc);

			if (lDataInizioMisura != null) {
				lMisMod.setDataInizioMisura(lDataInizioMisura);

				if (lUfficioScarc.equals("SORV"))
					lMisMod.setDataScarcerazione(lDataInizioMisura);
			}

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.LUOGO_DELLA_DETENZIONE))
				lMisMod.setDescrLuogoProva(
						getRequestStringParameter(ICostantiMisuraAlternativa.LUOGO_DELLA_DETENZIONE));

			lMisMod.setCssIdCssa(getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA));

			// // setto il deposito ordinanza
			// DepositoOrdinanzaPcModel lDepOrdMod = setDepositoOrdinanzaPc(lCodiceUffEmi);
			// lDepOrdMod.setIdCssaComp(getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA));
			// if(!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA))
			// lDepOrdMod.setLuogoSvolgimentoProva(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA));

			// setto il deposito ordinanza
			DepositoOrdinanzaPcModel lDepOrdMod = setDepositoOrdinanzaPc(lCodiceUffEmi);
			lDepOrdMod.setIdCssaComp(getRequestBigDecimalParameter(ICostantiCSSA.CAMPO_ID_CSSA));
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.LUOGO_DELLA_DETENZIONE))
				lDepOrdMod.setLuogoSvolgimentoProva(
						getRequestStringParameter(ICostantiMisuraAlternativa.LUOGO_DELLA_DETENZIONE));

			// MEV10-s3: anticipo questo metodo per prevenire errore inserimento dati ufficio UDS / TDS
			NotificaModel[] lNotificheMod = this.setNotificheRipristinoArrestiDomiciliariMisuraAlternativa();

			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisuraModel = lCtrlMisura
					.ExInserisciMisuraAlternativaEventoNotifica(lEveMod, lDepOrdMod, lTenMod, lMisMod);

			// INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE
			EventoNotificaModel lEveNot = new EventoNotificaModel();

			// if(lMisMod != null && lMisMod.getCodTipoUfficioScarcerazione() != null&&
			// lMisMod.getCodTipoUfficioScarcerazione().equals("PROC") )
			// lEveNot.getEvento().setCodTipoProvvedimento("24");
			// else if (lMisMod != null && lMisMod.getCodTipoUfficioScarcerazione() != null &&
			// lMisMod.getCodTipoUfficioScarcerazione().equals("SORV") )
			// lEveNot.getEvento().setCodTipoProvvedimento("12");
			// else
			// lEveNot.getEvento().setCodTipoProvvedimento("04");

			// INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE
			String lProcSorv = this.getRequestStringParameter("tipo");
			if (lProcSorv.equals("procura")) { // PROC
				lEveNot.getEvento().setCodTipoProvvedimento("24");
			} else if (lProcSorv.equals("mds")) {
				// SORV
				lEveNot.getEvento().setCodTipoProvvedimento("12");
			} else {
				lEveNot.getEvento().setCodTipoProvvedimento("04");
			}

			// lEveNot.getEvento().setCodTipoProvvedimento(lTipoProvv);
			// String lCodiceMotivo = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO);
			// String lCodiceMotivo = campoCodMotivo;

			// CAMPO_COD_MOTIVO da Oggetto Decisione
			lEveNot.getEvento().setCodMotivo(campoCodMotivo);
			lEveNot.setEvento(setEventoProvvedimentoMisuraAlternativa(lEveNot.getEvento()));
			lEveNot.getEvento().setEveIdEvento(lMisuraModel.getEveIdEvento());
			// lEveNot.getEvento().setCodTipoProvvedimento(lTipoProvv);

			// NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();
			// NotificaModel[] lNotificheMod =
			// this.setNotificheRipristinoArrestiDomiciliariMisuraAlternativa();
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
					+ "=siap.siep.misuraalternativa.action.ActDettaglioRipristinoArrestiDomiciliari&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNotModel.getEvento().getIdEvento() + " & "
					+ ICostantiEvento.CAMPO_COD_MOTIVO + "=" + campoCodMotivo;
		} else {
			// la misura alternativa esiste
			String codiceMotivo = lMisAlModSospesa.getCodTipoMisura();
			EventoNotificaModel lEve = new EventoNotificaModel();
			this.setRequestAttribute("tipoMisura", tipoMisura);

			// String lProcSorv = this.getRequestStringParameter("tipo");
			// if (lProcSorv.equals("procura")){ //PROC
			// lEve.getEvento().setCodTipoProvvedimento("24");
			// } else if(lProcSorv.equals("mds")){
			// //SORV
			// lEve.getEvento().setCodTipoProvvedimento("12");
			// } else{
			// lEve.getEvento().setCodTipoProvvedimento("04");
			// }

			lEve.getEvento().setCodMotivo(getChangeMotivo(codiceMotivo));

			String lTipoProvv = "";
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE))
				lTipoProvv = getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE);

			lEve.getEvento().setCodTipoProvvedimento(lTipoProvv);

			// String campoCodMotivo ="";
			// if(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO_TRIBUNALE)!=null)
			// campoCodMotivo = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO_TRIBUNALE);
			// else if(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO_MAGISTRATO)!=null)
			// campoCodMotivo = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO_MAGISTRATO);

			// lEve.getEvento().setCodMotivo(campoCodMotivo);
			lEve.getEvento().setCodMotivo(codiceMotivo);
			lEve.setEvento(setEventoProvvedimentoMisuraAlternativa(lEve.getEvento()));
			lEve.getMagistrato().setCodMagistrato(this.calcolaMagistrato());
			lEve.getEvento().setEveIdEvento(lIdOrdinanza);

			// Inserisco l'array di Notifiche nell'Evento
			// NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
			NotificaModel[] lNotifiche = this.setNotificheRipristinoArrestiDomiciliariMisuraAlternativa();
			lEve.setNotifiche(lNotifiche);

			BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
			PenaResiduaModel lPenaRes = new PenaResiduaModel();
			lPenaRes.setIdPenaResidua(lIdPenaRes);
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
				lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
						ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
						ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

			lMisAlModSospesa.setCodUfficioAggiornamento(lCodiceUfficio);
			lMisAlModSospesa.setCodOperatoreAggiornamento(lCodiceOperatore);
			lMisAlModSospesa.setDataAggiornamento(DateUtils.getSysDate());

			if (this.getRequestStringParameter("tipo").equals("mds")) {
				lMisAlModSospesa.setCodTipoUfficioScarcerazione("SORV");
				lMisAlModSospesa.setDataScarcerazione(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE));

				lMisAlModSospesa.setDataInizioMisura(
						getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_SCARCERAZIONE,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_SCARCERAZIONE));
			} else if (this.getRequestStringParameter("tipo").equals("procura")) {
				lMisAlModSospesa.setCodTipoUfficioScarcerazione("PROC");
				lMisAlModSospesa.setDataInizioMisura(getRequestDateParameter(
						ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
			}

			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE))
				lMisAlModSospesa.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

			EventoNotificaModel lRetModel = new EventoNotificaModel();
			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			lRetModel = lCtrlMisura.ExInserisciOModificaMANotifica(lEve, lPenaRes, lMisAlModSospesa, null);

			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.misuraalternativa.action.ActDettaglioRipristinoArrestiDomiciliari&"
					+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		}
		return lPage;
	}

}