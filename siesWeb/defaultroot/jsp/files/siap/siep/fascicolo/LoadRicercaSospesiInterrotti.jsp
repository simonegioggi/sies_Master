<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.sico.ufficio.model.UfficioAccorpatoModel"%>

<jsp:useBean id="tipo" scope="request" class="java.lang.String"/>
<jsp:useBean id="elencoUfficiAccorpati" scope="request" class="java.util.Vector" />

<% 
Collection motivo =(Collection) request.getAttribute("motivo");
Collection oggetto =(Collection) request.getAttribute("oggetto");
%>

<%
     String strMotivo ="";
     Iterator itx = motivo.iterator();
         
     while(itx.hasNext())
     {
        DecodificheModel lDecMod = (DecodificheModel)itx.next();

        strMotivo += lDecMod.getCodiceAlternativo() +";";
        strMotivo += lDecMod.getFiltro()+"#";
     }

     
     String strOggetto ="";
     Iterator itxOggetto = oggetto.iterator();
     while(itxOggetto.hasNext())
     {
        DecodificheModel lDecMod = (DecodificheModel)itxOggetto.next();

        strOggetto += lDecMod.getCode() +";";
        strOggetto += lDecMod.getDescription()+"#";
     }
%>

<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ricerca Provvedimenti Sospesi/Interrotti</title>

    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">

  var strMotivo = "<%=strMotivo%>";
  var strOggetto = "<%=strOggetto%>"


  function caricaCombo (valueTextStr, sep1, sep2, filtro, selField,valueTextStr2)
  {
    // valueTextStr = stringa nel formato richiesto
    // sep1 = separatore interno alla coppia di valori
    // sep2 = separatore tra coppie
    // filtro = valore su cui fare il test
    // selField = oggetto combo da caricare


    clearDropDown(selField);
   
    var aPairs = valueTextStr.split(sep2);
    var aPairs2 = valueTextStr2.split(sep2);
    
    if (valueTextStr.substr(valueTextStr.length - 1) == sep2)
    {
      aPairs[aPairs.length - 1] = null;
      aPairs.length--;
    }
    
    if (valueTextStr2.substr(valueTextStr2.length - 1) == sep2)
    {
      aPairs2[aPairs2.length - 1] = null;
      aPairs2.length--;
    }

    ArrApp = new Array(); 
    var strTuttiTipi='';
    for (var i=0; i < aPairs.length; i++)
    {
      aValueText = aPairs[i].split(sep1);
      strTuttiTipi += aValueText[1]+';'; 

      if (filtro=='null' || filtro==aValueText[0])
      {
    		ArrApp[i]= aValueText[1];	  
      }
    }

  
   var strTuttiMotivi='';  

   for (var i=0; i < aPairs2.length; i++)
    {
      aValueText2 = aPairs2[i].split(sep1);
                
      for(var j=0; j<ArrApp.length; j++)
      {      
       if (ArrApp[j]=='null' || ArrApp[j]==aValueText2[0])
        {
         strTuttiMotivi += aValueText2[0]+';';
         
         oItem = new Option;
    	 oItem.value = aValueText2[0];
    	 oItem.text = aValueText2[1];
    	 selField.options[selField.options.length] = oItem;
        }
      }
  
     } 
     
     if(filtro == '0001')
     {
      optAppTuttiTipi = new Option;
      optAppTuttiTipi.value = strTuttiTipi;
      optAppTuttiTipi.text = 'Tutti';
      selField.options[selField.options.length] = optAppTuttiTipi;     
     }
     else
     {
      optApp = new Option;
      optApp.value = strTuttiMotivi;
      optApp.text = 'Tutti';
      selField.options[selField.options.length] = optApp;      
     }
                             
     selField.options.selectedIndex = 0;       
  }

  function clearDropDown (selField)
  {
  	while (selField.options.length > 0)
  	selField.options[0] = null;
  }

  function caricatuttecombo()
  {
    caricaCombo(strMotivo,';','#',document.f.<%=ICostantiFascicoloSiep.CAMPO_TIPO_INT_SOSP%>.value,document.f.<%=ICostantiFascicoloSiep.CAMPO_MOTIVO_INT_SOSP%>,strOggetto);
  }


  function Verify()
  {
     // Non è possibile specificare solo il numero o solo l'anno
      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value.length != 0)
           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length == 0) )
      {
        alert("Valorizzare Anno inizio ricerca");
        return false;
      }
      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value.length == 0)
           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0) )
      {
        alert("Valorizzare Numero inizio ricerca");
        return false;
      }
      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value.length != 0)
           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length == 0) )
      {
        alert("Valorizzare Anno di fine ricerca");
        return false;
      }
      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value.length == 0)
           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
      {
        alert("Valorizzare Numero di fine ricerca");
        return false;
      }
      
      if((document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value.length != 0)
           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
      {
        if((document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value.length == 0)
           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length == 0))
           {
             alert("Valorizzare anno e numero iniziale");
             return false;
           }
      
      }
            
      // Non è possibile cercare per numero/anno fine minore di numero/anno inizio
      if( (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value.length != 0)
           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0)
           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value.length != 0)
           && (document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
      {
        if(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value < document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
        {
          alert("Anno inizio maggiore Anno fine");
          return false;
        }
        else if(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>.value == document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
        {

          if(parseInt(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value) < parseInt(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value))
          {
            alert("Numero inizio maggiore Numero fine");
            return false;
          }
        }
      }
     
      mostraAttesa('Attendere: elaborazione in corso');

    
      return true;
  }
  

// accetta una stringa di testo non html da mostrare
function mostraAttesa(testo) 
{

    var puntini = 0,
    testoIntrattenimento = prendiElementoDaId("testo-temporaneo"),

    animaTesto = function() 
    {

      var testoAggiunto = "";

      for(var a = 0; a < puntini; a++)
        testoAggiunto += ".";

      testoIntrattenimento.nodeValue = testo + testoAggiunto;

      if(puntini < 4)
        puntini++;
      else
        puntini = 0;

      setTimeout(animaTesto, 300);
    }

  if(testoIntrattenimento.firstChild) 
  {

    animaTesto = function(){};
    testoIntrattenimento.removeChild(testoIntrattenimento.firstChild);
  }
  else 
  {

    testoIntrattenimento = document.createTextNode(testo);

    prendiElementoDaId("testo-temporaneo").appendChild(testoIntrattenimento);

    animaTesto();
  }
}

function prendiElementoDaId(id_elemento) 
{
	var elemento;
	if(document.getElementById)
		elemento = document.getElementById(id_elemento);
	else
		elemento = document.all[id_elemento];
	return elemento;
}
  
    </script>
  </head>
  <body class="corpo" onLoad="javascript:caricatuttecombo()">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG">
           <font  class="label">Funzione :&nbsp;</font>
         <font class="campo">Ricerca Provvedimenti Sospesi/Interrotti</font>
        </td>
      </tr>
    </table>
    <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.fascicolo.action.ActRicercaSospesiInterrotti">

  <table width="100%">
  <tr>
    <td class="l" colspan="4">Indicare eventuale intervallo procedimenti:</td>
  </tr>
  <tr>
      <td class="L" width="25%">
        <font class="label">
          Anno/Numero Iniziale
        </font>
      </td>
      <td class="l" width="25%">
        <input type="text" title="Anno Procedimento Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_INIZIALE%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero Procedimento Iniziale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>" maxlength="14" size="14">
        <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>" value="">
      </td>
      <td class="L" width="25%">
        <font class="label">
          Anno/Numero Finale
        </font>
      </td>
      <td class="l" width="25%">
        <input type="text" title="Anno Procedimento Finale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO_FINALE%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero Procedimento Finale" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>" maxlength="14" size="14">
        <input type="hidden" name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>" value="">
      </td>
  </tr>

  <tr>
      <td class="L" width="25%">
        <font class="label">
          Ufficio Accorpato
        </font>
      </td>
      <td class="l" width="25%">
         	<select name="<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>">
         	<option value="0" >-</option>
  <%
  Iterator it = elencoUfficiAccorpati.iterator();
  int indice = 0;
  while (it.hasNext())
  {
  	UfficioAccorpatoModel ua = (UfficioAccorpatoModel) it.next();
  %>
         	<option value="<%=ua.getIncrProgressivo()%>-<%=ua.getCodUfficio()%>" ><%=ua.getDescrizione()%></option>
  <%
	indice ++;
  }
  %>
         	</select>
      </td>
      <td></td>
      <td></td>
  </tr>

   <tr>
      <td colspan="4"> &nbsp;&nbsp; </td>
   </tr>
  </table>   
  <table width="100%">  
  <tr>
    <td class="l" width="40%">
      Indicare il tipo di Sospensione/Interruzione 
    </td>
     <td class="l" colspan="4">
      <select Title="Tipo" name="<%=ICostantiFascicoloSiep.CAMPO_TIPO_INT_SOSP%>" onchange="javascript:caricatuttecombo()">
        <%=tipo%>
      </select>
    </td>
  </tr>
  <tr>
    <td class="l" width="40%">
      Indicare il motivo della sospensione/interruzione 
    </td>
    <td class="l" colspan="4">
      <select Title="Motivo" class="small" name="<%=ICostantiFascicoloSiep.CAMPO_MOTIVO_INT_SOSP%>" >
      </select>
    </td>
  </tr>
 
  <tr><td>&nbsp;</td></tr>
  <tr>
    <td class="lNoBord" colspan="4">
      <br><INPUT class="bottone" type="submit" name="R" value="Ricerca" onClick="javascript:return checkNewProg();">
    </td>   
  </tr>
</table>
 <div> 
 <table width="100%" >
  <tr>
    <td width="35%">
        &nbsp;    
    </td>   
    <td width="30%" class="lrosso">
        <p id="testo-temporaneo"></p>     
    </td> 
    <td width="35%">
     &nbsp;
    </td>     
  </tr>
 </table>
</div>
</form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("f");

    frmvalidator.setAddnlValidationFunction("Verify");

    function checkNewProg(){
        var ufficioAccorpato = document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO%>.value;
        var parts=ufficioAccorpato.split("-"); 
        var offSetInt = parseInt(parts[0]);

        var numProgIni = document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_INIZIALE%>.value;
        var numProgIniInt = 0;
        if (numProgIni){
        	numProgIniInt = parseInt(numProgIni);
            }
        var newProgIniInt = numProgIniInt + offSetInt;
        if (newProgIniInt>0){
            document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value = newProgIniInt;
            }
        //alert(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_INIZIALE%>.value);

        var numProgFin = document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_ORIGIN_FINALE%>.value;
        var numProgFinInt = 0;
        if (numProgFin){
        	numProgFinInt = parseInt(numProgFin);
            }
        var newProgFinInt = numProgFinInt + offSetInt;
        if (newProgFinInt>0){
            document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value = newProgFinInt;
            }
        //alert(document.f.<%=ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR_FINALE%>.value);

        return true;
    }
  </script>
</body>
</html>