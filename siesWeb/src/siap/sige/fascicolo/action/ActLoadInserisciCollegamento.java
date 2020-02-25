package siap.sige.fascicolo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.web.ActionSige;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciCollegamento</p>
* <p>Description: Classe Action per la load Inserisci del Procedimento Collegato</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class ActLoadInserisciCollegamento extends ActionSige implements ICostantiFascicoloSige
{
    public String processRequest() throws Exception
    {
        // Si ricava il Fascicolo dalla sessione
        FascicoloSigeEstesoModel lFasSigeEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");

        // Si Imposta il Tipo Ufficio.
        Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficioSige(), lFasSigeEsteso.getFascicoloSige().getCodTipoUfficioInserimento(), 46);
        String[] lFilter = {"DIB","DIBM","CAP"};
        lOption.setFilter(lFilter);
        
        setRequestAttribute("tipoUfficioSIGE", "" + lOption );
        setRequestAttribute("modalita", "I");
        // Impostazione della provenienza.
        setRequestAttribute("provenienza", "I");
        setRequestAttribute("fascicoloSigeEsteso", lFasSigeEsteso);
        setRequestAttribute("soggFascPadre", null);

        return PG_LOAD_MODIFICACOLLEGAMENTO;
    }
}
