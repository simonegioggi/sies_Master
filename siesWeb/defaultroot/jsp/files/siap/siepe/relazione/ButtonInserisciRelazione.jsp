<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.template.action.ICostantiTemplate" %>
<%@ page import="siap.siepe.relazione.action.ICostantiRelazione" %>

<jsp:useBean id="Modificabile" scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%
boolean isModificabile = false;

if (Modificabile == null || Modificabile.trim().length() < 1  ||  Modificabile.compareTo("SI") == 0)
		isModificabile = true;


if (isModificabile)
{
    Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

    //Visualizzazione dei bottoni
    if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
    {

      Iterator lIterBottoni = lFunFiglie.iterator();

      FunctionModel lFun = null;
      while(lIterBottoni.hasNext())
      {
        lFun = (FunctionModel)lIterBottoni.next();
        if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE)  && lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO) )
        {
%>
       <td class="LBG">
            <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci Relazione" width="24" height="24" border="0">
           </a>
       </td>
<%
        }
      } // endwhile
    }
}
%>