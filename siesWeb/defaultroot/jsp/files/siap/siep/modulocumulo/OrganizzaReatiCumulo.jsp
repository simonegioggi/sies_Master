<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.modulocumulo.model.ReatoCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiReatoCumulo"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>



<jsp:useBean id="IstruttoriaCumulo"    scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="reaticum"          scope="request" class="java.util.Vector"/>
<jsp:useBean id="stringareatocum"   scope="request" class="java.util.Vector"/>
<jsp:useBean id="stringanorma"      scope="request" class="java.util.Vector"/>


<html>
<head>
  <title>[S.I.E.S.] - Continuazione Reati (Cumulo)</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<SCRIPT LANGUAGE="JavaScript">
// parametri per il floating layer
floatX=10;
floatY=0;
layerwidth=100;
layerheight=130;
halign="center";
valign="top";
delayspeed=1;

</script>
<script language="JavaScript" src="<%=IWebConstants.JS_FLAYER%>"></script>
<SCRIPT LANGUAGE="JavaScript">

var arrReati = new Array();

  function addReato(modalita, nreato, nreatomanuale) 
  {

    var num;
    var ind;
    var opt;
    var nomereato;
      
    if (modalita == "INSERIMENTO") 
    {
      maxProg = maxProg + 1;
      nreato = maxProg;   
    }
  
    if (nreatomanuale != "") 
      nomereato = nreatomanuale
    else
      nomereato = nreato
      
    ind = f.cbreati.options.length;
    f.lista_norme.length = 0;   
    nomereato = "REATO " + nomereato; 
    opt = new Option(nomereato, nreato, false, true);
    f.cbreati[ind] = opt;
    arrReati[ind] = new Array();
  }

// ---------------------------------

  function addNorme() 
  {   
  
    var arrStrNorma;
    var ind;
    var i;
    var r;
    var opt;
    var check = false;
    
    ind = f.lista_norme.length;
  
    r=0;
    for (i=0; i < f.chkReati.length; i++) 
    {
      if (f.chkReati[i].checked) 
      {
        check = true;
        arrStrNorma = f.chkReati[i].value.split("@!");
        opt = new Option(arrStrNorma[1], f.chkReati[i].value);
        f.lista_norme[ind+r] = opt;
        r++;
        f.chkReati[i].disabled = true;
        f.chkReati[i].checked = false;
      }
    }
    
    if (check)  
      allineaArray();
    else
      alert("Nessuna norma dei Reati da organizzare selezionata!");
  }

// ---------------------------------

  function delNorme() 
  {   
  
    var ind;
    ind = f.lista_norme.selectedIndex;    
    if (ind != -1) 
    {
      enableCheck(f.lista_norme.options[ind].value);
      f.lista_norme.options[ind] = null;
      allineaArray();
    }
    else 
    {
      alert("Nessuna norma dei Reati organizzati selezionata!");
    }   
  }

// ---------------------------------

  function enableCheck(str) 
  {
    var i;
    for (i=0; i < f.chkReati.length; i++) 
    {
      if (f.chkReati[i].value == str) 
      {
        f.chkReati[i].disabled = false;
        break;
      }
    } 
    
    return false;
  }

// ---------------------------------

  function loadNorme(i) 
  {
    var arrStrNorma;
    var y;
    var opt;
      
    f.lista_norme.length = 0; 
    for (y=0; y < arrReati[i].length; y++) 
    {
      arrStrNorma = arrReati[i][y].split("@!");
      opt = new Option(arrStrNorma[1], arrReati[i][y]);
      f.lista_norme[y] = opt;
    }
  }

// ---------------------------------

  function isNormaPresenteInLista(idNorma) 
  {
    var arrNormaLista;
    var y;
    for (y=0; y < f.lista_norme.length; y++) 
    {
      arrNormaLista = f.lista_norme.options[y].value.split("@!");
  
      if (arrNormaLista[0] == idNorma) 
        return true;
    }
    
    return false;
  }

// ---------------------------------

  function isNormaPresenteInArray(idNorma) 
  {
    var arrStrNorma;
    var ir;
    var yr;
    for (ir=0; ir < arrReati.length; ir++) 
    {
      for (yr=0; yr < arrReati[ir].length; yr++) 
      {
        arrStrNorma = arrReati[ir][yr].split("@!");
        if (idNorma == arrStrNorma[0]) 
        {
          alert("Norma già presente in " + f.cbreati.options[ir].text);
          return true; 
        }
      }
    } 
  
    return false;
  }

// ---------------------------------

  function allineaArray() 
  {
    var i;
    arrReati[f.cbreati.selectedIndex] = new Array();
    for (i=0; i < f.lista_norme.length; i++) 
    {
      arrReati[f.cbreati.selectedIndex][i] = f.lista_norme.options[i].value;
    }   
  }

// ---------------------------------

  function disabilitaElementi(bool)
  {
      
    f.NR.disabled = bool;
    f.conferma.disabled = bool;
    f.annulla.disabled = bool;
    f.inside.disabled = bool;
    f.out.disabled = bool;
    f.su.disabled = bool;
    f.giu.disabled = bool;
  }

// ---------------------------------

  function creaStringhe() 
  {
    var arrStrNorma;
    var i;
    var y;
    var str;
    disabilitaElementi(true);
    str = "";
    for (i=0; i < arrReati.length; i++) 
    {
      str += f.cbreati.options[i].value + "-";
      for (y=0; y < arrReati[i].length; y++) 
      {
        arrStrNorma = arrReati[i][y].split("@!");
        str += arrStrNorma[0] + "-";
      }
      str = str.substring(0,str.length-1);
      str += "=";
    } 
    
    str = str.substring(0,str.length-1);
    f.strreatinorme.value = str;
    
    f.submit();
  
  }

// ---------------------------------

  function leggiArray() 
  {
    var arrStrNorma;
    var i;
    var y;
    var str;
    for (ir=0; ir < arrReati.length; ir++) 
    {
      alert (ir);
      for (yr=0; yr < arrReati[ir].length; yr++) 
      {
        alert (arrReati[ir][yr]);
      }
    } 
    
    return false;
  }

// ---------------------------------

  function abbandona() 
  {
    if (confirm("Annullare le operazione effettuate?") == true) 
    {
      history.back();
    }   
  }

// -------------------------------------------------------------------
// swapOptions(select_object,option1,option2)
//  Scambia le posizioni di due option in una select
// -------------------------------------------------------------------
  function swapOptions(obj,i,j) 
  {
    var o = obj.options;
    var i_selected = o[i].selected;
    var j_selected = o[j].selected;
    var temp = new Option(o[i].text, o[i].value, o[i].defaultSelected, o[i].selected);
    var temp2= new Option(o[j].text, o[j].value, o[j].defaultSelected, o[j].selected);
    o[i] = temp2;
    o[j] = temp;
    o[i].selected = j_selected;
    o[j].selected = i_selected;
  }

// -------------------------------------------------------------------
// moveOptionUp(select_object)
// Muove l'option selezionato su di una posizione
// -------------------------------------------------------------------
  function moveOptionUp(obj) 
  {
    var i;
    var check = false;
    
    if (!hasOptions(obj)) {
      alert("Nessuna norma nella lista dei Reati organizzati!"); 
      return; 
    }
    for (i=0; i<obj.options.length; i++) 
    {
      if (obj.options[i].selected) 
      {
        check = true;
        
        if (i != 0 && !obj.options[i-1].selected) 
        {
          swapOptions(obj,i,i-1);
          obj.options[i-1].selected = true;
        }
      }
    }
  
    if (check)  
      allineaArray();
    else
      alert("Nessuna norma dei Reati organizzati selezionata!");
  }

// -------------------------------------------------------------------
// moveOptionDown(select_object)
// Muove l'option selezionato giù di una posizione
// -------------------------------------------------------------------
  function moveOptionDown(obj) 
  {
    var i;
    var check = false;
    
    if (!hasOptions(obj)) { 
      alert("Nessuna norma nella lista dei Reati organizzati!");
      return; 
    }
  
    for (i=obj.options.length-1; i>=0; i--) {
  
      if (obj.options[i].selected) {
        
        check = true;
        if (i != (obj.options.length-1) && ! obj.options[i+1].selected) {
  
          swapOptions(obj,i,i+1);
          obj.options[i+1].selected = true;
        }
      }
    }
  
    if (check)  
      allineaArray();
    else
      alert("Nessuna norma dei Reati organizzati selezionata!");
  }

// -------------------------------------------------------------------
// hasOptions(obj)
// Funzione di utilità per determinare se un oggetto select ha un array options
// -------------------------------------------------------------------
  function hasOptions(obj) 
  {
    if (obj!=null && obj.options!=null) { return true; }
    return false;
  } 

//==========================================================================
// Ritorna alla Griglia dei dati analitici
//==========================================================================
function tornaIndietro(action)
{
  document.OrganizzaReatiCumulo.<%=IWebConstants.ACTION_FIELD%>.value = action;
  document.OrganizzaReatiCumulo.submit();
}

    
</script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
        <font class="campo">Organizzazione Reati Relativi al Titolo Cumulato</font>
      </td>
      <td class="LBG">
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActRicercaReatoCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>
     
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="OrganizzaReatiCumulo">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActOrganizzaReatiCumulo">

    <!-- Campi sempre presenti sulle form dei dati analitici -->
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
    <input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">

    <input type="hidden" name="strreatinorme" value="" >
    
    <table width="40%"> 
      <tr><td class="TitoloNoCap" colspan=2><b>Reati da organizzare</b></td>
<%
      Iterator itx = reaticum.iterator();
      int contNorme = 0;
      int contReato = 0;    
      String strNorma = new String();
      String strReato = new String();
      String lProgressivo = new String();
      while ( itx.hasNext()) 
      {
        ReatoCumuloModel lReato = (ReatoCumuloModel)itx.next();
        if (lReato.getProgrCircostanza().intValue() == 1) 
        { 
          lProgressivo = (lReato.getProgrNumeroManuale() != null) ? lReato.getProgrNumeroManuale().toString() : lReato.getProgrReato().toString();
   %>
            <tr height="10"><td> </td></tr>
            <tr><td class="L" colspan="2" align="left">REATO <%=lProgressivo%> - 
    <%
              strReato = (String)stringareatocum.get(contReato);
    %>
              <%=StringUtils.cStrForJS(strReato)%>            
            </td></tr>
          <%
              contReato++;
            }
          
            strNorma = (String)stringanorma.get(contNorme);
            %>
              <tr>
                <td width="10%"><input type="checkbox" name="chkReati" value="<%=lReato.getIdReatoCum().toString()%>@!<%=strNorma%>" DISABLED></td>
                <td align="left">
                <%=StringUtils.cStrForJS(strNorma)%>
                </td>
            </tr>
     <%
            contNorme++;
          }
        %>
    </table>
  <script>
  if (NS4) {document.write('<LAYER NAME="floatlayer" LEFT="'+floatX+'" TOP="'+floatY+'">');}
  if ((IE4) || (NS6)) {document.write('<div id="floatlayer" style="position:absolute; left:'+floatX+'; top:'+floatY+';">');}
  </script> 

  <table width="40%">
    <tr>
      <td colspan="3" align="center"  class="TitoloNoCap"><b>Reati organizzati</b></td>
    </tr>
    
    <tr>
      <td>
        <table>
          <tr>
                <td><input type="button" value="---->" name="inside" onClick="addNorme()"></td>
          </tr>
          
          <tr><td> </td></tr>

          <tr>
                <td><input type="button" value="<----" name="out" onClick="delNorme()"></td>
          </tr>

          <tr height=130>
                <td>&nbsp;</td>
          </tr>

        </table>
      </td>

      <td>
        <table>
          <tr>
              <td><select name="cbreati" onChange="loadNorme(this.selectedIndex);" style="width: 250"></select></td>
          </tr>
      
          <tr>        
                <td><select name="lista_norme" size=8 style="width: 250"></select></td>
          
          <td VALIGN="TOP">
         <table>
           <tr>         
               <td><input type="button" name="su" value="Sposta su" style="width: 90" onClick="moveOptionUp(f.lista_norme)"></td>
          </tr>
          <tr>
               <td><input type="button" name="giu" value="Sposta giù" style="width: 90" onClick="moveOptionDown(f.lista_norme)"></td>
          </tr>
        </table>
        </td>       
    </tr>

    <tr>
          <td align="center"><input type="button" name="NR" value="Nuovo Reato" onClick="addReato('INSERIMENTO', '', '');"></td>
    </tr>

    <tr height=50 valign=bottom>
      <td align="center"><input type="button" name="conferma" value="Conferma" style="width: 90" onClick="creaStringhe();">
      &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="annulla" value="Annulla" style="width: 90" onClick="abbandona();"></td>

    </tr>
  </table>
  
  
<script>
  f = document.OrganizzaReatiCumulo;
  if (NS4) 
    document.write('</LAYER>');

  if ((IE4) || (NS6)) 
    document.write('</DIV>');

  ifloatX=floatX;
  ifloatY=floatY;
  define();
  window.onresize=define;
  lastX=-1;
  lastY=-1;
  adjust();
</script>

<script language="Javascript">
  <%
      ReatoCumuloModel lReato2 = new ReatoCumuloModel();
      String lProgressivo2 = new String();
      String strValue = new String();
      Iterator itx2 = reaticum.iterator();
      int ind1 = 0;
      int ind2 = 0;
      int contNorme2 = 0;
      while (itx2.hasNext()) 
      {
        lReato2 = (ReatoCumuloModel)itx2.next();
        if (lReato2.getProgrCircostanza().intValue() == 1) 
        { 
          lProgressivo2 = (lReato2.getProgrNumeroManuale() != null) ? lReato2.getProgrNumeroManuale().toString() : lReato2.getProgrReato().toString();
  %>
          addReato("PRECARICAMENTO", "<%=lReato2.getProgrReato().toString()%>", "<%=lProgressivo2%>");
          arrReati[<%=ind1%>] = new Array();
  <%          
          ind1++;
          ind2 = 0;
        }
        
        strNorma = (String)stringanorma.get(contNorme2);
        strValue = lReato2.getIdReatoCum().toString() + "@!" + strNorma;
    %>
        arrReati[<%=ind1-1%>][<%=ind2%>] = "<%=strValue%>";
    <%
        ind2++;       
        contNorme2++;
      }
    %>
    
    maxProg = <%=lReato2.getProgrReato().intValue()%>;
    
    f.cbreati.selectedIndex = 0;    
    loadNorme(0);
        
</script>

 </body>
</html>