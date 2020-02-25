package siap.siep.modulocumulo.action;

/**
* <p>Title: ActLoadCancellaDatoAnalitico</p>
* <p>Description: Classe Action per la load della PopUp per la richiesta Motivazioni
* cancellazione.
* Utilizzata solo nella cancellazione logica.
* </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import f3b.util.F3BException;

public class ActLoadCancellaDatoAnalitico extends ActionModuloCumulo implements ICostantiModuloCumulo
{
 /*****************************************************************************
  * Azione di caricamento della PupUp di Cancellazione logica dei dati Analitici. 
  * 
  * @return Nome della pagina JSP da visualizzare
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
    }

    String lNomeForm = getRequestStringParameter(NOME_FORM);
    setRequestAttribute("aNomeFormChiamante", lNomeForm);

    String lMotivoModifica = "";
    
    if (!isRequestParameterNullObj(CAMPO_MOTIVO_MODIFICA))
      lMotivoModifica = getRequestStringParameter(CAMPO_MOTIVO_MODIFICA);
    
    setRequestAttribute("aMotivoModifica", lMotivoModifica);

    // 
    // setRequestAttribute("modalita", "C");
     
    return PG_LOAD_POPUP_ANNULLAMENTO;
  }
}