<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.jms.ICostantiJMS" %>

<%@ page import="f3b.log.LogF3B" %>


<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String"/>


<%
//==============================================================================
// jsp per la visualizzazione delle icone di azione (dettaglio e cancellazione)
// da includere nelle liste dei risultati delle ricerche atti effettuate sulle
// tabella MESSAGGIO.
// 
// 
//==============================================================================
%>

  <table>
    <tr>
<%
    // presenza del Link per il bottone di ritorno
    boolean retFlag = false;
    retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
    String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";

    Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

    // Visualizzazione dei bottoni
    if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
    {
      Iterator lIterBottoni = lFunFiglie.iterator();
      FunctionModel lFun = null;
      while(lIterBottoni.hasNext())
      {
        lFun = (FunctionModel)lIterBottoni.next();   
        
        // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        //siesLogger.debug("XXXXX -------> JSP: Function id = "+ lFun.getFunctionId().intValue() );
        // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        //siesLogger.debug("XXXXX -------> JSP: Function id = "+ lFun );
        // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        //siesLogger.debug("XXXXX -------> JSP: CodTipoOperazione = "+request.getParameter("CodTipoOperazione") );
        
        if(lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
        {
          
           if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO))
           {
             if ((request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_ISTANZA )==0 && (lFun.getFunctionId().intValue()==21120283))||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_ISTANZA )==0 && (lFun.getFunctionId().intValue()==21120120))||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_ISTANZA )==0 && (lFun.getFunctionId().intValue()==31020012))||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_ISTANZA )==0 && (lFun.getFunctionId().intValue()==21120277))||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_ORDINANZA )==0 && (lFun.getFunctionId().intValue()==21020078))||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_ORDINANZA )==0 && (lFun.getFunctionId().intValue()==31020025))||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_ORDINANZA )==0 && (lFun.getFunctionId().intValue()==22010061))||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_DECRETO )==0 && (lFun.getFunctionId().intValue()==21020078))||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_DECRETO )==0 && (lFun.getFunctionId().intValue()==31020026))||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_DECRETO )==0 && (lFun.getFunctionId().intValue()==22010051))||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_RICORSO )==0 && (lFun.getFunctionId().intValue()==31020028))||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_PROVVEDIMENTO )==0 && (lFun.getFunctionId().intValue()==31020012))||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_PROVVEDIMENTO )==0 && (lFun.getFunctionId().intValue()==21020075)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_ATTI_CONVERSIONE )==0 && (lFun.getFunctionId().intValue()==31020012)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_ATTI_CONVERSIONE )==0 && (lFun.getFunctionId().intValue()==21020075)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_ATTIVITA )==0 && (lFun.getFunctionId().intValue()==61070010)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_ATTIVITA )==0 && (lFun.getFunctionId().intValue()==31020082)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_RICHIESTA_UEPE )==0 && (lFun.getFunctionId().intValue()==61070010)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_RICHIESTA_UEPE )==0 && (lFun.getFunctionId().intValue()==31020085)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_RELAZIONE_UEPE )==0 && (lFun.getFunctionId().intValue()==61070010)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_RELAZIONE_ATTIVITA )==0 && (lFun.getFunctionId().intValue()==61070010)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_RELAZIONE_ATTIVITA )==0 && (lFun.getFunctionId().intValue()==31020083)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_RELAZIONE_RICHIESTA_UEPE )==0 && (lFun.getFunctionId().intValue()==61070010)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_RELAZIONE_RICHIESTA_UEPE )==0 && (lFun.getFunctionId().intValue()==31020034)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_SANZIONE_SOSTITUTIVA )==0 && (lFun.getFunctionId().intValue()==31020012)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_COMPETENZA )==0 && (lFun.getFunctionId().intValue()==21210617)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_COMPETENZA )==0 && (lFun.getFunctionId().intValue()==21210619)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.SOLLECITO_TRASFERIMENTO_COMPETENZA )==0 && (lFun.getFunctionId().intValue()==21210617)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.SOLLECITO_TRASFERIMENTO_COMPETENZA )==0 && (lFun.getFunctionId().intValue()==21210619)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.ESITO_TRASFERIMENTO_COMPETENZA )==0 && (lFun.getFunctionId().intValue()==21210627)) ||                 
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_RICHIESTA_RELAZIONE )==0 && (lFun.getFunctionId().intValue()==21020078)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_COMPETENZA_MS )==0 && (lFun.getFunctionId().intValue()==25020271)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_ESECUZIONE_MS )==0 && (lFun.getFunctionId().intValue()==25020271)) ||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_SENTENZA )==0 && (lFun.getFunctionId().intValue()==21020078))||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_SENTENZA )==0 && (lFun.getFunctionId().intValue()==31020025))||
                 (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_SENTENZA )==0 && (lFun.getFunctionId().intValue()==22010061))
                 
                 // Nuovo cumulo
                 || (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.RICHIESTA_TRASMISSIONE_ATTI_PER_COMP )==0 && (lFun.getFunctionId().intValue()==21080131)) 
                 || (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.COMUNICAZIONE_CUMULO_PROCURE_COMPETENTI )==0 && (lFun.getFunctionId().intValue()==21210627))
                 // MEV 42 -Ulteriori Requisiti
                 || (request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.ESITO_SEGUITO_ATTI )==0 && (lFun.getFunctionId().intValue()==21210627))
                 
                 // DL 146/2013 Richieste Cessazione/Prosecuzione 51bis
                 ||(request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.RICHIESTA_CESSAZIONE_MISURA )==0 && (lFun.getFunctionId().intValue()==21020075)) 
                 ||(request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.RICHIESTA_CESSAZIONE_MISURA )==0 && (lFun.getFunctionId().intValue()==31020012)) 

                 ||(request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.RICHIESTA_PROSECUZIONE_MISURA )==0 && (lFun.getFunctionId().intValue()==21020075)) 
                 ||(request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.RICHIESTA_PROSECUZIONE_MISURA )==0 && (lFun.getFunctionId().intValue()==31020012)) 
                 //=============================================================
                 // Richiesta accertamento pericolosità Sociale (Mis Sic)
                 // Dettaglio per il profilo SIEP (riscontro trasmissioni)
                 ||(request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_RICHIESTA_ACCERTA_PERICOLO_SOCIALE )==0 && (lFun.getFunctionId().intValue()==21020075)  ) 
                 // Dettaglio per il profilo SIUS (presa in carico atti ricevuti)
                 ||(request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_RICHIESTA_ACCERTA_PERICOLO_SOCIALE )==0 && (lFun.getFunctionId().intValue()==31020012)  )
                )
              {
               // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
               //  siesLogger.debug("XXXXX -------> JSP_11 : Function id = "+ lFun.getFunctionId().intValue() );
               // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
               //  siesLogger.debug("XXXXX -------> JSP_11 : Function id = "+ lFun );
               // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
               //  siesLogger.debug("XXXXX -------> JSP_11 : CodTipoOperazione = "+request.getParameter("CodTipoOperazione") );
%>
                <td>
                  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>&<%=request.getParameter("CampoAzioneChiamante")%>=<%=request.getParameter("ValoreAzioneChiamante")%>">
                    <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                  </a>
                </td>
<%
              }
              else if((request.getParameter("CodTipoOperazione").compareTo(ICostantiJMS.TRASFERIMENTO_COMPETENZA)==0 && (lFun.getFunctionId().intValue()==22010061))){
              %>
                <td>
                  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.presaincarico.action.ActDettaglioPresaincaricoCompetenza&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>&<%=request.getParameter("CampoAzioneChiamante")%>=<%=request.getParameter("ValoreAzioneChiamante")%>">
                    <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                  </a>
                </td>  
                <%
              }
            } // end if tipo funzione dettaglio

            // STUB 04/11/2004 Cancellazione Messaggi.
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA)  )
            {
              if (   request.getParameter("Modificabile")==null 
                  || (   request.getParameter("Modificabile")!=null
                      && !request.getParameter("Modificabile").equals("N")
                     )
                 )
              {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>">
                  <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                </a>
              </td>
<%
              }
            }
          }
        }
      }
%>
  </tr>
  </table>