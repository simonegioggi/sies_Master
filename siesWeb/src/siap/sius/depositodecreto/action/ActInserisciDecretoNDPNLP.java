package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoEventoModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GPTenoreModel;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

public class ActInserisciDecretoNDPNLP extends ActionSius implements ICostantiDepositoDecreto {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Si prelevano dati di sessione.
		String lCodiceOperatore = getCodUtenteConnesso();
		String lCodiceUfficio = getCodUfficioUtenteConnesso();
		String lCodiceComune = getCodComuneUtenteConnesso();

		// Si preleva dall sessione il fascicolo GPModel.
		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Preleva id generale procedimento.
		BigDecimal lIdGenProc = lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();

		// Istanzia model generale procedimento.
		GeneraleProcedimentoModel lGenProcModel = new GeneraleProcedimentoModel();
		lGenProcModel.setIdGeneraleProcedimento(lIdGenProc);
		lGenProcModel.setCodOggettoProcedimento(
				getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO));
		lGenProcModel.setDataAggiornamento(DateUtils.getSysDate());
		lGenProcModel.setCodUfficioAggiornamento(lCodiceUfficio);
		lGenProcModel.setCodOperatoreAggiornamento(lCodiceOperatore);

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel lMagRel = lMagCtrl
				.ExRicercaEstesaMagRelByFascicolo(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

		if (lMagRel == null || lMagRel.getMagistrato() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Magistrato relatore non definito !");
		// MEV_65: Punto 1.13 se magistrato è scaduto impossibile inserire decreto od ordinanza
		else {
			IMagistrato im = SICOLookupRemote.getMagistratoRemote();
			MagistratoModel mm = new MagistratoModel();
			mm.setCodMagistrato(lMagRel.getMagistrato().getCodMagistrato());
			mm.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
			mm.setCognome(lMagRel.getMagistrato().getCognome());
			mm.setNome(lMagRel.getMagistrato().getNome());
			Vector v = im.ExRicercaMagistrato(mm);
			if (!v.isEmpty()) {
				MagistratoModel mag = (MagistratoModel) v.get(0);
				if (mag.getDataFineValidita() != null
						&& (DateUtils.isLower(mag.getDataFineValidita(), DateUtils.getSysDate())
								|| DateUtils.isEquals(mag.getDataFineValidita(), DateUtils.getSysDate())))
					throw new SIUSException(SIUSException.USER_MESSAGE,
							"Attenzione! Impossibile emettere il provvedimento. Assegnatario del procedimento è un magistrato non più in servizio!");
			}
		}

		// Prelevare il codice Magistrato_Relatore
		String lCodMagistrato = lMagRel.getMagistrato().getCodMagistrato();

		// Gestione oggetti Tenore.
		String lCodOggetti = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_OGGETTO);
		String lDescOggetti = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO);
		// STUB 19/04/2004 Aggiunti i Codici Dettaglio Oggetti.
		String lCodDettaglioOggetti = getRequestStringParameter(
				ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO);

		// Inserisce nel model aggregante l'Array di model dei Tenori e Il model GeneraleProcedimento.
		GPTenoreModel lGPTenoreModel = new GPTenoreModel();

		// STUB 19/04/2004 lGPTenoreModel.setTenori( this.parseOggettiTenori( lCodOggetti, lDescOggetti,
		// lCodMagistrato ) );
		TenoreModel[] lTenori = this.parseOggettiTenori(lCodOggetti, lDescOggetti, lCodDettaglioOggetti,
				"0004", lCodMagistrato);
		lGPTenoreModel.setTenori(lTenori);

		lGPTenoreModel.setGeneraleProcedimentoModel(lGenProcModel);

		// Prepara il model DepositoDecreto. -- Creare un Metodo private ? --
		DepositoDecretoModel lDepDecrModel = new DepositoDecretoModel();
		lDepDecrModel
				.setDataEmissione(getRequestDateParameter(ICostantiDepositoDecreto.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiDepositoDecreto.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_EMISSIONE));
		lDepDecrModel.setCodTipoDecreto(NDP_NLP);
		lDepDecrModel.setCodMagistrato(lCodMagistrato); // da Inserire qui ? Oppure nel controller?.
		lDepDecrModel.setGenPridGeneraleProcedimento(lIdGenProc);
		lDepDecrModel.setCodOperatoreInserimento(lCodiceOperatore);
		lDepDecrModel.setCodUfficioInserimento(lCodiceUfficio);
		lDepDecrModel.setDataInserimento(DateUtils.getSysDate());

		// Si inserisce in decretoModel il valore inserito nel campo "rilevato".
		lDepDecrModel.setNote(getRequestStringParameter(ICostantiDepositoDecreto.CAMPO_NOTE));

		// Prepara Model Evento.
		EventoModel lEventoModel = new EventoModel();
		lEventoModel.setCodTipoEvento("01"); // 01 = Provvedimento.
		lEventoModel.setCodTipoProvvedimento("02"); // 02 = Decreto.
		lEventoModel.setCodLuogoEmittente(lCodiceComune);
		lEventoModel.setCodUfficioEmittente(lCodiceUfficio);
		lEventoModel.setCodEsito("0004");
		// Angela aggiunto il codice magistrato
		lEventoModel.setCodMagistrato(lCodMagistrato);
		lEventoModel
				.setDataEmissione(getRequestDateParameter(ICostantiDepositoDecreto.CAMPO_ANNO_DATA_EMISSIONE,
						ICostantiDepositoDecreto.CAMPO_MESE_DATA_EMISSIONE,
						ICostantiDepositoDecreto.CAMPO_GIORNO_DATA_EMISSIONE));

		lEventoModel.setFasSieIdFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		lEventoModel.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		lEventoModel.setCodOperatoreInserimento(lCodiceOperatore);
		lEventoModel.setCodUfficioInserimento(lCodiceUfficio);
		lEventoModel.setDataInserimento(DateUtils.getSysDate());
		lEventoModel.setCodLuogoDestinatario("-");
		lEventoModel.setCodTipoUfficioDestinatario("-");
		lEventoModel.setCodUfficioDestinatario("-");

		DepositoDecretoEventoModel lDepDecrEveModel = new DepositoDecretoEventoModel();
		lDepDecrEveModel.setDepositoDecreto(lDepDecrModel);
		lDepDecrEveModel.setEvento(lEventoModel);

		// Chiamata Controller per Inserimento.
		IDepositoDecreto lDepDecrCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
		lDepDecrEveModel = lDepDecrCtrl.ExInserisciDecreto(lGPTenoreModel, lDepDecrEveModel);

		// Prepara la pagina di destinazione, in questo caso è il dettaglio del decreto d'inammissibilità.
		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);
		lRedirectTo.setAction("siap.sius.depositodecreto.action.ActLoadDettaglioDecretoNDPNLP");
		lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,
				lDepDecrEveModel.getEvento().getIdEvento().toString());

		return lRedirectTo.toString();
	}

}