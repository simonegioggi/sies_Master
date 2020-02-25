<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sige.curatore.action.ICostantiCuratore"%>
<%@ page import="siap.sius.curatore.action.ICostantiCuratoreSius"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloSiusModel"%>

<jsp:useBean id="TornaQui"	scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita"	scope="request" class="java.lang.String"/>
<jsp:useBean id="curatore"	scope="request" class="siap.sius.curatore.model.CuratoreSiusModel"/>
<jsp:useBean id="acdest"		scope="request" class="java.lang.String" />
<jsp:useBean id="AzioneChiamante" scope="request" class="java.lang.String" />
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="tipoCuratore"		scope="request" class="java.lang.String" />

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Curatore SIUS </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
	  function init()
  	  {
    	document.LoadInserisciCuratoreSius.<%=ICostantiCuratoreSius.CAMPO_GIORNO_DATA_INIZIO%>.focus();
  	  }
      function Verify()
      {
        if(document.LoadInserisciCuratoreSius.<%=ICostantiCuratore.CAMPO_COGNOME %>.value=="")
        {
          alert("Il Cognome del Curatore è obbligatorio");

          return false;
        }

        if(document.LoadInserisciCuratoreSius.<%=ICostantiCuratore.CAMPO_NOME %>.value=="")
        {
          alert("Il Nome del Curatore è obbligatorio");

          return false;
        }
        if(document.LoadInserisciCuratoreSius.<%=ICostantiCuratoreSius.CAMPO_FLAG_TIPO %>.value=="-")
        {
          alert("Il Tipo è obbligatorio");

          return false;
        }

        if (document.LoadInserisciCuratoreSius.<%=ICostantiCuratoreSius.CAMPO_GIORNO_DATA_INIZIO%>.value.length==1)
			    document.LoadInserisciCuratoreSius.<%=ICostantiCuratoreSius.CAMPO_GIORNO_DATA_INIZIO%>.value='0'+document.LoadInserisciCuratoreSius.<%=ICostantiCuratoreSius.CAMPO_GIORNO_DATA_INIZIO%>.value;
		    if (document.LoadInserisciCuratoreSius.<%=ICostantiCuratoreSius.CAMPO_MESE_DATA_INIZIO%>.value.length==1)
			    document.LoadInserisciCuratoreSius.<%=ICostantiCuratoreSius.CAMPO_MESE_DATA_INIZIO%>.value='0'+document.LoadInserisciCuratoreSius.<%=ICostantiCuratoreSius.CAMPO_MESE_DATA_INIZIO%>.value;

		    var data_to_verify = document.LoadInserisciCuratoreSius.<%=ICostantiCuratoreSius.CAMPO_GIORNO_DATA_INIZIO%>.value+'/'+document.LoadInserisciCuratoreSius.<%=ICostantiCuratoreSius.CAMPO_MESE_DATA_INIZIO%>.value+'/'+document.LoadInserisciCuratoreSius.<%=ICostantiCuratoreSius.CAMPO_ANNO_DATA_INIZIO%>.value;

        if (!ControllaDataPassaVuota(data_to_verify) )
        {
          alert('Data di inzio competenza non valida');

          return false;
  		  }

        if ((data_to_verify == document.LoadInserisciCuratoreSius.DataVecchia.value) && (document.LoadInserisciCuratoreSius.<%=ICostantiCuratoreSius.CAMPO_ID_CURATORE_VECCHIO%>.value==document.LoadInserisciCuratoreSius.<%=ICostantiCuratore.CAMPO_ID_CURATORE%>.value))
        {
          alert('Nessuna Modifica Richiesta');

          return false;
        }
        //inserisco il parametro return true per disattivare il pulsante "conferma" del form
        return true;
      }

      function ListaCuratori(a_formname)
      {
        var a_codnum = document.LoadInserisciCuratoreSius.<%=ICostantiCuratoreSius.CAMPO_ID_CURATORE_VECCHIO %>.value;

        var desktop;
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sige.curatore.action.ActLoadRicercaCuratoreLista&formname="+a_formname, "Ricerca_WCuratore","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
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

        lAzione = "siap.sius.curatore.action.ActInserisciCuratoreSius";
%>
        <font class="campo">Assegnazione/Cambio Curatore</font>
		<!-- BOTTONE DI RITORNO -->
        	<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </td>
    </tr>
  </table>
  <br>
    <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  <br>
  <FORM method="POST" name="LoadInserisciCuratoreSius" action="<%= IWebConstants.PG_MAIN%>">
  	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  	<input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">
  <table>

<%	String aIdCuratore =	"-";
  	String aDataInizio = "-";
  	String aIdCurVecchio = "-";
  	String aIdCurNuovo = "-";
  	String aDataVecchia = null;
  	String aCognome = "-";
  	String aNome = "";
  	String aDescrTipo = "-";
  	String lDesCur = "Curatore Sius";

  	if ( curatore!=null && curatore.getCuratore()!=null &&
  	      curatore.getCuratore().getIdCuratore()!=null )
	{
  		aIdCuratore = StringUtils.toStringJSP(curatore.getCuratore().getIdCuratore() );
  	  aDataInizio = DateUtils.getDateToString(curatore.getDataInizio(), "dd-MM-yyyy");
  	  aIdCurVecchio = StringUtils.toStringJSP(curatore.getCuratore().getIdCuratore() );
  	  aIdCurNuovo = StringUtils.toStringJSP(curatore.getCuratore().getIdCuratore() );  	  	
  	  aDataVecchia = StringUtils.toStringJSP(DateUtils.getDateToString(curatore.getDataInizio(),"dd/MM/yyyy")) ;
  	  aCognome = StringUtils.toStringJSP(curatore.getCuratore().getCognome() ) ;
  	  aNome = StringUtils.toStringJSP(curatore.getCuratore().getNome() ) ;
  	  aDescrTipo = StringUtils.toStringJSP(curatore.getDescrTipo(), "-" ) ;
  	  lDesCur += " Nuovo ";
			%>
		
     <tr>
       <td class="Titolo" colspan=6> Curatore Sius Attuale</td>
     </tr>
     <tr>
       <td class="l">Cognome e Nome</td>
       <td class="L"><%=aCognome%>&nbsp; <%=aNome%></td>
     </tr>
     <tr>
       <td class="l">Tipo</td>
       <td class="L"><%=aDescrTipo%>&nbsp;</td>
     </tr>
     <tr>
       <td class="l">Data Assegnazione</td>
       <td class="L"><%=aDataInizio%> </td>
     </tr>
<%} %>
     <tr>
       <td class="Titolo" colspan=6> <%=lDesCur%> </td>
     </tr>
     <tr>
       <td class="l">Curatore <font class=ob>(*)</font></td>
       <td class="L">
         <input title="Cognome Curatore" readonly type="text" name="<%=ICostantiCuratore.CAMPO_COGNOME%>"   maxlength="35" size="25" >
         <input title= "Nome Curatore" readonly  type="text" name="<%=ICostantiCuratore.CAMPO_NOME%>"        maxlength="35" size="25">
         <a href="Javascript:ListaCuratori('LoadInserisciCuratoreSius');">
           <img src="/images/filefolder.gif" border=0>
         </a>
       </td>
		</tr>

		<tr>
			<td class="l">Tipo : </td>
			<td class="l">
				<select name="<%=ICostantiCuratoreSius.CAMPO_FLAG_TIPO%>" title="Tipo Curatore" >
					<%=tipoCuratore%>
				</select>
     	</td>	
     </tr>

     <tr>
       <td class="l">Data Inizio Competenza <font class=ob>(*)</font></td>
       <td class="L">
          <input type="text" size="2" maxlength="2" name="<%=ICostantiCuratoreSius.CAMPO_GIORNO_DATA_INIZIO%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" size="2" maxlength="2" name="<%=ICostantiCuratoreSius.CAMPO_MESE_DATA_INIZIO%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
          <input type="text" size="4" maxlength="4" name="<%=ICostantiCuratoreSius.CAMPO_ANNO_DATA_INIZIO%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>

      <td>
        <input type="HIDDEN" title="CodiceCuratoreVecchio" value="<%=aIdCurVecchio%>" type="text" name="<%=ICostantiCuratoreSius.CAMPO_ID_CURATORE_VECCHIO %>"  maxlength="35" size="35">
        <input type="HIDDEN" title="CodiceCuratoreNuovo" value="<%=aIdCurNuovo%>" type="text" name="<%=ICostantiCuratore.CAMPO_ID_CURATORE %>"  maxlength="35" size="35">
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
  var frmvalidator = new Validator("LoadInserisciCuratoreSius");

  frmvalidator.addValidation("<%=ICostantiCuratore.CAMPO_COGNOME %>","req","Il Cognome del Curatore è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiCuratore.CAMPO_COGNOME %>","req","Il Nome del Curatore è obbligatorio");

  frmvalidator.addValidation("<%=ICostantiCuratoreSius.CAMPO_ANNO_DATA_INIZIO%>","numeric");
  frmvalidator.addValidation("<%=ICostantiCuratoreSius.CAMPO_ANNO_DATA_INIZIO%>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiCuratoreSius.CAMPO_ANNO_DATA_INIZIO%>","lt=3000");

  frmvalidator.addValidation("<%= ICostantiCuratoreSius.CAMPO_GIORNO_DATA_INIZIO%>","req","Il campo Giorno Inizio Validità è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiCuratoreSius.CAMPO_GIORNO_DATA_INIZIO%>","numeric");

  frmvalidator.addValidation("<%= ICostantiCuratoreSius.CAMPO_MESE_DATA_INIZIO%>","req","Il campo Mese Inizio Validità è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiCuratoreSius.CAMPO_MESE_DATA_INIZIO%>","numeric");

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>