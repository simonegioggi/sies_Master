package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.jms.util.ParserMessageRec;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.stampa.action.ICostantiStampaSius;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.tenore.controller.ITenore;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title: ActLoadDettaglioDecretoinammissibilita
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Decreto Inammissibilita
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
public class ActLoadDettaglioDecretoIrreperibilità extends ActionSiap implements ICostantiDepositoDecreto {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		setLinkRitorno();

		FascicoloGPModel lFasGPMod = new FascicoloGPModel();
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

//		BigDecimal lIdGenProcredimento = lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();

		// BigDecimal idDepositoDecreto =
		// getRequestBigDecimalParameter(ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO);
		BigDecimal aIdFascicoloSius = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius();

		// genny 23/02/2004
		// Preleva id evento dalla request.
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Preleva attraverso l'id evento generato, il decreto in deposito decreto.
		IDepositoDecreto lCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
		DepositoDecretoEventoMotivazioniModel lDepDecrMotMod = lCtrl
				.ExRicercaDecretoEventoMotivazioniIncompetenzaByIdEvento(lIdEvento);

//		DepositoDecretoModel depositodecretomodel = lDepDecrMotMod.getDepositoDecreto();
		// Inserisce l'id evento nel model. Perchè mai ????????????
		lDepDecrMotMod.getEvento().setIdEvento(lIdEvento);

		// genny 10/02/2004 da Act InserisciDecretoIrreperibilità bisogna passare lIdGenProcredimento come
		// parametro invece di idEvento
		// IDepositoDecreto lDepDecretoCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
		// DepositoDecretoModel lDepDecreto =
		// lDepDecretoCtrl.ExRicercaDepositoDecretoByKey(idDepositoDecreto);

		// Preleva i tenori, per il generale procedimento.
		ITenore lTenCtrl = SIUSLookupRemote.getTenoreRemote();
		// Vector lTenori =
		// lTenCtrl.ExRicercaTenoreByGenProc(lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
		Vector lTenori = lTenCtrl.ExRicercaTenoreByDecreto(lDepDecrMotMod.getDepositoDecreto()
				.getIdDepositoDecreto());

		// Imposta gli oggetti nella request.
		setRequestAttribute("tenori", lTenori);
		setRequestAttribute("depositoDecretoMotivazioni", lDepDecrMotMod);
		// setRequestAttribute("depositodecretomodel", lDepDecreto);

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel lMagRel = lMagCtrl.ExRicercaEstesaMagRelByFascicolo(lFasGPMod
				.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("magistratorelatore", lMagRel);
		// genny 06/02/2004
		AvvocatoSiusModel lAvvSius = null;
		IAvvocato lAvvocatoSiusCtrl = SIUSLookupRemote.getAvvocatoRemote();
		lAvvSius = lAvvocatoSiusCtrl.ExRicercaAvvocatoByKeyAvvocatoFasSius(lFasGPMod.getFascicoloSiusModel()
				.getIdFascicoloSius());
		setRequestAttribute("avvocatosius", lAvvSius);

		// genny 13/02/2004 carico la lista degli avvocati legati al fascicolo
		IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
		// Riempi l'Array contenente le tipologie di dati da prelevare
		int[] aTipoDati = { ICostantiStampaSius.TREE_AVVOCATO };
		// Crea il TreeModel con i dati che occorrono
		TreeModel lTreeDati = lCtrlSta.ExPrelevaDatiVideo(aIdFascicoloSius, aTipoDati);
		// Converte i dati ottenuti per utilizzarli come model
		ParserMessageRec lParser = new ParserMessageRec(lTreeDati);
		setRequestAttribute("avvocato", lParser.getAvvocato());

		// Valorizzazione eventuale bottone di ritorno Spostata 30-4-2004
		/*
		 * if(!isRequestParameterNullObj("acdest")) {
		 * setRequestAttribute("acdest",getRequestStringParameter("acdest")); }
		 */
		return PG_LOAD_DETTAGLIO_DECRETO_IRREPERIBILITA;
	}

}