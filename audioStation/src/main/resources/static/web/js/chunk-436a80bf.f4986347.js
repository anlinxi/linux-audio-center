(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-436a80bf"],{"389a":function(t,s,i){"use strict";i.r(s);var a=function(){var t=this,s=t._self._c;return s("div",{staticClass:"rank"},[s("div",{staticClass:"w1200"},[s("div",{staticClass:"rank-container"},[s("div",{staticClass:"rank-aside"},[s("div",{staticClass:"rank-type"},[s("div",{staticClass:"type-hd"},[s("span",{class:"Top"===t.type?"active":"",on:{click:function(s){return t.selectType("Top")}}},[t._v("TOP榜")]),s("span",{class:"Feature"===t.type?"active":"",on:{click:function(s){return t.selectType("Feature")}}},[t._v("特色榜")]),s("span",{class:"Other"===t.type?"active":"",on:{click:function(s){return t.selectType("Other")}}},[t._v("场景榜")])]),s("div",{staticClass:"type-main"},t._l(t.list,(function(i,a){return s("div",{key:a,staticClass:"type-item",class:t.rId==i.id?"active":"",on:{click:function(s){return t.selectItem(i)}}},[s("el-image",{staticClass:"item-img",attrs:{src:i.coverImgUrl}},[s("div",{staticClass:"image-slot",attrs:{slot:"placeholder"},slot:"placeholder"},[s("i",{staticClass:"iconfont icon-placeholder"})])]),s("div",{staticClass:"item-info"},[s("div",{staticClass:"item-title"},[t._v(" "+t._s(i.name)+" ")]),s("div",{staticClass:"item-time"},[t._v(" "+t._s(i.updateFrequency)+" ")])])],1)})),0)])]),s("div",{staticClass:"rank-main"},[s("div",{staticClass:"rank-list-hd"},[s("el-image",{staticClass:"rank-img",attrs:{src:t.rankInfo.coverImgUrl}},[s("div",{staticClass:"image-slot",attrs:{slot:"placeholder"},slot:"placeholder"},[s("i",{staticClass:"iconfont icon-placeholder"})])]),t.rankInfo?s("div",{staticClass:"rank-info"},[s("div",{staticClass:"rank-title"},[t._v(" "+t._s(t.rankInfo.name)+" "),s("span",[t._v("("+t._s(t.$utils.formartDate(t.rankInfo.updateTime,"MM月dd日"))+" 更新)")])]),t.rankInfo.tags?s("div",{staticClass:"rank-tags"},t._l(t.rankInfo.tags,(function(i,a){return s("router-link",{key:a,staticClass:"tag",attrs:{to:{path:"/playlist",query:{cat:i}}}},[t._v("#"+t._s(i))])})),1):t._e(),s("div",{staticClass:"rank-digital"},[s("span",{staticClass:"rank-playCount"},[s("i",{staticClass:"iconfont icon-playnum"}),t._v(" "+t._s(t.$utils.formartNum(t.rankInfo.playCount))+"次")]),s("span",{staticClass:"rank-collect"},[s("i",{staticClass:"iconfont icon-collect"}),t._v(" "+t._s(t.$utils.formartNum(t.rankInfo.subscribedCount)))]),s("span",{staticClass:"rank-comment"},[s("i",{staticClass:"iconfont icon-comment"}),t._v(" "+t._s(t.$utils.formartNum(t.rankInfo.commentCount)))])]),s("div",{staticClass:"rank-desc"},[t._v(" "+t._s(t.rankInfo.description)+" ")])]):t._e()],1),s("div",{staticClass:"song-header"},[s("h4",[t._v("歌曲列表 "),s("em",[t._v(t._s(t.total+"首歌"))])]),s("span",{staticClass:"play-all",on:{click:t.playAllSongs}},[s("i",{staticClass:"iconfont icon-audio-play"}),t._v(" 播放全部")])]),t.isLoading?[s("Loading")]:[s("song-list",{attrs:{songList:t.songList,stripe:!0}})]],2)])])])},e=[],n=(i("a717"),i("7736")),l=i("eb4d"),r=i("18fd"),o=i("bf93"),c={name:"Rank",components:{songList:r["a"],Loading:o["a"]},data(){return{list:[],type:"Top",listTop:[],listFeature:[],listOther:[],rId:0,rankInfo:{},songList:[],isLoading:!0,total:0}},computed:{},created(){this.rId=this.$route.query.rId,this.type=this.$route.query.type?this.$route.query.type:this.type},mounted(){this.getTopListDetail()},methods:{...Object(n["d"])({setPlayStatus:"SET_PLAYSTATUS",setPlayList:"SET_PLAYLIST",setPlayIndex:"SET_PLAYINDEX"}),async getTopListDetail(){const{data:t}=await this.$http.topListDetail();if(200!==t.code)return this.$msg.error("数据请求失败");this.listTop=t.list.filter(t=>t.ToplistType),this.listFeature=t.list.filter(t=>!t.ToplistType&&t.name.indexOf("云音乐")>=0),this.listOther=t.list.filter(t=>!t.ToplistType&&t.name.indexOf("云音乐")<0),this.list=this.type?this["list"+this.type]:this.listTop,this.rId=this.rId?this.rId:this.listTop[0].id},async getListDetail(){this.isLoading=!0;const{data:t}=await this.$http.listDetail({id:this.rId,s:-1});if(200!==t.code)return this.$msg.error("数据请求失败");this.rankInfo=t.playlist,this.songList=this._formatSongs(t.playlist.tracks,t.privileges),this.total=this.songList.length,this.isLoading=!1},selectType(t){this.type=t,this.list=this["list"+this.type],this.rId=this["list"+t][0].id,this.$router.push({path:"rank",query:{type:this.type,rId:this.rId}})},selectItem(t){this.rId=t.id,this.$router.push({path:"rank",query:{type:this.type,rId:this.rId}})},playAllSongs(){this.playAll({list:this.songList})},_formatSongs(t,s){const i=[];return t.map((t,a)=>{t.id&&(t.license=!s[a].cp,i.push(Object(l["b"])(t)))}),i},...Object(n["b"])(["playAll"])},watch:{rId(t,s){this.rId=t,this.rId&&this.getListDetail()}}},d=c,p=(i("72db"),i("e607")),h=Object(p["a"])(d,a,e,!1,null,"11c78862",null);s["default"]=h.exports},"72db":function(t,s,i){"use strict";i("9b2e")},"9b2e":function(t,s,i){}}]);
//# sourceMappingURL=chunk-436a80bf.f4986347.js.map