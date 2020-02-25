<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<%@page import="siap.siep.provvedimentopm.action.ICostantiProvvedimento"%>
<%@page import="siap.sius.impugnazione.action.ICostantiImpugnazione"%>


<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficio"  scope="request" class="java.lang.String"/>


<%
//==============================================================================
// JSP di include per la visualizzazione delle icone di Azione dell'elenco provvedimenti
// per opposizione.
// Parametri presi in input
// - CampoIdEntita
// - ValoreIdEntita
// - CodTipoProvvedimento
// - CodMotivo
// - numeroOpposizioni
// - modificabile
//==============================================================================
%>

<script language="JavaScript">

  function confermaInserimento(a_action, a_entityname1, a_entityvalue1, a_entityname2, a_entityvalue2, a_destnname, a_destvalue, a_message )
  {
      str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname1 + "=" +a_entityvalue1 + "&" + a_entityname2 + "=" +a_entityvalue2 + "&" + a_destnname + "=" +a_destvalue;
      if (window.confirm("" + a_message ))
      {
        window.location.href=str;
      }
  }

</script>

  <table>
    <tr>
<%
      Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
      //Visualizzazione dei bottoni
      if( lFunFiglie != null && lFunFiglie.size() != 0 )
      {
        Iterator lIterBottoni = lFunFiglie.iterator();

        while(lIterBottoni.hasNext())
        {
          FunctionModel lFun = (FunctionModel)lIterBottoni.next();

          if(lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
          {
            //==================================================================
            // RICERCA            
            //==================================================================
            if(   lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_RICERCA)  
               && Integer.parseInt(request.getParameter("numeroOpposizioni"))>0
              )
            {
            %>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&tipoOper=A&TornaQui=<%=TornaQui%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Visualizza Opposizioni (<%=request.getParameter("numeroOpposizioni")%>)" border="0">
                </a>
              </td>
            <%
            }
            
            //==================================================================
            // INSERIMENTO            
            //==================================================================
            if(   lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO)
               && "S".equals (request.getParameter("Modificabile"))
              )
            {
              // Non è possibile trattare ricorsi per decreti di unificazione/Fissazione Udienza.
              if (   !(    request.getParameter("CodTipoProvvedimento").compareTo("02")==0 
                       && (   request.getParameter("CodMotivo").compareTo("0600")==0  
                           || request.getParameter("CodMotivo").compareTo("0601")==0 
                          )  
                      )
                 )
              {
                if (Integer.parseInt(request.getParameter("numeroOpposizioni"))==0 )
                {
                %>
                  <td>
                    <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&tipoOper=I&TornaQui=<%=TornaQui%>">
                      <img src="/images/new24.gif" alt="Inserisci Opposizione" width="12" height="12" border="0">
                    </a>
                  </td>
                <%
                }
                else
                {
                %>
                <td>
                  <a href="Javascript:confermaInserimento('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>','<%=request.getParameter("CodTipoProvvedimento")%>','TornaQui','<%=TornaQui%>', 'Per il provvedimento è già presente una Opposizione, si vuole inserirne una nuova ?' );">
                    <img src="/images/new24.gif" width="12" height="12" alt="Inserisci nuova Opposizione" border="0">
                  </a>
                </td>
<%
                }
              }
            } // end if TIPO_INSERIMENTO
            
            
 



          } // end if FUNZIONE_BOTTONE
        } // end while
      }
%>
    </tr>
  </table>