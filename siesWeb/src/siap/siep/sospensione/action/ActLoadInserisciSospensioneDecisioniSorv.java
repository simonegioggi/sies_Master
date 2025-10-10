package siap.siep.sospensione.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * ActLoadInserisciSospensioneDecisioniSorv - Classe Action per la load inserimento di Sospensione Decisioni
 * Sorveglianza
 *
 * @version 1.0
 */
public class ActLoadInserisciSospensioneDecisioniSorv extends ActionSiap implements ICostantiSospensione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// Controllo Presenza del Fascicolo in Sessione
		if (isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		isFascicoloSiepDiCompetenza();

		if (isFascicoloNonValidato() || isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		/******************************* Posizione Giuridica **********************************/
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		if (notEsistePosizioneGiuridica(lPos))
			return IWebConstants.PG_MESSAGE;

		setRequestAttribute("posizioneluogoaltra", lPos);
		PosizioneGiuridicaModel lPosizione = lPos.getPosizioneGiuridica();

		/******************************* Pena Complessiva *****************************/
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		String lFlagErgastolo = "N";
		// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
		if (lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != "") {
			if (lPenComMod.getCodTipoPenaDetentiva().equals("03"))
				lFlagErgastolo = "S";
			else if (lPenComMod.getCodTipoPenaDetentiva().equals("04"))
				lFlagErgastolo = "D";
		}

		setRequestAttribute("flagergastolo", lFlagErgastolo);
		/******************************* Fine Pena Complessiva ************************/
		/*********************************** Pena Residua ***************************/

		// Controllo Esistenza pena residua per quel fascicolo
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaResMod = new PenaResiduaModel();

		// MEV_2019-09-SIEP: aggiunta impostazione parametro
		String tipoOperazione = "";
		if (!isRequestParameterNullObj("tipoOperazione")) {
			tipoOperazione = getRequestStringParameter("tipoOperazione");
			setRequestAttribute("tipoOperazione", tipoOperazione);
		}

		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA)
				&& getRequestStringParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA) != null) {
			BigDecimal lKeyPenaRe = new BigDecimal(
					getRequestStringParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA));
			lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaByKey(lKeyPenaRe);
			setRequestAttribute("penaresidua", lPenaResMod);

			PenaResiduaModel lPenModel = (PenaResiduaModel) getSessionAttribute("SOSPpenaresidua");
			setRequestAttribute("nuovapenaresidua", lPenModel);

			if (!isSessionAttributeNullObj("SOSPENSIONE")) {
				SospensioneModel lSospModel = (SospensioneModel) getSessionAttribute("SOSPENSIONE");
				setRequestAttribute("sospensione", lSospModel);
			}
		} else {
			if (!"MODIFICA".equals(tipoOperazione))
				isEventoNonValidato();
			// rimuovo la pena dalla sessione
			removeSessionAttribute("SOSPpenaresidua");
			// rimuovo la sospensione dalla sessione
			removeSessionAttribute("SOSPENSIONE");

			if (lPosizione.getCodPosizioneGiuridica() != null
					&& (!lPosizione.getCodPosizioneGiuridica().equals("07")
							&& !lPosizione.getCodPosizioneGiuridica().equals("16")
							&& !lPosizione.getCodPosizioneGiuridica().equals("17")
							&& !lPosizione.getCodPosizioneGiuridica().equals("46")
							// LIBERO
							&& !lPosizione.getCodPosizioneGiuridica().equals("47"))) {
				lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
			} else {
				lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
			}

			// La Pena Residua deve essere presente e deve avere settate le date inizio/fine
			// (a meno del caso libero in cui le date non ci sono e puo' non essere validata)
			String lErrore = null;
			String lAzioneChiamante = null;

			if (lPenaResMod == null) {
				// lErrore = "Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?";
				lErrore = "Pena Residua da Espiare Inesistente o non validata. Eseguire Calcolo della pena?";
				lAzioneChiamante = "siap.siep.calcolopena.action.ActLoadCalcoloPena";
			} else if ((!lPosizione.isLibero() && !lPosizione.getCodPosizioneGiuridica().equals("16")
					&& !lPosizione.getCodPosizioneGiuridica().equals("17")
					&& !lPosizione.getCodPosizioneGiuridica().equals("46")
					&& !lPosizione.getCodPosizioneGiuridica().equals("47")) // non è libero
					&& (lPenaResMod.getDataInizio() == null // non ha le date
							|| lPenaResMod.getDataFine() == null)
					&& lFlagErgastolo.equals("N")) {
				lErrore = "Data decorrenza pena inestistente. Rivedere la Posizione Giuridica";
				lAzioneChiamante = "siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica";
			}

			if (lErrore != null) {
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, lErrore);
				lRedirigi.setAction(lAzioneChiamante + "&" + ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "="
						+ getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

				return IWebConstants.PG_MESSAGE;
			}

			setRequestAttribute("penaresidua", lPenaResMod);
		}

		if (lPenaResMod != null && lPenaResMod.getFlagValidato().equals("N"))
			setRequestAttribute("dataeditabile", "S");

		// Se posizione = 46 cerca i dati della SOSPENSIONE
		if (lPosizione != null && lPosizione.getCodPosizioneGiuridica() != null
		// LIBERO IN SOSPENSIONE
				&& lPosizione.getCodPosizioneGiuridica().equals("46")) {
			/******************************* SOSPENSIONE ***********************************/
			// Se è libero in sospensione mi aspetto una pena sospesa validata e sospesa
			// con il relativo record di sospensione, se non è l'ultima la cerco
			if (lPenaResMod.getFlagPenaSospesa() == null || (lPenaResMod.getFlagPenaSospesa() != null
					&& lPenaResMod.getFlagPenaSospesa().equals("N"))) {
				PenaResiduaModel lPenaResiduaSospesaValidata = lPenResCtrl
						.ExRicercaPenaResiduaUltimaValidataSospesa(lIdFascicolo);
				if (lPenaResiduaSospesaValidata == null)
					throw new SIEPException(SIEPException.USER_MESSAGE,
							"La posizione giuridica è LIBERO IN SOSPENSIONE ma non esiste una pena residua validata e sospesa. Impossibile eseguire la richiesta.");
			}
		}

		// misura alternativa
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();

		MisuraAlternativaModel lMisSospesa = null;
		if (!isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS)) {
			BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
					ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
			if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
				lMisSospesa = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);
		} else if ("MODIFICA".equals(tipoOperazione)) {
			// MEV_2019-09-SIEP: aggiunta nuova gestione per modifica
			IEvento ie = SICOLookupRemote.getEventoRemote();
			EventoModel em = null;
			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO)) {
				em = ie.ExRicercaEventoByKey(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
				lMisSospesa = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(em.getEveIdEvento());
			}
		}
		if (lMisSospesa != null) {
			UfficioModel lUffMod = new UfficioModel();
			lUffMod = lCtrlUffEmi.getUfficioByKey(lMisSospesa.getChiaveUfficioFascicoloSius());
			setRequestAttribute("UfficioEmittente", lUffMod);
			// MEV_2019-09-SIEP: aggiunto controllo per diversificare la setRequestAttribute
			if ("MODIFICA".equals(tipoOperazione)) {
				Collection<DecodificheModel> c = DecodificheManager.getInstance()
						.getTipoUfficioSiepTDSMUDSM();
				Object[] dms = c.toArray();
				for (int i = 0; i < dms.length; i++) {
					DecodificheModel dm = (DecodificheModel) dms[i];
					if ("UDS".equals(dm.getCode()))
						dm.setDescription("UFFICIO DI SORVEGLIANZA");
				}
				Option o = new Option(c, lUffMod.getCodTipoUfficio());
				setRequestAttribute("comboUfficioEmittenteModif", "" + o);
			}
		}
		setRequestAttribute("misuraalternativa", lMisSospesa);

		// tipo provvedimento
		Option lOption = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		lOption.setFilter(new String[] { "02", "03" }); // solo DECRETO o ORDINANZA
		// MEV_2019-09-SIEP: aggiunta impostazione per il campo tipo provvedimento
		if (!Utils.isNullObj(lMisSospesa))
			lOption.setSelected(lMisSospesa.getCodTipoDecisione());
		setRequestAttribute("tipoprovvedimento", "" + lOption);

		IMagistratoCompetente imc = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel mcmm = imc
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		// MEV_2019-09-SIEP: per la modifica (se non esiste il MAGISTRATO nel fascicolo ma solo nell'evento)
		// oppure se siamo in modifica
		if (Utils.isNullObj(mcmm) || "MODIFICA".equals(tipoOperazione)) {
			if (!Utils.isNullObj(lMisSospesa) && !Utils.isNullObj(lMisSospesa.getCodMagistrato())) {
				// Ricerca Magistrato
				IMagistrato im = SICOLookupRemote.getMagistratoRemote();
				MagistratoModel mm = im.ExRicercaMagistratoByCod(lMisSospesa.getCodMagistrato());
				if (!Utils.isNullObj(mcmm) && !Utils.isNullObj(mcmm.getMagistrato())
						&& !mm.getCodMagistrato().equals(mcmm.getMagistrato().getCodMagistrato())) {
					mcmm = new MagistratoCompetenteMagistratoModel();
					mcmm.setMagistrato(mm);
				}
			}
		}
		if (mcmm != null)
			setRequestAttribute("magistratocompetente", mcmm);

		// MEV_2019-09-SIEP: aggiunta ricerca evento notifica
		IEvento ie = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel enm = new EventoNotificaModel();
		if ("MODIFICA".equals(tipoOperazione)) {
			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO))
				try {
					enm = ie.ExRicercaEventoNotificaByKey(
							getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
				} catch (Exception e) {
					siesLogger.warn(getClass().getName() + ".processRequest: NON CI SONO NOTIFICHE PER EVENTO!");
					enm = new EventoNotificaModel();
					ArrayList<NotificaModel> lNotifiche = new ArrayList<NotificaModel>();
					enm.setNotifiche(lNotifiche.toArray(new NotificaModel[0]));
				}
			Hashtable<?, ?> lTable = ricercaNotifiche(enm.getNotifiche());
			// Autorita' esterna E
			AutoritaEsternaModel lAutE = null;
			if (lTable.get("AutE") != null)
				lAutE = ((NotificaModel) lTable.get("AutE")).getAutoritaEsterna();
			// setRequestAttribute("autoritaEsternaE", lAutE);
			Option lOptionAutoritaE = null;
			if (lAutE != null && lAutE.getCodTipoAutorita() != null)
				lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita(),
						lAutE.getCodTipoAutorita());
			else
				lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
			setRequestAttribute("codiceAutorita", "" + lOptionAutoritaE);

			// Autorita' esterna N
			// AutoritaEsternaModel lAutN = null;
			// if (lTable.get("AutN") != null) {
			// lAutN = ((NotificaModel) lTable.get("AutN")).getAutoritaEsterna();
			// setRequestAttribute("autoritaEsternaN", lAutN);
			// }
			// Option lOptionAutoritaN = new Option(DecodificheManager.getInstance().getTipoAutorita());
			// setRequestAttribute("codiceAutoritaN", "" + lOptionAutoritaN);

			// Autorita' esterna C
			// AutoritaEsternaModel lAutC = null;
			// if (lTable.get("AutC") != null)
			// lAutC = ((NotificaModel) lTable.get("AutC")).getAutoritaEsterna();
			// setRequestAttribute("autoritaEsternaC", lAutC);
			// Option lOptionAutoritaC = null;
			// if (lAutC != null && lAutC.getCodTipoAutorita() != null)
			// lOptionAutoritaC = new Option(DecodificheManager.getInstance().getTipoAutorita(),
			// lAutC.getCodTipoAutorita());
			// else
			// lOptionAutoritaC = new Option(DecodificheManager.getInstance().getTipoAutorita());
			// setRequestAttribute("codiceAutoritaC", "" + lOptionAutoritaC);

			// Cssa
			// if (lTable.get("NotCssa") != null) {
			// setRequestAttribute("daticssa", ((NotificaModel) lTable.get("NotCssa")).getCSSA());
			// if ("MODIFICA".equals(tipoOperazione)) {
			// Collection<DecodificheModel> c = DecodificheManager.getInstance()
			// .getTipoUffEsePenEstSerSocMin();
			// Object[] dms = c.toArray();
			// for (int i = 0; i < dms.length; i++) {
			// DecodificheModel dm = (DecodificheModel) dms[i];
			// if ("UEPE".equals(dm.getCode()))
			// dm.setDescription("UEPE");
			// else if ("USSM".equals(dm.getCode()))
			// dm.setDescription("USSM");
			// else if ("UEPESS".equals(dm.getCode()))
			// dm.setDescription("UEPESS");
			// else if ("USSMSS".equals(dm.getCode()))
			// dm.setDescription("USSMSS");
			// }
			// Option o = new Option(c, ((NotificaModel) lTable.get("NotCssa")).getCSSA().getTipo());
			// String[] lFiltro = new String[3];
			// lFiltro[0] = "-";
			// lFiltro[1] = "UEPE";
			// lFiltro[2] = "USSM";
			// o.setFilter(lFiltro);
			// setRequestAttribute("comboCSSATrattinoModif", "" + o);
			// }
			// }

			// Ufficio TDS
			// String UffTDS = null;
			// if (lTable.get("UffTDS") != null) {
			// UffTDS = ((NotificaModel) lTable.get("UffTDS")).getUfficio().getDescrComune();
			// setRequestAttribute("UffTDS", UffTDS);
			// }

			// Ufficio UDS
			// String UffUDS = null;
			// if (lTable.get("UffUDS") != null) {
			// UffUDS = ((NotificaModel) lTable.get("UffUDS")).getUfficio().getDescrComune();
			// setRequestAttribute("UffUDS", UffUDS);
			// }

			// istituto
			// String Istituto = null;
			// String LuogoIstituto = null;
			// if (lTable.get("lNotIstituto") != null) {
			// Istituto = ((NotificaModel) lTable.get("lNotIstituto")).getIstitutoDetenzione()
			// .getDescrTipoIstituto();
			// LuogoIstituto = ((NotificaModel) lTable.get("lNotIstituto")).getIstitutoDetenzione()
			// .getDescrComune();
			// setRequestAttribute("Istituto", Istituto);
			// setRequestAttribute("LuogoIstituto", LuogoIstituto);
			// }

			// if (lAutN != null && lAutN.getCodTipoAutorita() != null)
			// lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(),
			// lAutN.getCodTipoAutorita());
			// else
			// lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
			// setRequestAttribute("autoritaEsternaAvv", "" + lOption);
		}
		setRequestAttribute("eventonotifica", enm);
		// FINE MEV_2019-09

		// Autorità esterna
		Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutorita", "" + lOptionAutoritaE);

		// STUB 14/12/2005 Si imposta il Tipo Ufficio SIUS.
		Option lOptionSIUS = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
		setRequestAttribute("tipoUfficioSIUS", "" + lOptionSIUS);

		// OGGETTO DECISIONE
		Option lOptionOggetto = null;
		// MEV_2019-09-SIEP: si differenzia per PM e PMM
		if (isUfficioMinorenni())
			lOptionOggetto = new Option(DecodificheManager.getInstance().getOggettoDecisioneMinor());
		else
			lOptionOggetto = new Option(DecodificheManager.getInstance().getOggettoDecisione());
		// MEV_2019-09-SIEP: aggiunta impostazione per il campo codice motivo
		if (!Utils.isNullObj(lMisSospesa))
			lOptionOggetto.setSelected(lMisSospesa.getCodTipoMisura());
		// FINE MEV_2019-09-SIEP
		setRequestAttribute("motivoProvv", "" + lOptionOggetto);

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", getFiltroMinorenni());

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		// restituisce la jsp di VIEW
		return PG_LOAD_INSERISCI_SOSPENSIONE_DECISIONI_SORVEGLIANZA;
	}

	/**
	 * ricerca notifiche
	 *
	 * @param aNotifiche
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
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

}