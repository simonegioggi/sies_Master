<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.sanzionesostitutiva.action.ICostantiSanzioneSostitutiva"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"%>
<%@ page import="siap.siep.sanzionesostitutiva.model.SanzioneSostitutivaModel"%>

<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.decodifiche.controller.IDecodifiche"%>
<%@ page import="siap.sico.util.SICOLookupRemote"%>

<jsp:useBean id="evento" scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="AnnotazioneMan" scope="request" class="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel"/>
<jsp:useBean id="strDescrUfficio"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="strDescrComune"     			scope="request" class="java.lang.String"/>

<%
DecodificheModel lModel = new DecodificheModel();
IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
lModel.setContesto("MOTIVO_PROVVEDIMENTO");
lModel.setCodiceAlternativo("REVOCA");
Collection lColMotivo = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);
Iterator itxOggetto = lColMotivo.iterator();
String arti="";
String moti="";
while(itxOggetto.hasNext())
{
   DecodificheModel lDecMod = (DecodificheModel)itxOggetto.next();
   if(lDecMod.getCode().equals(evento.getEvento().getCodMotivo())){
	   arti= lDecMod.getFiltro();
	   moti=lDecMod.getDescription();
	   break;
   }
}



%>

<html>
<head>
<title>[S.I.E.S.] - Gestione evento </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>"> 
  <script language="JavaScript">
  var desktop;  
  	function ListaDocumentiSius(a_formname)
    {
    }
    
  </script> 
</head>

<body class="corpo">
	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="DettaglioAnnotazioneRevocaSosp">
	<table>
	    <tr>
		    <td class="LBG">
		    	<a href="Javascript:window.print();">
		    		<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
		    	</a>
		    </td>
		    <td class="LBG">
		    	<font class="label">Funzione :</font>&nbsp;&nbsp;
		    	<font class="campo">Dettaglio Annotazione Revoca Beneficio ex art.168 c.p. - 674 c.p.p.</font>
			</td>
		</tr>
	</table>
	<br>
		<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
	<br>    
	
	<table width="80%"  cellspacing=6 cellpadding=6>
	    <tr><td class="Titolo" colspan="2">Estremi del Provvedimento</td></tr>
    <tr>		
		<td class="l" width="30%">Data Arrivo Atto</td>
		<td class="l">
			<font class="campo">
				<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getEvento().getDataEmissione(),"dd-MM-yyyy"))%>
			&nbsp;</font>
		</td>
	</tr>
	<%
	String provvedimento="Ordinanza";
	if (AnnotazioneMan.getAnnoSentenzaSiap()!=null && AnnotazioneMan.getNumeroSentenzaSiap()!=null)
		provvedimento="Sentenza";	
	%>
    <tr>
    	<td class="l" width="30%">Tipo Provvedimento</td>
    	<td class="l">
			<font class="campo">
    			<%=provvedimento %>   
    		&nbsp;</font> 		
    	</td>
    </tr>
	<%if (provvedimento.equals("Sentenza")) {
	%>
     <tr> 
      <td class="l" width="30%"> Data Provvedimento</td>
      <td class="l">
			<font class="campo">
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(AnnotazioneMan.getDataSentenzaSiap(),"dd-MM-yyyy"))%> 
        	&nbsp;</font>
      </td>
    </tr>
     <tr> 
      <td class="l" width="30%"> Anno/Numero Provvedimento</td>
      <td class="l">
			<font class="campo">
        		<%=StringUtils.toStringJSP(AnnotazioneMan.getAnnoSentenzaSiap())%>
        		/
        		<%=StringUtils.toStringJSP(AnnotazioneMan.getNumeroSentenzaSiap())%>
        	&nbsp;</font>
      </td>
    </tr>
     <tr> 
      <td class="l" width="30%"> Anno/Numero R.G.N.R.</td>
      <td class="l">
			<font class="campo">
        		<%=StringUtils.toStringJSP(AnnotazioneMan.getAnnoGe())%>
        		/
        		<%=StringUtils.toStringJSP(AnnotazioneMan.getNumeroGe())%>
        	&nbsp;</font>
      </td>
    </tr>
     <tr> 
      <td class="l" width="30%"> Anno/Numero Reg.Gen.</td>
      <td class="l">
			<font class="campo">
        		<%=StringUtils.toStringJSP(AnnotazioneMan.getAnnoRege())%>
        		/
        		<%=StringUtils.toStringJSP(AnnotazioneMan.getNumeroRege())%>
        	&nbsp;</font>
      </td>
    </tr>
	<%	} else {%>
     <tr> 
      <td class="l" width="30%"> Data Provvedimento</td>
      <td class="l">
			<font class="campo">
			<%=StringUtils.toStringJSP(DateUtils.getDateToString(AnnotazioneMan.getDataGE(),"dd-MM-yyyy"))%> 
        	&nbsp;</font>
      </td>
    </tr>
     <tr> 
      <td class="l" width="30%"> Anno/Numero Provvedimento</td>
      <td class="l">
			<font class="campo">
        		<%=StringUtils.toStringJSP(AnnotazioneMan.getAnnoGe())%>
        		/
        		<%=StringUtils.toStringJSP(AnnotazioneMan.getNumeroGe())%>
        	&nbsp;</font>
      </td>
    </tr>
     <tr> 
      <td class="l" width="30%"> Articolo</td>
      <td class="l">
			<font class="campo">
				<%=arti%>
        	&nbsp;</font>
      </td>
    </tr>
     <tr> 
      <td class="l" width="30%"> Motivazione</td>
      <td class="l">
			<font class="campo">
				<%=moti%>
        	&nbsp;</font>
      </td>
    </tr>
	<%} %>
    <tr>
      <td class="l" width="30%">Ufficio Emittente</td>
      <td class="l">
			<font class="campo">
	          <%=strDescrUfficio%>
				&nbsp;di&nbsp;
	          <%=strDescrComune%>
	        	&nbsp;
	        </font>
      </td>
    </tr>
	<%
	String ck1="disabled";
	String ck2="disabled";
	if ((evento.getEvento().getAnnIdAnnotazioneManuale())!=null) {
		//si tratta di una sentenza
		if (AnnotazioneMan.getMotivazioni()!=null){
			if (AnnotazioneMan.getMotivazioni().contains("Sospensione Condizionale"))
				ck1="checked disabled";
			if (AnnotazioneMan.getMotivazioni().contains("Non menzione"))
				ck2="checked disabled";
		}
	}
	%>

     <tr>
       <td class="l" width="30%">Sospensione Condizionale </td>
       <td class="l"><input type="radio" value="1" <%=ck1%> ></td>
       <td>&nbsp;</td>
     </tr>
    <tr>
       <td class="l" width="30%">Non Menzione </td>
       <td class="l"><input type="radio" value="2" <%=ck2%> ></td>
     </tr>
     <% if (provvedimento.equals("Ordinanza")) {%>
    <tr>
      <td  class="l" width="30%">Note</td>
      <td class="l">
			<font class="campo">
	        	
	        &nbsp;</font>
      </td>
    </tr>
    <%} %>
    

</table>

	
</FORM>
</body>
</html>