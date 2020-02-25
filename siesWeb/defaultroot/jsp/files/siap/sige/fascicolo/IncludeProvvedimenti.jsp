<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sige.provvedimento.util.ProvvedimentoSigeUtils"%>
<%@page import="siap.sige.udienza.model.UdienzaSigeModel"%>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>
<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="provvedimenti" scope="request" class="java.util.Vector"/>
<jsp:useBean id="flag_valida" scope="request" class="java.lang.String"/>
<jsp:useBean id="isModificabile" scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
<script language="JavaScript">

//funzione per il richiamo alla cancellazione
function conferma(a_action, a_parameter, a_entityname ,a_parameter2 ,a_entityname2)
{
   var documentoRegistrato = a_entityname2;
   if (window.confirm('Confermi la cancellazione ?'))
   {
      if (documentoRegistrato=="S")
      {
         window.open("<%= IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=" + a_action + "&" + a_parameter + "=" +a_entityname + "&" + a_parameter2 + "=" +a_entityname2, "Cancella_provvedimento","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
         window.parent.close();
      }else{
           str = "/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActCancellaProvvedimento&" +a_parameter +"=" + a_entityname;
               window.location.href=str;
      }
    }
}
// Funzione di visualizzazione motivo annullamento
function cancella(idEvento)
{
    window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.provvedimento.action.ActLoadCancellaProvvedimento&IdEvento="+idEvento,"Cancella_provvedimento", "top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
}
</script>
<%@page import="f3b.util.Utils"%>
<html>
<%
	boolean modificabile = true;
	if (isModificabile != null && isModificabile.equalsIgnoreCase("NO"))
  	  modificabile = false;

	String lFunAnnullaValidaProvvedimento = "";
	String lFunAnnullaValidaAllegato = "";

	if (flag_valida.equals(""))
  	  flag_valida="SI";
	String isDepositato = "NO";
	
     // Estrazione funzioni annulla Validazione
    Collection <FunctionModel>lFunzFiglie = (Collection<FunctionModel>)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
    for (FunctionModel lFunz : lFunzFiglie) {
        if(lFunz.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_LINK)  && lFunz.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA)) {
            if (lFunz.getVisualizationOrder().intValue() == 1)
                lFunAnnullaValidaProvvedimento = lFunz.getNameAction();
           	else if (lFunz.getVisualizationOrder().intValue() == 2)
                lFunAnnullaValidaAllegato = lFunz.getNameAction();
        }
    }
	

%>
	<%if ( provvedimenti.size() == 0 ) {%>
  	<table>
        <td class="LBG">
          <font class="label"> Non ci sono provvedimenti riferiti al procedimento indicato </font>
        </td>
  </table>
<%} else {%>
  <table width="96%">
  
    <tr>
      <td class="int" nowrap="nowrap">Data emissione</td>
      <td class="int" >Tipo provvedimento</td>
      <td class="int" >Oggetti provvedimento</td>
      <td class="int" nowrap="nowrap">Data Udienza</td>
      <td class="int" nowrap="nowrap">Data Deposito</td>
      <td class="int" width="9%">Ricorsi</td>
      <td class="int" width="9%">Opposizioni</td>
      <td class="int" >Provv.<br />Validato</td>
      <td class="int" >Deposito<br />Validato</td>
    </tr>
  
<%
    Vector <ProvvedimentoSigeEventoModel> provv=(Vector <ProvvedimentoSigeEventoModel>)provvedimenti;
    ProvvedimentoSigeUtils provvUtils=new ProvvedimentoSigeUtils();
    for (ProvvedimentoSigeEventoModel lProvEve : provv ) {
  %>
    <tr>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lProvEve.getEventoNotifica().getEvento().getDataEmissione(),"dd-MM-yyyy"),"-") %></td>
      <td class="l" ><%=provvUtils.getDescrizioneTipoProvvedimento(lProvEve) %></td>
      <td class="l"><%=provvUtils.getOggettiProvvedimento(lProvEve) %></td>
      <td class="l"><%=provvUtils.getLinkDettaglioOrdinanzaUdienza(lProvEve) %></td>
      <%--td class="c" ><%=StringUtils.toStringJSP(DateUtils.getDateToString(lProvEve.getProvvedimento().getDataDeposito(),"dd-MM-yyyy"),"-")%></td --%>
      <td class="c"><%=provvUtils.getLinkDettaglioDeposito(lProvEve) %></td>
      <td class="c"><%=provvUtils.getDataRicorso(lProvEve.getRicorsi()) %> </td>
      <td class="c"><%=provvUtils.getDataOpposizione(lProvEve.getOpposizioni()) %> </td>
      <%=provvUtils.getProvvedimentoValidato(lProvEve, flag_valida, lFunAnnullaValidaProvvedimento, modificabile) %>
      <%=provvUtils.getDepositoValidato(lProvEve, flag_valida, lFunAnnullaValidaAllegato) %>
    </tr>
<%
   } // endwhile
%>
  </table>
<%
  }  // endif provvedimenti.size()
%>
  </body>
</html>