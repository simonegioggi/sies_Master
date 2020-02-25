package siap.sige.udienza.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.SIGEException;
import siap.sige.aula.controller.IAula;
import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.detenzione.controller.IFasSigeDetenzione;
import siap.sige.detenzione.model.FasSigeDetenzioneModel;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.magistratoassegnatario.controller.IMagistratoAssegnatario;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.sezione.controller.ISezione;
import siap.sige.sezione.model.SezioneModel;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeEstesoModel;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciFissazioneUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load della form di inserimento Fissazione Udienza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciFissazioneUdienza extends ActRicercaFSigePuntuale implements
		ICostantiUdienzaSige, ICostantiCollegio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	String mRetPage = PG_LOAD_INSERISCIFISSAZIONEUDIENZA;
	String modalita = "";
	String lTipoGiudizio = "-";
	String lSezioneUdienza = "-";
	AulaUdienzaModel aulaUdienza = null;
	EventoNotificaModel eventoNotificaModel = null;
	ProvvedimentoSigeEventoModel provvSigeEveMod = null;

	protected FascicoloSigeEstesoModel mFasEsteso;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("inizio - processRequest di ActLoadInserisciFissazioneUdienza");

		// Gestione pulsante di ritorno.
		setLinkRitorno();

		// Se si proviene dal menu' s'invoca processRequest della supercalsse.
		if (isRequestParameterNullObj("ritorno")) {
			// Invoca la process Request della superclasse se provengo dal menu'.
			// complete = false;
			super.processRequest();
		}

		// Fascicolo Sige Esteso in sessione.
		mFasEsteso = getFascicoloSigeEstesoInSessione();
		BigDecimal lIdFasSige = mFasEsteso.getFascicoloSige().getIdFascicoloSige();

		// Codice Tipo Ufficio dell'utente connesso.
		UfficioModel um = getUfficioUtenteConnesso();
		String lCodTipoUfficio = um.getCodTipoUfficio();
		modalita = "I";

		// 20171004: [SG] controllo se esiste sezione per ufficio
		SezioneModel lSezMod = new SezioneModel();
		lSezMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		lSezMod.setCodice("");
		lSezMod.setDescrizione("");
		ISezione iSezione = SIGELookupRemote.getSezioneRemote();
		Vector sezioni = null;
		try {
			sezioni = iSezione.ExRicercaSezione(lSezMod);
		} catch (Exception ex) {
			siesLogger.info("UFFICIO SENZA SEZIONI!");
			sezioni = new Vector();
		}
		setRequestAttribute("ufficioConSezioni", "" + !sezioni.isEmpty());

		// Gestione Accoglimento Opposizione:
		Boolean esisteImpugnazioniProvvedimento = checkImpugnazioniProvvedimento(mFasEsteso);

		// Se esiste una Udienza gia' fissata per il Fascicolo
		if (analisiUdienzeProcedimento(lIdFasSige)) {
			// Se esisteImpugnazioniProvvedimento=true (CodStatoFascicolo=="07" && CodTenoreDecisione=="10")
			// ==> Opposizione accolta non devo bloccare ==> NO MESSAGGIO
			if (IsFascicoloSigeIscrittoCompetenza() == false && !esisteImpugnazioniProvvedimento)
				throw new SIGEException(SIGEException.USER_MESSAGE,
						ICostantiFascicoloSige.MSG_NON_MODIFICABILE);

			// Lock per evitare la fissazione contemporanea di 2 Udienze per lo stesso fascicolo
			LockModel lck = LockController.lockIfNotLocked(getServletContext(), "ProcedimentoSIGE",
					lIdFasSige.toString(), getCodUtenteConnesso(), getSession().getId());
			if (lck != null) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il " + lck.getEntity()
						+ " e' in gestione ad un altro utente!<BR>Riprovare piu' tardi!");
				return IWebConstants.PG_MESSAGE;
			}

			// Solo la prima volta vengono messi i Tenori in sessione
			if (isRequestParameterNullObj(IWebConstants.FLAG_RITORNO)) {
				// Ricerca tenori presenti
				ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();
				TenoreSigeModel aTenore = new TenoreSigeModel();
				aTenore.setFasIdFascicoloSige(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
				Vector<TenoreSigeEstesoModel> lTenoriEstesi = lCtrl.ExRicercaTenoriEstesiAttivi(aTenore);
				// MERGE v10: aggiunto segmento di codice come in inserimento emissione ordinanza
				// in caso di assenza di tenori attivi, si ripristinano quelli della richiesta SIGE
				if (lTenoriEstesi.size() == 0 && mFasEsteso.getRichiestaSige() != null)
					lTenoriEstesi = lCtrl.ExRicercaTenoreEstesoByRichiesta(mFasEsteso.getRichiestaSige()
							.getIdRichiestaSige());

				setSessionAttribute("tenori", lTenoriEstesi);
				setRequestAttribute("tenori", lTenoriEstesi);
			}

			// Avvocati attuali assegnati al fascicolo SIGE.
			FascicoloSigeUtils lFasSigeUtils = new FascicoloSigeUtils();
			Vector<Object> lAvvocati = lFasSigeUtils.ricercaAvvocati(mFasEsteso.getFascicoloSige()
					.getIdFascicoloSige());
			if (lAvvocati.size() > 0)
				setRequestAttribute("avvocato", lAvvocati);

			// Magistrato Assegnatario
			// MagistratoAssegnatarioModel lMagAss = mFasEsteso.getMagAssegnatario();
			// setRequestAttribute("magistratoassegnatario", lMagAss);
			IMagistratoAssegnatario lMagCtrl = SIGELookupRemote.getMagistratoAssegnatarioRemote();
			MagistratoAssegnatarioModel lMagAss = lMagCtrl.ExRicercaEstesaMagAssCorrenteXFascicolo(mFasEsteso
					.getFascicoloSige().getIdFascicoloSige());
			setRequestAttribute("magistratoassegnatario", lMagAss);

			// Ricerca LUOGO DETENZIONE
			IFasSigeDetenzione lDetenzioneCtrl = SIGELookupRemote.getFasSigeDetenzioneRemote();
			FasSigeDetenzioneModel lDetenzione = lDetenzioneCtrl
					.ExRicercaUltimaDetenzioneFascicolo(lIdFasSige);
			setRequestAttribute("detenzione", lDetenzione);

			// il tipo giudizio va definito quando si definisce l'udienza
			if (lTipoGiudizio == null || "".equals(lTipoGiudizio) || "-".equals(lTipoGiudizio)) {
				// altrimenti si verifica quello in precedenza selezionato nella definizione del procedimento
				lTipoGiudizio = (mFasEsteso.getFascicoloSige().getCodTipoGiudizio() == null ? "-"
						: mFasEsteso.getFascicoloSige().getCodTipoGiudizio().trim());
			}
			// Carica Combo x TipoGiudizio.
			Option lOptionGiudizio = getComboTipoGiudizio(lTipoGiudizio, lCodTipoUfficio);

			setRequestAttribute("tipoGiudizioVal", lTipoGiudizio);
			setRequestAttribute("tipoGiudizio", lOptionGiudizio.toString());

			// Preleva elenco degli altri destinatari.
			Option lOptionAut = new Option();
			lOptionAut = new Option(DecodificheManager.getInstance().getTipoAutorita(), 75);

			// 20171016: [SG] cambiata gestione, prendo la sezione dal fascicolo sige in sessione
			// if (lSezioneUdienza == null || "".equals(lSezioneUdienza) || "-".equals(lSezioneUdienza)) {
			// lSezioneUdienza = "-";
			// if (lMagAss != null) {
			// IMagistrato lCtrl = SIGELookupRemote.getMagistratoRemote();
			// // 20170918: [SG] aggiunto parametro di passaggio poichè il magistrato può essere inserito
			// // da un ufficio differente da quello in cui ha delle udienze poichè trasferito
			// MagistratoModel lMagMod = lCtrl.ExRicercaMagistratoByCod(lMagAss.getMagCodMagistrato(),
			// getCodUfficioUtenteConnesso());
			// if (lMagMod.getMagistratoSezioni().length > 0) {
			// BigDecimal idSezMag = lMagMod.getMagistratoSezioni()[0].getSezIdSezione();
			// if (idSezMag != null) {
			// lSezioneUdienza = idSezMag.toString();
			// }
			// }
			// }
			// }
			if (lSezioneUdienza == null || "".equals(lSezioneUdienza) || "-".equals(lSezioneUdienza)) {
			if (mFasEsteso.getFascicoloSige() != null && mFasEsteso.getFascicoloSige().getIdSezione() != null)
				lSezioneUdienza = "" + mFasEsteso.getFascicoloSige().getIdSezione();
			else
				lSezioneUdienza = "-";
			}
			Option lOptionSezioniUdienza = new Option(
					SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), lSezioneUdienza,
					Option.BLANK_ITEM);
			lOptionSezioniUdienza.setValueBlankItem("-");
			if(lSezioneUdienza!=null)
				lOptionSezioniUdienza.setSelected(lSezioneUdienza);
			
			setRequestAttribute("elencoSezioniUdienza", lOptionSezioniUdienza.toString());

			if (aulaUdienza == null)
				aulaUdienza = getAulaPredefinita(lSezioneUdienza);
			setRequestAttribute("aulaUdienza", aulaUdienza);

			// Carica Tipo Destinatario in base al Tipo Ufficio.
			String strTipoDest = lFasSigeUtils.leggiTipoDestinatario(this.getUfficioUtenteConnesso()
					.getCodTipoUfficio());
			setRequestAttribute("TipoDest", strTipoDest);

			// LISTA UFFICI SOGGETTO
			Option lOptionSog = null;
			if (lDetenzione != null
					&& lDetenzione.getLuogoDetenzione() != null
					&& lDetenzione.getLuogoDetenzione().getIstitutoDetenzione() != null
					&& lDetenzione.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto().length() > 0)
				lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(), lDetenzione
						.getLuogoDetenzione().getIstitutoDetenzione().getCodTipoIstituto(), 75);
			else
				lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(), 75);

			// LISTA UFFICI
			Collection<DecodificheModel> lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
			String[] lStringFilter = { "-", "22" };
			Option lOptionAvv = new Option(lTipoIstituto, "22", 75);
			lOptionAvv.setFilter(lStringFilter);
			Collection<DecodificheModel> collTipoIstituto = DecodificheManager.getInstance()
					.getTipoAutorita();

			// Evento notifica Model.
			EventoNotificaModel lEve = new EventoNotificaModel();
			setRequestAttribute("evento", lEve);
			setRequestAttribute("eventoNotifica", eventoNotificaModel);
			setRequestAttribute("provvedimentoSige", provvSigeEveMod);
			setRequestAttribute("indirizzoUfficio", um.getIndirizzo());

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
							&& !"".equals(nm.getAutoritaEsterna().getCodTipoAutorita()) && nm.getIdParteUdienza()==null) {
						// notifica x altro destinatario che non sia però per parti udienza!!!
						notAltro = nm;
						chooseAltro = nm.getAutoritaEsterna().getCodTipoAutorita();
					} else if (CODTIPONOTIFICACOMUNICAZIONE.equals(nm.getCodTipoNotifica())) {
						notificaComunicazione = "checked";
					}
				}
			}
			setRequestAttribute("notificaComunicazione", notificaComunicazione);
			setRequestAttribute("vectNotAvv", vectNotAvv);
			setRequestAttribute("notSogg", notSogg);
			setRequestAttribute("notAltro", notAltro);

			Option otpSogg = new Option(DecodificheManager.getInstance().getTipoAutorita(), chooseSogg, 75);
			Option otpAltro = new Option(DecodificheManager.getInstance().getTipoAutorita(), chooseAltro, 75);
			setRequestAttribute("tipoAutoritaSogg", otpSogg.toString());
			setRequestAttribute("tipoAutoritaAltro", otpAltro.toString());

			setRequestAttribute("luogodet", lDetenzione);
			setRequestAttribute("TipiIstituti1", "" + lOptionAvv);
			setRequestAttribute("TipiIstitutiColl", collTipoIstituto);
			setRequestAttribute("TipiIstitutiSog", "" + lOptionSog);
			setRequestAttribute("tipoAutorita", lOptionAut.toString());
			setRequestAttribute("modalita", "I");
			setRequestAttribute("modalita", modalita);

			// String lIdUdienzaSige = null;
			// if (!isRequestParameterNullObj("IdUdienzaSige")) {
			// lIdUdienzaSige = getParameter("IdUdienzaSige");
			// setRequestAttribute("UdienzaSige", getUdienzaSige(lIdUdienzaSige));
			// }
			// 20190519 [SG]: aggiunta gestione idUdienzaSige
			if (!isRequestParameterNullEmptyObj("idUdiSig")) {
				UdienzaSigeModel udiSige = getUdienzaSige(getRequestStringParameter("idUdiSig"));
				setRequestAttribute("UdienzaSige", udiSige);
			}

			// Verifico se provengo dalla pagina di "Inserimento Esito Impugnazione
			// Sige" per poter gestire il bottone di ritorno nella pagina
			// di Inserimento Fissazione Udienza
			if (!isRequestParameterNullObj("provenienza")) {
				String provenienza = getRequestStringParameter("provenienza");
				if (provenienza != null && !provenienza.equals("") && provenienza.equals("EsitoImpugnazione")) {
					setRequestAttribute("provEsitoImpugnaz", provenienza);
				}
			}
		} // endif analisiUdienzeProcedimento

		// info per il log
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");
		// valore di ritorno
		return mRetPage;
	}

	private String getIdSezioneUdienzaSige() {
		String ret = null;
		try {
			String uid = mFasEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige().toString();
			UdienzaSigeModel usm = getUdienzaSige(uid);
			ret = usm.getCodIdSezioneUdienza().toString();
		} catch (Exception e) {
		}
		return ret;
	}

	private AulaUdienzaModel getAulaUdienzaSige(String sezione) {
		AulaUdienzaModel aulaUdienza = null;
		try {
			BigDecimal idSezione = new BigDecimal(sezione);
			String uid = mFasEsteso.getUdienzaProcedimento().getUdiIdUdienzaSige().toString();
			UdienzaSigeModel usm = getUdienzaSige(uid);
			BigDecimal idAula = usm.getCodIdAulaUdienza();
			IAula lCtrl = SIGELookupRemote.getAulaRemote();
			aulaUdienza = lCtrl.ExRicercaAulaByKey(idAula, idSezione);
		} catch (Exception e) {
		}
		return aulaUdienza;
	}

	private AulaUdienzaModel getAulaPredefinita(String sezione) {
		Vector<AulaUdienzaModel> aulaUdienzaVect = null;
		AulaUdienzaModel aulaUdienza = null;
		try {
			BigDecimal idSezione = new BigDecimal(sezione);
			IAula lCtrl = SIGELookupRemote.getAulaRemote();
			aulaUdienzaVect = lCtrl.ExRicercaAulaByIdSezione(idSezione);
			for (AulaUdienzaModel aula : aulaUdienzaVect) {
				if ("S".equals(aula.getFlagPredefinita())) {
					aulaUdienza = aula;
				}
			}
		} catch (Exception e) {
		}
		return aulaUdienza;
	}

	private void loadForm() throws Exception {

		if (!isRequestParameterNullObj(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE)) {
			// Se nella request c'e' l'ID di CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE siamo nel caso di una
			// Rifissazione

			// l'ID_UDIENZA_PROCEDIMENTO_SIGE viene passato nella request.
			String[] idUdienza = super.getRequest().getParameterValues(
					ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE);

			// BigDecimal idUdienzaProcedimentoSige =
			// getRequestBigDecimalParameter(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE);
			BigDecimal idUdienzaProcedimentoSige = new BigDecimal(idUdienza[idUdienza.length - 1]);
			setRequestAttribute(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE,
					idUdienzaProcedimentoSige.toString());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Rifissazione Udienza" + idUdienzaProcedimentoSige);

			// Preventivamente si controlla l'esistenza di una Udienza per il fascicolo
			IUdienzaProcedimentoSige lUdiProCtrl = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
			UdienzaProcedimentoSigeModel lUdiProSige = lUdiProCtrl
					.ExRicercaUdienzaProcedimentoSigeByKey(idUdienzaProcedimentoSige);
			if (lUdiProSige != null) {
				// caso di rifissazione
				modalita = "R";

				// Se trovato UDIENZA_PROCEDIMENTO_SIGE si ricava l'ID Udienza e l'ID Evento
				BigDecimal lIdEvento = lUdiProSige.getEveIdEvento();
				setRequestAttribute("IdEvento", lIdEvento.toString());

				IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
				eventoNotificaModel = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);

				IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
				provvSigeEveMod = lCtrlProv.ExRicercaProvvedimentoByIdEvento(lIdEvento);

				BigDecimal mIdUdienzaSige = lUdiProSige.getUdiIdUdienzaSige();

				// chiama il controller
				IPartiUdienza lCtrl = SIGELookupRemote.getPartiUdienzaRemote();
				Vector<AnagraficaPartiUdienzaModel> lPartiC = lCtrl.ExRicercaPartiUdienzaByIdUdienza(
						idUdienzaProcedimentoSige, "C");
				setRequestAttribute("udienzaPartiC", lPartiC);

				Vector<AnagraficaPartiUdienzaModel> lPartiO = lCtrl.ExRicercaPartiUdienzaByIdUdienza(
						idUdienzaProcedimentoSige, "O");
				setRequestAttribute("udienzaPartiO", lPartiO);

				if (mIdUdienzaSige != null) {
					// potrebbe essere il caso di un collegio in modifica
					String sIdUdienzaSige = mIdUdienzaSige.toString();
					setRequestAttribute("UdienzaSige", getUdienzaSige(sIdUdienzaSige));
					setRequestAttribute("IdUdienzaSige", mIdUdienzaSige.toString());
				} else if (!isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
					// potrebbe essere il caso di un collegio creato dopo aver fissato l'udienza
					// Se nella request e' flaggato il FORM_DEF_COLLEGIO siamo nel caso di un inserimento
					// udienza diretto
					lTipoGiudizio = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO);

					// BigDecimal lIdUdienzaSige = getRequestBigDecimalParameter(CAMPO_ID_UDIENZA_SIGE);

					idUdienza = super.getRequest().getParameterValues(CAMPO_ID_UDIENZA_SIGE);
					BigDecimal lIdUdienzaSige = new BigDecimal(idUdienza[idUdienza.length - 1]);
					String sIdUdienzaSige = lIdUdienzaSige.toString();
					UdienzaSigeModel udiSige = getUdienzaSige(sIdUdienzaSige);
					setRequestAttribute("UdienzaSige", udiSige);

					if ("M".equals(lTipoGiudizio) && udiSige != null
							&& udiSige.getCodIdSezioneUdienza() != null) {
						// nel caso di udienza monocratica sul form si trova una combo con le sezioni per
						// l'udienza
						lSezioneUdienza = udiSige.getCodIdSezioneUdienza().toString();
					}
				}

				// nel caso di rifissazione prelevo i dati della sezione e dell'aula dalla Udienza Sige
				// associata
				lSezioneUdienza = getIdSezioneUdienzaSige();
				aulaUdienza = getAulaUdienzaSige(lSezioneUdienza);
			}
		} else if (!isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
			// Se nella request e' flaggato il FORM_DEF_COLLEGIO siamo nel caso di un inserimento udienza
			// diretto
			lTipoGiudizio = getRequestStringParameter(ICostantiFascicoloSige.CAMPO_COD_TIPO_GIUDIZIO);
			String idUdienza[] = super.getParameterValues(CAMPO_ID_UDIENZA_SIGE);
			// BigDecimal lIdUdienzaSige = getRequestBigDecimalParameter(CAMPO_ID_UDIENZA_SIGE);
			String sIdUdienzaSige = idUdienza[idUdienza.length - 1];
			UdienzaSigeModel udiSige = getUdienzaSige(sIdUdienzaSige);
			setRequestAttribute("UdienzaSige", udiSige);

			if (udiSige != null && udiSige.getCodIdSezioneUdienza() != null) {
				// nel caso di una creazione del collegio prelevo i dati dell'aula dall'udienza sige
				lSezioneUdienza = udiSige.getCodIdSezioneUdienza().toString();
				aulaUdienza = getAulaUdienzaSige(lSezioneUdienza);
			}
		}

	}

	private boolean esisteOpposizione(BigDecimal idProvvedimento) throws F3BException {
		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		BigDecimal numOpposizioni = lCtrlProv.ExCountOpposizioniAccolteByIdProvvedimento(idProvvedimento);
		return numOpposizioni.intValue() > 0;
	}

	/**
	 * Viene analizzata la lista delle Udienze gia' assegnate al Procedimento. Se esiste una udienza gia'
	 * Fissata si rimanda al dettaglio del decreto. (NO)Se esiste una PRE_FISSAZIONE Udienza se ne ricavano i
	 * dati per utilizzarli nella nuava Fissazione. Se esiste una FISSAZIONE A SEGUITO RINVIO non si rimanda
	 * al dettaglio Rinvio, ma viene predisposto un alert.
	 * 
	 * @param aUdiPro
	 * @return true se puo' essere effettuata la Fissazione.
	 */
	private boolean analisiUdienzeProcedimento(BigDecimal aIdFasSige) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".analisiUdienzeProcedimento: inizio");
		boolean retValue = true;

		// mostra la maschera di inserimento
		if (!isRequestParameterNullObj("inserisci") && isRequestParameterNullObj("StoTornando")) {
			// mostra la maschera di inserimento (escluso quando sto tornando)
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Mostra la maschera di inserimento");
		} else if (!isRequestParameterNullObj(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE)
				|| !isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
			loadForm();
		} else if (mFasEsteso.getUdienzaProcedimento() != null
				&& mFasEsteso.getUdienzaProcedimento().getEveIdEvento() != null
				&& isRequestParameterNullObj("StoTornando")) {
			// esiste un procedimento associato al fascicolo vado in dettaglio (escluso quando sto tornando)
			gestioneFissazione(mFasEsteso.getUdienzaProcedimento(), "");
			retValue = false;
		} else if (isRequestParameterNullObj("StoTornando")) {
			// Preventivamente si controlla l'esistenza di una Udienza (in udienza procedimento sige) per il
			// Fascicolo Sige (escluso quando sto tornando)
			IUdienzaProcedimentoSige lUdiProCtrl = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
			Vector<UdienzaProcedimentoSigeModel> lUdiProVect = lUdiProCtrl
					.ExRicercaUdienzaProcedimentoByIdFascicoloSige(aIdFasSige);

			if (lUdiProVect != null && lUdiProVect.size() > 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Numero di record trovati ->" + lUdiProVect.size());
				// Ricerca di Udienze gia' Fissate per il procedimento
				if (esisteUdienzaFissata(lUdiProVect))
					retValue = false;
			} else {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Elenco Udienze preesistenti vuoto");
				// Viene effettuato il controllo sull'esistenza di un Provvedimento definitorio gia' emesso
				// per il Fascicolo SIGE.
				IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
				// Controllo anche se provvedimento definitorio e' annullato.
				ProvvedimentoSigeEventoModel lProvvedimento = lCtrlProv
						.ExRicercaProvvedimentoDefinitorioByIdFascicolo(mFasEsteso.getFascicoloSige()
								.getIdFascicoloSige());
				if (lProvvedimento != null && lProvvedimento.getProvvedimento() != null
						&& lProvvedimento.getEventoNotifica().getEvento() != null
						&& lProvvedimento.getEventoNotifica().getEvento().getFlagDocumentoRegistrato() != "A") {
					// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
					// di LogF3B.getLogger()
					// siesLogger.debug("Provvedimento: " + lProvvedimento);
					if (!esisteOpposizione(lProvvedimento.getProvvedimento().getIdProvvedimentoSige()))
						throw new SIGEException(SIGEException.USER_MESSAGE,
								"Operazione non consentita! E' stato gia' emesso un Provvedimento di tipo definitorio!");
				}
			}
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".analisiUdienzeProcedimento: fine");
		return retValue;
	}

	private boolean esisteUdienzaFissata(Vector<UdienzaProcedimentoSigeModel> aUdiProVect) throws Exception {

		boolean retValue = false;

		Iterator<UdienzaProcedimentoSigeModel> itx = aUdiProVect.iterator();

		while (itx.hasNext()) {
			UdienzaProcedimentoSigeModel lUdiProSige = (UdienzaProcedimentoSigeModel) itx.next();
			if (lUdiProSige != null && lUdiProSige.getFlagRinviata() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Flag ->" + lUdiProSige.getFlagRinviata());

				IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
				EventoModel lEvento = lEventoCtrl.ExRicercaEventoByKey(lUdiProSige.getEveIdEvento());

				if (lUdiProSige.getFlagRinviata().compareTo(ICostantiUdienzaProcedimentoSige.UDIENZA_FISSATA) == 0) {
					// Si verifica se il tipo di provvedimento attraverso l'evento sia una ordinanza
					// poiche' in caso di cancellazione di rinvii di udienze se esiste un rinvio
					// quest'ultimo e' valorizzato con flagrinviata a F pertanto solo attraverso
					// l'evento e il tipo provvedimento e' possibile decidere quale tipo di dettaglio da
					// presentare.

					if (lEvento.getCodTipoProvvedimento().equals(
							ICostantiProvvedimentoSige.COD_ORDINANZA_GENERICA)) {
						if (isUdienzaProcedimentoSigeOK(lUdiProSige, 0)) {
							// Gestione Udienza gia' FISSATA A SEGUITO RINVIO
							// gestioneASeguitoRinvio(lUdiProSige);
							gestioneFissazione(lUdiProSige, "S");
							retValue = true;
							break;
						}
					}

					if (isUdienzaProcedimentoSigeOK(lUdiProSige, 1)) {
						// Gestione Udienza gia' FISSATA
						gestioneFissazione(lUdiProSige, "");
						retValue = true;
						break;
					}
				} else if (lUdiProSige.getFlagRinviata().compareTo(
						ICostantiUdienzaProcedimentoSige.UDIENZA_SEGUITO_RINVIO) == 0) {
					if (isUdienzaProcedimentoSigeOK(lUdiProSige, 0)) {
						// Gestione Udienza gia' FISSATA A SEGUITO RINVIO
						// gestioneASeguitoRinvio(lUdiProSige);
						gestioneFissazione(lUdiProSige, "S");
						retValue = true;
						break;
					}
				}
			}
		}
		return retValue;
	}

	private void gestioneFissazione(UdienzaProcedimentoSigeModel aUdiProSige, String lPresenzaRinvio) {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".gestioneFissazione: inizio");

		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sige.udienza.action.ActLoadDettaglioFissazioneUdienza");
		lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + aUdiProSige.getEveIdEvento());
		lPage.setParameter("modalita", "I");
		lPage.setParameter("Cancellabile", "NO");
		lPage.setParameter("presenzaRinvio", lPresenzaRinvio);
		mRetPage = lPage.toString();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".gestioneFissazione: fine");
	}

	/*
	 * private void gestioneASeguitoRinvio (UdienzaProcedimentoSigeModel aUdiProSige) throws Exception { //
	 * [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	 * LogF3B.getLogger() siesLogger.debug( getClass().getName() + ".gestioneASeguitoRinvio: inizio" );
	 * RedirectTo lPage = new RedirectTo(); lPage.setPage(IWebConstants.PG_MAIN);
	 * 
	 * if( aUdiProSige.getEveIdEvento() == null) { // Rinvio Udienza senza Ordinanza
	 * lPage.setAction("siap.sige.udienzaprocedimento.action.ActDettaglioOrdinanzaRinvioUdienza");
	 * lPage.setParameter(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE, "" +
	 * aUdiProSige.getIdUdienzaProcedimentoSige() ); lPage.setParameter("modalita", "I");
	 * lPage.setParameter("Cancellabile","NO"); } else { // Rinvio Udienza con Ordinanza
	 * lPage.setAction("siap.sige.udienzaprocedimento.action.ActDettaglioVerbaleRinvioUdienza");
	 * lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO , "" + aUdiProSige.getEveIdEvento());
	 * lPage.setParameter(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE, "" +
	 * aUdiProSige.getIdUdienzaProcedimentoSige() ); lPage.setParameter("Cancellabile","NO"); //mRetPage =
	 * lPage.toString(); }
	 * 
	 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	 * LogF3B.getLogger() mRetPage = lPage.toString(); siesLogger.debug( getClass().getName() +
	 * ".gestioneASeguitoRinvio: fine" ); }
	 */

	// Controlla la coerenza dei dati nel record di UDIENZA_PROCEDIMENTO_SIGE.
	// I controlli sono:
	// La valorizzazione del campo UDI_ID_UDIENZA_SIGE viene sempre controllata.
	// La valorizzazione del campo EVE_ID_EVENTO viene controllata solo se il parametro aTipoControlli > 0.
	private boolean isUdienzaProcedimentoSigeOK(UdienzaProcedimentoSigeModel aUdiProSige, int aTipoControlli) {
		boolean retValue = true;
		if (aUdiProSige.getUdiIdUdienzaSige() == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn(" manca UDI_ID_UDIENZA_SIGE nel record in UDIENZA_PROCEDIMENTO_SIGE! ID ->"
					+ aUdiProSige.getIdUdienzaProcedimentoSige());
			retValue = false;
		}

		if (aTipoControlli > 0 && aUdiProSige.getEveIdEvento() == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn(" manca EVE_ID_UDIENZA nel record in UDIENZA_PROCEDIMENTO_SIGE! ID ->"
					+ aUdiProSige.getIdUdienzaProcedimentoSige());
			retValue = false;
		}
		return retValue;
	}

	protected UdienzaSigeModel getUdienzaSige(String aIdUdienzaSige) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("inizio");
		// ==========================================
		// Recupera i dati del record
		// ==========================================
		IUdienzaSige lCtrl = SIGELookupRemote.getUdienzaSigeRemote();
		UdienzaSigeModel lUdiMod = lCtrl.ExRicercaUdienzaSigeById(new BigDecimal(aIdUdienzaSige));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Lettura di lUdiMod : " + lUdiMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("fine");
		return lUdiMod;
	}

}