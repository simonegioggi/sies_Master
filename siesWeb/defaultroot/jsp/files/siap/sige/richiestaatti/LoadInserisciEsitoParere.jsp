<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sius.produzioneatti.action.ICostantiProduzioneAtti"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>

<jsp:useBean id="evento"              scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="dataInsFS"           scope="request" class="java.lang.String"/>
<jsp:useBean id="codTipoUfficioS"     scope="request" class="java.lang.String"/>
<jsp:useBean id="descTipoUfficioS"    scope="request" class="java.lang.String"/>
<jsp:useBean id="codMotivo"           scope="request" class="java.lang.String"/>
<jsp:useBean id="ProvvedimentoEvento" scope="request" class="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"/>

<%
  String lAzione = "siap.sige.richiestaatti.action.ActInserisciEsitoParere";
  EventoModel lProvvedimento = new EventoModel();
  lProvvedimento = new EventoModel(evento);

  String checkFavorevole="", checkPFavorevole="", checkContrario="", checkNEParere="";
  String slzstr="";
  if (lProvvedimento.getCodEsito().compareTo("0751")==0)
  {
	  slzstr="Favorevole";
  }
  else if (lProvvedimento.getCodEsito().compareTo("0752")==0)
  {
	  slzstr="Contrario";
  }
  else if (lProvvedimento.getCodEsito().compareTo("0753")==0)
  {
	  slzstr="Parzialmente Favorevole";
  }
  else if (lProvvedimento.getCodEsito().compareTo("0755")==0)
  {
	  slzstr="Incompetenza";
  }
  else if (lProvvedimento.getCodEsito().compareTo("0761")==0)
  {
	  slzstr="Inammissibile";
  }
  else if (lProvvedimento.getCodEsito().compareTo("0762")==0)
  {
	  slzstr="Altro";
  }
  else
  {
	  slzstr="-";
  }
%>

<script language="JavaScript">
    var desktop;
    function ListaUffici(a_formname,a_fieldname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
    }

    function calendario(a_formname,a_field_year,a_field_month,a_field_day)
    {
      desktop = 
          window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
    }
</script>

<html>
<head>
  <title>[S.I.E.S.] - Esito Parere </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
    function Verify()
    {
      // Controllo validita' della data richiesta
      var data_emissione=document.LoadInserisciEsitoParere.<%=ICostantiProduzioneAtti.CAMPO_GIORNO_DATA_EMISSIONE2%>.value+'/'+document.LoadInserisciEsitoParere.<%=ICostantiProduzioneAtti.CAMPO_MESE_DATA_EMISSIONE2%>.value+'/'+document.LoadInserisciEsitoParere.<%=ICostantiProduzioneAtti.CAMPO_ANNO_DATA_EMISSIONE2%>.value;
      if (! ControllaData(data_emissione))
      {
        alert('Data emissione non valida');
        return false;
      }

      // Data emissione minore <= data sistema
      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
      if ( ! CompareDate( data_emissione,data_sistema) )
      {
        alert('Data richiesta maggiore della data attuale!');
        return false;
      }

      // Data inserimento Fascicolo Sius <= data emissione
      if ( !CompareDate( '<%=dataInsFS%>', data_emissione) )
      {
        alert('Data emissione minore della data di inserimento del fascicolo SIGE!');
        return false;
      }

      // Data emissione parere <= data richiesta parere
      var data_richiesta='<%=DateUtils.getDateToString(lProvvedimento.getDataEmissione(),"dd/MM/yyyy")%>';
      if ( !CompareDate( data_richiesta, data_emissione) )
      {
        alert('Data emissione parere minore della data di richiesta parere!');
        return false;
      }
      return true;
    }
  </script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label"> Funzione :</font>&nbsp;
        <font class="campo">Esito Parere </font>
      </td>
  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>

    <tr>
    	<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
    </tr>
  </table>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciEsitoParere">
    <table cellspacing=2 cellpadding=2>

      <tr>
        <td class="l">Data Richiesta Parere</td>
        <td class="L">
          <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lProvvedimento.getDataEmissione(),"dd-MM-yyyy"))%></font>&nbsp;
        </td>
      </tr>

      <tr>
        <td class="l">Destinatario </td>
        <td class="L" >
         <font class="campo"><%=descTipoUfficioS%></font>
        </td>
        <td><input type="hidden" Title="codDestinatario" name="<%=ICostantiProduzioneAtti.CAMPO_COD_DESTINATARIO%>" value="<%=codTipoUfficioS%>" size="5"></td>
      </tr>

      <tr>
        <td class="l">Tipo Parere</td>
        <td class="L" >
          <font class="campo"><%=codMotivo%></font>&nbsp;
        </td>
      </tr>

      <tr>
        <td class="l">Data Emissione Parere<font class=ob>(*)</font></td>
        <td class="L">
          <input Title="Giorno" type="text" size="2" maxlength="2" name="<%= ICostantiProduzioneAtti.CAMPO_GIORNO_DATA_EMISSIONE2 %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lProvvedimento.getDataRicezioneAtti(),"dd"))%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" > /
          <input Title="Mese"   type="text" size="2" maxlength="2" name="<%= ICostantiProduzioneAtti.CAMPO_MESE_DATA_EMISSIONE2 %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lProvvedimento.getDataRicezioneAtti(),"MM"))%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > /
          <input Title="Anno"   type="text" size="4" maxlength="4" name="<%= ICostantiProduzioneAtti.CAMPO_ANNO_DATA_EMISSIONE2 %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lProvvedimento.getDataRicezioneAtti(),"yyyy"))%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >

		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadInserisciEsitoParere','<%=ICostantiProduzioneAtti.CAMPO_ANNO_DATA_EMISSIONE2%>','<%=ICostantiProduzioneAtti.CAMPO_MESE_DATA_EMISSIONE2%>','<%=ICostantiProduzioneAtti.CAMPO_GIORNO_DATA_EMISSIONE2%>');">
       		  <img src="/images/calendario.gif" border=0>
          </a>
        </td>
      </tr>

      <tr>
        <td class="l">Esito Parere <font class=ob>(*)</font></td>
          <td> 
             <select Title="Esito Parere" name="<%=ICostantiProduzioneAtti.CAMPO_ESITO_PARERE%>" >
             	<%-- MERGE v10: modificato commento --%>
               <%-- <option selected value ="<%//=slzstr%>"/><%//=slzstr %> --%>
<%
			   String sel = "";
			   if (slzstr.equals("-"))
			   		sel = " selected";
%>
			   <option value="-" <%=sel%> />-
<%
			   sel = "";
			   if (slzstr.equals("Favorevole"))
			   		sel = " selected";
%>	
               
               <option value="FAVOREVOLE" <%=sel%> />Favorevole
<%
			   sel = "";
			   if (slzstr.equals("Contrario"))
			   		sel = " selected";
%>
               <option value="CONTRARIO" <%=sel%> />Contrario
<%
			   sel = "";
			   if (slzstr.equals("Parzialmente Favorevole"))
			   		sel = " selected";
%>
               <option value="PARZIALMENTE FAVOREVOLE" <%=sel%> />Parzialmente Favorevole
<%
			   sel = "";
			   if (slzstr.equals("Incompetenza"))
			   		sel = " selected";
%>
               <option value="INCOMPETENZA" <%=sel%> />Incompetenza
<%
			   sel = "";
			   if (slzstr.equals("Inammissibile"))
			   		sel = " selected";
%>
               <option value="INAMMISSIBILE" <%=sel%> />Inammissibile
<%
			   sel = "";
			   if (slzstr.equals("Altro"))
			   		sel = " selected";
%>
               <option value="ALTRO" <%=sel%> />Altro
          </td> 
        
      </tr>

   	  <!-- MEV 15 - Revisione SIGE --> 
   	  <tr>
          <td class="l"  width="30%" >Motivazioni</td>
          <td class="L">
              <textarea title="Motivazioni" name="<%=ICostantiProvvedimentoSige.CAMPO_NOTE%>" cols=80 rows=3 ><%=StringUtils.toStringJSP(ProvvedimentoEvento.getProvvedimento().getNote()).trim()%></textarea>
          </td>
      </tr>

      <tr>
        <td>
          <input class="bottone" type="submit" value="Conferma">
        </td>
      </tr>
    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=lProvvedimento.getIdEvento()%>" >
  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciEsitoParere");

    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_GIORNO_DATA_EMISSIONE2 %>","req","Il campo Giorno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_GIORNO_DATA_EMISSIONE2%>","numeric");

    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_MESE_DATA_EMISSIONE2%>","req","Il campo Mese è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_MESE_DATA_EMISSIONE2%>","numeric");

    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_ANNO_DATA_EMISSIONE2%>","req","Il campo Anno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_ANNO_DATA_EMISSIONE2%>","numeric");
    frmvalidator.addValidation("<%=ICostantiProduzioneAtti.CAMPO_ANNO_DATA_EMISSIONE2%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");

	frmvalidator.addValidation("<%= ICostantiProduzioneAtti.CAMPO_ESITO_PARERE %>","req","Il campo Esito Parere  è obbligatorio");
	
    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

  </body>
</html>