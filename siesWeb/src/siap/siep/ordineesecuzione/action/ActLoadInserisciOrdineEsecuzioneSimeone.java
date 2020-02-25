package siap.siep.ordineesecuzione.action;

import java.util.Date;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 *
 * <p>
 * Title: ActLoadInserisciOrdineEsecuzioneSimeone
 * </p>
 * <p>
 * Description: Classe d'inserimento dell'Ordine di Esecuzione Simeone
 * </p>
 * <p>
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * not attributable
 *
 */
public class ActLoadInserisciOrdineEsecuzioneSimeone extends ActionSiap implements ICostantiOrdineEsecuzione {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr()
					+ " non è stato Validato. Impossibile inserire un ordine d'esecuzione con sospensione!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerValidazione&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		isFascicoloSiepDiCompetenza();

		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " Il fascicolo risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		this.isEventoNonValidato();
		// Controllo Esistenza pena residua non validata per quel fascicolo

		PenaResiduaModel lPenaResMod = new PenaResiduaModel();
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		if (lPenaResMod == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
			lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		if (lPenaResMod != null && lPenaResMod.getFlagValidato() != null
				&& lPenaResMod.getFlagValidato().equals("N")) {
			setRequestAttribute("dataeditabile", "S");
		} else {
			// dataeditabile N
		}

		// Controllo esistenza almeno un avvocato per fascicolo.
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		try {
			/* Vector lAvvVect = */lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		} catch (SIEPException e) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					e.getMessage() + " Impossibile eseguire l'Ordine di Esecuzione.");
			lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		Date lDataInizioPena = null;
		Date lDataFinePenaA = null;
		// Date lDataFinePenaM = null;

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (lPos == null || lPos.getPosizioneGiuridica() == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
			lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}
		setRequestAttribute("posizioneluogoaltra", lPos);

		// Imposta Tipo Istituto
		Option lOptionIstituto = new Option(DecodificheManager.getInstance().getTipoIstituto());
		setRequestAttribute("tipoIstituto", "" + lOptionIstituto);

		// Riempimento ComboBoX
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaN", "" + lOption);

		// Lista UFFICIO ESECUZIONE PENALE ESTERNA (UEPE/UEPESS) e UFFICIO SERVIZI SOCIALI MINORILI
		// (USSM/USSMSS)
		Option lOptionUffDest = new Option(DecodificheManager.getInstance().getTipoUffEsePenEstSerSocMin(),
				"-");
		String[] lFiltro = new String[3];
		lFiltro[0] = "-";
		lFiltro[1] = "UEPE";
		lFiltro[2] = "USSM";
		lOptionUffDest.setFilter(lFiltro);
		setRequestAttribute("tipUffDestSerSoc", "" + lOptionUffDest);

		// Lista UFFICIO DI SORVEGLIANZA (-/UDS/UDSM)
		lOptionUffDest = new Option(DecodificheManager.getInstance().getTipoUfficioSiepTDSMUDSM(), "-");
		lFiltro = new String[3];
		lFiltro[0] = "-";
		lFiltro[1] = "UDS";
		lFiltro[2] = "UDSM";
		lOptionUffDest.setFilter(lFiltro);
		setRequestAttribute("tipUffDestMagSor", "" + lOptionUffDest);

		// Lista UFFICIO DI SORVEGLIANZA (-/TDS/TDSM)
		lOptionUffDest = new Option(DecodificheManager.getInstance().getTipoUfficioSiepTDSMUDSM(), "-");
		lFiltro = new String[3];
		lFiltro[0] = "-";
		lFiltro[1] = "TDS";
		lFiltro[2] = "TDSM";
		lOptionUffDest.setFilter(lFiltro);
		setRequestAttribute("tipUffDestEntSor", "" + lOptionUffDest);

		// modifica relativa al tipo istituto
		if (lFascMod.getFlagAltraCausa() != null && lFascMod.getFlagAltraCausa().equals("S")) {
			// modifica relativa al tipo istituto
			if (lPos != null && lPos.getAltraCausa() != null
					&& (lPos.getAltraCausa().getCodTipoPosGiuridica().equals("23")
							|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("78")
							|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("79")
							|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("80")
							|| lPos.getAltraCausa().getCodTipoPosGiuridica().equals("81"))) {
				lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
			} else {
				if (lPos != null && lPos.getAltraCausa() != null
						&& lPos.getAltraCausa().getIstitutoDetenzione() != null)
					lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							lPos.getAltraCausa().getIstitutoDetenzione().getCodTipoIstituto());
			}
		} else {
			// modifica relativa al tipo istituto
			if (lPos.getPosizioneGiuridica().isLibero()
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("02")
					|| lPos.getPosizioneGiuridica().getCodPosizioneGiuridica().equals("04")) {
				lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
			} else {
				if (lPos.getLuogoDetenzione() != null
						&& lPos.getLuogoDetenzione().getIstitutoDetenzione() != null)
					lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),
							lPos.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto());
			}
		}
		lOption.setSelected("-");
		setRequestAttribute("autoritaEsternaE", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(), "SSPA");
		setRequestAttribute("tipoUfficio", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(), "UDS");
		setRequestAttribute("tipoUfficioMagistrato", "" + lOption);

		EventoModel lEve = new EventoModel();
		lEve.setDescrUfficioDestinatario(getUfficioUtenteConnesso().getDescrComune());

		IMagistratoCompetente lMagCtrl = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagi = lMagCtrl
				.ExRicercaMagistratoCompetenteByFascicoloDataFine(lFascMod.getIdFascicoloSiep());
		setRequestAttribute("magistrato", lMagi);
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());

		setRequestAttribute("avvocati", lAvvocati);

		// Imposta Modalità.
		setRequestAttribute("modalita", "I");
		setRequestAttribute("evento", lEve);

		IPenaResidua lCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel llPenMod = lCtrl
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascMod.getIdFascicoloSiep());

		lDataInizioPena = llPenMod.getDataInizio();
		// lDataFinePenaM = llPenMod.getDataFine();
		lDataFinePenaA = llPenMod.getDataFinePresunta();
		setRequestAttribute("StrdataInizioPena", DateUtils.getDateToString(lDataInizioPena, "dd-MM-yyyy"));
		setRequestAttribute("StrdataFinePenaA", DateUtils.getDateToString(lDataFinePenaA, "dd-MM-yyy"));
		setRequestAttribute("penaresidua", llPenMod);

		// Istanza
		EventoModel lEveMod = new EventoModel();
		lEveMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lEveMod.setCodTipoEvento("03");
		lEveMod.setFlagDocumentoRegistrato("S");
		// GDV - Modifica dato che l'unica istanza da appiccicare al simeone è quella C001
		// Per ora non ci sta il filtro
		// ---- lEveMod.setCodMotivo("C001");
		// Leviamo il filtro per ora---
		IEvento lEvCtrl = SICOLookupRemote.getEventoRemote();
		Vector lVectEvento = null;
		try {
			lVectEvento = lEvCtrl.ExRicercaEvento(lEveMod);
		} catch (F3BException e) {
			if (e.getErrorCode() != F3BException.USER_MESSAGE) {
				throw e;
			}
		}

		if (lVectEvento != null) {
			if (lVectEvento.size() > 0) {
				setRequestAttribute("istanza", lVectEvento.firstElement());
				lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
				setRequestAttribute("tipoufficiosius", "" + lOption);
			}
		}

		return PG_LOAD_INSERISCI_ORDINE_ESECUZIONE_SIMEONE; // restituisce la jsp di VIEW
	}

}