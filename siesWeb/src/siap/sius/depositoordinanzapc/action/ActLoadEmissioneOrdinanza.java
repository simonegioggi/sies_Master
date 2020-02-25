package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.tenore.controller.ITenore;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciDepositoOrdinanzaPc
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di DepositoOrdinanzaPc
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
@SuppressWarnings("rawtypes")
public class ActLoadEmissioneOrdinanza extends ActRicercaFSPuntuale implements ICostantiDepositoOrdinanzaPc {

	public String processRequest() throws Exception {

		String lRetPage = PG_LOAD_EMISSIONE_ORDINANZA; // pagina di view


		if (this.isRequestParameterNullObj("ritorno")) {
			// Solo se provengo da menuù
			super.processRequest();
		}

		String lCodOggetti = new String();
		String lDescOggetti = new String();
		String lCodDettagli = new String(); // STUB 21/04/2004

		if (this.isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "fascicoloSiusGP non in sessione");

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		if (lFasGPMod.getGeneraleProcedimentoModel().getDataCameraConsiglio() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Impossibile emettere un'ordinanza per il fascicolo "
							+ lFasGPMod.getFascicoloSiusModel().getChiaveAnno() + "/"
							+ lFasGPMod.getFascicoloSiusModel().getChiaveProgr()
							+ ". Non è stata fissata l'Udienza.");

		// Preleva l'id di generale procedimento.
		BigDecimal lIdGenProc = lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();
		if (lIdGenProc == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Id Generale Procedimento assente");
		/*
		 * IDepositoOrdinanzaPc lCtrlDep = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		 * 
		 * // Verifica se già esistono ordinanze per il fascicolo selezionato. if(
		 * this.isRequestParameterNullObj("Modifica") &&
		 * lCtrlDep.ExVerificaEsistenzaDepositoOrdinanzaByIdGenProc( lIdGenProc, null ) ) { // Se già esiste
		 * l'ordinanza viene lanciato il dettaglio Luigi 5-11-2003
		 * 
		 * DepositoOrdinanzaPcModel lDepOrd = lCtrlDep.ExRicercaDepositoOrdinanzaPcByGenProc(lIdGenProc); if
		 * (lDepOrd == null) throw new
		 * SIUSException(SIUSException.USER_MESSAGE,"Generale Procedimento assente");
		 * 
		 * if (lDepOrd.getIdEventoGenerato() == null) throw new
		 * SIUSException(SIUSException.USER_MESSAGE,"Errore nei dati: ID_EVENTO mancante");
		 * 
		 * //Prepara la pagina di destinazione, il Dettaglio. RedirectTo lPage = new RedirectTo();
		 * lPage.setPage(IWebConstants.PG_MAIN);
		 * lPage.setAction("siap.sius.depositoordinanzapc.action.ActDettaglioEmissioneOrdinanza");
		 * lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,"" + lDepOrd.getIdEventoGenerato()); lRetPage =
		 * lPage.toString(); } else {
		 */
		// Effettua la ricerca dei tenori eventualmente già presenti
		// questo per mostrare l'elenco degli oggetti selezionati.
		ITenore lCtrl = SIUSLookupRemote.getTenoreRemote();
		Vector lTenori = lCtrl.ExRicercaTenoreByGenProc(lIdGenProc);

		// Preleva eventuali tenori, per ricavare codice oggetto
		// e descrizione.
		// Questa istruzione è utile per riportare nella form
		// gli oggetti archiviati.
		if (lTenori != null) {
			Iterator itx = lTenori.iterator();
			while (itx.hasNext()) {
				TenoreModel lTenore = (TenoreModel) itx.next();
				lCodOggetti += lTenore.getCodOggettoTenore() + "|";
				lDescOggetti += lTenore.getDescrOggettoTenore() + "\n";
				// STUB 21/04/2004 Aggiunti i Codici dettaglio.
				if (lTenore.getCodDettaglioOggetto() != null && lTenore.getCodDettaglioOggetto().length() > 1) {
					lCodDettagli += lTenore.getCodOggettoTenore() + lTenore.getCodDettaglioOggetto() + "|";
				}
			}
		}

		setRequestAttribute("codOggetti", lCodOggetti);
		setRequestAttribute("descOggetti", lDescOggetti);
		setRequestAttribute("codDettagli", lCodDettagli); // STUB 21/04/2004

		// Preleva il cod Oggetto procedimento per poi passarlo alla Option
		String lCodOggettoProc = lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento();

		// Imposta Contenuto.
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

		Option lOption = new Option();

		if (strCodTipoUfficio.equals("TDS"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoTDS(),
					lCodOggettoProc, 75);
		else if (strCodTipoUfficio.equals("UDS"))
			lOption = new Option(DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance()
					.getOggettoProcedimentoUDS(), "U004"), lCodOggettoProc, 75);
		else
			lOption = new Option(DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance()
					.getOggettoProcedimento(), "U004"), lCodOggettoProc, 75);

		setRequestAttribute("contenuto", "" + lOption);
		setRequestAttribute("modalita", "IF");

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel lMagRel = lMagCtrl.ExRicercaEstesaMagRelByFascicolo(lFasGPMod
				.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("magistratorelatore", lMagRel);
		// this.setSessionAttribute("magistratorelatore", lMagRel);
		// }
		return lRetPage; // restituisce la jsp di VIEW

	}

}