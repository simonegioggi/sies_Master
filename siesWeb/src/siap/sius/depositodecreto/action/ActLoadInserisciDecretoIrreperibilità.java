package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sius.SIUSException;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciDecretoInammissibilita
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di ActLoadInserisciDecretoInammissibilita
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
public class ActLoadInserisciDecretoIrreperibilità extends ActRicercaFSPuntuale
		implements ICostantiDepositoDecreto {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		MagistratoRelatoreModel lMagRel = null;

		// AvvocatoSiusModel lAvvSius = null;

		String lCodOggetti = new String();
		String lDescOggetti = new String();
		String lCodOggettoProc = new String();
		String lCodDettagli = new String(); // STUB 19/04/2004
		// BigDecimal lIdFasSius = null;

		String lRetPage = ICostantiDepositoDecreto.PG_LOAD_INSERISCI_DECRETO_IRREPERIBILITA;

		this.setLinkRitorno();
		if (this.isRequestParameterNullObj("ritorno")) {
			// Invoca la process Request della superclasse se provengo dal menu'.
			super.processRequest();
		}

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		// BigDecimal aIdFascicoloSius = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
		// .getFascicoloSiusModel().getIdFascicoloSius();
		BigDecimal lIdGenProc = lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();
		IDepositoDecreto lDepDecrCtrl = SIUSLookupRemote.getDepositoDecretoRemote();

		if (lDepDecrCtrl.ExVerificaEsistenzaDepositoDecretoByIdGenProcCodTipoDec(lIdGenProc,
				IRREPERIBILITA)) {
			// Se già esiste un decreto viene lanciato il dettaglio.
			DepositoDecretoModel lDepDec = lDepDecrCtrl.ExRicercaDepositoDecretoByGenProc(lIdGenProc,
					IRREPERIBILITA);

			if (lDepDec == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Deposito Decreto assente");

			if (lDepDec.getIdEventoGenerato() == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Errore nei dati: ID_EVENTO mancante");

			// Prepara la pagina di destinazione, il Dettaglio.
			setRequestAttribute("depositodecretomodel", lDepDec);
			RedirectTo lPage = new RedirectTo();
			lPage.setPage(IWebConstants.PG_MAIN);
			lPage.setAction("siap.sius.depositodecreto.action.ActLoadDettaglioDecretoIrreperibilità");
			lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + lDepDec.getIdEventoGenerato());
			lRetPage = lPage.toString();
		} else {
			// Se esiste un decreto di inammissibilità si impedisce l'inserimento
			if (lDepDecrCtrl.ExEsisteDepositoDecretoByGenProcCodEsito(lIdGenProc, "0002")) {
				// setta la risposta nella request
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Inserimento impossibile : Esiste un Decreto di irreperibilità");

				// Prepara la "pagina" di destinAction
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				lRedirigi.setAction(
						"siap.sius.depositodecreto.action.ActLoadFSPInserisciDecretoIrreperibilità");
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				lRetPage = IWebConstants.PG_MESSAGE;
			} else {
				// Esegue il controlle dello stato del fascicolo SIEP, nel caso di fascicolo archiviato
				// il metodo ereditato inserisce in request il messaggio di conferma personalizzato
				// per il caso di emissione provvedimento.
				if (super.checkFascicoloSIEPArchiviatoPerEmissioneProvvedimento())
					return PG_WARNING;

				// Preleva id fascicolo sius
				// lIdFasSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

				TenoreModel[] lTenori = lFasGPMod.getTenori();
				for (int i = 0; i < lTenori.length; i++) {
					if (lTenori[i] != null) {
						lCodOggetti += lTenori[i].getCodOggettoTenore() + "|";
						lDescOggetti += lTenori[i].getDescrOggettoTenore() + "\n";
						// STUB 19/04/2004 Aggiunti i Codici dettaglio.
						if (lFasGPMod.getTenori()[i].getCodDettaglioOggetto() != null
								&& lFasGPMod.getTenori()[i].getCodDettaglioOggetto().length() > 1) {
							lCodDettagli += lFasGPMod.getTenori()[i].getCodOggettoTenore()
									+ lFasGPMod.getTenori()[i].getCodDettaglioOggetto() + "|";
						}
					}
				}

				setRequestAttribute("codOggetti", lCodOggetti);
				setRequestAttribute("descOggetti", lDescOggetti);
				setRequestAttribute("codDettagli", lCodDettagli); // STUB 19/04/2004

				//
				// Preleva il cod Oggetto procedimento per poi passarlo come contenuto
				lCodOggettoProc = lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
				if (lCodOggettoProc == null)
					throw new SIUSException(SIUSException.USER_MESSAGE, "Oggetto Procedimento assente !");
				// Imposta Contenuto.
				setRequestAttribute("codContenuto", lCodOggettoProc);
				String lDescContenuto = DecodificheUtils.getDescbyCode(
						DecodificheManager.getInstance().getOggettoProcedimento(), lCodOggettoProc);
				setRequestAttribute("contenuto", lDescContenuto);

				//

				// Ricerca del Magistrato Relatore
				IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
				lMagRel = lMagCtrl.ExRicercaEstesaMagRelByFascicolo(
						lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
				setRequestAttribute("magistratorelatore", lMagRel);

				// Ricerca avvocati assegnati al fascicolo

				IAvvocato lAvvCtrl = SIUSLookupRemote.getAvvocatoRemote();
				Vector lAvvocato = lAvvCtrl.ExRicercaAvvocatiByFascicoloNoError(
						lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

				setRequestAttribute("avvocato", lAvvocato);

				// Imposta Tipo Ufficio con Trattino.
				Option lOption2 = new Option(DecodificheManager.getInstance().getTipoUfficioSiusTrattino());
				setRequestAttribute("tipoUfficioSIUSTrattino", "-" + lOption2);
			}
		}

		return lRetPage; // restituisce la jsp di VIEW
	}

}