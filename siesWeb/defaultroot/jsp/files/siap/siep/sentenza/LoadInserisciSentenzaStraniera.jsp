<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>
<%@ page import="siap.siep.sentenza.action.ICostantiSentenza"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="sentenza" scope="request" class="siap.siep.sentenza.model.SentenzaModel"/>
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

  <head>
    <title>[S.I.E.S.] - Gestione Sentenza Straniera</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
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
      
    </script>
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
	  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
	  <script language="JavaScript">
      function Verify(){
       
        if (document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value.length==1)
          document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value;
        if (document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
          document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;
        if (document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value.length==1)
          document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value='0'+document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value;

        //Data Sentenza
        var d2=document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>.value+'/'+document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>.value;
        if (! ControllaData(d2))
        {
          alert('Data Sentenza non valida');
          return false;
        }

        return true;
      }
    </script>
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;
<%
    SentenzaModel lSentenza = new SentenzaModel();
    String lAction = new String();

    if( modalita.equals("I") )
    {
      lAction = "siap.siep.sentenza.action.ActInserisciSentenzaStraniera";
%>
      <font class="campo">Inserimento Sentenza Straniera</font>
<%
    }
    else if( modalita.equals("M") )
    {
      lAction = "siap.siep.sentenza.action.ActModificaSentenzaStraniera";
      lSentenza = new SentenzaModel(sentenza);
%>
      <font class="campo">Modifica Sentenza Straniera</font>
<%
    }
%>
  </td>
    </tr>
  </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciSentenzaStraniera">

  <table cellspacing=2 cellpadding=2>
    
    <tr><td class="Titolo" colspan=4>Sentenza di appello da Eseguire</td></tr>
     <tr>
      <td class="l">Data Sentenza <font class="ob">(*)</font></td>
         <td class="L" colspan=3>
            <input Title="Data Sentenza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"dd")) %>" name="<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input Title="Data Sentenza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"MM")) %>" name="<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input Title="Data Sentenza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataProvvedimento(),"yyyy")) %>" name="<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
      </tr>
     <tr>
      <td class="l">Anno/Numero Sentenza <font class="ob">(*)</font></td>
      <td class="L">
          <input Title="Anno Sentenza" value="<%=StringUtils.toStringJSP( lSentenza.getAnnoSentenza()) %>" type="text" name="<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA %>" 
          maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
         /<input Title="Numero Sentenza" value="<%=lSentenza.getNumeroSentenza() %>" type="text" name="<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA %>" maxlength="6" size="6">
      </td>
		</tr>
    <tr>
      <td class="l">Autorità Emittente <font class="ob">(*)</font></td>
      <td class="L" colspan=3>
          <select Title="Autorità Emittente" name="<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>">
          <%=autoritaEmi%>
          </select>
      </td>
		</tr>
   	<tr>
				<td class="l">Luogo Emittente <font class=ob>(*)</font></td>
      <td class="L"  colspan=3>
         <input Title="Luogo Emittente" name="<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>"
            value="<%=lSentenza.getDescrLuogoEmittente()%>" type="text" maxlength="35" size="35">
			       <a href="Javascript:ListaUfficiPerTipo('LoadInserisciSentenzaStraniera','<%=ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>',document.LoadInserisciSentenzaStraniera.<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>[document.LoadInserisciSentenzaStraniera.<%=ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
          		<img src="/images/filefolder.gif" border=0>
          </a>
      </td>
		</tr>
    <tr>
      <td class="l">Sezione Autorità Emittente</td>
      <td class="L" colspan=3>
          <input Title="Sezione Autorità Emittente" value="<%=StringUtils.toStringJSP(lSentenza.getNumSezioneAutoritaEmittente()) %>" type="text" name="<%= ICostantiSentenza.CAMPO_NUM_SEZIONE_AUTORITA_EMITTENTE %>" maxlength="30" size="30" >
      </td>
		</tr>
    <tr><td class="Titolo" colspan=4>Sentenza di riferimento</td></tr>
		<tr>
		  <td class="l">Estremi Sentenza Straniera</td>
      <td class="L" colspan=3>
        <textarea cols=80 rows=5 Title="Note" name="<%=ICostantiSentenza.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(lSentenza.getNote())%></textarea>
      </td>
	</tr>    
    <tr>
      <td colspan=2>
 	      <br>
        <INPUT class="bottone" type="submit" name="INSERISCI" value="Conferma">
      </td>
    </tr>
  </table>

  <input type="HIDDEN" name="Action" value="<%=lAction%>" >
  <input type="HIDDEN" name="<%=ICostantiSentenza.CAMPO_ID_SENTENZA%>" value="<%=lSentenza.getIdSentenza()%>">

  </form>
  <script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("LoadInserisciSentenzaStraniera");
 

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO %>","req","Il campo Giorno della data Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO%>","gt=1");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO %>","req","Il campo Mese della data Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO %>","req","Il campo Anno della data Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO%>","gt=1900");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA %>","req","Il campo Anno Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","numeric");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_ANNO_SENTENZA%>","gt=1900");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA %>","req","Il campo Numero Sentenza è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_NUMERO_SENTENZA%>","numeric");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","req","Il campo Autorità Emittente è obbligatorio");

    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo Luogo Emittente è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE%>","alphabetic");


    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>