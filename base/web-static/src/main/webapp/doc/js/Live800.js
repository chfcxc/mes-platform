var  operator1 = new Array("o53833","上海市","南京市","无锡市","徐州市","常州市","苏州市","南通市","连云港市","淮安市","盐城市","扬州市","镇江市","泰州市","宿迁市","合肥市","芜湖市","蚌埠市","淮南市","马鞍山市","淮北市","铜陵市","安庆市","黄山市","阜阳市","宿州市","滁州市","六安市","宣城市","池州市","亳州市","武汉市","黄石市","十堰市","宜昌市","襄阳市","鄂州市","荆门市","孝感市","荆州市","黄冈市","咸宁市","随州市","恩施土家族苗族自治州","仙桃市","潜江市","天门市","神农架林区");
var  operator2 = new Array("o50409","广州市","珠海市","汕头市","佛山市","韶关市","湛江市","肇庆市","江门市","茂名市","惠州市","梅州市","汕尾市","河源市","阳江市","清远市","东莞市","中山市","潮州市","揭阳市","云浮市","厦门市","福州市","泉州市","莆田市","漳州市","宁德市","南平市","三明市","龙岩市","南宁市","柳州市","桂林市","梧州市","北海市","防城港市","钦州市","贵港市","玉林市","百色市","贺州市","河池市","来宾市","崇左市","长沙市","株洲市","湘潭市","衡阳市","邵阳市","岳阳市","常德市","张家界市","益阳市","娄底市","郴州市","永州市","怀化市");
var  operator3 = new Array("o50411","重庆市","成都市","绵阳市","自贡市","攀枝花市","泸州市","德阳市","广元市","遂宁市","内江市","乐山市","资阳市","宜宾市","南充市","达州市","雅安市","广安市","巴中市","眉山市","阿坝藏族羌族自治州","甘孜藏族自治州","凉山彝族自治州","西宁市","海东市","海北藏族自治州","黄南藏族自治州","海南藏族自治州","果洛藏族自治州","玉树藏族自治州","海西蒙古族藏族自治","贵阳市","遵义市","六盘水市","安顺市","铜仁市","毕节市","黔西南布依族苗族自","黔东南苗族侗族自治","黔南布依族苗族自治","西安市","宝鸡市","咸阳市","渭南市","铜川市","延安市","榆林市","安康市","汉中市","商洛市","杨凌示范区","昆明市","曲靖市","玉溪市","昭通市","保山市","丽江市","普洱市","临沧市","德宏傣族景颇族自治","怒江傈僳族自治州","迪庆藏族自治州","大理白族自治州","楚雄彝族自治州","红河哈尼族彝族自治","文山壮族苗族自治州","西双版纳傣族自治州","拉萨市","昌都市","日喀则市","林芝市","山南市","那曲地区","阿里地区","乌鲁木齐市","克拉玛依市","吐鲁番市","哈密市","阿克苏地区","喀什地区","和田地区","昌吉回族自治州","博尔塔拉蒙古自治州","巴音郭楞蒙古自治州","克孜勒苏柯尔克孜自","伊犁哈萨克自治州","石河子市","阿拉尔市","图木舒克市","五家渠市","北屯市","铁门关市","双河市","可克达拉市","昆玉市","兰州市","嘉峪关市","金昌市","白银市","天水市","酒泉市","张掖市","武威市","定西市","陇南市","平凉市","庆阳市","临夏回族自治州","甘南藏族自治州");
var  operator4 = new Array("o50827","深圳市");
var  operator5 = new Array("o51509","杭州市","宁波市");
var  operator6 = new Array("o51510","温州市");
var  operator7 = new Array("o51511","绍兴市");
var  operator8 = new Array("o51512","湖州市");
var  operator9 = new Array("o51513","嘉兴市");
var  operator10 = new Array("o51514","金华市");
var  operator11 = new Array("o51515","衢州市");
var  operator12 = new Array("o51516","舟山市");
var  operator13 = new Array("o51517","台州市");
var  operator14 = new Array("o51518","丽水市");
var  operator15 = new Array("o52734","北京市","石家庄市","唐山市","秦皇岛市","邯郸市","邢台市","保定市","张家口市","承德市","沧州市","廊坊市","衡水市","定州市","辛集市");
var operators = new Array(operator1,operator2,operator3,operator4,operator5,operator6,operator7,operator8,operator9,operator10,operator11,operator12,operator13,operator14,operator15);

            var chatUrl = "http://chat.live800.com/live800/chatClient/chatbox.jsp?companyID=92969&configID=122193&jid=4269973685";
            createChatUrl(operators,chatUrl);
          
            var assembledChatUrl = null;


            // $("#Live800").click(function () {
            //     //debugger;
				
            //     //createChatUrl(operators, chatUrl);
            //     //openchat()

            // })

            function openchat() {
                if (assembledChatUrl != null)
                    try {
                        window.open(assembledChatUrl, "live800Chat", "width=590,height=470,left=300,top=150,menubar=0,toolbar=0,statusbar=0");
                    } catch (e) {
                        e.message;
                    }
                else{
                    alert("客服未就绪，请稍后……");
 
					
				}
            }
            function createChatUrl(serArray, Url) {
                var ls = document.createElement('script');
                ls.type = 'text/javascript';
                ls.src = "http://chat.live800.com/live800/scripts/ipInfo.jsp";
                var s = document.getElementsByTagName('script')[0];
                s.parentNode.insertBefore(ls, s);
                ls.onload = ls.onreadystatechange = function () {
                    if (!this.readyState || this.readyState == 'loaded' || this.readyState == 'complete' || state == "interactive") {
                        assembledChatUrl = assembleUrl(serArray, Url);
						
						// openchat();
                    }
                }
            }


            function assembleUrl(serArray, chatUrl) {
                var cUrl = chatUrl;
                var serivcesId = checkService(serArray);
                var isSkill;
                if (!serivcesId) {
                    serivcesId = "k5582";  //这里设置的是没有匹配到city，province，country进入的分组
                }

                isSkill = /^k/.test(serivcesId);
                serivcesId = serivcesId.substring(1);
                if (isSkill == true) {
                    cUrl += "&skillId=";
                    cUrl += serivcesId;
                } else {
                    cUrl += "&operatorId=";
                    cUrl += serivcesId;
                }
                

                cUrl += "&enterurl="; //客服端显示的“最近访客页面”,即发起对话的页面。
                cUrl += encodeURIComponent(document.URL || window.location);
                if (document.referrer != (document.URL || window.location) && document.referrer != "")
                    cUrl += "&pagereferrer=" + encodeURIComponent(document.referrer);//pagereferrer访客来源。如果不是外站访客第一着陆页面，该信息将无法获取则不进行提交。
                cUrl += "&tm=" + new Date().getTime();
                return cUrl;
            }

            function checkService(servArray) {//检测访客地区所属的客服或分组，先过滤市再是省最后是国家，并返回的客服或分组的id（operatorId skillId）。
                var serivcesId = null;
                // var scope = 0;
                for (s = 0; s < 3; s++) {
                    for (i = 0; servArray.length > i; i++) {
                        var opr = servArray[i];
                        var oprId = null;
                        for (j = 0; opr.length > j; j++) {
                            if (j == 0) {
                                oprId = opr[j];
                                continue;
                            }
                            if (opr[j] == ipInfo[0].city) {
                                return oprId;
                            } else if (opr[j] == ipInfo[0].province) {
                                return oprId;
                            } else if (opr[j] == ipInfo[0].country) {
                                return oprId;
                            }
                        }
                    }
                }
            }