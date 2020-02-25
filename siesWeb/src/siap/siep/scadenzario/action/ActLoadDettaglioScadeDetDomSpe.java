package siap.siep.scadenzario.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;





/**
* <p>Title: ActLoadDettaglioScadenzarioFinePena</p>
* <p>Description: Classe Action per la load dettaglio di Scadenzario</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioScadeDetDomSpe extends ActionSiap implements ICostantiScadenzario
{
public String processRequest() throws F3BException
  {

 		 String lId = getRequestStringParameter(CAMPO_ID_SCADENZARIO);
 		 // riempie il model
		 // chiama il controller

		 IScadenzario lCtrl = SIEPLookupRemote.getScadenzarioRemote();
		 ScadenzarioModel llScaMod = lCtrl.ExRicercaScadenzarioByKey(new BigDecimal(lId));
		 setRequestAttribute("scadenzario", llScaMod);
                 llScaMod.setFlagVisto("S");
                 llScaMod.setDataVisto(DateUtils.getSysDate());
                 lCtrl.ExModificaScadenzario(llScaMod);

		 return PG_LOAD_DETTAGLIO_SCADE_DET_DOM_SPE;
	 }



}