<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="dataInsFS"          					scope="request" class="java.lang.String"/>
<jsp:useBean id="ElencoFiltratoTipiAutorita" 	scope="request" class="java.lang.String"/>
<jsp:useBean id="Azione"											scope="request" class="java.lang.String"/>
<jsp:useBean id="Funzione"										scope="request" class="java.lang.String"/>

<%
	String lFormName = "LoadInserisciRichiestaSanzSostDocumentiIstruttori";
  // Azione da chiamare per l'inserimento dei dati.
  String lAzione = Azione;
	String lFunzione = Funzione;
%>

<script language="JavaScript">
  var desktop;
  
  // Chiamata lista Comuni con filtro sulla Provincia dell'ufficio connesso.
  function ListaComuniRicercaUfficio(a_formname,a_fieldname)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  }

  // Destinatario 2 - Elenco Istituti Penitenziari.
  function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  }

  // Destinatario 3 - Elenco dei UEPE.
  function ListaUEPE(a_formname,a_fieldname)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSA&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
  }

  // Chiamata lista Comuni filtrata per codice tipo ufficio.
  function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
  {
     desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }

  // Chiamata lista comuni completa. ( Da Elimnare ? ).
  function ListaComuniCompleta(a_formname,a_fieldname)
  {
     desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
</script>
<html>
<head>
  <title>[S.I.E.S.] - Richieste Istruttorie Sanzioni Sostitutive </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  	function Verify()
    {
      if (document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value;

      if (document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value;

      // Controllo validita' della data emissione
      var data_emissione=document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      if (! ControllaData(data_emissione))
      {
        alert('Data di emissione non valida');
        return false;
      }

      // Data emissione minore <= data sistema
      var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
      if ( ! CompareDate( data_emissione,data_sistema) )
      {
        alert('Data Emissione maggiore della data attuale!');
        return false;
      }

      // Data inserimento Fascicolo Sius <= data Emissione
      if ( !CompareDate( '<%=dataInsFS%>', data_emissione) )
      {
        alert('Data Emissione minore della data di inserimento del fascicolo SIUS!');
        return false;
      }

      // Controlla che le coppie di campi Destinatario/Sede siano riempiti
      // Destinatario 1
      if (document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[2].value != '-'
          && document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[2].value == '')
      {
        alert('La Sede del destinatario 3 è obbligatoria');
        return false;
      }
      // Destinatario 4
      if (document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[3].value != '-'
          && document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[3].value == '')
      {
        alert('La Sede del destinatario 4 è obbligatoria');
        return false;
      }

      // Controlla che almeno un destinatario sia inserito
      if(	document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0].value == ''
       	&& document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[2].value == '-'
       	&& document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[3].value == '-'
       	&& document.<%=lFormName%>.<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[1].value == '')
      	{
        	alert('Inserire almeno un Destinatario.');
        	return false;
      	}
      	return true;
    }

  </script>
</head>

<body onLoad="document.forms[0].elements[0].focus()" class="corpo">
  <table>
    <tr>
    	<td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
        <font class="campo"><%=lFunzione%></font>
      </td>
      <td class="LBG">
        <a href="javascript:history.go(-1);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="<%=ICostantiRichiestaAtti.MSG_BUTTON_HISTORY%>" width="24" height="24" border="0">
        </a>
      </td>
    </tr>

    <tr>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  
  </table>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="<%=lFormName%>">
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Data Emissione <font class=ob>(*)</font></td>
        <td class="L">
          <input Title="Giorno" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"  > /
          <input Title="Mese" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          <input Title="Anno" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE %>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
        </td>
      </tr>
      <!-- Start new impl -->

      <!-- Start Destinatario 1 -->
      	<tr>
          <td class="l">Destinatario n°1</td>
          <td class="L" colspan=3>
           <table>
           <tr>
             <td class="l">Istituto Penitenziario </td>
             <input type="hidden"  Title="Tipo" name="tipoDest" value="IST_DET" size=50>
           	 <input name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="hidden">
             <td class="l">
               <input type = "hidden" Title="Sede" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>" value="" type="text" maxlength="35" size="50">
               <input type="hidden"  Title="Istituto" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>" value="" size=50>
               <input readonly  Title="Istituto" name="Comune" value="" size=50>
               <a href="Javascript:ListaIstitutoDetenzione('<%=lFormName%>','<%= ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>[0]','Comune');">
               <img src="/images/filefolder.gif" border=0></a>
             </td>
          </tr>
          </table>
        </tr>

        <!-- End Destinatario 1 -->

        <!-- Start Destinatario 2 -->

        <tr>
          <td class="l">Destinatario n°2 </td>
          <td class="L" colspan=3>
          <table>
          	<tr>
            	<td class="l">UEPE &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            										 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            										 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            										 &nbsp;&nbsp;&nbsp; </td>
            										 
             <input type="hidden" Title="Tipo" name="tipoDest" value="UEPE" size=50>
             <input name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="hidden" >
             <td class="l">
             		<input Title="Sede" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>" value="" type="text" maxlength="35" size="50">
              	<input type="hidden"  Title="Tipo" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>" value="CSSA" size=50>
                <a href="Javascript:ListaUEPE('<%=lFormName%>','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[1]');">
                <img src="/images/filefolder.gif" border=0></a>
             </td>
          	</tr>
					</table>
				</tr>        
        <!-- End Destinatario 2 -->

        <!-- Start Destinatario 3 -->
        <tr>
          <td class="l">Destinatario n°3 </td>
          <td class="L" colspan=3>
          <table>
          	<tr>
            	<td class="l">Tipo </td>
            	<input type="hidden" Title="Tipo" name="tipoDest" value="AUT_EXT" size=50>
            	<td class="l">
             		<select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
              		<%= ElencoFiltratoTipiAutorita %>
             		</select>
            	</td>
          	</tr>

          	<tr>
            	<td class="l">Sede <font class=ob>(*)</font></td>
            	<td class="l">
            		<input Title="Sede" name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
                		value="" type="text" maxlength="35" size="35">
                	<a href="Javascript:ListaComuniRicercaUfficio('<%=lFormName%>','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[2]');">
                	<img src="/images/filefolder.gif" border=0> </a>
            	</td>
          	</tr>

          	<tr>
            	<td class="l">Indirizzo </td>
            	<td class="L" colspan=3>
             		<input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
            	</td>
          	</tr>
          </table>
        </tr>
        <!-- End Destinatario  3 -->

        <!-- Start Destinatario 4 -->
        <tr>
          <td class="l">Destinatario n°4</td>
          <td class="L" colspan=3>
          <table>
          	<tr>
            	<td class="l">Tipo</td>
            	<input type="hidden" Title="Tipo" name="tipoDest" value="AUT_EXT" size=50>
            	
            	<td class="l" >
              	<select title="Destinatario" name="<%=ICostantiRichiestaAtti.CAMPO_COD_DESTINATARIO%>">
               	<%= ElencoFiltratoTipiAutorita %>
              	</select>
            	</td>
          </tr>

          <tr>
          	<td class="l">Sede <font class=ob>(*)</font></td>
          	
          	<td class="l">
            	<input Title="Sede " name="<%=ICostantiRichiestaAtti.CAMPO_SEDE%>"
                		value="" type="text" maxlength="35" size="35">
                <a href="Javascript:ListaComuniRicercaUfficio('<%=lFormName%>','<%=ICostantiRichiestaAtti.CAMPO_SEDE%>[3]');">
                <img src="/images/filefolder.gif" border=0> </a>
          
          	</td>
          </tr>
          
          <tr>
            <td class="l">Indirizzo</td>
            <td class="L" colspan=3>
             <input title="Indirizzo" name="<%=ICostantiRichiestaAtti.CAMPO_NOTE%>" value="" type="text" maxlength="80" size="80">
            </td>
          </tr>
          
        </table>
       </tr>
       <!-- End Destinatario 4 -->

        <!-- End new impl  -->

        <!-- Campo Note + campo hidden -->
        <tr>
            <td class="l">Note 1</td>
            <td class="L" colspan=3>
             <input title="Note" name="<%= ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO %>" type="text" maxlength="80" size="80">
            </td>
        </tr>

        <!-- Campo Note + campo hidden -->
        <tr>
            <td class="l">Note 2</td>
            <td class="L" colspan=3>
             <TEXTAREA title="Note" name="<%= ICostantiRichiestaAtti.CAMPO_AGGIUNTIVO %>"  cols=80 rows=4 ></textarea>
            </td>
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
    var frmvalidator = new Validator("<%=lFormName%>");

    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE %>","req","Il campo Giorno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno è obbligatorio");
    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiRichiestaAtti.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
  </body>
</html>