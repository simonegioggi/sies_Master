<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>
<%@ page import="siap.sige.impugnazione.action.ICostantiImpugnazioneSige" %>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige" %>
<%@ page import="siap.sige.impugnazione.controller.IImpugnazioneSige" %>
<%@ page import="siap.sige.util.SIGELookupRemote" %>

<%@page import="siap.siep.provvedimentopm.action.ICostantiProvvedimento"%>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficio"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoImpugnazione"  scope="session" class="java.lang.String"/>
<jsp:useBean id="codTipoImpugnazione"  scope="session" class="java.lang.String"/>

<script language="JavaScript">

	function confermaInserimento(a_action, a_entityname1, a_entityvalue1, a_entityname2, a_entityvalue2, a_destnname, a_destvalue, a_message )
	{
  		str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname1 + "=" +a_entityvalue1 + "&" + a_entityname2 + "=" +a_entityvalue2 + "&" + a_destnname + "=" +a_destvalue;
    	// alert("Stringa di conferma1 ->" + str);
    	if (window.confirm("" + a_message ))
    	{
    		window.location.href=str;
    	}
	}

</script>

  <table>
    <tr>
<%
      Collection <FunctionModel>lFunFiglie = (Collection<FunctionModel>)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
      String strTitolo="Elenco Ricorsi per il Provvedimento";
      if (codTipoImpugnazione.equals(ICostantiImpugnazioneSige.COD_TIPO_OPPOSIZIONE))
        strTitolo = "Elenco Opposizioni per il Provvedimento";
      
      String labelTipoImpugnazione="Ricorso";
      if (codTipoImpugnazione.equals(ICostantiImpugnazioneSige.COD_TIPO_OPPOSIZIONE) ) 
  		labelTipoImpugnazione="Opposizione";
		
       for (FunctionModel lFun  : lFunFiglie) {
          if(lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE)) {
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_RICERCA)) {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiImpugnazioneSige.CAMPO_ID_IMPUGNAZIONE%>=<%=request.getParameter("CampoIdImpugnazione")%>&tipoOper=A&TornaQui=<%=TornaQui%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="<%=strTitolo%>" border="0">
                </a>
              </td>
<%          }
            
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO)) {
 %>
                  <td>
                    <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&tipoOper=I&TornaQui=<%=TornaQui%>">
                      <img src="/images/new24.gif" alt="Inserisci <%=labelTipoImpugnazione %>" width="12" height="12" border="0">
                    </a>
                  </td>
<%
            }
          }
       }
      
%>
    </tr>
  </table>