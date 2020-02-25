package siap.siep.certificatostatoesecuzione.action;


/**
* <p>Title: ActInserisciCertificatoStatoEsec</p>
* <p>Description: Classe Action per l'inserimento di CertificatoStatoEsec</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;
import siap.siep.certificatostatoesecuzione.controller.ICertificatoStatoEsec;
import siap.siep.certificatostatoesecuzione.model.CertificatoStatoEsecModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciCertificatoStatoEsec extends ActionSiap implements ICostantiCertificatoStatoEsec {

 /*****************************************************************************
  * Azione di Inserimento del CertificatoStatoEsec
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  ****************************************************************************/
  public String processRequest() throws F3BException {
    CertificatoStatoEsecModel lCerMod = new CertificatoStatoEsecModel();

    //========================================================================== 
    // Recupero i dati presenti in maschera 
    //========================================================================== 
    lCerMod.setIdCertificatoStatoEsec    ( getRequestBigDecimalParameter ( CAMPO_ID_CERTIFICATO_STATO_ESEC) );
    lCerMod.setAnnotazioni               ( getRequestStringParameter     ( CAMPO_ANNOTAZIONI) );
    lCerMod.setFlagUpload                ( getRequestStringParameter     ( CAMPO_FLAG_UPLOAD) );
    lCerMod.setCodOperatoreInserimento   ( getRequestStringParameter     ( CAMPO_COD_OPERATORE_INSERIMENTO) );
    lCerMod.setDataInserimento           ( getRequestDateParameter       ( CAMPO_ANNO_DATA_INSERIMENTO,CAMPO_MESE_DATA_INSERIMENTO,CAMPO_GIORNO_DATA_INSERIMENTO) );
    lCerMod.setCodUfficioInserimento     ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_INSERIMENTO) );
    lCerMod.setCodOperatoreAggiornamento ( getRequestStringParameter     ( CAMPO_COD_OPERATORE_AGGIORNAMENTO) );
    lCerMod.setDataAggiornamento         ( getRequestDateParameter       ( CAMPO_ANNO_DATA_AGGIORNAMENTO,CAMPO_MESE_DATA_AGGIORNAMENTO,CAMPO_GIORNO_DATA_AGGIORNAMENTO) );
    lCerMod.setCodUfficioAggiornamento   ( getRequestStringParameter     ( CAMPO_COD_UFFICIO_AGGIORNAMENTO) );
    lCerMod.setFasSieIdFascicoloSiep     ( getRequestBigDecimalParameter ( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );

    //=================================================== 
    // Recupera il controller ed effettua l'inserimento 
    //=================================================== 
    ICertificatoStatoEsec lCtrl = SIEPLookupRemote.getCertificatoStatoEsecRemote();
    CertificatoStatoEsecModel lCerRetMod = new CertificatoStatoEsecModel();
    lCerRetMod=lCtrl.ExInserisciCertificatoStatoEsec(lCerMod);

    //====================================================================== 
    // Prepara la pagina di destinazione
    // Viene restituita la pagina di dettaglio con i dati appena inseriti
    //====================================================================== 
    String lPage="";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.certificatostatoesecuzione.action.ActLoadDettaglioCertificatoStatoEsec";
    lPage += "&" + CAMPO_ID_CERTIFICATO_STATO_ESEC + "=" + lCerRetMod.getIdCertificatoStatoEsec().toString();

    return lPage;
  }
}