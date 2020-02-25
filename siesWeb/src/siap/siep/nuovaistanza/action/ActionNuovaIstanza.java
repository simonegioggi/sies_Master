package siap.siep.nuovaistanza.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Azione Padre di tutte le Action della nuova istanza.
 * <p>
 * Title: ActionNuovaIstanza
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Agile S.r.l.
 * </p>
 *
 * @version 5.0
 */
public class ActionNuovaIstanza extends ActionSiap
		implements ICostantiNuovaIstanza, ICostantiSentenza, ICostantiSoggetto {

	/**
	 * getFascicolo
	 *
	 * @return lFasMod
	 * @throws F3BException
	 */
	protected FascicoloSiepModel getFascicolo(BigDecimal aIdSentenza) throws Exception {
		// data irrevocabilità per ora non sò come gestirla verrà inserita nella modifica??

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		lFasMod.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy"))); // Anno corrente
		// Il progressivo del fascicolo (in base all'anno e all'ufficio) viene calcolato applicativamente

		lFasMod.setChiaveUfficio(lUtenteMod.getUfficioUtente().getCodUfficio()); // Ufficio dell'operatore che
																					// inserisce

		lFasMod.setCodStatoFascicolo("02"); // Stato fascicolo settato ad aperto
		lFasMod.setCodMotivoArchiviazione("-"); // Motivo di archiviazione '-' per le join
		lFasMod.setCodTipoPosLibero("-"); // Motivo di archiviazione '-' per le join

		lFasMod.setDataIscrizione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));

		lFasMod.setFlagValidato("N"); // Il flag di validazione viene impostato a 'NO'
		lFasMod.setFlagAltraCausa("N"); // Il flag altra causa viene gestito nella gestione della posizione
										// giuridica

		lFasMod.setCodOperatoreInserimento(lUtenteMod.getUserId());
		lFasMod.setDataInserimento(DateUtils.getSysDate());
		lFasMod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

		lFasMod.setSenIdSentenza(aIdSentenza);

		lFasMod.setTipoProgressivo(9);

		if (!isRequestParameterNullObj(ICostantiNuovaIstanza.CAMPO_ANNO_DATA_IRREVOCABILITA)) {
			lFasMod.setDataIrrevocabilita(
					getRequestDateParameter(ICostantiNuovaIstanza.CAMPO_ANNO_DATA_IRREVOCABILITA,
							ICostantiNuovaIstanza.CAMPO_MESE_DATA_IRREVOCABILITA,
							ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_IRREVOCABILITA));
		}
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_ISTANZA)) {
			lFasMod.setDataArrivoAtto(getRequestDateParameter(CAMPO_ANNO_DATA_ISTANZA,
					CAMPO_MESE_DATA_ISTANZA, CAMPO_GIORNO_DATA_ISTANZA));
		} else if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_DEPOSITO)) {
			lFasMod.setDataArrivoAtto(getRequestDateParameter(CAMPO_ANNO_DATA_DEPOSITO,
					CAMPO_MESE_DATA_DEPOSITO, CAMPO_GIORNO_DATA_DEPOSITO));
		}

		return lFasMod;
	}

	/**
	 * getEvento
	 *
	 * @param aIdFascicolo
	 * @return lEveMod
	 * @throws F3BException
	 */
	protected EventoModel getEvento(BigDecimal aIdFascicolo) throws Exception {
		EventoModel lEveMod = new EventoModel();

		lEveMod.setCodTipoEvento("03");
		lEveMod.setCodTipoProvvedimento("08");
		lEveMod.setCodMotivo("0993"); // da verificare con gis
		lEveMod.setFlagDocumentoRegistrato("S");
		lEveMod.setFlagVideoSiep("S");
		lEveMod.setFlagStampaSiep("S");
		lEveMod.setFasSieIdFascicoloSiep(aIdFascicolo);
		lEveMod.setDataInserimento(DateUtils.getSysDate());
		lEveMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEveMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		// lEveMod.setDataEmissione(DateUtils.getSysDate());
		lEveMod.setDataEmissione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));
		lEveMod.setCodEsito("-");
		lEveMod.setCodMagistrato("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodTipoUfficioDestinatario("-");
		lEveMod.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEveMod.setCodUfficioEmittente(getCodUfficioUtenteConnesso());

		return lEveMod;
	}

	/**
	 * getSoggetto
	 *
	 * @return lSogMod
	 * @throws F3BException
	 */
	protected SoggettoModel getSoggetto() throws Exception {
		SoggettoModel lSogMod = new SoggettoModel();

		lSogMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		lSogMod.setNome(getRequestStringParameter(CAMPO_NOME));
		lSogMod.setSesso(getRequestStringParameter(CAMPO_SESSO));
		lSogMod.setAnnoNascita(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_NASCITA));
		lSogMod.setMeseNascita(getRequestBigDecimalParameter(CAMPO_MESE_DATA_NASCITA));
		lSogMod.setDataNascita(getRequestDateParameter(CAMPO_ANNO_DATA_NASCITA, CAMPO_MESE_DATA_NASCITA,
				CAMPO_GIORNO_DATA_NASCITA));
		lSogMod.setDataNascitaPresunta(getRequestStringParameter(CAMPO_DATA_NASCITA_PRESUNTA));

		// Recupero dati del Comune di nascita
		ComuneModel lComMod;
		if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
				&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
			// se presente dal codice comune (e descrizione)
			lComMod = new ComuneModel(getDatiComuneByCodDescrFlagVal(
					getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
					getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
		} else {
			// altrimenti dalla sola descrizione (rischio omonimi)
			lComMod = new ComuneModel(
					getDatiComuneByDescrOmonimiaFlagVal(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
		}

		lSogMod.setCodComuneNascita(lComMod.getCodComune());
		lSogMod.setCodProvinciaNascita(lComMod.getCodProvincia());

		if (!isRequestParameterNullObj(CAMPO_NAZIONALITA)
				&& getRequestStringParameter(CAMPO_NAZIONALITA) != null
				&& getRequestStringParameter(CAMPO_NAZIONALITA).equals("E")) {
			lSogMod.setCodComuneCasellario("342");
		} else {
			lSogMod.setCodComuneCasellario(lComMod.getCodSedeGiudiziaria());
		}

		lSogMod.setCodStatoNascita(getRequestStringParameter(CAMPO_COD_STATO_NASCITA));
		lSogMod.setDescComuneNascitaEstero(getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_ESTERO));
		lSogMod.setNazionalita(getRequestStringParameter(CAMPO_NAZIONALITA));
		lSogMod.setPaternita(getRequestStringParameter(CAMPO_PATERNITA));
		lSogMod.setCognomeMadre(getRequestStringParameter(CAMPO_COGNOME_MADRE));
		lSogMod.setNomeMadre(getRequestStringParameter(CAMPO_NOME_MADRE));
		lSogMod.setCodFiscale(getRequestStringParameter(CAMPO_COD_FISCALE).toUpperCase());
		lSogMod.setAttoNascita(getRequestStringParameter(CAMPO_ATTO_NASCITA).toUpperCase());
		lSogMod.setCodAfis(getRequestStringParameter(CAMPO_COD_AFIS).toUpperCase());
		lSogMod.setNote(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOTE));
		lSogMod.setFlagPresenzaFascicolo("N");

		lSogMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lSogMod.setDataInserimento(DateUtils.getSysDate());
		lSogMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		return lSogMod;
	}

	/**
	 * getModificaNuovaIstanza
	 *
	 * @return NuovaIstanzaModel
	 * @throws F3BException
	 */
	protected NuovaIstanzaModel getModificaNuovaIstanza(NuovaIstanzaModel lNuoMod) throws Exception {

		lNuoMod.setFlagPresdep(getRequestStringParameter(CAMPO_FLAG_PRESDEP));

		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_ISTANZA)) {
			lNuoMod.setDataIstanza(getRequestDateParameter(CAMPO_ANNO_DATA_ISTANZA, CAMPO_MESE_DATA_ISTANZA,
					CAMPO_GIORNO_DATA_ISTANZA));
		} else if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_DEPOSITO)) {
			lNuoMod.setDataIstanza(getRequestDateParameter(CAMPO_ANNO_DATA_DEPOSITO, CAMPO_MESE_DATA_DEPOSITO,
					CAMPO_GIORNO_DATA_DEPOSITO));
		}

		if (!isRequestParameterNullObj(CAMPO_COD_AUTORITA_MITTENTE)) {
			lNuoMod.setCodAutoritaMittente(getRequestStringParameter(CAMPO_COD_AUTORITA_MITTENTE));
			lNuoMod.setCodSedeMittente(
					getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_SEDE_MITTENTE)).getCodComune());
		} else {
			lNuoMod.setCodAutoritaMittente("-");
			lNuoMod.setCodSedeMittente("-");
		}

		// 27/04/2010
		if (!isRequestParameterNullObj(CAMPO_DESCR_MITTENTE)) {
			lNuoMod.setDescrMittente(getRequestStringParameter(CAMPO_DESCR_MITTENTE));
		}
		if (!isRequestParameterNullObj(CAMPO_SOGG_PRESENTANTE)) {
			lNuoMod.setSoggPresentante(getRequestStringParameter(CAMPO_SOGG_PRESENTANTE));
		}

		if (!isRequestParameterNullObj(CAMPO_SOGG_PRESENTANTE_IDENTIFICATO)) {
			lNuoMod.setSoggPresentanteIdentificato(
					getRequestStringParameter(CAMPO_SOGG_PRESENTANTE_IDENTIFICATO));
		}

		if (!isRequestParameterNullObj(CAMPO_AVV_ID_AVVOCATO_PRESENTANTE)) {
			lNuoMod.setAvvIdAvvocatoPresentante(
					getRequestBigDecimalParameter(CAMPO_AVV_ID_AVVOCATO_PRESENTANTE));
		}

		lNuoMod.setAvvIdAvvocato(getRequestBigDecimalParameter(CAMPO_AVV_ID_AVVOCATO));
		lNuoMod.setCodContenuto(getRequestStringParameter(CAMPO_COD_CONTENUTO));
		lNuoMod.setNote(getRequestStringParameter(ICostantiNuovaIstanza.CAMPO_NOTE));

		lNuoMod.setCodUfficioDestinatario("-");
		lNuoMod.setDataAggiornamento(DateUtils.getSysDate());
		lNuoMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lNuoMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lNuoMod.setCodTipoUfficioDestinatario("-");
		lNuoMod.setCodLuogoDestinatario("-");
		lNuoMod.setTipoAvvocato(getRequestStringParameter(CAMPO_TIPO_AVVOCATO));
		lNuoMod.setDataNotificaAvvocato(getRequestDateParameter(ICostantiAvvocato.CAMPO_ANNO_DATA_NOMINA,
				ICostantiAvvocato.CAMPO_MESE_DATA_NOMINA, ICostantiAvvocato.CAMPO_GIORNO_DATA_NOMINA));

		return lNuoMod;
	}

	/**
	 * getNuovaIstanza
	 *
	 * @param lIdFascicolo
	 * @return
	 * @throws F3BException
	 */
	protected NuovaIstanzaModel getNuovaIstanza(BigDecimal lIdFascicolo) throws Exception {
		NuovaIstanzaModel lNuoMod = new NuovaIstanzaModel();

		lNuoMod.setCodStatoIstanza("01");// iscritto
		lNuoMod.setCodEsito("-");
		lNuoMod.setFlagPresdep(getRequestStringParameter(CAMPO_FLAG_PRESDEP));

		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_ISTANZA)) {
			lNuoMod.setDataIstanza(getRequestDateParameter(CAMPO_ANNO_DATA_ISTANZA, CAMPO_MESE_DATA_ISTANZA,
					CAMPO_GIORNO_DATA_ISTANZA));
		} else if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_DEPOSITO)) {
			lNuoMod.setDataIstanza(getRequestDateParameter(CAMPO_ANNO_DATA_DEPOSITO, CAMPO_MESE_DATA_DEPOSITO,
					CAMPO_GIORNO_DATA_DEPOSITO));
		}

		if (!isRequestParameterNullObj(CAMPO_COD_AUTORITA_MITTENTE)) {
			lNuoMod.setCodAutoritaMittente(getRequestStringParameter(CAMPO_COD_AUTORITA_MITTENTE));
			lNuoMod.setCodSedeMittente(
					getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_SEDE_MITTENTE)).getCodComune());
		} else {
			lNuoMod.setCodAutoritaMittente("-");
			lNuoMod.setCodSedeMittente("-");
		}

		// 27/04/2010
		if (!isRequestParameterNullObj(CAMPO_DESCR_MITTENTE)) {
			lNuoMod.setDescrMittente(getRequestStringParameter(CAMPO_DESCR_MITTENTE));
		}
		if (!isRequestParameterNullObj(CAMPO_SOGG_PRESENTANTE)) {
			lNuoMod.setSoggPresentante(getRequestStringParameter(CAMPO_SOGG_PRESENTANTE));
		}

		if (!isRequestParameterNullObj(CAMPO_SOGG_PRESENTANTE_IDENTIFICATO)) {
			lNuoMod.setSoggPresentanteIdentificato(
					getRequestStringParameter(CAMPO_SOGG_PRESENTANTE_IDENTIFICATO));
		}

		if (!isRequestParameterNullObj(CAMPO_AVV_ID_AVVOCATO_PRESENTANTE)) {
			lNuoMod.setAvvIdAvvocatoPresentante(
					getRequestBigDecimalParameter(CAMPO_AVV_ID_AVVOCATO_PRESENTANTE));
		}

		lNuoMod.setAvvIdAvvocato(getRequestBigDecimalParameter(CAMPO_AVV_ID_AVVOCATO));
		lNuoMod.setCodContenuto(getRequestStringParameter(CAMPO_COD_CONTENUTO));
		lNuoMod.setNote(getRequestStringParameter(ICostantiNuovaIstanza.CAMPO_NOTE));

		if (lIdFascicolo != null)
			lNuoMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		lNuoMod.setCodUfficioDestinatario("-");
		lNuoMod.setDataInserimento(DateUtils.getSysDate());
		lNuoMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNuoMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lNuoMod.setCodTipoUfficioDestinatario("-");
		lNuoMod.setCodLuogoDestinatario("-");
		if (lNuoMod.getAvvIdAvvocato() != null) {
			lNuoMod.setTipoAvvocato(getRequestStringParameter(CAMPO_TIPO_AVVOCATO));
			lNuoMod.setDataNotificaAvvocato(getRequestDateParameter(ICostantiAvvocato.CAMPO_ANNO_DATA_NOMINA,
					ICostantiAvvocato.CAMPO_MESE_DATA_NOMINA, ICostantiAvvocato.CAMPO_GIORNO_DATA_NOMINA));
		} else {
			lNuoMod.setTipoAvvocato("-");
		}

		return lNuoMod;
	}

	/**
	 * getSentenza
	 *
	 * @return lSenMod
	 * @throws F3BException
	 */
	protected SentenzaModel getSentenza() throws Exception {

		SentenzaModel lSenMod = new SentenzaModel();

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		lSenMod.setDescrUfficioInserimento(lUtenteMod.getUfficioUtente().getDescrTipoUfficio());
		lSenMod.setCodTipoProvvedimento("01");

		if (!isRequestParameterNullObj(CAMPO_ANNO_REGE_PM)) {
			lSenMod.setNumeroRegePm(getRequestStringParameter(CAMPO_NUMERO_REGE_PM));
			lSenMod.setAnnoRegePm(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_PM));
		}

		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("gip")) {
			lSenMod.setAnnoRegeGip(getRequestBigDecimalParameter("ARG"));
			lSenMod.setNumeroRegeGip(getRequestStringParameter("NRG"));
		}
		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("dib")) {
			lSenMod.setAnnoRegeDib(getRequestBigDecimalParameter("ARG"));
			lSenMod.setNumeroRegeDib(getRequestStringParameter("NRG"));
		}
		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("cas")) {
			lSenMod.setAnnoRegeCas(getRequestBigDecimalParameter("ARG"));
			lSenMod.setNumeroRegeCas(getRequestStringParameter("NRG"));
		}
		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("cap")) {
			lSenMod.setAnnoRegeCap(getRequestBigDecimalParameter("ARG"));
			lSenMod.setNumeroRegeCap(getRequestStringParameter("NRG"));
		}
		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("casap")) {
			lSenMod.setAnnoRegeCasap(getRequestBigDecimalParameter("ARG"));
			lSenMod.setNumeroRegeCasap(getRequestStringParameter("NRG"));
		}
		// MEV_66: aggiunte quattro nuove proprietà
		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("gup")) {
			lSenMod.setAnnoRegeGup(getRequestBigDecimalParameter("ARG"));
			lSenMod.setNumeroRegeGup(getRequestStringParameter("NRG"));
		}
		if (getRequestStringParameter("TipoRG").equalsIgnoreCase("capsm")) {
			lSenMod.setAnnoRegeCapsm(getRequestBigDecimalParameter("ARG"));
			lSenMod.setNumeroRegeCapsm(getRequestStringParameter("NRG"));
		}

		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_PROVVEDIMENTO))
			lSenMod.setDataProvvedimento(getRequestDateParameter(CAMPO_ANNO_DATA_PROVVEDIMENTO,
					CAMPO_MESE_DATA_PROVVEDIMENTO, CAMPO_GIORNO_DATA_PROVVEDIMENTO));

		lSenMod.setNumeroSentenza(getRequestStringParameter(CAMPO_NUMERO_SENTENZA));
		lSenMod.setAnnoSentenza(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA));

		// 23/04/2010 Controllo Ufficio Emittente.
		/* String lCodUfficioEmittente = */getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE),
				getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE));

		// Hanno inserito l'autorità di riferimento
		String lCodTipo = getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE);

		if (getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_PROVV_RIF).compareTo("-") != 0) {

			if (lCodTipo.equals("CAP") || lCodTipo.equals("CAPSM") || lCodTipo.equals("CASAP")
					|| lCodTipo.equals("PGCAP") || lCodTipo.equals("PGMI") || lCodTipo.equals("PGMID")) {
				if (getRequestStringParameter(CAMPO_COD_TIPO_PROVV_RIF).equals("01"))
					lSenMod.setCodTipoProvvRif("03");
				else
					lSenMod.setCodTipoProvvRif("04");
			}
		}
		// COD_TIPO_RITO
		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_RITO)) {

			lSenMod.setCodTipoRito(getRequestStringParameter(CAMPO_COD_TIPO_RITO));

			if (!isRequestParameterNullObj(CAMPO_COD_TIPO_RITO_RIF)) {
				if (!getRequestStringParameter(CAMPO_COD_TIPO_RITO_RIF).equals("-"))
					lSenMod.setCodTipoRito(getRequestStringParameter(CAMPO_COD_TIPO_RITO_RIF));
			}
		} else {
			lSenMod.setCodTipoRito("-");
		}

		String lCodSedeEmittente = getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE))
				.getCodComune();
		lSenMod.setCodLuogoEmittente(lCodSedeEmittente);
		lSenMod.setCodTipoAutoritaEmittente(lCodTipo);
		lSenMod.setNumSezioneAutoritaEmittente(
				getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE));

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PROVV_RIF)
				&& getRequestStringParameter(CAMPO_COD_TIPO_PROVV_RIF) != null) {
			lSenMod.setCodTipoProvvRif(getRequestStringParameter(CAMPO_COD_TIPO_PROVV_RIF));
		} else {
			lSenMod.setCodTipoProvvRif("-");

		}

		if (!isRequestParameterNullObj(CAMPO_COD_LUOGO_PROVV_RIF)
				&& getRequestStringParameter(CAMPO_COD_LUOGO_PROVV_RIF) != null) {
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_PROVV_RIF)));
			lSenMod.setCodLuogoProvvRif(lComMod.getCodComune());
		} else {
			lSenMod.setCodLuogoProvvRif("-");

		}

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_AUTORITA_PROVV_RIF)
				&& getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_PROVV_RIF) != null) {
			lSenMod.setCodTipoAutoritaProvvRif(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_PROVV_RIF));
		} else {
			lSenMod.setCodTipoAutoritaProvvRif("-");
		}
		lSenMod.setDataProvvRif(getRequestDateParameter(CAMPO_ANNO_DATA_PROVV_RIF, CAMPO_MESE_DATA_PROVV_RIF,
				CAMPO_GIORNO_DATA_PROVV_RIF));
		lSenMod.setAnnoProvvRif(getRequestBigDecimalParameter(CAMPO_ANNO_PROVV_RIF));
		lSenMod.setNumeroProvvRif(getRequestStringParameter(CAMPO_NUMERO_PROVV_RIF));

		lSenMod.setNumSezioneAutoritaProvvRif(
				getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_PROVV_RIF));

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PROVVEDIMENTO_RIF)) {
			lSenMod.setCodTipoProvvedimentoRif(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO_RIF));
		} else {
			lSenMod.setCodTipoProvvedimentoRif("-");
		}
		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO)) {
			lSenMod.setCodTipoProvvedimentoAltro(
					getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO));
		} else {
			lSenMod.setCodTipoProvvedimentoAltro("-");
		}
		if (!isRequestParameterNullObj(CAMPO_SEDE_NOTIZIA_REATO)) {
			String lCodSedeNotizia = getCodComuneByDescr(getRequestStringParameter(CAMPO_SEDE_NOTIZIA_REATO))
					.getCodComune();
			lSenMod.setCodSedeNotiziaReato(lCodSedeNotizia);
		} else {
			lSenMod.setCodSedeNotiziaReato("-");
		}

		lSenMod.setCodBilanciamentoCircostanze("-"); // Per le join

		/*
		 * ---------------------------------------------------------------------------- Pezza
		 * d'appoggio....occhio che è palesemente na fregnaccia Gianluca 15.03.2004
		 * ----------------------------------------------------------------------------
		 */
		lSenMod.setNote1DecisioneCassazione(getRequestStringParameter("ANNOREGECAS"));
		lSenMod.setNote2DecisioneCassazione(getRequestStringParameter("NUMREGECAS"));

		if (!isRequestParameterNullObj(CAMPO_ANNO_RACCOLTA_GENERALE)) {
			lSenMod.setAnnoRaccoltaGenerale(getRequestBigDecimalParameter(CAMPO_ANNO_RACCOLTA_GENERALE));
			lSenMod.setNumeroRaccoltaGenerale(getRequestStringParameter(CAMPO_NUMERO_RACCOLTA_GENERALE));
		}

		if (isRequestParameterNullObj(CAMPO_COD_TIPO_DECISIONE_CASSAZIONE))
			lSenMod.setCodTipoDecisioneCassazione("-");
		else {
			lSenMod.setCodTipoDecisioneCassazione(
					getRequestStringParameter(CAMPO_COD_TIPO_DECISIONE_CASSAZIONE));
			lSenMod.setAnnoSentenzaCassazione(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA_CASSAZIONE));
			lSenMod.setNumeroSentenzaCassazione(getRequestStringParameter(CAMPO_NUMERO_SENTENZA_CASSAZIONE));
		}

		lSenMod.setNote(getRequestStringParameter(ICostantiSentenza.CAMPO_NOTE));

		// Valorizzazione dati 23/04/2010
		lSenMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lSenMod.setDataInserimento(DateUtils.getSysDate());
		lSenMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		getControlloSentenzaEsistente(lSenMod);

		return lSenMod;
	}

	/**
	 * getDecreto
	 *
	 * @return lSenMod
	 * @throws F3BException
	 */
	protected SentenzaModel getDecreto() throws Exception {

		SentenzaModel lSenMod = new SentenzaModel();

		lSenMod.setCodTipoProvvedimento("02");

		lSenMod.setAnnoRegeGip(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_GIP_D));
		lSenMod.setNumeroRegeGip(getRequestStringParameter(CAMPO_NUMERO_REGE_GIP_D));

		if (!isRequestParameterNullObj(CAMPO_ANNO_REGE_PM_D)) {
			lSenMod.setNumeroRegePm(getRequestStringParameter(CAMPO_NUMERO_REGE_PM_D));
			lSenMod.setAnnoRegePm(getRequestBigDecimalParameter(CAMPO_ANNO_REGE_PM_D));
		}

		if (!isRequestParameterNullObj(CAMPO_SEDE_NOTIZIA_REATO_D)) {
			String lCodSedeNotizia = getCodComuneByDescr(
					getRequestStringParameter(CAMPO_SEDE_NOTIZIA_REATO_D)).getCodComune();
			lSenMod.setCodSedeNotiziaReato(lCodSedeNotizia);
		} else {
			lSenMod.setCodSedeNotiziaReato("-");
		}

		lSenMod.setDataProvvedimento(getRequestDateParameter(CAMPO_ANNO_DATA_PROVVEDIMENTO_D,
				CAMPO_MESE_DATA_PROVVEDIMENTO_D, CAMPO_GIORNO_DATA_PROVVEDIMENTO_D));
		lSenMod.setNumeroSentenza(getRequestStringParameter(CAMPO_NUMERO_SENTENZA_D));
		lSenMod.setAnnoSentenza(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA_D));

		// 23/04/2010 Controllo Ufficio Emittente.
		/* String lCodUfficioEmittente = */getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE_D),
				getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE_D));

		String lCodTipo = getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE_D);
		String lCodSedeEmittente = getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE_D))
				.getCodComune();
		lSenMod.setCodLuogoEmittente(lCodSedeEmittente);
		lSenMod.setCodTipoAutoritaEmittente(lCodTipo);
		lSenMod.setNumSezioneAutoritaEmittente(
				getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE_D));
		lSenMod.setCodTipoProvvRif("-");
		lSenMod.setCodLuogoProvvRif("-");
		lSenMod.setCodTipoAutoritaProvvRif("-");

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PROVVEDIMENTO_RIF)) {
			lSenMod.setCodTipoProvvedimentoRif(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO_RIF));
		} else {
			lSenMod.setCodTipoProvvedimentoRif("-");
		}
		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO)) {
			lSenMod.setCodTipoProvvedimentoAltro(
					getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO));
		} else {
			lSenMod.setCodTipoProvvedimentoAltro("-");
		}

		if (isRequestParameterNullObj(CAMPO_COD_TIPO_DECISIONE_CASSAZIONE_D))
			lSenMod.setCodTipoDecisioneCassazione("-");
		else {
			lSenMod.setCodTipoDecisioneCassazione(
					getRequestStringParameter(CAMPO_COD_TIPO_DECISIONE_CASSAZIONE_D));
			lSenMod.setAnnoSentenzaCassazione(
					getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA_CASSAZIONE_D));
			lSenMod.setNumeroSentenzaCassazione(
					getRequestStringParameter(CAMPO_NUMERO_SENTENZA_CASSAZIONE_D));
		}

		lSenMod.setNote(getRequestStringParameter(ICostantiSentenza.CAMPO_NOTE_D));
		if (!isRequestParameterNullObj(CAMPO_ANNO_RACCOLTA_GENERALE_D)) {
			lSenMod.setAnnoRaccoltaGenerale(getRequestBigDecimalParameter(CAMPO_ANNO_RACCOLTA_GENERALE_D));
			lSenMod.setNumeroRaccoltaGenerale(getRequestStringParameter(CAMPO_NUMERO_RACCOLTA_GENERALE_D));
		}

		lSenMod.setCodBilanciamentoCircostanze("-");

		// Valorizzazione dati 23/04/2010
		lSenMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lSenMod.setDataInserimento(DateUtils.getSysDate());
		lSenMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		getControlloSentenzaEsistente(lSenMod);

		return lSenMod;
	}

	/**
	 * getSentenzaStraniera
	 *
	 * @return lSenMod
	 * @throws F3BException
	 */

	protected SentenzaModel getSentenzaStraniera() throws Exception {
		SentenzaModel lSenMod = new SentenzaModel();

		lSenMod.setCodTipoProvvedimento("05");

		lSenMod.setDataProvvedimento(getRequestDateParameter(CAMPO_ANNO_DATA_PROVVEDIMENTO_SS,
				CAMPO_MESE_DATA_PROVVEDIMENTO_SS, CAMPO_GIORNO_DATA_PROVVEDIMENTO_SS));
		lSenMod.setNumeroSentenza(getRequestStringParameter(CAMPO_NUMERO_SENTENZA_SS));
		lSenMod.setAnnoSentenza(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA_SS));

		// 23/04/2010 Controllo Ufficio Emittente.
		/* String lCodUfficioEmittente = */getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE_SS),
				getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE_SS));

		String lCodTipo = getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE_SS);
		String lCodSedeEmittente = getCodComuneByDescr(
				getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE_SS)).getCodComune();
		lSenMod.setCodLuogoEmittente(lCodSedeEmittente);
		lSenMod.setCodTipoAutoritaEmittente(lCodTipo);
		lSenMod.setNumSezioneAutoritaEmittente(
				getRequestStringParameter(CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE_SS));
		lSenMod.setCodTipoProvvRif("-");
		lSenMod.setCodLuogoProvvRif("-");
		lSenMod.setCodTipoAutoritaProvvRif("-");
		lSenMod.setCodBilanciamentoCircostanze("-");
		lSenMod.setNote(getRequestStringParameter(ICostantiSentenza.CAMPO_NOTE_SS));

		if (isRequestParameterNullObj(CAMPO_COD_TIPO_DECISIONE_CASSAZIONE_D))
			lSenMod.setCodTipoDecisioneCassazione("-");
		else {
			lSenMod.setCodTipoDecisioneCassazione(
					getRequestStringParameter(CAMPO_COD_TIPO_DECISIONE_CASSAZIONE_D));
			lSenMod.setAnnoSentenzaCassazione(
					getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA_CASSAZIONE_D));
			lSenMod.setNumeroSentenzaCassazione(
					getRequestStringParameter(CAMPO_NUMERO_SENTENZA_CASSAZIONE_D));
		}
		if (!isRequestParameterNullObj(CAMPO_SEDE_NOTIZIA_REATO_D)) {
			String lCodSedeNotizia = getCodComuneByDescr(
					getRequestStringParameter(CAMPO_SEDE_NOTIZIA_REATO_D)).getCodComune();
			lSenMod.setCodSedeNotiziaReato(lCodSedeNotizia);
		} else {
			lSenMod.setCodSedeNotiziaReato("-");
		}
		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PROVVEDIMENTO_RIF)) {
			lSenMod.setCodTipoProvvedimentoRif(getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO_RIF));
		} else {
			lSenMod.setCodTipoProvvedimentoRif("-");
		}
		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO)) {
			lSenMod.setCodTipoProvvedimentoAltro(
					getRequestStringParameter(CAMPO_COD_TIPO_PROVVEDIMENTO_ALTRO));
		} else {
			lSenMod.setCodTipoProvvedimentoAltro("-");
		}

		// Valorizzazione dati 23/04/2010
		lSenMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lSenMod.setDataInserimento(DateUtils.getSysDate());
		lSenMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		getControlloSentenzaEsistente(lSenMod);

		return lSenMod;
	}

	/**
	 * getControlloSentenzaEsistente
	 *
	 * @param lSenMod
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	private void getControlloSentenzaEsistente(SentenzaModel lSenMod) throws Exception {

		SentenzaModel lRis = null;
		ISentenza lSCtrl = SIEPLookupRemote.getSentenzaRemote();
		Vector lRisRic = null;

		try {
			lRisRic = lSCtrl.ExRicercaSentenzaDuplicata(lSenMod);
		} catch (SIEPException e) {
		}

		if (lRisRic != null && lRisRic.size() > 0) {

			lRis = (SentenzaModel) lRisRic.get(0);
			String lDescrizioneProvv = "";
			if (lRis.getCodTipoProvvedimento() != null && (lRis.getCodTipoProvvedimento().equals("01")
					|| lRis.getCodTipoProvvedimento().equals("05")))
				lDescrizioneProvv = "La sentenza ";
			else
				lDescrizioneProvv = "Il decreto ";

			throw new SIEPException(SIEPException.SENTENZA_PRESENTE_NEL_SISTEMA,
					lDescrizioneProvv + lRis.getAnnoSentenza() + "/" + lRis.getNumeroSentenza() + " - "
							+ lRis.getDescrTipoAutoritaEmittente() + " <br>di "
							+ lRis.getDescrLuogoEmittente() + " è già presente in archivio.");
		}
	}

}