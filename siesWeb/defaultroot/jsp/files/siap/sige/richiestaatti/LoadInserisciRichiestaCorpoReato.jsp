<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.istruttoria.action.ICostantiIstruttoria"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>

<jsp:useBean id="modalita"    scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita"    scope="request" class="java.lang.String"/>
<jsp:useBean id="procura"     scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"      scope="request" class="siap.sico.evento.model.EventoModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Richiesta Corpo Reato </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">

    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    function Verify()
	  {
	  // Controlla che le coppie di campi Destinatario/Sede siano riempiti
   	if (document.LoadRichiestaCorpoReato.<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO%>[0].value != '-'
        && document.LoadRichiestaCorpoReato.<%=ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO%>[0].value == '')
    {
    	alert('Sede Autorità Giudicante obbligatoria');
      return false;
    }
    
      	  	
		  if (document.LoadRichiestaCorpoReato.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  document.LoadRichiestaCorpoReato.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadRichiestaCorpoReato.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  if (document.LoadRichiestaCorpoReato.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  document.LoadRichiestaCorpoReato.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadRichiestaCorpoReato.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  var data_to_verify = document.LoadRichiestaCorpoReato.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadRichiestaCorpoReato.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadRichiestaCorpoReato.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
		  {
       alert('Data di emissione non valida');
			 return false;
		  }
	  }

    function calendario(a_formname,a_field_year,a_field_month,a_field_day)
    {
      desktop = 
          window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }
    </script>


  </head>

 <body class="corpo" >
   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Richiesta Corpo Reato</font>

      <%
		EventoModel lProvvedimento = new EventoModel();

        String lAzione = new String();

        if( modalita.equals("I") )
         {
          lProvvedimento = new EventoModel(evento);
          lAzione = "siap.sige.richiestaatti.action.ActInserisciRichiestaCorpoReato";
      %>
          <font class="campo">Inserisci Richiesta Corpo Reato</font>
      <%}
        else if( modalita.equals("M") )
        {
          lProvvedimento = new EventoModel(evento);
          lAzione = "siap.sige.richiestaatti.action.ActInserisciRichiestaCorpoReato";
      %>  <font class="campo">Modifica Richiesta Corpo Reato</font>
      <%}
      %>
      </td>
      <!-- BOTTONE DI RITORNO -->
      <td class="LBG">
        <a href="javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
<br>
    <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
  <br>
  <FORM method="POST" name="LoadRichiestaCorpoReato" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.richiestaatti.action.ActInserisciRichiestaCorpoReato">
    <table>

	<tr>
        <td class="l">Data Emissione <font class="ob">(*)</font></td >
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >

		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadRichiestaCorpoReato','<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>');">
       		  <img src="/images/calendario.gif" border=0>
          </a>
        </td>
   </tr>
   
    <tr>
          <td class="l">Autorità Giudicante <font class="ob">(*)</font></td >
           <td class="L">
             <select Title="Autorita " name="<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO%>" >
               <%=autorita%>
             </select>
         </td>
	</tr>

	<tr><td class="l">Sede <font class="ob">(*)</font></td><td class="L">
       <input title="Sede Autorita Giudicante" value="" type="text" name="<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>"  maxlength="35" size="35">
       <a href="Javascript:ListaComuni('LoadRichiestaCorpoReato','<%= ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO %>');">
       <img src="/images/filefolder.gif" border=0></a></td>
    </tr>
    <tr>
        <td class="l">Ufficio <font class="ob"></font></td>
        <td class="L">
        	Ufficio Corpi di Reato
        	<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        	<%--
            <input value="" type="text" size="35" maxlength="40" name="<%= ICostantiIstruttoria.TIPO_DOCUMENTO %>">
    		--%>    
        </td>
    </tr>
	<!-- Campo Note  -->
	<tr>
           <td class="l">Note </td>
           <td class="L" colspan=3>
            <TEXTAREA title="Note" name="<%= ICostantiIstruttoria.CAMPO_NOTE %>"  cols=80 rows=4 ></textarea>
           </td>
       </tr>

    <tr>
       <td class="lNoBord" colspan="2">
       <br><br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
       </td>
   </tr>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadRichiestaCorpoReato");

  
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2999");
  
  frmvalidator.addValidation("<%=ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO%>", "req","Il campo Sede è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO%>","maxlen=35","La lunghezza massima per la Sede è di 35 caratteri");
  frmvalidator.addValidation("<%=ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO%>","alpha");
  
 </script>

</table>
	</form>

	</body>
</html>