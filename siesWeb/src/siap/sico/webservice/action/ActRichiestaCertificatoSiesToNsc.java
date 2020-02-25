package siap.sico.webservice.action;

import java.math.BigDecimal;

import f3b.web.IWebConstants;

public class ActRichiestaCertificatoSiesToNsc extends ActWsBase
{
    public String  processRequest() throws Exception
    { 

        String tipoFascicolo = getRequestStringParameter("TipoFascicolo");
        setRequestAttribute("tipoFascicolo", tipoFascicolo);
        
        BigDecimal idEvento = getRequestBigDecimalParameter("IdEvento");
        setRequestAttribute("idEvento", idEvento);
        
        return IWebConstants.ROOT_DIR + "/files/siap/sico/webservice/RichiestaCertificatoSiesToNsc.jsp";
    }
    	
}
