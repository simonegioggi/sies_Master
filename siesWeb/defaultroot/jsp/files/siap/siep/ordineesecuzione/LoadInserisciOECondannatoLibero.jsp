<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>

<jsp:useBean id="evento"          scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="modalita"        scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsterna" scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"      scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>


<html>
  <head>
    <title>[S.I.E.S.] - Gestione Ordine Esecuzione </title>
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
		  if (document.LoadInserisciOECondannatoLibero.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  document.LoadInserisciOECondannatoLibero.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOECondannatoLibero.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  if (document.LoadInserisciOECondannatoLibero.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  document.LoadInserisciOECondannatoLibero.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciOECondannatoLibero.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  var data_to_verify = document.LoadInserisciOECondannatoLibero.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOECondannatoLibero.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciOECondannatoLibero.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
		  {
       alert('Data di emissione non valida');
			 return false;
		  }

      if (document.LoadInserisciOECondannatoLibero.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[0].value.length == 1)
			{
       alert('Il Primo campo Destinatario è obbligatorio');
			 return false;
		  }

	  }

</script>

  </head>

  <body class="corpo" >



  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;

      <%
			  EventoModel lProvvedimento = new EventoModel();

        String lAzione = new String();

        if( modalita.equals("I") )
			  {
          lProvvedimento = new EventoModel(evento);
          lAzione = "siap.siep.ordineesecuzione.action.ActInserisciOELibero";
      %>
		<font class="campo">Inserimento Ordine di Esecuzione Condannato Libero</font>
      <%}
        else if( modalita.equals("M") )
        {
          lProvvedimento = new EventoModel(evento);
			    lAzione = "siap.siep.ordineesecuzione.action.ActModificaEvento";
      %>  <font class="campo">Modifica di Ordine di Esecuzione Condannato Libero</font>
      <%}
      %>
      </td>
    </tr>
  </table>
<br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" name="LoadInserisciOECondannatoLibero" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordineesecuzione.action.ActInserisciOELibero">
    <table>
      <tr>
        <td class="l">Data Emissione</td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
      <tr>
        <td class="l">Magistrato</td>
        <td class="L">
         <input title="Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getCognome() +" "+magistrato.getNome() )%>" type="text" name="<%= ICostantiEvento.CAMPO_MAGISTRATO %>"  maxlength="35" size="35">
      </td>
      <td class="L">
      <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getCodMagistrato() )%>" type="text" name="<%= ICostantiEvento.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35">
      </td>
      </tr>

<tr><td class="Titolo" colspan=6>Autorità Destinazione per Esecuzione </td></tr>
     <!-- Visualizzare Magistrato Competente -->
      <tr>
          <td class="l">Autorità Destinazione </td >
           <td class="L">
             <select Title="Autorita Esterna" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
               <%=autoritaEsterna%>
             </select>
       </td>
		   <td rowspan=2 class="l">Note</td>
       <td rowspan=2 class="L">
          <TEXTAREA title="Note" name="<%= ICostantiNotifica.CAMPO_NOTE %>"  cols=40 rows=5 ></textarea>
       </td>
		  </tr>
      <tr><td class="l">Sede </td><td class="L">
             <input title="Sede Autorita Esterna" value="<%=StringUtils.toStringJSP(lProvvedimento.getDescrUfficioDestinatario()) %>" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>"  maxlength="35" size="35">
             <a href="Javascript:ListaComuni('LoadInserisciOECondannatoLibero','<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[0]');">
             <img src="/images/filefolder.gif" border=0></a></td>
      </tr>
<tr><td class="Titolo" colspan=6>Destinatario per Notifica </td></tr>
      <tr>
          <td class="l">Destinatario </td >
           <td class="L">
             <select Title="Autorita Esterna" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
               <%=autoritaEsterna%>
             </select>
         </td>
	   <td rowspan=2 class="l">Note</td>
     <td rowspan=2 class="L">
          <TEXTAREA title="Note" name="<%= ICostantiNotifica.CAMPO_NOTE %>"  cols=40 rows=5 ></textarea>
     </td>
  </tr>

   <tr><td class="l">Sede </td><td class="L">
       <input title="Sede Autorita Esterna" value="<%=StringUtils.toStringJSP(lProvvedimento.getDescrUfficioDestinatario()) %>" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>"  maxlength="35" size="35">
       <a href="Javascript:ListaComuni('LoadInserisciOECondannatoLibero','<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[1]');">
       <img src="/images/filefolder.gif" border=0></a></td>
   </tr>
   <tr>
       <td class="lNoBord" colspan="2">
       <br><br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
       </td>
   </tr>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciOECondannatoLibero");
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
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=3000");

 </script>


  </table>
	</form>
	</body>
</html>