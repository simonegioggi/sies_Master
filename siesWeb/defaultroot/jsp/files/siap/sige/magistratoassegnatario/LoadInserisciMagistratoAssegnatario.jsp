<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sige.magistratoassegnatario.action.ICostantiMagistratoAssegnatario"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"%>

<jsp:useBean id="TornaQui"            scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"            scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioMagistratoModel"/>
<jsp:useBean id="acdest" 		      scope="request" class="java.lang.String" />
<jsp:useBean id="AzioneChiamante"     scope="request" class="java.lang.String" />
<jsp:useBean id="FascicoloSigeEsteso" scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Magistrato Assegnatario </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
	  function init()
  	  {
    	document.LoadInserisciMagistratoAssegnatario.<%=ICostantiMagistratoAssegnatario.CAMPO_GIORNO_DATA_INIZIO%>.focus();
  	  }
      function Verify()
      {
        if(document.LoadInserisciMagistratoAssegnatario.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
        {
          alert("Il Cognome del Magistrato è obbligatorio");

          return false;
        }

        if(document.LoadInserisciMagistratoAssegnatario.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
        {
          alert("Il Nome del Magistrato è obbligatorio");

          return false;
        }

        if (document.LoadInserisciMagistratoAssegnatario.<%=ICostantiMagistratoAssegnatario.CAMPO_GIORNO_DATA_INIZIO%>.value.length==1)
			    document.LoadInserisciMagistratoAssegnatario.<%=ICostantiMagistratoAssegnatario.CAMPO_GIORNO_DATA_INIZIO%>.value='0'+document.LoadInserisciMagistratoAssegnatario.<%=ICostantiMagistratoAssegnatario.CAMPO_GIORNO_DATA_INIZIO%>.value;
		    if (document.LoadInserisciMagistratoAssegnatario.<%=ICostantiMagistratoAssegnatario.CAMPO_MESE_DATA_INIZIO%>.value.length==1)
			    document.LoadInserisciMagistratoAssegnatario.<%=ICostantiMagistratoAssegnatario.CAMPO_MESE_DATA_INIZIO%>.value='0'+document.LoadInserisciMagistratoAssegnatario.<%=ICostantiMagistratoAssegnatario.CAMPO_MESE_DATA_INIZIO%>.value;

		    var data_to_verify = document.LoadInserisciMagistratoAssegnatario.<%=ICostantiMagistratoAssegnatario.CAMPO_GIORNO_DATA_INIZIO%>.value+'/'+document.LoadInserisciMagistratoAssegnatario.<%=ICostantiMagistratoAssegnatario.CAMPO_MESE_DATA_INIZIO%>.value+'/'+document.LoadInserisciMagistratoAssegnatario.<%=ICostantiMagistratoAssegnatario.CAMPO_ANNO_DATA_INIZIO%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data di inzio competenza non valida');

          return false;
  		  }

        if ((data_to_verify == document.LoadInserisciMagistratoAssegnatario.DataVecchia.value) && (document.LoadInserisciMagistratoAssegnatario.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO_VECCHIO%>.value==document.LoadInserisciMagistratoAssegnatario.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value))
        {
          alert('Nessuna Modifica Richiesta');

          return false;
        }
        //inserisco il parametro return true per disattivare il pulsante "conferma" del form
        return true;
      }

      function ListaMagistrati(a_formname)
      {
        var a_codnum = document.LoadInserisciMagistratoAssegnatario.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO_VECCHIO %>.value;

        var desktop;
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sige.magistrato.action.ActLoadRicercaMagistratoAssegnazioneLista&formname="+a_formname, "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }

      function calendario(a_formname,a_field_year,a_field_month,a_field_day)
      {
        desktop = 
            window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
      }
    </script>
  </head>
  <body class="corpo" onLoad="Javascript:init();">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        String lAzione = new String();

        lAzione = "siap.sige.magistratoassegnatario.action.ActInserisciMagistratoAssegnatario";
%>
        <font class="campo">Assegnazione/Cambio Magistrato</font>
		<!-- BOTTONE DI RITORNO -->
        	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
  <br>
  <FORM method="POST" name="LoadInserisciMagistratoAssegnatario" action="<%= IWebConstants.PG_MAIN%>">
  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  	<input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">
  <table>

<%	String aCodMagistrato =	"-";
  	String aDataInizio = "-";
  	String aCodMagVecchio = "-";
  	String aCodMagNuovo = "-";
  	String aDataVecchia = null;
  	String aCognome = "-";
  	String aNome = "";

  	if ( magistrato!=null && magistrato.getMagistrato()!=null &&
  	      magistrato.getMagistrato().getCodMagistrato()!=null )
	{
  		aCodMagistrato = StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() );
  	  	aDataInizio = DateUtils.getDateToString(magistrato.getMagistratoAssegnatario().getDataInizio(), "dd-MM-yyyy");
  	  	aCodMagVecchio = StringUtils.toStringJSP(magistrato.getMagistratoAssegnatario().getMagCodMagistrato() );
  	  	aCodMagNuovo = StringUtils.toStringJSP(magistrato.getMagistrato().getCodMagistrato() );  	  	
  	  	aDataVecchia = StringUtils.toStringJSP(DateUtils.getDateToString(magistrato.getMagistratoAssegnatario().getDataInizio(),"dd/MM/yyyy")) ;
  	  	aCognome = StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() ) ;
  	  	aNome = StringUtils.toStringJSP(magistrato.getMagistrato().getNome() ) ;
	}%>
		
     <tr>
       <td class="Titolo" colspan=6> Magistrato Assegnatario Attuale</td>
     </tr>
     <tr>
       <td class="l">Cognome e Nome</td>
       <td class="L"><%=aCognome%>&nbsp; <%=aNome%></td>
     </tr>
     <tr>
       <td class="l">Data Assegnazione</td>
       <td class="L"><%=aDataInizio%> </td>
     </tr>

     <tr>
       <td class="Titolo" colspan=6> Nuovo Magistrato Assegnatario </td>
     </tr>
     <tr>
       <td class="l">Magistrato <font class=ob>(*)</font></td>
       <td class="L">
         <input title="Cognome Magistrato" readonly type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>"   maxlength="35" size="25" >
         <input title= "Nome Magistrato" readonly  type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>"        maxlength="35" size="25">
         <a href="Javascript:ListaMagistrati('LoadInserisciMagistratoAssegnatario');">
           <img src="/images/filefolder.gif" border=0>
         </a>
       </td>
     </tr>

     <tr>
       <td class="l">Data Inizio Competenza <font class=ob>(*)</font></td>
       <td class="L">
          <input type="text" size="2" maxlength="2" name="<%=ICostantiMagistratoAssegnatario.CAMPO_GIORNO_DATA_INIZIO%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" size="2" maxlength="2" name="<%=ICostantiMagistratoAssegnatario.CAMPO_MESE_DATA_INIZIO%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" size="4" maxlength="4" name="<%=ICostantiMagistratoAssegnatario.CAMPO_ANNO_DATA_INIZIO%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">

		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadInserisciMagistratoAssegnatario','<%=ICostantiMagistratoAssegnatario.CAMPO_ANNO_DATA_INIZIO%>','<%=ICostantiMagistratoAssegnatario.CAMPO_MESE_DATA_INIZIO%>','<%=ICostantiMagistratoAssegnatario.CAMPO_GIORNO_DATA_INIZIO%>');">
       		  <img src="/images/calendario.gif" border=0>
          </a>
      </td>

      <td>
        <input type="HIDDEN" title="CodiceMagistratoVecchio" value="<%=aCodMagVecchio%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO_VECCHIO %>"  maxlength="35" size="35">
        <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=aCodMagNuovo%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35">
        <input type="HIDDEN" title="DataVecchia" value="<%=aDataVecchia%>" type="text" name="DataVecchia">
        <input type="HIDDEN" name="<%=ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE%>" value=<%=AzioneChiamante%>>
        <input type="HIDDEN" name="acdest" value="<%=acdest%>">
      </td>
    </tr>
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
      </td>
    </tr>
  </table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator = new Validator("LoadInserisciMagistratoAssegnatario");

  frmvalidator.addValidation("<%=ICostantiMagistrato.CAMPO_COGNOME %>","req","Il Cognome del Magistrato è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiMagistrato.CAMPO_COGNOME %>","req","Il Nome del Magistrato è obbligatorio");

  frmvalidator.addValidation("<%=ICostantiMagistratoAssegnatario.CAMPO_ANNO_DATA_INIZIO%>","numeric");
  frmvalidator.addValidation("<%=ICostantiMagistratoAssegnatario.CAMPO_ANNO_DATA_INIZIO%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiMagistratoAssegnatario.CAMPO_ANNO_DATA_INIZIO%>","lt=3000");

  frmvalidator.addValidation("<%= ICostantiMagistratoAssegnatario.CAMPO_GIORNO_DATA_INIZIO%>","req","Il campo Giorno Inizio Validità è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMagistratoAssegnatario.CAMPO_GIORNO_DATA_INIZIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiMagistratoAssegnatario.CAMPO_MESE_DATA_INIZIO%>","req","Il campo Mese Inizio Validità è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMagistratoAssegnatario.CAMPO_MESE_DATA_INIZIO%>","numeric");

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>