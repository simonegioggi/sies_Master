<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants" %>

<%@ page import="siap.sius.udienza.action.ICostantiUdienza" %>
<%@ page import="siap.sius.udienza.model.UdienzaMagistratoRelModel" %>
<%@ page import="siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento" %>

<jsp:useBean id="procedimenti" 	scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="TornaQui"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="udienza" 			scope="request" class="siap.sius.udienza.model.UdienzaModel"/>
<jsp:useBean id="periodo"     	scope="request" class="java.lang.String"/>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<html>
 <head>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <title>[S.I.E.S.] - ListaProcedimentiUdieneMagistratiProcedimenti</title>
  <script language="JavaScript" src="/html/conferma.js"></script>
  <script language="JavaScript">
      function ListaUdienze(aNomeForm,aCampoID,aNomeCampoGG,aNomeCampoMM,aNomeCampoAA,aNomeCampoLuogo,aNomeCampoColl)
      {
        var desktop;

        var lLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.udienza.action.ActLoadRicercaUdienzaXProcedimenti";
        lLink += "&formname="+ aNomeForm;
        lLink += "&campoID="+ aCampoID;
        lLink += "&campoGG=" + aNomeCampoGG;
        lLink += "&campoMM=" + aNomeCampoMM;
        lLink += "&campoAA=" + aNomeCampoAA;
        lLink += "&campoLuogo=" + aNomeCampoLuogo;
        lLink += "&campoColl=" + aNomeCampoColl;
        desktop = window.open(lLink, "Lista_Udienze","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=800,height=550");
      }
  </script>  
 </head>

  <body class="corpo">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font>
      <font class="campo"> Elenco numero procedimenti per magistrato relatore e per udienza</font></td>
     </td>
      <td class="LBG" nowrap>
          <jsp:include page="<%=IWebConstants.PG_COMBO_STAMPA2P%>">
          <jsp:param name="CampoIdEntita" value="Data1" />
          <jsp:param name="ValoreIdEntita" value="<%=DateUtils.getDateToString(udienza.getDataUdienza(), "ddMMyyyy")%>" />
          <jsp:param name="CampoIdEntitaPP" value="Data2" />
          <jsp:param name="ValoreIdEntitaPP" value="<%=DateUtils.getDateToString(udienza.getDataUdienzaFine(), "ddMMyyyy")%>" />
       </jsp:include>
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>
<br>
	<table>
	<tr>
	<td class="lVerdeNB"><%=periodo%> </td>
	</tr>
	</table>

	<table>
		<tr>
			<td class="label">
  			<a href="Javascript:ListaUdienze( 'ListaUdienzeMagistratiProcedimenti',
                                       '<%=ICostantiUdienzaProcedimento.CAMPO_UDI_ID_UDIENZA%>',
                                       '<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA%>',
                                       '<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA%>',
                                       '<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>',
                                       '<%=ICostantiUdienza.CAMPO_LUOGO_UDIENZA%>',
                                       '<%=ICostantiUdienza.CAMPO_NUM_COLLEGIO%>');">
                                       Visualizza elenco udienze
        		<img src="/images/filefolder.gif" border=0>
    		</a>
 			</td>
    </tr>
	</table>

  <br>
  
<% if (procedimenti.size() > 0)
   {
      Iterator iTx = procedimenti.iterator();
 %>
  <table width=90%>
    <tr>
      <td class="int" width=25%>Udienza</td>
      <td class="int" width=35%>MAGISTRATO</td>
      <td class="int" width=15%>Totale procedimenti</td>
      <td class="int" width=15%>Di cui gia Rinviati/Prefissati</td>
    </tr>
<%
  while ( iTx.hasNext())
  {
     UdienzaMagistratoRelModel lUdiMagProcCorr = (UdienzaMagistratoRelModel)iTx.next();
%>
   <tr>
   <td class="l" >
   		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.udienzaprocedimento.action.ActRicercaUdienzaProcedimento&tiporicerca=xmagistrato&tipo=PP&<%=ICostantiUdienzaProcedimento.CAMPO_UDI_ID_UDIENZA%>=<%=lUdiMagProcCorr.getIdUdienza()%><%=retParam%>" Title="Procedimenti per Udienza">
      	<%=DateUtils.getDateToString(lUdiMagProcCorr.getDataUdienza(),"dd-MM-yyyy") + " Coll. " + lUdiMagProcCorr.getNumCollegio()%>
      </a>
   </td>
   <%
    for ( int i = lUdiMagProcCorr.getNumMagistrati() - 1; i >= 0; i--) { %>
   <td class="l"> <%= StringUtils.toStringJSP(lUdiMagProcCorr.getMagistrato(i).getCognome()) + " " + StringUtils.toStringJSP(lUdiMagProcCorr.getMagistrato(i).getNome()) %></td>
   <td class="l"> <%="" + lUdiMagProcCorr.getMagistrato(i).getNumProcedimenti()%></td>
   <td class="l"> <%="" + lUdiMagProcCorr.getMagistrato(i).getNumProcedimentiDaRinvio() + " / " + lUdiMagProcCorr.getMagistrato(i).getNumProcedimentiPrefissati()%></td>
    </tr>
   <tr><td class="l" width=15%> . </td>
   <% if (i == 0) { %>
   <td class="c"> Totali: </td>
   <td class="l"> <%="" + lUdiMagProcCorr.getNumProcedimenti()%></td>
   <td class="l"><%="" + lUdiMagProcCorr.getNumProcedimentiDaRinvio() + " / " + lUdiMagProcCorr.getNumProcedimentiPrefissati()%></td>
    </tr>
 <% }}} %>
    </table>
<% } else{%>
 <font class="campo"> Nessun elemento trovato </font>
<% }%>
  </form>
  <br>
  </body>
</html>