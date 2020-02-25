<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

  <table>
    <tr>
<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  
  String lModificabile = "SI";

  if (request.getParameter("Modificabile") != null)
  {
    lModificabile = request.getParameter("Modificabile");
  }

  String lAnnullato = "N";
  if (request.getParameter("annullato") != null)
  {
    lAnnullato = request.getParameter("annullato");
  }

  // Costruzione del secondo parametro opzionale Luigi 15-07-2005
  String Param2 = "";
  if (request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV) != null && request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV).length() > 0 )
  if (request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV) != null && request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV).length() > 0 )
      Param2 = "&" + request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV) + "=" + request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV);


      Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

      //Visualizzazione dei bottoni
      if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
      {
        Iterator lIterBottoni = lFunFiglie.iterator();
        FunctionModel lFun = null;
        while(lIterBottoni.hasNext())
        {
          lFun = (FunctionModel)lIterBottoni.next();
          
          if(lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
          {
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO))
            {
%>
              <td>
            <%if(request.getParameter("ValoreAzioneChiamante")!=null && !request.getParameter("ValoreAzioneChiamante").equals(""))
              {%>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>&<%=request.getParameter("CampoAzioneChiamante")%>=<%=request.getParameter("ValoreAzioneChiamante")%><%=Param2%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                </a>
            <%}else
              {%>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%><%=Param2%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                </a>

            <%}%>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) && (lModificabile.equals("SI") && lAnnullato.equals("N")))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%><%=Param2%>">
                  <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                </a>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA)&& (lModificabile.equals("SI") && lAnnullato.equals("N")))
            {
%>
              <td>
                <a href="Javascript:conferma('<%=lFun.getNameAction()%><%=Param2 %>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>');">
                  <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                </a>
              </td>
<%
            }
          
            
           if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA)&&(request.getParameter("Stampa").compareTo("SI")==0))
            {
%>
              <td>
                <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sige.richiestaatti.action.ActLoadDocumento&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>')">
                  <img src="/images/print.gif" alt="Stampa" width="12" height="12" border="0">
                </a>
              </td>
<%
            }  
        	   
        	if(request.getParameter("Stampa").compareTo("SI")==0 && request.getParameter("Solleciti").compareTo("NO")==0)
            {
%>
              <td>
                <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=siap.sige.richiestaatti.action.ActLoadDocumento&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>')">
                  <img src="/images/print.gif" alt="Stampa" width="12" height="12" border="0">
                </a>
              </td>
<%
            }  
          } 
        }
        
        
        
        
        
        if (request.getParameter("Solleciti").compareTo("SI")==0)
        {
%>
        <td>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.richiestaatti.action.ActElencoSolleciti&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=request.getParameter("CampoRitorno")%>=<%=request.getParameter("ValCampoRitorno")%>">
            <!--  img src="/images/attach.gif" width="12" height="12" alt="Solleciti" border="0" -->
            <img src="/images/solleciti.png" width="12" height="12" alt="Solleciti" border="0" />
          </a>
        </td>
<%          }
      }
      
      if (request.getParameter("Solleciti").compareTo("SI") != 0) {
%>
    <td>
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.richiestaatti.action.ActCancellaRichiestaAtti&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=request.getParameter("idEvento")%>">
            <img src="/images/delete.gif" alt="Stampa" width="12" height="12" border="0">
        </a>
     </td>
<%
      }
%>
    </tr>
  </table>