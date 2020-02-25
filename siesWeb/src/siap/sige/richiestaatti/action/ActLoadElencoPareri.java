package siap.sige.richiestaatti.action;

import java.util.Collection;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadElencoPareri
 * </p>
 * <p>
 * Description: Caricamento pagina per la Ricerca delle richieste di parere.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActLoadElencoPareri extends ActRicercaFSigePuntuale implements ICostantiRichiestaAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio ");

		// Cstruzione opzioni "Tipo Parere"
		Collection lColMotivoParere = null;
		DecodificheModel lModel = new DecodificheModel();

		// Esegue il Lookup del Decodifiche Controller
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		// Imposta il contesto e il Filtro
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setFiltro("SIGE");
		lModel.setCode("0753");
		lColMotivoParere = lDecodifiche.ExRicercaDecodifiche(lModel);

		// DecodeModel lDecMod = new DecodeModel("0","Parere");
		// lColMotivoParere.add(lDecMod);

		// Option lOptionTipoParere = new Option(lColMotivoParere,"0",35 );
		// setRequestAttribute("colMotivoParere","" + lOptionTipoParere);
		Option lOptionTipoParere = new Option(lColMotivoParere, "0", 35);
		setRequestAttribute("colMotivoParere", "" + lOptionTipoParere);

		// Cstruzione opzioni "Contenuto"
		Collection lContenuti = null;
		if (this.getCodUfficioUtenteConnesso().compareToIgnoreCase("TDS") == 0)
			lContenuti = DecodificheManager.getInstance().getOggettoProcedimentoTDS();
		else if (this.getCodUfficioUtenteConnesso().compareToIgnoreCase("UDS") == 0)
			lContenuti = DecodificheManager.getInstance().getOggettoProcedimentoUDS();
		else
			lContenuti = DecodificheManager.getInstance().getOggettoProcedimento();

		Option lOptionContenuti = new Option(lContenuti, 50);
		setRequestAttribute("contenuto", "" + lOptionContenuti);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: fine ");

		return PG_LOAD_ELENCOPARERI;
	}

}