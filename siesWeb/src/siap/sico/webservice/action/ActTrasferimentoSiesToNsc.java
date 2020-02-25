package siap.sico.webservice.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.statoprocedimento.controller.IStatoProcedimento;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

@SuppressWarnings("rawtypes")
public class ActTrasferimentoSiesToNsc extends ActWsBase {

	public String processRequest() throws Exception {

		if (!this.isSessionAttributeNullObj("fascicolo")) {
			// Se provengo del Menù Scelta Rapida devo recuparere i dati dalla sessione - Cerco in sessione il
			// fascicolo per recuperare l'id
			FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

			if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) // Controllo Validazione Fascicolo
			{
				RedirectTo lRedirigi = new RedirectTo();

				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
								+ " non è stato Validato. Impossibile effettuare il trasferimento!");
				lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
						+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

				return IWebConstants.PG_MESSAGE;
			} else {
				// ----> Blocco Inizio Disaccopiamento KEY SIES-NSC
				Vector lStatoProcedimento = CercaStatoProcedimento(lFascMod.getIdFascicoloSiep());
				if (lStatoProcedimento.size() > 0) {
					StatoProcedimentoModel lStatoProcedimentoModel = (StatoProcedimentoModel) lStatoProcedimento
							.elementAt(0);
					// Caso in cui abbiamo le Chiavi Accoppiate e lo Stato del procedimento è "Archiviato per
					// Non Luogo a Procedere:fascicolo archiviato per errore"
					// Disaccoppiare le chiavi
					if (lFascMod.getKeyProvvNsc() != null
							&& lStatoProcedimentoModel.getCodStatoProcedimento().equals("0305")) {
						return IWebConstants.ROOT_DIR
								+ "/files/siap/sico/webservice/TrasferimentoSiesToNscPerCancKey.jsp";
					}

					if (lStatoProcedimentoModel.getCodStatoProcedimento().equals("0305")) {
						RedirectTo lRedirigi = new RedirectTo();

						lRedirigi.setPage(IWebConstants.PG_MAIN);
						setRequestAttribute(IWebConstants.MESSAGE_TEXT,
								"Il Procedimento N." + lFascMod.getChiaveAnno() + "/"
										+ lFascMod.getChiaveProgr()
										+ " NON può essere trasferito in quanto iscritto per errore.");
						lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
								+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
						setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

						return IWebConstants.PG_MESSAGE;

					}
				}
				// ----> Fine Blocco

				// Se il Procedimento è arrivato da NSC non lo possso rimandare a NSC
				if (lFascMod.getKeyProvvNsc() != null
						&& lFascMod.getCodOperatoreInserimento().substring(0, 4).equals("nsc-")) {
					RedirectTo lRedirigi = new RedirectTo();

					lRedirigi.setPage(IWebConstants.PG_MAIN);
					setRequestAttribute(IWebConstants.MESSAGE_TEXT,
							"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
									+ " è stato trasferito da NSC. Impossibile effettuare il trasferimento!");
					lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
							+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
					setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

					return IWebConstants.PG_MESSAGE;
				} else {
					/*
					 * Se Il Procedimento è già stato inviato a NSC (KEY NSC presenti) lo possimao rispedire
					 * solo nel caso di un Aggiornamento dei dati precedentemente inviati. Chiediamo
					 * all'utente di Confermare o meno l'operazione.
					 */
					if (this.isRequestParameterNullObj("warning") && (lFascMod.getKeyProvvNsc() != null
							&& !lFascMod.getCodOperatoreInserimento().substring(0, 4).equals("nsc-"))) {
						setRequestAttribute(IWebConstants.ACTION_FIELD, "" + getClass().getName());
						setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N."
								+ lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
								+ " è già stato trasferito a NSC. Si vuole trasferire di nuovo il procedimento ?");
						return IWebConstants.ROOT_DIR
								+ "/files/siap/sico/webservice/WarningProcedimentoInviato.jsp";
					} else {
						return IWebConstants.ROOT_DIR
								+ "/files/siap/sico/webservice/TrasferimentoSiesToNsc.jsp";
					}
				}
			}

			/*
			 * else { return IWebConstants.ROOT_DIR +
			 * "/files/siap/sico/webservice/TrasferimentoSiesToNsc.jsp"; }
			 */
		} else {
			// Se non ho l'id fascicolo ne sulla request ne in sessione restituisco la
			// pagina di ricerca fascicolo
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO
					+ "siap.siep.fascicolo.action.ActLoadDettaglioFascicolo";
		}
	}

	private Vector CercaStatoProcedimento(BigDecimal aFascicoloSIEP) throws Exception {
		IStatoProcedimento lCtrlStatoProc = SIEPLookupRemote.getStatoProcedimentoRemote();
		Vector lStatoProcVector = new Vector();
		try {
			lStatoProcVector = lCtrlStatoProc.ExRicercaStatoProcedimentoByFascicoloSiep(aFascicoloSIEP);
		} catch (SIEPException e) {
			throw e;
		}
		return lStatoProcVector;
	}

}