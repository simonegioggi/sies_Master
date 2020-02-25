package siap.sige.impugnazione.action;

import java.util.Collection;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.impugnazione.controller.IImpugnazioneSige;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciImpugnazioneSige
 * </p>
 * <p>
 * Description: Classe Action per la load di inserisci Impugnazione Sige
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
@SuppressWarnings("unchecked")
public class ActLoadInserisciEsitoImpugnazioneSige extends ActionSige implements ICostantiImpugnazioneSige {

	protected FascicoloSigeEstesoModel mFasEsteso;
	protected EventoNotificaModel eventoNotificaModel;

	public String processRequest() throws Exception {

		// this.gestioneRitorno();

		// Fascicolo Sige Esteso in sessione.
		mFasEsteso = getFascicoloSigeEstesoInSessione();

		IImpugnazioneSige lCtrlImp = SIGELookupRemote.getImpugnazioneSigeRemote();
		ImpugnazioneSigeModel impugnazione = lCtrlImp
				.ExRicercaImpugnazioneByKey(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
		String lCodTipoImpugnazione = impugnazione.getCodTipoImpugnazione();
		ProvvedimentoSigeEventoModel lPSMod = impugnazione.getProvvedimentoSige();

		if (!Utils.isNullObj(impugnazione)) {
			// Lock
			LockModel lck = LockController.lockIfNotLocked(getServletContext(), "Impugnazione", impugnazione
					.getIdImpugnazioneSige().toString(), getCodUtenteConnesso(), getSession().getId());
			if (lck != null) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il/la  " + lck.getEntity()
						+ " è in gestione ad un altro utente!<BR />Riprovare più tardi !");
				return IWebConstants.PG_MESSAGE;
			}

			// Si passa nella request l'impugnazione.
			setRequestAttribute("impugnazione", impugnazione);
			setRequestAttribute("modalita", "M");

		}

		// Verifica sullo Scadenzario se il procedimento è impugnabile.
		// IScadenzarioSige lCtrl = SIGELookupRemote.getScadenzarioSigeRemote();
		// lScaMod = lCtrl.ExRicercaScadenzarioSigeByIdFascicoloTipo
		// (lFasEst.getFascicoloSige().getIdFascicoloSige(), ICostantiImpugnazioneSige.COD_TIPO_SCADENZARIO);

		// STUB 25/05/2004 Commentate le eccezioni sollevate per mancanza scadenzario.
		/**
		 * Commentato il blocco da scadenzario if(Utils.isNullObj(lScaMod)) throw new F3BException(
		 * F3BException.USER_MESSAGE, "Procedimento non Impugnabile" );
		 * if(Utils.isNullObj(lScaMod.getDataFineScadenza())) throw new F3BException(
		 * F3BException.USER_MESSAGE, "Procedimento non Impugnabile" ); else if
		 * (lScaMod.getDataFineScadenza().before( DateUtils.getSysDate()) ) throw new F3BException(
		 * F3BException.USER_MESSAGE, "Procedimento non Impugnabile: data ricorso scaduta. " );
		 **/
		// Si passa nella request lo scadenzario l'evento e il Decreto/DepositoOrdinanzaPc.

		// setRequestAttribute("scadenzario", lScaMod);
		setRequestAttribute("provvedimento", lPSMod);
		// String aNumProvv=
		// lPSMod.getProvvedimento().getChiaveAnno().toString()+"/"+lPSMod.getProvvedimento().getChiaveProgr().toString();
		// String aDataProvv = DateUtils.getDateToString(lPSMod.getProvvedimento().getDataEmissione(),
		// "dd-MM-yyyy");
		// String aDataDeposito = DateUtils.getDateToString(lPSMod.getProvvedimento().getDataDeposito(),
		// "dd-MM-yyyy");

		// setRequestAttribute("numProvv", aNumProvv );
		// setRequestAttribute("dataProvv", aDataProvv );
		// setRequestAttribute("dataDeposito", aDataDeposito );

		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		String codEsitoTenore = (impugnazione.getCodTenoreDecisione() == null ? "-" : impugnazione
				.getCodTenoreDecisione());

		Option lOption = new Option(DecodificheManager.getInstance().getTenoreDecisioneRicorsoSige(),
				codEsitoTenore);

		String[] lFilterTipoImp = { "-", "01", "02", "03", "04", "05", "06", "07", "08", "12" };

		if (lCodTipoImpugnazione.equals(COD_TIPO_OPPOSIZIONE)) {
			lFilterTipoImp = new String[] { "-", "10", "13", "05", "11" };
		}
		lOption.setFilter(lFilterTipoImp);

		setRequestAttribute("tenoreDecisioneRicorso", "" + lOption);
		setRequestAttribute("ListaUffici", "" + lOption);
		setRequestAttribute("tipoUfficio", strCodTipoUfficio);

		// Imposta ComboBOX Autorita Destinataria
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
		String[] lFilterCSS = { "CSS" };
		lOption.setFilter(lFilterCSS);
		setRequestAttribute("ListaUffici", "" + lOption);

		Option optUfficiRecuperCrediti = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-",
				75);
		optUfficiRecuperCrediti.setFilter(new String[] { "-", "37", "54", "57", "98" });
		optUfficiRecuperCrediti.setSelected(this.getTipoUfficioRecuperoCrediti(impugnazione.getNotifiche()));

		setRequestAttribute("ufficiRecuperoCrediti", optUfficiRecuperCrediti.toString());

		Option lOptionUffici = new Option(DecodificheManager.getInstance().getAutoritaCompetente());
		lOptionUffici.setFilter(new String[] { "-", "PGCAP", "PM", "PMM" });
		lOptionUffici.setSelected(this.getTipoUfficioPubblicoMinistero(impugnazione.getNotifiche()));
		setRequestAttribute("ufficiPubbliciMinisteri", lOptionUffici.toString());
		String sedePubblicoMinistero = this.getSedeUfficioPubblicoMinistero(impugnazione.getNotifiche());
		String sedeUfficioRecuperoCrediti = this.getSedeUfficioRecuperoCrediti(impugnazione.getNotifiche());

		setRequestAttribute("sedeRecuperoCrediti", sedeUfficioRecuperoCrediti);
		setRequestAttribute("sedePubblicoMinistero", sedePubblicoMinistero);

		setSessionAttribute("codTipoImpugnazione", impugnazione.getCodTipoImpugnazione());

		// ***************************** INIZIO MODIFICA 17/03/2017
		EventoNotificaModel eventoNotificaModel = null;
		ProvvedimentoSigeEventoModel provvSigeEveMod = null;

		// Codice Tipo Ufficio dell'utente connesso.
		UfficioModel um = getUfficioUtenteConnesso();
		// String lCodTipoUfficio = um.getCodTipoUfficio();

		// Evento notifica Model.
		EventoNotificaModel lEve = new EventoNotificaModel();

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		// BigDecimal idEvento =
		// impugnazione.getProvvedimentoSigeGenerato().getProvvedimento().getIdEventoGenerato();
		if (impugnazione.getProvvedimentoSigeGenerato() != null
				&& impugnazione.getProvvedimentoSigeGenerato().getProvvedimento().getIdEventoGenerato() != null) {
			// if(lPSMod != null && lPSMod.getEventoNotifica() != null &&
			// lPSMod.getEventoNotifica().getEvento() != null){
			// eventoNotificaModel =
			// lCtrlEvento.ExRicercaEventoNotificaByKey(lPSMod.getEventoNotifica().getEvento().getIdEvento());
			eventoNotificaModel = lCtrlEvento.ExRicercaEventoNotificaByKey(impugnazione
					.getProvvedimentoSigeGenerato().getProvvedimento().getIdEventoGenerato());
		}

		setRequestAttribute("evento", lEve);
		setRequestAttribute("eventoNotifica", eventoNotificaModel);
		setRequestAttribute("provvedimentoSige", provvSigeEveMod);
		setRequestAttribute("indirizzoUfficio", um.getIndirizzo());

		FascicoloSigeUtils lFasSigeUtils = new FascicoloSigeUtils();
		// Carica Tipo Destinatario in base al Tipo Ufficio.
		String strTipoDest = lFasSigeUtils.leggiTipoDestinatario(this.getUfficioUtenteConnesso()
				.getCodTipoUfficio());
		setRequestAttribute("TipoDest", strTipoDest);

		// Avvocati attuali assegnati al fascicolo SIGE.
		Vector<Object> lAvvocati = lFasSigeUtils.ricercaAvvocati(mFasEsteso.getFascicoloSige()
				.getIdFascicoloSige());
		if (lAvvocati.size() > 0)
			setRequestAttribute("avvocato", lAvvocati);

		Vector<NotificaModel> vectNotAvv = new Vector<NotificaModel>();
		NotificaModel notSogg = new NotificaModel();
		NotificaModel notAltro = new NotificaModel();
		String notificaComunicazione = "";
		String chooseSogg = "";
		String chooseAltro = "";
		if (eventoNotificaModel != null && eventoNotificaModel.getNotifiche() != null) {
			NotificaModel[] notList = eventoNotificaModel.getNotifiche();
			for (NotificaModel nm : notList) {
				if (nm.getAvvIdAvvocatoFascicoloSige() != null) {
					// notifica x avvocato
					vectNotAvv.add(nm);
				} else if (nm.getSogIdSoggetto() != null) {
					// notifica x soggetto
					notSogg = nm;
					if (nm.getAutoritaEsterna() != null
							&& nm.getAutoritaEsterna().getCodTipoAutorita() != null
							&& !"".equals(nm.getAutoritaEsterna().getCodTipoAutorita())) {
						// soggetto con autorita' esterna
						chooseSogg = nm.getAutoritaEsterna().getCodTipoAutorita();
					} else if (nm.getIstDetIdIstitutoDetenzione() != null
							&& !"".equals(nm.getIstDetIdIstitutoDetenzione())) {
						// soggetto con id

					}
				} else if (nm.getAutoritaEsterna() != null
						&& nm.getAutoritaEsterna().getCodTipoAutorita() != null
						&& !"".equals(nm.getAutoritaEsterna().getCodTipoAutorita())) {
					// notifica x altro destinatario
					notAltro = nm;
					chooseAltro = nm.getAutoritaEsterna().getCodTipoAutorita();
				} else if (ICostantiUdienzaSige.CODTIPONOTIFICACOMUNICAZIONE.equals(nm.getCodTipoNotifica())) {
					notificaComunicazione = "checked";
				}
			}
		}
		setRequestAttribute("notificaComunicazione", notificaComunicazione);
		setRequestAttribute("vectNotAvv", vectNotAvv);
		setRequestAttribute("notSogg", notSogg);
		setRequestAttribute("notAltro", notAltro);
		
		//@emma 20072018 intervento post COLLAUDO 11.2 
		// recupero la liste delle autorità tranne quelle con codice 54 e 55 che sono duplicate in BANCA DATI
		Collection<DecodificheModel> tipoAutorita = null;
	    DecodificheModel lModel = new DecodificheModel();
	    IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
	    lModel.setContesto("TIPO_AUTORITA");
	    String[] autNotSelect = new String[]{"54", "55"};
	    tipoAutorita = lDecodifiche.ricercaAllTipoAutoritaNotIn(autNotSelect);

		Option otpSogg = new Option(tipoAutorita, chooseSogg, 75);
		Option otpAltro = new Option(tipoAutorita, chooseAltro, 75);
		setRequestAttribute("tipoAutoritaSogg", otpSogg.toString());
		setRequestAttribute("tipoAutoritaAltro", otpAltro.toString());

		Collection<DecodificheModel> collTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
		setRequestAttribute("TipiIstitutiColl", collTipoIstituto);

		return getPage(impugnazione);
	}

	private String getPage(ImpugnazioneSigeModel impugnazione) {
		String page = PG_LOAD_INSERISCI_ESITO_IMPUGNAZIONESIGE;
		if (impugnazione.getProvvedimentoSigeGenerato() != null) {
			/*
			 * EventoModel evento=impugnazione.getProvvedimentoSigeGenerato().getEventoNotifica().getEvento();
			 * if ("S".equalsIgnoreCase(evento.getFlagDocumentoRegistrato()) &&
			 * impugnazione.getCodTenoreDecisione() != null &&
			 * !impugnazione.getCodTenoreDecisione().equals("")) { RedirectTo redirect = new RedirectTo();
			 * redirect.setPage(IWebConstants.PG_MAIN); redirect.setAction(
			 * "siap.sige.impugnazione.action.ActLoadDettaglioImpugnazioneSige" );
			 * redirect.setParameter(CAMPO_ID_IMPUGNAZIONE,
			 * (impugnazione.getIdImpugnazioneSige().toString())); page=redirect.toString(); }
			 */
		}
		return page;
	}

	private String getTipoUfficioPubblicoMinistero(Vector<NotificaModel> notifiche) {
		String codiceUfficioPubblicoMinistero = "-";
		for (NotificaModel notifica : notifiche) {
			if (notifica.getUfficio() != null) {
				codiceUfficioPubblicoMinistero = notifica.getUfficio().getCodTipoUfficio();
			}
		}

		return codiceUfficioPubblicoMinistero;
	}

	private String getSedeUfficioPubblicoMinistero(Vector<NotificaModel> notifiche) {
		String sedeUfficioPubblicoMinistero = "";
		for (NotificaModel notifica : notifiche) {
			if (notifica.getUfficio() != null) {
				sedeUfficioPubblicoMinistero = notifica.getUfficio().getDescrComune();
			}
		}
		return sedeUfficioPubblicoMinistero;
	}

	private String getTipoUfficioRecuperoCrediti(Vector<NotificaModel> notifiche) {
		String tipoUfficioRecuperoCrediti = "-";
		for (NotificaModel notifica : notifiche) {
			if (notifica.getAutoritaEsterna() != null) {
				tipoUfficioRecuperoCrediti = notifica.getAutoritaEsterna().getCodTipoAutorita();
			}
		}
		return tipoUfficioRecuperoCrediti;
	}

	private String getSedeUfficioRecuperoCrediti(Vector<NotificaModel> notifiche) {
		String sedeUfficioRecuperoCrediti = "-";
		for (NotificaModel notifica : notifiche) {
			if (notifica.getAutoritaEsterna() != null) {
				sedeUfficioRecuperoCrediti = notifica.getAutoritaEsterna().getDescrSede();
			}
		}
		return sedeUfficioRecuperoCrediti;
	}

}
