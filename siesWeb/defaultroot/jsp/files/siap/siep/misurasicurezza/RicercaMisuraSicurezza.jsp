<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.List" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.misurasicurezza.action.ICostantiMisuraSicurezza" %>
<%@ page import="siap.siep.misurasicurezza.model.MisuraSicurezzaModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<%
	// Gestione funzione SIGE
	boolean modoSIGE = false;
	String   lModificabile = "";
	String   lCancellabile = "";

	if (modo != null && modo.equalsIgnoreCase("SIGE"))
	{
		modoSIGE = true;
		lModificabile = (String)request.getAttribute("Modificabile");
		lCancellabile =  (String)request.getAttribute("Cancellabile");
	}
	
	FascicoloSiepModel lFascicolo = null;
	if (!modoSIGE)
  		lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Misura Sicurezza</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

  <BODY class="corpo">
  <FORM method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;
        <font class="campo">Elenco Misura Sicurezza</font></td>
    <% 
     if(modoSIGE)
     {
  %>     
      <td class="LBG">
          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE%>"/>
         <jsp:param name="ValoreIdEntita" value="0"/>
          <jsp:param name="Modificabile" value="<%=lModificabile%>"/>
          <jsp:param name="Cancellabile" value="<%=lCancellabile%>"/>
        </jsp:include>
     </td> <%} %>     
  	 <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
     </tr>
   </table>
   <br>
  <%if (!modoSIGE)
		{%>
    	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <%} else {%>
	   <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
		 <jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
  <%}%>
  <br>
<%
	List misurasicurezza =(List) request.getAttribute("misurasicurezza");

	if(misurasicurezza.size() > 0)
	{
%>    
    <br>

    <table cellpadding=2 cellspacing=2>

    <tr>
      <td class="int">Natura Misura</td>
      <td class="int">Tipo Misura</td>
      <td class="int">Num. Anni</td>
      <td class="int">Num. Mesi</td>
      <td class="int">Num. Giorni</td>

<!--  	17-12-2014	 	-->
	  <td class="int"> Data fine Validita</td>
	  <td class="int"> Stato </td>

<!-- 	15_01-2015	 -->
	  <td class="int"> Anno/Numero Procedimento</td>
	  <td class="int"> Data Provvedimento </td>
	  	      
      <td class="int">Azioni</td>
     </tr>

<%  String valClass="";
	String Annullato="";
    Iterator itx = misurasicurezza.iterator();
    while ( itx.hasNext())
    {
      MisuraSicurezzaModel lMis = (MisuraSicurezzaModel)itx.next();
      if(lMis != null && lMis.getIdMisuraSicurezza() != null)
    	  if(lMis.getFlagAnnullaMisura() != null && lMis.getFlagAnnullaMisura().compareTo("A")==0 )
    	  {	  
    		  valClass = "cRosso";
    		  Annullato = "SI";
    	  }	  
    	  else
    	  {	  
    		  valClass = "C";
    		  Annullato = "NO";
    	  }	  
%>
    <tr>
      <td class="<%=valClass%>"><%=StringUtils.toStringJSP(lMis.getDescrNatura())%>&nbsp;</td>
      <td class="<%=valClass%>"><%=StringUtils.toStringJSP(lMis.getDescrTipo())%>&nbsp;</td>
      <td class="<%=valClass%>"><%=StringUtils.toStringJSP(lMis.getNumAnni(),"0")%>&nbsp;</td>
      <td class="<%=valClass%>"><%=StringUtils.toStringJSP(lMis.getNumMesi(),"0")%>&nbsp;</td>
      <td class="<%=valClass%>"><%=StringUtils.toStringJSP(lMis.getNumGiorni(),"0")%>&nbsp;</td>

<!--  	17-12-2014	 	-->
      <td class="<%=valClass%>"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMis.getDataFineValidita(),"dd-MM-yyyy"),"-") %>&nbsp;</td>
      <td class="<%=valClass%>"><%=StringUtils.toStringJSP(lMis.getFlagAnnullaMisura(),"-")%>&nbsp;</td>

<!--  	15-01-2015	 	-->
<%	if(lMis.getRiferimentoFascicoloSiep() != null && 
		lMis.getRiferimentoFascicoloSiep().getIdRiferimentoFascicoloSiep() != null) 
	{ 
		String Tipo="";
		if(lMis.getRiferimentoFascicoloSiep().getFlagMS() != null &&
			lMis.getRiferimentoFascicoloSiep().getFlagMS().equals("M") )
		{	
			Tipo="Reg.Mod.38"; 
		}
		else if(lMis.getRiferimentoFascicoloSiep().getFlagMS() != null &&
				lMis.getRiferimentoFascicoloSiep().getFlagMS().equals("N") )
			{ 
				Tipo="SIEP";
			}
			else
			{}%>
			
		<td class="<%=valClass%>">
			<%=StringUtils.toStringJSP(lMis.getRiferimentoFascicoloSiep().getAnnoFascicoloSiep())%>&nbsp;
			/
			<%=StringUtils.toStringJSP(lMis.getRiferimentoFascicoloSiep().getProgrFascicoloSiep())%>&nbsp;
			<%=Tipo%>
		</td>
      	<td class="<%=valClass%>">
      		<%=StringUtils.toStringJSP(DateUtils.getDateToString(lMis.getRiferimentoFascicoloSiep().getDataProvvedimento(),"dd-MM-yyyy"),"") %>&nbsp;
      	</td>

<%	}
	else
	{ %>
		<td class="<%=valClass%>">&nbsp;</td>
		<td class="<%=valClass%>">&nbsp;</td>
<%	} %>
	
<!-- 		 -->
	      
      <td class="<%=valClass%>">
 <% if(modoSIGE) { %> 
         <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
        <jsp:param name="CampoIdEntita" value="<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>"/>
        <jsp:param name="ValoreIdEntita" value="<%=lMis.getIdMisuraSicurezza()%>"/>
           <jsp:param name="Modificabile" value="<%=lModificabile%>" />
        </jsp:include>
       </td>
<% 
 }else{
       
       String modificabile = "";
           if (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(lFascicolo.getChiaveUfficio() ) 
        	  	&& Annullato.compareTo("NO") == 0 )   
              {modificabile = "SI";}
           else
              {modificabile = "NO";}
 %>
 <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%-- jsp:include page="<%=ISIAPCostantiWeb.PG_BUTTONS_GESTIONE_FASCICOLO_VALIDATO%>" --%>
      <jsp:include page="<%=IWebConstants.PG_BUTTONS_RICERCA_MISURE_SICUREZZA_SIEP%>">
        <jsp:param name="CampoIdEntita" value="<%=ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA%>"/>
        <jsp:param name="ValoreIdEntita" value="<%=lMis.getIdMisuraSicurezza()%>"/>
        <jsp:param name="FlagValidato" value="<%=lFascicolo.getFlagValidato()%>" />
        <jsp:param name="Modificabile" value="<%=modificabile%>" />
      </jsp:include>
      </td>
<%} %>
    </tr>
<%
    }
%>
    </table>
 <%
   }else { %>
    <table width="80%">
    <tr>
          <td class="int" align="left">Nessuna Misura di Sicurezza definita </td>
  </tr>
  <% }// endif  %>
    
  </FORM>
</body>
</html>