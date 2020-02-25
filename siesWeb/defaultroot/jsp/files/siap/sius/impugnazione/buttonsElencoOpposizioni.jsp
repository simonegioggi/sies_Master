<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sius.impugnazione.action.ICostantiImpugnazione"%>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>


<%
//==============================================================================
// Jsp Inclusa per la visualizzazione delle Azioni disponibili sulla singola
// Opposizione
// - CampoIdEntita
// - ValoreIdEntita
// - CampoIdImpugnazione
// - CodTipoProvvedimento
// - CodMotivo
// - FlagAnnullato
// - Modificabile
//==============================================================================
%>
  <table>
    <tr>
<%
      Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

      // Visualizzazione dei bottoni
      if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
      {
        Iterator lIterBottoni = lFunFiglie.iterator();

        while(lIterBottoni.hasNext())
        {
          FunctionModel lFun = (FunctionModel)lIterBottoni.next();

          if(lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
          {
            
            //==================================================================
            // La MODIFICA è operativa solo se esiste il ricorso associato 
            // al provvedimento.
            //==================================================================
            if(   lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) 
               && "S".equals (request.getParameter("Modificabile"))
              )
            {
              // Previste 2 funzioni di modifica: Aggiorna e Modifica
              // Non è possibile trattare ricorsi per decreti di unificazione/Fissazione Udienza.
              if (  !(   request.getParameter("CodTipoProvvedimento").compareTo("02")==0 
                      && (   request.getParameter("CodMotivo").compareTo("0600")==0  
                          || request.getParameter("CodMotivo").compareTo("0601")==0 
                         ) 
                     )
                  && ( request.getParameter("FlagAnnullato").compareTo("S")!=0)
                 )
              {
              
                if (lFun.getNameAction().compareTo("siap.sius.impugnazione.action.ActLoadModificaOpposizione")==0)
                {
                %>
                <td>
                  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&<%=ICostantiImpugnazione.CAMPO_ID_IMPUGNAZIONE%>=<%=request.getParameter("CampoIdImpugnazione")%>&TornaQui=<%=TornaQui%>">
                    <img src="/images/modifica.gif" width="12" height="12" alt="Modifica Opposizione" border="0">
                  </a>
                </td>
                <% 
                } else {
                %>
                <td>
                  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&<%=ICostantiImpugnazione.CAMPO_ID_IMPUGNAZIONE%>=<%=request.getParameter("CampoIdImpugnazione")%>&TornaQui=<%=TornaQui%>">
                    <img src="/images/aggiorna24.gif" width="12" height="12" alt="Aggiorna Opposizione" border="0">
                  </a>
                </td>
                <% 
                }
              }
            }
            %>
            
            <%
            //==================================================================
            // Il dettaglio del ricorso è operativo per ogni ricorso associato 
            // al provvedimento.
            //==================================================================
            if(   lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO) )
            {
              // Non è possibile trattare ricorsi per decreti di unificazione/Fissazione Udienza.
              if (  !(   request.getParameter("CodTipoProvvedimento").compareTo("02")==0 
                      && (   request.getParameter("CodMotivo").compareTo("0600")==0  
                          || request.getParameter("CodMotivo").compareTo("0601")==0 
                         ) 
                     )
                 )
              {
              %>
                <td>
                  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&<%=ICostantiImpugnazione.CAMPO_ID_IMPUGNAZIONE%>=<%=request.getParameter("CampoIdImpugnazione")%>&TornaQui=<%=TornaQui%>">
                    <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Opposizione" border="0">
                  </a>
                </td>
              <%
              }
            }
            %>
            
            <%
            //==================================================================
            //   ANNULLAMENTO
            //==================================================================
            if(   lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) 
               && "S".equals (request.getParameter("Modificabile"))
              ) 
            {
              if ( request.getParameter("FlagAnnullato").compareTo("S")!=0 )
              {
              %>
              <td>
                <a href="Javascript:confermaAnnullamento3Param('<%=lFun.getNameAction()%>'
                                                              ,'<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>'
                                                              ,'<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>'
                                                              ,'<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>'
                                                              ,'<%=request.getParameter("CodTipoProvvedimento")%>'
                                                              ,'<%=ICostantiImpugnazione.CAMPO_ID_IMPUGNAZIONE%>'
                                                              ,'<%=request.getParameter("CampoIdImpugnazione")%>' );">
                  <img src="/images/delete.gif" width="12" height="12" alt="Annulla Opposizione" border="0">
                </a>
              </td>
              <%
              }
            }
            %>
            
<%            
        }  // end if ICostantiFunzioni.FUNZIONE_BOTTONE
      } // end while
    } //
%>
    </tr>
  </table>