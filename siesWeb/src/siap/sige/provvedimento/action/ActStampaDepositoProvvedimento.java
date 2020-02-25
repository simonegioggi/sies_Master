package siap.sige.provvedimento.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.ufficio.model.UfficioModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import siap.sius.documentoallegato.action.ICostantiDocumentoAllegato;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActStampaDepositoProvvedimento extends ActionSige implements ICostantiProvvedimentoSige
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	/**
  * Azione di Stampa del Deposito Provvedimento
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws Exception
  */
  public String processRequest() throws Exception
  {

    // Fascicolo Sige Esteso in sessione.
  	FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");

    String lId = getRequestStringParameter( ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO);
    BigDecimal idEvento=null;
    try {
    	idEvento=super.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    } catch (Exception e) {
    	
    }
    

    DocumentoAllegatoModel lDAMod = new DocumentoAllegatoModel();
    // chiama il controller di Documento Allegato.
    IDocumentoAllegato lCtrlDA = SIUSLookupRemote.getDocumentoAllegatoRemote();
    lDAMod = lCtrlDA.ExRicercaDocumentoAllegatoByKey(new BigDecimal(lId));

    //lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
    lDAMod.setIdDocumentoAllegato(new BigDecimal(lId));

    UfficioModel lUff = new UfficioModel(this.getUfficioUtenteConnesso());

    lDAMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lDAMod.setCodUfficioAggiornamento(lUff.getCodUfficio());
    lDAMod.setDataAggiornamento(DateUtils.getSysDate());
    lDAMod.setFlagDocumentoRegistrato("N");
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info(">>>> Id del template prelevato dal documentoAllegato : "  + lDAMod.getTemIdTemplate() ) ;

    lDAMod.setDescrUfficioAggiornamento(lUff.getDescrTipoUfficio());

    IProvvedimentoSige lCtrl = SIGELookupRemote.getProvvedimentoRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumentoAllegato(lFasEsteso.getFascicoloSige().getIdFascicoloSige(),lDAMod, lUff.getCodUfficio(), super.getUtenteConnesso(), idEvento);

    setRequestAttribute("documentoAllegato", lDAMod);
    setRequestAttribute("report", lReport);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.info( ">>> ID del template archiviato in documentoAllegato : " + lDAMod.getTemIdTemplate());

 //   return IWebConstants.PG_DOWNLOAD;
    return IWebConstants.PG_DOWNLOAD_NEW;
  }
}