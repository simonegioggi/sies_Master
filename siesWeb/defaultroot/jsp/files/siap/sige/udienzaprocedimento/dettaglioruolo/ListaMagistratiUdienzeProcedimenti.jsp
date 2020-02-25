<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.sius.udienza.model.UdienzaMagistratoRelModel" %>
<%@ page import="siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento" %>

<jsp:useBean id="procedimenti" 	scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="TornaQui"     	scope="request" class="java.lang.String"/>
<jsp:useBean id="udienza" 			scope="request" class="siap.sius.udienza.model.UdienzaModel"/>
<jsp:useBean id="StackDiRitorno" scope="session" class="java.util.Stack"/>
<jsp:useBean id="VisualizzaUdienza"     	scope="session" class="java.lang.String"/>
<jsp:useBean id="periodo"     	scope="request" class="java.lang.String"/>


<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  
  String lRet = "#";
  if (StackDiRitorno != null)
  {
    if (!StackDiRitorno.isEmpty())
    {
      lRet = (String) StackDiRitorno.peek();
      lRet  += "&" + IWebConstants.FLAG_RITORNO + "=1";
    }
  }
  
  // Switch sulla visualizzazione dei dati relativi alle udienze
  boolean UdienzaSi = true;
  String lMessaggioUdienza = "Nascondi Udienze";
  if (VisualizzaUdienza.equalsIgnoreCase("NO"))
  {
	  UdienzaSi = false;
	  lMessaggioUdienza = "Visualizza Udienze";
  }
 
  
%>

<html>
 <head>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <title>[S.I.E.S.] - ListaProcedimentiUdieneMagistratiProcedimenti</title>
  <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font>
      <font class="campo"> Elenco numero procedimenti per magistrato relatore e per udienza</font></td>

     </td>
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
  			<a href="<%=lRet%>">
  			<td class="bottone">&nbsp;&nbsp; <%=lMessaggioUdienza%> &nbsp;&nbsp;
  			</td>
  			</a>
    	</td>
    </tr>
 	</table>

  <br>
  
<% if (procedimenti.size() > 0)
   {
 %>
  <table width=90%>
    <tr>
      <td class="int" width=35%>MAGISTRATO</td>
      <%if(UdienzaSi) { %>
       <td class="int" width=25%>Udienza</td>
       <%} %>
      <td class="int" width=15%>Totale procedimenti</td>
      <td class="int" width=15%>Di cui gia Rinviati/Prefissati</td>
    </tr>
<%
	// Totalizzatori complessivi
	int TotGen1 = 0, TotGen2 = 0, TotGen3 = 0;

  // Ciclo su tutte le udienze (i)
  for (int i=0; i < procedimenti.size(); i++)
  {
	int Tot1 = 0, Tot2 = 0, Tot3 = 0;
	UdienzaMagistratoRelModel lUdiMagProcCorr = (UdienzaMagistratoRelModel)procedimenti.get(i);
	// Ciclo sui Magistrati relativi all'Udienza corrente (j)
    for ( int j = lUdiMagProcCorr.getNumMagistrati() - 1; j >= 0; j--) { 	
    
    if ((lUdiMagProcCorr.getMagistrato(j).getMessage() == null || lUdiMagProcCorr.getMagistrato(j).getMessage().length() == 0) && lUdiMagProcCorr.getMagistrato(j).getTipoMagistratoRelatore().length() > 0 )
    {
    %>
   <tr><td class="l"> <%=StringUtils.toStringJSP(lUdiMagProcCorr.getMagistrato(j).getCognome()) + " " + StringUtils.toStringJSP(lUdiMagProcCorr.getMagistrato(j).getNome()) + " " + StringUtils.toStringJSP( lUdiMagProcCorr.getMagistrato(j).getTipoMagistratoRelatore()) %></td>
   <%if (UdienzaSi) { %>
    <td class="l" >
   		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.udienzaprocedimento.action.ActRicercaUdienzaProcedimento&tiporicerca=xmagistrato&tipo=PP&<%=ICostantiUdienzaProcedimento.CAMPO_UDI_ID_UDIENZA%>=<%=lUdiMagProcCorr.getIdUdienza()%><%=retParam%>" Title="Procedimenti per Udienza">
      	<%=DateUtils.getDateToString(lUdiMagProcCorr.getDataUdienza(),"dd-MM-yyyy") + " Coll. " + lUdiMagProcCorr.getNumCollegio()%>
      </a>
   </td>
 
   <td class="l"> <%="" + lUdiMagProcCorr.getMagistrato(j).getNumProcedimenti()%></td>
   <td class="l"> <%="" + lUdiMagProcCorr.getMagistrato(j).getNumProcedimentiDaRinvio() + " / " + lUdiMagProcCorr.getMagistrato(j).getNumProcedimentiPrefissati()%></td>
    </tr>
 <%  
   }
 	lUdiMagProcCorr.getMagistrato(j).setMessage("preso");
	//Incremento dei totalizzatori 
 	Tot1 +=  lUdiMagProcCorr.getMagistrato(j).getNumProcedimenti();
 	Tot2 +=  lUdiMagProcCorr.getMagistrato(j).getNumProcedimentiDaRinvio();
 	Tot3 +=  lUdiMagProcCorr.getMagistrato(j).getNumProcedimentiPrefissati();
 
 	TotGen1 +=  lUdiMagProcCorr.getMagistrato(j).getNumProcedimenti();
 	TotGen2 +=  lUdiMagProcCorr.getMagistrato(j).getNumProcedimentiDaRinvio();
 	TotGen3 +=  lUdiMagProcCorr.getMagistrato(j).getNumProcedimentiPrefissati();

 	// Ricerca dello stesso magistrato per le restanti udienze (ii)
 	for (int ii = i + 1; ii < procedimenti.size(); ii++)
 	{
 		UdienzaMagistratoRelModel lUdiMagProc2 = (UdienzaMagistratoRelModel)procedimenti.get(ii);
 		// Ciclo sui Magistrati relativi alla seconda Udienza
 	    for ( int jj = lUdiMagProc2.getNumMagistrati() - 1; jj >= 0; jj--) {
 	    	if( lUdiMagProcCorr.getMagistrato(j).equals(lUdiMagProc2.getMagistrato(jj)) && (lUdiMagProc2.getMagistrato(jj).getMessage() == null || lUdiMagProc2.getMagistrato(jj).getMessage().length() == 0) )
 	    	{
 	    			lUdiMagProc2.getMagistrato(jj).setMessage("preso");
 	    	%>
 	    <%if (UdienzaSi) { %>
 	    	 <tr><td class="l" width=15%> . </td>
 	   
            <td class="l" >
   		<a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.udienzaprocedimento.action.ActRicercaUdienzaProcedimento&tiporicerca=xmagistrato&tipo=PP&<%=ICostantiUdienzaProcedimento.CAMPO_UDI_ID_UDIENZA%>=<%=lUdiMagProc2.getIdUdienza()%><%=retParam%>" Title="Procedimenti per Udienza">
      	<%=DateUtils.getDateToString(lUdiMagProc2.getDataUdienza(),"dd-MM-yyyy") + " Coll. " + lUdiMagProc2.getNumCollegio()%>
      </a>
   </td>
    <td class="l"> <%="" + lUdiMagProc2.getMagistrato(jj).getNumProcedimenti()%></td>
   <td class="l"> <%="" + lUdiMagProc2.getMagistrato(jj).getNumProcedimentiDaRinvio() + " / " + lUdiMagProc2.getMagistrato(jj).getNumProcedimentiPrefissati()%></td>
 	</tr>
 <%
 	 }
 	// Incremento dei totalizzatori 
	Tot1 +=  lUdiMagProc2.getMagistrato(jj).getNumProcedimenti();
	Tot2 +=  lUdiMagProc2.getMagistrato(jj).getNumProcedimentiDaRinvio();
	Tot3 +=  lUdiMagProc2.getMagistrato(jj).getNumProcedimentiPrefissati();

	TotGen1 +=  lUdiMagProc2.getMagistrato(jj).getNumProcedimenti();
	TotGen2 +=  lUdiMagProc2.getMagistrato(jj).getNumProcedimentiDaRinvio();
	TotGen3 +=  lUdiMagProc2.getMagistrato(jj).getNumProcedimentiPrefissati();
	
 	} // endif uguaglianza tra Magistrati
 	    	
 } // Fine ciclo Magistrati su altra Udienza (jj)
 } // Fine ciclo altre Udienze (μμ)
 // Totali
 
 %>
    <%if (UdienzaSi){ %>
   <tr><td class="l" width=15%> . </td>
   <td class="c"> Totali: </td>
   <%} %>
   <td class="l"> <%="" + Tot1%></td>
   <td class="l"><%="" +Tot2 + " / " + Tot3%></td>
    </tr>
<%
	// reset totalizzatori
	Tot1 = 0;
	Tot2 = 0;
	Tot3 = 0;
   } // endif
}// Fine magistrati relativi alla stessa Udienza (j)

 } // Fine udienza corrente (μ)
 
 
%>
  <tr> <td class="c"> Totali complessivi: </td>
    <%if (UdienzaSi){ %>
   <td class="l" width=15%> </td>
   <%} %>
   <td class="l"> <%="" + TotGen1%></td>
   <td class="l"><%="" +TotGen2 + " / " + TotGen3%></td>
    </tr>
    </table>
<% } else{%>
 <font class="campo"> Nessun elemento trovato </font>
<% }%>
  </form>
  <br>
  </body>
</html>