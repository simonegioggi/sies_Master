package siap.sius.depositodecreto.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
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
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.util.SIUSLookupRemote;

public class ActModificaDesignazioneMagistratoRelatore extends ActionSius
		implements ICostantiDepositoDecreto {

	/**
	 * MEV_9: aggiunta action di modifica dati
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

		// Prelevare il codice Magistrato_Relatore
		String codMagistrato = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);

		IMagistratoRelatore img = SIUSLookupRemote.getMagistratoRelatoreRemote();
		// Ricerca del Magistrato Relatore associato al fascicolo
		MagistratoRelatoreModel mrm = img
				.ExRicercaEstesaMagRelByFascicolo(fgpm.getFascicoloSiusModel().getIdFascicoloSius());
		if (Utils.isNullObj(mrm) || !mrm.getMagCodMagistrato().equals(codMagistrato)) {
			// Se il magistrato relatore non esisteva, oppure se è diverso dal vecchio, lo inserisco ex novo
			MagistratoRelatoreModel mrmNew = new MagistratoRelatoreModel();
			mrmNew.setMagCodMagistrato(codMagistrato);
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
		}

		// Prepara il model DepositoDecreto
		BigDecimal idDepositoDecreto = getRequestBigDecimalParameter(
				ICostantiDepositoDecreto.CAMPO_ID_DEPOSITO_DECRETO);
		IDepositoDecreto idd = SIUSLookupRemote.getDepositoDecretoRemote();
		DepositoDecretoModel ddmOLD = idd.ExRicercaDepositoDecretoByKey(idDepositoDecreto);
		DepositoDecretoModel ddm = new DepositoDecretoModel(ddmOLD);
		ddm.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));
		ddm.setCodMagistrato(codMagistrato);
		ddm.setCodOperatoreAggiornamento(codUtenteConnesso);
		ddm.setCodUfficioAggiornamento(codUfficioUtenteConnesso);
		ddm.setDataAggiornamento(DateUtils.getSysDate());
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_TERMINE_EMISSIONE)
				&& !isRequestParameterNullObj(CAMPO_MESE_DATA_TERMINE_EMISSIONE)
				&& !isRequestParameterNullObj(CAMPO_GIORNO_DATA_TERMINE_EMISSIONE))
			ddm.setDataTermineEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_TERMINE_EMISSIONE,
					CAMPO_MESE_DATA_TERMINE_EMISSIONE, CAMPO_GIORNO_DATA_TERMINE_EMISSIONE));
		if (!isRequestParameterNullObj(CAMPO_NUMERO_GIORNI_TERMINE_EMISSIONE))
			ddm.setNumGiorniTermineEmissione(
					getRequestBigDecimalParameter(CAMPO_NUMERO_GIORNI_TERMINE_EMISSIONE));

		// Prepara Model Evento
		BigDecimal idEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		// Richiesta di accesso al controller
		IEvento ie = SICOLookupRemote.getEventoRemote();
		// Ricerco l'evento legato al deposoto decreto
		EventoModel emOLD = ie.ExRicercaEventoByKey(idEvento);
		EventoModel em = new EventoModel(emOLD);
		em.setCodLuogoEmittente(codComuneUtenteConnesso);
		em.setCodUfficioEmittente(codUfficioUtenteConnesso);
		em.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));
		em.setCodOperatoreAggiornamento(codUtenteConnesso);
		em.setCodUfficioAggiornamento(codUfficioUtenteConnesso);
		em.setDataAggiornamento(DateUtils.getSysDate());

		DepositoDecretoEventoModel ddem = new DepositoDecretoEventoModel();
		ddem.setDepositoDecreto(ddm);
		ddem.setEvento(em);

		// Chiamata Controller per modifica
		idd.ExModificaDecretoMagistratoRelatore(ddem);

		setRequestAttribute("idEvento", idEvento);

		// Prepara la pagina di destinazione, in questo caso è il dettaglio del decreto di Designazione del
		// Magistrato Relatore
		RedirectTo rt = new RedirectTo();
		rt.setPage(IWebConstants.PG_MAIN);
		rt.setAction("siap.sius.depositodecreto.action.ActLoadDettaglioDesignazioneMagistratoRelatore");
		rt.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, idEvento.toString());
		// valore di ritorno
		return rt.toString();
	}

}