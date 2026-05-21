<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.security.model.ProfileModel" %>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiSoggettoCumulato" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato" %>
<%@ page import="siap.siep.modulocumulo.model.SoggettoCumulatoModel" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>

<jsp:useBean id="soggetto"    			scope="request" class="siap.siep.modulocumulo.model.SoggettoCumulatoModel"/>
<jsp:useBean id="modalita"    			scope="request" class="java.lang.String"/>
<jsp:useBean id="nazioni"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="StatoCittadinanza" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="sesso"       			scope="request" class="java.lang.String"/>
<jsp:useBean id="dataNascitaPresunta" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="UtenteConnesso"      	scope="session" class="siap.sico.utente.model.UtenteModel"/>

<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"    scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<!-- 			LoadInserisciSoggettoCumulato		 -->
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="/css/style.css">
    <title>[S.I.E.S.] - Gestione Cumulo: Inserimento Soggetto - </title>
   <script language="JavaScript">
   var desktop;
   function ListaComuni(a_formname,a_fieldname)
   {
        desktop = window.open("/jsp/Main.jsp?Action=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
   }
    </script>
    <script language="JavaScript" src=/html/gen_validatorv2.js></script>
    <script language="JavaScript" src=/html/ControllaData.js></script>
    
   <script language="JavaScript">
   function Verify()
   {
        var ritorno = true;
        if (document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_COD_STATO_NASCITA%>[document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_COD_STATO_NASCITA%>.selectedIndex].value=='039')
        {
	          document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>.value="";
	          if (document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_COD_COMUNE_NASCITA%>.value.length==0)
	          {
	              alert('Il Comune di Nascita è obbligatorio se lo Stato di Nascita è Italia');
	              document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_COD_COMUNE_NASCITA%>.focus;
	              return false;
	          }
        }
        else
        {
          	document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_COD_COMUNE_NASCITA%>.value='';
          	cancellaCodComuneReale();
    	}
        
        if (document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_GIORNO_DATA_NASCITA%>.value.length==1)
          document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_GIORNO_DATA_NASCITA%>.value='0'+document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_GIORNO_DATA_NASCITA%>.value;
        
          if (document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_MESE_DATA_NASCITA%>.value.length==1)
          document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_MESE_DATA_NASCITA%>.value='0'+document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_MESE_DATA_NASCITA%>.value;
          
       	if(document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_DATA_NASCITA_PRESUNTA%>[document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_DATA_NASCITA_PRESUNTA%>.selectedIndex].value == 'N')
        {
	          var data_to_verify=document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_GIORNO_DATA_NASCITA%>.value+'/'+document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_MESE_DATA_NASCITA%>.value+'/'+document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_ANNO_DATA_NASCITA%>.value;
	          if (! ControllaData(data_to_verify))
	          {
	            alert('Data di nascita non valida');
	            return false;
	          }
        }
        ritorno =  controllaEtaSoggetto();
        return ritorno;
        
   }
   </script>

   <script language="JavaScript">
   function controllaEtaSoggetto()
   {
        var tipoUff = "PM";
        var ritorno = true;
        var oggi = new Date();
        var anno = Math.abs(document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_ANNO_DATA_NASCITA%>.value);
        var mese = 1;
        var giorno = 1;
        if (document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_MESE_DATA_NASCITA%>.value.length > 1 )
            mese = document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_MESE_DATA_NASCITA%>.value;
        if (document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_GIORNO_DATA_NASCITA%>.value.length > 1)
            giorno = document.SoggCumulo.<%=ICostantiSoggettoCumulato.CAMPO_GIORNO_DATA_NASCITA%>.value;
        var anno14 = anno + 14;
        var anno18 = anno + 18;
        var data_compleanno14 = new Date( anno14, mese -1, giorno);
        var data_compleanno18 = new Date( anno18, mese -1, giorno);
           //alert ("14esimo compleanno ->" + data_compleanno14.toString());
           //alert ("18esimo compleanno ->" + data_compleanno18.toString());
           //alert("Tipo Ufficio " + tipoUff);

        if (tipoUff == "PMM" || tipoUff ==  "DIBM")
        {
           // caso Tribunale dei Minori
           if (oggi < data_compleanno14 )
             ritorno = window.confirm('Il soggetto non ha compiuto i 14 anni! Confermi il suo inserimento?');
           if ( oggi > data_compleanno18)
             ritorno = window.confirm('Il soggetto ha più di 18 anni! Confermi il suo inserimento?');
        }
        else
        {
           if (oggi < data_compleanno18)
              ritorno = window.confirm('Il soggetto non ha compiuto i 18 anni. Confermi il suo inserimento?');
        }
        return ritorno;
	}
      
    function cancellaCodComuneReale() 
    {
        document.SoggCumulo.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";       
    }
    
    </script>

  </head>
  <BODY class="corpo">
  <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="/images/quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG><font class="label">Funzione :</font>&nbsp;
              <%
          SoggettoCumulatoModel lSoggetto = null;
          String lAction = new String();
          lAction = "siap.siep.modulocumulo.action.ActInserisciSoggettoCumulato";

          if( modalita.equals("I") )
          {
            lSoggetto = new SoggettoCumulatoModel();
            //lAction = "siap.siep.modulocumulo.action.ActInserisciSoggettoCumulato";
      %>
            <font class="campo">Inserimento Soggetto Cumulo</font>
      <%
          }
          else if( modalita.equals("M") )
          {
            lSoggetto =  new SoggettoCumulatoModel(soggetto);
           	//lAction = "siap.siep.modulocumulo.action.ActModificaSoggettoCumulato";
      %>
            <font class="campo">Modifica Soggetto Cumulo</font>
      <%
          }
      %>
      
    </td>
  </tr>
  </table>
  <% // INCLUDE DEL DETTAGLIO DEL TITOLO%>
  <br>
  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
      </td>
    </tr>
  </table>
  <form  method="POST" action="/jsp/Main.jsp" name="SoggCumulo" id="SoggCumulo">
  
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  <input type="hidden" name="modalita" value="<%=modalita%>">
  
     <table cellspacing=2 cellpadding=2>
    <tr>
        <td class="l">Cognome <font class=ob>(*)</font></td>
        <td class="L"><input title="Cognome" value="<%=StringUtils.toStringJSP(lSoggetto.getCognome())%>" type="text"
        	name="<%=ICostantiSoggettoCumulato.CAMPO_COGNOME%>" maxlength="35" size="35"></td>
        	
        <td class="l">Nome <font class=ob>(*)</font></td>
        <td class="L"><input title="Nome" value="<%=StringUtils.toStringJSP(lSoggetto.getNome())%>" type="text"
        	name="<%=ICostantiSoggettoCumulato.CAMPO_NOME%>" maxlength="35" size="35"></td>
    </tr>
    <tr>
        <td class="l">Sesso <font class=ob>(*)</font></td>
        <td class="L">
          <select title="Sesso" name="<%=ICostantiSoggettoCumulato.CAMPO_SESSO%>">
          <%= sesso %>
          </select>
        </td>
    </tr>
    <tr>
        <td class="l">Data di nascita <font class=ob>(*)</font></td>
          <td class="L">
<%		if(modalita.equals("I"))
		{ %>
            <input type="text" title="Giorno Data di nascita" name="<%=ICostantiSoggettoCumulato.CAMPO_GIORNO_DATA_NASCITA %>" value="" maxlength="2" size="2" 
            	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" title="Mese Data di nascita" name="<%=ICostantiSoggettoCumulato.CAMPO_MESE_DATA_NASCITA %>" value="" maxlength="2" size="2"
            	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
            /
            <input type="text" title="Anno Data di nascita" name="<%=ICostantiSoggettoCumulato.CAMPO_ANNO_DATA_NASCITA %>" value="" maxlength="4" size="4"
            	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%		}
		else if(modalita.equals("M")) 
		{	
			if(lSoggetto.getDataNascita() == null) 
			{	%>
			
				<input type="text" title="Giorno Data di nascita" name="<%=ICostantiSoggettoCumulato.CAMPO_GIORNO_DATA_NASCITA %>" value="" maxlength="2" size="2"
	            	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	            /
	            <input type="text" title="Mese Data di nascita" name="<%=ICostantiSoggettoCumulato.CAMPO_MESE_DATA_NASCITA %>" 
	            	value="<%=StringUtils.toStringJSP(lSoggetto.getMeseNascita(), "")%>" maxlength="2" size="2" 
	            	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
	            /
	            <input type="text" title="Anno Data di nascita" name="<%=ICostantiSoggettoCumulato.CAMPO_ANNO_DATA_NASCITA %>" 
	            	value="<%=StringUtils.toStringJSP(lSoggetto.getAnnoNascita(), "")%>" maxlength="4" size="4" 
	            	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
	            	
	            	
<%			}
			else
			{	%>
	            <input type="text" title="Giorno Data di nascita" name="<%=ICostantiSoggettoCumulato.CAMPO_GIORNO_DATA_NASCITA %>" 
	            	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSoggetto.getDataNascita(),"dd"), "")%>" maxlength="2" size="2" 
	            	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
	            /
	            <input type="text" title="Mese Data di nascita" name="<%=ICostantiSoggettoCumulato.CAMPO_MESE_DATA_NASCITA %>" 
	            	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSoggetto.getDataNascita(),"MM"), "")%>" maxlength="2" size="2" 
	            	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
	            /
	            <input type="text" title="Anno Data di nascita" name="<%=ICostantiSoggettoCumulato.CAMPO_ANNO_DATA_NASCITA %>" 
	            	value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lSoggetto.getDataNascita(),"yyyy"), "")%>" maxlength="4" size="4" 
	            	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%			}
			
		}	%>
						            	
          </td>
        <td class="l">Data Presunta</td>
        <td class="L">
          <select title="Data presunta" name="<%=ICostantiSoggettoCumulato.CAMPO_DATA_NASCITA_PRESUNTA %>">
  			<%=dataNascitaPresunta%>
          </select>
        </td>
      </tr>
      <tr>
        <td class="l">Comune Nascita <font class=ob>(*)</font></td>
        <td class="L">
          <input title="Comune di Nascita" value="<%=StringUtils.toStringJSP(lSoggetto.getDescrComuneNascita())%>" type="text" name="<%=ICostantiSoggettoCumulato.CAMPO_COD_COMUNE_NASCITA %>"  maxlength="35" size="35" onChange="cancellaCodComuneReale();">
          <a href="Javascript:ListaComuni('SoggCumulo','<%=ICostantiSoggettoCumulato.CAMPO_COD_COMUNE_NASCITA%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </td>
      </tr>

  
    <tr>
      <td class="l">Stato Cittadinanza</td>
      <td class="L">
		<select title="Stato Cittadinanza" name="<%=ICostantiSoggettoCumulato.CAMPO_NAZIONALITA%>">
			<%=StatoCittadinanza %>
		</select>
      </td>
      <td class="l">Stato di Nascita</td>
      <td class="L">
		<select  title="Stato di Nascita" name="CodStatoNascita"<%=ICostantiSoggettoCumulato.CAMPO_COD_STATO_NASCITA%> >
			<%=nazioni %>
        </select>
          </td>
    </tr>

    <tr>
        <td class="l">Comune Nascita Estero</td>
        <td class="L"><input title="Comune di Nascita Estero" value="<%=StringUtils.toStringJSP(lSoggetto.getDescComuneNascitaEstero(), "")%>" type="text"
        	name="<%=ICostantiSoggettoCumulato.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>"></td>
    </tr>

    <tr>
        <td class="l">Paternità</td>
        <td class="L"><input title="Paternità" value="<%=StringUtils.toStringJSP(lSoggetto.getPaternita(), "")%>" type="text"
        	name="<%=ICostantiSoggettoCumulato.CAMPO_PATERNITA %>"  maxlength="35" size="35"></td>
    </tr>
    <tr>
        <td class="l">Cognome Madre</td>
        <td class="L">
        	<input title="Cognome della madre" value = "<%=StringUtils.toStringJSP(lSoggetto.getCognomeMadre(), "")%>" type="text" name="<%=ICostantiSoggettoCumulato.CAMPO_COGNOME_MADRE%>" maxlength="35" size="35">
        </td>

        <td class="l">Nome Madre</td>
        <td class="L">
        	<input title="Nome della madre" value = "<%=StringUtils.toStringJSP(lSoggetto.getNomeMadre(), "")%>" type="text" name="<%=ICostantiSoggettoCumulato.CAMPO_NOME_MADRE%>" maxlength="35" size="35">
        </td>
    </tr>
    <tr><td colspan=4 class=l>&nbsp;</td></tr>
    <tr>
        <td class="l">Codice Fiscale</td>
        <td class="L"><input  title="Codice Fiscale" id=CodFiscale
        	value="<%=StringUtils.toStringJSP(lSoggetto.getCodFiscale(), "")%>" type="text"
       		 name="<%=ICostantiSoggettoCumulato.CAMPO_COD_FISCALE%>"  maxlength="16" size="18"></td>

        <td class="l">Atto Nascita</td>
        <td class="L"><input title="Atto di nascita" value="<%=StringUtils.toStringJSP(lSoggetto.getAttoNascita(),"")%>" type="text"
        	name="<%=ICostantiSoggettoCumulato.CAMPO_ATTO_NASCITA%>"  maxlength="10" size="10"></td>
    </tr>

    <tr>
       <td class="l">Codice CUI</td>
       <td class="L">
        	<input title="Codice CUI" value="<%=StringUtils.toStringJSP(lSoggetto.getCodAfis(),"")%>" type = "text" name="<%=ICostantiSoggettoCumulato.CAMPO_COD_AFIS%>" maxlength="7" size="7">
       </td>
       <td >&nbsp;</td><td >&nbsp;</td>
    </tr>

    <tr>
      <td class="l">Note</td>
      <td class="L" colspan=3>
        <TEXTAREA title="note" name="<%=ICostantiSoggettoCumulato.CAMPO_NOTE %>" cols=80 rows=5 ><%=StringUtils.toStringJSP(lSoggetto.getNote(),"")%></textarea>
      </td>
    </tr>
    <tr>
      <td colspan=2>
     <%	if( modalita.equals("I") )
   	   	{%> 
        	<input onclick="Javascript:return Verify();"  class=bottone  type="submit" value="Conferma" title="Inserisci Soggetto">
    <%	}
     	else if( modalita.equals("M") )
     	{%>
     		<input onclick="Javascript:return Verify();"  class=bottone  type="submit" value="Conferma" title="Modifica Soggetto">
   <%	} %>  	    	
      </td>
    </tr>

    <input type="HIDDEN" name="<%=ICostantiSoggettoCumulato.CAMPO_ID_SOGGETTO_CUMULATO %>" value="<%=lSoggetto.getIdSoggettoCumulato() %>">
    <input type="HIDDEN" name="<%=ICostantiSoggettoCumulato.CAMPO_FLAG_STATO %>" value="<%=lSoggetto.getFlagStato() %>">
    <input type="HIDDEN" name="CodComuneReale" value="">

  </table>

  </form>
<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("SoggCumulo");
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_NOME%>","req","Il campo Nome Soggetto è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_NOME%>","maxlen=35","La lunghezza massima per il nome è di 35 caratteri");
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_NOME%>","alphabetic");

  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_COGNOME%>","req","Il campo Cognome Soggetto è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_COGNOME%>","maxlen=35","La lunghezza massima per il cognome è di 35 caratteri");
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_COGNOME%>","alpha");


  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_ANNO_DATA_NASCITA %>","req","Il campo Anno di Nascita è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_ANNO_DATA_NASCITA %>","maxlen=4","La lunghezza massima per l'anno di nascita è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_ANNO_DATA_NASCITA %>","minlen=4","La lunghezza minima per l'anno di nascita è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_ANNO_DATA_NASCITA %>","numeric");
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_ANNO_DATA_NASCITA %>","gt=1900");
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_ANNO_DATA_NASCITA %>","lt=3000");
  <%-- Ticket#202506130166 - SIES: Anomalia inserimento provvedimento - schermata sede dell'autorità emittente--%>
  <%-- ELIMINATO CONTROLLO per consentire inserimento comuni tipo MERANO/MERAN) --%>
<%--   frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_COD_COMUNE_NASCITA %>","alpha"); --%>
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_COD_STATO_NASCITA%>","alphanumeric");
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_DESC_COMUNE_NASCITA_ESTERO%>","alphanumeric");
  
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_PATERNITA %>","alphabetic");
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_COGNOME_MADRE %>","alphabetic");
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_NOME_MADRE %>","alphabetic");

  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_COD_FISCALE%>","alphanumeric");
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_NAZIONALITA %>","alphanumeric");  
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_ATTO_NASCITA%>","alphanumeric");
  
<%--   frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_DESC_COMUNE_NASCITA_ESTERO %>","alpha"); --%>
  frmvalidator.addValidation("<%=ICostantiSoggettoCumulato.CAMPO_COD_AFIS%>","alphanumeric" );

</script>
</body>
</html>