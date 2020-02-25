package siap.sius.udienza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.xml.TreeModel;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.jms.util.ParserMessageRec;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.sige.curatore.controller.ICuratore;
import siap.sige.curatore.model.CuratoreModel;
import siap.sige.util.SIGELookupRemote;
import siap.sius.SIUSException;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import siap.sius.curatore.controller.ICuratoreSius;
import siap.sius.curatore.model.CuratoreSiusModel;
import siap.sius.esperto.controller.IEsperto;
import siap.sius.esperto.model.EspertoModel;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.provvedimento.action.ICostantiProvvedimento;
import siap.sius.provvedimento.util.RicercaProvvedimentiUtil;
import siap.sius.stampa.action.ICostantiStampaSius;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciFissazioneUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load della form di inserimento Fissazione Udienza
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadInserisciFissazioneUdienza extends ActRicercaFSPuntuale
		implements ICostantiUdienza, ICostantiFascicoloSius {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	String mRectPage = PG_LOAD_INSERISCIFISSAZIONEUDIENZA;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		BigDecimal lIdFasSius = null;
		FascicoloGPModel lFasGPMod = null;
		MagistratoRelatoreModel lMagRel = null;
		MagistratoModel lMagistrato = null;
		EspertoModel lEsperto = null;
		ParserMessageRec lParser = null;
		String lFascSospeso = "NO";

		// Gestione bottone di ritorno
		setLinkRitorno();

		// Modifica del 04/10/2013 mev "Revisione Misure di Sicurezza SIUS"
		// In fase di Fissazione Udienza, dare la possibilità all'utente di definire
		// una nuova udienza direttamente dalla pagina "Inserimento Fissazione Udienza"
		// senza passare dalle Funzioni Amministrative
		String idUdienza = null;
		if (!isRequestParameterNullObj("IdUdienza")) {
			idUdienza = getRequestStringParameter("IdUdienza");
			UdienzaModel udienza = null;
			IUdienza lUdi = SIUSLookupRemote.getUdienzaRemote();
			udienza = lUdi.ExRicercaUdienzaByKey(BigDecimal.valueOf(Long.parseLong(idUdienza)));
			setRequestAttribute("udienza", udienza);
		}

		// variabile utilizzata per capire se è stata inserita una nuova udienza
		String provenienza = null;
		if (!isRequestParameterNullObj("Provenienza")) {
			provenienza = getRequestStringParameter("Provenienza");
		}

		if (isRequestParameterNullObj("ritorno") && provenienza == null) {
			// Invoca la process Request della superclasse se provengo dal menu'.
			super.processRequest();
		}

		// Fascicolo Sius
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Fascicolo SIUS non presente in sessione!");

		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		if (lFasGPMod == null || lFasGPMod.getFascicoloSiusModel() == null
				|| lFasGPMod.getGeneraleProcedimentoModel() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Dati Fascicolo non presenti in sessione!");

		// Viene effettuato il controllo sulla preesistenza di un Provvedimento definitorio
		// già emesso per il Fascicolo SIUS.
		// Se esiste almeno un provvedimento di questo tipo non può esserne emesso una fissazione udeinza.
		RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(
				lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
		boolean lEsistenzaDoc = lRicerca.verificaEsistenzaProv();
		if (lEsistenzaDoc)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Per il procedimento indicato è già stato emesso un provvedimento. Non è consentito emettere un nuovo provvedimento");

		if (lRicerca.verificaEsistenzaSospensione())
			lFascSospeso = "SI";

		// MERGE v10: verifico anche per le sentenze
		if ("NO".equals(lFascSospeso) && lRicerca.verificaEsistenzaSospensioneSentenza())
			lFascSospeso = "SI";

		lIdFasSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

		// Se non esiste una Udienza già fissata per il Fascicolo
		if (analisiUdienzeProcedimento(
				lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento())) {

			if (IsFascicoloSiusModificabile() == false)
				throw new SIUSException(SIUSException.USER_MESSAGE,
						ICostantiFascicoloSius.MSG_NON_MODIFICABILE);

			// Lock per evitare la fissazione contemporanea di 2 Udienze per lo stesso fascicolo
			LockModel lck = LockController.lockIfNotLocked(getServletContext(), "ProcedimentoSIUS",
					lIdFasSius.toString(), getCodUtenteConnesso(), getSession().getId());
			if (lck != null) {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il  " + lck.getEntity()
						+ " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
				return IWebConstants.PG_MESSAGE;
			}

			// Magistrato relatore
			IMagistratoRelatore lMagRelCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
			lMagRel = lMagRelCtrl.ExRicercaMagRelByFascicolo(lIdFasSius);

			if (lMagRel != null) {
				// Magistrato
				if (lMagRel.getMagCodMagistrato() != null) {
					IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
					lMagistrato = lMagCtrl.ExRicercaMagistratoByCod(lMagRel.getMagCodMagistrato());
				}

				// Esperto
				if (lMagRel.getEspIdEsperto() != null) {
					IEsperto lEspCtrl = SIUSLookupRemote.getEspertoRemote();
					lEsperto = lEspCtrl.ExRicercaEspertoByKey(lMagRel.getEspIdEsperto());
				}
			}

			// Ricerca avvocati assegnati al fascicolo Luigi 9-7-04
			IAvvocato lAvvCtrl = SIUSLookupRemote.getAvvocatoRemote();
			Vector<DecodificheModel> lAvvocato = lAvvCtrl.ExRicercaAvvocatiByFascicoloNoError(
					lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
			setRequestAttribute("avvocato", lAvvocato);

			// Preleva dati AVVOCATI e LUOGODETENZIONE
			IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
			// Riempi l'Array contenente le tipologie di dati da prelevare
			int[] aTipoDati = { ICostantiStampaSius.TREE_LUOGODET };
			// Crea il TreeModel con i dati che occorrono
			TreeModel lTreeDati = lCtrlSta
					.ExPrelevaDatiVideo(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(), aTipoDati);
			// Converte i dati ottenuti per utilizzarli come model
			lParser = new ParserMessageRec(lTreeDati);

			LuogoDetenzioneModel lLuogoDetMod = lParser.getLuogoDetenzione();

			String lCodOggetti = new String();
			String lDescOggetti = new String();
			String lCodOggettoProc = new String();
			String lCodDettagli = new String(); // STUB 15/04/2004

			lCodOggettoProc = lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento();

			if (lFasGPMod.getTenori() != null) {
				for (int i = 0; i < lFasGPMod.getTenori().length; i++) {
					lCodOggetti += lFasGPMod.getTenori()[i].getCodOggettoTenore() + "|";
					lDescOggetti += lFasGPMod.getTenori()[i].getDescrOggettoTenore() + "\n";
					// STUB 15/04/2004 Aggiunti i Codici dettaglio.
					if (lFasGPMod.getTenori()[i].getCodDettaglioOggetto() != null
							&& lFasGPMod.getTenori()[i].getCodDettaglioOggetto().length() > 1) {
						lCodDettagli += lFasGPMod.getTenori()[i].getCodOggettoTenore()
								+ lFasGPMod.getTenori()[i].getCodDettaglioOggetto() + "|";
					}
				}
			}

			// Elenco contenuti.
			Option lOption = new Option();
			String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
			if (strCodTipoUfficio.equals("TDS"))
				lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoTDS(),
						lCodOggettoProc, 75);
			else if (strCodTipoUfficio.equals("UDS"))
				lOption = new Option(
						DecodificheUtils.getDecodesWithoutCode(
								DecodificheManager.getInstance().getOggettoProcedimentoUDS(), "U004"),
						lCodOggettoProc, 75);
			else
				lOption = new Option(
						DecodificheUtils.getDecodesWithoutCode(
								DecodificheManager.getInstance().getOggettoProcedimento(), "U004"),
						lCodOggettoProc, 75);

			// Preleva elenco degli altri destinatari.
			Collection<DecodificheModel> autorita = new Vector<>();
			autorita.addAll(DecodificheManager.getInstance().getTipoAutorita());
			String filtroMinorenni = super.getFiltroMinorenni();
			if (filtroMinorenni.equalsIgnoreCase("true")) {
				List<DecodificheModel> listAutorita = new ArrayList<>();
				listAutorita.addAll(autorita);
				listAutorita.addAll(DecodificheManager.getInstance().getTipoAutoritaMinorenni());
				Collections.sort(listAutorita, new DecodificheModel.OrderByDescrizione());
				autorita = listAutorita;
			}
			Option lOptionAut = new Option(autorita, 75);

			// LISTA UFFICI SOGGETTO
			Option lOptionSog = new Option();
			if (lLuogoDetMod != null && lLuogoDetMod.getIstitutoDetenzione() != null
					&& lLuogoDetMod.getIstitutoDetenzione().getCodTipoIstituto().length() > 0)
				lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(),
						lLuogoDetMod.getIstitutoDetenzione().getCodTipoIstituto(), 75);
			else
				lOptionSog = new Option(DecodificheManager.getInstance().getTipoIstituto(), 75);

			// LISTA UFFICI
			Collection<DecodificheModel> lTipoIstituto = DecodificheManager.getInstance().getTipoAutorita();
			// MEV10-s3: modificato array per i minorenni
			String[] lStringFilter = null;
			// MEV_65 aggiunte voci per notifica avvocato e gestita selezione
			if ("true".equals(filtroMinorenni))
				// lStringFilter = new String[] { "-", "22", "A2" };
				lStringFilter = new String[] { "-", "22", "A2", "C1" };
			else
				// lStringFilter = new String[] { "-", "22" };
				lStringFilter = new String[] { "-", "22", "C0", "C1" };
			Option lOptionAvv = new Option();
			if (isRequestParameterNullObj("Aggiungi")) {
				// lOptionAvv = new Option(lTipoIstituto, "22", 75);
				lOptionAvv = new Option(lTipoIstituto, "C1", 75); // predefinito
			} else {
				lOptionAvv = new Option(lTipoIstituto, "-", 75);
			}
			lOptionAvv.setFilter(lStringFilter);

			// Evento notifica Model.
			EventoNotificaModel lEve = new EventoNotificaModel();
			setRequestAttribute("evento", lEve);

			// Avvocati assegnati al fascicolo con data fine = null
			AvvocatoFascicoloSiusModel lAvvFascMod = new AvvocatoFascicoloSiusModel();
			lAvvFascMod.setFasSiuIdFascicoloSius((lFasGPMod.getFascicoloSiusModel()).getIdFascicoloSius());

			// Eventuale Curatore Sius 18/05/2011
			UfficioModel lUff = getUfficioUtenteConnesso();
			CuratoreSiusModel lCuratore = null;
			if (lUff.getCodTipoUfficio().equals("UDS")) {
				ICuratoreSius lCurSiusCtrl = SIUSLookupRemote.getCuratoreSiusRemote();
				lCuratore = lCurSiusCtrl
						.ExRicercaCurSiusByFascicolo(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
				if (lCuratore != null) {
					// Curatore
					if (lCuratore.getCurIdCuratore() != null) {
						ICuratore lCurCtrl = SIGELookupRemote.getCuratoreRemote();
						CuratoreModel lCurMod = lCurCtrl.ExRicercaCuratoreByKey(lCuratore.getCurIdCuratore());
						lCuratore.setCuratore(lCurMod);
					}
				}
				setRequestAttribute("curatore", lCuratore);
			}

			setRequestAttribute("luogodet", lLuogoDetMod);
			// setRequestAttribute("avvocato", lParser.getAvvocatoSius() );
			setRequestAttribute("magistrato", lMagistrato);
			setRequestAttribute("esperto", lEsperto);
			setRequestAttribute("TipiIstituti1", "" + lOptionAvv);
			setRequestAttribute("TipiIstitutiSog", "" + lOptionSog);
			setRequestAttribute("tipoAutorita", lOptionAut.toString());
			setRequestAttribute("contenuto", "" + lOption);
			setRequestAttribute("codOggetti", lCodOggetti);
			setRequestAttribute("descOggetti", lDescOggetti);
			setRequestAttribute("codDettagli", lCodDettagli);
			setRequestAttribute("modalita", "I");
			setRequestAttribute("fascSospeso", lFascSospeso);

		} // endif analisiUdienzeProcedimento
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return mRectPage;
	}

	/**
	 * Viene analizzata la lista delle udienze già assegnate al Procedimento. Se esiste una udienza già
	 * Fissata si rimanda al dettaglio del decreto. Se esiste una PRE_FISSAZIONE Udienza se ne ricavano i dati
	 * per utilizzarli nella nuava Fissazione. Se esiste una FISSAZIONE A SEGUITO RINVIO si rimanda al
	 * dettaglio Rinvio.
	 *
	 * @param aUdiPro
	 * @return true se può essere effettuata la Fissazione.
	 */
	private boolean analisiUdienzeProcedimento(BigDecimal aIdGenProc) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".analisiUdienzeProcedimento: inizio");
		boolean retValue = true;

		// Se nella request c'è l'ID di UDIENZA_PROCEDIMENTO siamo nel caso di una Rifissazione
		if (!isRequestParameterNullObj(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO)) {
			setRequestAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO,
					getRequestStringParameter(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Rifissazione Udienza"
					+ getRequestStringParameter(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO));
		} else {
			IUdienzaProcedimento lUdiProCtrl = null;
			Vector lUdiProVect = null;

			// Preventivamente si controlla l'esistenza di una Udienza per il fascicolo
			lUdiProCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();
			lUdiProVect = lUdiProCtrl.ExRicercaUdienzaProcedimentoByGeneraleProcedimento(aIdGenProc);

			if (lUdiProVect != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Numero di record trovati ->" + lUdiProVect.size());
				// Ricerca di Udienze già Fissate per il procedimento
				if (esisteUdienzaFissata(lUdiProVect)) {
					retValue = false;
				} else {
					// Ricerca di Udienze Preissate per il procedimento
					Iterator itx = lUdiProVect.iterator();
					while (itx.hasNext()) {
						UdienzaProcedimentoModel lUdiPro = (UdienzaProcedimentoModel) itx.next();
						if (lUdiPro != null && lUdiPro.getFlagRinviata() != null) {
							if (lUdiPro.getFlagRinviata()
									.compareTo(ICostantiUdienzaProcedimento.UDIENZA_PREFISSATA) == 0) {
								if (isUdienzaProcedimentoOK(lUdiPro, 0)) {
									// Gestione Udienza già PRE-FISSATA
									gestionePreFissazione(lUdiPro);
									break;
								}
							}
						}
					}
				}
			} else
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Elenco Udienze preesistenti vuoto");
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".analisiUdienzeProcedimento: fine");
		return retValue;
	}

	private boolean esisteUdienzaFissata(Vector aUdiProVect) throws Exception {

		boolean retValue = false;

		Iterator itx = aUdiProVect.iterator();

		while (itx.hasNext()) {
			UdienzaProcedimentoModel lUdiPro = (UdienzaProcedimentoModel) itx.next();
			if (lUdiPro != null && lUdiPro.getFlagRinviata() != null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Flag ->" + lUdiPro.getFlagRinviata());

				if (lUdiPro.getFlagRinviata().compareTo(ICostantiUdienzaProcedimento.UDIENZA_FISSATA) == 0) {
					// 20080220 Patch.
					// Si verifica se il tipo di provvedimento attraverso l'evento sia una ordinanza
					// poichè in caso di cancellazione di rinvii di udienze se esiste un rinvio
					// quest'ultimo è valorizzato con flagrinviata a F pertanto solo attraverso
					// l'evento e il tipo provvedimento è possibile decidere quale tipo di dettaglio da
					// presentare.
					if (verificaTipoEventoProvvedimento(lUdiPro.getEveIdEvento(),
							ICostantiProvvedimento.COD_ORDINANZA)) {
						if (isUdienzaProcedimentoOK(lUdiPro, 0)) {
							// Gestione Udienza già FISSATA A SEGUITO RINVIO
							gestioneASeguitoRinvio(lUdiPro);
							retValue = true;
							break;
						}
					}

					if (isUdienzaProcedimentoOK(lUdiPro, 1)) {
						// Gestione Udienza già FISSATA
						gestioneFissazione(lUdiPro);
						retValue = true;
						break;
					}
				} else if (lUdiPro.getFlagRinviata()
						.compareTo(ICostantiUdienzaProcedimento.UDIENZA_SEGUITO_RINVIO) == 0) {
					if (isUdienzaProcedimentoOK(lUdiPro, 0)) {
						// Gestione Udienza già FISSATA A SEGUITO RINVIO
						gestioneASeguitoRinvio(lUdiPro);
						retValue = true;
						break;
					}
				}
			}
		}
		return retValue;
	}

	private void gestioneFissazione(UdienzaProcedimentoModel aUdiPro) {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".gestioneFissazione: inizio");

		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.sius.udienza.action.ActLoadDettaglioFissazioneUdienza");
		lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + aUdiPro.getEveIdEvento());
		lPage.setParameter("modalita", "I");
		mRectPage = lPage.toString();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".gestioneFissazione: fine");
	}

	private void gestionePreFissazione(UdienzaProcedimentoModel aUdiPro) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".gestionePreFissazione: inizio");

		// Ricerca Udienza
		UdienzaModel lUdienza = null;
		IUdienza lUdi = SIUSLookupRemote.getUdienzaRemote();
		lUdienza = lUdi.ExRicercaUdienzaByKey(aUdiPro.getUdiIdUdienza());
		setRequestAttribute("UdienzaPreFissata", lUdienza);
		setRequestAttribute(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO,
				"" + aUdiPro.getIdUdienzaProcedimento());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".gestionePreFissazione: fine");
	}

	private void gestioneASeguitoRinvio(UdienzaProcedimentoModel aUdiPro) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".gestioneASeguitoRinvio: inizio");
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);

		if (aUdiPro.getEveIdEvento() == null) {
			// Rinvio Udienza senza Ordinanza
			lPage.setAction("siap.sius.udienza.action.ActDettaglioRinvioUdienza");
			lPage.setParameter(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO,
					"" + aUdiPro.getIdUdienzaProcedimento());
			lPage.setParameter("modalita", "I");
		} else {
			// Rinvio Udienza con Ordinanza
			lPage.setAction("siap.sius.udienza.action.ActDettaglioVerbaleRinvioUdienza");
			lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + aUdiPro.getEveIdEvento());
			lPage.setParameter(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO,
					"" + aUdiPro.getIdUdienzaProcedimento());
			mRectPage = lPage.toString();
		}
		mRectPage = lPage.toString();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".gestioneASeguitoRinvio: fine");
	}

	// Controlla la coerenza dei dati nel record di UDIENZA_PROCEDIMENTO.
	// I controlli sono:
	// La valorizzazione del campo UDI_ID_UDIENZA viene sempre controllata.
	// La valorizzazione del campo EVE_ID_EVENTO viene controllata solo se il parametro aTipoControlli > 0.
	private boolean isUdienzaProcedimentoOK(UdienzaProcedimentoModel aUdiPro, int aTipoControlli) {

		boolean retValue = true;
		if (aUdiPro.getUdiIdUdienza() == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn(" manca UDI_ID_UDIENZA nel record in UDIENZA_PROCEDIMENTO! ID ->"
					+ aUdiPro.getIdUdienzaProcedimento());
			retValue = false;
		}

		if (aTipoControlli > 0 && aUdiPro.getEveIdEvento() == null) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn(" manca EVE_ID_UDIENZA nel record in UDIENZA_PROCEDIMENTO! ID ->"
					+ aUdiPro.getIdUdienzaProcedimento());
			retValue = false;
		}

		return retValue;
	}

	/*
	 * Metodo che verifica se il tipo provvedimento di un evento corrisponda al CodTipoProvvedimento passto
	 * come parametro. Vedi commento nel punto utilizzato.
	 */
	private boolean verificaTipoEventoProvvedimento(BigDecimal aIDEvento, String aCodTipoProvvedimento)
			throws Exception {

		IEvento lEventoCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEvento = lEventoCtrl.ExRicercaEventoByKey(aIDEvento);

		if (lEvento.getCodTipoProvvedimento().equals(aCodTipoProvvedimento))
			return true;

		return false;
	}

}