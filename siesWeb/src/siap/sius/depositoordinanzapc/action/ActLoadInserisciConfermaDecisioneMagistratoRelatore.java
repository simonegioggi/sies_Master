package siap.sius.depositoordinanzapc.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.SIUSException;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.provvedimento.util.RicercaProvvedimentiUtil;
import siap.sius.tenore.controller.ITenore;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienza.controller.IUdienza;
import siap.sius.udienza.model.UdienzaModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * MEV_2019-09: aggiunta action di caricamento dati
 *
 * @author Gioggi
 */
public class ActLoadInserisciConfermaDecisioneMagistratoRelatore extends ActRicercaFSPuntuale
		implements ICostantiDepositoOrdinanzaPc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Imposta la pagina di ritorno
		String retPage = PG_LOAD_INSERISCI_CONFERMA_DECISIONE_MAGISTRATO_RELATORE;

		// info per il log
		siesLogger.debug("ActLoadInserisciConfermaDecisioneMagistratoRelatore: inizio!");
		setLinkRitorno();
		// 20231206: trasformo in warning su osservazione di Luigi G.
		if (isRequestParameterNullObj("ritorno") && isRequestParameterNullObj("warning"))
			// Invoca la process Request della superclasse se si proviene dal menu'
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

		// Devono esistere 1) ordinanza applicazione provvisoria, 2) data esecutività e 3) data udienza
		// Ricerco evento del fascicolo: COD_TIPO_PROVVEDIMENTO 03 COD_MOTIVO 0680 COD_ESITO 0270
		// Ordinanza Affidamento in Prova al Servizio Sociale (Art. 47 O.P. - Art. 678 comma 1-ter
		// c.p.p.) - Applica provvisoriamente
		IEvento ie = SICOLookupRemote.getEventoRemote();
		Vector<?> v = ie.ExRicercaEventoByFascicoloSius(fgpm.getFascicoloSiusModel().getIdFascicoloSius(),
				null);
		boolean existOrdinanzaApplicazioneProvvisoria = false;
		BigDecimal idEventoOrdinanza = null;
		for (int i = 0; i < v.size(); i++) {
			EventoModel em = (EventoModel) v.elementAt(i);
			if ("0270".equals(em.getCodEsito()) && "S".equals(em.getFlagDocumentoRegistrato())
					&& em.getNumAllValidati() > 0) {
				existOrdinanzaApplicazioneProvvisoria = true;
				idEventoOrdinanza = em.getIdEvento();
				break;
			}
		}
		if (!existOrdinanzaApplicazioneProvvisoria)
			// MEV_2024-092: cambio messaggio da Provvisoria M.A. a Misure Alternative Dl 123/2018
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Operazione consentita solo se sul Procedimento sia stata emessa un'ordinanza di "
							+ "Applicazione Misure Alternative Dl 123/2018 con esito 'Applica provvisoriamente' "
							+ "depositata e validata!");

		IDepositoOrdinanzaPc idopc = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		// DepositoOrdinanzaPcModel dopcm = idopc.ExRicercaDepositoOrdinanzaPcByGenProcTipoOrd(
		// fgpm.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(), "AM");
		DepositoOrdinanzaPcModel dopcm = idopc.ExRicercaDepositoOrdinanzaPcByEvento(idEventoOrdinanza);
		// 20231206: trasformo in warning su osservazione di Luigi G.
		if (!Utils.isNullObj(dopcm) && Utils.isNullObj(dopcm.getDataEsecutivita())
				&& isRequestParameterNullObj("warning")) {
			// throw new SIUSException(SIUSException.USER_MESSAGE,
			// "L'Ordinanza di Applicazione Provvisoria è priva della Data Esecutività!");
			setRequestAttribute(IWebConstants.ACTION_FIELD, "" + getClass().getName());
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"L'Ordinanza di Applicazione Misure Alternative Dl 123/2018 &egrave; priva della Data "
					+ "Esecutivit&agrave;. Si vuole procedere con la Ratifica?");
			// pagina di ritorno
			return IWebConstants.PG_WARNING;
		}

		UdienzaModel um = null;
		if (Utils.isNullObj(fgpm.getGeneraleProcedimentoModel().getUdiIdUdienza()))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Data Udienza assente per il procedimento!");
		else {
			IUdienza iu = SIUSLookupRemote.getUdienzaRemote();
			um = iu.ExRicercaUdienzaByKey(fgpm.getGeneraleProcedimentoModel().getUdiIdUdienza());
			if (Utils.isNullObj(um) || Utils.isNullObj(um.getDataUdienza()))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Data Udienza assente per il procedimento!");
		}

		setRequestAttribute("dataUdienzaStr", DateUtils.getDateToString(um.getDataUdienza(), "dd/MM/yyyy"));
		// dati x l'ordinanza di Applicazione Misure Alternative Dl 123/2018 (ex Provvisoria M.A.)
		String descrTipoOrdinanza = (dopcm.getCodTipoOrdinanza() != null)
				? (DecodificheUtils.getDescbyCode(DecodificheManager.getInstance().getTipoOrdinanza(),
						dopcm.getCodTipoOrdinanza()))
				: "";
		dopcm.setDescrTipoOrdinanza(descrTipoOrdinanza);
		setRequestAttribute("dopcm", dopcm);

		// Viene effettuato il controllo sulla preesistenza di un Provvedimento declaratorio
		// già emesso per il Fascicolo SIUS.
		// Se esiste almeno un provvedimento di questo tipo non può esserne emesso un altro.
		RicercaProvvedimentiUtil rpu = new RicercaProvvedimentiUtil(idGP);
		boolean esisteProv = rpu.verificaEsistenzaProv();
		if (esisteProv)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Per il procedimento indicato è già stato emesso un provvedimento. "
							+ "Non è consentito emettere un nuovo provvedimento");

		// Preleva il cod Oggetto procedimento per poi passarlo come contenuto
		String codOggettoProcedimento = fgpm.getGeneraleProcedimentoModel().getCodOggettoProcedimento();

		if (codOggettoProcedimento == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Contenuto Procedimento assente!");

		TenoreModel[] tm = fgpm.getTenori();
		if (tm.length == 0) {
			// setta la risposta nella request
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Oggetto Procedimento assente!");
			// Prepara la "pagina" di destinAction
			RedirectTo rt = new RedirectTo();
			rt.setPage(IWebConstants.PG_MAIN);
			rt.setAction("siap.sius.fascicolo.action.ActLoadDettaglioFascicolo");
			rt.setParameter(CAMPO_ID_FASCICOLO_SIUS,
					fgpm.getFascicoloSiusModel().getIdFascicoloSius().toString());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
			// valore di ritorno
			return IWebConstants.PG_MESSAGE; /* rt.toString(); */
		}

		// Imposta Contenuto.
		setRequestAttribute("codContenuto", codOggettoProcedimento);
		String descContenuto = DecodificheUtils.getDescbyCode(
				DecodificheManager.getInstance().getOggettoProcedimento(), codOggettoProcedimento);
		setRequestAttribute("descContenuto", descContenuto);

		// cerco i tenori dell'ordinanza di applicazione Misure Alternative Dl 123/2018 (ex provvisoria M.A.)
		ITenore it = SIUSLookupRemote.getTenoreRemote();
		Vector<?> tenoriOrdinanza = it.ExRicercaTenoreByOrdinanza(dopcm.getIdDepositoOrdinanzaPc());
		int dimFinale = 0;
		for (int i = 0; i < tenoriOrdinanza.size(); i++) {
			// Deve essere riportato solo l'oggetto che è stato applicato provvisoriamente
			if ("0270".equals(((TenoreModel) tenoriOrdinanza.get(i)).getCodEsitoTenore()))
				dimFinale += 1;
		}
		if (dimFinale == 0)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Esito 'Applica Provvisoriamente' assente!");

		String[] codOggettiTenore = new String[dimFinale];
		String[] descrOggettiTenore = new String[dimFinale];
		String[] codDettagliOggetto = new String[dimFinale];
		// motivi
		String[] codMotiviProvvedimento = new String[dimFinale];
		String[] descrMotiviProvvedimento = new String[dimFinale];
		// esiti
		String[] codEsitiTenore = new String[dimFinale];

		String codMotivoProvvedimento = null;
		String descrMotivoProvvedimento = null;

		// Recupera l'elenco dei tenori
		for (int i = 0; i < dimFinale; i++) {
			// Deve essere riportato solo l'oggetto che è stato applicato provvisoriamente
			if ("0270".equals(((TenoreModel) tenoriOrdinanza.get(i)).getCodEsitoTenore())) {
				// dati per questa ordinanza di Conferma Decisione Magistrato Relatore
				codMotivoProvvedimento = DecodificheUtils.getCodebyCodAlt2(
						DecodificheManager.getInstance().getMotivoProvvedimento(),
						((TenoreModel) tenoriOrdinanza.get(i)).getCodOggettoTenore());
				codMotiviProvvedimento[i] = codMotivoProvvedimento;
				descrMotivoProvvedimento = DecodificheUtils.getDescbyCode(
						DecodificheManager.getInstance().getMotivoProvvedimento(), codMotivoProvvedimento);
				descrMotiviProvvedimento[i] = descrMotivoProvvedimento;
				codOggettiTenore[i] = codMotivoProvvedimento;
				descrOggettiTenore[i] = descrMotivoProvvedimento;
				codEsitiTenore[i] = getEsito(codMotivoProvvedimento);
				codDettagliOggetto[i] = "-";
			}
		}

		setRequestAttribute("codOggetti", codOggettiTenore);
		setRequestAttribute("descOggetti", descrOggettiTenore);
		setRequestAttribute("codDettagli", codDettagliOggetto);
		setRequestAttribute("codMotiviProvvedimento", codMotiviProvvedimento);
		setRequestAttribute("descrMotiviProvvedimento", descrMotiviProvvedimento);
		setRequestAttribute("codEsitiTenore", codEsitiTenore);
		setRequestAttribute("codTipoOrdinanza", "CM");

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore imr = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel mrm = imr
				.ExRicercaEstesaMagRelByFascicolo(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("magistratorelatore", mrm);

		// Ricerca avvocati assegnati al fascicolo
		IAvvocato ia = SIUSLookupRemote.getAvvocatoRemote();
		Vector lAvvocato = ia
				.ExRicercaAvvocatiByFascicoloNoError(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("avvocato", lAvvocato);

		// info per il log
		siesLogger.debug("ActLoadInserisciConfermaDecisioneMagistratoRelatore: fine!");

		// restituisce la jsp di VIEW
		return retPage;
	}

	// Preleva gli esiti dalla CG_REF_CODES
	private String getEsito(String codiceOggetti) throws Exception {

		IDecodifiche id = SICOLookupRemote.getDecodificheRemote();
		Collection<?> c = id.ExRicercaEsitiByOggetto(codiceOggetti);
		// INIZIO rimuovere se andrà messo anche l'esito di NON CONFERMA
		Iterator<?> i = c.iterator();
		while (i.hasNext()) {
			DecodificheModel dm = (DecodificheModel) i.next();
			if ("0272".equals(dm.getCodiceAlternativo()))
				i.remove();
		}
		// FINE rimuovere
		Option o = new Option(c, false);
		return o.toString();
	}

}