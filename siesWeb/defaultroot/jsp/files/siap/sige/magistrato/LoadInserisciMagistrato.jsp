<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.sige.magistratosezione.model.MagistratoSezioneModel"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.log.LogF3B"%>

<%@ page import="siap.sige.magistrato.model.MagistratoModel"%>
<%@ page import="siap.sige.magistrato.action.ICostantiMagistrato"%>

<jsp:useBean id="modalita"         	scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"       	scope="request" class="siap.sige.magistrato.model.MagistratoModel"/>
<jsp:useBean id="magistratosezione" scope="request" class="siap.sige.magistratosezione.model.MagistratoSezioneModel"/>
<jsp:useBean id="elencoFlagStato"  	scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoSezioni"  	scope="request" class="java.lang.String"/>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Magistrato </title>

    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">

      var DataOdierna= "";   // data Inizio Validità limite inferiore
      function initDataOdierna (data)
      {
    	  DataOdierna = data;
      }
      
      var DataIniDIA= "";   // data Inizio Assegnazione limite inferiore
      function initDataIniDIA (data)
      {
    	  DataIniDIA = data;
      }
      
      function  Verify()
      {
        var ritorno = true;
        <%-- 20170918: [SG] solo in modifica --%>
        var data_to_verify2 = "";
        <% if ("M".equals(modalita)) { %>
        	data_to_verify2 = document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_GIORNO_DATA_FINE_VALIDITA%>.value+'/'+document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_MESE_DATA_FINE_VALIDITA%>.value+'/'+document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_ANNO_DATA_FINE_VALIDITA%>.value;
        <% } %>

        var data_to_verifyDIA = document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_GIORNO_DATA_INIZIO_ASSEGNAZIONE%>.value+'/'+document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_MESE_DATA_INIZIO_ASSEGNAZIONE%>.value+'/'+document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_ANNO_DATA_INIZIO_ASSEGNAZIONE%>.value;
        var data_to_verifyDFA = document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_GIORNO_DATA_FINE_ASSEGNAZIONE%>.value+'/'+document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_MESE_DATA_FINE_ASSEGNAZIONE%>.value+'/'+document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_ANNO_DATA_FINE_ASSEGNAZIONE%>.value;
        
        //inizio controlli sulle date inizio e fine assegnazione
        if(data_to_verifyDIA.length > 2){
	        if ( ! ControllaData(data_to_verifyDIA))
	        {
	        	alert('Data di Inizio Assegnazione scorretta');
	            ritorno = false;
	        }  
	        
	        else if (DataIniDIA.length > 2  && ! CompareDate(data_to_verifyDIA, DataIniDIA))
        	{
        		alert('Data Assegnazione non può essere successiva alla data iniziale di assegnazione');
          		ritorno = false;
        	} 
        }	
        if (data_to_verifyDFA.length > 2)
        {
	    	if (! ControllaData(data_to_verifyDFA))
	        {
	        	alert('Data di Fine Assegnazione scorretta');
	            ritorno = false;
	        }
	    	else if (data_to_verifyDIA.length > 2 && ! CompareDate(data_to_verifyDIA, data_to_verifyDFA))
	        {
	          alert('Data di Inizio Assegnazione non può essere successiva a quella di Fine');
	          ritorno = false;
	        }
        }
        //fine controlli sulle date inizio e fine assegnazione
        
        if ((document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_COGNOME%>.value.length == 0)
            || (document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_NOME%>.value.length == 0)
            || (document.LoadInserisciMagistrato.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.value.length == 0))
        {
          alert("Occorre inserire i dati necessari alla individuazione del magistrato");
          ritorno = false;
        }
        <%-- 20170918: [SG] solo in modifica --%>
        else if (data_to_verify2.length > 2)
        {
          if (! ControllaData(data_to_verify2))
          {
          	alert('Data Fine Validità scorretta');
            ritorno = false;
          }
          else if ( !CompareDate(DataOdierna, data_to_verify2)) {
        	  // 20170915: [SG] modificati i msgs di allarme
//             alert("DATA ODIERNA: " + DataOdierna);
//             alert("DATA FINE: " + data_to_verify2);
        		alert('Data Fine Validità: ' + data_to_verify2 + ' non può essere precedente alla Data Odierna: ' + DataOdierna);
            	ritorno = false;
          }
        }
        return ritorno;
      }

      function ListaWMagistrati(a_formname)
      {
        var desktop;
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.w_magistrato.action.ActLoadRicercaWMagistrato&formname="+a_formname, "Ricerca_WMagistrato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
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
      <tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
			</a>
		</td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <%
          MagistratoModel lMagistrato= null;
          MagistratoSezioneModel lSezioneMagistrato= null;
          String lAzione = new String();

          if( modalita.equals("I") )
          {
        	lAzione = "siap.sige.magistrato.action.ActInserisciMagistrato";
        	  
            lMagistrato = new MagistratoModel();
            lMagistrato.setDataInizioValidita(DateUtils.getSysDate());
            
            lSezioneMagistrato = new MagistratoSezioneModel();
            lSezioneMagistrato.setDataInizioAssegnazione(DateUtils.getSysDate());
        %>
          <font class="campo">Inserimento di un Magistrato</font>
        <%
          }
          else if( modalita.equals("M") )
          {
        	lAzione = "siap.sige.magistrato.action.ActModificaMagistrato";
            lMagistrato = new MagistratoModel(magistrato);
            lSezioneMagistrato = new MagistratoSezioneModel(magistratosezione);
        %>
          <font class="campo">Modifica di un Magistrato</font>
        <%
        	}
        %>
          <script language="JavaScript">
            initDataOdierna( "<%=DateUtils.getSysDate("dd/MM/yyyy")%>");
            initDataIniDIA( "<%=StringUtils.toStringJSP(DateUtils.getDateToString ( lSezioneMagistrato.getDataInizioAssegnazione(), "dd/MM/yyyy" )) %>");
          </script>
        </td>
        <!-- BOTTONE DI RITORNO -->
		<jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      </tr>
    </table>

    <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMagistrato">
      <table cellspacing=2 cellpadding=2>
       <tr>
          <td class="l">Cognome <font class=ob>(*)</font></td>
          <td class="l">
				<input value="<%=lMagistrato.getCognome()%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME %>"  readonly > 
		  </td>
      <%
        if( modalita.equals("I") )
        {
      %>
      		<td class="l">
            <a href="Javascript:ListaWMagistrati('LoadInserisciMagistrato');">
            Seleziona dalla lista <img src="/images/filefolder.gif" border=0>
            </a>
          </td>
      <%
      	} 
      %>

        </tr>
        <tr>
          <td class="l">Nome <font class=ob>(*)</font></td>
          <td class="l"><input value="<%=StringUtils.toStringJSP(lMagistrato.getNome()) %>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"  readonly></td>
        </tr>

        <tr>
          <td class="l">Codice CSM <font class=ob>(*)</font></td>
          <td class="l"><input value="<%=StringUtils.toStringJSP(lMagistrato.getCodMagistrato()) %>" type="text" name="<%= ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  readonly></td>
        </tr>
<%
// 20171016: [SG] eliminata la multiselezione in modifica
        if( modalita.equals("I")) {
      %>
		<tr>
      		<td class="l">Sezione</td>
      		<td class="l">
        		<select multiple title="Sezione" name="<%=ICostantiMagistrato.CAMPO_SEZIONE%>">
        		<%=elencoSezioni%>
        		</select>
					<br>
					<font class=cVerde>Per selezionare/deselezionare più sezioni <br> premere tasto CTRL + tasto sinistro del mouse.</font> 
      		</td>
    	</tr>
<%
        } else {
      %>
      <tr>
      		<td class="l">Sezione</td>
      		<td class="l">
        		<select title="Sezione" name="<%=ICostantiMagistrato.CAMPO_SEZIONE%>">
        		<%=elencoSezioni%>
        		</select>
      		</td>
    	</tr>
    	<%
        }
      %>
    	<tr>
          <td class="l">Data Inizio Assegnazione</td>
          <td class="l">
	          <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSezioneMagistrato.getDataInizioAssegnazione(),"dd")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiMagistrato.CAMPO_GIORNO_DATA_INIZIO_ASSEGNAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
	          /
	          <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSezioneMagistrato.getDataInizioAssegnazione(),"MM")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiMagistrato.CAMPO_MESE_DATA_INIZIO_ASSEGNAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
	          /
	          <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSezioneMagistrato.getDataInizioAssegnazione(),"yyyy")) %>" type="text" size="4" maxlength="4" name="<%= ICostantiMagistrato.CAMPO_ANNO_DATA_INIZIO_ASSEGNAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
	
			  <!-- MEV 15 - Revisione SIGE -->
			  <a href="javascript:calendario('LoadInserisciMagistrato','<%=ICostantiMagistrato.CAMPO_ANNO_DATA_INIZIO_ASSEGNAZIONE%>','<%=ICostantiMagistrato.CAMPO_MESE_DATA_INIZIO_ASSEGNAZIONE%>','<%=ICostantiMagistrato.CAMPO_GIORNO_DATA_INIZIO_ASSEGNAZIONE%>');">
	      	      <img src="/images/calendario.gif" border=0>
	       	  </a>
          </td>
        </tr>
        <tr>
          <td class="l">Data Fine Assegnazione</td>
          <td class="l">
	          <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSezioneMagistrato.getDataFineAssegnazione(),"dd")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiMagistrato.CAMPO_GIORNO_DATA_FINE_ASSEGNAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> 
	          /
	          <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSezioneMagistrato.getDataFineAssegnazione(),"MM")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiMagistrato.CAMPO_MESE_DATA_FINE_ASSEGNAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
	          /
	          <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSezioneMagistrato.getDataFineAssegnazione(),"yyyy")) %>" type="text" size="4" maxlength="4" name="<%= ICostantiMagistrato.CAMPO_ANNO_DATA_FINE_ASSEGNAZIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
			  <!-- MEV 15 - Revisione SIGE -->
			  <a href="javascript:calendario('LoadInserisciMagistrato','<%=ICostantiMagistrato.CAMPO_ANNO_DATA_FINE_ASSEGNAZIONE%>','<%=ICostantiMagistrato.CAMPO_MESE_DATA_FINE_ASSEGNAZIONE%>','<%=ICostantiMagistrato.CAMPO_GIORNO_DATA_FINE_ASSEGNAZIONE%>');">
	      	      <img src="/images/calendario.gif" border=0>
	       	  </a>
          </td>
        </tr>      

        <tr>
          <td class="l">Email Ufficio</td>
          <td class="l"><input value="<%=StringUtils.toStringJSP(lMagistrato.getEMailUfficio()) %>" type="text" name="<%=ICostantiMagistrato.CAMPO_E_MAIL_UFFICIO%>"></td>
        </tr>
        <tr>
          <td class="l">Email Privata</td>
          <td class="l"><input value="<%=StringUtils.toStringJSP(lMagistrato.getEMailPrivata()) %>" type="text" name="<%= ICostantiMagistrato.CAMPO_E_MAIL_PRIVATA %>"  ></td>
        </tr>
        <tr>
          <td class="l">Num. cell.</td>
          <td class="l"><input value="<%=StringUtils.toStringJSP(lMagistrato.getNumCellulare()) %>" type="text" name="<%= ICostantiMagistrato.CAMPO_NUM_CELLULARE  %>"  ></td>
        </tr>
        <tr>
          <td class="l">Disponibilità</td>
          <td class="l">
            <select title="FlagStato" name="<%= ICostantiMagistrato.CAMPO_FLAG_STATO %>">
            	<%= elencoFlagStato %>
            </select>
          </td>
          <%-- 20170918: [SG] solo in modifica --%>
			<% if ("M".equals(modalita)) { %>
          <td class="l">Data Fine Validità</td>
          <td class="l">
          <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lMagistrato.getDataFineValidita(),"dd")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiMagistrato.CAMPO_GIORNO_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> 
          /
          <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lMagistrato.getDataFineValidita(),"MM")) %>" type="text" size="2" maxlength="2" name="<%= ICostantiMagistrato.CAMPO_MESE_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          /
          <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lMagistrato.getDataFineValidita(),"yyyy")) %>" type="text" size="4" maxlength="4" name="<%= ICostantiMagistrato.CAMPO_ANNO_DATA_FINE_VALIDITA %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
		  <!-- MEV 15 - Revisione SIGE -->
		  <a href="javascript:calendario('LoadInserisciMagistrato','<%=ICostantiMagistrato.CAMPO_ANNO_DATA_FINE_VALIDITA%>','<%=ICostantiMagistrato.CAMPO_MESE_DATA_FINE_VALIDITA%>','<%=ICostantiMagistrato.CAMPO_GIORNO_DATA_FINE_VALIDITA%>');">
      	      <img src="/images/calendario.gif" border=0>
       	  </a>
          </td>
          <% } %>
        </tr>
        <tr>
          <td>
            <input class="bottone" type="submit" value="Conferma">
          </td>
        </tr>
      </table>
      <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
    </form>
      <script language="JavaScript" type="text/javascript">
        var frmvalidator  = new Validator("LoadInserisciMagistrato");
        frmvalidator.setAddnlValidationFunction("Verify");
      </script>
  </body>
</html>