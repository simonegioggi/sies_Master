<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.sentenzariunita.model.SentenzaRiunitaModel"%>
<%@ page import="siap.siep.sentenzariunita.action.ICostantiSentenzaRiunita"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="sentenza" scope="request" class="siap.siep.sentenzariunita.model.SentenzaRiunitaModel"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmi" scope="request" class="java.lang.String"/>

<jsp:useBean id="UtenteConnesso"     scope="session" class="siap.sico.utente.model.UtenteModel" />

  <head>
    <title>[S.I.E.S.] - Registrazione Sentenze di primo grado riunite in Appello</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

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

   function Verify()
    {
      if (document.LoadInserisciSentenzaRiunita.<%=ICostantiSentenzaRiunita.CAMPO_GIORNO_DATA_SENTENZA%>.value.length==1)
        document.LoadInserisciSentenzaRiunita.<%=ICostantiSentenzaRiunita.CAMPO_GIORNO_DATA_SENTENZA%>.value='0'+document.LoadInserisciSentenzaRiunita.<%=ICostantiSentenzaRiunita.CAMPO_GIORNO_DATA_SENTENZA%>.value;
      if (document.LoadInserisciSentenzaRiunita.<%=ICostantiSentenzaRiunita.CAMPO_MESE_DATA_SENTENZA%>.value.length==1)
        document.LoadInserisciSentenzaRiunita.<%=ICostantiSentenzaRiunita.CAMPO_MESE_DATA_SENTENZA%>.value='0'+document.LoadInserisciSentenzaRiunita.<%=ICostantiSentenzaRiunita.CAMPO_MESE_DATA_SENTENZA%>.value;

      var d2=document.LoadInserisciSentenzaRiunita.<%=ICostantiSentenzaRiunita.CAMPO_GIORNO_DATA_SENTENZA%>.value+'/'+document.LoadInserisciSentenzaRiunita.<%=ICostantiSentenzaRiunita.CAMPO_MESE_DATA_SENTENZA%>.value+'/'+document.LoadInserisciSentenzaRiunita.<%=ICostantiSentenzaRiunita.CAMPO_ANNO_DATA_SENTENZA%>.value;
      if (!ControllaDataPassaVuota(d2))
      {	alert('Data Sentenza non valida');
         return false;
      }

/* -- Commentato Rework del 17-06-2003 --
      var tendina = document.LoadInserisciSentenzaRiunita.<%= ICostantiSentenzaRiunita.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>.value;

      if (tendina.substring(0,3)=='GIP')
      {
        if (document.LoadInserisciSentenzaRiunita.<%= ICostantiSentenzaRiunita.CAMPO_ANNO_REGE_GIP %>.value.length==0 || document.LoadInserisciSentenzaRiunita.<%= ICostantiSentenzaRiunita.CAMPO_NUMERO_REGE_GIP%>.value.length==0)
        {
          alert('Il campo Numero Re.Ge. GIP è obbligatorio');
          return false;
        }
      }
     if (tendina.substring(0,3)=='DIB' || tendina=='CAS')
      {
        if (document.LoadInserisciSentenzaRiunita.<%= ICostantiSentenzaRiunita.CAMPO_ANNO_REGE_DIB_CAS %>.value.length==0 || document.LoadInserisciSentenzaRiunita.<%= ICostantiSentenzaRiunita.CAMPO_NUMERO_REGE_DIB_CAS%>.value.length==0)
        {
          alert('Il campo Numero Re.Ge. DIB/CAS  è obbligatorio');
          return false;
        }
      }
*/
    }
  </script>
  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;
  <%
    SentenzaRiunitaModel lSentenza = new SentenzaRiunitaModel();
    String lAction = new String();

    if( modalita.equals("I") )
    {
      lAction = "siap.siep.sentenzariunita.action.ActInserisciSentenzaRiunita";
  %>
      <font class="campo">Inserimento Sentenze di Primo grado riunite in Appello</font>
  <%
    }
    else if( modalita.equals("M") )
    {
      lAction = "siap.siep.sentenzariunita.action.ActModificaSentenzaRiunita";
      lSentenza = new SentenzaRiunitaModel(sentenza);
  %>
      <font class="campo">Modifica Sentenze di Primo grado riunite in Appello</font>
  <%
    }

  %>
  </td>
    </tr>
  </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciSentenzaRiunita">

  <table cellspacing=4 cellpadding=4>

  <tr>
        	<tr>
<td class="l">Anno/Numero R.G.N.R. </td>
      <td class="L">
          <input Title="Anno R.G.N.R." value="<%=StringUtils.toStringJSP( lSentenza.getAnnoRegePm()) %>" type="text" name="<%= ICostantiSentenzaRiunita.CAMPO_ANNO_REGE_PM %>" maxlength="4" size="4">
         /<input Title="Numero R.G.N.R." value="<%=lSentenza.getNumeroRegePm() %>" type="text" name="<%= ICostantiSentenzaRiunita.CAMPO_NUMERO_REGE_PM %>" maxlength="6" size="6">
      </td>

		</tr>

  <%
String ARG="";
String NRG="";
String Tipo="";

if (lSentenza.getAnnoRegeCas()!=null)
{
  ARG=lSentenza.getAnnoRegeCas()+"";
  NRG=lSentenza.getNumeroRegeCas()+"";
  Tipo="cas";
}
if (lSentenza.getAnnoRegeDib()!=null)
{
  ARG=lSentenza.getAnnoRegeDib()+"";
  NRG=lSentenza.getNumeroRegeDib()+"";
  Tipo="dib";
}
if (lSentenza.getAnnoRegeGip()!=null)
{
  ARG=lSentenza.getAnnoRegeGip()+"";
  NRG=lSentenza.getNumeroRegeGip()+"";
  Tipo="gip";
}


%>
		<tr>
     <td class="l">Anno/Numero Reg.Gen. </td>
      <td class="L">
          <input Title="Anno Reg.Gen." value="<%=ARG%>" type="text" name="ARG" maxlength="4" size="4">
         /<input Title="Numero Reg.Gen." value="<%=NRG%>" type="text" name="NRG" maxlength="6" size="6">
      &nbsp;

      <select name="TipoRG">
      <%
      String sel="";
      if (Tipo.equals("gip"))
        sel=" selected";
      %>
      <option value="gip"<%=sel%>>GIP</option>
      <%
      sel="";
      if (Tipo.equals("dib"))
        sel=" selected";
      %>
      <option value="dib"<%=sel%>>DIB</option>
      <%
      sel="";
      if (Tipo.equals("cas"))
        sel=" selected";
      %>
      <option value="cas"<%=sel%>>CAS</option>

      </select>
      </td>

		</tr>
	<!------------ANNA ---------------------------------------------->
	<tr>
<%
		String sedePM =( modalita.equals("M")? lSentenza.getDescrSedeNotiziaReato() : UtenteConnesso.getUfficioUtente().getDescrComune());
%>
		<td class="l">Sede PM <font class=ob>(*)</font></td>		
		<td class="L">
			<input Title="Sede PM"
				name="<%=ICostantiSentenzaRiunita.CAMPO_SEDE_NOTIZIA_REATO%>" value="<%=sedePM%>" type="text"
				maxlength="35" size="35">
  	    <a href="Javascript:ListaUfficiPerTipo('LoadInserisciSentenzaRiunita','<%=ICostantiSentenzaRiunita.CAMPO_SEDE_NOTIZIA_REATO%>','PM');">
				<img src="/images/filefolder.gif" border=0>
			</a>
		</td>
	</tr>
	<!---------------------------------------------------------->

    <tr><td class="Titolo" colspan=4>Sentenza Riunita</td></tr>

     <tr>
      <td class="l">Data Sentenza </td>
         <td class="L" colspan=3>
            <input Title="Data Sentenza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataSentenza(),"dd")) %>" name="<%= ICostantiSentenzaRiunita.CAMPO_GIORNO_DATA_SENTENZA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input Title="Data Sentenza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataSentenza(),"MM")) %>" name="<%= ICostantiSentenzaRiunita.CAMPO_MESE_DATA_SENTENZA %>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
            -
            <input Title="Data Sentenza" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lSentenza.getDataSentenza(),"yyyy")) %>" name="<%= ICostantiSentenzaRiunita.CAMPO_ANNO_DATA_SENTENZA %>"maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </td>
      </tr>
     <tr>
      <td class="l">Anno/Numero Sentenza </td>
      <td class="L">
          <input Title="Anno Sentenza" value="<%=StringUtils.toStringJSP( lSentenza.getAnnoSentenza()) %>" type="text" name="<%= ICostantiSentenzaRiunita.CAMPO_ANNO_SENTENZA %>" maxlength="4" size="4" >
         /<input Title="Numero Sentenza" value="<%=lSentenza.getNumeroSentenza() %>" type="text" name="<%= ICostantiSentenzaRiunita.CAMPO_NUMERO_SENTENZA %>" maxlength="6" size="6">
      </td>
		</tr>
    <tr>
      <td class="l">Autorità Emittente <font class="ob">(*)</font></td>
      <td class="L" colspan=3>
          <select Title="Autorità Emittente" name="<%= ICostantiSentenzaRiunita.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>" >
          <%=autoritaEmi%>
          </select>
      </td>
		</tr>
   	<tr>
				<td class="l">Luogo Emittente <font class=ob>(*)</font></td>
      <td class="L"  colspan=3>
         <input Title="Luogo Emittente" name="<%=ICostantiSentenzaRiunita.CAMPO_COD_LUOGO_EMITTENTE%>"
            value="<%=lSentenza.getDescrLuogoEmittente()%>" type="text" maxlength="35" size="35">
	       <a href="Javascript:ListaUfficiPerTipo('LoadInserisciSentenzaRiunita','<%=ICostantiSentenzaRiunita.CAMPO_COD_LUOGO_EMITTENTE%>',document.LoadInserisciSentenzaRiunita.<%= ICostantiSentenzaRiunita.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>[document.LoadInserisciSentenzaRiunita.<%=ICostantiSentenzaRiunita.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0>
          </a>
      </td>
		</tr>
    <tr>
      <td class="l">Sezione Autorità Emittente </font></td>
      <td class="L" colspan=3>
          <input Title="Sezione Autorità Emittente" value="<%=StringUtils.toStringJSP(lSentenza.getSezioneAutoritaEmittente()) %>" type="text" name="<%= ICostantiSentenzaRiunita.CAMPO_SEZIONE_AUTORITA_EMITTENTE %>" maxlength="30" size="30" >
      </td>
		</tr>
<tr>
      <td colspan=2>
 	      <br>
        <INPUT onclick="Javascript:return Verify();" class="bottone" type="submit" name="INSERISCI" value="Conferma">
      </td>
    </tr>


  </table>

  <input type="HIDDEN" name="Action" value="<%=lAction%>" >
  <input type="HIDDEN" name="<%=ICostantiSecurity.CAMPO_ID_FUNZIONE%>" value="<%=request.getAttribute(ICostantiSecurity.CAMPO_ID_FUNZIONE)%>">
  <input type="HIDDEN" name="<%=ICostantiSentenzaRiunita.CAMPO_ID_SENTENZA_RIUNITA%>" value="<%=lSentenza.getIdSentenzaRiunita()%>">

  </form>
  <script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("LoadInserisciSentenzaRiunita");



  frmvalidator.addValidation("<%= ICostantiSentenzaRiunita.CAMPO_GIORNO_DATA_SENTENZA%>","numeric");

  frmvalidator.addValidation("<%= ICostantiSentenzaRiunita.CAMPO_MESE_DATA_SENTENZA%>","numeric");

  frmvalidator.addValidation("<%= ICostantiSentenzaRiunita.CAMPO_ANNO_DATA_SENTENZA%>","numeric");

// frmvalidator.addValidation("<%= ICostantiSentenzaRiunita.CAMPO_ANNO_REGE_PM %>","req","Il campo Anno Re.Ge PM è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenzaRiunita.CAMPO_ANNO_REGE_PM%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenzaRiunita.CAMPO_ANNO_REGE_PM%>","gt=1900");


// frmvalidator.addValidation("ARG","req","Il campo Anno Reg.Gen è obbligatorio");
  frmvalidator.addValidation("ARG","numeric");
  frmvalidator.addValidation("ARG","gt=1900");

//  frmvalidator.addValidation("NRG","req","Il campo Numero Reg.Gen è obbligatorio");
  frmvalidator.addValidation("NRG","numeric");


//  frmvalidator.addValidation("<%= ICostantiSentenzaRiunita.CAMPO_NUMERO_REGE_PM %>","req","Il campo Numero Re.Ge PM è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenzaRiunita.CAMPO_NUMERO_REGE_PM%>","numeric");

  frmvalidator.addValidation("<%= ICostantiSentenzaRiunita.CAMPO_ANNO_SENTENZA%>","numeric");
  frmvalidator.addValidation("<%= ICostantiSentenzaRiunita.CAMPO_ANNO_SENTENZA%>","gt=1900");

  frmvalidator.addValidation("<%= ICostantiSentenzaRiunita.CAMPO_NUMERO_SENTENZA%>","numeric");

  frmvalidator.addValidation("<%= ICostantiSentenzaRiunita.CAMPO_COD_TIPO_AUTORITA_EMITTENTE %>","req","Il campo Autorità Emittente è obbligatorio");

  frmvalidator.addValidation("<%= ICostantiSentenzaRiunita.CAMPO_COD_LUOGO_EMITTENTE %>","req","Il campo Luogo Emittente è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiSentenzaRiunita.CAMPO_COD_LUOGO_EMITTENTE%>","alphabetic");

    </script>
  </body>
</html>