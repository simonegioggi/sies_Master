package siap.siep.provvedimentopm.action;

/**
* <p>Title: ActInserisciProvvedimento</p>
* <p>Description: Classe Action per l'inserimento di Provvedimento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayOutputStream;

import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
//import siap.siep.provvedimentopm.controller.ProvvedimentoController;
import siap.siep.provvedimentopm.controller.IProvvedimento;
import siap.siep.provvedimentopm.model.ProvvedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciProvvedimento extends ActionSiap
                                       implements ICostantiProvvedimento
{
  /**
  * Azione di Inserimento del Provvedimento.
  * <p>
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws F3BException
  {
    FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");
    UtenteModel lUtenteMod = this.getUtenteConnesso();

    ProvvedimentoModel lProMod = new ProvvedimentoModel();

    lProMod.setFasSieIdFascicoloSiep( lFascicoloModel.getIdFascicoloSiep() );
    lProMod.setDataEmissione( getRequestDateParameter( CAMPO_DATA_AAAA_EMISSIONE, CAMPO_DATA_MM_EMISSIONE, CAMPO_DATA_GG_EMISSIONE ) );
    lProMod.setDestinatario( getRequestStringParameter( AUTORITA_DESTINATARIO ) );
    lProMod.setSedeDestinatario( getRequestStringParameter( AUTORITA_SEDE ) );

    lProMod.setDataScadenza( getRequestDateParameter( CAMPO_DATA_AAAA_EMISSIONE, CAMPO_DATA_MM_EMISSIONE, CAMPO_DATA_GG_EMISSIONE ) );
    lProMod.setData( getRequestDateParameter( CAMPO_DATA_AAAA_EMISSIONE, CAMPO_DATA_MM_EMISSIONE, CAMPO_DATA_GG_EMISSIONE ) );

    IProvvedimento lCtrl = SIEPLookupRemote.getProvvedimentoRemote();
    ByteArrayOutputStream lReport = lCtrl.ExInserisciProvvedimento( lProMod, lUtenteMod );

    //Prepara la pagina di destinazione
    //--setRequestAttribute(IWebConstants.MESSAGE_TEXT,"Inserimento Avvenuto Correttamente!");

    //--RedirectTo lRedirigi = new RedirectTo();
    //--lRedirigi.setPage( IWebConstants.PG_MAIN );
    //--lRedirigi.setParameter("modalita","I");
    //--lRedirigi.setAction("siap.siep.provvedimentopm.action.ActLoadDettaglioProvvedimento" );

    //--setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

    //--return IWebConstants.PG_MESSAGE;

    // ByteArrayOutputStream lReport = (ByteArrayOutputStream)lCtrl.ExStampaRichiesta(lTree, ReportGenerator.RTF);

    setRequestAttribute("report", lReport);

    return IWebConstants.PG_DOWNLOAD;
  }
}