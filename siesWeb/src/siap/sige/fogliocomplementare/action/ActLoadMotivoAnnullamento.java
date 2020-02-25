package siap.sige.fogliocomplementare.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sige.documentoallegato.controller.IDocumentoAllegato;
import siap.sige.documentoallegato.model.DocumentoAllegatoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;

public class ActLoadMotivoAnnullamento extends ActionSige implements ICostantiFoglioComp {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	public String processRequest() throws Exception
    {
	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
	    gestioneRitorno();
	    BigDecimal idFc =super.getRequestBigDecimalParameter(CAMPO_ID_DOCUMENTO_ALLEGATO);
	    IDocumentoAllegato iCtrl = SIGELookupRemote.getDocumentoAllegatoController();
	    DocumentoAllegatoModel fc=iCtrl.ExRicercaFCById(idFc);
        super.setRequestAttribute("fc", fc);
	    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	    siesLogger.debug( getClass().getName() + ".processRequest: fine" );
	    return PG_LOAMOTIVIDANNULLAMENTOFOGLIOCOMP;
  }
}