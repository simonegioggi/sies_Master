package siap.sige.provvedimento.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sico.ufficio.model.UfficioModel;
import siap.sige.SIGEException;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActStampaOrdinanzaSospensione extends ActionSige implements ICostantiProvvedimentoSige
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	/**
  * Azione di Stampa dell'Ordinanza
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws Exception
  */
  public String processRequest() throws Exception
  {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug("ActStampaOrdinanzaSospensione : Inizio");

    //FascicoloGPModel lFasGPMod = new FascicoloGPModel();
    //lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

    // Fascicolo Sige Esteso in sessione.
  	FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");

   
    // chiama il controller x leggere il ProvvedimentoSigeEventoModel.
    IProvvedimentoSige lCtrl = SIGELookupRemote.getProvvedimentoRemote();
    ProvvedimentoSigeEventoModel lProEveMod = lCtrl.ExRicercaProvvedimentoDefinitorioByIdFascicolo(lFasEsteso.getFascicoloSige().getIdFascicoloSige() );

    //lProEveMod = new ProvvedimentoSigeEventoModel() ;
    //lProEveMod.getEvento().setFasSieIdFascicoloSiep(lFasEsteso.getFascicoloSiep().getIdFascicoloSiep());

    String lId = getRequestStringParameter( ICostantiEvento.CAMPO_ID_EVENTO);
    EventoModel lEveMod = new EventoModel();
    lEveMod.setIdEvento(new BigDecimal(lId));

    UfficioModel lUff = new UfficioModel(this.getUfficioUtenteConnesso());

    lEveMod.setDescrLuogoEmittente(lUff.getDescrComune());
    lEveMod.setDescrUfficioEmittente(lUff.getDescrTipoUfficio());


    lEveMod.setDataAggiornamento(DateUtils.getSysDate());
    lEveMod.setCodUfficioAggiornamento(lUff.getCodUfficio());
    lEveMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lEveMod.setFlagDocumentoRegistrato("N");
    
    // Se il template non è stato definito sono in un Emissione Ordinanza UDS
    if(lEveMod.getTemIdTemplate() == null || lEveMod.getTemIdTemplate().trim().length()<1)
    {
    	if(!isRequestParameterNullObj(ICostantiTemplate.CAMPO_ID_TEMPLATE))
    		lEveMod.setTemIdTemplate(this.getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE));
    	else
    		lEveMod.setTemIdTemplate("SIGE_OR_010") ;
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("ID TEMPLATE -> : "+ lEveMod.getTemIdTemplate());

    }

    //lEveMod.setNomeTemplate(TEMPLATE_ORDINANZA);
    //lProEveMod.setNomeTemplate("SIGE_OR_010");
    //if (lProEveMod.getNomeTemplate() != null)
    //  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    //  siesLogger.info("Nome del template di stampa ordinanza : " + lProEveMod.getNomeTemplate());
    //else
    //  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    //  siesLogger.info("Nome del template di stampa ordinanza assente ");

    IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();

    ByteArrayOutputStream lReport = lCtrlProv.ExStampaProvvedimento(lEveMod, lFasEsteso.getFascicoloSige().getIdFascicoloSige(), getCodUfficioUtenteConnesso(), super.getUtenteConnesso() );

   //Prepara la pagina di destinazione
   if (lReport != null)
       setRequestAttribute("report", lReport);
   else
       throw new SIGEException(SIGEException.USER_MESSAGE, "Nessun documento è stato generato!");

    setRequestAttribute("ProvvEvento", lProEveMod);

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("ActStampaOrdinanzaSospensione : Fine");

    return IWebConstants.PG_DOWNLOAD;
    //return IWebConstants.PG_DOWNLOAD_NEW;
  }

}