package siap.sius.prescrizione.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sius.prescrizione.controller.IPrescrizione;
import siap.sius.prescrizione.model.PrescrizioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadDettaglioPrescrizione</p>
* <p>Description: Classe Action per la load dettaglio di Prescrizione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadDettaglioPrescrizione extends ActionSiap implements ICostantiPrescrizione
{
public String processRequest() throws F3BException {

 		 String lId = getRequestStringParameter(CAMPO_ID_PRESCRIZIONE);
 		 // riempie il model
		 // chiama il controller

		 IPrescrizione lCtrl = SIUSLookupRemote.getPrescrizioneRemote();
		 PrescrizioneModel lPreMod = lCtrl.ExRicercaPrescrizioneByKey(new BigDecimal(lId));
		 setRequestAttribute("prescrizione", lPreMod);





		 return PG_LOAD_DETTAGLIOPRESCRIZIONE;
	 }



}