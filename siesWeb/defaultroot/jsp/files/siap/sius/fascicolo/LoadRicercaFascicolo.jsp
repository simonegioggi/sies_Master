<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>
<%-- MEV10-s3: aggiunta import di classe e cambiata classe all'oggetto "ufficioUtenteConnesso" --%>
<%@ page import="siap.sico.ufficio.model.UfficioModel"%> 

<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicolo" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="tipoUfficioSIEPTrattino" scope="request" class="java.lang.String"/>
<jsp:useBean id="titolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="next_action" scope="request" class="java.lang.String"/>
<jsp:useBean id="ufficioUtenteConnesso" scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ufficiAccorpati" scope="request" class="java.util.Vector" />

<%
// Controllo se il Titolo della maschera  e l'Action 
// da attivare sono passate nella request

	boolean lTitoloPassato = false;
	String lTitolo = "Ricerca Titolo Esecutivo";
	String lNextAction = "siap.sius.fascicolo.action.ActRicercaFascicolo";	
	if (titolo != null && titolo.length() > 0)
	{
		lTitoloPassato = true;
		lTitolo = titolo;
	}
	if (next_action != null && next_action.length() > 0)
	{
		lNextAction = next_action;
	}
%>

<html>
<head>
  <title>[S.I.E.S.] - Iscrizione atti con Titolo esecutivo</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
    function Verify()
    {
      var node;
      node=document.getElementById('singolo');
      if (node.style.visibility=='hidden')
      {return VerifyIntervallo();}
      else
      {return VerifySingolo();}
    }

    function radioOnLoad(){
    	radioBase();
    	loadUfficiAccorpatiByDesc();
        }

    function radioBase()
    {
      var nodeSingolo;
      var nodeIntervallo;

      nodeIntervallo=document.getElementById('divIntervallo');
      nodeSingolo=document.getElementById('singolo');

      if(document.LoadRicercaFascicolo.tipo[0].checked)
      {
        nodeIntervallo.style.visibility='hidden';
        nodeSingolo.style.visibility='visible';
      }
      else if (document.LoadRicercaFascicolo.tipo[1].checked )
      {
        radio();
        nodeIntervallo.style.visibility='visible';
        nodeSingolo.style.visibility='hidden';
      }
    }

    function radio()
    {
      var nodeSingolo;
      var nodeIntervallo;

      nodeSingolo=document.getElementById('singolo');
      nodeIntervallo=document.getElementById('divIntervallo');

      if(document.LoadRicercaFascicolo.tipo[0].checked)
      {
        pulisci();
        nodeSingolo.style.visibility='visible';
        nodeIntervallo.style.visibility='hidden';
        document.LoadRicercaFascicolo.valoreRadio.value='0';
      }
      else if (document.LoadRicercaFascicolo.tipo[1].checked )
      {
        pulisci();
        nodeSingolo.style.visibility='hidden';
        nodeIntervallo.style.visibility='visible';
        document.LoadRicercaFascicolo.valoreRadio.value='1';
      }
    }

    function pulisci()
    {
      document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value='';
      document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>.value='';
      document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value='';
      document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value='';
      document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value='';
      document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value='';
    }

    function VerifySingolo()
    {
      if (document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.value=="" )
      {
        alert("Il campo Anno è obbligatorio");
        document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>.focus();
        return false;
      }
      if (document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>.value=="" )
      {
        alert("Il campo Progressivo è obbligatorio");
        document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>.focus();
        return false;
      }
      loadNumProgOrigin();
      return true;
    }

    function VerifyIntervallo()
    {
      if (document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value=="")
      {
        alert('Il campo Anno Iniziale è obbligatorio');
        document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.focus();
        return false;
      }
      if (document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value=="")
      {
        alert('Il campo Progressivo Iniziale è obbligatorio');
        document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.focus();
        return false;
      }
      if (document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value=="")
      {
        alert('Il campo Anno Finale è obbligatorio');
        document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.focus();
        return false;
      }
      if (document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value=="")
      {
        alert('Il campo Progressivo Finale è obbligatorio');
        document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.focus();
        return false;
      }

      // L'anno iniziale deve essere = anno finale.
      if (document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value   !=
          document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
      {
        alert('I campi Anno Iniziale e Anno Finale devono essere uguali');
        document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.focus();
        return false;
      }

      // Il progr. finale deve essere > del progr. iniziale. La loro differenza non può essere > 200.
      var progr_finale   = document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value;
      var progr_iniziale = document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value;
      if (progr_finale - progr_iniziale > 200)
      {
        alert('Intervallo progressivi troppo ampio ( massimo 200 )!');
        document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.focus();
        return false;
      }
      loadNumProgIntervalOrigin();
      return true;
    }

  </script>

  <script language="JavaScript">
      var desktop;
      function ListaUffici(a_formname,a_fieldname) {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

      function ListaDistretti(a_formname,a_fieldname) {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

      function ListaUfficiMinor(a_formname,a_fieldname) {
        //desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname+"&minor=yes", "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadUfficiMinorDistretto&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

      function ChoosePopup() {
          var selectTipoUfficio = document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_UFFICIO%>;
          var indiceTipoUfficio = selectTipoUfficio.options.selectedIndex;
          var codTipoUfficio = selectTipoUfficio[indiceTipoUfficio].value;
          //alert("codTipoUfficio : "+codTipoUfficio);
          if (codTipoUfficio == 'PM') {
             ListaUffici('LoadRicercaFascicolo','<%= ICostantiFascicoloSiep.CAMPO_DESCR_COMUNE_UFFICIO %>');
          } else if (codTipoUfficio == 'PMM'){
        	  ListaUfficiMinor('LoadRicercaFascicolo','<%=ICostantiFascicoloSiep.CAMPO_DESCR_COMUNE_UFFICIO%>');
          } else if (codTipoUfficio == 'PGCAP'){
         	 ListaDistretti('LoadRicercaFascicolo','<%=ICostantiFascicoloSiep.CAMPO_DESCR_COMUNE_UFFICIO%>');
          }
      }

      function ResetField()
      {
          document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_DESCR_COMUNE_UFFICIO%>.value="";
          var selectUfficioAccorpato = document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>;
          selectUfficioAccorpato.options.length = 0;
          selectUfficioAccorpato.options[selectUfficioAccorpato.options.length] = new Option("-", "0");
      }

      function TrasformaRes(a_formname,a_fieldname,a_fieldname2)
      {
        desktop = window.open("/jsp/Main.jsp?Action=siap.sico.util.ActCalcolaNumeroRes&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
      function TrasformaPret(a_formname,a_fieldname,a_fieldname2)
      {
        desktop = window.open("/jsp/Main.jsp?Action=siap.sico.util.ActCalcolaNumeroPret&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
  </script>

  <script language="JavaScript" src="/html/gen_validatorv2.js"></script>

</head>
  <body class="corpo" onLoad="radioOnLoad();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo"><%=lTitolo%></font>
<%
          FascicoloSiepModel lModel = new FascicoloSiepModel();
%>
        </td>
      </tr>
    </table>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadRicercaFascicolo'>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lNextAction%>">
    <input type="HIDDEN" name="valoreRadio" value="">
<% if (!lTitoloPassato) { %>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="label">Indicare gli estremi del titolo esecutivo :</td>
      </tr>
    </table>
<% } %>
    <table width=68% >
       <tr>
        <td class="l">Tipo Ufficio </td>
        <td class="L">
          <select title="tipoUfficioSIEPTrattino" class=small name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_UFFICIO%>" onchange="Javascript:ResetField();">
            <%= tipoUfficioSIEPTrattino %>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Sede Procura </td>
        <td class="l">
           <input Title="Sede Procura" name="<%=ICostantiFascicoloSiep.CAMPO_DESCR_COMUNE_UFFICIO%>"
              value="<%=lModel.getDescrComuneUfficio()%>" type="text" maxlength="35" size="35" readonly="readonly">
              <a href="Javascript:ChoosePopup();">
              <img src="/images/filefolder.gif" border=0>
              </a>
        </td>
      </tr>

      <tr>
        <td class="l">Ufficio Accorpato </td>
        <td class="l">
         	<select name="<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>">
         	<option value="0" >-</option>
         	</select>
        </td>
      </tr>
    </table>

    <table width=68%>
      <tr><td class="Titolo" colspan='2' >Tipo Ricerca</td></tr>

      <tr>
        <td class="c">Singolo Procedimento SIEP&nbsp;
          <input type="radio" name="tipo" value="Singolo" checked onClick="radioBase();">
            &nbsp;&nbsp;Intervallo Procedimenti SIEP &nbsp;
          <input type="radio" name="tipo" value="Intervallo"  onClick="radioBase();">
      </tr>
    </table>

 <div id="singolo" style="width:100%;">
    <table width=68%>
      <tr>
        <td class="l" width=35%>Anno/Numero <font class=ob>(*)</font></td>
        <td class="l">
          <input Title="Anno SIEP "  type="text" name="<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          /<input Title="Numero SIEP " type="text" name="<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN %>" maxlength="14" size="14">
          <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>" value="">
           &nbsp;&nbsp;<a href="Javascript:TrasformaRes('LoadRicercaFascicolo','<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO %>','<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN %>');">
           R.E.S.<img src="/images/filefolder.gif" border=0></a>&nbsp;&nbsp;
           <a href="Javascript:TrasformaPret('LoadRicercaFascicolo','<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO %>','<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN %>');">
           P.T.<img src="/images/filefolder.gif" border=0></a>
        </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
    </table>
 </div>

 <div id="divIntervallo" style="position:relative; top:-50px; width:100%;">
    <table width=68%>
      <tr>
        <td class="l"width=35%>Anno/Numero Iniziale <font class=ob>(*)</font></td>
        <td class="l">
          <input Title="Anno SIEP Iniziale"  type="text" name="<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE %>" maxlength="4" size="4" onBlur="javascript:document.LoadRicercaFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE %>.value=document.LoadRicercaFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE %>.value" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          /<input Title="Numero SIEP Iniziale" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE %>" maxlength="14" size="14">
          <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>" value="">
        </td>

      </tr>
      <tr>
        <td class="l">Anno/Numero Finale <font class=ob>(*)</font></td>
        <td class="l">
          <input Title="Anno SIEP Finale"  type="text" name="<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          /<input Title="Numero SIEP Finale" type="text" name="<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE %>" maxlength="14" size="14">
          <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>" value="">
        </td>
      </tr>

    </table>
 </div>
 &nbsp;<input onclick="Javascript:return Verify();" class="bottone" type="submit" name="RICERCA" value="Ricerca" style="position:relative; top:-45px;">

 </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadRicercaFascicolo");

    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>","maxlen=14","La lunghezza massima per il Numero Fascicolo è di 14 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>","numeric");

    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","maxlen=4","La lunghezza massima per l'Anno Fascicolo è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","minlen=4","La lunghezza minima per l'Anno Fascicolo è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO%>","numeric");
    
	// MEV10-s3: aggiunta impostazione di un valore nella combo del tipo ufficio
	<% String codTipoUfficio = ufficioUtenteConnesso.getCodTipoUfficio(); %>
	var selectTipoUfficio = document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_UFFICIO%>;
	if ("UDSM" == "<%= codTipoUfficio %>" || "TDSM" == "<%= codTipoUfficio %>") {
		selectTipoUfficio.value = "PMM";
	}
	else if ("UDS" == "<%= codTipoUfficio %>" || "TDS" == "<%= codTipoUfficio %>") {
		selectTipoUfficio.value = "PM";
	}
  </script>

  <script language="JavaScript" type="text/javascript">
  var ufficiAccorpatiArray = new Array();

  <%
  Iterator uaIter = ufficiAccorpati.iterator();
  int uaIndice = 0;
  while (uaIter.hasNext())
  {
  	UfficioAccorpatoModel uaModel = (UfficioAccorpatoModel) uaIter.next();
  %>
  ufficiAccorpatiArray[<%=uaIndice%>] = new Array("<%=uaModel.getDescrizione()%>","<%=uaModel.getIncrProgressivo()%>","<%=uaModel.getCodTipoUfficio()%>","<%=uaModel.getCodUfficioNew()%>","<%=uaModel.getCodUfficio()%>","<%=uaModel.getDescrizioneNewUfficio()%>"); 
  <%
  uaIndice ++;
  }
  %>

  function transCoding(cod){
	  var ret = cod;
	  if (cod=='DIB'){
		  ret = 'Tribunale Ordinario';
	  } else if (cod=='TRIBSD'){
		  ret = 'Sezione Distaccata Tribunale';
	  } else if (cod=='CAS'){
		  ret = 'Corte Assise';
	  } else if (cod=='GIP'){
		  ret = 'Gip presso Tribunale';
	  } else if (cod=='PM'){
		  ret = 'Procura presso Tribunale';
	  } else if (cod=='PGCAP'){
		  ret = 'Procura presso Corte Appello';
	  }
	  return ret;
  }

  function loadUfficiAccorpati(codUfficio){
		var i=0;
		var ufficioAccorpatoSelect = document.LoadRicercaFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>;
		//alert("loadUfficiAccorpati: "+codUfficio);
		ufficioAccorpatoSelect.options.length = 0;
		ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
		while(i<ufficiAccorpatiArray.length){
			var ufficio = ufficiAccorpatiArray[i];
			if (ufficio[3]==codUfficio){
				ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option(ufficio[0]+" ("+transCoding(ufficio[2])+")", ufficio[1]+"-"+ufficio[4]+"-"+ufficio[2]);
			}
			i++;
		}
	}

  function loadUfficiAccorpatiByDesc(){
		var i=0;
		var ufficioAccorpatoSelect = document.LoadRicercaFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>;
		var ufficioBaseDesc = document.LoadRicercaFascicolo.<%= ICostantiFascicoloSiep.CAMPO_DESCR_COMUNE_UFFICIO %>.value;
		ufficioAccorpatoSelect.options.length = 0;
		ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option("-", "0");
		while(i<ufficiAccorpatiArray.length){
			var ufficio = ufficiAccorpatiArray[i];
			if (ufficio[5]==ufficioBaseDesc){
				if (ufficio[2]=='PM' || ufficio[2]=='PGCAP'){
					ufficioAccorpatoSelect.options[ufficioAccorpatoSelect.options.length] = new Option(ufficio[0]+" ("+transCoding(ufficio[2])+")", ufficio[1]+"-"+ufficio[4]+"-"+ufficio[2]);
				}
			}
			i++;
		}
	}

  function loadNumProgOrigin(){
	  document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value = "";
	  var numProg = document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN%>.value;
      var ufficioAccorpato = document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>.value;
      var parts=ufficioAccorpato.split("-"); 
      var offSetInt = parseInt(parts[0]);
      if (numProg){
    	  var newProg = parseInt(numProg) + offSetInt;
    	  document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value = newProg;
          }
	  //alert(document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR%>.value);
	  }

  function loadNumProgIntervalOrigin(){
      var ufficioAccorpato = document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>.value;
      var parts=ufficioAccorpato.split("-"); 
      var offSetInt = parseInt(parts[0]);
      document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value = "";
      var numProgIni = document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value;
      if (numProgIni){
      	var numProgIniInt = parseInt(numProgIni);
        var newProgIniInt = numProgIniInt + offSetInt;
        document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value = newProgIniInt;
          }
      //alert(document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value);

      document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value = "";
      var numProgFin = document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value;
      if (numProgFin){
          var numProgFinInt = parseInt(numProgFin);
          var newProgFinInt = numProgFinInt + offSetInt;
          document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value = newProgFinInt;
          }
      //alert(document.LoadRicercaFascicolo.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value);
  	}

	function verificaScelta(){
		var ufficioAccorpatoSelect = document.LoadRicercaFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO %>;
		if (ufficioAccorpatoSelect.selectedIndex>0){
			var tipoUfficioSelect = document.LoadRicercaFascicolo.<%= ICostantiFascicoloSiep.CAMPO_CHIAVE_UFFICIO %>;
			tipoUfficioSelect.options[0].setAttribute("selected", "selected");
			var descUfficio = document.LoadRicercaFascicolo.<%= ICostantiFascicoloSiep.CAMPO_DESCR_COMUNE_UFFICIO %>;
			descUfficio.value = "";
		}
	}
  </script>

  </body>
</html>