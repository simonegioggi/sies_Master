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

<jsp:useBean id="misureSicurezza" scope="request" class="java.util.Vector" />
<jsp:useBean id="isModificabile" scope="request" class="java.lang.String" />
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="caller" scope="request" class="java.lang.String" />
<jsp:useBean id="dataFineValidita"     scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<%@page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel"%>
<%@page import="siap.sius.misurasicurezza.action.ICostantiSiusMisuraSicurezza"%>
<%@ page import="siap.sius.rifasiep.action.ICostantiRifFascicoloSiep" %>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Misure Sicurezza</title>
    <script language="JavaScript" src="/html/conferma.js"></script>
  </head>

  <body class="corpo">

  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class=label>Funzione :</font>
        <font class=campo>Elenco Misure Sicurezza</font>
      </td>

      <!-- BOTTONE DI INSERIMENTO -->
      <td class="LBG">
        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.misurasicurezza.action.ActLoadInserisciSiusMisuraSicurezza&IdFascicoloSius=<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>" >
          <img  align="middle" src="/images/new24.gif" alt="Inserimento Misura Sicurezza" width="24" height="24" border="0">
        </a>
      </td>
      <!-- BOTTONE DI RITORNO -->
        <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
        
    </tr>
  </table>
  <br>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  <br>

  <table>
<%
	if (misureSicurezza.size() == 0 )
  	{
%>
    <td class="L" >
      <font class="label" > <font color="red"> Misure Sicurezza non presenti </font>
    </td>
<% 	} else {
%>    
    <br>

    <table cellpadding=2 cellspacing=2>

    <tr>
      <td class="int">Natura Misura</td>
      <td class="int">Tipo Misura</td>
      <td class="int">Num. Anni</td>
      <td class="int">Num. Mesi</td>
      <td class="int">Num. Giorni</td>
      <td class="int">Fine Validità</td>
      <td class="int">Anno/Num. SIEP</td>
      <td class="int">Data Sent.</td>
      <td class="int">Azioni</td>
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
<%	  if (dataFineValidita != null && lMis.getEveIdEvento() == null )  { 
%>    
	  <td class=C><%=dataFineValidita%>&nbsp;</td>
<%	 	} 
	  else { 
%>
	  	  <td class=C>- &nbsp;</td>
<%	  }  %>

<% 
if(lMis.getRiferimentoFascicoloSiep() != null){
%>
      <td class=C><font class="label">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.rifasiep.action.ActLoadDettaglioRifFascicoloSiep&<%=ICostantiRifFascicoloSiep.CAMPO_ID_RIFERIMENTO_FASCICOLO_SIEP%>=<%=lMis.getRiferimentoFascicoloSiep().getIdRiferimentoFascicoloSiep()%><%=retParam%>" >

<% 
	if( lMis.getRiferimentoFascicoloSiep().getFlagMS() == null || lMis.getRiferimentoFascicoloSiep().getFlagMS().equals("") || lMis.getRiferimentoFascicoloSiep().getFlagMS().equals("N")) {
%>
            <%=lMis.getRiferimentoFascicoloSiep().getAnnoFascicoloSiep()%>
            /
            <%=lMis.getRiferimentoFascicoloSiep().getProgrFascicoloSiep()%>
<%
	} else {
		if (lMis.getRiferimentoFascicoloSiep().getAnnoFascicoloSiep() == null && lMis.getRiferimentoFascicoloSiep().getProgrFascicoloSiep() == null){
			if(lMis.getRiferimentoFascicoloSiep().getFlagMS().equals("M")){
%>		
				Es. Mis. Sic.	
<%		
			} else {
%>
				Es. Pene Pec.
<%				
			}
		} else {
			if(lMis.getRiferimentoFascicoloSiep().getFlagMS().equals("M")){
%>
				<%=lMis.getRiferimentoFascicoloSiep().getAnnoFascicoloSiep()%>
				/ 
				<%=lMis.getRiferimentoFascicoloSiep().getProgrFascicoloSiep()%> MS
<%
			} else {
%>				
				<%=lMis.getRiferimentoFascicoloSiep().getAnnoFascicoloSiep()%>
				/ 
				<%=lMis.getRiferimentoFascicoloSiep().getProgrFascicoloSiep()%> PP
<%				
			}
		}
	}
%>

          </a>
        </font>&nbsp;</td>

        <td class=C>
        <%
        if (lMis.getRiferimentoFascicoloSiep().getDataInserimento() != null)
        {%>
          <font class="campo"><%=DateUtils.getDateToString(lMis.getRiferimentoFascicoloSiep().getDataInserimento(),"dd-MM-yyyy")%></font>&nbsp;
        <%}%>
		
		</td>        

<% 
} else if(lMis.getFascicoloSiep() != null){
%>	  
      <td class=C><font class="label">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lMis.getFascicoloSiep().getIdFascicoloSiep()%><%=retParam%>" >
            <%=lMis.getFascicoloSiep().getChiaveAnno()%>
            /
            <%=lMis.getFascicoloSiep().getChiaveProgr()%>
          </a>
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
      <td class=C><font class="label">&nbsp;</td>
      <td class=C>&nbsp;</td>  
<%	
}
%>	

<%				
			if (isModificabile.compareTo("SI")==0 )	{ 
%>
       			<td class=c>
        			<jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
           			<jsp:param name="CampoIdEntita" value="<%=ICostantiSiusMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>" />
           			<jsp:param name="ValoreIdEntita" value="<%=lMis.getIdMisuraSicurezza()%>" />
        			</jsp:include>
      			</td>
    		</tr>
    		<%} else {%>
      			<td class=c>-</td>
    		</tr>
    		<%}
    }
%>
    </table>
 <%
   } %>
  </form>
  </body>
</html>