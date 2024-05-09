<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.verbale.model.VerbaleModel"%>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>


<html>

<jsp:useBean id="tipoAutorita" scope="request" class="java.lang.String"/>
<jsp:useBean id="FlagOmesse" scope="request" class="java.lang.String"/>
<jsp:useBean id="idEventoOIPP" scope="request" class="java.lang.String"/>


<head>
<title>[S.I.E.S.] - Gestione Verbale Vane Ricerche </title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
</script>
<script language="JavaScript">
function Verifica()
{
      if (document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value.length==1)
       document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value='0'+document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value;
      if (document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value.length==1)
       document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value='0'+document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value;
        var data_to_verify = document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>.value+'/'+document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>.value+'/'+document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>.value;
      if (! ControllaData(data_to_verify))
      {
        alert('Data non valida');
        return false;
      }
      if (document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
       document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
       document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value;
        var data_to_verify = document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciVerbaleVaneRicerche.<%=ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      if (! ControllaData(data_to_verify))
      {
        alert('Data non valida');
        return false;
      }
      return true;
 }

</SCRIPT>



</head>


		<body class="corpo">
			<table>
			<tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
			 <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
 <%
			 VerbaleModel lVerbale = new VerbaleModel();
       SoggettoModel lSoggetto = new SoggettoModel();

			 String lAzione = new String();
			   lAzione = "siap.siep.verbale.action.ActInserisciVerbaleVaneRicerche";
			%><font class="campo">Inserimento Verbale Vane Ricerche</font>

       </td>
       
      <% if (idEventoOIPP.length()>0) { %>
      <td class="LBG">
        <a href="/jsp/Main.jsp?Action=siap.siep.sanzionesostitutiva.action.ActLoadInserisciRinnovoRicercheOIPP&IdEvento=<%=idEventoOIPP%>">
          <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0"></a>      
      </td>  
      <% } %>
             
      </tr>
   </table>
   
    <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    
		<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciVerbaleVaneRicerche">
     <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.verbale.action.ActInserisciVerbaleVaneRicerche">
     <input type="HIDDEN" name="FlagOmesse" value="<%=FlagOmesse %>">
     <input type="HIDDEN" name="idEventoOIPP" value="<%=idEventoOIPP %>">

		 <table cellspacing=2 cellpadding=2>


		<tr>
				<td class="l">Data pervenimento del verbale</td>
				<td class="l">
        <input title="Giorno Pervenimento" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
				-
        <input title="Mese Pervenimento" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
				-
        <input title="Anno Pervenimento" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
		</tr>

    <tr>
       <td class="l" >Data verbale</td>
        <td class="l">

          <input title="Giorno Verbale" size=2 maxlength=2 value="<%=DateUtils.getSysDate("dd")%>" type="text" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
         -
          <input title="Mese Verbale" size=2 maxlength=2 value="<%=DateUtils.getSysDate("MM")%>" type="text" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          -
          <input title="Anno Verbale" size=4 maxlength=4 value="<%=DateUtils.getSysDate("yyyy")%>" type="text" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
      </tr>


      <tr>
        <td class="l">Autorità che ha redatto il verbale</td>
        <td class="l">
          <select title="TipoAutorita" name="<%=ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO%>">
            <%=tipoAutorita%>
          </select>
        </td>
      </tr>

        <tr>
            <td class="l">Luogo</td>
            <td class="L">
            <input title="Luogo" value="<%=StringUtils.toStringJSP(lSoggetto.getDescrComuneNascita()) %>" type="text" name="<%=  ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO%>"  maxlength="35" size="35">
            <a href="Javascript:ListaComuni('LoadInserisciVerbaleVaneRicerche','<%= ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO %>');">
            <img src="/images/filefolder.gif" border=0>
            </a>
            </td>
	           <td class="l">Indirizzo</td>
             <td class="L">
             <TEXTAREA title="Indirizzo" name="<%= ICostantiVerbale.CAMPO_NOTE %>" cols=30><%=StringUtils.toStringJSP(lVerbale.getNote(),"") %></textarea>
             </td>            
         </tr>

         <tr>
	           <td class="l">N.Protocollo vane ricerche</td>
             <td class="L">
            <input type="text" title="Note" name="<%= ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO %>" value="<%=lVerbale.getNumeroProtocollo()%>"  maxlength="35" size="35"  >
             </td>
           </tr>


          <td>&nbsp;</td>
          <tr>
           <td>
            <INPUT  class="bottone" type="submit" name="INSERISCI" value="Conferma">
           </td>
          </tr>

</table>
		</form>
 <script language="JavaScript" type="text/javascript">
 var frmvalidator  = new Validator("LoadInserisciVerbaleVaneRicerche");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO%>","lt=2099");

  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO %>","alphabetic");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");


  frmvalidator.setAddnlValidationFunction("Verifica");

 </script>
	</body>
</html>