<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.posizionemateriale.model.PosizioneMaterialeModel"%>
<%@ page import="siap.siep.posizionemateriale.action.ICostantiPosizioneMateriale"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>

<jsp:useBean id="fascicoliposizionemateriale" scope="request" class="siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascicoliModel"/>
<%--
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel"/>
--%>

<%
	PosizioneMaterialeModel lPosizioneMateriale = fascicoliposizionemateriale.getPosizioneMateriale();  
%>

<%--
		PosizioneMaterialeModel lPosizioneMateriale = fascicoliposizionemateriale.getPosizioneMateriale();  
	
		String lModificabile= "SI";
	  if (lPosizioneMateriale.getCodOperatoreInserimento().compareTo( UtenteConnesso.getUserId()) == 0)
	  {
	    lModificabile= "SI";
	  }
	  else
	  {
	    lModificabile= "NO";
	  }
--%>

<html>
<head>
<title>[S.I.A.P.] - Dettaglio PosizioneMateriale </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/conferma.js"></script>

</head>

	<body class="corpo">
    <table>
      <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo">Dettaglio Posizione Materiale</font>
      </td>
      <td class="LBG">
          <jsp:include page="<%=ICostantiPosizioneMateriale.PG_TOOLBAR_HEADER%>">
           <jsp:param name="CampoIdEntita" value="<%=ICostantiPosizioneMateriale.CAMPO_COD_POSIZIONE_MATERIALE%>"/>
           <jsp:param name="ValoreIdEntita" value="<%=lPosizioneMateriale.getCodPosizioneMateriale()%>" />
           <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiPosizioneMateriale.CAMPO_COD_UFFICIO%>"/>
           <jsp:param name="ValoreIdEntitaProvv" value="<%=lPosizioneMateriale.getCodUfficio()%>" />
       </jsp:include>
     </td>
  	<!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
   </tr>
 </table>
	<BR>
	<table cellspacing=4 cellpadding=4>
	  <tr>
	    <td class="int">Codice Posizione Materiale</td>
	    <td class="l"><font class="campo"><%=lPosizioneMateriale.getCodPosizioneMateriale() %></font></td>
	  </tr>
	  <tr>
	  <td class="int">Descrizione Posizione Materiale</td>
	    <td class="l"><font class="campo"><%=lPosizioneMateriale.getDescPosizioneMateriale() %></font></td>
	  </tr>
	  <tr>
	    <td class="int">Ufficio</td>
	    <td class="l"><font class="campo"><%=lPosizioneMateriale.getDescrUfficio()%></font></td>
	  </tr>
<%
		if( lPosizioneMateriale.getDataFineValidita() != null )
		{
%>
		  <tr>
		    <td class="int">Data Fine Validità</td>
		    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lPosizioneMateriale.getDataFineValidita(),"dd-MM-yyyy"))%></font></td>
		  </tr>
<%
		}
%>
  </table>
<%
	List lListaFascicoliSiep = fascicoliposizionemateriale.getFascicoliSiep();
	if(lListaFascicoliSiep != null && !lListaFascicoliSiep.isEmpty())
	{
%>	
		<br>
		<table width="60%" cellspacing=2 cellpadding=2>
	  	<tr><td class="Titolo" colspan=5>Fascicoli SIEP associati</td></tr>
			<tr>
				<td class="int">Numero SIEP</td>
      	<td class="int">Data di Iscrizione</td>					
	      <td class="int">Soggetto</td>
				<td class="int">Data Irrevocabilità</td>
				<td class="int">Dalla Data</td>
			</tr>
<%
	    Iterator itx = lListaFascicoliSiep.iterator();
	    while ( itx.hasNext())
	    {
	      FascicoloSiepModel lFasMod = (FascicoloSiepModel)itx.next();
%>
				<tr>
		      <td class="c"><font class="label"><%=lFasMod.getChiaveAnno()%>/<%=lFasMod.getChiaveProgr()%></font></td>
		      <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lFasMod.getDataIscrizione(),"dd-MM-yyyy"))%></font></td>
<%    
					if (lFasMod.getSoggetto() != null)
      		{ 
%>
        		<td class="c"><font class="label"><%=StringUtils.toStringJSP(lFasMod.getSoggetto().getCognome()) +" " + StringUtils.toStringJSP(lFasMod.getSoggetto().getNome())%></font></td>
<%    
					}
      		else
      		{ 
%>
						<td class="c">&nbsp;</td>
<%    
					} 	
%>
	      	<td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lFasMod.getDataIrrevocabilita(),"dd-MM-yyyy"))%></font></td>
					<td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lFasMod.getDataInizioPosizioneMateriale(),"dd-MM-yyyy"))%></font></td>
				</tr>
<%
			}
%>	
	  </table>
<%
	}
%>	
<%
	List lListaFascicoliSius = fascicoliposizionemateriale.getFascicoliSius();
	if(lListaFascicoliSius != null && !lListaFascicoliSius.isEmpty())
	{
%>	
		<br>
		<table width="90%" cellspacing=2 cellpadding=2>
	  	<tr><td class="Titolo" colspan=5>Fascicoli SIUS associati</td></tr>
			<tr>
				<td class="int">Numero SIUS</td>
				<td class="int">Data Emissione</td>
	      <td class="int">Data Udienza</td>
	      <td class="int">Contenuto</td>
				<td class="int">Dalla Data</td>
			</tr>
<%
	    Iterator itx = lListaFascicoliSius.iterator();
	    while ( itx.hasNext())
	    {
	      FascicoloGPModel lFasGPMod = (FascicoloGPModel)itx.next();
%>
				<tr>
<%     
					if ( lFasGPMod.getFascicoloSiusModel() != null )
       		{
%>
		      	<td class="c"><font class="label"><%=StringUtils.toStringJSP(lFasGPMod.getFascicoloSiusModel().getChiaveAnno())%>/<%=StringUtils.toStringJSP(lFasGPMod.getFascicoloSiusModel().getChiaveProgr())%></font></td>
<%
       		}
       		else
       		{
%>
						<td class="c"><font class="label">-&nbsp;</font></td>
<%
					}
%>
        	<td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lFasGPMod.getGeneraleProcedimentoModel().getDataRichiesta(),"dd-MM-yyyy"),"-")%></font></td>
<%     
					if ( lFasGPMod.getGeneraleProcedimentoModel() != null )
       		{
%>
          	<td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lFasGPMod.getGeneraleProcedimentoModel().getDataCameraConsiglio(),"dd-MM-yyyy"))%></font></td>
<%
       		}
       		else
       		{
%>
						<td class="c"><font class="label">-&nbsp;</font></td>
<%
					}
%>
					<td class="c"><font class="label"><%=StringUtils.toStringJSP(lFasGPMod.getGeneraleProcedimentoModel().getDescrOggettoProcedimento())%>&nbsp;</font></td>
					<td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lFasGPMod.getFascicoloSiusModel().getDataInizioPosizioneMateriale(),"dd-MM-yyyy"))%></font></td>
<%
			}
%>	
	  </table>
<%
	}
%>	
</body>
</html>