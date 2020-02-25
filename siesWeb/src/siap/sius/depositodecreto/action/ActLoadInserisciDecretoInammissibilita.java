package siap.sius.depositodecreto.action;

import java.math.BigDecimal;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.provvedimento.util.RicercaProvvedimentiUtil;
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
public class ActLoadInserisciDecretoInammissibilita extends ActRicercaFSPuntuale
		implements ICostantiDepositoDecreto {

	public String processRequest() throws Exception {

		MagistratoRelatoreModel lMagRel = null;
		String lRetPage = ICostantiDepositoDecreto.PG_LOAD_INSERISCI_DECRETO_INAMMISSIBILITA;

		String lCodOggetti = new String();
		String lDescOggetti = new String();
		String lCodOggettoProc = new String();
		String lCodDettagli = new String(); // STUB 19/04/2004
		// BigDecimal lIdFasSius = null;

		if (this.isRequestParameterNullObj("ritorno")) {
			// Invoca la process Request della superclasse se provengo dal menu'.
			super.processRequest();
		}

		if (this.isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "fascicoloSiusGP non in sessione");

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		BigDecimal lIdGenProc = lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();
		if (lIdGenProc == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Id Generale Procedimento assente");

		IDepositoDecreto lDepDecrCtrl = SIUSLookupRemote.getDepositoDecretoRemote();

		// Verifica esistenza di un deposito decreto per il fasciclo sius
		// selezionato.
		if (lDepDecrCtrl.ExVerificaEsistenzaDepositoDecretoByIdGenProcCodTipoDec(lIdGenProc,
				INAMMISSIBILITA)) {
			// Se già esiste un decreto viene lanciato il dettaglio Luigi 20-11-2003
			DepositoDecretoModel lDepDec = lDepDecrCtrl.ExRicercaDepositoDecretoByGenProc(lIdGenProc,
					INAMMISSIBILITA);

			if (lDepDec == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Deposito Decreto assente");

			if (lDepDec.getIdEventoGenerato() == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Errore nei dati: ID_EVENTO mancante");

			// Prepara la pagina di destinazione, il Dettaglio.
			RedirectTo lPage = new RedirectTo();
			lPage.setPage(IWebConstants.PG_MAIN);
			lPage.setAction("siap.sius.depositodecreto.action.ActLoadDettaglioDecretoInammissibilita");
			lPage.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + lDepDec.getIdEventoGenerato());
			lRetPage = lPage.toString();
		} else {
			// Viene effettuato il controllo sulla preesistenza di un Provvedimento declaratorio
			// già emesso per il Fascicolo SIUS.
			// Se esiste almeno un provvedimento di questo tipo non può esserne emesso un altro.
			RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(lIdGenProc);
			if (lRicerca.verificaEsistenzaProv())
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Per il procedimento indicato è già stato emesso un provvedimento. Non è consentito emettere un nuovo provvedimento");
			else {
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

				// Preleva il cod Oggetto procedimento per poi passarlo come contenuto
				lCodOggettoProc = lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
				if (lCodOggettoProc == null)
					throw new SIUSException(SIUSException.USER_MESSAGE, "Oggetto Procedimento assente !");
				// Imposta Contenuto.
				setRequestAttribute("codContenuto", lCodOggettoProc);
				String lDescContenuto = DecodificheUtils.getDescbyCode(
						DecodificheManager.getInstance().getOggettoProcedimento(), lCodOggettoProc);
				setRequestAttribute("contenuto", lDescContenuto);

				// Ricerca del Magistrato Relatore
				IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
				lMagRel = lMagCtrl.ExRicercaEstesaMagRelByFascicolo(
						lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
				setRequestAttribute("magistratorelatore", lMagRel);
			}
		}
		return lRetPage; // restituisce la jsp di VIEW
	}

}