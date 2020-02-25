package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import siap.siep.modulocumulo.controller.IBeneficioCumulo;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

/**
* <p>Title: ActDettaglioRevocaBeneficioCumulo	</p>
* <p>Description: Classe Action per la load dettaglio del Beneficio Revocato in Istruttoria	</p> 
* <p>			 (Gestione Cumulo)	</p>
*/

public class ActDettaglioRevocaBeneficioCumulo extends ActionModuloCumulo implements ICostantiBeneficiCumulo
{
public String processRequest() throws F3BException {

 		 String lId = getRequestStringParameter(CAMPO_ID_BENEFICIO_CUMULO);
 		 
 		//====================================================================================
 	    // Recupero i dati del TITOLO e ISTRUTTORIA CUMULO, da passare alla form  di Dettaglio
 	    //====================================================================================
 	    super.getDatiIstruttoria();
 	    super.getDatiTitoloCumulato();

		IBeneficioCumulo lCtrl = SIEPLookupRemote.getBeneficioCumuloRemote();
		BeneficioCumuloModel llBenMod = lCtrl.ExRicercaBeneficioCumuloByKey(new BigDecimal(lId));
		setRequestAttribute("beneficioCumulo", llBenMod);
		 
		String lTipoForm = getRequestStringParameter(TIPO_FORM_BENEFICIO);
		String lPage="";
		 
		if (lTipoForm.equals(TIPO_FORM_SOSPENSIONE))
			lPage = PG_DETTAGLIO_REVOCA_BENEFICI_CUMULO;
		else	 if(lTipoForm.equals(TIPO_FORM_INDULTO))
			lPage = PG_DETTAGLIO_REVOCA_BENEFICI_INDULTO_CUMULO;

		 return lPage;
	 }

}