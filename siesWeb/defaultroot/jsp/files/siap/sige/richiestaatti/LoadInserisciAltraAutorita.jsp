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
<%@ page import="siap.sige.richiestaatti.action.ICostantiRichiestaAtti"%>

<jsp:useBean id="modalita"    scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita"    scope="request" class="java.lang.String"/>
<jsp:useBean id="autorita1"   scope="request" class="java.lang.String"/>
<jsp:useBean id="evento"      scope="request" class="siap.sico.evento.model.EventoModel"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Richiesta Altra Autorita</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">

    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
     desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    // Chiamata lista Comuni con filtro sulla Provincia dell'ufficio connesso.
    function ListaComuniRicercaUfficio(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }
    function Verify()
	  {
	  // Controlla che le coppie di campi Destinatario/Sede siano riempiti
   	if (document.LoadRichiestaAltraAutorita.<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO%>[0].value != '-'
        && document.LoadRichiestaAltraAutorita.<%=ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO%>[0].value == '')
    {
    	alert('La Sede del destinatario n°1 è obbligatoria');
      return false;
    }
    
    if (document.LoadRichiestaAltraAutorita.<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO%>[1].value != '-'
        && document.LoadRichiestaAltraAutorita.<%=ICostantiIstruttoria.SEDE_AUTORITA_DESTINATARIO%>[1].value == '')
    {
    	alert('La Sede del destinatario n°2 è obbligatoria');
      return false;
    }
    // Controlla che non siano inseriti entrambi i destinatari
<%--     //if (document.LoadRichiestaAltraAutorita.<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO%>[0].value !== '-' --%>
<%--     //    && document.LoadRichiestaAltraAutorita.<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO%>[1].value !== '-') --%>
    //{
   // 	alert('Inserire solo un Destinatario con la relativa Sede.');
   //   return false;
   // }

    // Controlla che almeno un destinatario sia inserito
    if (document.LoadRichiestaAltraAutorita.<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO%>[0].value == '-'
        && document.LoadRichiestaAltraAutorita.<%=ICostantiIstruttoria.AUTORITA_DESTINATARIO%>[1].value == '-')
    {
    	alert('Inserire almeno un Destinatario con la relativa Sede.');
      return false;
    }
	  	  	
		  if (document.LoadRichiestaAltraAutorita.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  document.LoadRichiestaAltraAutorita.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadRichiestaAltraAutorita.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  if (document.LoadRichiestaAltraAutorita.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  document.LoadRichiestaAltraAutorita.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadRichiestaAltraAutorita.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  var data_to_verify = document.LoadRichiestaAltraAutorita.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadRichiestaAltraAutorita.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadRichiestaAltraAutorita.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

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
      <font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Richiesta ad Altra Autorita</font>

      <%
		EventoModel lProvvedimento = new EventoModel();

        String lAzione = new String();

        if( modalita.equals("I") )
         {
          lProvvedimento = new EventoModel(evento);
          lAzione = "siap.sige.richiestaatti.action.ActInserisciRichiestaAltraAutorita";
      %>
          <font class="campo">Inserisci Richiesta Ad Altra Autorita</font>
      <%}
        else if( modalita.equals("M") )
        {
          lProvvedimento = new EventoModel(evento);
          lAzione = "siap.sige.richiestaatti.action.ActInserisciRichiestaAltraAutorita";
      %>  <font class="campo">Modifica Richiesta Ad Altra Autorita</font>
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
  <FORM method="POST" name="LoadRichiestaAltraAutorita" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.richiestaatti.action.ActInserisciRichiestaAltraAutorita">
    <table>

	<tr>
        <td class="l">Data Emissione <font class="ob">(*)</font></td >
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
		  
		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadRichiestaAltraAutorita','<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>','<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>');">
       		  <img src="/images/calendario.gif" border=0>
          </a>
        </td>
   </tr>
<!-- Start Destinatario 4 -->
        <tr>
          <td class="l">Destinatario</td>
          <td class="L" colspan=3>
          <table>
          <tr>
            <td class="l">Tipo</td>

            <input type="hidden"  Title="Tipo" name="tipoDest" value="AUT_EXT" size=50>

            <td class="l" >
             <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
              <%= autorita1 %>
             </select>
            </td>
          </tr>

          <tr>
            <td class="l">Sede <font class=ob>(*)</font></td>
            <td class="l">
            <input Title="Sede" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
                value="" type="text" maxlength="35" size="35">
                  <a href="Javascript:ListaComuniRicercaUfficio('LoadRichiestaAltraAutorita','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[0]');">            
                <img src="/images/filefolder.gif" border=0> </a>
            </td>
          </tr>

          <tr>
            <td class="l">Indirizzo</td>
            <td class="L" colspan=3>
             <input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
            </td>
          </tr>
          </table>
        </tr>
        <!-- End Destinatario  4 -->
        <!-- Start Destinatario 5 -->
        <tr>
          <td class="l">Destinatario </td>
          <td class="L" colspan=3>
          <table>

          <tr>
            <td class="l">Tipo</td>

            <input type="hidden"  Title="Tipo" name="tipoDest" value="AUT_EXT" size=50>

            <td class="l" >
              <select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
               <%= autorita %>
              </select>
            </td>
          </tr>

          <tr>
	          <td class="l">Sede <font class=ob>(*)</font></td>
	          <td class="l">
	            <input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
	                value="" type="text" maxlength="35" size="35">
	                <a href="Javascript:ListaComuniRicercaUfficio('LoadRichiestaAltraAutorita','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[1]');">
	                <img src="/images/filefolder.gif" border=0> </a>
	          </td>
          </tr>

          <tr>
            <td class="l">Indirizzo</td>
            <td class="L" colspan=3>
             <input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
            </td>
          </tr>
          </table>
        </tr>
        <!-- End Destinatario 5 -->
 		<!-- Campo Note + campo hidden -->
        <tr>
            <td class="l">Note 1</td>
            <td class="L" colspan=3>
             <input title="Note" name="<%= ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO %>" type="text" maxlength="80" size="80">
            </td>
        </tr>

        <!-- Campo Note + campo hidden -->
        <tr>
            <td class="l">Note 2</td>
            <td class="L" colspan=3>
             <TEXTAREA title="Note" name="<%= ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO %>"  cols=80 rows=4 ></textarea>
            </td>
        </tr>
      
    

    <tr>
       <td class="lNoBord" colspan="2">
       <br><br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
       </td>
   </tr>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadRichiestaAltraAutorita");

  
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
 </script>

</table>
	</form>

	</body>
</html>