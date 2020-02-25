package siap.sius.fascicolo.action;

import java.util.Collection;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sius.ActionSius;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadRicercaFascicoloSius</p>
* <p>Description: Classe Action per la load di RicercaFascicoloSius</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadRicercaFascicoloSius extends ActionSius implements ICostantiFascicoloSius {
  public String processRequest() throws Exception {
      // Imposta Tipo Ufficio con Trattino.
	  Collection <DecodificheModel> options=new Vector <DecodificheModel> ();  
	  options.addAll(DecodificheManager.getInstance().getTipoUfficioSiusTrattino());
	  String filtroMinorenni=this.getFiltroMinorenni();
	  if (filtroMinorenni.equalsIgnoreCase("true")) {
	      options.add(new DecodificheModel("TDSM", "TRIBUNALE PER I MINORENNI IN FUNZIONE DI TRIBUNALE DI SORVEGLIANZA", "TIPO_UFFICIO_SIUS", "", "", "", "", "", ""));
	      options.add(new DecodificheModel("UDSM", "UFFICIO DI SORVEGLIANZA PER I MINORENNI", "TIPO_UFFICIO_SIUS", "", "", "", "", "", ""));
	  }
	 
	  UfficioModel um = getUfficioUtenteConnesso();
	  String tipoUfficio = um.getCodTipoUfficio();
      Option lOption = new Option( options);
      lOption.setSelected(tipoUfficio);
      setRequestAttribute("tipoUfficioSIUSTrattino", "-" + lOption );
      String strDescrComune = getUfficioUtenteConnesso().getDescrComune();
      setRequestAttribute("ComuneUfficioConnesso", strDescrComune );
      return PG_LOAD_RICERCAFASCICOLOSIUS; //restituisce la jsp di VIEW
  }
}
