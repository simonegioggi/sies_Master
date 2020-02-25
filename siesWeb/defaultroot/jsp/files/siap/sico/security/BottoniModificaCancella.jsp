<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate" %>


<jsp:useBean id="Modificabile"              scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="Trasferibile"              scope="request" class="java.lang.String"/>
<jsp:useBean id="DaValidare"              scope="request" class="java.lang.String"/>
<jsp:useBean id="MessaggioRiapertura"              scope="request" class="java.lang.String"/>


<%
 boolean isModificabile = false;
 boolean isTrasferibile = false;
 boolean isDaValidare = false;

if (Modificabile == null || Modificabile.trim().length() < 1  ||  Modificabile.compareTo("SI") == 0)
    isModificabile = true;
if (Trasferibile == null || Trasferibile.trim().length() < 1  ||  Trasferibile.compareTo("SI") == 0)
    isTrasferibile = true;
if (DaValidare == null || DaValidare.trim().length() < 1  ||  DaValidare.compareTo("SI") == 0)
    isDaValidare = true;

    Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

    //Visualizzazione dei bottoni
    if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
    {
      Iterator lIterBottoni = lFunFiglie.iterator();

      FunctionModel lFun = null;
      while(lIterBottoni.hasNext())
      {
        lFun = (FunctionModel)lIterBottoni.next();
        if(isModificabile &&  lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE)  && lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) )
        {
%>
          <td class="LBG">
            <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
            </a>
         </td>
<%
        }
         else if( isModificabile && lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE)  && lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) )
        {
%>
         <!-- BOTTONE DI CANCELLAZIONE -->
         <td class="LBG">
           <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','TornaQui','<%=TornaQui%>');">
           <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
           </a>
         </td>
<%
        }
         else if( isTrasferibile && lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE)  && lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_TRASFERIMENTO) )
        {
%>
       <!-- BOTTONE DI TRASFERIMENTO -->
       <td class="LBG">
            <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
           <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>net24.gif" alt="Trasferisci" width="24" height="24" border="0">
           </a>
       </td>
<%
        }
         else if( isDaValidare && lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE)  && lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_VALIDAZIONE) )
        {
%>
       <!-- BOTTONE DI VALIDAZIONE / CHIUSURA DOCUMENTO -->
       <td class="LBG">
            <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
           <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>lucchetto24.gif" alt="<%=lFun.getLabelFunction()%>" width="24" height="24" border="0">
           </a>
       </td>
<%
        }
         else if( !isDaValidare && lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE)  && lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_RIAPERTURA) )
         {
 %>
           <!-- BOTTONE DI SVALIDAZIONE /RIAPERTURA DOCUMENTO -->
          <td class="LBG">
            <a href="Javascript:confermaMessage('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','TornaQui','<%=TornaQui%>', '<%=MessaggioRiapertura%>' );">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>lucchettoaperto24.gif" alt="<%=lFun.getLabelFunction() %>" width="24" height="24" border="0">
            </a>
          </td>
 <%
         }
       } // endwhile
    }
%>