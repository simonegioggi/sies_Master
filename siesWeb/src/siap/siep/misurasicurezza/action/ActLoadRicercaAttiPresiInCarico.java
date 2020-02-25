package siap.siep.misurasicurezza.action;

import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.jms.action.ICostantiSicoJMS;
import siap.sico.web.ActionSiap;
import siap.sius.presaincarico.action.ICostantiPresaincarico;
import f3b.web.html.Option;

/**
 * 
 * @author d.fiorletta
 *
 */
public class ActLoadRicercaAttiPresiInCarico extends ActionSiap implements ICostantiMisuraSicurezza 
{
  public String processRequest() throws Exception 
  {
    
    // Imposta Tipo Ufficio SIEP
    //FIXME correggere i tipi di ufficio aggiungendo quelli della sorveglianza
    Option lOptionUff = new Option( DecodificheManager.getInstance().getTipoUfficioPM());
    if (   !isRequestParameterNullObj(ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO)
        && getRequestStringParameter(ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO).length()>0)
    {
      lOptionUff.setSelected(getRequestStringParameter(ICostantiPresaincarico.CAMPO_COD_TIPO_UFFICIO));
    }
    else {
      lOptionUff.setSelected("-");
    }
    
    setRequestAttribute("tipoUfficioSIEP", "" + lOptionUff );
    
   
    // Imposta ESITO
    Vector <DecodificheModel> lEsiti = new Vector <DecodificheModel>();
    lEsiti.add(new DecodificheModel("TUTTI","TUTTI","","","","","","","") );
    lEsiti.add(new DecodificheModel("01001","PRESI IN CARICO","","","","","","","") );
    lEsiti.add(new DecodificheModel("01003","RESTITUITI AL MITTENTE","","","","","","","") );
    lEsiti.add(new DecodificheModel("01004","INOLTRATI PER COMPETENZA","","","","","","","") );
    lEsiti.add(new DecodificheModel("01005","ISCRITTI IN CLASSE IV","","","","","","","") );
    //lEsiti.add(new DecodificheModel("-","RICEVUTI","","","","","","","") );
    
    Option lOptionEsiti = new Option( lEsiti);
    
    if (   !isRequestParameterNullObj(ICostantiSicoJMS.CAMPO_TIPO_ESITO)
        && getRequestStringParameter(ICostantiSicoJMS.CAMPO_TIPO_ESITO).length()>0)
    {
      lOptionEsiti.setSelected(getRequestStringParameter(ICostantiSicoJMS.CAMPO_TIPO_ESITO));
    }
    
    

    setRequestAttribute("listaEsiti", "" + lOptionEsiti );
    
    return PG_LOAD_RICERCA_ATTI_PRESI_IN_CARICO;
  }

}
