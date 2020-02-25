<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.nuovaistanza.model.NuovaIstanzaModel"%>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>

<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>

<jsp:useBean id="listaIstanze"  scope="request" class="java.util.Vector"/>
<jsp:useBean id="listaeventi"  scope="request" class="java.util.Vector"/>
<jsp:useBean id="statoIstanza"  scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioGE"   	scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioMdS"   	scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioTdS"   	scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioPM"		scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"   	scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>

<jsp:useBean id="avvocati"     	scope="request" class="java.util.Vector"/>

<jsp:useBean id="codiceAutoritaE"     scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaE"   scope="request" class="java.lang.String"/>

<jsp:useBean id="autoritaEsternaAvv"     scope="request" class="java.lang.String"/>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Disposizioni del PM</title>   
 <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript">
	var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
       	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

	function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
   	{
     	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&TipoUfficio="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
   	}

    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
    {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }

  	function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
  	{
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
  	}

   	function ListaFunzionari(a_formname, a_field2,a_field3)
  	{
    	var desktop;
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.utente.action.ActLoadRicercaUtenteAttivo&formname="+a_formname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Funzionario", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
  	}

  	function ListaUDS(a_formname,a_fieldname)
  	{
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  	}

  	function ListaComuniTds(formname,fieldname)
  	{
    	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  	}

</script>
</head>
<body>
<body class="corpo">
   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Disposizioni del Pubblico Ministero</font>
      </td>
          <td class="LBG">
          <a href="javascript:history.back()">
            <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
    </tr>
  </table>
 
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <!-- ******************FORM****************** -->
  
<form name="InserisciDisposizioniPM"  method="POST" action="<%=IWebConstants.PG_MAIN%>" onSubmit="riempiCodIstituto()">
   <input type="hidden"  name="Action" value="siap.siep.nuovaistanza.action.ActInserisciDisposizioniPM" />
  <jsp:include page="/jsp/files/siap/siep/nuovaistanza/IncludeListaIstanzeDisposizioni.jsp"></jsp:include> 

<%
String lDisplayDatiInoltro = "display:none";
if(listaIstanze.size()==1 && listaeventi.size()==0)
{
	lDisplayDatiInoltro = "display:block";
 }
%>
<br><br>
	<div style="<%=lDisplayDatiInoltro%>" id="datiInoltro" >

  <table>
   	<tr> <td>&nbsp;</td> <tr>

		<tr>
			<td class="Titolo" colspan=3>Ulteriori Dati dell'istanza </td></tr>
		<tr>
			<td class="l" width="15%">Data Restituzione Istanza</td>
			<td class="L">    
				<input value="<%=DateUtils.getSysDate("dd")%>" name="<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DISPOSIZIONE%>" type="text" size="2" maxlength="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
				<input value="<%=DateUtils.getSysDate("MM")%>" name="<%=ICostantiNuovaIstanza.CAMPO_MESE_DISPOSIZIONE%>"  type="text" size="2" maxlength="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > /
				<input value="<%=DateUtils.getSysDate("yyyy")%>" name="<%=ICostantiNuovaIstanza.CAMPO_ANNO_DISPOSIZIONE%>" type="text" size="4" maxlength="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
			</td> 
   	</tr>
   	
    <tr>
			<td class="l">Oggetto <br> Decisione <font class=ob>(*)</font></td>
			<td>
        <select Title="Oggetto" name="<%=ICostantiNuovaIstanza.CAMPO_COD_STATO_ISTANZA%>" ><%=statoIstanza%>
        </select>
      </td>
   	</tr>
		<tr>
      <td class="l">Note</td>
			<td>
				<TEXTAREA title="Note" name="<%= ICostantiNuovaIstanza.CAMPO_NOTE %>"  cols=60 rows=3 ></textarea>
			</td>
		</tr>
   
		<br>
   
		<tr>
   		<td class="l">Firmatario</td>
   		<script language="javascript">
			function visFirmatario(elementoV, elementoH)
			{
				document.getElementById(elementoV).style.display='block';
				document.getElementById(elementoH).style.display='none';
			}
   		</script>
   		
   		<td class="l">
	   		<input checked type="Radio" name="selFirm" value="mag" id="selFirm1" onclick="visFirmatario('nomeMag', 'nomeFunc')"> Magistrato
				<input type="Radio" name="selFirm" value="fun" id="selFirm2" onclick="visFirmatario('nomeFunc', 'nomeMag')"> Funzionario

				<div id="nomeMag" onclick="visFirmatario('nomeMag', 'nomeFunc')">
	 				<table style="border:0;padding:0">
						<tr>
							<td>
								<input type="HIDDEN" title="CodiceMagistratoNuovo" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() )%>" type="text" maxlength="35" size="35" >
								<input readonly title="Cognome Magistrato" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() )%>" type="text" maxlength="35" size="25">
								<input readonly title= "Nome Magistrato" name="<%=ICostantiMagistrato.CAMPO_NOME%>"	value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome() )%>" type="text" maxlength="35" size="25">
								<a href="Javascript:ListaMagistrati('InserisciDisposizioniPM','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
									<img src="/images/filefolder.gif" border=0>
								</a>
							</td>
						</tr>
					</table>
				</div>
				<div id="nomeFunc" style="display:none; position:relative;">
					<table  style="border:0;padding:0">
						<tr>
							<td>						
								<input readonly title="Cognome Funzionario" value="<%=StringUtils.toStringJSP(UtenteConnesso.getCognome().toUpperCase())%>" type="text" name="CognomeFunzionario" maxlength="35" size="25">
								<input readonly title= "Nome Funzionario" value="<%=StringUtils.toStringJSP(UtenteConnesso.getNome().toUpperCase())%>" type="text" name="NomeFunzionario" maxlength="35" size="25">
								<a href="Javascript:ListaFunzionari('InserisciDisposizioniPM','CognomeFunzionario','NomeFunzionario');"> 
									<img src="/images/filefolder.gif" border=0>
								</a>					
							</td>
						</tr>
					</table>
				</div>					
			</td>
  	</tr>
    <tr> </tr>
		<tr>
			<td class="Titolo" colspan=3>Destinatari </td></tr>
		<tr>
    		<td class="l">Ufficio Giudice dell'Esecuzione </td>
			<td class="L" colspan=2>
      			<select Title="Ufficio Giudice dell'Esecuzione" name="ufficioGE" ><%=ufficioGE%>
				</select>
						&nbsp;&nbsp;&nbsp;&nbsp;Sede
     		<input title="Sede Ufficio Giudice Esecuzione"  type="text" name="sedeGE"  maxlength="35" size="35">
     			<a href="Javascript:ListaUfficiPerTipo('InserisciDisposizioniPM','sedeGE',document.InserisciDisposizioniPM.ufficioGE[document.InserisciDisposizioniPM.ufficioGE.selectedIndex].value);">
       			<img src="/images/filefolder.gif" border="0">
       		</a>
   			</td>
		</tr>
		<tr>
      		<td class="l">In riferimento al Procedimento N°</td>
			<td class="L" colspan=2>
				<input Title="Anno procedimento" value="" type="text" name="ARG_ge" maxlength="4" size="4"> /
				<input Title="Numero procedimento" value="" type="text" name="NRG_ge" maxlength="6" size="6">
			</td>
		</tr>
     	<tr>
      		<td class="l">MdS</td>
			<td class="L" colspan=2>
        		<select Title="Magistrato Sorveglianza" name="ufficioMds" ><%=ufficioMdS%></select>
				&nbsp;&nbsp;&nbsp;&nbsp;Sede
        		<input title="Sede Ufficio Magistrato Sorveglianza"  type="text" name="sedeMds"  maxlength="35" size="35">
        		<a href="Javascript:ListaUfficiPerTipo('InserisciDisposizioniPM','sedeMds',
     			document.InserisciDisposizioniPM.ufficioMds[document.InserisciDisposizioniPM.ufficioMds.selectedIndex].value);">
          		<img src="/images/filefolder.gif" border="0"></a>
      		</td>
   		</tr>
		<tr>
      		<td class="l">In riferimento al Procedimento N°</td>
			<td class="L" colspan=2>
				<input Title="Anno" value="" type="text" name="ARG_mds" maxlength="4" size="4"> /
				<input Title="Numero" value="" type="text" name="NRG_mds" maxlength="6" size="6">
			</td>
		</tr>
		<tr>
      <td class="l">TdS</td>
			<td class="L" colspan=2>
        <select Title="Tribunale Sorveglianza" name="ufficioTds" ><%=ufficioTdS%>
        </select>
						&nbsp;&nbsp;&nbsp;&nbsp;Sede
        <input title="Sede Ufficio Tribunale Sorveglianza"  type="text" name="sedeTds"  maxlength="35" size="35">
        <a href="Javascript:ListaUfficiPerTipo('InserisciDisposizioniPM','sedeTds',
     			document.InserisciDisposizioniPM.ufficioTds[document.InserisciDisposizioniPM.ufficioTds.selectedIndex].value);">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
   </tr>
	<tr>
      <td class="l">In riferimento al Procedimento N°</td>
		<td class="L">
			<input Title="Anno" value="" type="text" name="ARG_tds" maxlength="4" size="4"> /
			<input Title="Numero" value="" type="text" name="NRG_tds" maxlength="6" size="6">
		</td>
	</tr>

     <tr>
      <td class="l">Ufficio Pubblico Ministero</td>
		<td class="L" colspan=2>
        <select Title="Ufficio PM" class="small" name="ufficioPM" >
        	<%=ufficioPM%>
        </select>
						<br>&nbsp;&nbsp;&nbsp;&nbsp;Sede
        <input title="Sede Ufficio PM"  type="text" name="sedePM"  maxlength="35" size="35">
        <a href="Javascript:ListaUfficiPerTipo('InserisciDisposizioniPM','sedePM',
     			document.InserisciDisposizioniPM.ufficioPM[document.InserisciDisposizioniPM.ufficioPM.selectedIndex].value);">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
	</tr>
	<tr>
      <td class="l" >In riferimento al Procedimento N°</td>
		<td class="L">
		<%-- MEV_66: escluso controllo js onkeypress="return TicTabNumField(this, event) e modificato maxlength e size (13 e 15) --%>
			<input Title="Anno" value="" type="text" name="ARG_pm" maxlength="4" size="4"> /
			<input Title="Numero" value="" type="text" name="NRG_pm" maxlength="6" size="6">
		</td>
	</tr>

   	<tr>
    	<td class="l">Istituto Detenzione</td>
<%
			if(posizioneluogoaltra != null &&  
				 posizioneluogoaltra.getLuogoDetenzione()!= null && 
				 posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione() != null)
   		{%>
       <td class="l">
              <input readonly Title="Istituto" name="Comune" 
              	value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
              <input type="hidden"  Title="Istituto" name="IstitutoDet" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('InserisciDisposizioniPM',
              'IstitutoDet','Comune');">
              <img src="/images/filefolder.gif" border=0></a></td>
  <%}else{%>
              <td class="l">
              <input readonly Title="Istituto" name="Comune" value="" size=50>
              <input type="hidden"  Title="Istituto" name="IstitutoDet" value="" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('InserisciDisposizioniPM',
              'IstitutoDet','Comune');">
              <img src="/images/filefolder.gif" border=0></a></td>

   <%}%>
	</tr>
</table>
<br>
<table width="100%">
	<tr><td class="Titolo" colspan="4">Notifica al Condannato </td></tr>
    <tr>
		<td class="l">Autorità <br>Destinazione <font class=ob>(*)</font></td>
        <td class="L" colspan="3">
            <select  Title="Autorita Esterna"  class="small" name="AutE_condannato">
             <%=codiceAutoritaE%>
             </select>
      </td>
    </tr>
	<tr>
      	<td class="l">Sede </td>
      	<td class="L"> 
      		<input title="Sede Autorita Esterna" value="" type="text" name="sedeAutE_Condannato"  maxlength="35" size="35">
        		<a href="Javascript:ListaComuni('InserisciDisposizioniPM','sedeAutE_Condannato');"> 
          		<img src="/images/filefolder.gif" border=0>
          	</a>  
      	</td>
        <td class="l">Indirizzo</td>
        <td class="L">
              <TEXTAREA title="Note" name="IndAutE_Condannato"  cols=50 rows=2 ></textarea>
        </td>
    </tr>
</table>
<br>
<table width="100%">
<%
  int lIdxAvv = 1;
  Iterator lItxAvv = avvocati.iterator();
  if(lItxAvv.hasNext())
  {%>
		<tr><td>&nbsp;</td></tr>
    <tr><td class="Titolo" colspan="4">Notifica al Difensore </td></tr>
<%
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv = (AvvocatoSiepModel)lItxAvv.next();
%>
          <tr>
            <td class="l" colspan="4">Per Avvocato&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
              </font>
              &nbsp;Foro di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>
              </font>
              &nbsp;Difensore di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
              </font>
            </td>
            <input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="CodAvvocato<%=lIdxAvv%>"  maxlength="35" size="35">
          </tr>
    			<tr>
						<td class="l">Autorità Destinazione </td>
        		<td class="L" colspan="3">
            	<select  Title="Autorita Esterna"  class="small" name="AutE_<%=lIdxAvv%>">
             		<%=autoritaEsternaAvv%>
             	</select>
        		</td>
    			</tr>
					<tr>
      			<td class="l">Sede </td>
      			<td> <input title="Sede Autorita Esterna" value="" type="text" name="sedeAutE_<%=lIdxAvv%>"  maxlength="35" size="35">
        			<a href="Javascript:ListaComuni('InserisciDisposizioniPM','sedeAutE_<%=lIdxAvv%>');">
          			<img src="/images/filefolder.gif" border=0>
          		</a>
      			</td>
        		<td class="l">Note</td>
        		<td class="L">
              <TEXTAREA title="Note" name="IndAutE_<%=lIdxAvv%>"  cols=50 rows=2 ></textarea>
        		</td>
    			</tr>

    			<tr><td>&nbsp;</td>
<%
    	lIdxAvv++;
  	}
  }
%>
		<tr><td class="Titolo" colspan="4">Destinatario per Notifica </td></tr>
    <tr>
			<td class="l">Altro Destinatario </td>
      <td class="L" colspan="3">
      	<select  Title="Autorita Esterna"  class="small" name="AutE_altro">
        	<%=autoritaEsternaE%>
        </select>
      </td>
    </tr>
    
		<tr>
			<td class="l">Sede </td>
			<td class="L">
				<input title="Sede Autorita Esterna" value="" type="text" name="sedeAutE_altro"  maxlength="35" size="35">
				<a href="Javascript:ListaComuni('InserisciDisposizioniPM','sedeAutE_altro');">
         	<img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
      	<TEXTAREA title="Note" name="IndAutE_altro"  cols=50 rows=2 ></textarea>
      </td>
    </tr>
	</table>
	<table>
		<tr>
    	<td class="lNoBord" colspan="2">
       	<br><br>
        <input  value="Conferma" type="submit"> 
       </td>
		</tr>
	</table>  

</div>

<%
int da_compilare=0;
Iterator<NuovaIstanzaModel> lItx = listaIstanze.iterator() ;
while (lItx.hasNext()){  
   NuovaIstanzaModel lModel =  (NuovaIstanzaModel)lItx.next(); 
   if (lModel.getDescrStatoIstanza().equals("Da compilare e da Validare"))
	   da_compilare++;
}
if (da_compilare>0 && lDisplayDatiInoltro.equals("display:block")){
%>
<%
}
%> 
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("InserisciDisposizioniPM");

  frmvalidator.addValidation("<%=ICostantiNuovaIstanza.CAMPO_COD_STATO_ISTANZA%>","req","Il campo Oggetto Decisione è obbligatorio");
  frmvalidator.addValidation("AutE_condannato","req","Il campo Autorità Destinazione è obbligatorio");

  

 </script>
</body>
</html>