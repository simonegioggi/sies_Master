<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<jsp:useBean id="sentenza"                  scope="request" class="siap.siep.sentenza.model.SentenzaModel" />
<jsp:useBean id="modalita"                  scope="request" class="java.lang.String" />
<jsp:useBean id="tipoDecisioneCassazione"   scope="request" class="java.lang.String" />
<jsp:useBean id="autoritaEmi"               scope="request" class="java.lang.String" />
<jsp:useBean id="UtenteConnesso"            scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="codFunzione"               scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmiDecretoPenale"  scope="request" class="java.lang.String" />

<head>
<title>[S.I.E.S.] - Gestione Decreto</title>
<link rel="STYLESHEET" type="text/css"
	href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">
        var desktop;
        function ListaComuni(a_formname,a_fieldname)
        {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
        }
	    
        // 09/06/2010 Lista Uffici per TIPO_UFFICIO
	    function ListaUfficiPerTipo(a_formname, a_fieldname, codTipoUfficio)
	    {
	      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
	    }

      	function calendario(a_formname,a_field_year,a_field_month,a_field_day)
      	{
        	desktop = 
            	window.open("<%=IWebConstants.ROOT_DIR%>" + "files/siap/sico/Calendario.jsp?formname="+a_formname+"&fieldyear="+a_field_year+"&fieldmonth="+a_field_month+"&fieldday="+a_field_day, "Calendario","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=250");
      	}
    </script>
<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
    function Verify()
    {      
      if (document.LoadInserisciDecreto.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value.length==1)
        document.LoadInserisciDecreto.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciDecreto.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value;
      if (document.LoadInserisciDecreto.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
        document.LoadInserisciDecreto.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciDecreto.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;

      
      var d2=document.LoadInserisciDecreto.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciDecreto.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciDecreto.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;
      if (! ControllaData(d2))
      {	alert('Data Decreto non valida');
         return false;
      }
      return true;
    }
  </script>
</head>

<body class="corpo">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0> </a>
		</td>
		<td class="LBG"><font class="label">Funzione :</font>&nbsp; 
<%SentenzaModel lSentenza = new SentenzaModel();
 	String lAction = new String();

 	if (modalita.equals("I")) {
 		lAction = "siap.siep.sentenza.action.ActInserisciDecreto";
 %> <font class="campo">Inserimento Estremi Decreto Penale</font> <%
 		} else if (modalita.equals("M")) {
 		lAction = "siap.siep.sentenza.action.ActModificaDecreto";
 		lSentenza = new SentenzaModel(sentenza);
 %> <font class="campo">Modifica Estremi Decreto Penale</font> <%
 }
 %>
		</td>
<%
	  // MEV 15 - Revisione SIGE
	  // Il pulsante Torna Indietro viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Inserimento Estremi Decreto Penale da Iscrizione Manuale
	  if(codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90010000)){
%>
	      <!-- BOTTONE DI RITORNO -->
	      <td class="LBG">
	        <a href="javascript:history.go(-1);">
	          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
	        </a>
	      </td>
<%		  
	  }
%>
	</tr>
</table>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciDecreto">

<table cellspacing=2 cellpadding=2>

	<tr>

	</tr>

	<tr>
		
	</tr>
	
	<tr>
		<td class="l">Anno/Numero R.G.N.R.<font class="ob">(*)</font></td>
		<td class="L"><input Title="Anno Re.Ge. PM"
			value="<%=StringUtils.toStringJSP( lSentenza.getAnnoRegePm()) %>"
			type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM %>"
			maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
			<input Title="Numero Re.Ge. PM"
			value="<%=lSentenza.getNumeroRegePm() %>" type="text"
			name="<%= ICostantiSentenza.CAMPO_NUMERO_REGE_PM %>" maxlength="6"
			size="6"></td>

		<td class="l"></td>
		<td class="L"></td>

	</tr>
	<tr>
		<td class="l">Sede PM <font class=ob>(*)</font></td>		
		<% 
			String sedePM =( modalita.equals("M")? lSentenza.getDescrSedeNotiziaReato() : UtenteConnesso.getUfficioUtente().getDescrComune());
			if (UtenteConnesso.getUfficioUtente().getCodTipoUfficio().equals("PGCAP") || modalita.equals("M")) {
		%>
		<td class="L">
			<input Title="Sede PM" name="<%=ICostantiSentenza.CAMPO_SEDE_NOTIZIA_REATO%>" value="<%=sedePM%>" type="text" maxlength="35" size="35">
      	<a href="Javascript:ListaUfficiPerTipo('LoadInserisciDecreto','<%=ICostantiSentenza.CAMPO_SEDE_NOTIZIA_REATO%>','PM');">
					<img src="/images/filefolder.gif" border=0>
				</a>
		</td>
    <% }else{%>		
		<td class="L">
			<input Title="Sede PM" type="text" name="<%=ICostantiSentenza.CAMPO_SEDE_NOTIZIA_REATO%>"
				value="<%=UtenteConnesso.getUfficioUtente().getDescrComune()%>" maxlength="35" size="35" readonly>
		</td>
  <% }%>
	</tr>
	<tr>
		<td class="l">Anno/Numero Reg.Gen. GIP </font></td>
		<td class="L">
			<input Title="Anno Re.Ge. GIP" value="<%=StringUtils.toStringJSP( lSentenza.getAnnoRegeGip()) %>" type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_REGE_GIP %>" 
			maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
			<input Title="Numero Re.Ge. GIP" value="<%=StringUtils.toStringJSP(lSentenza.getNumeroRegeGip()) %>"type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_REGE_GIP  %>" maxlength="6" size="6">
		</td>
	<tr>
		<td class="Titolo" colspan=4>Decreto Penale da Eseguire</td>
	</tr>
	</tr>
	<tr>
		<td class="l">Data Decreto <font class="ob">(*)</font></td>
		<td class="L" colspan=3>
			<input Title="Data Decreto" type="text"	value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd")) %>" 
				name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Data Decreto" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"MM")) %>" 
				name="<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> - 
			<input Title="Data Decreto" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"yyyy")) %>"
				name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%
	  // MEV 15 - Revisione SIGE
	  // Il calendario viene visualizzato solo quando la maschera viene richiamata da SIGE
	  // Funzione: Iscrizione Soggetto da Iscrizione Manuale
	  if(codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90010000)){
%>
			<a href="javascript:calendario('LoadInserisciDecreto','<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>','<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>');">
       			<img src="/images/calendario.gif" border=0>
        	</a>
<%		  
	  }
%>
		</td>
	</tr>
	<tr>
		<td class="l">Anno/Numero Decreto <font class="ob">(*)</font></td>
		<td class="L">
			<input Title="Anno Decreto" value="<%=StringUtils.toStringJSP( lSentenza.getAnnoSentenza()) %>" type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA %>" 
			maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
			<input Title="Numero Decreto" value="<%=lSentenza.getNumeroSentenza() %>" type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA %>" maxlength="6" size="6">
		</td>
	</tr>
	<tr>
		<td class="l">Autorità Emittente <font class="ob">(*)</font></td>
		<td class="L" colspan=3>
			<select Title="Autorità Emittente" name="<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>"> 
<%
	  // MEV 15 - Revisione SIGE
	  // Quando la maschera viene richiamata da SIGE i valori presenti nella combo
	  // sono solo quelli contenenti GIP,GUP e Pretura
	  if(codFunzione != null && codFunzione.equals(ICostantiFascicoloSius.COD_FUNZIONE_90010000)){
%>
			<%=autoritaEmiDecretoPenale%>
<%
	  } else {			
%>		
			<%=autoritaEmi%>
<%
	  }
%>
		
		</select></td>
	</tr>
	<tr>
		<td class="l">Luogo Emittente <font class=ob>(*)</font></td>
		<td class="L" colspan=3>
			<input Title="Luogo Emittente" name="<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>" value="<%=lSentenza.getDescrLuogoEmittente()%>" type="text" maxlength="35" size="35"> 
        <a href="Javascript:ListaUfficiPerTipo('LoadInserisciDecreto','<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>',document.LoadInserisciDecreto.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>[document.LoadInserisciDecreto.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
					<img src="/images/filefolder.gif" border=0>
				</a>
			</td>
	</tr>
	<tr>
		<td class="l">Sezione Autorità Emittente </font></td>
		<td class="L" colspan=3>
			<input Title="Sezione Autorità Emittente" value="<%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaEmittente()) %>" type="text" name="<%= ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE %>" maxlength="30" size="30">
		</td>
	</tr>

	<!---------------------------------------------------------->
	<tr>
		<td class="Titolo" colspan=4>Sentenza Cassazione</td>
	</tr>
	<tr>
		<td class="l">Anno/Numero Sentenza</font></td>
		<td class="L" colspan=3>
			<input Title="Anno Sentenza Cassazione" value="<%=StringUtils.toStringJSP( lSentenza.getAnnoSentenzaCassazione())%>" type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA_CASSAZIONE %>" 
			maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
			<input Title="Numero Sentenza Cassazione" value="<%=StringUtils.toStringJSP(lSentenza.getNumeroSentenzaCassazione()) %>" type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA_CASSAZIONE%>" maxlength="6" size="6">
		</td>
	</tr>

	<tr>
		<td class="l">Anno/Numero Raccolta Generale</font></td>
		<td class="L" colspan=3>
			<input Title="Anno Raccolta Generale" value="<%=StringUtils.toStringJSP( lSentenza.getAnnoRaccoltaGenerale())%>" type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_RACCOLTA_GENERALE %>" 
			maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
			<input Title="Numero Raccolta Generale" value="<%=StringUtils.toStringJSP(lSentenza.getNumeroRaccoltaGenerale())%>" type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_RACCOLTA_GENERALE %>" maxlength="6" size="6">
		</td>
	</tr>

	<tr>

		<td class="l">Dispositivo Cassazione</font></td>
		<td class="L" colspan=3>
			<select Title="Dispositivo Cassazione" name="<%= ICostantiSentenza.CAMPO_COD_TIPO_DECISIONE_CASSAZIONE %>"> 
				<%=tipoDecisioneCassazione%>
			</select>
		</td>
	</tr>

	<!---------------------------------------------------------->
	<tr>
		
	</tr>
	<tr>
		<td class="l">Note</td>
		<td class="L" colspan=3><textarea cols=80 rows=5
			Title="Note Aggiuntive" name="<%=ICostantiSentenza.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(lSentenza.getNote())%></textarea>
		</td>
	</tr>
	<tr>
		<td colspan=2><br>
		<INPUT onclick="Javascript:return Verify();" class="bottone"
			type="submit" name="INSERISCI" value="Conferma"></td>
	</tr>
</table>

	<input type="HIDDEN" name="Action" value="<%=lAction%>">
	<input type="HIDDEN" name="<%=ICostantiSecurity.CAMPO_ID_FUNZIONE%>" value="<%=request.getAttribute(ICostantiSecurity.CAMPO_ID_FUNZIONE)%>">
	<input type="HIDDEN" name="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>" value="<%=lSentenza.getIdSentenza()%>">
</form>

<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("LoadInserisciDecreto");
  
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>","req","Il campo Giorno della data Iscrizione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","gt=1");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>","req","Il campo Mese della data Iscrizione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>","req","Il campo Anno della data Iscrizione è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","lt=2999");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM %>","req","Il campo Anno Re.Ge PM è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_REGE_PM%>","lt=2999");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_REGE_PM %>","req","Il campo Numero Re.Ge PM è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_REGE_PM%>","numeric");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA %>","req","Il campo Anno Decreto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","lt=2999");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA %>","req","Il campo Numero Decreto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>","numeric");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","req","Il campo Autorità Emittente è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","dontselect=0");

  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo Luogo Emittente è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>","alphabetic");

      </script>
</body>
</html>