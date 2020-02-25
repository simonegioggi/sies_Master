package siap.siep.fascicolo.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.SIAPException;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActValidazioneFascicolo extends ActionSiap implements ICostantiFascicoloSiep {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		// Viene cercato l'eventuale fascicolo da validare tra quelli dell'ufficio utente
		BigDecimal lChiaveProgr = getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR);
		BigDecimal lChiaveAnno = getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO);
		String lChiaveUfficio = lUtenteMod.getUfficioUtente().getCodUfficio();
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

		lFasMod.setChiaveProgr(lChiaveProgr);
		lFasMod.setChiaveAnno(lChiaveAnno);
		lFasMod.setChiaveUfficio(lChiaveUfficio);

		FascicoloSiepModel lFasModel = null;
		lFasModel = lCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

		if (lFasModel == null) {
			throw new SIEPException(SIAPException.USER_MESSAGE,
					"Il Fascicolo è inesistente o non appartiene all'ufficio dell'utente");
		}

		this.setSessionAttribute("fascicolo", lFasModel);// fascicolo prima della validazione

		if (lFasModel.getFlagValidato() != null && lFasModel.getFlagValidato().equals("S")) {
			throw new SIEPException(SIAPException.USER_MESSAGE, " Il Fascicolo è già stato validato");
		}

		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);

		PenaComplessivaModel lPenaCom = new PenaComplessivaModel();
		IPenaComplessiva lCtrlPena = SIEPLookupRemote.getPenaComplessivaRemote();
		lPenaCom = lCtrlPena.ExRicercaPenaComplessivaByIdFascicolo(lFasModel.getIdFascicoloSiep());
		// 22/10/2014 Esclusi dal successivo controllo di Pena Complessiva i Procedimenti della classe IV
		if ((lPenaCom == null) && ((lFasModel.getChiaveProgr().intValue() < 40000)
				|| (lFasModel.getChiaveProgr().intValue() >= 50000))) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Pena Complessiva Inesistente");
			lRedirigi.setAction("siap.siep.penacomplessiva.action.ActLoadInserisciPenaComplessiva&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;

		}

		MagistratoCompetenteMagistratoModel lMagMod = new MagistratoCompetenteMagistratoModel();
		IMagistratoCompetente lCtrlMagCom = SICOLookupRemote.getMagistratoCompetenteRemote();
		lMagMod = lCtrlMagCom.ExRicercaMagistratoCompetenteByFascicolo(lFasModel.getIdFascicoloSiep());
		if (lMagMod == null || lMagMod.getMagistratoCompetente() == null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun Magistrato assegnato al Fascicolo");
			lRedirigi.setAction("siap.sico.magistratocompetente.action.ActLoadInserisciMagistratoCompetente&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;

		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("dopo controllo magistrato ");

		// Paolo Cherubini 07/03/2011 aggiungo controllo esistenza data arrivo atto altrimenti
		// non permetto validazione fascicolo, (errore generato da Iscritto da NSC)
		if (lFasModel.getDataArrivoAtto() == null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Inserire la data Arrivo atto");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadModificaFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + lFasModel.getIdFascicoloSiep());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		// ==========================================================================
		// Verifico l'iscrizione dei reati
		// n.b. Solo per fascicoli di classe 1: Pena Detentiva (progr <=20.000 )
		// since 3.1upd02
		// ==========================================================================
		if (lFasModel.getChiaveProgr().intValue() <= 20000) {
			ReatoModel lReaMod = new ReatoModel();
			lReaMod.setFasSieIdFascicoloSiep(lFasModel.getIdFascicoloSiep());

			IReato lReatoCtrl = SIEPLookupRemote.getReatoRemote();
			try {
				lReatoCtrl.ExRicercaReato(lReaMod);
			} catch (F3BException e) {
				if (e.getErrorCode() == F3BException.USER_MESSAGE) { // Nessun elemento trovato
					setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessun Reato assegnato al Fascicolo");
					lRedirigi.setAction("siap.siep.reato.action.ActLoadInserisciReato&"
							+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
					setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
					return IWebConstants.PG_MESSAGE;
				} else {
					throw e;
				}
			}
		}

		// **************************************************************************************************
		// Federica - a9-rr-078
		// aggiunto controllo presenza comune se soggetto nato in italia

		if (lFasModel.getSoggetto().getCodComuneNascita().equals("-")
				&& lFasModel.getSoggetto().getCodStatoNascita().equals("039")) // 039 --> Italia
		{
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Soggetto incompleto, Inserire Comune di nascita!");
			lRedirigi.setAction("siap.sico.soggetto.action.ActLoadModificaSoggetto&IdSoggetto="
					+ lFasModel.getSoggetto().getIdSoggetto());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}
		// **************************************************************************************************

		// d.f. 05/03/2015 per i fascicoli di classe IV verifico la correttezza delle MS
		// alcuni fascicoli migrati hanno COD_TIPO_MISURA = "-"
		if (lFasModel.getChiaveProgr().intValue() > 40000 && lFasModel.getChiaveProgr().intValue() <= 50000) {
			IMisuraSicurezza lCtrlMs = SIEPLookupRemote.getMisuraSicurezzaRemote();
			List lListMisure = lCtrlMs.ExRicercaMisuraSicurezzaByIdFascicolo(lFasModel.getIdFascicoloSiep());

			if (lListMisure.size() > 0) {
				Iterator lIterMis = lListMisure.iterator();
				while (lIterMis.hasNext()) {
					MisuraSicurezzaModel lMisSicu = (MisuraSicurezzaModel) lIterMis.next();
					if ("-".equals(lMisSicu.getCodTipo())) {
						setRequestAttribute(IWebConstants.MESSAGE_TEXT,
								"Misure di sicurezza incomplete, è necessario provvedere alla correzione prima di poter validare il procedimento!");
						lRedirigi.setAction("siap.siep.misurasicurezza.action.ActRicercaMisuraSicurezza&"
								+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
								+ lFasModel.getIdFascicoloSiep());
						setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
						return IWebConstants.PG_MESSAGE;
					}
				}
			} else {
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Misure di sicurezza assenti, è necessario provvedere alla loro iscrizione prima di poter validare il procedimento!");
				lRedirigi.setAction("siap.siep.misurasicurezza.action.ActRicercaMisuraSicurezza&"
						+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
						+ lFasModel.getIdFascicoloSiep());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				return IWebConstants.PG_MESSAGE;

			}
		}

		// ==========================================================================
		lFasModel.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lFasModel.setDataAggiornamento(DateUtils.getSysDate());
		lFasModel.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lFasModel.setFlagValidato("S");
		lFasModel.setCodStatoFascicolo("03");

		// setto lo stato del procedimento
		StatoProcedimentoModel lStat = new StatoProcedimentoModel();
		lStat.setFasSieIdFascicoloSiep(lFasModel.getIdFascicoloSiep());
		lStat.setProgressivo(new BigDecimal(1));
		lStat.setDataInserimento(DateUtils.getSysDate());
		lStat.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lStat.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lStat.setCodStatoProcedimento("0109"); // Validato

		lFasMod = lCtrl.ExValidazione(lFasModel, lStat);

		if (!this.isRequestParameterNullObj(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE)) {
			setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
					this.getRequestStringParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE));
		}

		this.setSessionAttribute("fascicolo", lFasMod);// fascicolo dopo della validazione

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIEP + "="
				+ lFasMod.getIdFascicoloSiep().toString();
	}

}