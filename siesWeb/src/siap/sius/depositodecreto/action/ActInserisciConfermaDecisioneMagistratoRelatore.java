package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
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
import siap.sius.util.SIUSLookupRemote;

public class ActInserisciConfermaDecisioneMagistratoRelatore extends ActionSius
		implements ICostantiDepositoDecreto {

	/**
	 * MEV_9: aggiunta action di inserimento dati
	 *
	 * @author Gioggi
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Si prelevano dati di sessione.
		String codUtenteConnesso = getCodUtenteConnesso();
		String codUfficioUtenteConnesso = getCodUfficioUtenteConnesso();
		String codComuneUtenteConnesso = getCodComuneUtenteConnesso();

		// Si preleva dall sessione il fascicolo GPModel.
		FascicoloGPModel fgpm = new FascicoloGPModel();
		fgpm = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Preleva id generale procedimento.
		BigDecimal idGeneraleProcedimento = fgpm.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();

		// Istanzia model generale procedimento.
		GeneraleProcedimentoModel gpm = new GeneraleProcedimentoModel();
		gpm.setIdGeneraleProcedimento(idGeneraleProcedimento);
		gpm.setCodOggettoProcedimento(getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_CONTENUTO));
		gpm.setDataAggiornamento(DateUtils.getSysDate());
		gpm.setCodUfficioAggiornamento(codUfficioUtenteConnesso);
		gpm.setCodOperatoreAggiornamento(codUtenteConnesso);

		// Ricerca del Magistrato Relatore associato al fascicolo
		// MagistratoRelatoreModel mrm = img
		// .ExRicercaEstesaMagRelByFascicolo(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
		// Se il magistrato relatore non esisteva, lo inserisco ex novo
		IMagistratoRelatore img = SIUSLookupRemote.getMagistratoRelatoreRemote();
		MagistratoRelatoreModel mrmNew = new MagistratoRelatoreModel();
		mrmNew.setMagCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		mrmNew.setEspIdEsperto(null);
		mrmNew.setFasSiuIdFascicoloSius(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
		mrmNew.setDataInizio(DateUtils.getSysDate());
		mrmNew.setCodRuoloMagistrato("02");
		mrmNew.setDataInserimento(DateUtils.getSysDate());
		mrmNew.setCodOperatoreInserimento(getCodUtenteConnesso());
		mrmNew.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		IMagistrato im = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel mm = new MagistratoModel();
		mm.setCodMagistrato(mrmNew.getMagCodMagistrato());
		mm.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		mm.setCognome(getRequestStringParameter(ICostantiMagistrato.CAMPO_COGNOME));
		mm.setNome(getRequestStringParameter(ICostantiMagistrato.CAMPO_NOME));
		Vector v = im.ExRicercaMagistrato(mm);
		if (!v.isEmpty()) {
			MagistratoModel mag = (MagistratoModel) v.get(0);
			if (mag.getDataFineValidita() != null
					&& (DateUtils.isLower(mag.getDataFineValidita(), DateUtils.getSysDate())
							|| DateUtils.isEquals(mag.getDataFineValidita(), DateUtils.getSysDate())))
				throw new SIUSException(SIUSException.USER_MESSAGE,
						"Attenzione! Impossibile emettere il Decreto. Assegnatario del procedimento è un magistrato non più in servizio!");
		}
		mrmNew = img.ExInserisciMagistratoRelatore(mrmNew);

		// Prelevare il codice Magistrato_Relatore
		String codMagistrato = mrmNew.getMagCodMagistrato();

		// Gestione oggetti Tenore
		String codOggetti = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_COD_OGGETTO);
		String descOggetti = getRequestStringParameter(ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO);
		// String codDettaglioOggetti = getRequestStringParameter(
		// ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO);

		// Inserisce nel model aggregante l'Array di model dei Tenori e Il model GeneraleProcedimento
		GPTenoreModel gptm = new GPTenoreModel();
		// TenoreModel[] tmArray = parseOggettiTenori(codOggetti, descOggetti, codDettaglioOggetti, "0271",
		// codMagistrato);
		// gptm.setTenori(tmArray);
		gptm.setTenori(parseOggettiTenori(codOggetti, descOggetti, codMagistrato));
		gptm.setGeneraleProcedimentoModel(gpm);

		// Prepara il model DepositoDecreto
		DepositoDecretoModel ddm = new DepositoDecretoModel();
		ddm.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));
		ddm.setCodTipoDecreto(DECRETO_DESIGNAZIONE_MAGISTRATO_RELATORE_PER_MA);
		ddm.setCodMagistrato(codMagistrato);
		ddm.setGenPridGeneraleProcedimento(idGeneraleProcedimento);
		ddm.setCodOperatoreInserimento(codUtenteConnesso);
		ddm.setCodUfficioInserimento(codUfficioUtenteConnesso);
		ddm.setDataInserimento(DateUtils.getSysDate());
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_TERMINE_EMISSIONE)
				&& !isRequestParameterNullObj(CAMPO_MESE_DATA_TERMINE_EMISSIONE)
				&& !isRequestParameterNullObj(CAMPO_GIORNO_DATA_TERMINE_EMISSIONE))
			ddm.setDataTermineEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_TERMINE_EMISSIONE,
					CAMPO_MESE_DATA_TERMINE_EMISSIONE, CAMPO_GIORNO_DATA_TERMINE_EMISSIONE));

		// Prepara Model Evento
		EventoModel em = new EventoModel();
		em.setCodTipoEvento("01"); // 01 = Provvedimento.
		em.setCodTipoProvvedimento("02"); // 02 = Decreto.
		em.setCodLuogoEmittente(codComuneUtenteConnesso);
		em.setCodUfficioEmittente(codUfficioUtenteConnesso);
		em.setCodEsito("0271");
		em.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));
		em.setFasSieIdFascicoloSiep(fgpm.getFascicoloSiusModel().getFasSieIdFascicoloSiep());
		em.setFasSiuIdFascicoloSius(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
		em.setCodOperatoreInserimento(codUtenteConnesso);
		em.setCodUfficioInserimento(codUfficioUtenteConnesso);
		em.setDataInserimento(DateUtils.getSysDate());
		em.setCodLuogoDestinatario("-");
		em.setCodTipoUfficioDestinatario("-");
		em.setCodUfficioDestinatario("-");

		DepositoDecretoEventoModel ddem = new DepositoDecretoEventoModel();
		ddem.setDepositoDecreto(ddm);
		ddem.setEvento(em);

		// Chiamata Controller per Inserimento
		IDepositoDecreto idd = SIUSLookupRemote.getDepositoDecretoRemote();
		ddem = idd.ExInserisciDecretoMagistratoRelatore(gptm, ddem);

		setRequestAttribute("modalita", "I");
		setRequestAttribute("idEvento", ddem.getEvento().getIdEvento());

		// Prepara la pagina di destinazione, in questo caso è il dettaglio del decreto di Conferma Decisione
		// del Magistrato Relatore
		RedirectTo rt = new RedirectTo();
		rt.setPage(IWebConstants.PG_MAIN);
		rt.setAction("siap.sius.depositodecreto.action.ActLoadDettaglioConfermaDecisioneMagistratoRelatore");
		rt.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, ddem.getEvento().getIdEvento().toString());
		// valore di ritorno
		return rt.toString();
	}

}