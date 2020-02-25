<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.math.BigDecimal" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza" %>

<%@ page import="siap.sico.residenza.action.ICostantiResidenza" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.residenza.model.ResidenzaModel" %>
<%@ page import="siap.sico.residenza.model.ResidenzaAssociataModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="f3b.web.RedirectTo"%>

<jsp:useBean id="misureSicurezza" 	scope="request" class="java.util.Vector" />
<!--  jsp:useBean id="fascicoloSiusGP" 	scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/ -->
<jsp:useBean id="caller" 			scope="request" class="java.lang.String" />
<jsp:useBean id="dataFineValidita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     		scope="request" class="java.lang.String"/>

<%@page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@page import="siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza"%>
<%@ page import="siap.sius.rifasiep.action.ICostantiRifFascicoloSiep" %>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  boolean lFlagPopUp = false;
  if( (request.getParameter("PopUp") != null)&& (request.getParameter("PopUp").equals("Y")) )
  {
    lFlagPopUp = true;
  }
%>

<!-- 		LoadPopupDettaglioMisuraSic		 -->
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Dettaglio Misure</title>
    <script language="JavaScript">
    
    function Chiudi()
    {
    	window.parent.close();
    }
    
    function controlla()
    {
	        if(document.elenco.numeroMisure.value==0)
	        {
		          alert(" Misure Sicurezza non presenti");
		          window.parent.close();
	        }
    }
    
    </script>
    
  </head>

  <body class="corpo" onload="controlla();">

  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
  	<input type="hidden" name="numeroMisure" value="<%=misureSicurezza.size()%>">
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class=label>Funzione :</font>
        <font class=campo>Lista Dettaglio Misure Sicurezza</font>
      </td>
    </tr>
  </table>

<%
	if (misureSicurezza.size() == 0 )
  	{
%>
	<tr>
    <td class="L" >
      <font class="label" > <font color="red"> Misure Sicurezza non presenti </font>
    </td>
    </tr>
<% 	} else {
%>    
    <br>
	<table>
	<tr><td class="Titolo" colspan=7> Misure Sicurezza Ufficio Sorveglianza</td></tr>
	</table>
	
    <table cellpadding=2 cellspacing=2 >
    <tr>
      <td class="int">Natura Misura</td>
      <td class="int">Tipo Misura</td>
      <td class="int">Num. Anni</td>
      <td class="int">Num. Mesi</td>
      <td class="int">Num. Giorni</td>
      <td class="int">Fine Validità</td>
      <td class="int">Anno/Numero SIEP </td>
      <td class="int">Data Sentenza.</td>
     </tr>

<%
    Iterator itx = misureSicurezza.iterator();
    while ( itx.hasNext())
    {
      MisuraSicurezzaModel lMis = (MisuraSicurezzaModel)itx.next();
%>
    <tr>
      <td class=C><%=StringUtils.toStringJSP(lMis.getDescrNatura())%>&nbsp;</td>
      <td class=C><%=StringUtils.toStringJSP(lMis.getDescrTipo())%>&nbsp;</td>
      <td class=C><%=StringUtils.toStringJSP(lMis.getNumAnni(),"0")%>&nbsp;</td>
      <td class=C><%=StringUtils.toStringJSP(lMis.getNumMesi(),"0")%>&nbsp;</td>
      <td class=C><%=StringUtils.toStringJSP(lMis.getNumGiorni(),"0")%>&nbsp;</td>
      
<%	  if (lMis.getDataFineValidita() != null && lMis.getEveIdEvento() == null )  { 
%>    
	  <td class=C><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMis.getDataFineValidita(), "dd-MM-yyyy") )%>"&nbsp;</td>
<%	 	} 
	  else { 
%>
	  	  <td class=C>- &nbsp;</td>
<%	  }  %>

<% 
	 if(lMis.getFascicoloSiep() != null){
%>	  
      <td class=C><font class="campo">
         <%=lMis.getFascicoloSiep().getChiaveAnno()%>
            /
         <%=lMis.getFascicoloSiep().getChiaveProgr()%>
      </font>&nbsp;</td>

        <td class=C>
        <%
        if (lMis.getFascicoloSiep().getSentenza().getDataProvvedimento() != null)
        {%>
          <font class="campo"><%=DateUtils.getDateToString(lMis.getFascicoloSiep().getSentenza().getDataProvvedimento(),"dd-MM-yyyy")%></font>&nbsp;
        <%}%>
		
		</td>        

<% 
 } else {
%>
      <td class=C>&nbsp;</td>
      <td class=C>&nbsp;</td>  
<%	
}

   } %>
   
   	</tr>
    </table> 
   
<% }   %>
   
  </form>
  </body>
</html>