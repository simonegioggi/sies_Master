package siap.sius.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.CalendarUtil;
import siap.siep.calcolopena.controller.ICalcoloPena;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.sanzionesostitutiva.model.PeriodoEsecuzioneSanzioneModel;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;

public class ActSanzioneSostitutiva extends ActionSius implements ICostantiSanzioneSostitutiva {

	// 80 tipo scadenzario inizio o ripresa
	// 81 tipo scadenzario sospensione
	// 01 Flag Motivo Inizio
	// 02 Flag Motivo Ripresa
	// 03 Flag Motivo Sospensione

	protected PeriodoAltraSanzioneModel recuperoDatiMaschera(PeriodoAltraSanzioneModel lPerMod)
			throws F3BException {

		lPerMod.setDataInizioEsecuzione(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_ESECUZIONE,
				CAMPO_MESE_DATA_INIZIO_ESECUZIONE, CAMPO_GIORNO_DATA_INIZIO_ESECUZIONE));
		lPerMod.setMotivazione(getRequestStringParameter(CAMPO_MOTIVAZIONE));
		lPerMod.setFlagMotivo(getRequestStringParameter(CAMPO_FLAG_MOTIVO));
		ComuneModel lComMod = this.getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_AUTORITA));
		lPerMod.setCodLuogoAutorita(lComMod.getCodComune());

		if (!this.isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_SOSPENSIONE_GG))
			lPerMod.setSospensioneGG(getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_GG));
		if (!this.isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_SOSPENSIONE_MM))
			lPerMod.setSospensioneMM(getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_MM));
		if (!this.isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_SOSPENSIONE_AA))
			lPerMod.setSospensioneAA(getRequestBigDecimalParameter(CAMPO_SOSPENSIONE_AA));
		// 12/05/2008 if(!this.isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_NUMERO_GIORNI))
		// lPerMod.setNumeroGiorni (getRequestBigDecimalParameter ( CAMPO_NUMERO_GIORNI) );
		if (!this.isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_ANNO_DATA_SCADENZA))
			lPerMod.setDataScadenza(getRequestDateParameter(CAMPO_ANNO_DATA_SCADENZA,
					CAMPO_MESE_DATA_SCADENZA, CAMPO_GIORNO_DATA_SCADENZA));
		if (!this.isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_DA_RECUPERARE))
			lPerMod.setDaRecuperare(getRequestStringParameter(CAMPO_DA_RECUPERARE));
		if (!this.isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_DA_RECUPERARE_GG))
			lPerMod.setDaRecuperareGG(getRequestBigDecimalParameter(CAMPO_DA_RECUPERARE_GG));
		if (!this.isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_DA_RECUPERARE_MM))
			lPerMod.setDaRecuperareMM(getRequestBigDecimalParameter(CAMPO_DA_RECUPERARE_MM));
		if (!this.isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_DA_RECUPERARE_AA))
			lPerMod.setDaRecuperareAA(getRequestBigDecimalParameter(CAMPO_DA_RECUPERARE_AA));

		// controllo ufficio sospensione
		if (!this.isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_UFFICIO)) {
			lPerMod.setCodTipoUfficioSosp(getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO));
		} else {
			lPerMod.setCodTipoUfficioSosp("-");
		}

		// controllo dell'autorita
		if (!this.isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_AUTORITA)) {
			lPerMod.setCodTipoAutorita(
					this.getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_AUTORITA));
		} else {
			lPerMod.setCodTipoAutorita("-");
		}

		// controllo dell'istituto di detenzione
		if (!this.isRequestParameterNullObj(
				ICostantiSanzioneSostitutiva.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)) {
			lPerMod.setIstDetIdIstitutoDetenzione(this.getRequestStringParameter(
					ICostantiSanzioneSostitutiva.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));
		} else {
			lPerMod.setIstDetIdIstitutoDetenzione("-");
		}

		return lPerMod;
	}

	protected BigDecimal recuperoIdFascicolo() throws F3BException {

		BigDecimal lIdFasSius = null;
		FascicoloGPModel lFasGPMod = null;
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		lIdFasSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();
		return lIdFasSius;
	}

	protected BigDecimal recuperoIdFascicoloSiep() throws F3BException {

		BigDecimal lIdFasSiep = null;
		FascicoloGPModel lFasGPMod = null;
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		lIdFasSiep = lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep();
		return lIdFasSiep;
	}

	protected PeriodoEsecuzioneSanzioneModel calcoloDataScadenza(PeriodoAltraSanzioneModel lPerMod,
			EsecuzioneSanzioneSostitutivaModel lESSModelPar) throws F3BException {

		// se non è stata digitata la data scadenza significa che è stato
		// indicato il periodo per cui eseguo il calcolo della
		// data scadenza = data inizio + periodo indicato

		EsecuzioneSanzioneSostitutivaModel lESSModel = new EsecuzioneSanzioneSostitutivaModel(lESSModelPar);
		PeriodoEsecuzioneSanzioneModel lPerEse = new PeriodoEsecuzioneSanzioneModel();
		if (lPerMod.getDataScadenza() == null) {
			if (lPerMod.getSospensioneGG() != null || lPerMod.getSospensioneMM() != null
					|| lPerMod.getSospensioneAA() != null) {
				int NumGiorni = 0;
				int NumMesi = 0;
				int NumAnni = 0;
				CalendarModel lCalModDurata = new CalendarModel();

				if (lPerMod.getSospensioneGG() != null)
					NumGiorni = lPerMod.getSospensioneGG().intValue();
				if (lPerMod.getSospensioneMM() != null)
					NumMesi = lPerMod.getSospensioneMM().intValue();
				if (lPerMod.getSospensioneAA() != null)
					NumAnni = lPerMod.getSospensioneAA().intValue();

				if (NumAnni == 0 && NumMesi == 0 && NumGiorni == 0) {
					throw new SIUSException(SIUSException.USER_MESSAGE, "Periodo non valido");
				}

				lCalModDurata.setNumAnni(NumAnni);
				lCalModDurata.setNumMesi(NumMesi);
				lCalModDurata.setNumGiorni(NumGiorni);
				ICalcoloPena lCal = SIEPLookupRemote.getCalcoloPenaRemote();
				// 10/06/2011
				// lPerMod.setDataScadenza(lCal.exCalcolaNuovaDataFine(lPerMod.getDataInizioEsecuzione(),
				// lCalModDurata, false));
				lPerMod.setDataScadenza(
						lCal.exCalcolaNuovaDataFine(lPerMod.getDataInizioEsecuzione(), lCalModDurata, true));

				if (lESSModel == null || lESSModel.getIdEsecuzioneSanzioneSost().equals(null))
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Attenzione! ESECUZIONE SANZIONE SOSTITUTIVA Assente!");
				if (lPerMod.getDataScadenza().after(lESSModel.getDataTermineAttuale()))
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Data Scadenza Sospensione deve essere inferiore alla Data Termine Sanzione'");
			}
			// controllo se i giorni da recuperare sono superiori alla data scadenza
			// 12/05/2008 Controllo Periodo modificato.
			if (lPerMod.getDaRecuperare().equals("1")) {
				CalendarModel lCalMod = new CalendarModel();
				lCalMod.setDataInizio(lPerMod.getDataInizioEsecuzione());
				lCalMod.setDataFine(lPerMod.getDataScadenza());
				CalendarUtil lCalUtil = new CalendarUtil();
				lCalMod = lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod, false);

				int GG = 0, MM = 0, AA = 0;
				if (lPerMod.getDaRecuperareGG() != null)
					GG = lPerMod.getDaRecuperareGG().intValue();
				if (lPerMod.getDaRecuperareMM() != null)
					MM = lPerMod.getDaRecuperareMM().intValue();
				if (lPerMod.getDaRecuperareAA() != null)
					AA = lPerMod.getDaRecuperareAA().intValue();

				// 29/05/2008 Calcola quanti giorni bisogna recuperare
				int giorniDaRecuperare = (AA * 365) + (MM * 30) + GG;

				if (CalendarUtil.getTotGiorni(lCalMod) < giorniDaRecuperare) {
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Numero di giorni superiore alla Data Scadenza");
				}
				Date lDataTermineAttuale = DateUtils.moveDateTo(lESSModel.getDataTermineAttuale(),
						Calendar.DAY_OF_MONTH, giorniDaRecuperare);
				lESSModel.setDataTermineAttuale(lDataTermineAttuale);
			}
		}
		lPerEse.setPeriodoAltraSanzione(lPerMod);
		lPerEse.setEsecuzioneSanzioneSostitutiva(lESSModel);
		return lPerEse;
	}

	protected CalendarModel quantumEspiata(PeriodoAltraSanzioneModel lPerMod,
			EsecuzioneSanzioneSostitutivaModel lESSModel) throws F3BException {

		// calcolo dei quantum sanzione sostitutiva espiata
		Date DataInizio = null;
		Date DataFine = null;
		if ((lPerMod.getDataScadenza() != null) && lPerMod.getFlagMotivo().equals("03")) {
			// 13/06/2011 DataFine = lPerMod.getDataScadenza();
			DataFine = lPerMod.getDataInizioEsecuzione();
		} else {
			DataFine = lPerMod.getDataInizioEsecuzione();
		}
		DataInizio = lESSModel.getDataInizioSanzione();

		if (lPerMod.getDaRecuperare() != null && lPerMod.getDaRecuperare().equals("1")) {
			// 12/05/2008 Controllo Periodo modificato.
			int GG = 0, MM = 0, AA = 0;
			if (lPerMod.getDaRecuperareGG() != null)
				GG = lPerMod.getDaRecuperareGG().intValue();
			if (lPerMod.getDaRecuperareMM() != null)
				MM = lPerMod.getDaRecuperareMM().intValue();
			if (lPerMod.getDaRecuperareAA() != null)
				AA = lPerMod.getDaRecuperareAA().intValue();
			// 29/05/2008 Calcola quanti giorni bisogna recuperare
			int giorniDaRecuperare = (AA * 365) + (MM * 30) + GG;

			DataInizio = DateUtils.moveDateTo(DataInizio, Calendar.DAY_OF_MONTH, giorniDaRecuperare);
		}
		CalendarModel lCalMode = new CalendarModel();
		lCalMode.setDataInizio(DataInizio);
		lCalMode.setDataFine(DataFine);

		CalendarUtil lCalUtil = new CalendarUtil();
		// essendo una sottrazione metto true cosi se le date sono uguali viene zero
		lCalMode = lCalUtil.CalcolaNumGiorniMesiAnni(lCalMode, true);

		return lCalMode;
	}

	protected EsecuzioneSanzioneSostitutivaModel termineFinale(PeriodoAltraSanzioneModel lPerMod,
			EsecuzioneSanzioneSostitutivaModel lESSModel) throws F3BException {

		CalendarModel lCalModDurata = new CalendarModel();
		lCalModDurata.setNumAnni(lESSModel.getNumAnniSanzione());
		lCalModDurata.setNumMesi(lESSModel.getNumMesiSanzione());
		lCalModDurata.setNumGiorni(lESSModel.getNumGiorniSanzione());

		CalendarUtil lCalUtil = new CalendarUtil();
		lCalModDurata = lCalUtil.ricalcolaGAM(lCalModDurata);

		ICalcoloPena lCal = SIEPLookupRemote.getCalcoloPenaRemote();
		lESSModel.setDataTermineIniziale(
				(lCal.exCalcolaNuovaDataFine(lPerMod.getDataInizioEsecuzione(), lCalModDurata, true)));
		lESSModel.setDataTermineAttuale(
				(lCal.exCalcolaNuovaDataFine(lPerMod.getDataInizioEsecuzione(), lCalModDurata, true)));

		return lESSModel;
	}

	protected CalendarModel quantumTotale(EsecuzioneSanzioneSostitutivaModel lESSModel) throws F3BException {

		// calcolo dei quantum sanzione sostitutiva residua da espiare
		Date DataInizio = null;
		Date DataFine = null;
		DataInizio = lESSModel.getDataInizioSanzione();
		DataFine = lESSModel.getDataTermineAttuale();
		CalendarModel lCalModr = new CalendarModel();
		lCalModr.setDataInizio(DataInizio);
		lCalModr.setDataFine(DataFine);
		CalendarUtil lCalUtil = new CalendarUtil();
		lCalModr = lCalUtil.CalcolaNumGiorniMesiAnni(lCalModr, false);
		// metto false altrimenti in fase di load validazione sbaglia il calcolo
		// ossia manca un giorno paolo c. 23/04/2010

		return lCalModr;
	}

	protected PeriodoAltraSanzioneModel riempioquantumEspiataeResidua(PeriodoAltraSanzioneModel lPerMod,
			CalendarModel lCalMode, CalendarModel lCalModre) throws F3BException {

		lPerMod.setEspiataGG(new BigDecimal(lCalMode.getNumGiorni()));
		lPerMod.setEspiataMM(new BigDecimal(lCalMode.getNumMesi()));
		lPerMod.setEspiataAA(new BigDecimal(lCalMode.getNumAnni()));

		lPerMod.setResiduaGG(new BigDecimal(lCalModre.getNumGiorni()));
		lPerMod.setResiduaMM(new BigDecimal(lCalModre.getNumMesi()));
		lPerMod.setResiduaAA(new BigDecimal(lCalModre.getNumAnni()));
		return lPerMod;
	}

	protected EventoModel preparoEvento(PeriodoAltraSanzioneModel lPerMod) throws F3BException {

		// lEve.setCodMotivo(this.getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
		// lEve.setCodMagistrato(getRequestStringParameter("CodMagistrato_comp"));
		// lEve.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

		EventoModel lEve = new EventoModel();
		lEve.setCodMotivo("0047");
		lEve.setCodTipoEvento("13");
		lEve.setCodTipoProvvedimento("25");
		lEve.setCodEsito("-");
		lEve.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lEve.setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());
		lEve.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEve.setDataInserimento(DateUtils.getSysDate());
		lEve.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lEve.setCodLuogoDestinatario(lPerMod.getCodLuogoAutorita());
		lEve.setCodTipoUfficioDestinatario("-");

		lEve.setDataEmissione(lPerMod.getDataInizioEsecuzione());
		lEve.setDataTrasmissioneAtti(lPerMod.getDataScadenza());

		lEve.setFasSieIdFascicoloSiep(lPerMod.getFasSieIdFascicoloSiep());
		lEve.setFasSiuIdFascicoloSius(lPerMod.getFasSiuIdFascicoloSius());

		lEve.setFlagStampaSiep("N");
		lEve.setFlagVideoSiep("S");
		lEve.setFlagDocumentoRegistrato("S");
		return lEve;
	}

	protected void caricoIstitutiDetenzione(PeriodoAltraSanzioneModel lPerMod) throws F3BException {

		IstitutoDetenzioneModel lIstMod = null;
		if (lPerMod != null && lPerMod.getIstDetIdIstitutoDetenzione() != null
				&& !lPerMod.getIstDetIdIstitutoDetenzione().equals("-")) {
			IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
			lIstMod = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lPerMod.getIstDetIdIstitutoDetenzione());
		}
		setRequestAttribute("istitutodetenzione", lIstMod);
	}

	protected ScadenzarioSiusModel riempioScadenzario(PeriodoAltraSanzioneModel lPerMod,
			ScadenzarioSiusModel lScaMod, String lCodTipo) throws F3BException {

		lScaMod.setCodTipoScadenzario(lCodTipo);

		if (lPerMod.getDataInizioEsecuzione() != null && !lPerMod.getFlagMotivo().equals(("02"))) {
			lScaMod.setDataInizioScadenza(lPerMod.getDataInizioEsecuzione());
		}

		lScaMod.setDataFineScadenza(lPerMod.getDataScadenza());

		if (lScaMod.getCodOperatoreInserimento() == null
				|| lScaMod.getCodOperatoreInserimento().length() == 0) {
			lScaMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		} else {
			lScaMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		}

		if (lScaMod.getDataInserimento() == null) {
			lScaMod.setDataInserimento(DateUtils.getSysDate());
		} else {
			lScaMod.setDataAggiornamento(DateUtils.getSysDate());
		}

		if (lScaMod.getCodUfficioInserimento() == null || lScaMod.getCodUfficioInserimento().length() == 0) {
			lScaMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		} else {
			lScaMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		}

		lScaMod.setFasSiuIdFascicoloSius(lPerMod.getFasSiuIdFascicoloSius());
		if (lPerMod.getEveIdEvento() != null)
			lScaMod.setEveIdEvento(lPerMod.getEveIdEvento());
		return lScaMod;
	}

}