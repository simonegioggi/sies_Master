<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="f3b.log.LogF3B"%>
<%@ page import="siap.siep.penasospesa.action.ICostantiPenaSospesa" %>
<%@ page import="siap.siep.reato.action.ICostantiReato" %>
<%@ page import="siap.sico.evento.model.EventoModel" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna" %>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica" %>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>


<%@ page import="f3b.web.html.Option"%>

<jsp:useBean id="modalita"              scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicolo" scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel" />
<jsp:useBean id="lTipoFunzione"         scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     			scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoObbligo"     scope="request" class="java.lang.String"/>
<jsp:useBean id="oggetto"     scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"           scope="request" class="java.util.Vector"/>


<!-- Combo Autorità -->
<jsp:useBean id="tipoAutoritaPolizia"     scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaAltra"     scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaN"   scope="request" class="java.lang.String"/>

<%
  	SentenzaModel sentenza = fascicolo.getSentenza();
	String uffi_ge=new String();
	String luo_ge=new String();
	String sez_ge=new String();
  	if (sentenza.getDescrTipoAutoritaEmittente() != null){
		uffi_ge=sentenza.getDescrTipoAutoritaEmittente();
		luo_ge=sentenza.getDescrLuogoEmittente();
		sez_ge=sentenza.getNumSezioneAutoritaEmittente();
	}
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Richiesta Adempimento Obblighi</title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>

<script language="JavaScript" src="/html/gen_validatorv2.js"></script>
<script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
<script language="JavaScript">
function ListaComuni(a_formname,a_fieldname)
  {
  	var desktop;
	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
	
function ListaAvvocati(a_formname,a_filtro)
  {
	var desktop;
   	desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadListaAvvocatoPopup&formname="+a_formname+"&filtro="+a_filtro, "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=550,height=500");
  }

function LeggiDestinatari()
  {
	var myselect=document.LoadInserisciRichiestaAdempObblighi.<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_GE%>;
	var myselect_sede=document.LoadInserisciRichiestaAdempObblighi.<%=ICostantiPenaSospesa.CAMPO_SEDE_UFFICIO_GE%>;
	var myselect_sez=document.LoadInserisciRichiestaAdempObblighi.<%=ICostantiPenaSospesa.CAMPO_SEZIONE_UFFICIO_GE%>;
	var titi='<%=uffi_ge%>';
	var titi2='<%=luo_ge%>';
	var titi3='<%=sez_ge%>';
	for (var i=0; i<myselect.length; i++){ //loop through all form elements
 		if (myselect.options[i].text==titi){
  			myselect.options[i].selected=i;
  			myselect_sede.value=titi2;
  			if (titi3!='null')
  				myselect_sez.value=titi3;
  			break
  		}
  	}
  }
</script>

<script language="JavaScript">
function Verify()
  {
		    if (!(document.LoadInserisciRichiestaAdempObblighi.<%=ICostantiPenaSospesa.CAMPO_COD_ARTICOLO%>.value.length > 1))
		    {
		      alert('Specificare Tipologia Obbligo');
		      return false;
		    }
		    if (((!(document.LoadInserisciRichiestaAdempObblighi.<%=ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_GE%>.value.length > 1)) ||
		         (!(document.LoadInserisciRichiestaAdempObblighi.<%=ICostantiPenaSospesa.CAMPO_SEDE_UFFICIO_GE%>.value.length > 1) ) ) 	&&
		        ((!(document.LoadInserisciRichiestaAdempObblighi.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.value.length > 1) ) ||
		         (!(document.LoadInserisciRichiestaAdempObblighi.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>.value.length > 1) ) ) 	&&
			      ((!(document.LoadInserisciRichiestaAdempObblighi.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>.value.length > 1) ) ||
					   (!(document.LoadInserisciRichiestaAdempObblighi.<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>.value.length > 1) ) ) && 
					   (!(document.LoadInserisciRichiestaAdempObblighi.ck_avvocati.checked)) )
		    {
		      alert('Specificare Ufficio e Sede di almeno un Destinatario');
		      return false;
		    }
 }
</script>

</head>

  <body class="corpo" onload="LeggiDestinatari();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
		String lAzione = new String();
		if( modalita.equals("I") ){
        	lAzione = "siap.siep.penasospesa.action.ActInserisciRicAdempObblighi";
%>
			<font class="campo">Inserimento Richiesta Notizie Adempimento Obblighi</font>
<%
       }
       	else if( modalita.equals("M") )
       {
			lAzione = "siap.siep.penasospesa.action.ActModificaRicAdempObblighi";
%>
         	<font class="campo">Modifica Richiesta Notizie Adempimento Obblighi</font>
<%
       }
%>
      </td>
  	 <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
     </tr>
</table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciRichiestaAdempObblighi" >
    <table>
    <tr>
      <td class="l">Data Emissione</td>
      <td class="L" >
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      <td class="l">Data Trasmissione</td>
      <td class="L">
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
  </table>
    <table style="width: 95%;">
	    <tr><td class="Titolo" colspan=4>Dati Atto</td></tr>
  </table>
  <table style="width: 95%;">
		<tr>
        <td class="l">Tipologia Obbligo</td>
        <td class="l">
        <select name="<%=ICostantiPenaSospesa.CAMPO_COD_ARTICOLO%>">
        <%=tipoObbligo%>
        </select>
        </td>
		</tr>
		<tr>
	      	<td class="l">Note</td>
	      	<td class="l">
	        	<Textarea Title="Note" name="<%= ICostantiPenaSospesa.CAMPO_NOTE %>" cols=100 rows=2></textarea>
	      	</td>
		</tr>
		  </table>
    <jsp:include page="/jsp/files/siap/siep/penasospesa/IncDestinatariGe.jsp"/>

<table style="width: 95%;">
  <tr>
    <td class="l" width="20%">Autorità di Polizia </td>
    <td class="L">
      <select  Title="Autorita di Polizia"  name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
        <%=tipoAutoritaPolizia%>
      </select>
    </td>
    <td class="l">Indirizzo</td>
    <td class="L">
      <TEXTAREA title="Indirizzo" name="<%=ICostantiAutoritaEsterna.CAMPO_DESCRIZIONE%>"  cols="30"></textarea>
    </td>
  </tr>
  <tr>
    <td class="l">Sede </td>
    <td class="L">
      <input title="Sede Autorita di Polizia" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>"  maxlength="35" size="35">
      <a href="Javascript:ListaComuni('LoadInserisciRichiestaAdempObblighi','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>');">
        <img src="/images/filefolder.gif" border="0">
      </a>
  </tr>
  
    <tr>
      <td class="l" width="20%">Altra Autorità </td>
      <td class="L">
        <select  Title="Altra Autorita" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>">
          <%=autoritaEsternaAltra%>
        </select>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
        <TEXTAREA title="Indirizzo" name="<%=ICostantiAutoritaEsterna.CAMPO_NOTE_E%>"  cols="30"></textarea>
      </td>
    </tr>
    <tr>
      <td class="l">Sede </td>
      <td class="L">
        <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciRichiestaAdempObblighi','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E %>');">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
    </tr>
</table>  


<table  width="95%">
<%
      int lIdxAvv = 0;
	  int aa=0;
      Iterator lItxAvv = avvocati.iterator();
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
        aa++;
     
%>
    <tr>
        </table>
        <br>
        <table style="width: 95%;">
          <tr>
          <%if(aa==1){%>
		      <td class="L" width=5%>
		        <input type="checkbox" name="ck_avvocati"  value="0" >
		      
      	  <%} else {%>
		      <td class="L" width=5%>
		        
		      
      	  <%} %>
            <td class="l">Per Avvocato&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
              </font>
              &nbsp;Foro di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>
              </font>
              &nbsp;Difensore di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
              </font>
            <input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  maxlength="35" size="35">
		</td>
	</tr>
</table>
<table style="width: 95%;">
     <tr>
		      <td class="L" width=5%>
     <td class="l">Autorità Destinazione </td >
          <td class="L" colspan=3>
             <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>" >
               <%=autoritaEsternaN%>
             </select>
         </td>
     </tr>
     <tr>
		      <td class="L" width=5%>
      <td class="l">Sede </td><td class="L">
        <input title="Sede Foro Avvocato" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>" maxlength="35" size="35">
<%
        if(avvocati.size() > 1)
        {
%>
          <a href="Javascript:ListaComuni('LoadInserisciRichiestaAdempObblighi','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>[<%=lIdxAvv%>]');">
                    <img src="/images/filefolder.gif" border=0>
        </a>
<%
        }
        else
        {
%>
          <a href="Javascript:ListaComuni('LoadInserisciRichiestaAdempObblighi','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C %>');">
                    <img src="/images/filefolder.gif" border=0>
        </a>
<%
        }
%>
      </td>
        <td class="l">Note</td>
       <td class="L">
          <textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>"  cols=35></textarea>
       </td>
    </tr>
    <tr><td>&nbsp;</td>
<%
    lIdxAvv++;
  }
%>
    </tr> 
  </table>


 		<table style="width: 95%;">
  		<tr>
      	<td>
        	<input type="submit" value="Conferma" class="bottone"  name="Inserisci">
      	</td>
      	</tr>
      	</table>
   <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">     	
  <script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciRichiestaAdempObblighi");
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
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2999");
  //////
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","lt=2999");
  frmvalidator.setAddnlValidationFunction("Verify");
  
  
  frmvalidator.setAddnlValidationFunction("Verify");
</script>
  </FORM>
</body>
</html>