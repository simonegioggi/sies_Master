package siap.sius.presaincarico.action;

import java.util.Collection;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sius.ActionSius;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadRicercaAttiSius</p>
* <p>Description: Classe Action per la load ricerca di Messaggi dalla Sorveglianza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRicercaAttiSius extends ActionSius implements ICostantiPresaincarico {
  public String processRequest() throws Exception {
      this.setLinkRitorno();
      Collection <DecodificheModel> options=new Vector <DecodificheModel> ();  
	  options.addAll(DecodificheManager.getInstance().getTipoUfficioSIUS());
	  String filtroMinorenni=this.getFiltroMinorenni();
	  if (filtroMinorenni.equalsIgnoreCase("true")) {
	      options.add(new DecodificheModel("TDSM", "TRIBUNALE PER I MINORENNI IN FUNZIONE DI TRIBUNALE DI SORVEGLIANZA", "TIPO_UFFICIO_SIUS", "", "", "", "", "", ""));
	      options.add(new DecodificheModel("UDSM", "UFFICIO DI SORVEGLIANZA PER I MINORENNI", "TIPO_UFFICIO_SIUS", "", "", "", "", "", ""));
	  }
      String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
      Option lOption = new Option(options);
      lOption.setSelected(strCodTipoUfficio);
      setRequestAttribute("tipoUfficioSIUS", "" + lOption );
      return PG_LOAD_RICERCAATTISIUS;  //restituisce la jsp di VIEW
  }
}