package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
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
 * MEV_9: aggiunta action di caricamento dati
 *
 * @author Gioggi
 */
public class ActLoadInserisciConfermaDecisioneMagistratoRelatore extends ActRicercaFSPuntuale
		implements ICostantiDepositoDecreto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Imposta la pagina di ritorno
		String retPage = ICostantiDepositoDecreto.PG_LOAD_INSERISCI_CONFERMA_DECISIONE_MAGISTRATO_RELATORE;

		// info per il log
		siesLogger.debug("ActLoadInserisciConfermaDecisioneMagistratoRelatore: inizio!");
		setLinkRitorno();
		if (isRequestParameterNullObj("ritorno"))
			// Invoca la process Request della superclasse se si provine dal menu'.
			super.processRequest();

		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "fascicoloSiusGP non in sessione!");

		// Preleva il fascicoloGPModel dalla sessione e Recupera l'id generale procedimento
		FascicoloGPModel fgpm = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		if (Utils.isNullObj(fgpm.getGeneraleProcedimentoModel()))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Generale Procedimento assente!");
		BigDecimal idGP = fgpm.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();
		if (Utils.isNullObj(idGP))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Id Generale Procedimento assente!");

		// Richiesta di accesso al controller
		IDepositoDecreto idd = SIUSLookupRemote.getDepositoDecretoRemote();

		// Verifica esistenza di un deposito decreto per il fascicolo sius selezionato e tipo decreto
		if (idd.ExVerificaEsistenzaDepositoDecretoByIdGenProcCodTipoDec(idGP,
				DECRETO_DESIGNAZIONE_MAGISTRATO_RELATORE_PER_MA)) {
			// Se già esiste un decreto viene chiamato il dettaglio.
			DepositoDecretoModel ddm = idd.ExRicercaDepositoDecretoByGenProc(idGP,
					DECRETO_DESIGNAZIONE_MAGISTRATO_RELATORE_PER_MA);
			// Lancia messaggio di errore se il Deposito Decreto non esiste
			if (Utils.isNullObj(ddm))
				throw new SIUSException(SIUSException.USER_MESSAGE, "Deposito Decreto assente!");
			// Verifica se esiste l'id evento, altrimenti lancia un Messaggio di errore
			if (Utils.isNullObj(ddm.getIdEventoGenerato()))
				throw new SIUSException(SIUSException.USER_MESSAGE, "Errore nei dati: ID_EVENTO mancante!");

			// Richiesta di accesso al controller
			IEvento ie = SICOLookupRemote.getEventoRemote();
			// Ricerco l'evento legato al deposoto decreto
			EventoModel em = ie.ExRicercaEventoByKey(ddm.getIdEventoGenerato());
			String codEsito = !Utils.isNullObj(em.getCodEsito()) ? em.getCodEsito() : "";

			if ("0610".equals(codEsito))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Per il procedimento indicato è già stato emesso un provvedimento. "
								+ "Non è consentito emettere un nuovo provvedimento!");

			// Prepara la pagina di destinazione, il Dettaglio
			setRequestAttribute("depositodecretomodel", ddm);
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			rt.setAction(
					"siap.sius.depositodecreto.action.ActLoadDettaglioConfermaDecisioneMagistratoRelatore");
			rt.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, "" + ddm.getIdEventoGenerato());
			retPage = rt.toString();
		} else {
			MagistratoRelatoreModel mrm = null;

			String codOggettiTenore = new String();
			String descrOggettiTenore = new String();
			String codOggettoProcedimento = new String();
			String codDettagliOggetto = new String();

			// Esegue il controlle dello stato del fascicolo SIEP, nel caso di fascicolo archiviato
			// il metodo ereditato inserisce in request il messaggio di conferma personalizzato
			// per il caso di emissione provvedimento
			if (super.checkFascicoloSIEPArchiviatoPerEmissioneProvvedimento())
				return PG_WARNING;

			// Recupera l'elenco dei tenori.
			TenoreModel[] tm = fgpm.getTenori();
			for (int i = 0; i < tm.length; i++) {
				if (tm[i] != null) {
					codOggettiTenore += tm[i].getCodOggettoTenore() + "|";
					descrOggettiTenore += tm[i].getDescrOggettoTenore() + "\n";
					if (fgpm.getTenori()[i].getCodDettaglioOggetto() != null
							&& fgpm.getTenori()[i].getCodDettaglioOggetto().length() > 1)
						codDettagliOggetto += fgpm.getTenori()[i].getCodOggettoTenore()
								+ fgpm.getTenori()[i].getCodDettaglioOggetto() + "|";
				}
			}

			setRequestAttribute("codOggetti", codOggettiTenore);
			setRequestAttribute("descOggetti", descrOggettiTenore);
			setRequestAttribute("codDettagli", codDettagliOggetto);

			// Preleva il cod Oggetto procedimento per poi passarlo come contenuto
			codOggettoProcedimento = fgpm.getGeneraleProcedimentoModel().getCodOggettoProcedimento();

			if (codOggettoProcedimento == null)
				throw new SIUSException(SIUSException.USER_MESSAGE, "Oggetto Procedimento assente!");

			// Imposta Contenuto.
			setRequestAttribute("codContenuto", codOggettoProcedimento);
			String descContenuto = DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getOggettoProcedimento(), codOggettoProcedimento);
			setRequestAttribute("contenuto", descContenuto);

			// Ricerca del Magistrato Relatore
			IMagistratoRelatore imr = SIUSLookupRemote.getMagistratoRelatoreRemote();
			mrm = imr.ExRicercaEstesaMagRelByFascicolo(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
			setRequestAttribute("magistratorelatore", mrm);

			// Ricerca avvocati assegnati al fascicolo
			IAvvocato ia = SIUSLookupRemote.getAvvocatoRemote();
			Vector lAvvocato = ia
					.ExRicercaAvvocatiByFascicoloNoError(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
			setRequestAttribute("avvocato", lAvvocato);
		}

		// restituisce la jsp di VIEW
		return retPage;
	}

}