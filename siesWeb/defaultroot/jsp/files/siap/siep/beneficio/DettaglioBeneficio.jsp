<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.siep.beneficio.action.ICostantiBeneficio"%>
<%@ page import="siap.siep.beneficio.model.BeneficioModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.siep.tipologiaorario.model.TipologiaOrarioModel" %>


<jsp:useBean id="beneficio" scope="request" class="siap.siep.beneficio.model.BeneficioModel"/>
<jsp:useBean id="tipologiaorario" scope="request" class="java.util.Vector"/>
<jsp:useBean id="beneficiononmenzione" scope="request" class="siap.siep.beneficio.model.BeneficioModel"/>
<jsp:useBean id="lTipoFunzione"       scope="request" class="java.lang.String"/>
<jsp:useBean id="modo"       scope="request" class="java.lang.String"/>


<%
	// Gestione funzione SIGE
	boolean modoSIGE = false;
	if (modo != null && modo.equalsIgnoreCase("SIGE"))
		modoSIGE = true;

  FascicoloSiepModel lFascicolo = (FascicoloSiepModel)session.getAttribute("fascicolo");
%>

<html>
<%if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
 {%>
<script language="JavaScript">
var aForm=null;
 function Verify()
  {
   alert("La funzione di Iscrizione Guidata è stata Interrotta");
   aForm=document.getElementById("Abbandona");
    Disabilita();
  }

 function DisabilitaM()
  {

   aForm=document.getElementById("Misura");
    Disabilita();
  }

 function Disabilita()
  {
    if (aForm==null)
       aForm=document.getElementById("Beneficio");

    document.Abbandona.A.disabled = true;
    document.Misura.M.disabled = true;
    document.Beneficio.B.disabled = true;

   aForm.submit();
  }
</script>
<%}%>
<head>
<title>[S.I.E.S.] - Dettaglio Beneficio </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>


		<body class="corpo">
<FORM name="comandi" >
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Beneficio</font>
      </td>
 				<%    
 				if(modoSIGE)
				{
					String   lModificabile = (String)request.getAttribute("Modificabile");
					String   lCancellabile = (String)request.getAttribute("Cancellabile");
				 %>     
				      <td class="LBG">
				          <jsp:include page="<%=IWebConstants.PG_TOOLBAR_HEADER_NOSIEP%>">
	          				<jsp:param name="CampoIdEntita" value="<%=ICostantiBeneficio.CAMPO_ID_BENEFICIO%>" />
	          				<jsp:param name="ValoreIdEntita" value="<%=beneficio.getIdBeneficio()%>" />
				          <jsp:param name="Modificabile" value="<%=lModificabile%>"/>
				          <jsp:param name="Cancellabile" value="<%=lCancellabile%>"/>
				        </jsp:include>
				     </td> 

  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
				     
				<%} else { %>          
      
      <td class="LBG">
        <jsp:include page="<%=ISIAPCostantiWeb.PG_TOOLBAR_GESTIONE_FASCICOLO_VALIDATO%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiBeneficio.CAMPO_ID_BENEFICIO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=beneficio.getIdBeneficio()%>" />
          <jsp:param name="FlagValidato" value="<%=lFascicolo.getFlagValidato()%>" />
        </jsp:include>
      </td>
      <%} %>
    </tr>
  </table>

	<br>
  <%if(!modoSIGE){%>
	    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <%} else {%>
	   	<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
		 	<jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
  <%}%>
  <br>

</FORM>
		 <table cellspacing=2 cellpadding=2>
		<tr>
				<td class="l">Natura Beneficio</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrNaturaBeneficio()) %></font></td>
		</tr>
		<tr>
				<td class="l">Tipologia Beneficio</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrTipoBeneficio()) %></font></td>
		</tr>
		<tr>
				<td class="l">Decisione Giudice</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrSottotipoBeneficio()) %></font></td>
		</tr>	
        <tr>
				<td class="l">Non Menzione</td>
				<td class="l">
                 <%if("02".equals(beneficio.getCodTipoBeneficio()) ||( beneficiononmenzione != null && beneficiononmenzione.getIdBeneficio()!= null)){%>
                        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>V.gif" border="0">
                 <%} %>&nbsp;
				</td>
		</tr>		
        <tr>
				<td class="l">Durata Sospensione</td>
				<td class="l">
				  Anni <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumAnniSospensione(),"0") %></font>&nbsp;
				</td>
		</tr>			

		<tr>
				<td class="l">Obblighi del condannato ex art 165 c.p. </td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getDescrTipoSospSubordinata()) %></font></td>
		</tr>
		
		<tr>
				<td class="l">Tipologia Obbligo</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getNote()) %></font></td>
		</tr>		
        <tr>
				<td class="l">Termine Adempimento Obbligo</td>
				<td class="l">
				<%
				if (beneficio.getNumAnniAdempimento()!=null || beneficio.getNumMesiAdempimento()!=null || beneficio.getNumGiorniAdempimento()!=null)
				{
				%>
					Anni <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumAnniAdempimento(),"0") %></font>&nbsp;
					Mesi <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumMesiAdempimento(),"0") %></font>&nbsp;
					Giorni <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumGiorniAdempimento(),"0") %></font>
				<%
				} else
				{ %>
					-
			  <%}%>
				</td>
		</tr>
		
<%if("08".equals(beneficio.getCodTipoSospSubordinata())) 
{%>		
		<tr>
				<td class="l">Durata Prestazione Attività Non Retribuita</td>
				<td class="l">
				<%
				if (beneficio.getNumMesiPrestazione()!=null || beneficio.getNumGiorniPrestazione()!=null)
				{
				%>
					Mesi <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumMesiPrestazione(),"0") %></font>&nbsp;
					Giorni <font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumGiorniPrestazione(),"0") %></font>
				<%
				} else
				{ %>
					-
				<%}%>
				</td>
		</tr>

		<tr>
				<td class="l">Ore settimanali</td>
				<td class="l"><font class="campo"><%=StringUtils.toStringJSP(beneficio.getNumOreSettimanali()) %></font></td>
	
		</tr>
                 <tr>
				<td class="l">Frequenza Settimanale</td>
				<td class="l">
				<%if("D".equals(beneficio.getFlagFrequenzaSettimanale())) {%>  
				  <font class="campo">Determinata</font>
				 <%}else if("N".equals(beneficio.getFlagFrequenzaSettimanale())) {%> 
				  <font class="campo">Non Determinata</font>				 
				 <%} %>
				</td>
		</tr>

		</table>

<%
      if(tipologiaorario != null && !tipologiaorario.isEmpty())
      {
%>   	  
       <table width="90%">      
    	 <tr><td class="titolo" colspan=10>Tipologia Orario</td></tr>     	  
<%     
       Iterator iter = tipologiaorario.iterator();
       while (iter.hasNext()) 
       {
     	  TipologiaOrarioModel TipoOrMod = (TipologiaOrarioModel) iter.next();
%> 
       <tr>
        <td class="l">Giorno</td>      
        <td class="l"><font class="campo"><%=StringUtils.toStringJSP(TipoOrMod.getDescrNumGiorno())%></font></td>
        <td class="l"> dalle <font class="campo"><%=StringUtils.toStringJSP(TipoOrMod.getDalleOre())%></font></td>
        <td class="l">  alle <font class="campo"><%=StringUtils.toStringJSP(TipoOrMod.getAlleOre())%></font></td>
       </tr>
	   <tr>
        <td class="l">Ente Incaricato dei controlli</td>
        <td class="l" colspan='3'><font class="campo"><%=StringUtils.toStringJSP(TipoOrMod.getEnteIncaricato())%></font></td>
       </tr>       
<%
       }
%>       
       </table> 
<%
      }	
  }
%>
<%if(!lTipoFunzione.equals("") && !lTipoFunzione.equals("ritornodettaglio"))  // lTipoFunzione per capire che si proviene da iscrizione guidata
 {%>
<table>
<tr>
<td class="lNoBord">
<FORM method="POST" name="Beneficio" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.beneficio.action.ActGrigliaIscrizioneBenefici&lTipoFunzione=<%=lTipoFunzione%>">
      <br><INPUT class="bottone" type="submit" name="B" value="Altro Beneficio concesso" onclick="Javascript:Disabilita();">
 </FORM>
</td>

<td class="lNoBord">
<FORM method="POST" name="Misura" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misurasicurezza.action.ActLoadInserisciMisuraSicurezza&lTipoFunzione=<%=lTipoFunzione%>">
      <br><INPUT class="bottone" type="submit" name="M" value="Misura Sicurezza" onclick="Javascript:DisabilitaM();">
 </FORM>
</td>

<td class="lNoBord">
<FORM method="POST" name="Abbandona" action="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.beneficio.action.ActRicercaBeneficio&lTipoFunzione=ritornodettaglio">
      <br><INPUT class="bottone" type="submit" name="A" value="Abbandona" onclick="Javascript:return Verify();">
 </FORM>
</td>
</tr>
</table>
<%}%>             
                             
       
		
	</body>
</html>