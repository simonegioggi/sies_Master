package siap.sige.fogliocomplementare.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sige.documentoallegato.controller.IDocumentoAllegato;
import siap.sige.documentoallegato.model.DocumentoAllegatoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import siap.sius.documentoallegato.action.ICostantiDocumentoAllegato;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActInserisciCompFoglioComp extends ActionSige implements ICostantiFoglioComp{
	public String processRequest() throws Exception {
	    // Gestione del punto di ritorno
	    setLinkRitorno();
        
	    // Controller del DocumentoAllegato
	    IDocumentoAllegato lDocCtrl = SIGELookupRemote.getDocumentoAllegatoController();
	   
	    // Evento. Leggo l'evento collegato al decreto.
	    BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
	    

	    // Valorizzazione del Documento Allegato
	    DocumentoAllegatoModel aDocAllegato = new DocumentoAllegatoModel();
	    aDocAllegato.setDataInsMan( getRequestDateParameter( CAMPO_ANNO_DATA_INS_MANUALE,
	    		                                                CAMPO_MESE_DATA_INS_MANUALE,
	    		                                                CAMPO_GIORNO_DATA_INS_MANUALE ) );
	    
	    aDocAllegato.setCodTipoDocumento("06");
	    aDocAllegato.setAnnoFoglioComplementare(new BigDecimal(DateUtils.getSysDate("yyyy")));
  
	    aDocAllegato.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE,
	                                                            CAMPO_MESE_DATA_TRASMISSIONE,
	                                                            CAMPO_GIORNO_DATA_TRASMISSIONE ) );
	    aDocAllegato.setEveIdEvento(lIdEvento);
	    aDocAllegato.setCodOperatoreInserimento(this.getCodUtenteConnesso());
	    aDocAllegato.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
	    aDocAllegato.setDataInserimento(DateUtils.getSysDate());
	    
	    aDocAllegato = lDocCtrl.ExInserisciFoglioComplementare(aDocAllegato);
	    //return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.provvedimento.action.ActLoadInserisciCompFoglioComp&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+lEveMod.getIdEvento().toString();
	    // Ticket#20230202011 - si aggancia la stessa funzione di dettaglio agganciata dalla nuova lista
	    // return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sige.fogliocomplementare.action.ActLoadDettaglioCompFoglioComp&"+ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO+"="+aDocAllegato.getIdDocumentoAllegato();
	    return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sige.fogliocomplementare.action.ActLoadDettaglioFCTastoFunzione&"+ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO+"="+aDocAllegato.getIdDocumentoAllegato();
	    // Ticket#20230202011 - FINE
	}  	 
}