package siap.siep.modulocumulo.action;

/**
* <p>Title: ActLoadInserisciSollecitoRichiestaAttiPerTrasfCompCumulo</p>
* <p>Description: Classe Action per la load inserisci Sollecito Richiesta atti 	</p>
* <p>				 per Trasmissione Competenza Cumulo							</p>
*/

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.util.F3BException;
//import f3b.web.html.Option;
import f3b.web.html.Option;
import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadInserisciSollecitoRichiestaAttiPerTrasfCompCumulo extends ActionModuloCumulo
		implements ICostantiModuloCumulo, ICostantiMessaggio {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	Logger logger = Logger.getLogger("actionLogger");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		IstruttoriaCumuloModel IstruModel = super.getDatiIstruttoria();
		setRequestAttribute("IstrittoriaCumulo", IstruModel);

		FascicoloSiepModel lFasMod = null;
		IFascicoloSiep lCtrlF = SIEPLookupRemote.getFascicoloSiepRemote();
		lFasMod = lCtrlF.ExRicercaFascicoloByKey(IstruModel.getFasSieIdFascicoloSiep());

		BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
		IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
		MessaggioModel lMessaggio = lCrtl.ExRicercaMessaggioByKey(lIdMessage);

		// Codice autorità emittenete procedimento Classe IV a cui è iscritta la Misura Sicurezza Cumulo
		UfficioModel lUffMod = null;
		IUfficio CtrlU = SICOLookupRemote.getUfficioRemote();

		Option lOptionDestino = new Option(DecodificheManager.getInstance().getTipoUfficio(), "-");
		String[] lFiltroAut = { "PM", "PGCAP", "PMM" };
		lOptionDestino.setFilter(lFiltroAut);
		if (lMessaggio.getCodUfficioDestinatario() != null) {
			lUffMod = CtrlU.ExRicercaUfficioByCod(lMessaggio.getCodUfficioDestinatario());
			if (lUffMod != null && lUffMod.getCodUfficio() != null && lUffMod.getCodTipoUfficio() != null)
				lOptionDestino.setSelected(lUffMod.getCodTipoUfficio());
		}

		setRequestAttribute("ufficioDestinatarioSollecito", lUffMod);

		// OGGETTO Sollecito
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		DecodificheModel lModel = new DecodificheModel();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setCode("5203");
		Vector lVec = new Vector(lDecodifiche.ExRicercaDecodifiche(lModel));

		DecodificheModel oggettoSollecito = null;
		oggettoSollecito = (DecodificheModel) lVec.get(0);
		setRequestAttribute("oggettoProvvedimento", oggettoSollecito);
		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		//// LogF3B.getLogger()
		// siesLogger.debug("--XXX-- OggettoSoll = "+oggettoSollecito);

		// Magistrato Firmatario
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFasMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// ALTRO DESTINATARIO x la notifica
		Option lAEOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("autoritaEsternaN", "" + lAEOption);

		// Imposta la Modalità a Inserimento.
		setRequestAttribute("modalita", "I");

		setRequestAttribute("messaggioRichiesta", lMessaggio);

		// Restituisce la pagina di Inserimento dei Dati
		return PG_LOAD_INSERISCI_SOLLECITO_RICHIESTA_ATTI_TC_CUMULO;
	}
}