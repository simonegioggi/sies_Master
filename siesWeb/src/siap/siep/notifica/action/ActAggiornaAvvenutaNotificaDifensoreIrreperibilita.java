package siap.siep.notifica.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActAggiornaAvvenutaNotificaDifensoreIrreperibilita</p>
 * <p>Description: Classe Action per la load dettaglio di Scadenzario</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActAggiornaAvvenutaNotificaDifensoreIrreperibilita extends ActionSiap implements ICostantiNotifica
{
  public String processRequest() throws F3BException
  {
    //BigDecimal lflag = this.getRequestBigDecimalParameter("flag");
    
    FascicoloSiepModel lFasc = (FascicoloSiepModel)this.getSessionAttribute("fascicolo");

    NotificaModel lNotMod = new NotificaModel();

    String[] lArrayAbilita = new String[0];
    if( !isRequestParameterNullObj(ICostantiOrdineEsecuzione.ABILITA_NOTIFICA) )
    {
      lArrayAbilita = this.getRequestStringParameters(ICostantiOrdineEsecuzione.ABILITA_NOTIFICA);
    }
    
    NotificaModel[] lNotModArray = null;

    String[] lGiornoNot = null;
    if( !isRequestParameterNullObj(ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA) )
    {
      lGiornoNot = this.getRequestStringParameters(ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA);
    }
    
    String[] lMeseNot = null;
    if( !isRequestParameterNullObj(ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA) )
    {
      lMeseNot = this.getRequestStringParameters(ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA);
    }

    String[] lAnnoNot = null;
    if( !isRequestParameterNullObj(ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA) )
    {
      lAnnoNot = this.getRequestStringParameters(ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA);
    }
    
    String[] lAutoritaDelegata = null;
    if( !isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA) )
    {
      lAutoritaDelegata = this.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
    }
    
    String[] lSedeAutoritaDelegata = null;
    if( !isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_SEDE) )
    {
      lSedeAutoritaDelegata = this.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
    }
    
    String[] lIndirizzo = null;
    if( !isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE) )
    {
      lIndirizzo = this.getRequestStringParameters(ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE);
    }
    
    String[] lIdNotifica = null;
    if( !isRequestParameterNullObj(ICostantiNotifica.CAMPO_ID_NOTIFICA) )
    {
      lIdNotifica = this.getRequestStringParameters(ICostantiNotifica.CAMPO_ID_NOTIFICA);
    }

    int lLungArrayAbilita = lArrayAbilita.length;
    lNotModArray = new NotificaModel[lLungArrayAbilita];
    
    for (int i = 0; i < lLungArrayAbilita; i++)
    {
      if (lGiornoNot[i] != null && !lGiornoNot[i].equals(""))
      {
        String lGiorno = lGiornoNot[i];
        String lMese = lMeseNot[i];
        String lAnno = lAnnoNot[i];
        lNotMod = new NotificaModel();

        lNotMod.setDataAvvenutaNotifica(DateUtils.getDate(lAnno, lMese, lGiorno));
        lNotMod.setIdNotifica(new BigDecimal(lArrayAbilita[i]));
        lNotMod.setEveIdEvento(this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
        lNotMod.setCodiceOperatoreAggiornamento(getCodUtenteConnesso());
        lNotMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
        lNotMod.setDataAggiornamento(DateUtils.getSysDate());
        lNotMod.setCodEsito("01");
        lNotMod.setCodTipoNotifica("N");

        if(    lAutoritaDelegata[i] != null
            && !lAutoritaDelegata[i].equals("") 
            && !lAutoritaDelegata[i].equals("-")
           )
        {
          AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();

          lAutMod.setCodTipoAutorita(lAutoritaDelegata[i]);
          ComuneModel lComModel = new ComuneModel(getCodComuneByDescr(lSedeAutoritaDelegata[i]));
          lAutMod.setCodSede(lComModel.getCodComune());
          lAutMod.setDescrizione(lIndirizzo[i]);

          lAutMod.setCodOperatoreInserimento(getCodUtenteConnesso());
          lAutMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
          lAutMod.setDataInserimento(DateUtils.getSysDate());

          lNotMod.setAutoritaEsternaDelegata(lAutMod);
        }       

        lNotModArray[i] = lNotMod;
      }
    }

    // Aggiunge le notifiche di cui occorre sbiancare i dati 
    // in caso si proviene dalla pagina di modifica
    if(lIdNotifica != null && lIdNotifica.length > 0) 
    {
      List lListNotModTrovati = new ArrayList();
      List lListNotMod = null;
      
      for (int i = 0; i < lIdNotifica.length; i++)
      {
        boolean isTrovato = false;
        for (int j = 0; j < lArrayAbilita.length; j++)
        {
          if( lIdNotifica[i].equals(lArrayAbilita[j]) ) 
          {            
            isTrovato = true;
          }
        }
        
        if(!isTrovato)
        {
          NotificaModel lNotModAgg = new NotificaModel();

          lNotModAgg.setDataAvvenutaNotifica(null);
          lNotModAgg.setIdNotifica(new BigDecimal(lIdNotifica[i]));
          lNotModAgg.setEveIdEvento(this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
          lNotModAgg.setCodiceOperatoreAggiornamento(getCodUtenteConnesso());
          lNotModAgg.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
          lNotModAgg.setDataAggiornamento(DateUtils.getSysDate());
          lNotModAgg.setCodEsito("-");
          lNotModAgg.setAutoritaEsternaDelegata(null);
          lNotModAgg.setCodTipoNotifica("N");

          lListNotModTrovati.add(lNotModAgg);
        }       
      }
      
      if( !lListNotModTrovati.isEmpty() )
      {
        lListNotMod = new ArrayList();
        
        for (int i = 0; i < lNotModArray.length; i++)
        {
          lListNotMod.add(lNotModArray[i]);
        }
        
        lListNotMod.addAll(lListNotModTrovati);
        
        lNotModArray = (NotificaModel[]) lListNotMod.toArray(new NotificaModel[0]);
      }
    }
    
    Vector lEveVectUno = new Vector();
    IOrdineEsecuzione lCtrlUno = SIEPLookupRemote.getOrdineEsecuzioneRemote();

    lEveVectUno = lCtrlUno.ExAggiornaAvvenutaNotifica(lNotModArray, lFasc.getIdFascicoloSiep(), true);
    setRequestAttribute("notifiche", lEveVectUno);

    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.siep.notifica.action.ActLoadRicercaNotificaDecretoIrreperibilita&"+ICostantiEvento.CAMPO_ID_EVENTO+"="+((NotificaModel)lEveVectUno.get(0)).getEveIdEvento().toString();

    return lPage;
  }
}