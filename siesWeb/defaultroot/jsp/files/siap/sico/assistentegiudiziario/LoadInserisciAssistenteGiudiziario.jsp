<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel"%>
<%@ page import="siap.sico.assistentegiudiziario.action.ICostantiAssistenteGiudiziario"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<jsp:useBean id="modalita"              scope="request" class="java.lang.String"/>
<jsp:useBean id="assistentegiudiziario" scope="request" class="siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel"/>
<jsp:useBean id="elencoFlagStato"       scope="request" class="java.lang.String"/>
<jsp:useBean id="codFunzione"           scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - GestioneAssistenteGiudiziario </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	
  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
      var DataIni= "ciao";   // data Inizio Validità limite inferiore

      function initDataIni (data)
      {
        DataIni = data;
      }

      function  Verify()
      {
        var ritorno = true;
        var data_to_verify1 = document.LoadInserisciAssistenteGiudiziario.<%=ICostantiAssistenteGiudiziario.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>.value+'/'+document.LoadInserisciAssistenteGiudiziario.<%=ICostantiAssistenteGiudiziario.CAMPO_MESE_DATA_INIZIO_VALIDITA%>.value+'/'+document.LoadInserisciAssistenteGiudiziario.<%=ICostantiAssistenteGiudiziario.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>.value;
        var data_to_verify2 = document.LoadInserisciAssistenteGiudiziario.<%=ICostantiAssistenteGiudiziario.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value+'/'+document.LoadInserisciAssistenteGiudiziario.<%=ICostantiAssistenteGiudiziario.CAMPO_MESE_DATA_FINE_VALIDITA%>.value+'/'+document.LoadInserisciAssistenteGiudiziario.<%=ICostantiAssistenteGiudiziario.CAMPO_ANNO_DATA_FINE_VALIDITA%>.value;

        if ((document.LoadInserisciAssistenteGiudiziario.<%=ICostantiAssistenteGiudiziario.CAMPO_COGNOME%>.value.length == 0)
            || (document.LoadInserisciAssistenteGiudiziario.<%=ICostantiAssistenteGiudiziario.CAMPO_NOME%>.value.length == 0))
        {
          alert("Occorre inserire Cognome e Nome");
          ritorno = false;
        }
        else if (data_to_verify1.length > 2)
        {
          if (! ControllaData(data_to_verify1))
          {
            alert('Data di Inizio Validità scorretta');
            ritorno = false;
          }
          else if (! CompareDate(DataIni, data_to_verify1))
          {
              alert('Data di Inizio Validità non può essere anticipata');
              ritorno = false;
          }
          else if (data_to_verify2.length > 2)
          {
            if (! ControllaData(data_to_verify2))
            {
                  alert('Data di Fine Validità scorretta');
                  ritorno = false;
            }
            else if (! CompareDate(data_to_verify1, data_to_verify2))
            {
                    alert('Data di Inizio Validità non può essere successiva a quella di Fine');
                    ritorno = false;
            }
          }
        }
        else if (data_to_verify2.length > 2)
        {
          alert('Non è possibile specificare Data di Fine Validità senza specificare quella di Inizio Validità ');
          ritorno = false;
        }
        return ritorno;
      }

      function calendario(a_formname,a_field_year,a_field_month,a_field_day)
      {
        desktop = 
            window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
      }
    </script>
  </head>

  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <%
         AssistenteGiudiziarioModel lAssistenteGiudiziario = new AssistenteGiudiziarioModel();
         String lAzione = new String();
         if( modalita.equals("I") )
         {
           lAzione = "siap.sico.assistentegiudiziario.action.ActInserisciAssistenteGiudiziario";
            lAssistenteGiudiziario.setDataInizioValidita(DateUtils.getSysDate());
        %>
          <font class="campo">Inserimento di un Assistente Udienza</font>
        <%
          }
          else if( modalita.equals("M") )
          {
            lAzione = "siap.sico.assistentegiudiziario.action.ActModificaAssistenteGiudiziario";
            lAssistenteGiudiziario = assistentegiudiziario;
            if(lAssistenteGiudiziario.getDataInizioValidita() == null)
              lAssistenteGiudiziario.setDataInizioValidita(DateUtils.getSysDate());
        %>
          <font class="campo">Modifica di un Assistente Udienza</font>
        <%
          }
        %>
        </td>
      </tr>
      <script language="JavaScript">
      initDataIni( "<%=StringUtils.toStringJSP(DateUtils.getDateToString ( lAssistenteGiudiziario.getDataInizioValidita(), "dd/MM/yyyy" )) %>");
      </script>
    </table>

    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciAssistenteGiudiziario">
    <table cellspacing=4 cellpadding=4>
      <tr>
        <td class="l">Cognome (*)</td>
        <td class="l"><input value="<%=lAssistenteGiudiziario.getCognome() %>" type="text" name="<%= ICostantiAssistenteGiudiziario.CAMPO_COGNOME %>"  ></td>
      </tr>
      <tr>
        <td class="l">Nome (*)</td>
        <td class="l"><input value="<%=lAssistenteGiudiziario.getNome() %>" type="text" name="<%= ICostantiAssistenteGiudiziario.CAMPO_NOME %>"  ></td>
      </tr>
        <tr>
          <td class="l">Disponibilità</td>
          <td class="l">
            <select title="FlagStato" name="<%= ICostantiAssistenteGiudiziario.CAMPO_FLAG_STATO %>">
            <%=elencoFlagStato%>
            </select>
          </td>
        </tr>
      <tr>
        <tr>
          <td class="l">DataInizioValidita</td>
          <td>
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAssistenteGiudiziario.getDataInizioValidita(),"dd")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiAssistenteGiudiziario.CAMPO_GIORNO_DATA_INIZIO_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAssistenteGiudiziario.getDataInizioValidita(),"MM")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiAssistenteGiudiziario.CAMPO_MESE_DATA_INIZIO_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAssistenteGiudiziario.getDataInizioValidita(),"yyyy")) %>" type="text" size="4" maxlength="4" name="<%= ICostantiAssistenteGiudiziario.CAMPO_ANNO_DATA_INIZIO_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
<%
	  // MEV 15 - Revisione SIGE
	  // Il calendario viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Inserimento/Modifica di un Assistente Udienza da Funzione Amministrative
	  if( codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90110000) ){
%>
			<a href="javascript:calendario('LoadInserisciAssistenteGiudiziario','<%=ICostantiAssistenteGiudiziario.CAMPO_ANNO_DATA_INIZIO_VALIDITA%>','<%=ICostantiAssistenteGiudiziario.CAMPO_MESE_DATA_INIZIO_VALIDITA%>','<%=ICostantiAssistenteGiudiziario.CAMPO_GIORNO_DATA_INIZIO_VALIDITA%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
          </td>
        </tr>
        <tr>
          <td class="l">DataFineValidita</td>
          <td>
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAssistenteGiudiziario.getDataFineValidita(),"dd")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiAssistenteGiudiziario.CAMPO_GIORNO_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAssistenteGiudiziario.getDataFineValidita(),"MM")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiAssistenteGiudiziario.CAMPO_MESE_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
            /
            <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lAssistenteGiudiziario.getDataFineValidita(),"yyyy")) %>" type="text" size="4" maxlength="4" name="<%= ICostantiAssistenteGiudiziario.CAMPO_ANNO_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
<%
	  // MEV 15 - Revisione SIGE
	  // Il calendario viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Inserimento di un Assistente Udienza da Funzione Amministrative
	  if( codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90110000) ){
%>
			<a href="javascript:calendario('LoadInserisciAssistenteGiudiziario','<%=ICostantiAssistenteGiudiziario.CAMPO_ANNO_DATA_FINE_VALIDITA%>','<%=ICostantiAssistenteGiudiziario.CAMPO_MESE_DATA_FINE_VALIDITA%>','<%=ICostantiAssistenteGiudiziario.CAMPO_GIORNO_DATA_FINE_VALIDITA%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
          </td>
        </tr>
        <tr>
          <td>
            <input class="bottone" type="submit" value="Conferma">
          </td>
        </tr>
    </table>
        <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
        <input type="HIDDEN" name="<%=ICostantiAssistenteGiudiziario.CAMPO_ID_ASSISTENTE_GIUDIZIARIO%>" value="<%=lAssistenteGiudiziario.getIdAssistenteGiudiziario()%>" >
    </form>
    <script language="JavaScript" type="text/javascript">
      var frmvalidator  = new Validator("LoadInserisciAssistenteGiudiziario");

      frmvalidator.setAddnlValidationFunction("Verify");
    </script>
  </body>
</html>