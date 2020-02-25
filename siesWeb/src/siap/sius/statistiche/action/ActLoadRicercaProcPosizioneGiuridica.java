package siap.sius.statistiche.action;

import java.util.ArrayList;
import java.util.Collection;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.cancelleriaassegnataria.controller.ICancelleriaAssegnataria;
import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;
import siap.sius.collaboratore.controller.ICollaboratore;
import siap.sius.util.SIUSLookupRemote;
import f3b.model.DecodeModel;
import f3b.util.F3BException;
import f3b.web.html.Option;

public class ActLoadRicercaProcPosizioneGiuridica extends ActionSiap implements ICostantiStatistiche {

	public String processRequest() throws Exception {

		// Imposta Contenuto.
		Option lOption = new Option();
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

		lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimento(), 75);
		setRequestAttribute("tipoAtto", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getPosizioneGiuridica(), 75);
		setRequestAttribute("posizioneGiuridica", "" + lOption);

		lOption = new Option(this.listaCancellerieAssegnatarie());
		lOption.setAddBlankItem(Option.BLANK_ITEM);
		lOption.setValueBlankItem("-");
		setRequestAttribute("cancelleriaAssegnataria", "" + lOption);

		// Imposta Tipo Ufficio con Trattino.
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiusTrattino());
		setRequestAttribute("tipoUfficioSIUSTrattino", "" + lOption);

		// STUB 16/06/2004 Integrazione ricerca avanzata.
		setRequestAttribute("TipoUfficioConnesso", strCodTipoUfficio);
		String strDescrComune = getUfficioUtenteConnesso().getDescrComune();
		setRequestAttribute("ComuneUfficioConnesso", strDescrComune);

		// STUB 18/02/2005 Imposta l'elenco magistrati.
		String lCodUfficio = getCodUfficioUtenteConnesso();
		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		lOption = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio));
		lOption.setAddBlankItem(true);
		lOption.setValueBlankItem("Tutti");
		setRequestAttribute("magistrato", "" + lOption);

		// ricercaCancellerieAssegnatarie();
		filtroCollaboratore();

		return PG_LOAD_RICERCA_PROCPOSZIONEGIURIDICA; // restituisce la jsp di VIEW
	}

	/**
	 * Ricerca delle Cancellerie Assegnatarie definite per l'Ufficio. Se trovate le Cancellerie vengono
	 * passate nella request.
	 * 
	 * @throws F3BException
	 */
//	private void ricercaCancellerieAssegnatarie() throws F3BException {
//		// Prepara il model di ricerca delle Cancellerie Assegnatarie previste per l'Ufficio dell'uttente
//		CancelleriaAssegnatariaModel lCancAssModel = new CancelleriaAssegnatariaModel();
//		lCancAssModel.setCodUfficio(getCodUfficioUtenteConnesso());
//
//		// Ricerca
//		ICancelleriaAssegnataria lCancAssCtrl = SIUSLookupRemote.getCancelleriaAssegnatariaRemote();
//		Vector lElencoCancellerie = lCancAssCtrl.ExRicercaCancelleriaAssegnataria(lCancAssModel);
//		if (lElencoCancellerie != null && lElencoCancellerie.size() > 0) {
//			setRequestAttribute("cancellerie", lElencoCancellerie);
//		}
//	}

	/**
	 * Ricerca delle Cancellerie Assegnatarie definite per l'Ufficio. Se trovate le Cancellerie.
	 * 
	 * @throws F3BException
	 */
	@SuppressWarnings("unchecked")
	protected Collection<DecodeModel> listaCancellerieAssegnatarie() throws F3BException {

		Collection<DecodeModel> lElencoCbx = new ArrayList<DecodeModel>();
		Collection<CancelleriaAssegnatariaModel> lElenco = new Vector<CancelleriaAssegnatariaModel>();
		// Prepara il model di ricerca delle Cancellerie Assegnatarie previste per l'Ufficio dell'uttente
		CancelleriaAssegnatariaModel lCancAssModel = new CancelleriaAssegnatariaModel();
		lCancAssModel.setCodUfficio(getCodUfficioUtenteConnesso());
		// Ricerca
		ICancelleriaAssegnataria lCancAssCtrl = SIUSLookupRemote.getCancelleriaAssegnatariaRemote();
		lElenco = lCancAssCtrl.ExRicercaCancelleriaAssegnataria(lCancAssModel);
		// Iterazione per l'elenco ritornato dal controller e "rimappato" in DecodeModel (Zzzzz....)
		for (CancelleriaAssegnatariaModel lCancAss : lElenco) {
			lElencoCbx.add(new DecodeModel(lCancAss.getCodCancelleriaAssegnataria(), lCancAss
					.getDescCancelleriaAssegnataria()));
		}
		return lElencoCbx;
	}

	/**
	 * La funzione abilita il filtro nella Ricerca sul Collaboratore di Giustizia.
	 * 
	 * @throws F3BException
	 */
	private void filtroCollaboratore() throws F3BException {

		// Si controlla se esiste l'interfaccia per la Gestione Collaboratore di Giustizia
		ICollaboratore lCtrl = SIUSLookupRemote.getCollaboratoreRemote();
		if (lCtrl.ExIsPackage()) {
			setRequestAttribute("collaboratore", "SI");
		}
	}

}