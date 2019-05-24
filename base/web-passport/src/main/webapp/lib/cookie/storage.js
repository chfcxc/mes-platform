function setItems(pageSign){
	var arr=new Array();
	var searchItemsList = window['emTableConfig'].searchConfig.searchItems;//所有的查询条件集合
	var startVal=$("#outerId_page_div .emtable-page-right").find("b").html();
	var limitVal=$(".limitSelect").val();
	var pageObj={};
	pageObj.start=startVal;
	pageObj.limit=limitVal;
	for(var i=0;i<searchItemsList.length;i++){
		var queryStr = "";
		var id=searchItemsList[i]['id'];
		var type=searchItemsList[i]['type'];
		var value=$("#"+id).val();
		queryStr += id +"|" + type+"|"+value;
		arr.push(queryStr);
	}
	var saveSearchStr = arr.join("$");
	//存储，IE6~7 cookie 其他浏览器HTML5本地存储
	if (window.sessionStorage ) {
	    sessionStorage .setItem(pageSign+"info",saveSearchStr);	
	    sessionStorage .setItem(pageSign+"page",JSON.stringify(pageObj));	
	} else {
	    Cookie.write(pageSign+"info", saveSearchStr);	
	}
}
function getItems(pageSign){
	if(sessionStorage .getItem(pageSign+'info')!=void 0){
		var arrs = window.sessionStorage ? sessionStorage .getItem(pageSign+"info"): Cookie.read(pageSign+"info");	//IE6~7 cookie存储
		var queryStr = arrs.split("$");
		for(var i = 0; i < queryStr.length;i ++){
			var queryItem  = queryStr[i];
			var atts = queryItem.split("|");
			var id = atts[0];
			var type = atts[1];
			var value = atts[2];
			$("#"+id).val(value);
		}
	}
	if(sessionStorage .getItem(pageSign+'page')!=void 0){
		var pagearr = window.sessionStorage ? JSON.parse(sessionStorage .getItem(pageSign+"page")): Cookie.read(pageSign+"page");	//IE6~7 cookie存储
		var startVal=pagearr.start;
		var limitVal= pagearr.limit;
		tableItemLayout("emTableConfig" ,(startVal-1)*limitVal ,limitVal);
	}else{
		tableItemLayout("emTableConfig" ,0,20);
	}
}
